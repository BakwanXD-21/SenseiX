package ru.ino.senseix;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

/* By IqwanoINO / Fixed Span Range Logic for Native Engine */

public class AnsiHtmlHelper {
	
	private static final String[] COLOR_PALETTE = {
		"#000000", "#CD0000", "#00CD00", "#CDCD00", "#0000EE", "#CD00CD", "#00CDCD", "#E5E5E5", 
		"#7F7F7F", "#FF0000", "#00FF00", "#FFFF00", "#5C5CFF", "#FF00FF", "#00FFFF", "#FFFFFF"  
	};
	
	public static Spanned parse(String text) {
		if (text == null || text.isEmpty()) {
			return new SpannableStringBuilder("");
		}
		
		SpannableStringBuilder builder = new SpannableStringBuilder();
		int i = 0;
		int length = text.length();
		
		// State gaya aktif
		boolean currentBold = false;
		boolean currentItalic = false;
		boolean currentUnderline = false;
		String currentFg = null;
		String currentBg = null;
		
		// Pointer untuk menandai awal blok teks dengan warna yang sama
		int sectionStart = 0;
		
		while (i < length) {
			char c = text.charAt(i);
			
			// Deteksi Escape Sequence (\u001B[, \033[, atau char code 27)
			if ((c == '\u001B' || c == 27) && i + 1 < length && text.charAt(i + 1) == '[') {
				
				// 1. KUNCI DAN PASANG SPAN PADA BLOK TEKS SEBELUMNYA SEBELUM STATE BERUBAH
				applyCurrentStyles(builder, sectionStart, builder.length(), currentBold, currentItalic, currentUnderline, currentFg, currentBg);
				
				i += 2; // Lewati ESC dan '['
				StringBuilder codeBuffer = new StringBuilder();
				
				while (i < length && !Character.isLetter(text.charAt(i))) {
					codeBuffer.append(text.charAt(i));
					i++;
				}
				
				if (i < length && text.charAt(i) == 'm') {
					String[] tokens = codeBuffer.toString().split(";");
					
					for (String tokenStr : tokens) {
						tokenStr = tokenStr.trim();
						if (tokenStr.isEmpty()) {
							currentBold = false; currentItalic = false; currentUnderline = false;
							currentFg = null; currentBg = null;
							continue;
						}
						
						try {
							int code = Integer.parseInt(tokenStr);
							
							if (code == 0) {
								currentBold = false; currentItalic = false; currentUnderline = false;
								currentFg = null; currentBg = null;
							} else if (code == 1) {
								currentBold = true;
							} else if (code == 3) {
								currentItalic = true;
							} else if (code == 4) {
								currentUnderline = true;
							} else if (code == 22) {
								currentBold = false;
							} else if (code == 23) {
								currentItalic = false;
							} else if (code == 24) {
								currentUnderline = false;
							} 
							else if (code >= 30 && code <= 37) {
								currentFg = COLOR_PALETTE[code - 30];
							} else if (code >= 90 && code <= 97) {
								currentFg = COLOR_PALETTE[code - 90 + 8];
							} else if (code == 39) {
								currentFg = null;
							} 
							else if (code >= 40 && code <= 47) {
								currentBg = COLOR_PALETTE[code - 40];
							} else if (code >= 100 && code <= 107) {
								currentBg = COLOR_PALETTE[code - 100 + 8];
							} else if (code == 49) {
								currentBg = null;
							}
						} catch (NumberFormatException ignored) {}
					}
					
					// Update posisi awal segmen baru setelah pergantian kode warna
					sectionStart = builder.length();
					i++; // Lewati 'm'
				}
			} else {
				// Teks biasa langsung dikumpulkan ke builder tanpa memotong Span per huruf
				builder.append(c);
				i++;
			}
		}
		
		// 2. PASANG SPAN UNTUK SISA SEGMEN TEKS TERAKHIR DI UJUNG STRING
		applyCurrentStyles(builder, sectionStart, builder.length(), currentBold, currentItalic, currentUnderline, currentFg, currentBg);
		
		return builder;
	}
	
	/**
* Fungsi pembantu untuk menyuntikkan Span secara massal pada rentang index (start hingga end) yang valid.
*/	
	private static void applyCurrentStyles(SpannableStringBuilder builder, int start, int end, 
	boolean bold, boolean italic, boolean underline, 
	String fg, String bg) {
		// Jangan pasang span jika rentang teks kosong
		if (start >= end) return; 
		
		if (bold) {
			builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (italic) {
			builder.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (underline) {
			builder.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (fg != null) {
			try {
				builder.setSpan(new ForegroundColorSpan(Color.parseColor(fg)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception ignored) {}
		}
		if (bg != null) {
			try {
				// Sekarang BackgroundColorSpan mengunci satu blok kalimat utuh, dijamin muncul!
				builder.setSpan(new BackgroundColorSpan(Color.parseColor(bg)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (Exception ignored) {}
		}
	}
}
