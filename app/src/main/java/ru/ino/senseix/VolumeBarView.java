package ru.ino.senseix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
* VolumeBarView
*
* Custom View (extends View, TANPA androidx / appcompat) yang menggambar
* SATU path saja dari "vd_left.xml". Path ini bisa ditampilkan dalam dua arah:
*
*   - leftMode()  -> path asli (menghadap kiri)
*   - rightMode() -> path di-mirror horizontal (menghadap kanan)
*
* Pengisian progress berjalan dari BAWAH ke ATAS (seperti volume bar vertikal),
* warna terisi = biru, sisanya = warna background #323232. Bisa digeser (drag).
*/
public class VolumeBarView extends View {
	
	public static final int MODE_LEFT = 0;
	public static final int MODE_RIGHT = 1;
	
	// ---------- Ukuran asli viewport dari vd_left.xml ----------
	private static final float VIEWPORT_WIDTH = 152f;
	private static final float VIEWPORT_HEIGHT = 304f;
	
	// ---------- Public config ----------
	private int max = 100;
	private int progress = 0;
	private int mode = MODE_LEFT;
	
	private int progressColor = Color.parseColor("#ffffff"); // biru
	private int trackColor = Color.parseColor("#323232");    // background belum terisi
	
	public interface OnProgressChangeListener {
		void onProgressChanged(int progress, int max);
	}
	
	private OnProgressChangeListener onProgressChangeListener;
	
	// ---------- Paint & Path ----------
	private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private final Path basePath = new Path();   // path asli (mode kiri)
	private final Path drawPath = new Path();   // path yang benar-benar digambar (kiri atau hasil mirror)
	
	private final Matrix mirrorMatrix = new Matrix();
	private final RectF clipRect = new RectF();
	
	public VolumeBarView(Context context) {
		super(context);
		init();
	}
	
	public VolumeBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public VolumeBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private void init() {
		backgroundPaint.setStyle(Paint.Style.FILL);
		backgroundPaint.setColor(trackColor);
		
		progressPaint.setStyle(Paint.Style.FILL);
		progressPaint.setColor(progressColor);
		
		buildBasePath();
		applyMode(); // isi drawPath sesuai mode default (LEFT)
	}
	
	// ---------- Mode arah view ----------
	
	/** Tampilkan path dalam orientasi asli (menghadap kiri) */
	public void leftMode() {
		if (mode != MODE_LEFT) {
			mode = MODE_LEFT;
			applyMode();
			invalidate();
		}
	}
	
	/** Tampilkan path yang sudah di-mirror horizontal (menghadap kanan) */
	public void rightMode() {
		if (mode != MODE_RIGHT) {
			mode = MODE_RIGHT;
			applyMode();
			invalidate();
		}
	}
	
	public int getMode() {
		return mode;
	}
	
	private void applyMode() {
		drawPath.reset();
		if (mode == MODE_RIGHT) {
			mirrorHorizontal(basePath, VIEWPORT_WIDTH, drawPath);
		} else {
			drawPath.addPath(basePath);
		}
	}
	
	// ---------- Getter / Setter ----------
	
	public int getMax() {
		return max;
	}
	
	public void setMax(int max) {
		this.max = max > 0 ? max : 1;
		setProgress(progress); // re-clamp
		invalidate();
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int progress) {
		int clamped = Math.max(0, Math.min(progress, max));
		if (this.progress != clamped) {
			this.progress = clamped;
			invalidate();
			if (onProgressChangeListener != null) {
				onProgressChangeListener.onProgressChanged(this.progress, this.max);
			}
		}
	}
	
	public int getProgressColor() {
		return progressColor;
	}
	
	/** Warna saat terisi penuh (default biru) */
	public void setProgressColor(int color) {
		this.progressColor = color;
		progressPaint.setColor(color);
		invalidate();
	}
	
	public int getTrackColor() {
		return trackColor;
	}
	
	/** Warna background bagian belum terisi (default #323232) */
	public void setTrackColor(int color) {
		this.trackColor = color;
		backgroundPaint.setColor(color);
		invalidate();
	}
	
	public void setOnProgressChangeListener(OnProgressChangeListener listener) {
		this.onProgressChangeListener = listener;
	}
	
	// ---------- Path building ----------
	
	/**
* Bangun path asli langsung dari data path vd_left.xml
* (15 trapesium, tiap trapesium 4 titik lalu close).
*/	
	private void buildBasePath() {
		basePath.reset();
		
		trapezoid(0f, 12.79f, 0f, 0f, 152.7f, 0f, 146.67f, 12.79f);
		trapezoid(0.07f, 33.82f, 0.07f, 21.44f, 143.72f, 21.44f, 138.5f, 33.82f);
		trapezoid(0.1f, 54.5f, 0.1f, 41.88f, 135.07f, 41.88f, 129.72f, 54.5f);
		trapezoid(0.04f, 75.19f, 0.04f, 62.6f, 126.26f, 62.6f, 121.18f, 75.19f);
		trapezoid(0.09f, 95.9f, 0.09f, 83.28f, 118.37f, 83.28f, 113.12f, 95.9f);
		trapezoid(0.1f, 116.59f, 0.1f, 104f, 109.69f, 104f, 104.07f, 116.59f);
		trapezoid(0.08f, 137.78f, 0.08f, 124.92f, 100.64f, 124.92f, 95.08f, 137.78f);
		trapezoid(0.04f, 158.98f, 0.04f, 146.12f, 91.07f, 146.12f, 85.58f, 158.98f);
		trapezoid(0.04f, 180.08f, 0.04f, 167.21f, 82.66f, 167.21f, 77.24f, 180.08f);
		trapezoid(0.04f, 200.24f, 0.04f, 187.41f, 74.19f, 187.41f, 68.6f, 200.24f);
		trapezoid(0.04f, 220.45f, 0.04f, 207.58f, 65.79f, 207.58f, 60.47f, 220.45f);
		trapezoid(0.04f, 241.64f, 0.04f, 228.78f, 56.53f, 228.78f, 51.11f, 241.64f);
		trapezoid(0.04f, 262.09f, 0.04f, 249.26f, 48.12f, 249.26f, 42.74f, 262.09f);
		trapezoid(0.04f, 283.32f, 0.04f, 270.46f, 39.38f, 270.46f, 34.51f, 283.32f);
		trapezoid(0.04f, 304.24f, 0.04f, 291.38f, 30.77f, 291.38f, 25.72f, 304.24f);
	}
	
	private void trapezoid(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		basePath.moveTo(x1, y1);
		basePath.lineTo(x2, y2);
		basePath.lineTo(x3, y3);
		basePath.lineTo(x4, y4);
		basePath.close();
	}
	
	/**
* Method mirror horizontal: transform "source" ke "dest" dengan membalik sumbu X.
*/	
	private void mirrorHorizontal(Path source, float viewportWidth, Path dest) {
		mirrorMatrix.reset();
		mirrorMatrix.setScale(-1f, 1f);               // balik horizontal
		mirrorMatrix.postTranslate(viewportWidth, 0f); // geser balik ke area positif
		source.transform(mirrorMatrix, dest);
	}
	
	// ---------- Measure & Draw ----------
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		float density = getResources().getDisplayMetrics().density;
		int defaultWidth = (int) (VIEWPORT_WIDTH * density);
		int defaultHeight = (int) (VIEWPORT_HEIGHT * density);
		
		int w = resolveSize(defaultWidth, widthMeasureSpec);
		int h = resolveSize(defaultHeight, heightMeasureSpec);
		setMeasuredDimension(w, h);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (getWidth() == 0 || getHeight() == 0) return;
		
		float scaleX = getWidth() / VIEWPORT_WIDTH;
		float scaleY = getHeight() / VIEWPORT_HEIGHT;
		
		canvas.save();
		canvas.scale(scaleX, scaleY);
		
		// 1. Gambar seluruh bentuk dengan warna track/background dulu
		canvas.drawPath(drawPath, backgroundPaint);
		
		// 2. Isi progress dari BAWAH ke ATAS
		float fraction = (float) progress / (float) max;
		float filledHeight = VIEWPORT_HEIGHT * fraction;
		float top = VIEWPORT_HEIGHT - filledHeight;
		
		clipRect.set(0f, top, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		
		canvas.save();
		canvas.clipRect(clipRect);
		canvas.drawPath(drawPath, progressPaint);
		canvas.restore();
		
		canvas.restore();
	}
	
	// ---------- Touch (drag seperti volume bar, arah vertikal) ----------
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
			if (getParent() != null) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			updateProgressFromTouch(event.getY());
			return true;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			if (getParent() != null) {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			return true;
			default:
			return super.onTouchEvent(event);
		}
	}
	
	private void updateProgressFromTouch(float touchY) {
		if (getHeight() == 0) return;
		// touch di paling bawah -> progress penuh, touch di paling atas -> progress 0
		float fraction = 1f - (touchY / getHeight());
		if (fraction < 0f) fraction = 0f;
		if (fraction > 1f) fraction = 1f;
		setProgress(Math.round(fraction * max));
	}
}
