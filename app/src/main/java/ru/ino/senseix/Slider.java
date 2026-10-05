package ru.ino.senseix;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;

public class Slider extends SeekBar {
	
	/* ===== SIZE BASE (dp) ===== */
	private static final float TRACK_HEIGHT_DP = 14f;
	private static final float THUMB_HEIGHT_DP = 41f;
	private static final float THUMB_WIDTH_DP  = 3f;
	private static final float GAP_DP          = 5f;
	
	/* ===== COLOR ===== */
	private int trackActiveColor   = 0xFF0D47A1;
	private int trackInactiveColor = 0x4D0D47A1;
	private int thumbColor         = 0xFF0D47A1;
	
	/* ===== DIMENSION ===== */
	private float density;
	private float trackHeight;
	private float thumbHeight;
	private float thumbWidth;
	private float thumbWidthAnim;
	private float gapX;
	private float outerRadius;
	private float innerRadius;
	
	/* ===== CONFIGURATION BOOLEAN ===== */
	// true = Saat besar menjadi Box Rounded (kotak melengkung dikit)
	// false = Saat besar menjadi Normal Rounded (Kapsul melingkar penuh)
	private boolean useBoxRoundedWhenExpanded = false;
	
	/* ===== PAINT ===== */
	private Paint trackPaint;
	private Paint thumbPaint;
	
	/* ===== ANIMATION ===== */
	private ValueAnimator widthAnimator;
	
	/* ===== LISTENER CUSTOM ===== */
	private SliderListener sliderListener;

	/* ===== REUSABLE OBJECTS (Anti GC Lag) ===== */
	private final RectF thumbRect = new RectF();
	private final RectF trackRect = new RectF();
	
	/* ===== CONSTRUCTOR ===== */
	
	public Slider(Context context) {
		super(context);
		init();
	}
	
	public Slider(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	/* ===== INIT ===== */
	
	private void init() {
		
		density = getResources().getDisplayMetrics().density;
		
		trackHeight = dp(TRACK_HEIGHT_DP);
		thumbHeight = dp(THUMB_HEIGHT_DP);
		thumbWidth  = dp(THUMB_WIDTH_DP);
		gapX        = dp(GAP_DP);
		
		outerRadius = trackHeight / 2f;
		innerRadius = dp(2f);
		
		thumbWidthAnim = thumbWidth;
		
		trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		trackPaint.setStyle(Paint.Style.FILL);
		
		thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		thumbPaint.setStyle(Paint.Style.FILL);
		thumbPaint.setColor(thumbColor);
		
		setBackground(null);
		setThumb(null);
		setSplitTrack(false);
		
		int px = (int) dp(8);
		int py = (int) dp(6);
		setPadding(px, py, px, py);
	}
	
	private float dp(float v) {
		return v * density;
	}
	
	/* ================================= */
	/* ===== BOOLEAN CONFIGURATION ===== */
	/* ================================= */
	
	public void setUseBoxRoundedWhenExpanded(boolean enabled) {
		this.useBoxRoundedWhenExpanded = enabled;
		requestLayout();
		invalidate();
	}
	
	public boolean isUseBoxRoundedWhenExpanded() {
		return this.useBoxRoundedWhenExpanded;
	}
	
	/* ========================= */
	/* ===== LISTENER API ====== */
	/* ========================= */
	
	public interface SliderListener {
		void onValueChanged(Slider slider, int value, boolean fromUser);
		void onStartTracking(Slider slider);
		void onStopTracking(Slider slider);
	}
	
	public void setOnSliderChangeListener(SliderListener listener) {
		this.sliderListener = listener;
	}
	
	/* ========================= */
	/* ===== TOUCH SYSTEM ====== */
	/* ========================= */
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) return false;
		
		float x = event.getX();
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				getParent().requestDisallowInterceptTouchEvent(true);
				animateThumb(thumbWidthAnim, dp(2));
				if (sliderListener != null) sliderListener.onStartTracking(this);
				updateProgress(x, true);
				return true;
				
			case MotionEvent.ACTION_MOVE:
				updateProgress(x, true);
				return true;
				
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				animateThumb(thumbWidthAnim, thumbWidth);
				if (sliderListener != null) sliderListener.onStopTracking(this);
				getParent().requestDisallowInterceptTouchEvent(false);
				return true;
		}
		return false;
	}
	
	private void updateProgress(float touchX, boolean fromUser) {
		float left  = getPaddingLeft();
		float right = getWidth() - getPaddingRight();
		
		float clamped = Math.max(left, Math.min(touchX, right));
		float percent = (clamped - left) / (right - left);
		
		int value = Math.round(percent * getMax());
		setProgress(value);
		
		if (sliderListener != null) {
			sliderListener.onValueChanged(this, value, fromUser);
		}
		invalidate();
	}
	
	/* ========================= */
	/* ===== THUMB ANIMATION === */
	/* ========================= */
	
	private void animateThumb(float from, float to) {
		if (widthAnimator != null) widthAnimator.cancel();
		
		widthAnimator = ValueAnimator.ofFloat(from, to);
		widthAnimator.setDuration(180);
		widthAnimator.setInterpolator(new DecelerateInterpolator());
		
		widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				thumbWidthAnim = (float) animation.getAnimatedValue();
				invalidate();
			}
		});
		widthAnimator.start();
	}
	
	/* ========================= */
	/* ===== MEASURE =========== */
	/* ========================= */
	
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int size = MeasureSpec.getSize(heightMeasureSpec);
		
		int finalHeight;
		
		/* WRAP_CONTENT */
		if (mode != MeasureSpec.EXACTLY) {
			thumbHeight = dp(THUMB_HEIGHT_DP);
			thumbWidth  = dp(THUMB_WIDTH_DP);
			gapX        = dp(GAP_DP);
			trackHeight = dp(TRACK_HEIGHT_DP);
			
			if (useBoxRoundedWhenExpanded) {
				outerRadius = dp(5f); 
			} else {
				outerRadius = trackHeight / 2f; 
			}
			innerRadius = dp(2f);
			
			finalHeight = (int) thumbHeight + getPaddingTop() + getPaddingBottom();
			
		} else {
			/* HEIGHT XML (Mengikuti tinggi kustom dari XML layout, misal 100dp) */
			finalHeight = size;
			
			float usableHeight = finalHeight - getPaddingTop() - getPaddingBottom();
			
			/* UKURAN DINAMIS MENGIKUTI XML */
			trackHeight = usableHeight;
			thumbHeight = usableHeight; // BARU: Tinggi thumb sekarang mengunci tinggi XML penuh
			
			thumbWidth  = dp(3f);
			gapX        = dp(5f);
			
			// Logika Boolean Box Rounded vs Capsule
			if (useBoxRoundedWhenExpanded) {
				outerRadius = dp(10f); // Kotak bersudut tumpul tipis (Box Rounded)
			} else {
				outerRadius = trackHeight / 2f; // Bulat kapsul sempurna
			}
			
			innerRadius = dp(2f);
		}
		
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), finalHeight);
	}
	
	/* ========================= */
	/* ===== DRAW ============== */
	/* ========================= */
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		float centerY = getHeight() / 2f;
		
		float top     = centerY - trackHeight / 2f;
		float bottom  = centerY + trackHeight / 2f;
		
		float left  = getPaddingLeft();
		float right = getWidth() - getPaddingRight();
		
		float ratio = (float) getProgress() / getMax();
		float thumbX = left + ratio * (right - left);
		
		/* ACTIVE TRACK */
		trackPaint.setColor(trackActiveColor);
		float activeEnd = thumbX - gapX;
		if (activeEnd > left) {
			drawRect(canvas, left, top, activeEnd, bottom, outerRadius, innerRadius, trackPaint);
		}
		
		/* INACTIVE TRACK */
		trackPaint.setColor(trackInactiveColor);
		float inactiveStart = thumbX + gapX;
		if (inactiveStart < right) {
			drawRect(canvas, inactiveStart, top, right, bottom, innerRadius, outerRadius, trackPaint);
		}
		
		/* THUMB (Tinggi otomatis presisi mengikuti perubahan thumbHeight) */
		thumbRect.set(
			thumbX - thumbWidthAnim / 2f,
			centerY - thumbHeight / 2f,
			thumbX + thumbWidthAnim / 2f,
			centerY + thumbHeight / 2f
		);
		
		canvas.drawRoundRect(
			thumbRect,
			thumbWidthAnim / 2f,
			thumbWidthAnim / 2f,
			thumbPaint
		);
	}
	
	private void drawRect(Canvas c, float l, float t, float r, float b, float rl, float rr, Paint p) {
		Path path = new Path();
		trackRect.set(l, t, r, b);
		
		path.addRoundRect(
			trackRect,
			new float[]{ rl, rl, rr, rr, rr, rr, rl, rl },
			Path.Direction.CW
		);
		
		c.drawPath(path, p);
	}
}