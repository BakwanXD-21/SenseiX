package ru.ino.senseix;

import android.animation.ValueAnimator;
import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.PathInterpolator;
import android.widget.CompoundButton;

public class MaterialRadioButton extends CompoundButton {

    private Paint paintOuter;
    private Paint paintInner;
    private Paint paintRipple;

    // Mengubah aksen aktif menjadi 0xFF0D47A1 Sesuai Permintaan
    private int checkedColor = 0xFF0D47A1;
    private int uncheckedColor = 0xFF6E6E6E; // M3 Outline Color

    private final ArgbEvaluator colorEval = new ArgbEvaluator();
    private float density;
    
    // Status Animasi
    private float animScale = 0f;
    private float pressProgress = 0f;
    private ValueAnimator checkAnimator;
    private ValueAnimator pressAnimator;

    public MaterialRadioButton(Context context) {
        this(context, null);
    }

    public MaterialRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;

        paintOuter = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOuter.setStyle(Paint.Style.STROKE);

        paintInner = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintInner.setStyle(Paint.Style.FILL);

        paintRipple = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRipple.setStyle(Paint.Style.FILL);

        setButtonDrawable((Drawable) null);
        setBackground(null);
        setClickable(true);
        setFocusable(true);

        int leftPadding = (int) (40f * density);
        setPadding(leftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());

        animScale = isChecked() ? 1f : 0f;
    }

    // ===== PUBLIC API =====

    public void setCheckedColor(int color) {
        checkedColor = color;
        invalidate();
    }

    public void setUncheckedColor(int color) {
        uncheckedColor = color;
        invalidate();
    }

    // ===== INTERACTION & TOUCH =====

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return false;

        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animatePress(1f);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                animatePress(0f);
                break;
        }
        return true;
    }

    @Override
    public void toggle() {
        if (!isChecked()) {
            super.toggle();
        }
    }

    @Override
    public void setChecked(boolean checked) {
        boolean changed = checked != isChecked();
        super.setChecked(checked);
        if (changed) {
            playAnimation(checked);
        }
    }

    private void playAnimation(boolean checked) {
        if (checkAnimator != null) checkAnimator.cancel();

        checkAnimator = ValueAnimator.ofFloat(animScale, checked ? 1f : 0f);
        checkAnimator.setDuration(250);
        checkAnimator.setInterpolator(new PathInterpolator(0.2f, 0f, 0f, 1f));
        checkAnimator.addUpdateListener(a -> {
            animScale = (float) a.getAnimatedValue();
            invalidate();
        });
        checkAnimator.start();
    }

    private void animatePress(float target) {
        if (pressAnimator != null) pressAnimator.cancel();

        pressAnimator = ValueAnimator.ofFloat(pressProgress, target);
        pressAnimator.setDuration(160);
        pressAnimator.setInterpolator(new PathInterpolator(0.2f, 0f, 0f, 1f));
        pressAnimator.addUpdateListener(a -> {
            pressProgress = (float) a.getAnimatedValue();
            invalidate();
        });
        pressAnimator.start();
    }

    // ===== DRAW MATEMATIKA MATERIAL 3 =====

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float cx = 24f * density; 
        float cy = getHeight() / 2f;

        // 1. GAMBAR STATE LAYER / RIPPLE (M3 Halo Effect)
        if (pressProgress > 0f) {
            int rippleColor = (Integer) colorEval.evaluate(animScale, uncheckedColor, checkedColor);
            paintRipple.setColor(rippleColor);
            paintRipple.setAlpha((int) (30 * pressProgress)); // Opacity halus 12%
            
            float rippleRadius = 20f * density * pressProgress;
            canvas.drawCircle(cx, cy, rippleRadius, paintRipple);
        }

        // 2. KALKULASI DINAMIS LINGKARAN LUAR (Outer Circle)
        float strokeWidth = (2f + (3f * (float) Math.sin(animScale * Math.PI))) * density;
        paintOuter.setStrokeWidth(strokeWidth);

        int currentOuterColor = (Integer) colorEval.evaluate(animScale, uncheckedColor, checkedColor);
        paintOuter.setColor(currentOuterColor);

        float outerRadius = (10f - (0.5f * animScale)) * density;
        canvas.drawCircle(cx, cy, outerRadius, paintOuter);

        // 3. GAMBAR DOT TENGAH (Inner Dot)
        if (animScale > 0f) {
            paintInner.setColor(checkedColor);
            float currentInnerRadius = animScale * 5f * density;
            canvas.drawCircle(cx, cy, currentInnerRadius, paintInner);
        }
    }
}
