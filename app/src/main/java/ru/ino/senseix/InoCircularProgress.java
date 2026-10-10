package ru.ino.senseix;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class InoCircularProgress extends View {
	
	// ukuran tetap
	private final int DEFAULT_SIZE;
	private final float STROKE_WIDTH;
	private final float GAP_DEGREE = 20f;
	
	private Paint activePaint;
	private Paint inactivePaint;
	private RectF rectF = new RectF();
	
	// warna
	private int activeColor = 0xFF0D47A1;
	private int inactiveColor = 0x4D0D47A1;
	
	// determinate
	private int max = 100;
	private int progress = 0;
	
	// mode
	private boolean indeterminate = false;
	
	// animasi
	private ValueAnimator animator;
	private float rotationAngle = 0f;
	private float sweepAngle = 0f;
	
	public InoCircularProgress(Context c) {
		this(c, null);
	}
	
	public InoCircularProgress(Context c, AttributeSet a) {
		super(c, a);
		float d = c.getResources().getDisplayMetrics().density;
		STROKE_WIDTH = 4f * d;
		DEFAULT_SIZE = (int) (50f * d);
		init();
	}
	
	private void init() {
		activePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		activePaint.setStyle(Paint.Style.STROKE);
		activePaint.setStrokeWidth(STROKE_WIDTH);
		activePaint.setStrokeCap(Paint.Cap.ROUND);
		activePaint.setColor(activeColor);
		
		inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		inactivePaint.setStyle(Paint.Style.STROKE);
		inactivePaint.setStrokeWidth(STROKE_WIDTH);
		inactivePaint.setStrokeCap(Paint.Cap.ROUND);
		inactivePaint.setColor(inactiveColor);
		
		startAnimator();
	}
	
	/* ================= MODE ================= */
	
	public void setIndeterminate(boolean value) {
		if (indeterminate == value) return;
		indeterminate = value;
		
		if (value) {
			startAnimator();
		} else {
			stopAnimator();
			invalidate();
		}
	}
	
	public boolean isIndeterminate() {
		return indeterminate;
	}
	
	/* ================= PROGRESS ================= */
	
	public void setMax(int m) {
		max = Math.max(1, m);
		if (progress > max) progress = max;
		invalidate();
	}
	
	public void setProgress(int p) {
		progress = Math.max(0, Math.min(p, max));
		if (!indeterminate) invalidate();
	}
	
	/* ================= COLOR ================= */
	
	public void setTrackActiveColor(int c) {
		activeColor = c;
		activePaint.setColor(c);
		invalidate();
	}
	
	public void setTrackInactiveColor(int c) {
		inactiveColor = c;
		inactivePaint.setColor(c);
		invalidate();
	}
	
	/* ================= ANIMATION ================= */
	
	private void startAnimator() {
		stopAnimator();
		animator = ValueAnimator.ofFloat(0f, 1f);
		animator.setDuration(1500);
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.setInterpolator(new LinearInterpolator());
		animator.addUpdateListener(v -> {
			float t = (float) v.getAnimatedValue();
			rotationAngle = 360f * t;
			sweepAngle = t < 0.5f
			? (t / 0.5f) * 320f
			: (1f - (t - 0.5f) / 0.5f) * 320f;
			invalidate();
		});
		animator.start();
	}
	
	private void stopAnimator() {
		if (animator != null) {
			animator.cancel();
			animator = null;
		}
	}
	
	/* ================= DRAW ================= */
	
	@Override
	protected void onMeasure(int w, int h) {
		
		int widthMode = MeasureSpec.getMode(w);
		int heightMode = MeasureSpec.getMode(h);
		
		int widthSize = MeasureSpec.getSize(w);
		int heightSize = MeasureSpec.getSize(h);
		
		int width;
		int height;
		
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = DEFAULT_SIZE;
		}
		
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = DEFAULT_SIZE;
		}
		
		int size = Math.min(width, height);
		
		setMeasuredDimension(size, size);
	}
	
	@Override
	protected void onDraw(Canvas c) {
		super.onDraw(c);
		
		float w = getWidth();
		float h = getHeight();
		
		float size = Math.min(w, h);
		
		float cx = w / 2f;
		float cy = h / 2f;
		
		float density = getResources().getDisplayMetrics().density;
		
		/* scale berdasarkan ukuran default */
		float scale = size / (48f * density);
		
		/* stroke ikut membesar */
		float dynamicStroke = STROKE_WIDTH * scale;
		
		activePaint.setStrokeWidth(dynamicStroke);
		inactivePaint.setStrokeWidth(dynamicStroke);
		
		float r = (size - dynamicStroke) / 2f;
		
		rectF.set(
		cx - r,
		cy - r,
		cx + r,
		cy + r
		);
		
		float sweep;
		float rotate;
		
		if (indeterminate) {
			
			sweep = Math.max(10f, sweepAngle);
			rotate = rotationAngle;
			
		} else {
			
			sweep = (progress / (float) max) * 360f;
			rotate = -90f;
		}
		
		c.save();
		
		c.rotate(rotate, cx, cy);
		
		float gapStart = sweep + GAP_DEGREE;
		float gapSweep = 360f - sweep - GAP_DEGREE * 2f;
		
		if (gapSweep > 0) {
			c.drawArc(
			rectF,
			gapStart,
			gapSweep,
			false,
			inactivePaint
			);
		}
		
		c.drawArc(
		rectF,
		0,
		sweep,
		false,
		activePaint
		);
		
		c.restore();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		stopAnimator();
		super.onDetachedFromWindow();
	}
}
