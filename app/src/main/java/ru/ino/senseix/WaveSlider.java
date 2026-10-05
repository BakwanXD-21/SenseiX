package ru.ino.senseix;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

public class WaveSlider extends SeekBar {
	
	private float density;
	
	/* SIZE */
	private float trackHeight;
	private float thumbHeight;
	
	private float thumbWidthNormal;
	private float thumbWidthDrag;
	private float thumbWidth;
	
	private float gap;
	
	/* WAVE */
	private float waveAmplitude;
	private float waveLength;
	private float waveOffset;
	
	private boolean isDragging = false;
	
	/* COLOR */
	private int activeColor   = 0xFF0D47A1;
	private int inactiveColor = 0x4D0D47A1;
	private int thumbColor    = 0xFF0D47A1;
	
	/* PAINT */
	private Paint trackPaint;
	private Paint thumbPaint;
	
	/* ANIM */
	private ValueAnimator waveAnimator;
	
	/* LISTENER */
	// Menggunakan OnSeekBarChangeListener bawaan Android secara internal
	private OnSeekBarChangeListener internalListener;
	
	public WaveSlider(Context c) {
		super(c);
		init();
	}
	
	public WaveSlider(Context c, AttributeSet a) {
		super(c, a);
		init();
	}
	
	private void init() {
		
		density = getResources().getDisplayMetrics().density;
		
		trackHeight = dp(6);
		thumbHeight = dp(41);
		
		thumbWidthNormal = dp(3);
		thumbWidthDrag   = dp(2);
		
		thumbWidth = thumbWidthNormal;
		
		gap = dp(8);
		
		waveAmplitude = dp(4.5f);
		waveLength    = dp(26);
		
		trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		trackPaint.setStyle(Paint.Style.STROKE);
		trackPaint.setStrokeWidth(trackHeight);
		trackPaint.setStrokeCap(Paint.Cap.ROUND);
		
		thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		thumbPaint.setColor(thumbColor);
		
		setThumb(null);
		setSplitTrack(false);
		setBackground(null);
		
		int px = (int) dp(8);
		int py = (int) dp(6);
		setPadding(px, py, px, py);
		
		startWave();
	}
	
	private float dp(float v){
		return v * density;
	}
	
	/* Intercept setter agar kita bisa memicu callback internal kita sendiri */
	@Override
	public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
		super.setOnSeekBarChangeListener(l);
		this.internalListener = l;
	}
	
	/* ================= WAVE ================= */
	
	private void startWave() {
		
		if (waveAnimator != null) return;
		
		waveAnimator = ValueAnimator.ofFloat(0f, waveLength);
		
		waveAnimator.setDuration(1300);
		waveAnimator.setRepeatCount(ValueAnimator.INFINITE);
		waveAnimator.setInterpolator(new LinearInterpolator());
		
		waveAnimator.addUpdateListener(a -> {
			waveOffset = (float) a.getAnimatedValue();
			if (!isDragging) invalidate();
		});
		
		waveAnimator.start();
	}
	
	private void stopWave(){
		if (waveAnimator != null){
			waveAnimator.cancel();
			waveAnimator = null;
		}
	}
	
	/* ================= TOUCH ================= */
	
	@Override
	public boolean onTouchEvent(MotionEvent e){
		
		if (!isEnabled()) return false;
		
		float x = e.getX();
		
		float l = getPaddingLeft();
		float r = getWidth() - getPaddingRight();
		
		switch (e.getAction()) {
			
			case MotionEvent.ACTION_DOWN: {
				getParent().requestDisallowInterceptTouchEvent(true);
				
				isDragging = true;
				thumbWidth = thumbWidthDrag;
				stopWave();
				
				updateProgress(x, l, r);
				
				if (internalListener != null) {
					internalListener.onStartTrackingTouch(this);
				}
				
				return true;
			}
			
			case MotionEvent.ACTION_MOVE: {
				updateProgress(x, l, r);
				return true;
			}
			
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL: {
				getParent().requestDisallowInterceptTouchEvent(false);
				
				isDragging = false;
				thumbWidth = thumbWidthNormal;
				startWave();
				
				if (internalListener != null) {
					internalListener.onStopTrackingTouch(this);
				}
				
				invalidate();
				return true;
			}
		}
		
		return false;
	}
	
	private void updateProgress(float x, float l, float r){
		
		float clamped = Math.max(l, Math.min(x, r));
		float percent = (clamped - l) / (r - l);
		
		int newProgress = Math.round(percent * getMax());
		
		setProgress(newProgress);
		
		if (internalListener != null) {
			internalListener.onProgressChanged(this, newProgress, true);
		}
		
		invalidate();
	}
	
	/* ================= MEASURE ================= */
	
	@Override
	protected synchronized void onMeasure(int w, int h){
		int height = (int)(thumbHeight + getPaddingTop() + getPaddingBottom());
		setMeasuredDimension(MeasureSpec.getSize(w), height);
	}
	
	/* ================= DRAW ================= */
	
	@Override
	protected synchronized void onDraw(Canvas c){
		
		float cy = getHeight() / 2f;
		
		float padLeft  = getPaddingLeft();
		float padRight = getWidth() - getPaddingRight();
		
		float ratio = (float) getProgress() / getMax();
		float thumbX = padLeft + ratio * (padRight - padLeft);
		
		float halfStroke = trackHeight / 2f;
		
		float trackLeft  = padLeft + halfStroke;
		float trackRight = padRight - halfStroke;
		
		/* ACTIVE */
		trackPaint.setColor(activeColor);
		float waveEnd = thumbX - gap;
		
		if (waveEnd > trackLeft){
			drawWave(c, trackLeft, waveEnd, cy);
		}
		
		/* INACTIVE */
		trackPaint.setColor(inactiveColor);
		float lineStart = thumbX + gap;
		
		if (lineStart < trackRight){
			drawLine(c, lineStart, trackRight, cy);
		}
		
		/* THUMB */
		float half = thumbWidth / 2f;
		
		c.drawRoundRect(
			thumbX - half,
			cy - thumbHeight / 2f,
			thumbX + half,
			cy + thumbHeight / 2f,
			half,
			half,
			thumbPaint
		);
	}
	
	/* ================= TRACK ================= */
	
	private void drawLine(Canvas c, float start, float end, float y){
		if (end <= start) return;
		c.drawLine(start, y, end, y, trackPaint);
	}
	
	private void drawWave(Canvas c, float start, float end, float centerY){
		if (end <= start) return;
		
		Path p = new Path();
		float step = dp(1.5f);
		float x = start;
		
		while (x <= end){
			float y = isDragging
				? centerY
				: (float)(centerY + Math.sin((x + waveOffset) / waveLength * Math.PI * 2) * waveAmplitude);
			
			if (x == start){
				p.moveTo(x, y);
			} else {
				p.lineTo(x, y);
			}
			x += step;
		}
		
		float yEnd = isDragging
			? centerY
			: (float)(centerY + Math.sin((end + waveOffset) / waveLength * Math.PI * 2) * waveAmplitude);
			
		p.lineTo(end, yEnd);
		c.drawPath(p, trackPaint);
	}
	
	/* ================= COLOR ================= */
	
	public void setTrackActiveColor(int c){
		activeColor = c;
		invalidate();
	}
	
	public void setTrackInactiveColor(int c){
		inactiveColor = c;
		invalidate();
	}
	
	public void setThumbColor(int c){
		thumbColor = c;
		thumbPaint.setColor(c);
		invalidate();
	}
}
