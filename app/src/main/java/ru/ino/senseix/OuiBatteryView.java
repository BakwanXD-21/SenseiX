package ru.ino.senseix;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class OuiBatteryView extends View {

    private Paint bgPaint, progressPaint, cutTextPaint;

    private int displayPercent = 0;
    private int targetPercent = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int UPDATE_DELAY = 2000;

    private ValueAnimator animator;

    public OuiBatteryView(Context c) {
        super(c);
        init();
    }

    public OuiBatteryView(Context c, AttributeSet a) {
        super(c, a);
        init();
    }

    private void init() {

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        // 🔘 BACKGROUND
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(0x44FFFFFF);
        bgPaint.setStyle(Paint.Style.FILL);

        // ⚪ PROGRESS
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(0xFFFFFFFF);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setShadowLayer(12, 0, 0, 0x80FFFFFF);

        // 🕳 CUT TEXT (ANGKA BOLONG)
        cutTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cutTextPaint.setColor(Color.BLACK);
        cutTextPaint.setTextAlign(Paint.Align.CENTER);
        cutTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        cutTextPaint.setFakeBoldText(true);
        cutTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        start();
    }

    private void start() {
        handler.post(updateTask);
    }

    private final Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            readBattery();
            animateToTarget();
            handler.postDelayed(this, UPDATE_DELAY);
        }
    };

    private void readBattery() {
        BatteryManager bm = (BatteryManager)
                getContext().getSystemService(Context.BATTERY_SERVICE);

        if (bm != null) {
            targetPercent = bm.getIntProperty(
                    BatteryManager.BATTERY_PROPERTY_CAPACITY);
        }
    }

    private void animateToTarget() {
        if (animator != null) animator.cancel();

        animator = ValueAnimator.ofInt(displayPercent, targetPercent);
        animator.setDuration(450);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(a -> {
            displayPercent = (int) a.getAnimatedValue();
            invalidate();
        });

        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();

        float radius = h / 2f;

        RectF bgRect = new RectF(0, 0, w, h);
        float progressW = w * displayPercent / 100f;

        int saveLayer = canvas.saveLayer(0, 0, w, h, null);

        /* ===== BACKGROUND ===== */
        canvas.drawRoundRect(bgRect, radius, radius, bgPaint);

        /* ===== PROGRESS (KANAN LURUS) ===== */
        int saveClip = canvas.save();

        Path clipPath = new Path();
        clipPath.addRoundRect(bgRect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);

        canvas.drawRect(0, 0, progressW, h, progressPaint);

        canvas.restoreToCount(saveClip);

        /* ===== ANGKA BOLONG ===== */
        float textSize = h * 0.6f; // 🔥 sedikit lebih besar
        cutTextPaint.setTextSize(textSize);

        float centerX = w / 2f;
        float centerY = h / 2f - (cutTextPaint.descent() + cutTextPaint.ascent()) / 2;

        // 🔥 extra bold visual
        canvas.drawText(displayPercent + "", centerX - 0.5f, centerY, cutTextPaint);
        canvas.drawText(displayPercent + "", centerX + 0.5f, centerY, cutTextPaint);

        canvas.restoreToCount(saveLayer);
    }

    public void setProgressColor(int color) {
        progressPaint.setColor(color);
        invalidate();
    }

    public void setBackgroundColorCustom(int color) {
        bgPaint.setColor(color);
        invalidate();
    }
}