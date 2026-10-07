package ru.ino.senseix;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LanguageMenu {

	public interface OnPick { void onPick(String code); }

	public static final String DEFAULT = "id";

	public static final String[][] LANGS = {
		{"id", "Bahasa Indonesia"},
		{"en", "English"},
		{"ru", "\u0420\u0443\u0441\u0441\u043a\u0438\u0439"},
		{"ja", "\u65e5\u672c\u8a9e"},
		{"cn", "\u4e2d\u6587"}
	};

	public static String path(String code) { return "assets/languages/" + code + ".json"; }

	public static String saved(SharedPreferences p) {
		String c = p.getString("lang", DEFAULT);
		for (String[] l : LANGS) if (l[0].equals(c)) return c;
		return DEFAULT;
	}

	public static String nameOf(String code) {
		for (String[] l : LANGS) if (l[0].equals(code)) return l[1];
		return code;
	}

	/** Pengganti LanguageLoader.set("assets/languages/id.json") di initializeLogic */
	public static void load(Activity a, SharedPreferences p) {
		if (!LanguageLoader.set(a, path(saved(p)))) LanguageLoader.set(a, path(DEFAULT));
	}

	private static GradientDrawable round(int color, float r) {
		GradientDrawable g = new GradientDrawable();
		g.setColor(color);
		g.setCornerRadius(r);
		return g;
	}

	public static void show(final Activity a, DynamicThemeHelper th, final String current, final OnPick cb) {
		final float dp = a.getResources().getDisplayMetrics().density;

		Typeface f;
		try { f = Typeface.createFromAsset(a.getAssets(), "fonts/main.ttf"); }
		catch (Exception e) { f = Typeface.SANS_SERIF; }
		final Typeface font = f;
		final Typeface bold = Typeface.create(font, Typeface.BOLD);

		final Dialog dialog = new Dialog(a);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);

		LinearLayout root = new LinearLayout(a);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setPadding((int)(24*dp), (int)(24*dp), (int)(24*dp), (int)(18*dp));
		root.setBackground(round(th.getColor("colorSurfaceContainer"), 28*dp));

		TextView title = new TextView(a);
		title.setText(LanguageLoader.get("language_dialog_title", "Choose Language"));
		title.setTextSize(24);
		title.setTypeface(bold);
		title.setTextColor(th.getColor("colorOnSurface"));
		title.setPadding(0, 0, 0, (int)(8*dp));
		root.addView(title);

		TextView sub = new TextView(a);
		sub.setText(LanguageLoader.get("language_dialog_subtitle", "Applied instantly"));
		sub.setTextSize(13);
		sub.setTypeface(font);
		sub.setTextColor(th.getColor("colorPrimary"));
		sub.setPadding(0, 0, 0, (int)(18*dp));
		root.addView(sub);

		LinearLayout card = new LinearLayout(a);
		card.setOrientation(LinearLayout.VERTICAL);
		card.setPadding((int)(8*dp), (int)(8*dp), (int)(8*dp), (int)(8*dp));
		card.setBackground(round(th.getColor("colorSurfaceContainerHigh"), 20*dp));

		final MaterialRadioButton[] radios = new MaterialRadioButton[LANGS.length];
		for (int i = 0; i < LANGS.length; i++) {
			final int idx = i;
			final String code = LANGS[i][0];

			MaterialRadioButton rb = new MaterialRadioButton(a);
			rb.setText(LANGS[i][1]);
			rb.setTextSize(15);
			rb.setTypeface(font);
			rb.setTextColor(th.getColor("colorOnSurface"));
			rb.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			rb.setPadding(rb.getPaddingLeft(), (int)(14*dp), (int)(12*dp), (int)(14*dp));
			rb.setCheckedColor(th.getColor("colorPrimary"));
			rb.setUncheckedColor(th.getColor("colorOutline"));
			rb.setChecked(code.equals(current));
			rb.setOnClickListener(v -> {
				for (int j = 0; j < radios.length; j++) radios[j].setChecked(j == idx);
				v.postDelayed(() -> {
					if (dialog.isShowing()) dialog.dismiss();
					if (!code.equals(current)) cb.onPick(code);
				}, 220);
			});
			radios[i] = rb;
			card.addView(rb, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		}
		root.addView(card);

		View spacer = new View(a);
		spacer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(18*dp)));
		root.addView(spacer);

		View divider = new View(a);
		divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(1*dp)));
		divider.setBackgroundColor(th.getColor("colorOutline"));
		root.addView(divider);

		LinearLayout row = new LinearLayout(a);
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setGravity(Gravity.END);
		row.setPadding(0, (int)(14*dp), 0, 0);

		TextView close = new TextView(a);
		close.setText(LanguageLoader.get("btn_close", "Close"));
		close.setTextSize(14);
		close.setTypeface(bold);
		close.setTextColor(th.getColor("colorPrimary"));
		close.setPadding((int)(16*dp), (int)(12*dp), (int)(16*dp), (int)(12*dp));
		close.setOnClickListener(v -> dialog.dismiss());
		row.addView(close);
		root.addView(row);

		dialog.setContentView(root);
		Window w = dialog.getWindow();
		if (w != null) {
			w.setLayout((int)(a.getResources().getDisplayMetrics().widthPixels * 0.88f),
				ViewGroup.LayoutParams.WRAP_CONTENT);
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			w.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}
}