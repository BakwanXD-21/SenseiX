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
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.LinearLayout;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;
import rikka.shizuku.aidl.*;
import rikka.shizuku.api.*;
import rikka.shizuku.provider.*;
import ru.ino.senseix.CustomFillView;

public class SplashActivity extends Activity {
	
	private LinearLayout background;
	private CustomFillView appicon;
	
	private SharedPreferences user;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.splash);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		background = findViewById(R.id.background);
		appicon = findViewById(R.id.appicon);
		user = getSharedPreferences("data", Activity.MODE_PRIVATE);
	}
	
	private void initializeLogic() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			
			getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
			getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
			
			getWindow().getDecorView().setSystemUiVisibility(
			android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			);
		}
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
			getWindow().setNavigationBarContrastEnforced(false);
		}
		
		if (themeHelper == null) {
			themeHelper = new ru.ino.senseix.DynamicThemeHelper();
		}
		
		user = getSharedPreferences("data", Activity.MODE_PRIVATE);
		int warnaTemaTerpilih = user.getInt("color", Color.parseColor("#0D57A1"));
		themeHelper.generateFromBaseColor(warnaTemaTerpilih);
		background.setBackgroundColor(themeHelper.getColor("colorSurface"));
		
		appicon.setIcon(R.drawable.app_icon);
		appicon.setIconColor(Color.parseColor("#212121"));
		appicon.setFillColor(warnaTemaTerpilih);
		appicon.start(2000);
		
		navigateRunnable = new Runnable() {
			@Override
			public void run() {
				startActivity(new android.content.Intent(
				SplashActivity.this,
				MainActivity.class
				));
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		};
		// 3. Jalankan handler menggunakan variabel runnable
		delayHandler.postDelayed(navigateRunnable, 2007);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			
			getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
			getWindow().setNavigationBarColor(android.graphics.Color.TRANSPARENT);
			
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
				
				getWindow().setDecorFitsSystemWindows(false);
				
			} else {
				
				getWindow().getDecorView().setSystemUiVisibility(
				android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				);
				
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (delayHandler != null && navigateRunnable != null) {
			delayHandler.removeCallbacks(navigateRunnable); // Ini yang menghentikan waktu
		}
	}
	
	private Handler delayHandler = new Handler(Looper.getMainLooper());
	private Runnable navigateRunnable;
	private ru.ino.senseix.DynamicThemeHelper themeHelper;
	
	private static final int VIRTUAL_WIDTH = 411; // Mengikuti lebar standar Samsung A51
	private static final int VIRTUAL_HEIGHT = 913; // Menyesuaikan rasio tinggi layar modern (20:9)
	private Configuration virtualConfig;
	private DisplayMetrics virtualMetrics;
	
	@Override
	protected void attachBaseContext(Context newBase) {
		Context wrapped = wrapContext(newBase);
		virtualConfig = new Configuration(wrapped.getResources().getConfiguration());
		virtualMetrics = new DisplayMetrics();
		virtualMetrics.setTo(wrapped.getResources().getDisplayMetrics());
		super.attachBaseContext(wrapped);
	}
	
	private Context wrapContext(Context base) {
		DisplayMetrics dm = base.getResources().getDisplayMetrics();
		float scale = (float) dm.widthPixels / VIRTUAL_WIDTH;
		
		// Menggunakan basis 160 (mdpi) agar penskalaan density pas dengan lebar 411
		int targetDpi = (int) (scale * 160); 
		
		Configuration config = new Configuration(base.getResources().getConfiguration());
		config.densityDpi = targetDpi;
		config.fontScale = 1.0f;
		config.screenWidthDp = VIRTUAL_WIDTH;
		config.screenHeightDp = VIRTUAL_HEIGHT;
		config.smallestScreenWidthDp = VIRTUAL_WIDTH;
		
		Context context = base.createConfigurationContext(config);
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		metrics.density = targetDpi / 160f;
		metrics.densityDpi = targetDpi;
		metrics.scaledDensity = metrics.density;
		metrics.widthPixels = dm.widthPixels;
		metrics.heightPixels = dm.heightPixels;
		
		return context;
	}
	
	@Override
	public void applyOverrideConfiguration(Configuration overrideConfiguration) {
		if (overrideConfiguration != null && virtualConfig != null) {
			overrideConfiguration.setTo(virtualConfig);
		}
		super.applyOverrideConfiguration(overrideConfiguration);
	}
	
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		if (virtualConfig != null && virtualMetrics != null) {
			res.updateConfiguration(virtualConfig, virtualMetrics);
		}
		return res;
	}
}