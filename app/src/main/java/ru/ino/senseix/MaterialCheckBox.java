package ru.ino.senseix;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.PathInterpolator;
import android.widget.CompoundButton;

public class MaterialCheckBox extends CompoundButton {

    private Paint boxPaint;
    private Paint tickPaint;
    private Paint ripplePaint;
    
    private RectF boxRect;
    private Path tickPath;
    private Path dstTickPath;
    private PathMeasure pathMeasure;
    private final ArgbEvaluator colorEval = new ArgbEvaluator();

    // Mengubah warna aksen utama menjadi 0xFF0D47A1
    private int checkedColor = 0xFF0D47A1;
    private int uncheckedColor = 0xFF6E6E6E; // M3 Outline Color

    private float density;
    private float cornerRadius;
    
    // Properti Animasi
    private float progress = 0f;
    private float pressProgress = 0f;
    private ValueAnimator checkAnimator;
    private ValueAnimator pressAnimator;

    public MaterialCheckBox(Context context) {
        this(context, null);
    }

    public MaterialCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        cornerRadius = 4f * density; // Ketegasan sudut M3 (4dp)

        boxRect = new RectF();
        tickPath = new Path();
        dstTickPath = new Path();
        pathMeasure = new PathMeasure();

        boxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Mempertahankan PorterDuff CLEAR untuk melubangi kotak
        tickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tickPaint.setStyle(Paint.Style.STROKE);
        tickPaint.setStrokeCap(Paint.Cap.ROUND);
        tickPaint.setStrokeJoin(Paint.Join.ROUND);
        tickPaint.setStrokeWidth(2.5f * density);
        tickPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ripplePaint.setStyle(Paint.Style.FILL);

        // WAJIB untuk PorterDuff CLEAR pada custom view custom rendering
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setButtonDrawable((Drawable) null);
        setBackground(null);
        setClickable(true);
        setFocusable(true);

        // Lebar area sentuh responsif M3 (48dp target)
        int leftPadding = (int) (40f * density);
        setPadding(leftPadding, getPaddingTop(), getPaddingRight(), getPaddingBottom());

        progress = isChecked() ? 1f : 0f;
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
    public void setChecked(boolean checked) {
        boolean changed = checked != isChecked();
        super.setChecked(checked);
        if (changed) {
            playAnimation(checked);
        }
    }

    private void playAnimation(boolean checked) {
        if (checkAnimator != null) checkAnimator.cancel();

        checkAnimator = ValueAnimator.ofFloat(progress, checked ? 1f : 0f);
        checkAnimator.setDuration(240);
        checkAnimator.setInterpolator(new PathInterpolator(0.2f, 0f, 0f, 1f));
        checkAnimator.addUpdateListener(a -> {
            progress = (float) a.getAnimatedValue();
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

    // ===== DRAW =====

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float boxSize = 18f * density; // Ukuran kotak standar M3
        float cx = 24f * density;      // Poros tengah area sentuh 48dp
        float cy = getHeight() / 2f;

        float left = cx - (boxSize / 2f);
        float top = cy - (boxSize / 2f);

        boxRect.set(left, top, left + boxSize, top + boxSize);

        // 1. STATE LAYER / RIPPLE (M3 Halo Effect)
        if (pressProgress > 0f) {
            int rippleColor = (Integer) colorEval.evaluate(progress, uncheckedColor, checkedColor);
            ripplePaint.setColor(rippleColor);
            ripplePaint.setAlpha((int) (32 * pressProgress)); // Opacity tipis 12%
            
            float rippleRadius = 20f * density * pressProgress;
            canvas.drawCircle(cx, cy, rippleRadius, ripplePaint);
        }

        // 2. STRUKTUR KOTAK (Box)
        int currentBoxColor = (Integer) colorEval.evaluate(progress, uncheckedColor, checkedColor);

        if (progress > 0f) {
            boxPaint.setStyle(Paint.Style.FILL);
            boxPaint.setColor(currentBoxColor);
            canvas.drawRoundRect(boxRect, cornerRadius, cornerRadius, boxPaint);
        }

        boxPaint.setStyle(Paint.Style.STROKE);
        float strokeWidth = (2f - (0.5f * progress)) * density; 
        boxPaint.setStrokeWidth(strokeWidth);
        boxPaint.setColor(currentBoxColor);
        canvas.drawRoundRect(boxRect, cornerRadius, cornerRadius, boxPaint);

        // 3. TANDA CENTANG TEMBUS PANDANG (PorterDuff CLEAR)
        if (progress > 0f) {
            tickPath.reset();
            tickPath.moveTo(left + 0.26f * boxSize, top + 0.50f * boxSize);
            tickPath.lineTo(left + 0.44f * boxSize, top + 0.68f * boxSize);
            tickPath.lineTo(left + 0.76f * boxSize, top + 0.30f * boxSize);

            pathMeasure.setPath(tickPath, false);
            dstTickPath.reset();
            
            // Menggambar pemotongan jalur secara progresif (Animasi meluncur)
            pathMeasure.getSegment(0f, progress * pathMeasure.getLength(), dstTickPath, true);
            
            canvas.drawPath(dstTickPath, tickPaint);
        }
    }
}
