package ru.ino.senseix;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.PathInterpolator;

import java.util.ArrayList;
import java.util.List;

public class InoBottomNavigation extends View {
	
	private final float density;
	private final ArgbEvaluator colorEval = new ArgbEvaluator();
	private final PathInterpolator m3Interpolator = new PathInterpolator(0.2f, 0f, 0f, 1f);
	
	private final List<String> tabs = new ArrayList<>();
	private final List<Drawable> tabIcons = new ArrayList<>();
	
	// WARNA SOLID
	private int barBackgroundColor = 0xFF140C0B;
	private int activeColor = 0xFFFDD9D9;
	private int inactiveColor = 0xFF2C2424;
	private int indicatorColor = 0xFF6E2A2A;
	
	private TextPaint textPaint;
	private Paint indicatorPaint;
	private Paint barBgPaint;
	
	private int selectedPosition = 0;
	private float scrollPosition = 0f;
	
	private int pressedTabIndex = -1;
	private InoViewPager associatedViewPager;
	private ValueAnimator scrollAnimator;
	
	private float[] tabPositionsLeft;
	private float[] tabPositionsRight;
	
	// RectF reusable untuk menggambar rounded rect
	private final RectF rectF = new RectF();
	
	public InoBottomNavigation(Context context) {
		this(context, null);
	}
	
	public InoBottomNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		density = getResources().getDisplayMetrics().density;
		init();
	}
	
	private void init() {
		setClickable(true);
		
		textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(14f * density); // Sedikit disesuaikan agar lebih proporsional
		textPaint.setTextAlign(Paint.Align.LEFT); 
		textPaint.setFakeBoldText(true);
		
		indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		indicatorPaint.setStyle(Paint.Style.FILL);
		
		barBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barBgPaint.setStyle(Paint.Style.FILL);
	}
	
	/* ========================= TAB BUILDER ========================= */
	
	public InoBottomNavigation tab(String title) {
		tabs.add(title);
		tabIcons.add(null);
		updateArrays();
		requestLayout();
		return this;
	}
	
	public InoBottomNavigation tab(String title, Drawable icon) {
		tabs.add(title);
		tabIcons.add(icon);
		updateArrays();
		requestLayout();
		return this;
	}
	
	public void clearTabs() {
		tabs.clear();
		tabIcons.clear();
		selectedPosition = 0;
		scrollPosition = 0f;
		updateArrays();
		requestLayout();
	}
	
	private void updateArrays() {
		tabPositionsLeft = new float[tabs.size()];
		tabPositionsRight = new float[tabs.size()];
	}
	
	public void setupWithViewPager(InoViewPager viewPager) {
		associatedViewPager = viewPager;
		if (viewPager != null) {
			viewPager.addOnPageChangeListener(new InoViewPager.OnPageChangeListener() {
				@Override
				public void onPageScrolled(int position, float positionOffset) {
					scrollPosition = position + positionOffset;
					invalidate(); 
				}
				
				@Override
				public void onPageSelected(int position) {
					selectedPosition = position;
					invalidate();
				}
			});
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Disesuaikan menjadi 50dp agar icon dan indikator terlihat rapi secara vertikal (tanpa padding)
		int desiredHeight = (int) (50f * density); 
		
		int desiredWidth = (int) (260f * density); 
		
		int resolvedWidth = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY 
		? MeasureSpec.getSize(widthMeasureSpec) : desiredWidth;
		
		setMeasuredDimension(resolvedWidth, resolveSize(desiredHeight, heightMeasureSpec));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (tabs.isEmpty() || tabPositionsLeft == null) return false;
		
		float touchX = event.getX();
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			pressedTabIndex = -1;
			for (int i = 0; i < tabs.size(); i++) {
				if (touchX >= tabPositionsLeft[i] && touchX <= tabPositionsRight[i]) {
					pressedTabIndex = i;
					break;
				}
			}
			break;
			
			case MotionEvent.ACTION_UP:
			if (pressedTabIndex != -1) {
				int upIndex = -1;
				for (int i = 0; i < tabs.size(); i++) {
					if (touchX >= tabPositionsLeft[i] && touchX <= tabPositionsRight[i]) {
						upIndex = i;
						break;
					}
				}
				
				if (upIndex == pressedTabIndex && upIndex >= 0 && upIndex < tabs.size()) {
					selectedPosition = upIndex;
					if (associatedViewPager != null) {
						associatedViewPager.setCurrentItem(selectedPosition, true);
					} else {
						// Animasi standalone jika viewpager tidak dipakai
						animateToTab(selectedPosition);
					}
				}
			}
			pressedTabIndex = -1;
			break;
			
			case MotionEvent.ACTION_CANCEL:
			pressedTabIndex = -1;
			break;
		}
		return true;
	}
	
	private void animateToTab(int targetPosition) {
		if (scrollAnimator != null && scrollAnimator.isRunning()) {
			scrollAnimator.cancel();
		}
		scrollAnimator = ValueAnimator.ofFloat(scrollPosition, targetPosition);
		scrollAnimator.setDuration(300);
		scrollAnimator.setInterpolator(m3Interpolator);
		scrollAnimator.addUpdateListener(animation -> {
			scrollPosition = (float) animation.getAnimatedValue();
			invalidate();
		});
		scrollAnimator.start();
	}
	
	/* ========================= GAMBAR (DRAW) ========================= */
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (tabs.isEmpty() || tabPositionsLeft == null) return;
		
		float w = getWidth();
		float h = getHeight();
		
		// Radius melingkar penuh menyesuaikan tinggi view
		float cornerRadius = h / 2f; 
		
		float iconSize = 28f * density;
		float spacing = 8f * density; // Jarak icon ke text
		
		// 1. Gambar Background Utama Bar (Full View)
		barBgPaint.setColor(barBackgroundColor);
		rectF.set(0, 0, w, h);
		canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, barBgPaint);
		
		// 2. HITUNG DISTRIBUSI RASIO LEBAR ITEM (TANPA PADDING)
		float totalWeights = 0f;
		float[] weights = new float[tabs.size()];
		
		for (int i = 0; i < tabs.size(); i++) {
			float distance = Math.abs(scrollPosition - i);
			float activeWeight = Math.max(0f, Math.min(1f, 1f - distance));
			float interpWeight = m3Interpolator.getInterpolation(activeWeight);
			
			weights[i] = 1.0f + (1.0f * interpWeight); // Tab aktif mendapat lebar 2x dari tab inaktif
			totalWeights += weights[i];
		}
		
		float currentX = 0f; // Mulai langsung dari 0
		for (int i = 0; i < tabs.size(); i++) {
			float calculatedTabWidth = (weights[i] / totalWeights) * w; // Pakai full lebar 'w'
			
			tabPositionsLeft[i] = currentX;
			tabPositionsRight[i] = currentX + calculatedTabWidth;
			currentX += calculatedTabWidth;
		}
		
		/* ========================= MORPHING INDICATOR BOX ========================= */
		int leftIndex = (int) Math.floor(scrollPosition);
		int rightIndex = (int) Math.ceil(scrollPosition);
		leftIndex = Math.max(0, Math.min(leftIndex, tabs.size() - 1));
		rightIndex = Math.max(0, Math.min(rightIndex, tabs.size() - 1));
		
		float fraction = scrollPosition - leftIndex;
		float segmentWeight = m3Interpolator.getInterpolation(fraction);
		
		float finalLeft = tabPositionsLeft[leftIndex] + ((tabPositionsLeft[rightIndex] - tabPositionsLeft[leftIndex]) * segmentWeight);
		float finalRight = tabPositionsRight[leftIndex] + ((tabPositionsRight[rightIndex] - tabPositionsRight[leftIndex]) * segmentWeight);
		
		// Gambar indikator mengikuti atas & bawah tanpa padding
		indicatorPaint.setColor(indicatorColor);
		rectF.set(finalLeft, 0, finalRight, h);
		
		canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, indicatorPaint);
		
		/* ========================= RENDERING HORIZONTAL CONTENT ========================= */
		for (int i = 0; i < tabs.size(); i++) {
			float tLeft = tabPositionsLeft[i];
			float tRight = tabPositionsRight[i];
			float cx = (tLeft + tRight) / 2f;
			float cy = h / 2f; 
			
			float distance = Math.abs(scrollPosition - i);
			float activeWeight = Math.max(0f, Math.min(1f, 1f - distance));
			
			int currentTextColor = (Integer) colorEval.evaluate(activeWeight, inactiveColor, activeColor);
			Drawable icon = (i < tabIcons.size()) ? tabIcons.get(i) : null;
			
			if (icon != null) {
				icon.setTint(currentTextColor);
				
				String title = tabs.get(i);
				float textWidth = textPaint.measureText(title);
				
				// Hitung jarak pergeseran dari tengah untuk animasi transisi mulus 
				float shiftDistance = (textWidth + spacing) / 2f;
				
				// Interpolasi X Icon dan Text (bergerak ke kiri secara dinamis berdasarkan activeWeight)
				float iconLeft = cx - (iconSize / 2f) - (shiftDistance * activeWeight);
				float textLeft = iconLeft + iconSize + spacing;
				
				int finalIconTop = (int) (cy - (iconSize / 2f));
				icon.setBounds((int) iconLeft, finalIconTop, (int) (iconLeft + iconSize), (int) (finalIconTop + iconSize));
				icon.draw(canvas);
				
				textPaint.setColor(currentTextColor);
				
				// Animasi alpha text: Text mulai terlihat saat progress mencapai ~30%
				float textAlphaProgress = Math.max(0f, (activeWeight - 0.3f) / 0.7f);
				textPaint.setAlpha((int) (255 * textAlphaProgress));
				
				Paint.FontMetrics fm = textPaint.getFontMetrics();
				float textY = cy - ((fm.ascent + fm.descent) / 2f);
				canvas.drawText(title, textLeft, textY, textPaint);
			}
		}
	}
	
	/* ========================= SETTERS FOR COLORS ========================= */
	
	public void setActiveColor(int color) {
		this.activeColor = color;
		invalidate();
	}
	
	public void setInactiveColor(int color) {
		this.inactiveColor = color;
		invalidate();
	}
	
	public void setIndicatorColor(int color) {
		this.indicatorColor = color;
		invalidate();
	}
	
	public void setBarBackgroundColor(int color) {
		this.barBackgroundColor = color;
		invalidate();
	}
}