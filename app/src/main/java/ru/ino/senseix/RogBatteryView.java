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

public class RogBatteryView extends View {

    private Paint bgPaint, progressPaint, capPaint, borderPaint;

    private int displayPercent = 0;
    private int targetPercent = 0;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final int UPDATE_DELAY = 2000;

    private ValueAnimator animator;

    public RogBatteryView(Context c) {
        super(c);
        init();
    }

    public RogBatteryView(Context c, AttributeSet a) {
        super(c, a);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        float density = getResources().getDisplayMetrics().density;

        // BODY BG
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(0x1AFFFFFF); // Dibuat sedikit lebih transparan ala M3 (10% alpha)

        // PROGRESS
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(0xFFFFFFFF);
        // Shadow dihapus/dikurangi agar flat & clean khas Material 3
        progressPaint.setShadowLayer(5, 0, 0, 0x40FFFFFF); 

        // CAP (Kepala Baterai)
        capPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 🔥 OUTLINE / BORDER (TIPIS ALA M3)
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2f * density); // Ketebalan 2dp (bisa diubah ke 3f jika kurang tebal)
        borderPaint.setColor(0x80FFFFFF);       // Warna putih semi-transparan (50% alpha)

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
            targetPercent = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
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

        // Mengambil nilai stroke untuk offset border agar tidak terpotong tepi canvas
        float strokeOffset = borderPaint.getStrokeWidth() / 2f;

        float capHeight = h * 0.08f; // Disesuaikan proporsinya
        float bodyHeight = h - capHeight;

        // 🔥 RADIUS BOX ROUNDED ALA M3
        // Menggunakan density-independent radius agar serasi di semua resolusi layar
        float density = getResources().getDisplayMetrics().density;
        float bodyRadius = 2f * density; 
        float capRadius = 2f * density;

        // Koordinat body digeser masuk sebesar strokeOffset agar garis border tergambar sempurna
        RectF bodyRect = new RectF(strokeOffset, capHeight + strokeOffset, w - strokeOffset, h - strokeOffset);

        int saveLayer = canvas.saveLayer(0, 0, w, h, null);

        // ===== 1. DRAW BODY BACKGROUND =====
        canvas.drawRoundRect(bodyRect, bodyRadius, bodyRadius, bgPaint);

        // ===== 2. DRAW PROGRESS WITH CLIP =====
        float progressH = (bodyHeight - (strokeOffset * 2)) * displayPercent / 100f;
        float top = h - strokeOffset - progressH;

        int saveClip = canvas.save();
        Path clip = new Path();
        clip.addRoundRect(bodyRect, bodyRadius, bodyRadius, Path.Direction.CW);
        canvas.clipPath(clip);

        // Menggambar isi baterai di dalam area yang sudah di-clip
        canvas.drawRect(strokeOffset, top, w - strokeOffset, h - strokeOffset, progressPaint);
        canvas.restoreToCount(saveClip);

        // ===== 3. DRAW OUTLINE / BORDER =====
        canvas.drawRoundRect(bodyRect, bodyRadius, bodyRadius, borderPaint);

        // ===== 4. DRAW CAP (KEPALA BATERAI M3) =====
        float capWidth = w * 0.40f; // Lebih ramping khas M3
        float capLeft = (w - capWidth) / 2f;

        RectF capRect = new RectF(
                capLeft + strokeOffset,
                strokeOffset,
                capLeft + capWidth - strokeOffset,
                capHeight + strokeOffset
        );

        // Logika warna kepala baterai menyatu dengan progress
        if (displayPercent >= 100) {
            capPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            capPaint.setColor(progressPaint.getColor());
        } else {
            // Mengikuti gaya border body saat tidak penuh
            capPaint.setStyle(Paint.Style.STROKE);
            capPaint.setStrokeWidth(borderPaint.getStrokeWidth());
            capPaint.setColor(borderPaint.getColor());
        }

        canvas.drawRoundRect(capRect, capRadius, capRadius, capPaint);

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

    public void setBorderColor(int color) {
        borderPaint.setColor(color);
        invalidate();
    }
}
