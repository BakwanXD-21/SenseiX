package ru.ino.senseix;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.view.View;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundHelper {
	
	public enum GradientDirection { LEFT_RIGHT, RIGHT_LEFT, TOP_BOTTOM, BOTTOM_TOP, TL_BR, BL_TR }
	
	private static final LruCache<String, Bitmap> sCache = new LruCache<String, Bitmap>(20 * 1024 * 1024) {
		@Override
		protected int sizeOf(String key, Bitmap value) {
			return value.getByteCount();
		}
	};
	
	private static final ExecutorService sExecutor = Executors.newFixedThreadPool(3);
	private static final Handler sMainHandler = new Handler(Looper.getMainLooper());
	
	// -------------------------------------------------------------------------
	
	public static Bitmap centerCrop(Bitmap src, int targetW, int targetH) {
		Bitmap out = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(out);
		
		// Perbaikan: Menghapus '* 1.2f' agar skala gambar presisi dan tidak over-zoom
		float scale = Math.max((float) targetW / src.getWidth(), (float) targetH / src.getHeight()) * 0.90f;
		float scaledW = src.getWidth() * scale;
		float scaledH = src.getHeight() * scale;
		
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		matrix.postTranslate((targetW - scaledW) * 0.5f, (targetH - scaledH) * 0.5f);
		
		canvas.drawBitmap(src, matrix, new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		return out;
	}
	
	public static Bitmap applyGradientErase(Bitmap src, int alphaStart, int alphaEnd, GradientDirection dir) {
		int w = src.getWidth(), h = src.getHeight();
		Bitmap out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(out);
		canvas.drawBitmap(src, 0, 0, null);
		
		float x0 = 0, y0 = 0, x1 = 0, y1 = 0;
		if      (dir == GradientDirection.LEFT_RIGHT) { x1 = w; }
		else if (dir == GradientDirection.RIGHT_LEFT) { x0 = w; }
		else if (dir == GradientDirection.TOP_BOTTOM) { y1 = h; }
		else if (dir == GradientDirection.BOTTOM_TOP) { y0 = h; }
		else if (dir == GradientDirection.TL_BR)      { x1 = w; y1 = h; }
		else if (dir == GradientDirection.BL_TR)      { x1 = w; y0 = h; }
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setShader(new LinearGradient(x0, y0, x1, y1,
				(alphaStart << 24), (alphaEnd << 24), Shader.TileMode.CLAMP));
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		canvas.drawRect(0, 0, w, h, paint);
		return out;
	}
	
	// -------------------------------------------------------------------------
	
	private static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) return null;
		if (drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();
		
		int w = drawable.getIntrinsicWidth()  <= 0 ? 512 : drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight() <= 0 ? 512 : drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}
	
	private static boolean isAssetPath(Context c, String input) {
		if (c == null || input == null || input.isEmpty()) return false;
		try (InputStream is = c.getAssets().open(input)) { return true; }
		catch (Exception e) { return false; }
	}
	
	public static Bitmap getFrom(Context c, String source) {
		if (source == null || source.isEmpty()) return null;
		
		// 1. File Path
		if (source.startsWith("/")) {
			return new File(source).exists() ? BitmapFactory.decodeFile(source) : null;
		}
		
		// 2. Assets Folder
		if (isAssetPath(c, source)) {
			try (InputStream is = c.getAssets().open(source)) {
				return BitmapFactory.decodeStream(is);
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		// 3. Resources / Drawable
		if (c != null) {
			String resName = source.contains("/") ? source.substring(source.lastIndexOf("/") + 1) : source;
			resName = resName.contains(".") ? resName.substring(0, resName.indexOf(".")) : resName;
			
			int resId = c.getResources().getIdentifier(resName, "drawable", c.getPackageName());
			if (resId != 0) return drawableToBitmap(c.getResources().getDrawable(resId, c.getTheme()));
		}
		
		// 4. Package Name (app icon)
		try { return drawableToBitmap(c.getPackageManager().getApplicationIcon(source)); }
		catch (Exception e) { return null; }
	}
	
	// -------------------------------------------------------------------------
	
	public static void clearCache() {
		sCache.evictAll();
	}
	
	// -------------------------------------------------------------------------
	
	public static void setBackground(Context c, View v, String source) {
		setBackgroundWithGradient(c, v, source, 255, 255, GradientDirection.TOP_BOTTOM);
	}
	
	public static void setBackgroundWithGradient(final Context c, final View v,
												 final String source, final int alphaStart, final int alphaEnd,
												 final GradientDirection direction) {
		
		if (v == null || source == null || source.isEmpty()) return;
		
		int w = v.getWidth(), h = v.getHeight();
		if (w == 0 || h == 0) {
			v.post(() -> setBackgroundWithGradient(c, v, source, alphaStart, alphaEnd, direction));
			return;
		}
		
		final String cacheKey = source + "|" + w + "|" + h + "|" + alphaStart + "|" + alphaEnd + "|" + direction.name();
		
		v.setTag(cacheKey);
		
		Bitmap cached = sCache.get(cacheKey);
		if (cached != null && !cached.isRecycled()) {
			v.setBackground(new BitmapDrawable(c.getResources(), cached));
			return;
		}
		
		sExecutor.submit(() -> {
			Bitmap src     = null;
			Bitmap cropped = null;
			Bitmap result  = null;
			try {
				src = getFrom(c, source);
				if (src == null) return;
				
				cropped = centerCrop(src, w, h);
				result  = applyGradientErase(cropped, alphaStart, alphaEnd, direction);
				
				src = null;
				cropped = null;
				
				sCache.put(cacheKey, result);
				
				final Bitmap finalResult = result;
				sMainHandler.post(() -> {
					if (v == null) return;
					
					if (cacheKey.equals(v.getTag())) {
						v.setBackground(new BitmapDrawable(c.getResources(), finalResult));
					}
				});
				
			} catch (Exception e) {
				e.printStackTrace();
				if (src     != null && !src.isRecycled())     src.recycle();
				if (cropped != null && !cropped.isRecycled()) cropped.recycle();
				if (result  != null && !result.isRecycled())  result.recycle();
			}
		});
	}
}
