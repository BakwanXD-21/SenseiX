package ru.ino.senseix;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Penyimpanan pengaturan GameSpace per game (berdasarkan nama paket).
 * Memakai SharedPreferences "data" yang sama dengan pengaturan lain di app.
 */
public final class GameSpacePrefs {

	public static final String MODE_OPTIMIZE = "optimize";
	public static final String MODE_PERFORMANCE = "performance";
	public static final String MODE_EXPERIMENTAL = "experimental";

	private static final String PREFS = "data";

	private GameSpacePrefs() {}

	private static SharedPreferences prefs(Context c) {
		return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
	}

	private static String key(String pkg, String what) {
		return "gs_" + what + "_" + pkg;
	}

	public static String getMode(Context c, String pkg) {
		if (TextUtils.isEmpty(pkg)) return MODE_OPTIMIZE;
		return prefs(c).getString(key(pkg, "mode"), MODE_OPTIMIZE);
	}

	public static void setMode(Context c, String pkg, String mode) {
		if (TextUtils.isEmpty(pkg)) return;
		prefs(c).edit().putString(key(pkg, "mode"), mode).apply();
	}

	public static boolean isSenSpace(Context c, String pkg) {
		if (TextUtils.isEmpty(pkg)) return false;
		return prefs(c).getBoolean(key(pkg, "senspace"), false);
	}

	public static void setSenSpace(Context c, String pkg, boolean enabled) {
		if (TextUtils.isEmpty(pkg)) return;
		prefs(c).edit().putBoolean(key(pkg, "senspace"), enabled).apply();
	}
}
