package ru.ino.senseix;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DynamicThemeHelper {
	
	private final Map<String, Integer> colorPalette = new HashMap<>();
	
	public DynamicThemeHelper() {
		useDefaultPalette();
	}
	
	public void useDefaultPalette() {
		// Default Aksen Biru Murni
		colorPalette.put("colorPrimary", Color.parseColor("#1058C8"));
		colorPalette.put("colorPrimaryBtn", Color.parseColor("#0D47A1")); 
		colorPalette.put("colorPrimaryDark", Color.parseColor("#0A3778")); 
		colorPalette.put("colorAccent", Color.parseColor("#4A88E3"));
		colorPalette.put("colorOnPrimary", Color.parseColor("#FFFFFF"));  
		colorPalette.put("colorOnPrimaryDark", Color.parseColor("#E3F2FD"));
		colorPalette.put("colorOnAccent", Color.parseColor("#FFFFFF"));
		colorPalette.put("colorAccentContainer", Color.parseColor("#1A365D"));
		colorPalette.put("colorOnAccentContainer", Color.parseColor("#D2E3FC"));
		colorPalette.put("colorTertiary", Color.parseColor("#00838F"));
		colorPalette.put("colorOnTertiary", Color.parseColor("#FFFFFF"));
		
		// Default Surface M3 Tonal (Menggunakan basis biru gelap yang disamarkan)
		colorPalette.put("colorSurface", Color.parseColor("#0E141D"));
		colorPalette.put("colorSurfaceContainer", Color.parseColor("#151D29"));
		colorPalette.put("colorSurfaceContainerHigh", Color.parseColor("#1C2635"));
		colorPalette.put("colorSecondaryContainer", Color.parseColor("#151D29"));
		colorPalette.put("colorOnSurface", Color.parseColor("#E2E2E6"));
		colorPalette.put("colorOnSurfaceVariant", Color.parseColor("#C4C6D0"));
		colorPalette.put("colorOutline", Color.parseColor("#2D3848"));
	}
	
	/**
* GENERATOR DINAMIS - M3 TONAL BACKGROUND FROM BASE COLOR
*/	
	public void generateFromBaseColor(int baseColor) {
		float[] hsv = new float[3];
		Color.colorToHSV(baseColor, hsv);
		
		float h = hsv[0]; // Mengambil rona (Hue) dari warna terpilih agar selaras
		float s = hsv[1];
		float v = hsv[2];
		
		// 1. STRUKTUR WARNA UTAMA (KOMPONEN AKTIF / TOMBOL)
		int primary = Color.HSVToColor(new float[]{ h, Math.min(s * 1.0f, 0.90f), Math.max(v * 1.0f, 0.85f) });
		int primaryBtn = Color.HSVToColor(new float[]{ h, Math.min(s * 1.05f, 0.95f), v * 0.75f }); 
		int primaryContainer = Color.HSVToColor(new float[]{ h, Math.min(s * 0.90f, 0.80f), Math.max(v * 0.35f, 0.20f) });
		
		// 2. RUMUS M3 TONAL BACKGROUND (Dibuat lebih berasa & terpancar warna dasarnya)
		// Saturation dinaikkan menjadi (10% - 16%) agar rona warna pilihan lebih hidup/berasa
		float tonalSaturation = Math.max(0.10f, Math.min(s * 0.25f, 0.16f));
		
		// Value/Brightness dinaikkan dikit agar tidak terlalu gelap pekat/mati
		float bgValueSurface = 0.09f;          // Dasar background (lebih berasa tonalnya)
		float bgValueContainer = 0.14f;        // Kotak menu (terlihat kontras)
		float bgValueContainerHigh = 0.19f;   // Kotak menu saat ditekan/di atas
		
		int surface = Color.HSVToColor(new float[]{ h, tonalSaturation, bgValueSurface });
		int surfaceContainer = Color.HSVToColor(new float[]{ h, tonalSaturation + 0.02f, bgValueContainer });
		int surfaceContainerHigh = Color.HSVToColor(new float[]{ h, tonalSaturation + 0.04f, bgValueContainerHigh });
		
		// Outline dibuat lebih menyala mengikuti warna tonal
		int outline = Color.HSVToColor(new float[]{ h, tonalSaturation + 0.10f, 0.28f });
		
		// 3. MASUKKAN KE PALETTE MAP
		colorPalette.put("colorPrimary", primary);
		colorPalette.put("colorPrimaryBtn", primaryBtn); 
		colorPalette.put("colorPrimaryDark", primaryContainer);
		
		colorPalette.put("colorAccent", primary);
		colorPalette.put("colorAccentContainer", surfaceContainer);
		
		colorPalette.put("colorTertiary", primary);
		
		colorPalette.put("colorSurface", surface);
		colorPalette.put("colorSurfaceContainer", surfaceContainer);
		colorPalette.put("colorSurfaceContainerHigh", surfaceContainerHigh);
		
		colorPalette.put("colorSecondaryContainer", surfaceContainer);
		colorPalette.put("colorOutline", outline);
		
		colorPalette.put("colorOnPrimary", getContrastColor(primary));
		colorPalette.put("colorOnPrimaryDark", Color.WHITE);
		
		colorPalette.put("colorOnAccent", getContrastColor(primary));
		colorPalette.put("colorOnAccentContainer", primary);
		
		// Warna teks adaptif cerah di atas background tonal
		colorPalette.put("colorOnSurface", Color.parseColor("#F1F3F5")); 
		colorPalette.put("colorOnSurfaceVariant", Color.HSVToColor(new float[]{ h, 0.05f, 0.75f }));
	}
	
	private int getContrastColor(int color) {
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		
		double luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255.0;
		return (luminance > 0.5) ? Color.parseColor("#121212") : Color.parseColor("#FFFFFF");
	}
	
	public int getColor(String key) {
		if (colorPalette.containsKey(key)) {
			Integer color = colorPalette.get(key);
			return color != null ? color : Color.TRANSPARENT;
		}
		return Color.TRANSPARENT;
	}
	
	public void setColorRipple(View view, int backgroundColor, float radiusDp) {
		float d = view.getResources().getDisplayMetrics().density;
		GradientDrawable bg = new GradientDrawable();
		bg.setColor(backgroundColor);
		bg.setCornerRadius(radiusDp * d);
		
		if (Build.VERSION.SDK_INT >= 21) {
			int rippleColor = (getContrastColor(backgroundColor) == Color.WHITE) 
			? Color.parseColor("#1AFFFFFF")  
			: Color.parseColor("#1A000000"); 
			
			RippleDrawable ripple = new RippleDrawable(
			ColorStateList.valueOf(rippleColor),
			bg,
			null
			);
			view.setBackground(ripple);
		} else {
			view.setBackground(bg);
		}
		view.setClickable(true);
		view.setFocusable(true);
		applyClipPath(view, radiusDp);
	}
	
	public static void applyClipPath(final View view, float radiusDp) {
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			final float radiusPx = radiusDp * view.getResources().getDisplayMetrics().density;
			view.setClipToOutline(true);
			view.setElevation(0f); // Tetap flat clean tanpa bayangan M3 tebal
			view.setOutlineProvider(new android.view.ViewOutlineProvider() {
				@Override
				public void getOutline(View v, android.graphics.Outline outline) {
					outline.setRoundRect(0, 0, v.getWidth(), v.getHeight(), radiusPx);
				}
			});
		}
	}
	
	public void applyDynamicColorToInoViews(View view) {
		if (view == null) return;
		
		String className = view.getClass().getName();
		int primary = getColor("colorPrimary");
		int surface = getColor("colorSurface");
		int outline = getColor("colorOutline");
		
		if (className.contains("InoBottomNavigation")) {
			ru.ino.senseix.InoBottomNavigation nav = (ru.ino.senseix.InoBottomNavigation) view;
			nav.setBarBackgroundColor(getColor("colorSurfaceContainer"));
			nav.setIndicatorColor(getColor("colorPrimaryDark")); 
			nav.setActiveColor(primary);
			nav.setInactiveColor(getColor("colorOnSurfaceVariant"));
			view.invalidate();
		} 
		else if (className.contains("InoTabLayout")) {
			ru.ino.senseix.InoTabLayout tab = (ru.ino.senseix.InoTabLayout) view;
			tab.setActiveColor(primary);
			tab.setInactiveColor(getColor("colorOnSurfaceVariant"));
			tab.setIndicatorColor(primary);
			view.invalidate();
		} 
		else if (className.contains("SliderRange")) {
			ru.ino.senseix.SliderRange sliderRange = (ru.ino.senseix.SliderRange) view;
			sliderRange.setTrackActiveColor(primary);
			sliderRange.setTrackInactiveColor(getColor("colorOutline"));
			sliderRange.setThumbColor(primary);
			sliderRange.setDotActiveColor(surface);
			sliderRange.setDotInactiveColor(outline);
			view.invalidate();
		} 
		else if (className.contains("MaterialCheckBox")) {
			ru.ino.senseix.MaterialCheckBox checkBox = (ru.ino.senseix.MaterialCheckBox) view;
			checkBox.setCheckedColor(primary);
			checkBox.setUncheckedColor(outline);
			view.invalidate();
		} 
		else if (className.contains("MaterialRadioButton")) {
			ru.ino.senseix.MaterialRadioButton radio = (ru.ino.senseix.MaterialRadioButton) view;
			radio.setCheckedColor(primary);
			radio.setUncheckedColor(outline);
			view.invalidate();
		} 
		else if (className.contains("InoProgressBar")) {
			ru.ino.senseix.InoProgressBar pBar = (ru.ino.senseix.InoProgressBar) view;
			pBar.setTrackActiveColor(primary);
			pBar.setTrackInactiveColor(getColor("colorOutline"));
			view.invalidate();
		} 
		else if (className.contains("InoLoadingIndicator")) {
			ru.ino.senseix.InoLoadingIndicator loading = (ru.ino.senseix.InoLoadingIndicator) view;
			loading.setColor(primary);
			view.invalidate();
		}
		else if (className.contains("MaterialSwitch")) {
			ru.ino.senseix.MaterialSwitch mSwitch = (ru.ino.senseix.MaterialSwitch) view;
			mSwitch.setActiveColor(primary);
			mSwitch.setThumbActiveColor(getColor("colorSurface"));
			mSwitch.setInactiveColor(Color.TRANSPARENT); 
			mSwitch.setStrokeActiveColor(primary);
			mSwitch.setStrokeInactiveColor(outline);
			view.invalidate();
		}
		else if (className.contains("InoInputLayout")) {
			ru.ino.senseix.InoInputLayout inoInput = (ru.ino.senseix.InoInputLayout) view;
			inoInput.setColors(primary, outline);
			if (inoInput.getEditText() != null) {
				applyToEditText(inoInput.getEditText());
			}
			view.invalidate();
		}
		else if (className.contains("SimpleSwipeRefreshLayout")) {
			ru.ino.senseix.SimpleSwipeRefreshLayout refreshLayout = (ru.ino.senseix.SimpleSwipeRefreshLayout) view;
			refreshLayout.setIndicatorBackgroundColor(getColor("colorSecondaryContainer"));
			refreshLayout.setProgressColor(getColor("colorPrimaryBtn"));
		}
	}
	
	public void applyToTextView(TextView textView, String textColorKey) {
		textView.setTextColor(getColor(textColorKey));
	}
	
	public void applyToScrollbar(final View view) {
		if (view == null) return;
		try {
			float dp = view.getResources().getDisplayMetrics().density;
			int primary = getColor("colorPrimary");
			int scrollbarColor = Color.argb(128, Color.red(primary), Color.green(primary), Color.blue(primary));
			
			final GradientDrawable thumb = new GradientDrawable();
			thumb.setShape(GradientDrawable.RECTANGLE);
			thumb.setColor(scrollbarColor);
			thumb.setCornerRadius(100f * dp);
			thumb.setSize((int)(4f * dp), (int)(4f * dp));
			
			view.setVerticalScrollBarEnabled(true);
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
				view.setVerticalScrollbarThumbDrawable(thumb);
				view.invalidate();
				return;
			}
			view.post(new Runnable() {
				@Override
				public void run() {
					try {
						Method awakenMethod = View.class.getDeclaredMethod("awakenScrollBars");
						awakenMethod.setAccessible(true);
						awakenMethod.invoke(view);
						
						Field cacheField = View.class.getDeclaredField("mScrollCache");
						cacheField.setAccessible(true);
						Object cache = cacheField.get(view);
						if (cache == null) return;
						
						Field scrollBarField = cache.getClass().getDeclaredField("scrollBar");
						scrollBarField.setAccessible(true);
						Object scrollBar = scrollBarField.get(cache);
						if (scrollBar == null) return;
						
						Field verticalThumbField = scrollBar.getClass().getDeclaredField("mVerticalThumb");
						verticalThumbField.setAccessible(true);
						verticalThumbField.set(scrollBar, thumb);
						view.invalidate();
					} catch (Throwable e) {
						Log.e("SCROLLBAR", "Error: " + e.getMessage());
					}
				}
			});
		} catch (Throwable e) {
			Log.e("SCROLLBAR", "Error: " + e.getMessage());
		}
	}
	
	public void applyToEditText(android.widget.EditText editText) {
		int primary = getColor("colorPrimary");
		int outline = getColor("colorOutline");
		int textColor = getColor("colorOnSurface");
		int hintColor = getColor("colorOnSurfaceVariant");
		
		editText.setTextColor(textColor);
		editText.setHintTextColor(hintColor);
		
		int highlightColor = (primary & 0x00FFFFFF) | 0x4D000000; 
		editText.setHighlightColor(highlightColor);
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			int[][] states = new int[][] {
				new int[] {android.R.attr.state_focused}, 
				new int[] {-android.R.attr.state_focused} 
			};
			int[] colors = new int[] { primary, outline };
			editText.setBackgroundTintList(new android.content.res.ColorStateList(states, colors));
		}
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
			android.graphics.drawable.Drawable cursor = editText.getTextCursorDrawable();
			if (cursor != null) {
				cursor.setColorFilter(new android.graphics.PorterDuffColorFilter(primary, android.graphics.PorterDuff.Mode.SRC_IN));
				editText.setTextCursorDrawable(cursor);
			}
		}
	}
	
	public void applyToSystemBars(Window window) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			int surfaceColor = getColor("colorSurface");
			window.setStatusBarColor(surfaceColor);
			window.setNavigationBarColor(surfaceColor);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				int flags = window.getDecorView().getSystemUiVisibility();
				if (getContrastColor(surfaceColor) == Color.parseColor("#121212")) {
					flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; 
				} else {
					flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; 
				}
				window.getDecorView().setSystemUiVisibility(flags);
			}
		}
	}
	
	public void setBorderlessRipple(android.view.View view, int rippleColor) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			android.graphics.drawable.RippleDrawable borderlessRipple = new android.graphics.drawable.RippleDrawable(
			android.content.res.ColorStateList.valueOf(rippleColor),
			null, 
			null  
			);
			view.setBackground(borderlessRipple);
		} else {
			view.setBackgroundColor(android.graphics.Color.TRANSPARENT);
		}
		view.setClickable(true);
		view.setFocusable(true);
	}
}
