package ru.ino.senseix;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.BlurMaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeftGhzView extends LinearLayout {
	private Paint rootBgPaint;
	private Paint solidBgPaint;
	private Paint topGrayBgPaint;
	private Paint bgPaint;
	private Paint barBgPaint;
	private Paint barFillPaint;
	private Paint barGlowPaint;
	
	private Path originalRootBgPath;
	private Path scaledRootBgPath;
	
	private Path originalSolidBgPath;
	private Path scaledSolidBgPath;
	
	private float clipHeightPercent = 0.18f;
	
	private List<Path> originalBarPaths;
	private List<Path> scaledBarPaths;
	
	private float currentPercentage = 0f;
	private Handler handler;
	private Runnable updateRunnable;
	private ValueAnimator smoothAnimator;
	private float scaledClipY = 0f;
	
	private boolean isFirstLoad = true;
	private boolean isAnimatingToFull = false;
	
	public LeftGhzView(Context context) {
		super(context);
		init();
	}
	
	public LeftGhzView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		setWillNotDraw(false); 
		
		// 1. Layer Root/Outer Transparan
		rootBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		rootBgPaint.setColor(Color.parseColor("#96000000"));
		rootBgPaint.setStyle(Paint.Style.FILL);
		
		// 2. Layer Inside Frame Solid (70% bagian bawah)
		solidBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		solidBgPaint.setColor(Color.parseColor("#212121"));
		solidBgPaint.setStyle(Paint.Style.FILL);
		
		// 3. Layer Top Abu-abu
		topGrayBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		topGrayBgPaint.setColor(Color.parseColor("#272727"));
		topGrayBgPaint.setStyle(Paint.Style.FILL);
		
		bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bgPaint.setColor(0xFF0C5161);
		bgPaint.setStyle(Paint.Style.FILL);
		
		barBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barBgPaint.setColor(0xFF212121); 
		barBgPaint.setStyle(Paint.Style.FILL);
		
		barFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barFillPaint.setColor(0xFF00E5FF);
		barFillPaint.setStyle(Paint.Style.FILL);
		
		barGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		barGlowPaint.setColor(0xFF00E5FF);
		barGlowPaint.setStyle(Paint.Style.FILL);
		barGlowPaint.setMaskFilter(new BlurMaskFilter(12f, BlurMaskFilter.Blur.NORMAL));
		
		// Path 1: Root Outer Frame
		originalRootBgPath = new Path();
		originalRootBgPath.moveTo(1285.61f, 0.15f);
		originalRootBgPath.lineTo(0f, 0.15f);
		originalRootBgPath.lineTo(0f, 2052.96f);
		originalRootBgPath.lineTo(906.73f, 2053.11f);
		originalRootBgPath.lineTo(1601.13f, 710.51f);
		originalRootBgPath.cubicTo(1615.68f, 681.22f, 1620.21f, 625.63f, 1604.32f, 596.85f);
		originalRootBgPath.lineTo(1285.61f, 0.15f);
		originalRootBgPath.close();
		
		// Path 2: Solid Frame Inside
		originalSolidBgPath = new Path();
		originalSolidBgPath.moveTo(1134.75f, 0.15f);
		originalSolidBgPath.lineTo(0f, 0.15f);
		originalSolidBgPath.lineTo(0f, 2052.96f);
		originalSolidBgPath.lineTo(839.43f, 2052.96f);
		originalSolidBgPath.lineTo(1429.99f, 712.61f);
		originalSolidBgPath.cubicTo(1442.94f, 683.31f, 1441.71f, 625.27f, 1427.59f, 596.48f);
		originalSolidBgPath.lineTo(1134.75f, 0.15f);
		originalSolidBgPath.close();
		
		originalBarPaths = new ArrayList<>();
		
		Path p0 = new Path();
		p0.moveTo(1318.51f, 108.03f); p0.lineTo(1211.18f, 108.36f); p0.lineTo(1157.49f, 0f); p0.lineTo(1264.81f, 0.32f); p0.lineTo(1318.51f, 108.03f); p0.close();
		originalBarPaths.add(p0);
		
		Path p1 = new Path();
		p1.moveTo(1381.1f, 229.22f); p1.lineTo(1273.77f, 232.36f); p1.lineTo(1223.09f, 133.31f); p1.lineTo(1330.41f, 130.49f); p1.lineTo(1381.1f, 229.22f); p1.close();
		originalBarPaths.add(p1);
		
		Path p2 = new Path();
		p2.moveTo(1446.69f, 351.88f); p2.lineTo(1336.36f, 355.02f); p2.lineTo(1285.68f, 252.96f); p2.lineTo(1393f, 250.13f); p2.lineTo(1446.69f, 351.88f); p2.close();
		originalBarPaths.add(p2);
		
		Path p3 = new Path();
		p3.moveTo(1507.49f, 472.43f); p3.lineTo(1399.01f, 474.67f); p3.lineTo(1348.33f, 375.62f); p3.lineTo(1455.65f, 372.8f); p3.lineTo(1507.49f, 472.43f); p3.close();
		originalBarPaths.add(p3);
		
		Path p4 = new Path();
		p4.moveTo(1515.23f, 491.54f); p4.lineTo(1407.91f, 495.46f); p4.lineTo(1455.65f, 597.2f); p4.lineTo(1563.23f, 592.39f); p4.lineTo(1515.23f, 491.54f); p4.close();
		originalBarPaths.add(p4);
		
		Path p5 = new Path();
		p5.moveTo(1465.7f, 660.01f);
		p5.cubicTo(1466.08f, 643.52f, 1459.49f, 614.33f, 1459.49f, 614.33f);
		p5.lineTo(1569.18f, 610.55f);
		p5.cubicTo(1569.18f, 610.55f, 1579.61f, 644.87f, 1577.82f, 662.96f);
		p5.cubicTo(1575.97f, 681.89f, 1561.76f, 718.9f, 1561.76f, 718.9f);
		p5.lineTo(1454.69f, 717.75f);
		p5.cubicTo(1454.69f, 717.75f, 1465.32f, 678.17f, 1465.7f, 660.01f); p5.close();
		originalBarPaths.add(p5);
		
		Path p6 = new Path();
		p6.moveTo(1400.29f, 837.84f); p6.lineTo(1447.2f, 736.42f); p6.lineTo(1555.49f, 737.32f); p6.lineTo(1503.14f, 837.84f); p6.lineTo(1400.29f, 837.84f); p6.close();
		originalBarPaths.add(p6);
		
		Path p7 = new Path();
		p7.moveTo(1346.15f, 960.95f); p7.lineTo(1392.17f, 856.83f); p7.lineTo(1494.11f, 858.63f); p7.lineTo(1440.87f, 960.95f); p7.lineTo(1346.15f, 960.95f); p7.close();
		originalBarPaths.add(p7);
		
		Path p8 = new Path();
		p8.moveTo(1292.01f, 1082.27f); p8.lineTo(1338.03f, 978.15f); p8.lineTo(1431.84f, 979.94f); p8.lineTo(1377.7f, 1083.17f); p8.lineTo(1292.01f, 1082.27f); p8.close();
		originalBarPaths.add(p8);
		
		Path p9 = new Path();
		p9.moveTo(1367.78f, 1101.26f); p9.lineTo(1282.09f, 1100.36f); p9.lineTo(1238.77f, 1201.79f); p9.lineTo(1315.43f, 1205.38f); p9.lineTo(1367.78f, 1101.26f); p9.close();
		originalBarPaths.add(p9);
		
		Path p10 = new Path();
		p10.moveTo(1184.63f, 1325.8f); p10.lineTo(1230.64f, 1219.88f); p10.lineTo(1305.58f, 1223.47f); p10.lineTo(1254.13f, 1327.59f); p10.lineTo(1184.63f, 1325.8f); p10.close();
		originalBarPaths.add(p10);
		
		Path p11 = new Path();
		p11.moveTo(1129.59f, 1445.38f); p11.lineTo(1177.39f, 1341.26f); p11.lineTo(1244.21f, 1343.95f); p11.lineTo(1190.96f, 1446.28f); p11.lineTo(1129.59f, 1445.38f); p11.close();
		originalBarPaths.add(p11);
		
		Path p12 = new Path();
		p12.moveTo(1077.3f, 1565.79f); p12.lineTo(1123.32f, 1463.47f); p12.lineTo(1181.11f, 1465.26f); p12.lineTo(1129.65f, 1566.69f); p12.lineTo(1077.3f, 1565.79f); p12.close();
		originalBarPaths.add(p12);
		
		Path p13 = new Path();
		p13.moveTo(1024.06f, 1688.01f); p13.lineTo(1070.97f, 1586.58f); p13.lineTo(1119.73f, 1586.58f); p13.lineTo(1068.28f, 1689.8f); p13.lineTo(1024.06f, 1688.01f); p13.close();
		originalBarPaths.add(p13);
		
		Path p14 = new Path();
		p14.moveTo(1059.26f, 1710.65f); p14.lineTo(1016.83f, 1707.96f); p14.lineTo(970.81f, 1812.08f); p14.lineTo(1006.01f, 1814.77f); p14.lineTo(1059.26f, 1710.65f); p14.close();
		originalBarPaths.add(p14);
		
		Path p15 = new Path();
		p15.moveTo(996.09f, 1831.07f); p15.lineTo(963.58f, 1827.48f); p15.lineTo(915.77f, 1931.6f); p15.lineTo(943.74f, 1934.29f); p15.lineTo(996.09f, 1831.07f); p15.close();
		originalBarPaths.add(p15);
		
		Path p16 = new Path();
		p16.moveTo(857.09f, 2052.01f); p16.lineTo(908.54f, 1948.79f); p16.lineTo(934.65f, 1950.59f); p16.lineTo(885.06f, 2052.91f); p16.lineTo(857.09f, 2052.01f); p16.close();
		originalBarPaths.add(p16);
		
		Collections.sort(originalBarPaths, new Comparator<Path>() {
			@Override
			public int compare(Path p1, Path p2) {
				android.graphics.RectF r1 = new android.graphics.RectF();
				android.graphics.RectF r2 = new android.graphics.RectF();
				p1.computeBounds(r1, true);
				p2.computeBounds(r2, true);
				return Float.compare(r1.top, r2.top);
			}
		});
		Collections.reverse(originalBarPaths);
		
		scaledRootBgPath = new Path();
		scaledSolidBgPath = new Path();
		scaledBarPaths = new ArrayList<>();
		for (int i = 0; i < originalBarPaths.size(); i++) {
			scaledBarPaths.add(new Path());
		}
		
		startRealtimeMonitor();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w == 0 || h == 0) return;
		
		float scaleX = (float) w / 1615.83f;
		float scaleY = (float) h / 2053.11f;
		
		scaledClipY = h * clipHeightPercent;
		
		Matrix matrix = new Matrix();
		matrix.setScale(scaleX, scaleY);
		
		originalRootBgPath.transform(matrix, scaledRootBgPath);
		originalSolidBgPath.transform(matrix, scaledSolidBgPath);
		for (int i = 0; i < originalBarPaths.size(); i++) {
			originalBarPaths.get(i).transform(matrix, scaledBarPaths.get(i));
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// 1. Gambar Root Background Transparan
		canvas.drawPath(scaledRootBgPath, rootBgPaint);
		
		// 2. Gambar Solid Path FULL dengan warna Abu-abu
		canvas.drawPath(scaledSolidBgPath, topGrayBgPaint);
		
		// 3. Gambar Solid Path bagian bawah 70% dengan warna Hitam (Dengan Clipping)
		canvas.save();
		canvas.clipRect(0, scaledClipY, canvas.getWidth(), canvas.getHeight());
		canvas.drawPath(scaledSolidBgPath, solidBgPaint);
		canvas.restore();
		
		int totalBars = scaledBarPaths.size();
		float exactActiveIndex = totalBars * currentPercentage;
		
		// 4. Gambar background bar neon (gelap/unactive)
		for (int i = 0; i < totalBars; i++) {
			canvas.drawPath(scaledBarPaths.get(i), barBgPaint);
		}
		
		int middle = totalBars / 2;
		
		// 5. Gambar bar neon aktif dengan Gradasi Body + Gradasi Tip Pucuk Memudar
		for (int i = 0; i < totalBars; i++) {
			if (i >= exactActiveIndex) {
				continue;
			}
			
			Path barPath = scaledBarPaths.get(i);
			
			// Body Gradient (Terang di tengah, bertahap agak redup di bagian dasar/puncak frame)
			float distanceFromMiddle = Math.abs(i - middle);
			float maxDistance = Math.max(middle, totalBars - 1 - middle);
			float bodyFraction = 1.0f - (distanceFromMiddle / maxDistance) * 0.5f;
			
			// Tip Gradient (Memudar lembut pada 3 bar di pucuk nilai saat ini)
			float tipFade = 1.0f;
			float distanceToTip = exactActiveIndex - i;
			if (distanceToTip > 0 && distanceToTip < 3.0f) {
				tipFade = distanceToTip / 3.0f;
			} else if (distanceToTip <= 0) {
				tipFade = 0f;
			}
			
			int rawNeonAlpha = (int)(70 + (185 * bodyFraction));
			int rawFillAlpha = (int)(150 + (105 * bodyFraction));
			
			int finalNeonAlpha = (int)(rawNeonAlpha * tipFade);
			int finalFillAlpha = (int)(rawFillAlpha * tipFade);
			
			barGlowPaint.setAlpha(Math.min(255, Math.max(0, finalNeonAlpha)));
			barFillPaint.setAlpha(Math.min(255, Math.max(0, finalFillAlpha)));
			
			canvas.drawPath(barPath, barGlowPaint);
			canvas.drawPath(barPath, barFillPaint);
		}
	}
	
	private void startRealtimeMonitor() {
		if (isFirstLoad) {
			isFirstLoad = false;
			isAnimatingToFull = true;
			
			smoothAnimator = ValueAnimator.ofFloat(0f, 1.0f);
			smoothAnimator.setDuration(500);
			smoothAnimator.setInterpolator(new DecelerateInterpolator());
			smoothAnimator.addUpdateListener(animation -> {
				currentPercentage = (float) animation.getAnimatedValue();
				invalidate();
			});
			smoothAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(android.animation.Animator animation) {
					handler = new Handler(Looper.getMainLooper());
					handler.postDelayed(() -> {
						isAnimatingToFull = false;
						startMonitoringCPU();
					}, 120);
				}
			});
			smoothAnimator.start();
		}
	}
	
	private void startMonitoringCPU() {
		handler = new Handler(Looper.getMainLooper());
		updateRunnable = new Runnable() {
			@Override
			public void run() {
				int cpuMhz = GhzMonitor.getCpuSpeedMhz();
				float targetPercentage = Math.max(0.1f, Math.min(1.0f, (float) cpuMhz / 2800f));
				
				animateToPercentage(targetPercentage);
				handler.postDelayed(this, 1500);
			}
		};
		handler.post(updateRunnable);
	}
	
	private void animateToPercentage(float target) {
		if (smoothAnimator != null && smoothAnimator.isRunning()) {
			smoothAnimator.cancel();
		}
		smoothAnimator = ValueAnimator.ofFloat(currentPercentage, target);
		smoothAnimator.setDuration(1000);
		smoothAnimator.setInterpolator(new DecelerateInterpolator());
		smoothAnimator.addUpdateListener(animation -> {
			currentPercentage = (float) animation.getAnimatedValue();
			invalidate();
		});
		smoothAnimator.start();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (handler != null && updateRunnable != null) handler.removeCallbacks(updateRunnable);
		if (smoothAnimator != null) smoothAnimator.cancel();
	}
}