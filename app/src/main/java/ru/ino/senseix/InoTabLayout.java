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

public class InoTabLayout extends View {
	
	private final float density;
	
	private final ArgbEvaluator colorEval =
	new ArgbEvaluator();
	
	private final PathInterpolator m3Interpolator =
	new PathInterpolator(0.2f, 0f, 0f, 1f);
	
	/* ========================= */
	/* TAB DATA */
	/* ========================= */
	
	private final List<String> tabs =
	new ArrayList<>();
	
	private final List<Drawable> tabIcons =
	new ArrayList<>();
	
	/* ========================= */
	/* COLORS */
	/* ========================= */
	
	private int activeColor = 0xFF0D47A1;
	
	private int inactiveColor = 0xFF6E6E6E;
	
	private int indicatorColor = 0xFF0D47A1;
	
	/* ========================= */
	/* PAINT */
	/* ========================= */
	
	private TextPaint textPaint;
	
	private Paint indicatorPaint;
	
	private Paint ripplePaint;
	
	/* ========================= */
	/* STATE */
	/* ========================= */
	
	private int selectedPosition = 0;
	
	private float scrollPosition = 0f;
	
	private float pressX = -1f;
	
	private float pressProgress = 0f;
	
	private int pressedTabIndex = -1;
	
	private ValueAnimator pressAnimator;
	
	private InoViewPager associatedViewPager;
	
	public InoTabLayout(Context context) {
		this(context, null);
	}
	
	public InoTabLayout(Context context,
	AttributeSet attrs) {
		
		super(context, attrs);
		
		density =
		getResources()
		.getDisplayMetrics()
		.density;
		
		init();
	}
	
	private void init() {
		
		setClickable(true);
		
		textPaint =
		new TextPaint(Paint.ANTI_ALIAS_FLAG);
		
		textPaint.setTextSize(14f * density);
		
		textPaint.setTextAlign(Paint.Align.CENTER);
		
		indicatorPaint =
		new Paint(Paint.ANTI_ALIAS_FLAG);
		
		indicatorPaint.setStyle(Paint.Style.FILL);
		
		ripplePaint =
		new Paint(Paint.ANTI_ALIAS_FLAG);
		
		ripplePaint.setStyle(Paint.Style.FILL);
	}
	
	/* ========================= */
	/* TAB BUILDER */
	/* ========================= */
	
	public InoTabLayout tab(String title) {
		
		tabs.add(title);
		
		tabIcons.add(null);
		
		invalidate();
		
		return this;
	}
	
	public InoTabLayout tab(String title,
	Drawable icon) {
		
		tabs.add(title);
		
		tabIcons.add(icon);
		
		invalidate();
		
		return this;
	}
	
	public void clearTabs() {
		
		tabs.clear();
		
		tabIcons.clear();
		
		selectedPosition = 0;
		
		scrollPosition = 0f;
		
		invalidate();
	}
	
	/* ========================= */
	/* OLD ARRAY SUPPORT */
	/* ========================= */
	
	public void setTabs(String[] newTabs) {
		
		clearTabs();
		
		if (newTabs != null) {
			
			for (String s : newTabs) {
				
				tabs.add(s);
				
				tabIcons.add(null);
			}
		}
		
		invalidate();
	}
	
	/* ========================= */
	/* COLORS */
	/* ========================= */
	
	public void setActiveColor(int color) {
		
		activeColor = color;
		
		invalidate();
	}
	
	public void setInactiveColor(int color) {
		
		inactiveColor = color;
		
		invalidate();
	}
	
	public void setIndicatorColor(int color) {
		
		indicatorColor = color;
		
		invalidate();
	}
	
	/* ========================= */
	/* VIEWPAGER */
	/* ========================= */
	
	public void setupWithViewPager(InoViewPager viewPager) {
		associatedViewPager = viewPager;
		if (viewPager != null) {
			// UBAH: setOnPageChangeListener -> addOnPageChangeListener
			viewPager.addOnPageChangeListener(
			new InoViewPager.OnPageChangeListener() {
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
	
	
	/* ========================= */
	/* MEASURE */
	/* ========================= */
	
	@Override
	protected void onMeasure(
	int widthMeasureSpec,
	int heightMeasureSpec) {
		
		int desiredHeight =
		(int) (56f * density);
		
		setMeasuredDimension(
		MeasureSpec.getSize(widthMeasureSpec),
		resolveSize(
		desiredHeight,
		heightMeasureSpec
		)
		);
	}
	
	/* ========================= */
	/* TOUCH */
	/* ========================= */
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (tabs.size() == 0) {
			return false;
		}
		
		float tabWidth =
		(float) getWidth() / tabs.size();
		
		switch (event.getAction()) {
			
			case MotionEvent.ACTION_DOWN:
			
			pressX = event.getX();
			
			pressedTabIndex =
			(int) (pressX / tabWidth);
			
			animatePress(1f);
			
			break;
			
			case MotionEvent.ACTION_UP:
			
			if (pressedTabIndex != -1) {
				
				float upX = event.getX();
				
				int upIndex =
				(int) (upX / tabWidth);
				
				if (upIndex == pressedTabIndex) {
					
					selectedPosition = upIndex;
					
					if (associatedViewPager != null) {
						
						associatedViewPager
						.setCurrentItem(
						selectedPosition,
						true
						);
					}
				}
			}
			
			animatePress(0f);
			
			break;
			
			case MotionEvent.ACTION_CANCEL:
			
			animatePress(0f);
			
			break;
		}
		
		return true;
	}
	
	private void animatePress(float target) {
		
		if (pressAnimator != null) {
			pressAnimator.cancel();
		}
		
		pressAnimator =
		ValueAnimator.ofFloat(
		pressProgress,
		target
		);
		
		pressAnimator.setDuration(180);
		
		pressAnimator.setInterpolator(
		m3Interpolator
		);
		
		pressAnimator.addUpdateListener(
		animation -> {
			
			pressProgress =
			(float)
			animation
			.getAnimatedValue();
			
			invalidate();
		});
		
		pressAnimator.start();
	}
	
	/* ========================= */
	/* DRAW */
	/* ========================= */
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		if (tabs.size() == 0) {
			return;
		}
		
		float w = getWidth();
		
		float h = getHeight();
		
		float tabWidth =
		w / tabs.size();
		
		/* ========================= */
		/* RIPPLE */
		/* ========================= */
		
		if (pressProgress > 0f
		&& pressedTabIndex >= 0
		&& pressedTabIndex < tabs.size()) {
			
			ripplePaint.setColor(activeColor);
			
			ripplePaint.setAlpha(
			(int) (26 * pressProgress)
			);
			
			float cx =
			(pressedTabIndex * tabWidth)
			+ (tabWidth / 2f);
			
			float cy = h / 2f;
			
			canvas.drawCircle(
			cx,
			cy,
			(tabWidth / 2f) * pressProgress,
			ripplePaint
			);
		}
		
		/* ========================= */
		/* TEXT + ICON */
		/* ========================= */
		
		Paint.FontMetrics fm =
		textPaint.getFontMetrics();
		
		float textY =
		(h / 2f)
		- ((fm.ascent + fm.descent) / 2f);
		
		for (int i = 0; i < tabs.size(); i++) {
			
			float cx =
			(i * tabWidth)
			+ (tabWidth / 2f);
			
			float distance =
			Math.abs(scrollPosition - i);
			
			float activeWeight =
			Math.max(
			0f,
			Math.min(
			1f,
			1f - distance
			)
			);
			
			int currentTextColor =
			(Integer)
			colorEval.evaluate(
			activeWeight,
			inactiveColor,
			activeColor
			);
			
			textPaint.setColor(currentTextColor);
			
			textPaint.setFakeBoldText(
			activeWeight > 0.5f
			);
			
			Drawable icon = null;
			
			if (i < tabIcons.size()) {
				
				icon = tabIcons.get(i);
			}
			
			boolean hasIcon = icon != null;
			
			if (hasIcon) {
				
				icon.setTint(currentTextColor);
				
				int iconSize =
				(int) (20f * density);
				
				int left =
				(int) (cx - iconSize / 2f);
				
				int top =
				(int) (8f * density);
				
				icon.setBounds(
				left,
				top,
				left + iconSize,
				top + iconSize
				);
				
				icon.draw(canvas);
				
				canvas.drawText(
				tabs.get(i),
				cx,
				42f * density,
				textPaint
				);
				
			} else {
				
				canvas.drawText(
				tabs.get(i),
				cx,
				textY,
				textPaint
				);
			}
		}
		
		/* ========================= */
		/* INDICATOR */
		/* ========================= */
		
		float indicatorThickness =
		3f * density;
		
		float indicatorBottom = h;
		
		float indicatorTop =
		h - indicatorThickness;
		
		int leftIndex =
		(int) Math.floor(scrollPosition);
		
		int rightIndex =
		(int) Math.ceil(scrollPosition);
		
		leftIndex =
		Math.max(
		0,
		Math.min(
		leftIndex,
		tabs.size() - 1
		)
		);
		
		rightIndex =
		Math.max(
		0,
		Math.min(
		rightIndex,
		tabs.size() - 1
		)
		);
		
		float fraction =
		scrollPosition - leftIndex;
		
		float leftWidth =
		textPaint.measureText(
		tabs.get(leftIndex)
		) + 16f * density;
		
		float rightWidth =
		textPaint.measureText(
		tabs.get(rightIndex)
		) + 16f * density;
		
		float leftCenter =
		(leftIndex * tabWidth)
		+ (tabWidth / 2f);
		
		float rightCenter =
		(rightIndex * tabWidth)
		+ (tabWidth / 2f);
		
		float leftL =
		leftCenter - (leftWidth / 2f);
		
		float leftR =
		leftCenter + (leftWidth / 2f);
		
		float rightL =
		rightCenter - (rightWidth / 2f);
		
		float rightR =
		rightCenter + (rightWidth / 2f);
		
		float leftInterp =
		m3Interpolator.getInterpolation(
		Math.max(
		0f,
		(fraction - 0.2f) * 1.25f
		)
		);
		
		float rightInterp =
		m3Interpolator.getInterpolation(
		Math.min(
		fraction * 1.25f,
		1f
		)
		);
		
		float finalLeft =
		leftL
		+ ((rightL - leftL)
		* leftInterp);
		
		float finalRight =
		leftR
		+ ((rightR - leftR)
		* rightInterp);
		
		indicatorPaint.setColor(indicatorColor);
		
		RectF rect =
		new RectF(
		finalLeft,
		indicatorTop,
		finalRight,
		indicatorBottom
		);
		
		canvas.drawRoundRect(
		rect,
		indicatorThickness / 2f,
		indicatorThickness / 2f,
		indicatorPaint
		);
	}
}
