package ru.ino.senseix;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RAM Gauge View - Full Path Animated Light Flow (Kiri -> Kanan)
 */
public class RamView extends View {

    private static final float VIEWPORT_WIDTH  = 308f;
    private static final float VIEWPORT_HEIGHT = 116f;

    private static final String BASELINE_DATA = "M 5.984 60.279 C 10.203 63.984 17.475 67.585 25.192 70.638 C 34.693 74.445 47.967 77.532 59.835 80.002 C 71.806 82.437 83.536 83.912 96.879 85.215 C 112.314 86.724 128.95 87.925 147.266 87.959 C 168.978 88.028 195.663 86.793 218.507 84.735 C 239.773 82.814 261.485 78.115 276.268 73.622 C 286.455 70.535 293.898 67.105 299.764 63.126 C 304.394 60.005 307.378 56.575 308.167 52.905 C 309.059 48.994 307.07 43.712 304.085 40.248 C 301.204 36.921 296.402 34.863 291.36 32.222 C 284.706 28.757 270.06 24.641 261.142 22.275 C 254.488 20.491 249.857 19.668 243.169 18.536 C 236.137 17.301 228.351 16.066 221.354 15.106 C 215.008 14.214 209.52 13.425 201.871 12.945 C 191.17 12.225 162.975 12.156 162.975 12.156 C 162.975 12.156 190.347 12.979 201.048 13.906 C 208.423 14.557 215.248 15.346 221.697 16.409 C 228.385 17.507 236.034 18.982 242.002 20.423 C 246.77 21.589 250.132 22.481 254.522 24.264 C 260.113 26.494 268.928 30.164 272.289 33.285 C 274.759 35.583 276.268 38.739 275.891 41.448 C 275.548 44.261 273.044 47.314 269.648 49.818 C 265.189 53.076 257.232 55.683 249.411 58.05 C 240.185 60.862 229.174 63.057 217.512 64.772 C 203.895 66.796 186.402 68.1 172.442 68.751 C 159.168 69.369 148.02 69.163 135.672 68.751 C 124.559 68.374 104.014 66.522 104.014 66.522 C 104.014 66.522 86.143 64.327 80.895 63.4 C 75.236 62.406 69.576 61.205 63.54 59.353 C 56.302 57.158 44.606 53.385 39.53 49.852 C 34.865 46.593 33.013 43.026 33.458 39.459 C 33.904 35.789 40.113 30.918 43.886 28.826 C 50.06 25.396 64.157 21.28 74.275 19.188 C 79.455 18.09 93.826 16.204 102.196 15.175 C 108.816 14.351 114.269 13.768 120.409 13.322 C 128.847 12.739 150.833 12.053 C 150.833 12.053 C 150.833 12.053 130.39 11.745 121.198 12.259 C 113.926 12.671 106.174 13.425 99.314 14.214 C 91.666 15.106 83.914 16.032 75.75 17.336 C 67.004 18.742 49.168 22.686 C 49.168 22.686 C 49.168 22.686 40.387 24.607 35.551 26.013 C 29.788 27.694 22.174 29.581 16.926 32.29 C 12.021 34.863 7.528 38.224 4.784 41.654 C 2.245 44.741 0.393 48.48 0.668 51.67 C 0.976 54.688 2.897 57.569 5.984 60.279 Z";
    private static final String OVERLINE_DATA = "M 47.316 39.493 C 47.316 27.728 95.37 18.193 154.64 18.193 C 213.911 18.193 261.965 27.728 261.965 39.493 C 261.965 51.293 213.911 60.828 154.64 60.828 C 95.37 60.828 47.316 51.293 47.316 39.493 Z M 67.758 43.884 C 67.758 34.554 106.243 26.974 153.714 26.974 C 201.185 26.974 239.67 34.554 239.67 43.884 C 239.67 53.248 201.185 60.828 153.714 60.828 C 106.243 60.828 67.758 53.248 67.758 43.884 Z M 89.71 47.965 C 89.71 40.865 118.385 35.137 153.783 35.137 C 189.18 35.137 217.89 40.865 217.89 47.965 C 217.89 55.066 189.18 60.828 153.783 60.828 C 118.385 60.828 89.71 55.066 89.71 47.965 Z M 112.211 52.699 C 112.211 48.206 130.802 44.57 153.783 44.57 C 176.73 44.57 195.354 48.206 195.354 52.699 C 195.354 57.192 176.73 60.828 153.783 60.828 C 130.802 60.828 112.211 57.192 112.211 52.699 Z";
    private static final String MOVING_LINE_DATA = "M 64.877 26.151 C 67.484 28.14 72.012 30.095 76.814 31.776 C 82.713 33.8 90.945 35.515 98.32 36.818 C 105.729 38.156 113 38.945 121.301 39.631 C 130.87 40.454 141.195 41.105 152.548 41.14 C 166.028 41.174 182.595 40.488 196.795 39.39 C 209.966 38.361 223.446 35.823 232.639 33.388 C 238.95 31.707 243.58 29.855 247.216 27.694 C 250.063 26.013 251.915 24.161 252.43 22.172 C 252.978 20.045 251.744 17.198 249.891 15.312 C 248.108 13.528 245.124 12.396 241.968 10.956 C 237.852 9.069 228.763 6.84 223.24 5.571 C 219.124 4.61 216.243 4.164 212.093 3.547 C 207.737 2.895 202.866 2.209 198.544 1.695 C 194.6 1.215 191.204 0.803 186.471 0.529 C 179.817 0.151 162.324 0.117 162.324 0.117 C 162.324 0.117 179.302 0.529 185.956 1.043 C 190.518 1.386 194.771 1.832 198.75 2.415 C 202.9 2.998 207.668 3.821 211.373 4.576 C 214.322 5.193 216.415 5.708 219.124 6.668 C 222.589 7.869 228.077 9.858 230.169 11.539 C 231.678 12.774 232.604 14.489 232.398 15.964 C 232.158 17.473 230.615 19.153 228.523 20.491 C 225.744 22.24 220.805 23.681 215.969 24.95 C 210.241 26.459 203.381 27.66 196.143 28.586 C 187.706 29.684 176.867 30.404 168.189 30.747 C 159.957 31.09 153.028 30.953 145.379 30.747 C 138.485 30.541 125.725 29.546 125.725 29.546 C 125.725 29.546 114.647 28.346 111.388 27.831 C 107.855 27.283 104.357 26.665 100.584 25.67 C 96.125 24.47 88.853 22.412 85.697 20.491 C 82.816 18.742 81.65 16.821 81.924 14.866 C 82.199 12.877 86.075 10.27 88.407 9.138 C 92.249 7.251 100.995 5.056 107.272 3.89 C 110.462 3.341 119.414 2.312 124.594 1.729 C 128.71 1.283 132.071 0.974 135.878 0.734 C 141.126 0.426 154.778 0.048 154.778 0.048 C 154.778 0.048 142.087 -0.123 136.393 0.151 C 131.865 0.391 127.063 0.803 122.81 1.215 C 118.077 1.695 113.24 2.209 108.164 2.895 C 102.779 3.65 91.666 5.811 91.666 5.811 C 91.666 5.811 86.246 6.84 83.228 7.594 C 79.661 8.52 74.927 9.515 71.669 11.024 C 68.65 12.396 65.838 14.214 64.123 16.066 C 62.579 17.747 61.413 19.771 61.584 21.486 C 61.756 23.132 62.956 24.676 64.877 26.151 Z";
    private static final String STATIC_LINE_DATA = "M 193.331 102.331 L 193.022 106.378 C 193.022 106.378 219.124 105.246 229.449 103.943 C 237.063 102.983 242.929 102.297 249 100.753 C 254.693 99.313 259.77 97.495 264.846 95.402 C 269.991 93.276 279.835 88.268 279.835 88.268 C 279.835 88.268 268.688 92.041 263.131 93.653 C 257.678 95.197 252.773 96.534 247.045 97.563 C 240.802 98.695 234.662 99.244 227.048 100.033 C 207.119 102.057 192.508 102.811 193.331 102.331 Z M 29.788 88.577 C 29.788 88.577 42.548 95.265 49.339 97.7 C 55.822 100.033 58.875 100.959 67.175 102.365 C 79.352 104.389 116.327 106.378 116.327 106.378 L 115.916 102.057 C 115.916 102.057 80.449 100.307 68.479 98.592 C 60.898 97.495 56.92 96.774 50.883 95.231 C 44.057 93.516 29.788 88.577 29.788 88.577 Z";
    private static final String MAIN_TEXT_DATA = "M 142.126 97.994 L 140.966 105.644 Q 140.956 106.014 140.776 106.344 Q 140.596 106.674 140.296 106.914 Q 140.006 107.154 139.646 107.284 Q 139.276 107.414 138.886 107.414 L 135.566 107.414 L 140.816 113.014 L 135.216 113.014 L 130.236 107.414 L 129.346 113.014 L 124.246 113.014 L 126.306 99.004 L 125.556 97.994 L 142.126 97.994 Z M 131.136 100.544 L 130.516 104.864 L 136.146 104.864 L 136.736 100.544 L 131.136 100.544 Z M 160.016 97.994 L 161.736 113.014 L 157.166 113.014 L 156.726 108.924 L 150.346 108.924 L 148.486 113.014 L 144.156 113.014 L 150.316 98.734 L 149.556 97.994 L 160.016 97.994 Z M 154.096 100.544 L 151.436 106.384 L 156.526 106.384 L 155.876 100.544 L 154.096 100.544 Z M 172.016 97.994 L 174.636 107.414 L 180.166 97.994 L 185.526 97.994 L 183.186 113.014 L 178.596 113.014 L 180.016 105.104 L 175.826 112.014 L 171.736 112.014 L 169.826 105.374 L 168.666 113.014 L 164.066 113.014 L 166.396 99.004 L 165.376 97.994 L 172.016 97.994 Z";
    private static final String CORNER_TEXT_DATA = "M 152.706 14.146 L 146.226 14.146 L 146.566 11.876 L 138.806 11.876 L 136.926 24.516 L 144.696 24.516 L 145.106 20.626 L 143.216 17.386 L 151.966 17.386 L 150.476 27.756 L 132.306 27.756 Q 131.836 27.756 131.426 27.586 Q 131.026 27.416 130.746 27.116 Q 130.466 26.816 130.366 26.396 Q 130.276 25.966 130.426 25.496 L 133.026 8.636 L 153.456 8.636 L 152.706 14.146 Z M 179.526 8.636 L 178.386 16.406 L 176.396 17.696 L 177.996 19.006 L 176.886 25.496 Q 176.636 26.476 175.946 27.116 Q 175.266 27.756 174.276 27.756 L 156.106 27.756 L 158.726 9.926 L 157.766 8.636 L 179.526 8.636 Z M 164.866 11.876 L 164.446 16.066 L 171.916 16.066 L 172.626 11.876 L 164.866 11.876 Z M 163.746 19.306 L 162.996 24.516 L 170.766 24.516 L 171.506 19.306 L 163.746 19.306 Z";

    private static final int COLOR_TEAL      = 0xFF00E5FF;
    private static final int COLOR_TEAL_DARK = 0xFF033F4C;
    private static final int COLOR_CYAN      = 0xFF00E5FF;

    private static final int  BACK_ALPHA        = 70;
    private static final int  FRONT_ALPHA       = 255;
    private static final long SWEEP_DURATION_MS = 1700L;

    private final Path baselinePath   = new Path();
    private final Path overlinePath   = new Path();
    private final Path movingLinePath = new Path();
    private final Path staticLinePath = new Path();
    private final Path mainTextPath   = new Path();
    private final Path cornerTextPath = new Path();

    private final Paint baselinePaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint overlinePaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint staticLinePaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mainTextPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint cornerTextPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint movingTrackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint movingFillPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final LinearGradient movingFlowGradient = new LinearGradient(
            0f, 0f, 130f, 0f,
            new int[]{0x0000E5FF, 0xFF00E5FF, 0xFF00E5FF, 0x0000E5FF},
            new float[]{0f, 0.25f, 0.75f, 1f},
            Shader.TileMode.CLAMP
    );
    private final Matrix flowMatrix = new Matrix();
    private ComposeShader combinedMovingShader;
    private final LinearGradient depthCyanShader = getDepthShader(COLOR_CYAN);

    private final Matrix viewportMatrix = new Matrix();
    private ValueAnimator sweepAnimator;

    public RamView(Context context) { super(context); init(); }
    public RamView(Context context, AttributeSet attrs) { super(context, attrs); init(); }
    public RamView(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); init(); }

    private void init() {
        parsePathData(BASELINE_DATA,    baselinePath);
        parsePathData(OVERLINE_DATA,    overlinePath);
        parsePathData(MOVING_LINE_DATA, movingLinePath);
        parsePathData(STATIC_LINE_DATA, staticLinePath);
        parsePathData(MAIN_TEXT_DATA,   mainTextPath);
        parsePathData(CORNER_TEXT_DATA, cornerTextPath);

        configurePaints();
        initShaders();
        setupSweepAnimator();
    }

    private void configurePaints() {
        baselinePaint.setStyle(Paint.Style.FILL);
        staticLinePaint.setStyle(Paint.Style.FILL);
        mainTextPaint.setStyle(Paint.Style.FILL);
        movingTrackPaint.setStyle(Paint.Style.FILL);
        movingFillPaint.setStyle(Paint.Style.FILL);

        cornerTextPaint.setStyle(Paint.Style.FILL);
        cornerTextPaint.setColor(0xFFFFFFFF);

        overlinePaint.setStyle(Paint.Style.STROKE);
        overlinePaint.setStrokeWidth(1.3377f);
        overlinePaint.setStrokeCap(Paint.Cap.ROUND);
        overlinePaint.setStrokeJoin(Paint.Join.ROUND);
    }

    private void initShaders() {
        baselinePaint.setShader(getDepthShader(COLOR_TEAL));
        overlinePaint.setShader(getDepthShader(COLOR_TEAL_DARK));
        staticLinePaint.setShader(getDepthShader(COLOR_TEAL));
        mainTextPaint.setShader(getDepthShader(COLOR_TEAL));

        movingTrackPaint.setShader(new LinearGradient(
                0f, 0f, 0f, VIEWPORT_HEIGHT,
                withAlpha(COLOR_TEAL_DARK, 140),
                withAlpha(COLOR_TEAL, 200),
                Shader.TileMode.CLAMP));

        combinedMovingShader = new ComposeShader(movingFlowGradient, depthCyanShader, PorterDuff.Mode.MULTIPLY);
        movingFillPaint.setShader(combinedMovingShader);
    }

    private LinearGradient getDepthShader(int baseColor) {
        return new LinearGradient(
                0f, 0f, 0f, VIEWPORT_HEIGHT,
                withAlpha(baseColor, BACK_ALPHA),
                withAlpha(baseColor, FRONT_ALPHA),
                Shader.TileMode.CLAMP);
    }

    private static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    private void setupSweepAnimator() {
        // -150f ke VIEWPORT_WIDTH + 150f untuk menyapu dari KIRI ke KANAN penuh secara kontinu
        sweepAnimator = ValueAnimator.ofFloat(-150f, VIEWPORT_WIDTH + 150f);
        sweepAnimator.setDuration(SWEEP_DURATION_MS);
        sweepAnimator.setRepeatCount(ValueAnimator.INFINITE);
        sweepAnimator.setInterpolator(new LinearInterpolator());
        sweepAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                flowMatrix.setTranslate(progress, 0f);
                movingFlowGradient.setLocalMatrix(flowMatrix);
                movingFillPaint.setShader(combinedMovingShader);
                invalidate();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (sweepAnimator != null && !sweepAnimator.isStarted()) {
            sweepAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (sweepAnimator != null) sweepAnimator.cancel();
        super.onDetachedFromWindow();
    }

    public void setBaselineAlpha(int alpha)    { baselinePaint.setAlpha(clamp(alpha));    invalidate(); }
    public void setOverlineAlpha(int alpha)    { overlinePaint.setAlpha(clamp(alpha));    invalidate(); }
    public void setStaticLineAlpha(int alpha)  { staticLinePaint.setAlpha(clamp(alpha));  invalidate(); }
    public void setMainTextAlpha(int alpha)    { mainTextPaint.setAlpha(clamp(alpha));    invalidate(); }
    public void setCornerTextAlpha(int alpha)  { cornerTextPaint.setAlpha(clamp(alpha));  invalidate(); }
    public void setMovingTrackAlpha(int alpha) { movingTrackPaint.setAlpha(clamp(alpha)); invalidate(); }
    public void setCometAlpha(int alpha)       { movingFillPaint.setAlpha(clamp(alpha));  invalidate(); }

    public void setSweepDuration(long ms) {
        if (sweepAnimator != null && ms > 0) sweepAnimator.setDuration(ms);
    }

    private static int clamp(int v) { return v < 0 ? 0 : (v > 255 ? 255 : v); }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float density = getResources().getDisplayMetrics().density;
        int desiredWidth = (int) (VIEWPORT_WIDTH * density);
        int widthPx = resolveSize(desiredWidth, widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightPx;
        if (heightMode == MeasureSpec.EXACTLY) {
            heightPx = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            heightPx = Math.round(widthPx * (VIEWPORT_HEIGHT / VIEWPORT_WIDTH));
            if (heightMode == MeasureSpec.AT_MOST) {
                heightPx = Math.min(heightPx, MeasureSpec.getSize(heightMeasureSpec));
            }
        }
        setMeasuredDimension(widthPx, heightPx);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewportMatrix.setScale(w / VIEWPORT_WIDTH, h / VIEWPORT_HEIGHT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.concat(viewportMatrix);

        canvas.drawPath(baselinePath,   baselinePaint);
        canvas.drawPath(overlinePath,   overlinePaint);
        canvas.drawPath(staticLinePath, staticLinePaint);
        canvas.drawPath(mainTextPath,   mainTextPaint);

        // Track background moving line
        canvas.drawPath(movingLinePath, movingTrackPaint);

        // Gambar full path moving line dengan clipping & animasi gradient menyapu dari Kiri -> Kanan
        canvas.save();
        canvas.clipPath(movingLinePath);
        canvas.drawPath(movingLinePath, movingFillPaint);
        canvas.restore();

        canvas.drawPath(cornerTextPath, cornerTextPaint);

        canvas.restore();
    }

    private static void parsePathData(String pathData, Path outPath) {
        outPath.reset();
        if (pathData == null || pathData.trim().isEmpty()) return;

        Pattern pattern = Pattern.compile("([MLCQZ])([^MLCQZ]*)");
        Matcher matcher = pattern.matcher(pathData);
        while (matcher.find()) {
            char cmd = matcher.group(1).charAt(0);
            float[] args = parseNumbers(matcher.group(2));
            switch (cmd) {
                case 'M': if (args.length >= 2) outPath.moveTo(args[0], args[1]); break;
                case 'L': if (args.length >= 2) outPath.lineTo(args[0], args[1]); break;
                case 'C': if (args.length >= 6) outPath.cubicTo(args[0], args[1], args[2], args[3], args[4], args[5]); break;
                case 'Q': if (args.length >= 4) outPath.quadTo(args[0], args[1], args[2], args[3]); break;
                case 'Z': outPath.close(); break;
            }
        }
    }

    private static float[] parseNumbers(String raw) {
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) return new float[0];
        String[] tokens = trimmed.split("[\\s,]+");
        float[] numbers = new float[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            numbers[i] = Float.parseFloat(tokens[i]);
        }
        return numbers;
    }
}