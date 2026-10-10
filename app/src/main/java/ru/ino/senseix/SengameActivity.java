package ru.ino.senseix;

import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.*;
import android.content.SharedPreferences;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.provider.Settings;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import rikka.shizuku.aidl.*;
import rikka.shizuku.api.*;
import rikka.shizuku.provider.*;
import ru.ino.senseix.OuiBatteryView;
import ru.ino.senseix.VideoLayout;
import android.content.pm.*;

public class SengameActivity extends Activity {
	
	// Default: -1 (Tidak ada item yang dipilih)
	private String paketnama = "";
	private int selectedPosition = -1;
	
	private ArrayList<HashMap<String, Object>> apps = new ArrayList<>();
	
	private FrameLayout background;
	private FrameLayout mainscreen;
	private LinearLayout splashscreen;
	private LinearLayout previewicon;
	private LinearLayout gamelistframebg;
	private LinearLayout toolbar;
	private LinearLayout gamelistbg;
	private LinearLayout exitbtn;
	private LinearLayout titlebar;
	private OuiBatteryView ouibattery;
	private TextView clock;
	private ImageView icon_stop;
	private TextView exitbtntext;
	private TextView appname;
	private TextView manufactur;
	private ListView list;
	private LinearLayout editor_background;
	private LinearLayout applicationdetailbg;
	private TextView selected_appname;
	private LinearLayout versionbg;
	private LinearLayout buttonrow;
	private TextView selected_appversion;
	private LinearLayout restorebtn;
	private LinearLayout setbtn;
	private LinearLayout playbtn;
	private ImageView icon_resetore;
	private ImageView icon_set;
	private ImageView icon_play;
	private LinearLayout settingbtn;
	private ImageView icon_setting;
	private TextView playbtntext;
	private VideoLayout videosplash;
	
	private SharedPreferences user;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.sengame);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		background = findViewById(R.id.background);
		mainscreen = findViewById(R.id.mainscreen);
		splashscreen = findViewById(R.id.splashscreen);
		previewicon = findViewById(R.id.previewicon);
		gamelistframebg = findViewById(R.id.gamelistframebg);
		toolbar = findViewById(R.id.toolbar);
		gamelistbg = findViewById(R.id.gamelistbg);
		exitbtn = findViewById(R.id.exitbtn);
		titlebar = findViewById(R.id.titlebar);
		ouibattery = findViewById(R.id.ouibattery);
		clock = findViewById(R.id.clock);
		icon_stop = findViewById(R.id.icon_stop);
		exitbtntext = findViewById(R.id.exitbtntext);
		appname = findViewById(R.id.appname);
		manufactur = findViewById(R.id.manufactur);
		list = findViewById(R.id.list);
		editor_background = findViewById(R.id.editor_background);
		applicationdetailbg = findViewById(R.id.applicationdetailbg);
		selected_appname = findViewById(R.id.selected_appname);
		versionbg = findViewById(R.id.versionbg);
		buttonrow = findViewById(R.id.buttonrow);
		selected_appversion = findViewById(R.id.selected_appversion);
		restorebtn = findViewById(R.id.restorebtn);
		setbtn = findViewById(R.id.setbtn);
		playbtn = findViewById(R.id.playbtn);
		settingbtn = findViewById(R.id.settingbtn);
		icon_setting = findViewById(R.id.icon_setting);
		icon_resetore = findViewById(R.id.icon_resetore);
		icon_set = findViewById(R.id.icon_set);
		icon_play = findViewById(R.id.icon_play);
		playbtntext = findViewById(R.id.playbtntext);
		videosplash = findViewById(R.id.videosplash);
		user = getSharedPreferences("data", Activity.MODE_PRIVATE);
		
		exitbtn.setOnClickListener(_v -> finish());
		
		// RESTORE BACKGROUND: Jika tidak milih item, hapus background default global
		restorebtn.setOnClickListener(_v -> {
			String targetBgKey = (paketnama != null && !paketnama.isEmpty()) ? paketnama : "default_bg";
			removeCustomBackground(targetBgKey);
			BackgroundHelper.clearCache();
			loadAppBackground(targetBgKey);
		});
		
		// SET BACKGROUND: Mengganti background item terpilih, ATAU background default jika belum milih
		setbtn.setOnClickListener(_v -> {
			Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			Intent chooserIntent = Intent.createChooser(pickIntent, LanguageLoader.get("sg_pick_background", "Choose Background Image"));
			startActivityForResult(chooserIntent, 2167);
		});
		
		settingbtn.setOnClickListener(_v -> showGameSpaceDialog());
		
		playbtn.setOnClickListener(_v -> {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(SengameActivity.this)) {
				String pesanIzinOverlay = "SenseiX requires the 'Display over other apps' permission to allow performance optimization and real-time overlays to function smoothly in the background.\n\n" +
				"• Displays real-time performance stats\n" +
				"• Prevents the optimization process from being terminated by the system\n" +
				"• Completely safe and does not collect any personal data";
				
				tampilkanDialogKustom(
				LanguageLoader.get("overlay_title", "Permission Required"),
				LanguageLoader.get("overlay_subtitle", "Display Over Other Apps"),
				LanguageLoader.get("overlay_message", pesanIzinOverlay),
				LanguageLoader.get("btn_cancel", "Cancel"),
				LanguageLoader.get("btn_grant_permission", "Grant Permission"),
				() -> {},
				() -> {
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
						Intent intent = new Intent(
						android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						android.net.Uri.parse("package:" + getPackageName())
						);
						startActivity(intent);
					}
				}
				);
				return;
			}
			
			if (windowManager == null) {
				windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
			}
			loading = LayoutInflater.from(SengameActivity.this).inflate(R.layout.loading, null);
			final WindowManager.LayoutParams ino;
			
			int layoutType;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
			} else {
				layoutType = WindowManager.LayoutParams.TYPE_PHONE;
			}
			
			ino = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.MATCH_PARENT, 
			WindowManager.LayoutParams.MATCH_PARENT, 
			layoutType,
			WindowManager.LayoutParams.FLAG_FULLSCREEN 
			| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
			| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			PixelFormat.TRANSLUCENT
			);
			
			ino.gravity = Gravity.CENTER;
			ino.x = 0;
			ino.y = 0;
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
				ino.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
			}
			
			if (loading.getWindowToken() != null) {
				return;
			}
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				loading.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				);
			}
			
			windowManager.addView(loading, ino);
			
			final LinearLayout background = loading.findViewById(R.id.background);
			final ru.ino.senseix.CustomFillView appicon = loading.findViewById(R.id.appicon);
			final ru.ino.senseix.InoLoadingIndicator indicator = loading.findViewById(R.id.indicator);
			final ru.ino.senseix.InoCircularProgress circular = loading.findViewById(R.id.circular);
			indicator.setVisibility(View.GONE);
			circular.setVisibility(View.GONE);
			
			int colorStartOld = 0xFF151515;
			int colorEndOld   = 0xFF282828;
			int colorStartNew = 0x80151515;
			int colorEndNew   = 0x80282828;
			
			ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
			animator.setDuration(8000); 
			
			ArgbEvaluator evaluator = new ArgbEvaluator();
			
			animator.addUpdateListener(animation -> {
				float fraction = animation.getAnimatedFraction();
				int currentStart = (int) evaluator.evaluate(fraction, colorStartOld, colorStartNew);
				int currentEnd = (int) evaluator.evaluate(fraction, colorEndOld, colorEndNew);
				
				GradientDrawable gradient = new GradientDrawable(
				GradientDrawable.Orientation.BR_TL, 
				new int[]{currentStart, currentEnd}
				);
				background.setBackground(gradient);
			});
			
			animator.start();
			appicon.setIcon(R.drawable.app_icon);
			appicon.setIconColor(Color.parseColor("#E0E0E0"));
			appicon.setFillColor(themeHelper.getColor("colorPrimaryBtn"));
			appicon.start(1700);
			
			navigateRunnable = () -> {
				if (loading != null && loading.isAttachedToWindow()) {
					try {
						windowManager.removeViewImmediate(loading);
					} catch (Exception ignored) {}
					loading = null;
				}
			};
			
			delayHandler.postDelayed(navigateRunnable, 2000);
			
			String targetPackage = (paketnama != null && !paketnama.isEmpty()) ? paketnama : getPackageName();
			android.content.Intent intent = getPackageManager().getLaunchIntentForPackage(targetPackage);

			if (GameSpacePrefs.isSenSpace(SengameActivity.this, paketnama)) {
				startSenSpace(targetPackage);
			}
			
			if (intent != null) {
				intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}
	
	private void initializeLogic() {
		videosplash.setVideo("assets:movies/senseix.m21");
		videosplash.setLoop(true);
		videosplash.play();
		user = getSharedPreferences("data", Activity.MODE_PRIVATE);
		LanguageLoader.init(this);
		LanguageMenu.load(this, user);
		int warnaTemaTerpilih = user.getInt("color", Color.parseColor("#0D57A1"));
		themeHelper = new ru.ino.senseix.DynamicThemeHelper(); 
		themeHelper.generateFromBaseColor(warnaTemaTerpilih);
		applyLayerFilter(videosplash, warnaTemaTerpilih);
		
		getWindow().setStatusBarColor(Color.TRANSPARENT);
		getWindow().setNavigationBarColor(Color.TRANSPARENT);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			getWindow().setDecorFitsSystemWindows(false);
			WindowInsetsController controller = getWindow().getInsetsController();
			if (controller != null) {
				controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
				controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
			}
		} else {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(
			View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
			| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_FULLSCREEN
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
			);
		}
		
		new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
			splashscreen.animate().alpha(0f).setDuration(400).start();
			new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
				splashscreen.setVisibility(View.GONE);
				videosplash.release();
			}, 500);
		}, 2100);
		
		try {
			PackageManager pm = getPackageManager();
			PackageInfo pInfo = pm.getPackageInfo(getPackageName(), 0);
			
			selected_appname.setText(pm.getApplicationLabel(getApplicationInfo()).toString());
			selected_appversion.setText(pInfo.versionName);
		} catch (PackageManager.NameNotFoundException e) {
			selected_appname.setText("SenseiX");
			selected_appversion.setText("0.98.21");
		}
		
		// Muat background bawaan (default_bg) saat aplikasi dibuka
		loadAppBackground("default_bg");
		
		icon_resetore.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		icon_set.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		icon_play.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		icon_setting.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		icon_stop.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		playbtntext.setTextColor(themeHelper.getColor("colorPrimaryDark"));
		exitbtntext.setTextColor(themeHelper.getColor("colorPrimaryDark"));
		
		setColorRipple(playbtn, themeHelper.getColor("colorPrimaryBtn"), 8);
		setColorRipple(restorebtn, themeHelper.getColor("colorPrimaryBtn"), 8);
		setColorRipple(setbtn, themeHelper.getColor("colorPrimaryBtn"), 8);
		setColorRipple(settingbtn, themeHelper.getColor("colorPrimaryBtn"), 8);
		setColorRipple(exitbtn, themeHelper.getColor("colorPrimaryBtn"), 8);
		
		final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault());
		h = new Handler(android.os.Looper.getMainLooper());
		
		r = new Runnable() {
			@Override
			public void run() {
				clock.setText(sdf.format(new java.util.Date()));
				if (h != null) {
					h.postDelayed(this, 500);
				}
			}
		};
		
		h.post(r);
		
		versionbg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)21, 0xFF181818));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		videosplash.pause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setFont(getWindow().getDecorView());
		
		final ArrayList<String> receivedPackages = getIntent().getStringArrayListExtra("active_packages");
		
		helper = new AppListHelper(this);
		new Thread(() -> {
			ArrayList<HashMap<String, Object>> fetchedApps;
			
			if (receivedPackages != null && !receivedPackages.isEmpty()) {
				String[] packageArray = receivedPackages.toArray(new String[0]);
				fetchedApps = helper.getAppListFromPackages(packageArray);
			} else {
				fetchedApps = helper.getAppList(AppListHelper.MODE_GAMES);
			}
			
			final ArrayList<HashMap<String, Object>> finalApps = fetchedApps;
			
			runOnUiThread(() -> {
				originalAppsList.clear();
				originalAppsList.addAll(finalApps);
				
				apps.clear();
				apps.addAll(finalApps);
				
				// Hapus auto-select agar posisi tetap -1 secara default saat daftar dimuat
				listAdapter = new ListAdapter(apps);
				list.setAdapter(listAdapter);
			});
		}).start();
		
		videosplash.resume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		BackgroundHelper.clearCache();
		videosplash.release();
	}
	
	private WindowManager windowManager;
	private View loading;
	private ru.ino.senseix.DynamicThemeHelper themeHelper;
	private AppListHelper helper;
	
	private ArrayList<HashMap<String, Object>> originalAppsList = new ArrayList<>();
	private ListAdapter listAdapter;
	
	private Handler delayHandler = new Handler(Looper.getMainLooper());
	private Runnable navigateRunnable;
	
	private Handler h;
	private Runnable r;
	
	private void applyLayerFilter(final View videoLayout, final int targetColor) {
		if (videoLayout == null || !(videoLayout instanceof ru.ino.senseix.VideoLayout)) return;
		
		float r = android.graphics.Color.red(targetColor) / 255f;
		float g = android.graphics.Color.green(targetColor) / 255f;
		float b = android.graphics.Color.blue(targetColor) / 255f;
		
		float[] colorTransform = {
			r, 0, 0, 0, 0,
			0, g, 0, 0, 0,
			0, 0, b, 0, 0,
			0, 0, 0, 1, 0
		};
		final android.graphics.ColorMatrixColorFilter matrixFilter = new android.graphics.ColorMatrixColorFilter(colorTransform);
		final android.graphics.Paint targetPaint = new android.graphics.Paint();
		targetPaint.setColorFilter(matrixFilter);
		
		videoLayout.post(() -> {
			videoLayout.setLayerType(View.LAYER_TYPE_HARDWARE, targetPaint);
			videoLayout.invalidate();
		});
	}
	
	private void setFont(android.view.View view) {
		android.graphics.Typeface tf = android.graphics.Typeface.createFromAsset(getAssets(), "fonts/main.ttf");
		
		if (view instanceof android.widget.TextView) {
			android.widget.TextView tv = (android.widget.TextView) view;
			int existingStyle = android.graphics.Typeface.NORMAL;
			if (tv.getTypeface() != null) {
				existingStyle = tv.getTypeface().getStyle();
			}
			tv.setTypeface(tf, existingStyle);
		}
		
		if (view instanceof android.view.ViewGroup) {
			android.view.ViewGroup vg = (android.view.ViewGroup) view;
			for (int i = 0; i < vg.getChildCount(); i++) {
				setFont(vg.getChildAt(i));
			}
		}
	}
	
	public static void setGradientRipple(
	View view,
	int colorStart,
	int colorEnd,
	float radiusDp,
	GradientDrawable.Orientation orientation) {
		
		float d = view.getResources().getDisplayMetrics().density;
		
		GradientDrawable bg = new GradientDrawable(
		orientation,
		new int[]{colorStart, colorEnd}
		);
		bg.setCornerRadius(radiusDp * d);
		
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			RippleDrawable ripple = new RippleDrawable(
			android.content.res.ColorStateList.valueOf(0x33FFFFFF),
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
	
	public static void setTransparentStrokeRipple(
	View view,
	int strokeColor,
	int strokeWidthPx,
	float radiusDp) {
		
		float d = view.getResources().getDisplayMetrics().density;
		
		GradientDrawable bg = new GradientDrawable();
		bg.setColor(Color.TRANSPARENT);
		bg.setCornerRadius(radiusDp * d);
		bg.setStroke(strokeWidthPx, strokeColor);
		
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			RippleDrawable ripple = new RippleDrawable(
			android.content.res.ColorStateList.valueOf(0x22FFFFFF),
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
	
	public static void setColorRipple(
	View view,
	int color,
	float radiusDp) {
		
		float d = view.getResources().getDisplayMetrics().density;
		
		GradientDrawable bg = new GradientDrawable();
		bg.setColor(color);
		bg.setCornerRadius(radiusDp * d);
		
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			RippleDrawable ripple = new RippleDrawable(
			android.content.res.ColorStateList.valueOf(0x33FFFFFF),
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
			view.setElevation(8f);
			
			view.setOutlineProvider(new android.view.ViewOutlineProvider() {
				@Override
				public void getOutline(View v, android.graphics.Outline outline) {
					outline.setRoundRect(0, 0, v.getWidth(), v.getHeight(), radiusPx);
				}
			});
		}
	}
	
	private void tampilkanDialogKustom(
	String strTitle, 
	String strSubtitle, 
	CharSequence strMessage, 
	String textCancelBtn, 
	String textOkBtn, 
	Runnable onNoAction,
	Runnable onOkAction
	) {
		Dialog dialog = new Dialog(SengameActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		
		float dp = getResources().getDisplayMetrics().density;
		android.graphics.Typeface inoFont = android.graphics.Typeface.createFromAsset(getAssets(), "fonts/main.ttf");
		
		LinearLayout root = new LinearLayout(SengameActivity.this);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setPadding((int)(24 * dp), (int)(24 * dp), (int)(24 * dp), (int)(18 * dp));
		root.setBackground(new GradientDrawable() {{
				setColor(themeHelper.getColor("colorSurfaceContainer"));
				setCornerRadius(28 * dp);
			}});
		
		TextView title = new TextView(SengameActivity.this);
		title.setText(strTitle);
		title.setTextSize(24);
		title.setTypeface(android.graphics.Typeface.create(inoFont, android.graphics.Typeface.BOLD));
		title.setTextColor(themeHelper.getColor("colorOnSurface"));
		title.setPadding(0, 0, 0, (int)(8 * dp));
		root.addView(title);
		
		TextView subtitle = new TextView(SengameActivity.this);
		subtitle.setText(strSubtitle);
		subtitle.setTextSize(13);
		subtitle.setTypeface(inoFont);
		subtitle.setTextColor(themeHelper.getColor("colorPrimary"));
		subtitle.setPadding(0, 0, 0, (int)(18 * dp));
		root.addView(subtitle);
		
		LinearLayout infoCard = new LinearLayout(SengameActivity.this);
		infoCard.setOrientation(LinearLayout.VERTICAL);
		infoCard.setPadding((int)(16 * dp), (int)(16 * dp), (int)(16 * dp), (int)(16 * dp));
		infoCard.setBackground(new GradientDrawable() {{
				setCornerRadius(20 * dp);
				setColor(themeHelper.getColor("colorSurfaceContainerHigh"));
			}});
		
		TextView msg = new TextView(SengameActivity.this);
		msg.setText(strMessage);
		msg.setTextSize(14);
		msg.setTypeface(inoFont);
		msg.setTextColor(themeHelper.getColor("colorOnSurfaceVariant"));
		infoCard.addView(msg);
		root.addView(infoCard);
		
		View spacer = new View(SengameActivity.this);
		spacer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(18 * dp)));
		root.addView(spacer);
		
		View divider = new View(SengameActivity.this);
		divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(1 * dp)));
		divider.setBackgroundColor(themeHelper.getColor("colorOutline"));
		root.addView(divider);
		
		LinearLayout row = new LinearLayout(SengameActivity.this);
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setGravity(Gravity.END);
		row.setPadding(0, (int)(14 * dp), 0, 0);
		
		TextView btnCancel = new TextView(SengameActivity.this);
		btnCancel.setText(textCancelBtn);
		btnCancel.setTextSize(14);
		btnCancel.setTypeface(android.graphics.Typeface.create(inoFont, android.graphics.Typeface.BOLD));
		btnCancel.setTextColor(themeHelper.getColor("colorPrimary"));
		btnCancel.setPadding((int)(16 * dp), (int)(12 * dp), (int)(16 * dp), (int)(12 * dp));
		
		btnCancel.setOnClickListener(v -> {
			if (onNoAction != null) {
				onNoAction.run();
			}
			dialog.dismiss();
		});
		row.addView(btnCancel);
		
		TextView btnOk = new TextView(SengameActivity.this);
		btnOk.setText(textOkBtn);
		btnOk.setTextSize(14);
		btnOk.setTypeface(android.graphics.Typeface.create(inoFont, android.graphics.Typeface.BOLD));
		btnOk.setPadding((int)(18 * dp), (int)(12 * dp), (int)(18 * dp), (int)(12 * dp));
		btnOk.setTextColor(themeHelper.getColor("colorOnPrimary"));
		themeHelper.setColorRipple(btnOk, themeHelper.getColor("colorPrimary"), 18f);
		
		btnOk.setOnClickListener(v -> {
			if (onOkAction != null) {
				onOkAction.run();
			}
			dialog.dismiss();
		});
		row.addView(btnOk);
		root.addView(row);
		
		dialog.setContentView(root);
		
		Window w = dialog.getWindow();
		if (w != null) {
			int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.88f);
			w.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			w.setGravity(Gravity.CENTER);
		}
		
		dialog.show();
	}
	
	// ---------------------------------------------------------------- GameSpace / SenSpace

	private void showGameSpaceDialog() {
		if (paketnama == null || paketnama.isEmpty()) {
			Toast.makeText(SengameActivity.this,
			LanguageLoader.get("sg_pick_game_first", "Select a game first to configure GameSpace"),
			Toast.LENGTH_SHORT).show();
			return;
		}

		final String pkg = paketnama;
		final float dp = getResources().getDisplayMetrics().density;
		final android.graphics.Typeface inoFont = android.graphics.Typeface.createFromAsset(getAssets(), "fonts/main.ttf");

		final Dialog dialog = new Dialog(SengameActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);

		LinearLayout root = new LinearLayout(SengameActivity.this);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setPadding((int) (24 * dp), (int) (24 * dp), (int) (24 * dp), (int) (18 * dp));
		GradientDrawable rootBg = new GradientDrawable();
		rootBg.setColor(themeHelper.getColor("colorSurfaceContainer"));
		rootBg.setCornerRadius(28 * dp);
		root.setBackground(rootBg);

		root.addView(makeText(LanguageLoader.get("sg_gamespace_title", "SenseiX"), 24, inoFont,
		themeHelper.getColor("colorOnSurface"), true));

		TextView subtitle = makeText(LanguageLoader.get("sg_gamespace_subtitle", "GameSpace"), 13, inoFont,
		themeHelper.getColor("colorPrimary"), false);
		subtitle.setPadding(0, 0, 0, (int) (2 * dp));
		root.addView(subtitle);

		TextView forGame = makeText(String.format(LanguageLoader.get("sg_for_game", "For %s"), selected_appname.getText()),
		12, inoFont, themeHelper.getColor("colorOnSurfaceVariant"), false);
		forGame.setPadding(0, 0, 0, (int) (14 * dp));
		root.addView(forGame);

		// Kartu mode: Optimalkan / Performa / Eksperimental (tepat satu aktif)
		LinearLayout modeCard = newCard(dp);
		final String[] modeIds = {
			GameSpacePrefs.MODE_OPTIMIZE,
			GameSpacePrefs.MODE_PERFORMANCE,
			GameSpacePrefs.MODE_EXPERIMENTAL
		};
		String[] modeTitles = {
			LanguageLoader.get("sg_mode_optimize_title", "Optimize"),
			LanguageLoader.get("sg_mode_performance_title", "Performance"),
			LanguageLoader.get("sg_mode_experimental_title", "Experimental")
		};
		String[] modeDescs = {
			LanguageLoader.get("sg_mode_optimize_desc", "Balanced default mode"),
			LanguageLoader.get("sg_mode_performance_desc", "Prioritize FPS and responsiveness"),
			LanguageLoader.get("sg_mode_experimental_desc", "Test features, may be unstable")
		};

		final MaterialSwitch[] modeSwitches = new MaterialSwitch[modeIds.length];
		final String currentMode = GameSpacePrefs.getMode(SengameActivity.this, pkg);
		final boolean[] syncing = {false};
		for (int i = 0; i < modeIds.length; i++) {
			modeSwitches[i] = addSwitchRow(modeCard, modeTitles[i], modeDescs[i], inoFont, dp);
			modeSwitches[i].setChecked(modeIds[i].equals(currentMode));
		}
		for (int i = 0; i < modeIds.length; i++) {
			final int idx = i;
			modeSwitches[i].setOnCheckedChangeListener((btn, checked) -> {
				if (syncing[0]) return;
				if (!checked) {
					// Harus tetap ada satu mode aktif.
					syncing[0] = true;
					modeSwitches[idx].setChecked(true);
					syncing[0] = false;
					return;
				}
				syncing[0] = true;
				for (int j = 0; j < modeSwitches.length; j++) {
					if (j != idx) modeSwitches[j].setChecked(false);
				}
				syncing[0] = false;
				GameSpacePrefs.setMode(SengameActivity.this, pkg, modeIds[idx]);
			});
		}
		root.addView(modeCard);

		// Pemisah, lalu SenSpace terpisah di paling bawah
		View spacer = new View(SengameActivity.this);
		spacer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (14 * dp)));
		root.addView(spacer);

		LinearLayout senCard = newCard(dp);
		MaterialSwitch senSwitch = addSwitchRow(senCard,
		LanguageLoader.get("sg_senspace_title", "SenSpace"),
		LanguageLoader.get("sg_senspace_desc", "Show a floating menu while the game runs"),
		inoFont, dp);
		senSwitch.setChecked(GameSpacePrefs.isSenSpace(SengameActivity.this, pkg));
		senSwitch.setOnCheckedChangeListener((btn, checked) ->
		GameSpacePrefs.setSenSpace(SengameActivity.this, pkg, checked));
		root.addView(senCard);

		LinearLayout row = new LinearLayout(SengameActivity.this);
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setGravity(Gravity.END);
		row.setPadding(0, (int) (14 * dp), 0, 0);
		TextView btnClose = makeText(LanguageLoader.get("btn_close", "Close"), 14, inoFont,
		themeHelper.getColor("colorPrimary"), true);
		btnClose.setPadding((int) (16 * dp), (int) (12 * dp), (int) (16 * dp), (int) (12 * dp));
		btnClose.setOnClickListener(v -> dialog.dismiss());
		row.addView(btnClose);
		root.addView(row);

		// Scroll agar tetap muat di layar landscape yang pendek
		ScrollView scroll = new ScrollView(SengameActivity.this);
		scroll.addView(root);
		dialog.setContentView(scroll);

		Window w = dialog.getWindow();
		if (w != null) {
			int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.88f);
			w.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
			w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			w.setGravity(Gravity.CENTER);
		}
		dialog.show();
	}

	private LinearLayout newCard(float dp) {
		LinearLayout card = new LinearLayout(SengameActivity.this);
		card.setOrientation(LinearLayout.VERTICAL);
		card.setPadding((int) (16 * dp), (int) (6 * dp), (int) (16 * dp), (int) (6 * dp));
		GradientDrawable g = new GradientDrawable();
		g.setColor(themeHelper.getColor("colorSurfaceContainerHigh"));
		g.setCornerRadius(20 * dp);
		card.setBackground(g);
		return card;
	}

	private MaterialSwitch addSwitchRow(LinearLayout parent, String title, String desc,
	android.graphics.Typeface font, float dp) {
		LinearLayout row = new LinearLayout(SengameActivity.this);
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setGravity(Gravity.CENTER_VERTICAL);
		row.setPadding(0, (int) (10 * dp), 0, (int) (10 * dp));

		LinearLayout texts = new LinearLayout(SengameActivity.this);
		texts.setOrientation(LinearLayout.VERTICAL);
		texts.addView(makeText(title, 15, font, themeHelper.getColor("colorOnSurface"), true));
		texts.addView(makeText(desc, 12, font, themeHelper.getColor("colorOnSurfaceVariant"), false));
		row.addView(texts, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

		MaterialSwitch sw = new MaterialSwitch(SengameActivity.this);
		sw.setActiveColor(themeHelper.getColor("colorPrimary"));
		row.addView(sw, new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

		parent.addView(row);
		return sw;
	}

	private TextView makeText(String text, float sp, android.graphics.Typeface font, int color, boolean bold) {
		TextView t = new TextView(SengameActivity.this);
		t.setText(text);
		t.setTextSize(sp);
		t.setTypeface(bold ? android.graphics.Typeface.create(font, android.graphics.Typeface.BOLD) : font);
		t.setTextColor(color);
		return t;
	}

	private void startSenSpace(String targetPackage) {
		Intent svc = new Intent(SengameActivity.this, SenSpaceService.class);
		svc.putExtra(SenSpaceService.EXTRA_PACKAGE, targetPackage);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			startForegroundService(svc);
		} else {
			startService(svc);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2167 && resultCode == RESULT_OK && data != null) {
			Uri uri = data.getData();
			if (uri != null) {
				// Simpan ke paketnama jika ada item terpilih, atau simpan ke "default_bg"
				String targetKey = (paketnama != null && !paketnama.isEmpty()) ? paketnama : "default_bg";
				saveCustomBackground(targetKey, uri);
				BackgroundHelper.clearCache();
				loadAppBackground(targetKey);
			}
		}
	}
	
	public class ListAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public ListAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			boolean isNewView = (_v == null);
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			
			if (_view == null) {
				_view = _inflater.inflate(R.layout.apps, null);
			}
			
			final LinearLayout background = _view.findViewById(R.id.background);
			final ru.ino.senseix.RadiusLinearLayout iconbg = _view.findViewById(R.id.iconbg);
			final LinearLayout infobg = _view.findViewById(R.id.infobg);
			final ru.ino.senseix.MaterialSwitch switch_mode = _view.findViewById(R.id.switch_mode);
			final ImageView icon = _view.findViewById(R.id.icon);
			final TextView name = _view.findViewById(R.id.name);
			final TextView packagename = _view.findViewById(R.id.packagename);
			final TextView version = _view.findViewById(R.id.version);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && infobg != null) {
				LayoutTransition transition = infobg.getLayoutTransition();
				if (transition == null) {
					transition = new LayoutTransition();
					infobg.setLayoutTransition(transition);
				}
				transition.enableTransitionType(LayoutTransition.CHANGING);
				transition.setDuration(250);
			}
			
			HashMap<String, Object> item = _data.get(_position);
			icon.setImageDrawable((Drawable) item.get("icon"));
			
			String appNameText = item.get("name").toString();
			if (appNameText.contains("Pojav")) appNameText = "Pojav Launcher";
			if (appNameText.contains("Minecraft")) appNameText = "Minecraft";
			if (appNameText.contains("Mobile Legends")) appNameText = "Mobile Legends";
			name.setText(appNameText);
			
			packagename.setText(item.get("package").toString());
			version.setText(item.get("version").toString());
			iconbg.setRadius(21);
			
			float density = getResources().getDisplayMetrics().density;
			boolean isSelected = (_position == selectedPosition);
			
			if (isSelected) {
				setGradientRipple(
				background, 
				themeHelper.getColor("colorPrimaryBtn"), 
				themeHelper.getColor("colorPrimaryDark"), 
				12f, 
				android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT
				);
				
				int pad = (int) (8 * density);
				background.setPadding(pad, pad, pad, pad);
				
				LinearLayout.LayoutParams iconParams = (LinearLayout.LayoutParams) icon.getLayoutParams();
				iconParams.width = (int) (37 * density);
				iconParams.height = (int) (37 * density);
				icon.setLayoutParams(iconParams);
				
				name.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14f);
				name.setTextColor(Color.WHITE);
				
				packagename.setVisibility(View.VISIBLE);
				version.setVisibility(View.VISIBLE);
			} else {
				int strokeColor = 0x33FFFFFF;
				int strokeWidthPx = (int) (1 * density);
				
				setTransparentStrokeRipple(
				background,
				strokeColor,
				strokeWidthPx,
				10f
				);
				
				int padTB = (int) (5 * density);
				int padLR = (int) (6 * density);
				background.setPadding(padLR, padTB, padLR, padTB);
				
				LinearLayout.LayoutParams iconParams = (LinearLayout.LayoutParams) icon.getLayoutParams();
				iconParams.width = (int) (28 * density);
				iconParams.height = (int) (28 * density);
				icon.setLayoutParams(iconParams);
				
				name.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 12f);
				name.setTextColor(0xB3FFFFFF);
				
				packagename.setVisibility(View.GONE);
				version.setVisibility(View.GONE);
			}
			
			setFont(_view); 
			
			LinearLayout.LayoutParams marginParams = (LinearLayout.LayoutParams) background.getLayoutParams();
			if (marginParams == null) {
				marginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			marginParams.setMargins(0, 0, 0, (int) (6 * density));
			background.setLayoutParams(marginParams);
			
			switch_mode.setVisibility(View.GONE);
			
			background.setOnClickListener(_view1 -> {
				if (previewicon == null || !previewicon.isAttachedToWindow()) {
					return;
				}
				
				HashMap<String, Object> item1 = _data.get(_position);
				if (item1 == null) return;
				
				Object pkgObj = item1.get("package");
				if (pkgObj == null) return;
				
				String packageName = pkgObj.toString();
				
				selectedPosition = _position;
				paketnama = packageName;
				
				selected_appname.setText(name.getText().toString());
				selected_appversion.setText(version.getText().toString());
				
				notifyDataSetChanged();
				
				background.setEnabled(false);
				loadAppBackground(packageName);
				
				background.postDelayed(() -> background.setEnabled(true), 300);
			});
			
			if (isNewView) {
				Animation slideIn = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f
				);
				slideIn.setDuration(400); 
				slideIn.setInterpolator(new DecelerateInterpolator()); 
				background.startAnimation(slideIn);
			}
			
			return _view;
		}
	}
	
	private void saveCustomBackground(String fileNameKey, Uri sourceUri) {
		try {
			// 1. Decode opsi awal untuk membaca ukuran gambar tanpa memuat ke RAM
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			
			java.io.InputStream isBounds = getContentResolver().openInputStream(sourceUri);
			BitmapFactory.decodeStream(isBounds, null, options);
			if (isBounds != null) isBounds.close();
			
			// 2. Tentukan target resolusi maksimum (Cukup 1280px untuk background layar)
			final int MAX_DIMEN = 1280;
			int width = options.outWidth;
			int height = options.outHeight;
			int sampleSize = 1;
			
			if (width > MAX_DIMEN || height > MAX_DIMEN) {
				final int halfWidth = width / 2;
				final int halfHeight = height / 2;
				while ((halfWidth / sampleSize) >= MAX_DIMEN && (halfHeight / sampleSize) >= MAX_DIMEN) {
					sampleSize *= 2;
				}
			}
			
			// 3. Load bitmap yang sudah di-downscale ke RAM
			BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
			decodeOptions.inSampleSize = sampleSize;
			
			java.io.InputStream isActual = getContentResolver().openInputStream(sourceUri);
			Bitmap scaledBitmap = BitmapFactory.decodeStream(isActual, null, decodeOptions);
			if (isActual != null) isActual.close();
			
			if (scaledBitmap == null) return;
			
			// 4. Simpan ke direktori internal
			File dir = new File(getFilesDir(), "custom_bg");
			if (!dir.exists()) dir.mkdirs();
			
			// Menggunakan format .jpg (bukan .png)
			File targetFile = new File(dir, fileNameKey + ".jpg");
			FileOutputStream fos = new FileOutputStream(targetFile);
			
			// Compress menggunakan JPEG kualitas 80% (Ukuran file turun dari ~15MB menjadi ~150-300KB)
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.flush();
			fos.close();
			
			// Recycle bitmap dari memory
			scaledBitmap.recycle();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void removeCustomBackground(String fileNameKey) {
		File file = new File(getFilesDir() + "/custom_bg/" + fileNameKey + ".jpg");
		if (file.exists()) {
			file.delete();
		}
	}
	
	private void loadAppBackground(String targetKey) {
		if (targetKey == null || targetKey.isEmpty()) {
			targetKey = "default_bg";
		}
		
		// Ubah ekstensi menjadi .jpg
		File file = new File(getFilesDir() + "/custom_bg/" + targetKey + ".jpg");
		
		if (file.exists()) {
			BackgroundHelper.setBackgroundWithGradient(
			this,
			previewicon,
			file.getAbsolutePath(),
			8,
			80,
			BackgroundHelper.GradientDirection.LEFT_RIGHT
			);
		} else {
			if ("default_bg".equals(targetKey)) {
				BackgroundHelper.setBackgroundWithGradient(
				this,
				previewicon,
				"images/background",
				8,
				80,
				BackgroundHelper.GradientDirection.LEFT_RIGHT
				);
			} else {
				BackgroundHelper.setBackgroundWithGradient(
				this,
				previewicon,
				targetKey,
				8,
				80,
				BackgroundHelper.GradientDirection.LEFT_RIGHT
				);
			}
		}
	}
}
