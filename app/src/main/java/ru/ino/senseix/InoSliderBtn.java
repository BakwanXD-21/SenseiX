package ru.ino.senseix;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class InoSliderBtn extends View {
	
	public enum ShapeType {
		BOX, OCTAGON, ROUNDED, CUSTOM_RADIUS
	}
	
	public enum Alignment {
		LEFT, TOP, CENTER, RIGHT, BOTTOM
	}
	
	private Paint trackPaint;
	private Paint thumbPaint;
	private Paint textPaint;
	
	private float thumbX = 0;
	private float thumbSize = 0;
	private float customThumbWidth = -1;
	private float thumbPadding = 3f; // Padding thumb ke bg (in DP)
	
	// Radius Custom (in DP)
	private float bgRadiusDp = 12f;
	private float thumbRadiusDp = 8f;
	
	private boolean isSliding = false;
	private boolean isUnlocked = false;
	
	private final Path shapePath = new Path();
	private final RectF rectF = new RectF();
	private final float density;
	
	private ShapeType bgShape = ShapeType.ROUNDED;
	private ShapeType thumbShape = ShapeType.ROUNDED;
	
	// Posisi Tata Letak Icon & Text
	private Alignment iconPosition = Alignment.CENTER;
	private Alignment textPosition = Alignment.CENTER;
	
	private int trackColor = 0xFF181818;
	private int thumbColor = 0xFF00E5E5;
	private int textColor = Color.WHITE;
	private int iconColor = Color.BLACK;
	
	private String sliderText = "GESER UNTUK KONFIRMASI >>";
	private Drawable thumbIcon;
	
	private float iconMarginDp = 6f;
	private float textMarginDp = 0f;
	private float iconSizeDp = -1; // -1 berarti dinamis mengikuti tinggi thumb
	
	private OnSliderUnlockedListener listener;
	private ValueAnimator thumbAnimator;
	
	public interface OnSliderUnlockedListener {
		void onUnlocked();
	}
	
	public void setOnSliderUnlockedListener(OnSliderUnlockedListener listener) {
		this.listener = listener;
	}
	
	public InoSliderBtn(Context context) {
		this(context, null);
	}
	
	public InoSliderBtn(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public InoSliderBtn(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		density = getResources().getDisplayMetrics().density;
		init();
	}
	
	private void init() {
		trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		trackPaint.setStyle(Paint.Style.FILL);
		
		thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		thumbPaint.setStyle(Paint.Style.FILL);
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(14f * density);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setFakeBoldText(true);
		
		updateColors();
	}
	
	private void updateColors() {
		trackPaint.setColor(trackColor);
		thumbPaint.setColor(thumbColor);
		textPaint.setColor(textColor);
		if (thumbIcon != null) {
			thumbIcon.setColorFilter(new PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_IN));
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		thumbSize = (customThumbWidth > 0) ? customThumbWidth : h;
		if (!isSliding && !isUnlocked) {
			thumbX = 0;
		}
	}
	
	private void drawCustomShape(Canvas canvas, float left, float top, float right, float bottom, ShapeType type, float customRadiusDp, Paint paint) {
		shapePath.reset();
		float w = right - left;
		float h = bottom - top;
		
		switch (type) {
			case BOX:
			rectF.set(left, top, right, bottom);
			canvas.drawRect(rectF, paint);
			break;
			
			case ROUNDED:
			float radius = h / 2f;
			rectF.set(left, top, right, bottom);
			canvas.drawRoundRect(rectF, radius, radius, paint);
			break;
			
			case CUSTOM_RADIUS:
			float rPx = customRadiusDp * density;
			rectF.set(left, top, right, bottom);
			canvas.drawRoundRect(rectF, rPx, rPx, paint);
			break;
			
			case OCTAGON:
			float minSize = Math.min(w, h);
			float octCorner = minSize * 0.21f;
			shapePath.moveTo(left + octCorner, top);
			shapePath.lineTo(right - octCorner, top);
			shapePath.lineTo(right, top + octCorner);
			shapePath.lineTo(right, bottom - octCorner);
			shapePath.lineTo(right - octCorner, bottom);
			shapePath.lineTo(left + octCorner, bottom);
			shapePath.lineTo(left, bottom - octCorner);
			shapePath.lineTo(left, top + octCorner);
			shapePath.close();
			canvas.drawPath(shapePath, paint);
			break;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		float w = getWidth();
		float h = getHeight();
		
		// 1. Background / Track
		drawCustomShape(canvas, 0, 0, w, h, bgShape, bgRadiusDp, trackPaint);
		
		// 2. Teks Petunjuk (Kustomisasi Tata Letak Kiri, Atas, Tengah, Kanan, Bawah)
		if (!isUnlocked && sliderText != null) {
			drawCustomText(canvas, w, h);
		}
		
		// 3. Tombol Geser / Thumb
		float padding = thumbPadding * density;
		float thumbLeft = thumbX + padding;
		float thumbTop = padding;
		float thumbRight = thumbX + thumbSize - padding;
		float thumbBottom = h - padding;
		
		drawCustomShape(canvas, thumbLeft, thumbTop, thumbRight, thumbBottom, thumbShape, thumbRadiusDp, thumbPaint);
		
		// 4. Gambar Icon (Kustomisasi Tata Letak TOP, BOTTOM, LEFT, RIGHT, CENTER)
		if (thumbIcon != null) {
			drawCustomIcon(canvas, h, padding, thumbLeft, thumbTop, thumbRight, thumbBottom);
		}
	}
	
	private void drawCustomText(Canvas canvas, float w, float h) {
		float textX = w / 2f;
		float textY = (h / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f);
		float marginPx = textMarginDp * density;
		
		switch (textPosition) {
			case LEFT:
			textPaint.setTextAlign(Paint.Align.LEFT);
			textX = thumbSize + marginPx;
			break;
			case RIGHT:
			textPaint.setTextAlign(Paint.Align.RIGHT);
			textX = w - marginPx;
			break;
			case TOP:
			textPaint.setTextAlign(Paint.Align.CENTER);
			textY = marginPx - textPaint.ascent();
			break;
			case BOTTOM:
			textPaint.setTextAlign(Paint.Align.CENTER);
			textY = h - marginPx - textPaint.descent();
			break;
			case CENTER:
			default:
			textPaint.setTextAlign(Paint.Align.CENTER);
			textX = w / 2f;
			break;
		}
		
		canvas.drawText(sliderText, textX, textY, textPaint);
	}
	
	private void drawCustomIcon(Canvas canvas, float h, float padding, float thumbLeft, float thumbTop, float thumbRight, float thumbBottom) {
		float availableHeight = (thumbBottom - thumbTop) - (12f * density);
		float size = (iconSizeDp > 0) ? (iconSizeDp * density) : availableHeight;
		
		float thumbWidthActual = thumbRight - thumbLeft;
		float thumbHeightActual = thumbBottom - thumbTop;
		
		float marginPx = iconMarginDp * density;
		float iconLeftX;
		float iconTopY;
		
		// Posisi Horizontal
		switch (iconPosition) {
			case LEFT:
			iconLeftX = thumbLeft + marginPx;
			break;
			case RIGHT:
			iconLeftX = thumbRight - size - marginPx;
			break;
			case CENTER:
			case TOP:
			case BOTTOM:
			default:
			iconLeftX = thumbLeft + (thumbWidthActual / 2f) - (size / 2f);
			break;
		}
		
		// Posisi Vertikal
		switch (iconPosition) {
			case TOP:
			iconTopY = thumbTop + marginPx;
			break;
			case BOTTOM:
			iconTopY = thumbBottom - size - marginPx;
			break;
			case CENTER:
			case LEFT:
			case RIGHT:
			default:
			iconTopY = thumbTop + (thumbHeightActual / 2f) - (size / 2f);
			break;
		}
		
		int iconLeft = (int) iconLeftX;
		int iconTop = (int) iconTopY;
		int iconRight = (int) (iconLeftX + size);
		int iconBottom = (int) (iconTopY + size);
		
		if (iconLeft < iconRight && iconTop < iconBottom) {
			thumbIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
			thumbIcon.draw(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled() || isUnlocked) return false;
		
		float x = event.getX();
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			if (x >= thumbX && x <= thumbX + thumbSize) {
				isSliding = true;
				if (thumbAnimator != null && thumbAnimator.isRunning()) {
					thumbAnimator.cancel();
				}
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				return true;
			}
			break;
			
			case MotionEvent.ACTION_MOVE:
			if (isSliding) {
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				
				float targetX = x - (thumbSize / 2f);
				
				if (targetX < 0) {
					thumbX = 0;
				} else if (targetX > getWidth() - thumbSize) {
					thumbX = getWidth() - thumbSize;
				} else {
					thumbX = targetX;
				}
				invalidate();
				return true;
			}
			break;
			
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			if (isSliding) {
				isSliding = false;
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(false);
				}
				
				float threshold = getWidth() - thumbSize - (20f * density);
				
				if (thumbX >= threshold) {
					isUnlocked = true;
					thumbX = getWidth() - thumbSize;
					invalidate();
					
					if (listener != null) {
						listener.onUnlocked();
					}
					
					postDelayed(() -> resetSlider(), 300);
					
				} else {
					animateThumbTo(0);
				}
				return true;
			}
			break;
		}
		return super.onTouchEvent(event);
	}
	
	private void animateThumbTo(float targetX) {
		if (thumbAnimator != null && thumbAnimator.isRunning()) {
			thumbAnimator.cancel();
		}
		
		thumbAnimator = ValueAnimator.ofFloat(thumbX, targetX);
		thumbAnimator.setDuration(250);
		thumbAnimator.setInterpolator(new DecelerateInterpolator());
		thumbAnimator.addUpdateListener(animation -> {
			thumbX = (float) animation.getAnimatedValue();
			invalidate();
		});
		thumbAnimator.start();
	}
	
	public void resetSlider() {
		if (thumbAnimator != null && thumbAnimator.isRunning()) {
			thumbAnimator.cancel();
		}
		isUnlocked = false;
		isSliding = false;
		animateThumbTo(0);
	}
	
	/* ==================================== */
	/* ========= GETTER & SETTER ========== */
	/* ==================================== */
	
	// Atur Radius Kustom (Gunakan ShapeType.CUSTOM_RADIUS)
	public void setCustomRadiusDp(float bgRadiusDp, float thumbRadiusDp) {
		this.bgRadiusDp = bgRadiusDp;
		this.thumbRadiusDp = thumbRadiusDp;
		this.bgShape = ShapeType.CUSTOM_RADIUS;
		this.thumbShape = ShapeType.CUSTOM_RADIUS;
		invalidate();
	}
	
	// Atur Posisi Tata Letak Icon & Text (TOP, BOTTOM, LEFT, RIGHT, CENTER)
	public void setIconPosition(Alignment position) {
		this.iconPosition = position;
		invalidate();
	}
	
	public void setTextPosition(Alignment position) {
		this.textPosition = position;
		invalidate();
	}
	
	public void setIconMarginDp(float marginDp) {
		this.iconMarginDp = marginDp;
		invalidate();
	}
	
	public void setTextMarginDp(float marginDp) {
		this.textMarginDp = marginDp;
		invalidate();
	}
	
	public void setIconSizeDp(float sizeDp) {
		this.iconSizeDp = sizeDp;
		invalidate();
	}
	
	public void setThumbWidthDp(int widthDp) {
		if (widthDp > 0) {
			this.customThumbWidth = widthDp * density;
		} else {
			this.customThumbWidth = -1;
		}
		thumbSize = (customThumbWidth > 0) ? customThumbWidth : getHeight();
		invalidate();
	}
	
	public void setThumbPaddingDp(float paddingDp) {
		this.thumbPadding = paddingDp;
		invalidate();
	}
	
	public void setShapes(ShapeType bgShape, ShapeType thumbShape) {
		this.bgShape = bgShape;
		this.thumbShape = thumbShape;
		invalidate();
	}
	
	public void setColors(int trackColor, int thumbColor, int textColor, int iconColor) {
		this.trackColor = trackColor;
		this.thumbColor = thumbColor;
		this.textColor = textColor;
		this.iconColor = iconColor;
		updateColors();
		invalidate();
	}
	
	public void setSliderText(String text) {
		this.sliderText = text;
		invalidate();
	}
	
	public void setTextSizeSp(float sizeSp) {
		textPaint.setTextSize(sizeSp * density);
		invalidate();
	}
	
	public void setThumbIcon(Drawable icon) {
		this.thumbIcon = icon;
		updateColors();
		invalidate();
	}
}
