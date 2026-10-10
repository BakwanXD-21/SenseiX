package ru.ino.senseix;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LanguageLoader {

	private static Activity activity;
	private static String currentPath = "";
	private static final HashMap<String, String> data = new HashMap<>();

	/** Panggil sekali di onCreate (setelah setContentView). */
	public static void init(Activity a) {
		activity = a;
	}

	/** Contoh: LanguageLoader.set("assets/languages/id.json"); */
	public static boolean set(String path) {
		if (activity == null) return false;
		return set(activity, path);
	}

	public static boolean set(Activity a, String path) {
		activity = a;
		String p = path.trim();
		if (p.startsWith("/")) p = p.substring(1);
		if (p.startsWith("assets/")) p = p.substring(7);

		try {
			InputStream is = a.getAssets().open(p);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			int n;
			while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
			is.close();

			JSONObject json = new JSONObject(bos.toString("UTF-8"));
			data.clear();
			Iterator<String> keys = json.keys();
			while (keys.hasNext()) {
				String k = keys.next();
				data.put(k, json.optString(k, ""));
			}
			currentPath = path;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		apply(a);
		return true;
	}

	/** Terapkan ulang data ke semua view (mis. setelah onResume). */
	public static void apply(final Activity a) {
		a.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (Map.Entry<String, String> e : data.entrySet()) {
					String key = e.getKey();
					String value = e.getValue();

					String name = key;
					String type = "text";
					int dot = key.lastIndexOf('.');
					if (dot > 0) {
						name = key.substring(0, dot);
						type = key.substring(dot + 1);
					}

					int id = a.getResources().getIdentifier(name, "id", a.getPackageName());
					if (id == 0) continue;

					View v = a.findViewById(id);
					if (v == null) continue;

					if (type.equals("label") && v instanceof InoInputLayout) {
						((InoInputLayout) v).setLabelText(value);
					} else if (type.equals("hint") && v instanceof TextView) {
						((TextView) v).setHint(value);
					} else if (type.equals("text") && v instanceof TextView) {
						((TextView) v).setText(value);
					}
				}
			}
		});
	}

	/** Untuk teks dari kode (dialog, toast, status, dll). */
	public static String get(String key, String fallback) {
		String v = data.get(key);
		return (v != null) ? v : fallback;
	}

	public static String get(String key) {
		return get(key, key);
	}

	public static String currentPath() {
		return currentPath;
	}
}