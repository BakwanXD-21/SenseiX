package ru.ino.senseix;

import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

public class LayoutTransitionHelper {
	
	private static final int TAG_KEY = 0x12345678;
	
	// GLOBAL SPEED
	private static long globalDuration = 300;
	
	public static void setDuration(long duration) {
		globalDuration = duration;
	}
	
	public static long getDuration() {
		return globalDuration;
	}
	
	// STATE PER VIEW
	private static class State {
		int width, height;
		int left, top, right, bottom;
		boolean saved = false;
		boolean isExpanded = false; // Flag tambahan untuk tracking state akurat
	}
	
	private static State getState(View v) {
		Object tag = v.getTag(TAG_KEY);
		if (tag instanceof State) {
			return (State) tag;
		} else {
			State s = new State();
			v.setTag(TAG_KEY, s);
			return s;
		}
	}
	
	private static void saveOriginal(LinearLayout target) {
		State s = getState(target);
		if (s.saved) return;
		
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) target.getLayoutParams();
		
		// Menyimpan ukuran asli sebelum di-expand (misal wrap_content / pixel awal)
		s.width = lp.width;
		s.height = lp.height;
		s.left = lp.leftMargin;
		s.top = lp.topMargin;
		s.right = lp.rightMargin;
		s.bottom = lp.bottomMargin;
		
		s.saved = true;
	}
	
	private static Transition createTransition(Long customDuration) {
		ChangeBounds t = new ChangeBounds();
		t.setDuration(customDuration != null ? customDuration : globalDuration);
		t.setInterpolator(new DecelerateInterpolator());
		return t;
	}
	
	// =========================
	// 🚀 EXPAND (Tetap Mempertahankan Margin)
	// =========================
	public static void expand(ViewGroup parent, LinearLayout target, int width, int height, Long customDuration) {
		if (parent == null || target == null) return;
		
		saveOriginal(target);
		State s = getState(target);
		
		TransitionManager.beginDelayedTransition(parent, createTransition(customDuration));
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) target.getLayoutParams();
		params.width = width;
		params.height = height;
		
		// 🔥 PERBAIKAN: Gunakan kembali margin asli yang sudah disimpan di State
		params.setMargins(s.left, s.top, s.right, s.bottom);
		
		target.setLayoutParams(params);
		s.isExpanded = true;
	}
	
	// =========================
	// 🎯 OPTIONAL: override per call
	// =========================
	public static void expandWithSpeed(ViewGroup parent, LinearLayout target, int width, int height, long duration) {
		// Tinggal alihkan ke method expand utama dengan mempassing duration-nya
		expand(parent, target, width, height, duration);
	}
	
	// =========================
	// 🔽 COLLAPSE
	// =========================
	public static void collapse(ViewGroup parent, LinearLayout target, Long customDuration) {
		if (parent == null || target == null) return;
		
		State s = getState(target);
		if (!s.saved) return; // Belum pernah di-expand, abaikan
		
		// Trik utama Android standard transition:
		// Sebelum collapse ke wrap_content, kita paksa sistem membaca ukuran pixel saat ini 
		// agar ChangeBounds punya titik awal kalkulasi pixel yang jelas (mencegah loncat/glitch)
		int currentWidth = target.getWidth();
		int currentHeight = target.getHeight();
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) target.getLayoutParams();
		lp.width = currentWidth;
		lp.height = currentHeight;
		target.setLayoutParams(lp);
		
		// Jalankan transisi menuju ukuran asli (wrap_content)
		TransitionManager.beginDelayedTransition(parent, createTransition(customDuration));
		
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) target.getLayoutParams();
		params.width = s.width;   // Mengembalikan ke wrap_content
		params.height = s.height; // Mengembalikan ke wrap_content
		params.setMargins(s.left, s.top, s.right, s.bottom);
		
		target.setLayoutParams(params);
		s.isExpanded = false;
	}
	
	// =========================
	// 🔁 TOGGLE
	// =========================
	public static void toggle(ViewGroup parent, LinearLayout target, int expandWidth, int expandHeight, Long customDuration) {
		if (parent == null || target == null) return;
		
		State s = getState(target);
		
		if (s.isExpanded) {
			collapse(parent, target, customDuration);
		} else {
			expand(parent, target, expandWidth, expandHeight, customDuration);
		}
	}
}