package ru.ino.senseix;

import ru.ino.senseix.SplashActivity;
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
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import ru.ino.senseix.InoBottomNavigation;
import ru.ino.senseix.InoInputLayout;
import ru.ino.senseix.InoLoadingIndicator;
import ru.ino.senseix.InoSliderBtn;
import ru.ino.senseix.InoViewPager;
import ru.ino.senseix.MaterialSwitch;
import ru.ino.senseix.SimpleSwipeRefreshLayout;
import ru.ino.senseix.SliderRange;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.view.inputmethod.InputMethodManager;
import android.provider.Settings;


public class MainActivity extends Activity {
	
	private String ansioutput = "";
	private String onFinish = "";
	private String fromShell = "";
	private String type = "";
	private String action = "";
	private String onModule = "";
	
	private ArrayList<HashMap<String, Object>> apps = new ArrayList<>();
	
	private LinearLayout background;
	private LinearLayout mainscreen;
	private LinearLayout shellscreen;
	private LinearLayout settingscreen;
	private LinearLayout toolbar_mainscreen;
	private FrameLayout viewpagerbg;
	private LinearLayout titlebar_mainscreen;
	private ImageView infobtn;
	private ImageView settingbtn;
	private TextView currentview_title;
	private TextView currentview_subtitle;
	private InoViewPager viewpager;
	private LinearLayout bottombar;
	private LinearLayout homepage;
	private LinearLayout gamespage;
	private LinearLayout modulepage;
	private ScrollView homemenubg;
	private LinearLayout activationpage;
	private FrameLayout activationbg;
	private LinearLayout deviceinfobg;
	private InoSliderBtn sengame_openbtn;
	private LinearLayout supportmebtn;
	private LinearLayout mecommunitybtn;
	private ImageView activation_icon;
	private LinearLayout activation_information;
	private TextView activation_title;
	private TextView activation_subtitle;
	private LinearLayout devicebg;
	private LinearLayout boardbg;
	private LinearLayout archbg;
	private ImageView device_icon;
	private LinearLayout device_information;
	private TextView device_title;
	private TextView device_subtitle;
	private ImageView board_icon;
	private LinearLayout board_information;
	private TextView board_title;
	private TextView board_subtitle;
	private ImageView arch_icon;
	private LinearLayout arch_information;
	private TextView arch_title;
	private TextView arch_subtitle;
	private LinearLayout supportme_detailbg;
	private ImageView supportme_icon;
	private TextView supportme_title;
	private TextView supportme_subtitle;
	private LinearLayout mecommunity_detailbg;
	private ImageView mecommunity_icon;
	private TextView mecommunity_title;
	private TextView mecommunity_subtitle;
	private LinearLayout searchbar;
	private SimpleSwipeRefreshLayout swiperefreshlayout;
	private ImageView search_icon;
	private EditText inputsearch;
	private ImageView clearbutton;
	private ImageView morebtn;
	private ListView list;
	private ListView listmodule;
	private FrameLayout moduleiconbg;
	private TextView moduledetail;
	private InoLoadingIndicator indicator;
	private ImageView moduleicon;
	private ImageView actionbtn;
	private InoBottomNavigation bottomnavigation;
	private LinearLayout toolbar_shellscreen;
	private LinearLayout shellinputbg;
	private ScrollView shelloutputbg;
	private ImageView backbtn_shellscreen;
	private TextView shellscreen_title;
	private ImageView settingbtn_shellscreen;
	private EditText shell_input;
	private ImageView exec_button;
	private TextView shell_output;
	private LinearLayout toolbar_settingscreen;
	private ScrollView settingmenubg;
	private ImageView backbtn_settingscreen;
	private TextView settingscreen_title;
	private LinearLayout settingitembg;
	private LinearLayout itemsetting_terminaltextsize;
	private LinearLayout itemsetting_appearances;
	private LinearLayout customdns_feature;
	private LinearLayout customreso_feature;
	private LinearLayout customdpi_feature;
	private LinearLayout monitoring_feature;
	private LinearLayout itemsetting_terminaltextsize_infobg;
	private LinearLayout itemsetting_terminaltextsize_sliderbg;
	private ImageView itemsetting_terminaltextsize_icon;
	private LinearLayout itemsetting_terminaltextsize_detailbg;
	private TextView itemsetting_terminaltextsize_value;
	private TextView itemsetting_terminaltextsize_title;
	private TextView itemsetting_terminaltextsize_subtitle;
	private SliderRange itemsetting_terminaltextsize_slider;
	private LinearLayout appearancetitlebg;
	private LinearLayout appearancesitembg;
	private ImageView appearancetitle_icon;
	private LinearLayout appearancetitlebar;
	private TextView appearancetitle;
	private TextView appearancesubtitle;
	private LinearLayout appearanceitem_moduleinstall;
	private LinearLayout appearanceitem_onfinish;
	private LinearLayout appearanceitem_ansi;
	private LinearLayout appearanceitem_palette;
	private ImageView appearanceitem_iconmodule;
	private LinearLayout appearanceitem_moduleinstall_detail;
	private MaterialSwitch switch_moduleinstall;
	private TextView appearanceitem_moduleinstall_title;
	private TextView appearanceitem_moduleinstall_subtitle;
	private ImageView appearanceitem_iconfinish;
	private LinearLayout appearanceitem_onfinish_detail;
	private MaterialSwitch switch_onfinish;
	private TextView appearanceitem_onfinish_title;
	private TextView appearanceitem_onfinish_subtitle;
	private ImageView appearanceitem_iconansi;
	private LinearLayout appearanceitem_ansi_detail;
	private MaterialSwitch switch_ansi;
	private TextView appearanceitem_ansi_title;
	private TextView appearanceitem_ansi_subtitle;
	private ImageView appearanceitem_icontheme;
	private LinearLayout appearanceitem_palette_detail;
	private TextView appearanceitem_palette_title;
	private TextView appearanceitem_palette_subtitle;
	private LinearLayout customdns_titlebar;
	private LinearLayout inputdnsbg;
	private MaterialSwitch switch_customdns;
	private ImageView customdns_icon;
	private LinearLayout customdns_feature_detailbg;
	private TextView customdns_feature_title;
	private TextView customdns_feature_subtitle;
	private InoInputLayout inputdns1;
	private InoInputLayout inputdns2;
	private LinearLayout customres_titlebar;
	private LinearLayout inputresobg;
	private LinearLayout buttonresrow;
	private ImageView customres_icon;
	private LinearLayout customreso_feature_detailbg;
	private TextView customreso_feature_title;
	private TextView customreso_feature_subtitle;
	private InoInputLayout inputwidth;
	private InoInputLayout inputheight;
	private LinearLayout resetresobtn;
	private LinearLayout applyresobtn;
	private ImageView resetresobtnicon;
	private ImageView applyresobtnicon;
	private TextView applyresobtntext;
	private LinearLayout customdpi_titlebar;
	private InoInputLayout inputdpi;
	private LinearLayout buttondpirow;
	private ImageView customdpi_icon;
	private LinearLayout customdpi_feature_detailbg;
	private TextView customdpi_feature_title;
	private TextView customdpi_feature_subtitle;
	private LinearLayout resetdpibtn;
	private LinearLayout applydpibtn;
	private ImageView resetdpibtnicon;
	private ImageView applydpibtnicon;
	private TextView applydpibtntext;
	private LinearLayout monitoring_feature_detailbg;
	private MaterialSwitch switch_monitor;
	private HorizontalScrollView monitoritem_scrollbg;
	private LinearLayout monitoring_titlebar;
	private TextView monitoring_feature_subtitle;
	private ImageView monitoring_icon;
	private TextView monitoring_feature_title;
	private LinearLayout monitoritem_bg;
	private LinearLayout item_signalbg;
	private LinearLayout item_bateraibg;
	private LinearLayout item_suhubg;
	private LinearLayout item_fpsbg;
	private LinearLayout item_waktubg;
	private ImageView signal_ic;
	private TextView text_signal;
	private ImageView battery_ic;
	private TextView text_battery;
	private ImageView temp_ic;
	private TextView text_temp;
	private ImageView fps_ic;
	private TextView text_fps;
	private ImageView time_ic;
	private TextView text_time;
	
	private ShizukuHelper inoka;
	private SharedPreferences user;
    
    private LinearLayout itemsetting_language;
private ImageView itemsetting_language_icon;
private TextView itemsetting_language_value;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		background = findViewById(R.id.background);
		mainscreen = findViewById(R.id.mainscreen);
		shellscreen = findViewById(R.id.shellscreen);
		settingscreen = findViewById(R.id.settingscreen);
		toolbar_mainscreen = findViewById(R.id.toolbar_mainscreen);
		viewpagerbg = findViewById(R.id.viewpagerbg);
		titlebar_mainscreen = findViewById(R.id.titlebar_mainscreen);
		infobtn = findViewById(R.id.infobtn);
		settingbtn = findViewById(R.id.settingbtn);
		currentview_title = findViewById(R.id.currentview_title);
		currentview_subtitle = findViewById(R.id.currentview_subtitle);
		viewpager = findViewById(R.id.viewpager);
		bottombar = findViewById(R.id.bottombar);
		homepage = findViewById(R.id.homepage);
		gamespage = findViewById(R.id.gamespage);
		modulepage = findViewById(R.id.modulepage);
		homemenubg = findViewById(R.id.homemenubg);
		activationpage = findViewById(R.id.activationpage);
		activationbg = findViewById(R.id.activationbg);
		deviceinfobg = findViewById(R.id.deviceinfobg);
		sengame_openbtn = findViewById(R.id.sengame_openbtn);
		supportmebtn = findViewById(R.id.supportmebtn);
		mecommunitybtn = findViewById(R.id.mecommunitybtn);
		activation_icon = findViewById(R.id.activation_icon);
		activation_information = findViewById(R.id.activation_information);
		activation_title = findViewById(R.id.activation_title);
		activation_subtitle = findViewById(R.id.activation_subtitle);
		devicebg = findViewById(R.id.devicebg);
		boardbg = findViewById(R.id.boardbg);
		archbg = findViewById(R.id.archbg);
		device_icon = findViewById(R.id.device_icon);
		device_information = findViewById(R.id.device_information);
		device_title = findViewById(R.id.device_title);
		device_subtitle = findViewById(R.id.device_subtitle);
		board_icon = findViewById(R.id.board_icon);
		board_information = findViewById(R.id.board_information);
		board_title = findViewById(R.id.board_title);
		board_subtitle = findViewById(R.id.board_subtitle);
		arch_icon = findViewById(R.id.arch_icon);
		arch_information = findViewById(R.id.arch_information);
		arch_title = findViewById(R.id.arch_title);
		arch_subtitle = findViewById(R.id.arch_subtitle);
		supportme_detailbg = findViewById(R.id.supportme_detailbg);
		supportme_icon = findViewById(R.id.supportme_icon);
		supportme_title = findViewById(R.id.supportme_title);
		supportme_subtitle = findViewById(R.id.supportme_subtitle);
		mecommunity_detailbg = findViewById(R.id.mecommunity_detailbg);
		mecommunity_icon = findViewById(R.id.mecommunity_icon);
		mecommunity_title = findViewById(R.id.mecommunity_title);
		mecommunity_subtitle = findViewById(R.id.mecommunity_subtitle);
		searchbar = findViewById(R.id.searchbar);
		swiperefreshlayout = findViewById(R.id.swiperefreshlayout);
		search_icon = findViewById(R.id.search_icon);
		inputsearch = findViewById(R.id.inputsearch);
		clearbutton = findViewById(R.id.clearbutton);
		morebtn = findViewById(R.id.morebtn);
		list = findViewById(R.id.list);
		listmodule = findViewById(R.id.listmodule);
		moduleiconbg = findViewById(R.id.moduleiconbg);
		moduledetail = findViewById(R.id.moduledetail);
		indicator = findViewById(R.id.indicator);
		moduleicon = findViewById(R.id.moduleicon);
		actionbtn = findViewById(R.id.actionbtn);
		bottomnavigation = findViewById(R.id.bottomnavigation);
		toolbar_shellscreen = findViewById(R.id.toolbar_shellscreen);
		shellinputbg = findViewById(R.id.shellinputbg);
		shelloutputbg = findViewById(R.id.shelloutputbg);
		backbtn_shellscreen = findViewById(R.id.backbtn_shellscreen);
		shellscreen_title = findViewById(R.id.shellscreen_title);
		settingbtn_shellscreen = findViewById(R.id.settingbtn_shellscreen);
		shell_input = findViewById(R.id.shell_input);
		exec_button = findViewById(R.id.exec_button);
		shell_output = findViewById(R.id.shell_output);
		toolbar_settingscreen = findViewById(R.id.toolbar_settingscreen);
		settingmenubg = findViewById(R.id.settingmenubg);
		backbtn_settingscreen = findViewById(R.id.backbtn_settingscreen);
		settingscreen_title = findViewById(R.id.settingscreen_title);
		settingitembg = findViewById(R.id.settingitembg);
		itemsetting_terminaltextsize = findViewById(R.id.itemsetting_terminaltextsize);
		itemsetting_appearances = findViewById(R.id.itemsetting_appearances);
		itemsetting_language = findViewById(R.id.itemsetting_language);
itemsetting_language_icon = findViewById(R.id.itemsetting_language_icon);
itemsetting_language_value = findViewById(R.id.itemsetting_language_value);
        customdns_feature = findViewById(R.id.customdns_feature);
		customreso_feature = findViewById(R.id.customreso_feature);
		customdpi_feature = findViewById(R.id.customdpi_feature);
		monitoring_feature = findViewById(R.id.monitoring_feature);
		itemsetting_terminaltextsize_infobg = findViewById(R.id.itemsetting_terminaltextsize_infobg);
		itemsetting_terminaltextsize_sliderbg = findViewById(R.id.itemsetting_terminaltextsize_sliderbg);
		itemsetting_terminaltextsize_icon = findViewById(R.id.itemsetting_terminaltextsize_icon);
		itemsetting_terminaltextsize_detailbg = findViewById(R.id.itemsetting_terminaltextsize_detailbg);
		itemsetting_terminaltextsize_value = findViewById(R.id.itemsetting_terminaltextsize_value);
		itemsetting_terminaltextsize_title = findViewById(R.id.itemsetting_terminaltextsize_title);
		itemsetting_terminaltextsize_subtitle = findViewById(R.id.itemsetting_terminaltextsize_subtitle);
		itemsetting_terminaltextsize_slider = findViewById(R.id.itemsetting_terminaltextsize_slider);
		appearancetitlebg = findViewById(R.id.appearancetitlebg);
		appearancesitembg = findViewById(R.id.appearancesitembg);
		appearancetitle_icon = findViewById(R.id.appearancetitle_icon);
		appearancetitlebar = findViewById(R.id.appearancetitlebar);
		appearancetitle = findViewById(R.id.appearancetitle);
		appearancesubtitle = findViewById(R.id.appearancesubtitle);
		appearanceitem_moduleinstall = findViewById(R.id.appearanceitem_moduleinstall);
		appearanceitem_onfinish = findViewById(R.id.appearanceitem_onfinish);
		appearanceitem_ansi = findViewById(R.id.appearanceitem_ansi);
		appearanceitem_palette = findViewById(R.id.appearanceitem_palette);
		appearanceitem_iconmodule = findViewById(R.id.appearanceitem_iconmodule);
		appearanceitem_moduleinstall_detail = findViewById(R.id.appearanceitem_moduleinstall_detail);
		switch_moduleinstall = findViewById(R.id.switch_moduleinstall);
		appearanceitem_moduleinstall_title = findViewById(R.id.appearanceitem_moduleinstall_title);
		appearanceitem_moduleinstall_subtitle = findViewById(R.id.appearanceitem_moduleinstall_subtitle);
		appearanceitem_iconfinish = findViewById(R.id.appearanceitem_iconfinish);
		appearanceitem_onfinish_detail = findViewById(R.id.appearanceitem_onfinish_detail);
		switch_onfinish = findViewById(R.id.switch_onfinish);
		appearanceitem_onfinish_title = findViewById(R.id.appearanceitem_onfinish_title);
		appearanceitem_onfinish_subtitle = findViewById(R.id.appearanceitem_onfinish_subtitle);
		appearanceitem_iconansi = findViewById(R.id.appearanceitem_iconansi);
		appearanceitem_ansi_detail = findViewById(R.id.appearanceitem_ansi_detail);
		switch_ansi = findViewById(R.id.switch_ansi);
		appearanceitem_ansi_title = findViewById(R.id.appearanceitem_ansi_title);
		appearanceitem_ansi_subtitle = findViewById(R.id.appearanceitem_ansi_subtitle);
		appearanceitem_icontheme = findViewById(R.id.appearanceitem_icontheme);
		appearanceitem_palette_detail = findViewById(R.id.appearanceitem_palette_detail);
		appearanceitem_palette_title = findViewById(R.id.appearanceitem_palette_title);
		appearanceitem_palette_subtitle = findViewById(R.id.appearanceitem_palette_subtitle);
		customdns_titlebar = findViewById(R.id.customdns_titlebar);
		inputdnsbg = findViewById(R.id.inputdnsbg);
		switch_customdns = findViewById(R.id.switch_customdns);
		customdns_icon = findViewById(R.id.customdns_icon);
		customdns_feature_detailbg = findViewById(R.id.customdns_feature_detailbg);
		customdns_feature_title = findViewById(R.id.customdns_feature_title);
		customdns_feature_subtitle = findViewById(R.id.customdns_feature_subtitle);
		inputdns1 = findViewById(R.id.inputdns1);
		inputdns2 = findViewById(R.id.inputdns2);
		customres_titlebar = findViewById(R.id.customres_titlebar);
		inputresobg = findViewById(R.id.inputresobg);
		buttonresrow = findViewById(R.id.buttonresrow);
		customres_icon = findViewById(R.id.customres_icon);
		customreso_feature_detailbg = findViewById(R.id.customreso_feature_detailbg);
		customreso_feature_title = findViewById(R.id.customreso_feature_title);
		customreso_feature_subtitle = findViewById(R.id.customreso_feature_subtitle);
		inputwidth = findViewById(R.id.inputwidth);
		inputheight = findViewById(R.id.inputheight);
		resetresobtn = findViewById(R.id.resetresobtn);
		applyresobtn = findViewById(R.id.applyresobtn);
		resetresobtnicon = findViewById(R.id.resetresobtnicon);
		applyresobtnicon = findViewById(R.id.applyresobtnicon);
		applyresobtntext = findViewById(R.id.applyresobtntext);
		customdpi_titlebar = findViewById(R.id.customdpi_titlebar);
		inputdpi = findViewById(R.id.inputdpi);
		buttondpirow = findViewById(R.id.buttondpirow);
		customdpi_icon = findViewById(R.id.customdpi_icon);
		customdpi_feature_detailbg = findViewById(R.id.customdpi_feature_detailbg);
		customdpi_feature_title = findViewById(R.id.customdpi_feature_title);
		customdpi_feature_subtitle = findViewById(R.id.customdpi_feature_subtitle);
		resetdpibtn = findViewById(R.id.resetdpibtn);
		applydpibtn = findViewById(R.id.applydpibtn);
		resetdpibtnicon = findViewById(R.id.resetdpibtnicon);
		applydpibtnicon = findViewById(R.id.applydpibtnicon);
		applydpibtntext = findViewById(R.id.applydpibtntext);
		monitoring_feature_detailbg = findViewById(R.id.monitoring_feature_detailbg);
		switch_monitor = findViewById(R.id.switch_monitor);
		monitoritem_scrollbg = findViewById(R.id.monitoritem_scrollbg);
		monitoring_titlebar = findViewById(R.id.monitoring_titlebar);
		monitoring_feature_subtitle = findViewById(R.id.monitoring_feature_subtitle);
		monitoring_icon = findViewById(R.id.monitoring_icon);
		monitoring_feature_title = findViewById(R.id.monitoring_feature_title);
		monitoritem_bg = findViewById(R.id.monitoritem_bg);
		item_signalbg = findViewById(R.id.item_signalbg);
		item_bateraibg = findViewById(R.id.item_bateraibg);
		item_suhubg = findViewById(R.id.item_suhubg);
		item_fpsbg = findViewById(R.id.item_fpsbg);
		item_waktubg = findViewById(R.id.item_waktubg);
		signal_ic = findViewById(R.id.signal_ic);
		text_signal = findViewById(R.id.text_signal);
		battery_ic = findViewById(R.id.battery_ic);
		text_battery = findViewById(R.id.text_battery);
		temp_ic = findViewById(R.id.temp_ic);
		text_temp = findViewById(R.id.text_temp);
		fps_ic = findViewById(R.id.fps_ic);
		text_fps = findViewById(R.id.text_fps);
		time_ic = findViewById(R.id.time_ic);
		text_time = findViewById(R.id.text_time);
		user = getSharedPreferences("data", Activity.MODE_PRIVATE);
		
		infobtn.setOnClickListener(_v -> showCustomBottomSheet());
		
		settingbtn.setOnClickListener(_v -> {
			fromShell = "false";
			mainscreen.setVisibility(View.GONE);
			settingscreen.setVisibility(View.VISIBLE);
			itemsetting_terminaltextsize.setVisibility(View.GONE);
		});
		
		activationbg.setOnClickListener(_v -> {
			int targetColor = (themeHelper != null) ? themeHelper.getColor("colorPrimary") : 0xFF0D47A1;
			int targetColorEnd = (themeHelper != null) ? themeHelper.getColor("colorPrimaryContainer") : 0xFF1565C0;
			
			final Dialog loading = new Dialog(MainActivity.this);
			loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
			loading.setContentView(R.layout.loading);
			
			final LinearLayout background = loading.findViewById(R.id.background);
			final LinearLayout loadingbg = loading.findViewById(R.id.loadingbg);
			final ru.ino.senseix.InoCircularProgress circular = loading.findViewById(R.id.circular);
			final ru.ino.senseix.InoLoadingIndicator indicator = loading.findViewById(R.id.indicator);
			final ru.ino.senseix.CustomFillView appicon = loading.findViewById(R.id.appicon);
			
			indicator.setColor(targetColor);
			circular.setTrackActiveColor(targetColor);
			circular.setTrackInactiveColor(0xFF282828);
			
			circular.setIndeterminate(true);
			indicator.setVisibility(View.GONE);
			appicon.setVisibility(View.GONE);
			
			if (loading.getWindow() != null) {
				loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				loading.getWindow().setGravity(Gravity.CENTER);
			}
			
			loading.show();
			
			new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
				@Override
				public void run() {
					if (loading != null && loading.isShowing()) {
						loading.dismiss();
					}
					
					// 1. Jika Shizuku belum siap sama sekali (Service mati)
					if (!inoka.isBinderReady()) {
						Intent intent = getPackageManager()
						.getLaunchIntentForPackage("moe.shizuku.privileged.api");
						if (intent != null) {
							startActivity(intent);
						} else {
							Toast.makeText(MainActivity.this, "Shizuku App tidak ditemukan!", Toast.LENGTH_SHORT).show();
						}
						return;
					}
					
					// 2. Jika Shizuku hidup tetapi izin belum diberikan
					if (!inoka.isPermissionGranted()) {
						inoka.requestPermissionIfNeeded();
						return;
					}
					
					// 3. Jika Binder Ready DAN Permission sudah Granted
					activationbg.setEnabled(false);
					
					activation_title.setText("SenseiX is Online");
					activation_subtitle.setText("Service was activated!");
					activation_icon.setImageResource(R.drawable.app_icon);
					
					setGradientRipple(
					activationbg,
					targetColor,
					targetColorEnd,
					26,
					GradientDrawable.Orientation.TOP_BOTTOM
					);
				}
			}, 700);
			
		});
		
		supportmebtn.setOnClickListener(_v -> {
			Intent intent = new Intent(Intent.ACTION_VIEW,
			Uri.parse(c1qwR3oIn0.elpis(c1qwR3oIn0.LeonSKenedey("216767212121672121216767212167672167212167676721216767676721672121676721216721212121676721216767216721672121216721216767672121672167212167216767216721212121676721676721672121212167676721676767216767212121676721672121672121212167212121212167216767672121212121676721216767212167672167672167216767216721212121676767672121672167672121216767212167672121676721672121672167212167672167672121216721676721216721676721672121672167672167672121216767216721676721676721212121672167212121676767216767216767676721676721676767672167216721216721212167672121212121216767216721672121676721216721216767212121676721672121216767672167216767216721216767672121212121676721212121672167212121672167212167672167216721672121672167212167216721216767216721212121216721216767676721672121676767672167"))));
			startActivity(intent);
		});
		
		mecommunitybtn.setOnClickListener(_v -> {
			Intent intent = new Intent(Intent.ACTION_VIEW,
			Uri.parse(c1qwR3oIn0.elpis(c1qwR3oIn0.LeonSKenedey("21676721212167212121676721216767216721216767672121676767672167212167672121672121212167672121676721672167212121672121676767212167216721216721676721672121212167672167672167212121216767216721676721676721212167212121676721216721216721676721672121676767672167212167212167216767216721676721212121672121212167212167676721676721216721676721672121676721676767212167212167676721212167672121212121672167672167212167672167676721216767212167212121216767212167672167212167216767216721672167676721672167212167212167672167676767216767212121216721676721672121672167672167212121216721216767676721676721212167212121676721212121216721216767216721676767672121212167216721216767212167672121212121216767212121672167672167216767216721676721216721216767212167672121676721212167216721672121676721676721216721212167672167216767216721212121216721216767212121672167216721676721216767216721676721676767212121212167216767212121216721672167212121672121216767672167672167212121216767676721216721672167212121672167216721672167216721216767672121672167216721672167212167676767216721216721212121672167212121672121676721676721216721676721672121672121216767672167676721216767216767216721212121676721216721212167676767216721216767672121212121676721676721672167212167212167216721672167676721676767212121212167676721216721216767212121676721676721672167212167672167676767212167672121216721672121672121672167216721676767216721676721672121676767212121672167672121216767216721672167212121676721676767672121676721216721"))));
			startActivity(intent);
		});
		
		clearbutton.setOnClickListener(_v -> inputsearch.setText(""));
		
		morebtn.setOnClickListener(_v -> {
	Context context = MainActivity.this;
	float dp = getResources().getDisplayMetrics().density;
	android.graphics.Typeface inoFont = android.graphics.Typeface.createFromAsset(getAssets(), "fonts/main.ttf");
	int popupWidth = (int)(180 * dp);
	
	// --- WARNA TEMA MATERIAL 3 (DARK MODE) ---
	final int M3_SURFACE_CONTAINER = themeHelper.getColor("colorSurface");
	final int M3_ON_SURFACE = themeHelper.getColor("colorOnSurfaceVariant");
	
	// Root layout untuk PopupWindow (M3 Style)
	LinearLayout root = new LinearLayout(context);
	root.setOrientation(LinearLayout.VERTICAL);
	root.setPadding(0,18,0,18);
	root.setBackground(new GradientDrawable() {{
			setColor(M3_SURFACE_CONTAINER); 
			setCornerRadius(16 * dp);
		}});
	
	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
		root.setOutlineProvider(new android.view.ViewOutlineProvider() {
			@Override
			public void getOutline(View view, android.graphics.Outline outline) {
				outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 16 * dp);
			}
		});
		root.setClipToOutline(true);
	}
	
	final PopupWindow popup = new PopupWindow(root, popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
	popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	popup.setOutsideTouchable(true);
	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
		popup.setElevation(6 * dp);
	}
	
	// Key dari LanguageLoader (teks ikut bahasa aktif), fallback Inggris
	final String[] keys = {"menu_all", "menu_system", "menu_game", "menu_user"};
	final String[] fallbacks = {"All", "System", "Game", "User"};
	
	for (int i = 0; i < keys.length; i++) {
		final String key = keys[i];
		
		LinearLayout row = new LinearLayout(context);
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setGravity(Gravity.CENTER_VERTICAL);
		row.setPadding((int)(16 * dp), (int)(12 * dp), (int)(16 * dp), (int)(12 * dp)); 
		
		TextView menuItem = new TextView(context);
		menuItem.setText(LanguageLoader.get(key, fallbacks[i]));
		menuItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		menuItem.setTextColor(M3_ON_SURFACE);
		menuItem.setSingleLine(true);
		menuItem.setTypeface(inoFont);
		menuItem.setEllipsize(TextUtils.TruncateAt.END);
		row.addView(menuItem);
		
		row.setBackgroundResource(R.drawable.ripple);
		row.setClickable(true);
		row.setFocusable(true);
		
		row.setOnClickListener(v1 -> {
			popup.dismiss();
			switch (key) {
				case "menu_all":
				loadApplications(AppListHelper.MODE_ALL); 
				type = "0";
				break;
				
				case "menu_system":
				loadApplications(AppListHelper.MODE_SYSTEM);
				type = "1";
				break;
				
				case "menu_game":
				loadApplications(AppListHelper.MODE_GAMES);
				type = "2";
				break;
				
				case "menu_user":
				loadApplications(AppListHelper.MODE_USER);
				type = "3";
				break;
			}
		});
		
		root.addView(row, new LinearLayout.LayoutParams(
		LinearLayout.LayoutParams.MATCH_PARENT, 
		ViewGroup.LayoutParams.WRAP_CONTENT
		));
	}
	
	popup.showAsDropDown(morebtn, 0, (int)(4 * dp));
});
		
		actionbtn.setOnClickListener(_v -> {
			if (action.equals("terminal")) {
				mainscreen.setVisibility(View.GONE);
				shellscreen.setVisibility(View.VISIBLE);
				View currentView = getCurrentFocus();
				if (currentView != null && currentView.getContext() instanceof Activity) {
					((Activity) currentView.getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				}
			} else {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(Intent.createChooser(intent, "Pilih File Module"), 1001);
			}
		});
		
		backbtn_shellscreen.setOnClickListener(_v -> {
			mainscreen.setVisibility(View.VISIBLE);
			shellscreen.setVisibility(View.GONE);
		});
		
		settingbtn_shellscreen.setOnClickListener(_v -> {
			fromShell = "true";
			shellscreen.setVisibility(View.GONE);
			settingscreen.setVisibility(View.VISIBLE);
			itemsetting_terminaltextsize.setVisibility(View.VISIBLE);
		});
		
		exec_button.setOnClickListener(_v -> {
			if (shell_input.getText().toString().equals("CRTL + C")) {
				inoka.sendCtrlC(new ShizukuHelper.Callback() {
					@Override
					public void onOutput(String line) {}
					@Override
					public void onFinish(String result) {}
				});
				
				shell_input.setText("");
				
				return;
			}
			
			if (shell_input.getText().toString().equals("senseix-sengame")) {
				startActivity(new android.content.Intent(
				MainActivity.this,
				SengameActivity.class
				));
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				
				shell_input.setText("");
				return;
			}
			
			if (shell_input.getText().toString().equals("clear")) {
				shell_output.setText("Use shell command, at your own risk.\n");
			} else {
				if (shell_input.getText().toString().equals("wm size reset")) {
					inputwidth.getEditText().setText("");
					inputheight.getEditText().setText("");
				}
				if (shell_input.getText().toString().equals("wm density reset")) inputdpi.getEditText().setText("");
				if (ansioutput.equals("true")) {
					inoka.exec(shell_input.getText().toString(), new ShizukuHelper.Callback() {
						@Override
						public void onOutput(String line) {
							// 1. Ubah string mentah ANSI menjadi Spanned HTML berwaran
							android.text.Spanned coloredLine = AnsiHtmlHelper.parse(line);
							
							// 2. Tambahkan ke TextView (Gunakan append agar format warna tidak menimpa teks sebelumnya)
							shell_output.append(coloredLine);
							shell_output.append("\n");
							
							// Auto-scroll ke paling bawah setiap kali ada baris baru masuk  
							shelloutputbg.post(() -> {  
								shelloutputbg.fullScroll(android.view.View.FOCUS_DOWN);  
							});  
						}  
						
						@Override  
						public void onFinish(String result) {  
							if (onFinish.equals("true")) {
								shell_output.append(result + "\n");  
								
								shelloutputbg.post(() -> {  
									shelloutputbg.fullScroll(android.view.View.FOCUS_DOWN);  
								});  
							}
						}
					});
					
				} else {
					inoka.exec(shell_input.getText().toString(), new ShizukuHelper.Callback() {
						@Override
						public void onOutput(String line) {
							shell_output.append(line);
							shell_output.append("\n");
							
							shelloutputbg.post(() -> {  
								shelloutputbg.fullScroll(android.view.View.FOCUS_DOWN);  
							});  
						}  
						
						@Override  
						public void onFinish(String result) {  
							if (onFinish.equals("true")) {
								shell_output.append(result + "\n");  
								
								shelloutputbg.post(() -> {  
									shelloutputbg.fullScroll(android.view.View.FOCUS_DOWN);  
								});  
							}
						}
					});
					
				}
			}
			
			shell_input.setText("");
		});
		
		backbtn_settingscreen.setOnClickListener(_v -> {
			if (fromShell.equals("true")) {
				shellscreen.setVisibility(View.VISIBLE);
				settingscreen.setVisibility(View.GONE);
			}
			if (fromShell.equals("false")) {
				mainscreen.setVisibility(View.VISIBLE);
				settingscreen.setVisibility(View.GONE);
			}
			
		});
		
		itemsetting_terminaltextsize.setOnClickListener(_v -> {
			// Ambil root parent dari layout kamu (misal LinearLayout terluar atau RelativeLayout)
			ViewGroup parentLayout = (ViewGroup) itemsetting_terminaltextsize.getParent();
			
			if (itemsetting_terminaltextsize_sliderbg.getVisibility() == View.GONE) {
				itemsetting_terminaltextsize_sliderbg.setVisibility(View.VISIBLE);
				
				// Expand ke ukuran spesifik (misal: lebar Match_Parent, tinggi 400px) dengan durasi 200ms
				LayoutTransitionHelper.expand(
				parentLayout, 
				itemsetting_terminaltextsize, 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				200L
				);
			} else {
				itemsetting_terminaltextsize_sliderbg.setVisibility(View.GONE);
				
				// Collapse otomatis kembali ke murni WRAP_CONTENT bawaan XML awal dengan durasi 200ms
				LayoutTransitionHelper.collapse(
				parentLayout, 
				itemsetting_terminaltextsize, 
				200L
				);
			}
			
		});
		
		itemsetting_appearances.setOnClickListener(_v -> {
			// Ambil root parent dari layout kamu (misal LinearLayout terluar atau RelativeLayout)
			ViewGroup parentLayout = (ViewGroup) itemsetting_appearances.getParent();
			
			if (appearancesitembg.getVisibility() == View.GONE) {
				appearancesitembg.setVisibility(View.VISIBLE);
				
				// Expand ke ukuran spesifik (misal: lebar Match_Parent, tinggi 400px) dengan durasi 200ms
				LayoutTransitionHelper.expand(
				parentLayout, 
				itemsetting_appearances, 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				200L
				);
			} else {
				appearancesitembg.setVisibility(View.GONE);
				
				// Collapse otomatis kembali ke murni WRAP_CONTENT bawaan XML awal dengan durasi 200ms
				LayoutTransitionHelper.collapse(
				parentLayout, 
				itemsetting_appearances, 
				200L
				);
			}
			
		});
		
		monitoring_feature.setOnClickListener(_v -> switch_monitor.performClick());
		
		itemsetting_terminaltextsize_slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				itemsetting_terminaltextsize_value.setText(String.valueOf((long)(_progressValue)));
				shell_output.setTextSize((int)_progressValue);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				
			}
		});
		
		appearanceitem_moduleinstall.setOnClickListener(_v -> switch_moduleinstall.performClick());
		
		appearanceitem_onfinish.setOnClickListener(_v -> switch_onfinish.performClick());
		
		appearanceitem_ansi.setOnClickListener(_v -> switch_ansi.performClick());
		
		appearanceitem_palette.setOnClickListener(_v -> tampilkanColorPickerDialog());
		
        itemsetting_language.setOnClickListener(_v ->
	LanguageMenu.show(MainActivity.this, themeHelper, LanguageMenu.saved(user), code -> applyLanguage(code)));
    
		switch_moduleinstall.setOnCheckedChangeListener((_buttonView, _isChecked) -> {
			user.edit().putBoolean("onmodule", _isChecked).apply();
			onModule = _isChecked ? "true" : "false";
		});
		
		switch_onfinish.setOnCheckedChangeListener((_buttonView, _isChecked) -> {
			user.edit().putBoolean("onfinish", _isChecked).apply();
			onFinish = _isChecked ? "true" : "false";
		});
		
		switch_ansi.setOnCheckedChangeListener((_buttonView, _isChecked) -> {
			user.edit().putBoolean("ansi", _isChecked).apply();
			ansioutput = _isChecked ? "true" : "false";
		});
		
		switch_customdns.setOnCheckedChangeListener((_buttonView, _isChecked) -> {
			// 1. Cek apakah perubahan posisi switch dipicu oleh sistem (efek rollback)
			if (isBypassingListener) {
				return; // Keluar, jangan eksekusi kode di bawah agar error tidak terhapus
			}
			
			// Ambil isi DNS
			String d1 = (inputdns1.getEditText() != null)
			? inputdns1.getEditText().getText().toString().trim()
			: "";
			
			String d2 = (inputdns2.getEditText() != null)
			? inputdns2.getEditText().getText().toString().trim()
			: "";
			
			if (_isChecked) {
				
				if (d1.isEmpty() || d2.isEmpty()) {
					
					if (d1.isEmpty() && inputdns1.getEditText() != null) {
						inputdns1.getEditText().setError("Can't be Empty");
					}
					
					if (d2.isEmpty() && inputdns2.getEditText() != null) {
						inputdns2.getEditText().setError("Can't be Empty");
					}
					
					isBypassingListener = true;
					
					switch_customdns.post(new Runnable() {
						@Override
						public void run() {
							switch_customdns.setChecked(false);
							isBypassingListener = false;
						}
					});
					
					return;
				}
				
				// Jalankan VPN jika input valid
				MyVpnService.setDns(d1, d2);
				
				Intent intent = VpnService.prepare(MainActivity.this);
				
				if (intent != null) {
					startActivityForResult(intent, 100);
				} else {
					startService(new Intent(MainActivity.this, MyVpnService.class));
				}
				
			} else {
				// Blok ini sekarang aman. Hanya akan dieksekusi jika USER yang mematikan switch secara manual.
				if (inputdns1.getEditText() != null) {
					inputdns1.getEditText().setError(null);
				}
				
				if (inputdns2.getEditText() != null) {
					inputdns2.getEditText().setError(null);
				}
				
				Intent stop = new Intent(MainActivity.this, MyVpnService.class);
				stop.setAction("STOP");
				startService(stop);
			}
			
		});
		
		resetresobtn.setOnClickListener(_v -> {
			// 1. Bersihkan tanda error dan kosongkan teks di input saat reset ditekan
			inputwidth.getEditText().setError(null);
			inputheight.getEditText().setError(null);
			inputwidth.getEditText().setText("");
			inputheight.getEditText().setText("");
			
			boolean hasShizukuPermission = false;
			try {
				if (rikka.shizuku.Shizuku.pingBinder() && 
				rikka.shizuku.Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED) {
					hasShizukuPermission = true;
				}
			} catch (Exception e) {
				hasShizukuPermission = false;
			}
			
			// 2. Cek Izin Shizuku terlebih dahulu
			if (!hasShizukuPermission) {
				resetresobtn.post(new Runnable() {
					@Override
					public void run() {
						accessDeniedShizuku();
					}
				});
				
				try {
					if (rikka.shizuku.Shizuku.pingBinder()) {
						rikka.shizuku.Shizuku.requestPermission(1001);
					}
				} catch (Exception e) { 
					e.printStackTrace(); 
				}
				
				return; // Berhenti di sini jika tidak ada izin Shizuku
			}
			
			// 3. Jika izin Shizuku sudah diberikan, eksekusi perintah reset
			inoka.exec("wm size reset", new ShizukuHelper.Callback() {
				@Override
				public void onOutput(String line) {
					// Output proses shell
				}  
				
				@Override  
				public void onFinish(String result) {  
					// Opsional: jalankan wm density reset juga agar ukuran ikon kembali normal
					inoka.exec("wm density reset", new ShizukuHelper.Callback() {
						@Override public void onOutput(String l) {}
						@Override public void onFinish(String r) {}
					});
				}
			});
			
		});
		
		applyresobtn.setOnClickListener(_v -> {
			// 1. Reset error terlebih dahulu agar tanda merah hilang saat dicek kembali
			inputwidth.getEditText().setError(null);
			inputheight.getEditText().setError(null);
			
			boolean hasShizukuPermission = false;
			try {
				if (rikka.shizuku.Shizuku.pingBinder() && 
				rikka.shizuku.Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED) {
					hasShizukuPermission = true;
				}
			} catch (Exception e) {
				hasShizukuPermission = false;
			}
			
			// 2. Jika TIDAK ada izin Shizuku, minta izin atau tampilkan akses ditolak
			if (!hasShizukuPermission) {
				applyresobtn.post(new Runnable() {
					@Override
					public void run() {
						accessDeniedShizuku();
					}
				});
				
				try {
					if (rikka.shizuku.Shizuku.pingBinder()) {
						rikka.shizuku.Shizuku.requestPermission(1001);
					}
				} catch (Exception e) { 
					e.printStackTrace(); 
				}
				
				return; // Berhenti di sini jika tidak ada izin
			}
			
			// 3. Ambil data input dari EditText
			String width = inputwidth.getEditText().getText().toString().trim();
			String height = inputheight.getEditText().getText().toString().trim();
			
			boolean isValid = true;
			
			// 4. Validasi Input Width
			if (width.isEmpty()) {
				inputwidth.getEditText().setError("Can't be Empty");
				isValid = false;
			}
			
			// 5. Validasi Input Height
			if (height.isEmpty()) {
				inputheight.getEditText().setError("Can't be Empty");
				isValid = false;
			}
			
			inoka.exec("wm size " + width + "x" + height, new ShizukuHelper.Callback() {
				@Override
				public void onOutput(String line) {
					
				}  
				
				@Override  
				public void onFinish(String result) {  
					
				}
			});
			
		});
		
		resetdpibtn.setOnClickListener(_v -> {
			// 1. Bersihkan tanda error dan kosongkan teks di input saat reset ditekan
			inputdpi.getEditText().setError(null);
			inputdpi.getEditText().setText("");
			
			boolean hasShizukuPermission = false;
			try {
				if (rikka.shizuku.Shizuku.pingBinder() && 
				rikka.shizuku.Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED) {
					hasShizukuPermission = true;
				}
			} catch (Exception e) {
				hasShizukuPermission = false;
			}
			
			// 2. Cek Izin Shizuku terlebih dahulu
			if (!hasShizukuPermission) {
				resetdpibtn.post(new Runnable() {
					@Override
					public void run() {
						accessDeniedShizuku();
					}
				});
				
				try {
					if (rikka.shizuku.Shizuku.pingBinder()) {
						rikka.shizuku.Shizuku.requestPermission(1001);
					}
				} catch (Exception e) { 
					e.printStackTrace(); 
				}
				
				return; // Berhenti di sini jika tidak ada izin Shizuku
			}
			
			// 3. Jika izin Shizuku sudah diberikan, eksekusi perintah reset
			inoka.exec("wm density reset", new ShizukuHelper.Callback() {
				@Override
				public void onOutput(String line) {
					// Output proses shell
				}  
				
				@Override  
				public void onFinish(String result) {  
					
				}
			});
			
		});
		
		applydpibtn.setOnClickListener(_v -> {
			// 1. Reset error terlebih dahulu agar tanda merah hilang saat dicek kembali
			inputdpi.getEditText().setError(null);
			
			boolean hasShizukuPermission = false;
			try {
				if (rikka.shizuku.Shizuku.pingBinder() && 
				rikka.shizuku.Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED) {
					hasShizukuPermission = true;
				}
			} catch (Exception e) {
				hasShizukuPermission = false;
			}
			
			// 2. Jika TIDAK ada izin Shizuku, minta izin atau tampilkan akses ditolak
			if (!hasShizukuPermission) {
				applydpibtn.post(new Runnable() {
					@Override
					public void run() {
						accessDeniedShizuku();
					}
				});
				
				try {
					if (rikka.shizuku.Shizuku.pingBinder()) {
						rikka.shizuku.Shizuku.requestPermission(1001);
					}
				} catch (Exception e) { 
					e.printStackTrace(); 
				}
				
				return; // Berhenti di sini jika tidak ada izin
			}
			
			String value = inputdpi.getEditText().getText().toString().trim();
			
			boolean isValid = true;
			
			if (value.isEmpty()) {
				inputdpi.getEditText().setError("Can't be Empty");
				isValid = false;
			}
			
			inoka.exec("wm density " + value, new ShizukuHelper.Callback() {
				@Override
				public void onOutput(String line) {
					
				}  
				
				@Override  
				public void onFinish(String result) {  
					
				}
			});
			
		});
		
		switch_monitor.setOnCheckedChangeListener((_buttonView, _isChecked) -> {
			// Flag pengaman untuk mendeteksi apakah perubahan berasal dari sentuhan user atau program
			boolean currentPref = user.getBoolean("monitor", false);
			ViewGroup parentLayout = (ViewGroup) monitoring_feature.getParent();
			
			// Simpan state baru ke SharedPreferences
			user.edit().putBoolean("monitor", _isChecked).apply();
			
			if (_isChecked) {
				// PENGAMAN 1: Cek apakah view monitor secara fisik sudah menempel di WindowManager
				if (monitor != null && monitor.isAttachedToWindow()) {
					return; 
				}
				
				monitoritem_scrollbg.setVisibility(View.VISIBLE);
				
				// Expand ke ukuran spesifik (misal: lebar Match_Parent, tinggi 400px) dengan durasi 200ms
				LayoutTransitionHelper.expand(
				parentLayout, 
				itemsetting_appearances, 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				200L
				);
				
				// PENGAMAN 2: Pastikan windowManager tidak null
				if (windowManager == null) {
					windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
				}
				
				// Cek Izin Overlay untuk Android M ke atas
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
					// Reset switch ke false agar tidak membingungkan user jika izin ditolak
					switch_monitor.post(new Runnable() {
						@Override
						public void run() {
							switch_monitor.setChecked(false);
						}
					});
					user.edit().putBoolean("monitor", false).apply();
					requestOverlayPermission();
				}
				
				// Hanya inflate jika objek monitor benar-benar belum dibuat
				if (monitor == null) {
					monitor = LayoutInflater.from(MainActivity.this).inflate(R.layout.monitoring, null);
				}
				
				final WindowManager.LayoutParams ketua;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					ketua = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT
					);
				} else {
					ketua = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_PHONE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
					PixelFormat.TRANSLUCENT
					);
				}
				
				ketua.gravity = Gravity.TOP | Gravity.START;
				
				// Ambil Posisi yang tersimpan
				SharedPreferences prefs = getSharedPreferences("FloatingViewPrefs", MODE_PRIVATE);
				ketua.x = prefs.getInt("floating_x", 0);
				ketua.y = prefs.getInt("floating_y", 500);
				
				// PENGAMAN 3: Double-check token dan parent untuk menghindari Crash IllegalStateException
				if (monitor.getWindowToken() != null || monitor.getParent() != null) {
					return;
				}
				
				try {
					windowManager.addView(monitor, ketua);
				} catch (Exception e) {
					e.printStackTrace();
					return; // Gagalkan proses jika crash internal WindowManager terjadi
				}
				
				PingHelper.mulaiLoopPing(getApplicationContext(), 1000, new PingHelper.PingListener() {
					@Override
					public void onResult(String ms) {
						// Ditambahkan pengaman null sebelum update teks ping
						if (monitor != null) {
							((TextView) monitor.findViewById(R.id.signal_value)).setText(ms);
						}
					}
				});
				
				final LinearLayout signalitem = monitor.findViewById(R.id.signalitem);
				final LinearLayout bateraiitem = monitor.findViewById(R.id.bateraiitem);
				final LinearLayout suhuitem = monitor.findViewById(R.id.suhuitem);
				final LinearLayout fpsitem = monitor.findViewById(R.id.fpsitem);
				final LinearLayout waktuitem = monitor.findViewById(R.id.waktuitem);
				
				if (item1.equals("0")) signalitem.setVisibility(View.GONE); else signalitem.setVisibility(View.VISIBLE);
				if (item2.equals("0")) bateraiitem.setVisibility(View.GONE); else bateraiitem.setVisibility(View.VISIBLE);
				if (item3.equals("0")) suhuitem.setVisibility(View.GONE); else suhuitem.setVisibility(View.VISIBLE);
				if (item4.equals("0")) fpsitem.setVisibility(View.GONE); else fpsitem.setVisibility(View.VISIBLE);
				if (item5.equals("0")) waktuitem.setVisibility(View.GONE); else waktuitem.setVisibility(View.VISIBLE);
				
				// 🛠️ FIX 1 & 3: Inisialisasi View sekali saja di luar Runnable agar Hemat CPU & Ram
				final TextView tvBattery = monitor.findViewById(R.id.battery_value);
				final TextView tvThermostat = monitor.findViewById(R.id.thermostat_value);
				final TextView tvFps = monitor.findViewById(R.id.fps_value);
				final TextView tvTime = monitor.findViewById(R.id.time_value);
				final ImageView imgSignal = monitor.findViewById(R.id.signal_icon);
				final java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault());
				
				// 🛠️ FIX 2: Gunakan Looper.getMainLooper() agar tidak crash di Thread latar belakang
				h = new Handler(android.os.Looper.getMainLooper());
				
				r = new Runnable() {
					@Override
					public void run() {
						// 🛠️ FIX PENGAMAN NULL: Jika widget dimatikan, stop eksekusi di dalam loop ini
						if (monitor == null) return;
						
						try {
							tvBattery.setText(RogMonitor.getBatteryLevel(getApplicationContext()) + "%");
							tvThermostat.setText(RogMonitor.getBatteryTemp(getApplicationContext()) + "°C");
							tvFps.setText(String.format("%.0f FPS", RogMonitor.getFps()));
							tvTime.setText(sdf.format(new java.util.Date()));
							
							Context ctx = MainActivity.this;
							if (PingHelper.isConnectedWifi(ctx)) {
								imgSignal.setImageResource(R.drawable.icon_wifi);
							} else if (PingHelper.isConnectedMobileData(ctx)) {
								imgSignal.setImageResource(R.drawable.icon_signal);
							} else {
								imgSignal.setImageResource(R.drawable.icon_blocked); 
							}
							
							if (h != null) {
								h.postDelayed(this, 500);
							}
						} catch (Exception ignored) {}
					}
				};
				
				RogMonitor.startFps();
				h.post(r);
				
				monitor.findViewById(R.id.holder_icon).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						if (monitor.findViewById(R.id.menu).getVisibility() == View.VISIBLE) monitor.findViewById(R.id.menu).setVisibility(View.GONE); else monitor.findViewById(R.id.menu).setVisibility(View.VISIBLE);
					}
				});
				
				// Integrasi Chaining View (Tanpa deklarasi variabel satu per satu)
				monitor.findViewById(R.id.holder_icon).setOnTouchListener(new View.OnTouchListener() {
					private int initialX, initialY;
					private float initialTouchX, initialTouchY;
					private long downTime;
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
							case MotionEvent.ACTION_DOWN:
							initialX = ketua.x;
							initialY = ketua.y;
							initialTouchX = event.getRawX();
							initialTouchY = event.getRawY();
							downTime = System.currentTimeMillis();
							return true;
							
							case MotionEvent.ACTION_MOVE:
							ketua.x = initialX + (int) (event.getRawX() - initialTouchX);
							ketua.y = initialY + (int) (event.getRawY() - initialTouchY);
							try {
								windowManager.updateViewLayout(monitor, ketua);
							} catch (Exception ignored) {}
							return true;
							
							case MotionEvent.ACTION_UP:
							float diffX = Math.abs(event.getRawX() - initialTouchX);
							float diffY = Math.abs(event.getRawY() - initialTouchY);
							long clickDuration = System.currentTimeMillis() - downTime;
							
							if (diffX < 10 && diffY < 10 && clickDuration < 200) {
								v.performClick();
							} else {
								SharedPreferences.Editor editor = getSharedPreferences("FloatingViewPrefs", MODE_PRIVATE).edit();
								editor.putInt("floating_x", ketua.x);
								editor.putInt("floating_y", ketua.y);
								editor.apply();
							}
							return true;
						}
						return false;
					}
				});
				
				if (themeHelper == null) {
					themeHelper = new ru.ino.senseix.DynamicThemeHelper();
				}
				
				// Langsung set background ke ID masing-masing tanpa simpan variabel lokal
				monitor.findViewById(R.id.holder_icon).setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)26, (int)1, themeHelper.getColor("colorSecondaryContainer"), themeHelper.getColor("colorPrimaryBtn")));
				monitor.findViewById(R.id.menu).setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)26, 0x60161616));
			} else {
				monitoritem_scrollbg.setVisibility(View.GONE);
				
				// Collapse otomatis kembali ke murni WRAP_CONTENT bawaan XML awal dengan durasi 200ms
				LayoutTransitionHelper.collapse(
				parentLayout, 
				itemsetting_appearances, 
				200L
				);
				
				// 1. Matikan looping ping & handler global agar tidak mengakses view kosong
				PingHelper.stopLoopPing(); 
				if (h != null && r != null) {
					h.removeCallbacks(r);
				}
				h = null; // Reset handler
				
				try {
					RogMonitor.stopFps(); // Hentikan tracker FPS jika ada metodenya
				} catch (Exception ignored) {}
				
				// 2. PENGAMAN REMOVE: Pastikan hanya di-remove jika view benar-benar nempel di layar
				if (windowManager != null && monitor != null && monitor.isAttachedToWindow()) {
					try {
						windowManager.removeViewImmediate(monitor);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				monitor = null; // Set null agar GC membersihkan sisa memori view lama
			}
		});
	}
	
	private void initializeLogic() {
		type = "2";
		action = "terminal";
		inoka = new ShizukuHelper(MainActivity.this, 2167);
		LanguageLoader.init(this);
		LanguageMenu.load(this, user);
itemsetting_language_value.setText(LanguageMenu.nameOf(LanguageMenu.saved(user)));
		
		item1 = "1";
		item2 = "1";
		item3 = "1";
		item4 = "1";
		item5 = "1";
		
		themeHelper = new ru.ino.senseix.DynamicThemeHelper();
		
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
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
			LinearLayout view_background = findViewById(R.id.background);
			view_background.setOnApplyWindowInsetsListener((background, insets) -> {
				background.setPadding(0, 0, 0, 0);
				mainscreen.setPadding(0, insets.getSystemWindowInsetTop(), 0, insets.getSystemWindowInsetBottom());
				shellscreen.setPadding(0, insets.getSystemWindowInsetTop(), 0, insets.getSystemWindowInsetBottom());
				settingscreen.setPadding(0, insets.getSystemWindowInsetTop(), 0, insets.getSystemWindowInsetBottom());
				return insets.consumeSystemWindowInsets();
			});
		}
		
		setFont(getWindow().getDecorView());
		
		homemenubg.setPadding(
		homemenubg.getPaddingLeft(),
		homemenubg.getPaddingTop(),
		homemenubg.getPaddingRight(),
		(int)(170 * getResources().getDisplayMetrics().density)
		);
		
		homemenubg.setClipToPadding(false);
		
		list.setPadding(
		list.getPaddingLeft(),
		list.getPaddingTop(),
		list.getPaddingRight(),
		(int)(75 * getResources().getDisplayMetrics().density)
		);
		
		list.setClipToPadding(false);
		
		listmodule.setPadding(
		listmodule.getPaddingLeft(),
		listmodule.getPaddingTop(),
		listmodule.getPaddingRight(),
		(int)(75 * getResources().getDisplayMetrics().density)
		);
		
		listmodule.setClipToPadding(false);
		
		String[] abis =
		Build.SUPPORTED_ABIS;
		
		StringBuilder cpu =
		new StringBuilder();
		
		for (int i = 0; i < abis.length; i++) {
			
			cpu.append(abis[i]);
			
			if (i != abis.length - 1) {
				cpu.append("\n");
			}
		}
		
		boolean is64 =
		Build.SUPPORTED_64_BIT_ABIS.length > 0;
		
		arch_subtitle.setText(cpu.toString());
		
		shell_input.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(android.widget.TextView v, int actionId, android.view.KeyEvent event) {
				if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND || 
				actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE || 
				(event != null && event.getKeyCode() == android.view.KeyEvent.KEYCODE_ENTER)) {
					exec_button.performClick();
					return true;
				}
				return false;
			}
		});
		
		inputdns1.setLabelText(LanguageLoader.get("inputdns1.label"));
		inputdns1.setIsMultiline(false);
		inputdns1.setCornerRadius(12f);
		inputdns1.setTextSize(14f);
		inputdns1.setLabelSize(14f);
		
		inputdns2.setLabelText(LanguageLoader.get("inputdns2.label"));
		inputdns2.setIsMultiline(false);
		inputdns2.setCornerRadius(12f);
		inputdns2.setTextSize(14f);
		inputdns2.setLabelSize(14f);
		
		inputwidth.setLabelText(LanguageLoader.get("inputwidth.label"));
		inputwidth.setIsMultiline(false);
		inputwidth.setCornerRadius(12f);
		inputwidth.setTextSize(14f);
		inputwidth.setLabelSize(14f);
		
		inputheight.setLabelText(LanguageLoader.get("inputheight.label"));
		inputheight.setIsMultiline(false);
		inputheight.setCornerRadius(12f);
		inputheight.setTextSize(14f);
		inputheight.setLabelSize(14f);
		
		inputdpi.setLabelText(LanguageLoader.get("inputdpi.label"));
		inputdpi.setIsMultiline(false);
		inputdpi.setCornerRadius(12f);
		inputdpi.setTextSize(14f);
		inputdpi.setLabelSize(14f);
		
		sengame_openbtn.setThumbPaddingDp(5f);
		sengame_openbtn.setIconPosition(InoSliderBtn.Alignment.RIGHT);
		sengame_openbtn.setShapes(InoSliderBtn.ShapeType.ROUNDED, InoSliderBtn.ShapeType.ROUNDED); 
		sengame_openbtn.setSliderText(LanguageLoader.get("sengame_slider"));
		sengame_openbtn.setThumbWidthDp(121); 
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			sengame_openbtn.setThumbIcon(this.getDrawable(R.drawable.icon_play));
		} else {
			sengame_openbtn.setThumbIcon(getResources().getDrawable(R.drawable.icon_play));
		}
		
		sengame_openbtn.setOnSliderUnlockedListener(new InoSliderBtn.OnSliderUnlockedListener() {
			@Override
			public void onUnlocked() {
				sengame_openbtn.resetSlider();
				
				// Konversi HashSet ke ArrayList<String> agar bisa dikirim lewat Intent
				ArrayList<String> packagesToSend = new ArrayList<>(activePackages);
				
				Intent intent = new Intent(MainActivity.this, SengameActivity.class);
				intent.putStringArrayListExtra("active_packages", packagesToSend); // Kirim data!
				
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		
		helper = new AppListHelper(MainActivity.this);
		executorService = Executors.newSingleThreadExecutor();
		listAdapter = new ListAdapter(apps);
		list.setAdapter(listAdapter);
		
		//===========================sizebg=sizepb=dragrate
		swiperefreshlayout.setCustomSizeDp(55, 26, 55);
		swiperefreshlayout.setOnRefreshListener(() -> {
			switch (type) {
				case "0": // All
				loadApplications(AppListHelper.MODE_ALL);
				break;
				
				case "1": // System
				loadApplications(AppListHelper.MODE_SYSTEM);
				break;
				
				case "2": // Game
				loadApplications(AppListHelper.MODE_GAMES);
				break;
				
				case "3": // User
				loadApplications(AppListHelper.MODE_USER);
				break;
			}
			
			new Thread(() -> {
				runOnUiThread(() -> swiperefreshlayout.setRefreshing(false));
			}).start();
		});
		
		device_subtitle.setText(Build.VERSION.RELEASE + "(SDK " + Build.VERSION.SDK_INT + ")");
		board_subtitle.setText(Build.HARDWARE + " | " + Runtime.getRuntime().availableProcessors() + " CPU cores");
		shellscreen.setVisibility(View.GONE);
		settingscreen.setVisibility(View.GONE);
		settingbtn.setVisibility(View.GONE);
		// ==========================================
		// 1. SIGNAL ITEM (POSISI PERTAMA)
		// ==========================================
		item_signalbg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final LinearLayout signalitem = monitor.findViewById(R.id.signalitem);
				
				if (signalitem.getVisibility() == View.VISIBLE) {
					signalitem.setVisibility(View.GONE);
					item_signalbg.setBackground(buatBackground(25, 1, strokeOffColor, bgOffColor));
					item1 = "0";
				} else {
					signalitem.setVisibility(View.VISIBLE);
					item_signalbg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
					item1 = "1";
				}
			}
		});
		
		// ==========================================
		// 2. BATERAI ITEM
		// ==========================================
		item_bateraibg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final LinearLayout bateraiitem = monitor.findViewById(R.id.bateraiitem);
				
				if (bateraiitem.getVisibility() == View.VISIBLE) {
					bateraiitem.setVisibility(View.GONE);
					item_bateraibg.setBackground(buatBackground(25, 1, strokeOffColor, bgOffColor));
					item2 = "0";
				} else {
					bateraiitem.setVisibility(View.VISIBLE);
					item_bateraibg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
					item2 = "1";
				}
			}
		});
		
		// ==========================================
		// 3. SUHU ITEM
		// ==========================================
		item_suhubg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final LinearLayout suhuitem = monitor.findViewById(R.id.suhuitem);
				
				if (suhuitem.getVisibility() == View.VISIBLE) {
					suhuitem.setVisibility(View.GONE);
					item_suhubg.setBackground(buatBackground(25, 1, strokeOffColor, bgOffColor));
					item3 = "0";
				} else {
					suhuitem.setVisibility(View.VISIBLE);
					item_suhubg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
					item3 = "1";
				}
			}
		});
		
		// ==========================================
		// 4. FPS ITEM
		// ==========================================
		item_fpsbg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final LinearLayout fpsitem = monitor.findViewById(R.id.fpsitem);
				
				if (fpsitem.getVisibility() == View.VISIBLE) {
					fpsitem.setVisibility(View.GONE);
					item_fpsbg.setBackground(buatBackground(25, 1, strokeOffColor, bgOffColor));
					item4 = "0";
				} else {
					fpsitem.setVisibility(View.VISIBLE);
					item_fpsbg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
					item4 = "1";
				}
			}
		});
		
		// ==========================================
		// 5. WAKTU ITEM
		// ==========================================
		item_waktubg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final LinearLayout waktuitem = monitor.findViewById(R.id.waktuitem);
				
				if (waktuitem.getVisibility() == View.VISIBLE) {
					waktuitem.setVisibility(View.GONE);
					item_waktubg.setBackground(buatBackground(25, 1, strokeOffColor, bgOffColor));
					item5 = "0";
				} else {
					waktuitem.setVisibility(View.VISIBLE);
					item_waktubg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
					item5 = "1";
				}
			}
		});
		bottomnavigation
		.tab(LanguageLoader.get("page_home_title"),getDrawable(R.drawable.icon_home))
		.tab(LanguageLoader.get("page_apps_title"),getDrawable(R.drawable.icon_videogame))
		.tab(LanguageLoader.get("page_module_title"),getDrawable(R.drawable.icon_extension))
		;
		bottomnavigation.setupWithViewPager(viewpager);
		viewpager.addOnPageChangeListener(
		new InoViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset) {
				
			}
			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					currentview_title.setText(LanguageLoader.get("page_home_title"));
					currentview_subtitle.setText(LanguageLoader.get("page_home_subtitle"));
					infobtn.setVisibility(View.VISIBLE);
					settingbtn.setVisibility(View.GONE);
					actionbtn.setVisibility(View.VISIBLE);
					actionbtn.setImageResource(R.drawable.icon_terminal);
					actionbtn.animate()
					.translationY(0)
					.alpha(1.0f)
					.setDuration(221)
					.start();
					action = "terminal";
				}
				if (position == 1) {
					currentview_title.setText(LanguageLoader.get("page_apps_title"));
					currentview_subtitle.setText(LanguageLoader.get("page_apps_subtitle"));
					infobtn.setVisibility(View.GONE);
					settingbtn.setVisibility(View.GONE);
					sengame_openbtn.setVisibility(View.VISIBLE);
					actionbtn.animate()
					.translationY(actionbtn.getHeight())
					.alpha(0.0f)
					.setDuration(221)
					.withEndAction(new Runnable() {
						@Override
						public void run() {
							// Kode di sini baru jalan SETELAH animasi selesai
							actionbtn.setVisibility(View.GONE);
						}
					})
					.start();
					
					View currentView = getCurrentFocus();
					if (currentView != null && currentView.getContext() instanceof Activity) {
						((Activity) currentView.getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
					}
				}
				if (position == 2) {
					currentview_title.setText(LanguageLoader.get("page_module_title"));
					currentview_subtitle.setText(LanguageLoader.get("page_module_subtitle"));
					infobtn.setVisibility(View.GONE);
					settingbtn.setVisibility(View.VISIBLE);
					actionbtn.setVisibility(View.VISIBLE);
					actionbtn.setImageResource(R.drawable.icon_upload);
					actionbtn.animate()
					.translationY(0)
					.alpha(1.0f)
					.setDuration(221)
					.start();
					action = "module";
				}
			}
		});
		inputsearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				if (inputsearch.getText().toString().length() > 0) {
					clearbutton.setVisibility(View.VISIBLE);
					morebtn.setVisibility(View.GONE);
				} else {
					morebtn.setVisibility(View.VISIBLE);
					clearbutton.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				// tidak perlu apa-apa
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				// Pastikan adapter tidak null saat diketik
				if (listAdapter == null) return; 
				
				String query = _param1.toString().toLowerCase(Locale.getDefault()).trim();
				apps.clear(); // Bersihkan list operasional saat ini
				
				if (query.isEmpty()) {
					// Jika kolom pencarian kosong, kembalikan semua data dari list asli
					apps.addAll(originalAppsList);
				} else {
					// Lakukan filter berdasarkan nama aplikasi atau nama package
					for (HashMap<String, Object> item : originalAppsList) {
						if (item.get("name") != null && item.get("package") != null) {
							String appName = item.get("name").toString().toLowerCase(Locale.getDefault());
							String appPackage = item.get("package").toString().toLowerCase(Locale.getDefault());
							
							// Filter berdasarkan Nama Aplikasi ATAU Nama Package
							if (appName.contains(query) || appPackage.contains(query)) {
								apps.add(item);
							}
						}
					}
				}
				
				// Beritahu adapter bahwa data telah berubah agar ListView memperbarui tampilan
				listAdapter.notifyDataSetChanged();
			}
		});
		
		itemsetting_terminaltextsize_slider.setMax((int)100);
		itemsetting_terminaltextsize_slider.setProgress(12);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		int savedColor = user.getInt(
		"color",
		Color.parseColor("#1058C8")
		);
		
		Color.colorToHSV(savedColor, currentHsv);
		
		terapkanWarnaTemaDinamis(savedColor);
		loadApplications(AppListHelper.MODE_GAMES); 
		
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
		
		lastColorStart = 0;
		lastColorEnd = 0;
		lastState = -1;
		
		// Jalankan perulangan real-time
		statusHandler.post(statusRunnable);
		
		android.os.PowerManager pm = (android.os.PowerManager) getSystemService(android.content.Context.POWER_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && pm != null) {
			
			// Cek real-time: true jika SUDAH "Tidak Dibatasi" (Ignoring Battery Optimizations)
			boolean isIgnoringOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());
			
			if (!isIgnoringOptimizations) {
				String pesanBaterai = "To ensure the PING and FPS indicators run smoothly while gaming or in the background, please set SenseiX battery usage to 'Unrestricted'.\n\n" +
				"Steps after tapping the button below:\n" +
				"1. Select the 'Battery' menu.\n" +
				"2. Change from 'Optimized' to 'Unrestricted'.";
				
				tampilkanDialogKustom(
				"Background Permission",
				"Battery Optimization Detected",
				pesanBaterai,
				"Cancel",
				"Open Settings",
				() -> {}, // Aksi jika batal
				() -> {
					try {
						android.content.Intent intent = new android.content.Intent();
						intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						android.net.Uri uri = android.net.Uri.fromParts("package", getPackageName(), null);
						intent.setData(uri);
						startActivity(intent);
					} catch (Exception e) {
						try {
							startActivity(new android.content.Intent(android.provider.Settings.ACTION_SETTINGS));
						} catch (Exception ignored) {}
					}
				}
				);
			}
		}
		// 1. Ambil status data terlebih dahulu
		boolean isMonitorActive = user.getBoolean("monitor", false);
		
		// 2. Cek Izin Overlay untuk Monitor (Hanya jika datanya TRUE dan izin belum diberikan)
		if (isMonitorActive) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
				// Matikan switch sementara di UI agar tidak menipu user sebelum izin diberikan
				switch_monitor.setChecked(false); 
				
				Intent overlay = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
				startActivity(overlay);
				return; // Hentikan eksekusi ke bawah agar user mengurus izin dulu
			}
		}
		// Set nilainya langsung (bisa true atau false)
		switch_monitor.setChecked(isMonitorActive);
		
		// 4. Load Switch Biasa (Langsung set tanpa perlu kondisi IF)
		switch_moduleinstall.setChecked(user.getBoolean("onmodule", false));
		switch_onfinish.setChecked(user.getBoolean("onfinish", false));
		switch_ansi.setChecked(user.getBoolean("ansi", false));
		
		loadSenseiXModules(); 
	}
	
	@Override
	public void onPause() {
		super.onPause();
		statusHandler.removeCallbacks(statusRunnable);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (windowManager != null && monitor != null && monitor.isAttachedToWindow()) {
			try {
				windowManager.removeViewImmediate(monitor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		monitor = null;
		
		PingHelper.stopLoopPing(); 
	}
	
	@Override
	public void onBackPressed() {
		if (mainscreen.getVisibility() == View.VISIBLE) {
			
			// Jika sedang di halaman ViewPager selain Home
			if (viewpager.getCurrentItem() > 0) {
				viewpager.setCurrentItem(viewpager.getCurrentItem() - 1, true);
				return;
			}
			
			// Jika sudah di Home
			exitDialog();
			return;
		}
		
		if (shellscreen.getVisibility() == View.VISIBLE) {
			mainscreen.setVisibility(View.VISIBLE);
			shellscreen.setVisibility(View.GONE);
			return;
		}
		
		if (settingscreen.getVisibility() == View.VISIBLE) {
			if (fromShell.equals("true")) {
				shellscreen.setVisibility(View.VISIBLE);
				settingscreen.setVisibility(View.GONE);
			}
			if (fromShell.equals("false")) {
				mainscreen.setVisibility(View.VISIBLE);
				settingscreen.setVisibility(View.GONE);
			}
			return;
		}
		
		exitDialog();
	}
	

	public void _more() {
	}
	
	private WindowManager windowManager;
	private View monitor;
	private ArrayList<HashMap<String, Object>> originalAppsList = new ArrayList<>();
	private HashSet<String> activePackages = new HashSet<>();
	private ListAdapter listAdapter;
	private Handler h;
	private Runnable r;
	private String item1,item2,item3,item4,item5;
	
	private boolean isNavVisible = true;
	private boolean isBypassingListener = false;
	private boolean isBypassingListener2 = false;
	
	private void setFont(android.view.View view) {
		android.graphics.Typeface tf = android.graphics.Typeface.createFromAsset(
		getAssets(), "fonts/main.ttf");
		
		if (view instanceof android.widget.TextView) {
			android.widget.TextView tv = (android.widget.TextView) view;
			
			// 1. Ambil style yang sudah ada saat ini
			int existingStyle = android.graphics.Typeface.NORMAL;
			if (tv.getTypeface() != null) {
				existingStyle = tv.getTypeface().getStyle();
			}
			
			// 2. Cek apakah ini shell_output (bisa cek lewat ID atau kondisi lain)
			// Ganti R.id.shell_output sesuai dengan ID yang Anda gunakan di XML
			if (tv.getId() == R.id.shell_output) {
				// Menggunakan Monospace bawaan sistem Android dengan mempertahankan style
				tv.setTypeface(android.graphics.Typeface.MONOSPACE, existingStyle);
			} else {
				// Terapkan font custom (main.ttf) untuk TextView lainnya
				tv.setTypeface(tf, existingStyle);
			}
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
			
			final float radiusPx =
			radiusDp * view.getResources().getDisplayMetrics().density;
			
			view.setClipToOutline(true);
			view.setElevation(8f);
			
			view.setOutlineProvider(new android.view.ViewOutlineProvider() {
				@Override
				public void getOutline(View v,
				android.graphics.Outline outline) {
					
					outline.setRoundRect(
					0,
					0,
					v.getWidth(),
					v.getHeight(),
					radiusPx
					);
				}
			});
		}
	}
	
	public void setColorRadius(View view, int radius, int color) {
		view.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns(radius, color));
	}
	
	private String formatSize(long size) {
		
		float kb = size / 1024f;
		float mb = kb / 1024f;
		float gb = mb / 1024f;
		
		if (gb >= 1f) {
			return String.format(Locale.US, "%.1f GB", gb);
		}
		
		if (mb >= 1f) {
			return String.format(Locale.US, "%.1f MB", mb);
		}
		
		if (kb >= 1f) {
			return String.format(Locale.US, "%.1f KB", kb);
		}
		
		return size + " B";
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
		Dialog dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		
		float dp = getResources().getDisplayMetrics().density;
		android.graphics.Typeface inoFont = android.graphics.Typeface.createFromAsset(getAssets(), "fonts/main.ttf");
		
		// ROOT
		LinearLayout root = new LinearLayout(MainActivity.this);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setPadding((int)(24 * dp), (int)(24 * dp), (int)(24 * dp), (int)(18 * dp));
		root.setBackground(new GradientDrawable() {{
				setColor(themeHelper.getColor("colorSurfaceContainer"));
				setCornerRadius(28 * dp);
			}});
		
		// TITLE
		TextView title = new TextView(MainActivity.this);
		title.setText(strTitle);
		title.setTextSize(24);
		title.setTypeface(android.graphics.Typeface.create(inoFont, android.graphics.Typeface.BOLD));
		title.setTextColor(themeHelper.getColor("colorOnSurface"));
		title.setPadding(0, 0, 0, (int)(8 * dp));
		root.addView(title);
		
		// SUBTITLE
		TextView subtitle = new TextView(MainActivity.this);
		subtitle.setText(strSubtitle);
		subtitle.setTextSize(13);
		subtitle.setTypeface(inoFont);
		subtitle.setTextColor(themeHelper.getColor("colorPrimary"));
		subtitle.setPadding(0, 0, 0, (int)(18 * dp));
		root.addView(subtitle);
		
		// INFO CARD
		LinearLayout infoCard = new LinearLayout(MainActivity.this);
		infoCard.setOrientation(LinearLayout.VERTICAL);
		infoCard.setPadding((int)(16 * dp), (int)(16 * dp), (int)(16 * dp), (int)(16 * dp));
		infoCard.setBackground(new GradientDrawable() {{
				setCornerRadius(20 * dp);
				setColor(themeHelper.getColor("colorSurfaceContainerHigh"));
			}});
		
		// MESSAGE
		TextView msg = new TextView(MainActivity.this);
		msg.setText(strMessage);
		msg.setTextSize(14);
		msg.setTypeface(inoFont);
		msg.setTextColor(themeHelper.getColor("colorOnSurfaceVariant"));
		infoCard.addView(msg);
		root.addView(infoCard);
		
		// SPACE
		View spacer = new View(MainActivity.this);
		spacer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(18 * dp)));
		root.addView(spacer);
		
		// DIVIDER
		View divider = new View(MainActivity.this);
		divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(1 * dp)));
		divider.setBackgroundColor(themeHelper.getColor("colorOutline"));
		root.addView(divider);
		
		// BUTTON ROW
		LinearLayout row = new LinearLayout(MainActivity.this);
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setGravity(Gravity.END);
		row.setPadding(0, (int)(14 * dp), 0, 0);
		
		// TOMBOL BATAL / TUTUP
		TextView btnCancel = new TextView(MainActivity.this);
		btnCancel.setText(textCancelBtn);
		btnCancel.setTextSize(14);
		btnCancel.setTypeface(android.graphics.Typeface.create(inoFont, android.graphics.Typeface.BOLD));
		btnCancel.setTextColor(themeHelper.getColor("colorPrimary"));
		btnCancel.setPadding((int)(16 * dp), (int)(12 * dp), (int)(16 * dp), (int)(12 * dp));
		btnCancel.setOnClickListener(v -> dialog.dismiss());
		
		btnCancel.setOnClickListener(v -> {
			if (onNoAction != null) {
				onNoAction.run();
			}
			dialog.dismiss();
		});
		row.addView(btnCancel);
		
		// TOMBOL OK / MENGERTI
		TextView btnOk = new TextView(MainActivity.this);
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
	
	public void requestOverlayPermission() {
		tampilkanDialogKustom(
		LanguageLoader.get("overlay_title"),
		LanguageLoader.get("overlay_subtitle"),
		LanguageLoader.get("overlay_message"),
		LanguageLoader.get("btn_cancel"),
		LanguageLoader.get("btn_grant_permission"),
		() -> {
			// Action when Cancel is pressed (Optional: Show a warning toast)
		},
		() -> {
			// Action when Grant Permission is pressed: Open Overlay Settings
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
				Intent intent = new Intent(
				android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
				android.net.Uri.parse("package:" + getPackageName())
				);
				startActivity(intent);
			} else {
				// DONE
			}
		}
		);
		
		return;
	}
	
	public void exitDialog() {
		tampilkanDialogKustom(
		LanguageLoader.get("exit_title"),
		LanguageLoader.get("exit_subtitle"),
		LanguageLoader.get("exit_message"),
		LanguageLoader.get("btn_cancel"),
		LanguageLoader.get("btn_exit"),
		() -> {},
		() -> {
			// Aksi penutupan activity yang dipicu saat klik "Keluar"
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				finishAndRemoveTask();
			} else {
				finish();
			}
			if (windowManager != null && monitor != null && monitor.isAttachedToWindow()) {
				try {
					windowManager.removeViewImmediate(monitor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			monitor = null;
			
			PingHelper.stopLoopPing(); 
		}
		);
	}
	
	public void accessDeniedShizuku() {
		tampilkanDialogKustom(
		LanguageLoader.get("shizuku_denied_title"),
		LanguageLoader.get("shizuku_denied_subtitle"),
		LanguageLoader.get("shizuku_denied_message"),
		LanguageLoader.get("btn_close"),
		LanguageLoader.get("btn_got_it"),
		() -> {},
		() -> {}
		);
	}
	
	private GradientDrawable buatBackground(int radius, int strokeWidth, int strokeColor, int backgroundColor) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setCornerRadius(radius);
		drawable.setStroke(strokeWidth, strokeColor);
		drawable.setColor(backgroundColor);
		return drawable;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			View currentFocusView = getCurrentFocus();
			
			// 1. Periksa apakah yang sedang fokus saat ini adalah sebuah EditText
			if (currentFocusView instanceof android.widget.EditText) {
				Rect outRect = new Rect();
				currentFocusView.getGlobalVisibleRect(outRect);
				
				// 2. Jika titik koordinat jari ditekan DI LUAR batas area kotak inputan
				if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
					
					// Hapus error secara aman jika EditText dibungkus di dalam InoInputLayout
					if (currentFocusView.getParent() instanceof ru.ino.senseix.InoInputLayout) {
						ru.ino.senseix.InoInputLayout currentFocusInput = (ru.ino.senseix.InoInputLayout) currentFocusView.getParent();
						if (currentFocusInput.getEditText() != null) {
							currentFocusInput.getEditText().setError(null);
						}
					} else if (currentFocusView.getParent() != null && currentFocusView.getParent().getParent() instanceof ru.ino.senseix.InoInputLayout) {
						// Antisipasi jika EditText dibungkus layout perantara lagi di dalam InoInputLayout
						ru.ino.senseix.InoInputLayout currentFocusInput = (ru.ino.senseix.InoInputLayout) currentFocusView.getParent().getParent();
						if (currentFocusInput.getEditText() != null) {
							currentFocusInput.getEditText().setError(null);
						}
					} else {
						// Jika EditText biasa tanpa InoInputLayout
						((android.widget.EditText) currentFocusView).setError(null);
					}
					
					// Lepaskan fokus dari EditText secara paksa
					currentFocusView.clearFocus();
					
					// Tutup keyboard soft-input virtual secara otomatis demi kenyamanan user
					android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm != null) {
						imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
					}
				}
			}
		}
		return super.dispatchTouchEvent(event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
			boolean isShellOn = switch_moduleinstall.isChecked(); 
			Uri fileUri = data.getData();
			if (fileUri != null) {
				installModuleFromUri(fileUri, isShellOn);
			}
		}
		
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {
				startService(new Intent(this, MyVpnService.class));
				switch_customdns.setChecked(true);
			} else {
				switch_customdns.setChecked(false);
			}
		}
	}
	
	private void installModuleFromUri(Uri uri, boolean showShellScreen) {
		final String initialLog = "[*] Processing module package...\n";
		
		if (showShellScreen) {
			mainscreen.setVisibility(View.GONE);
			shellscreen.setVisibility(View.VISIBLE);
			shell_output.setText(initialLog);
		} else {
			// Kosong
		}
		
		File rootSd = android.os.Environment.getExternalStorageDirectory();
		File outputDir = new File(rootSd, ".SenseiX/module");
		
		if (!outputDir.exists() && !outputDir.mkdirs()) {
			if (!outputDir.exists()) {
				if (showShellScreen) {
					shell_output.append("[-] Failed to create output directory: " + outputDir.getAbsolutePath() + "\n");
				} else {
					Toast.makeText(getApplicationContext(), "Gagal membuat folder tujuan.", Toast.LENGTH_SHORT).show();
				}
				return;
			}
		}
		
		ArchiveExtractor.extractFromUri(this, uri, outputDir, new ArchiveExtractor.ExtractCallback() {
			@Override
			public void onProgress(String currentFile, int progressPercent, long extractedBytes) {
				if (showShellScreen) {
					// Buat ASCII Progress Bar: [ =====----------------- ]
					String progressBar = makeProgressBar(progressPercent, 20);
					double sizeInMb = extractedBytes / (1024.0 * 1024.0);
					
					final String dynamicStatus = String.format(
					java.util.Locale.US,
					"[ ! ] Extracting: %s\n\nProgress: %d%%\n%s  (%.2f MB)",
					currentFile,
					progressPercent,
					progressBar,
					sizeInMb
					);
					
					mainHandler.post(() -> {
						// KUNCI PAKAI setText BIAR GAK SPAM NEW LINE
						shell_output.setText(initialLog + dynamicStatus);
					});
				}
			}
			
			@Override
			public void onSuccess(File destinationDir) {
				mainHandler.post(() -> {
					if (showShellScreen) {
						String finalProgressBar = makeProgressBar(100, 20);
						shell_output.setText(initialLog + 
						"[ ! ] Extraction Completed!\n\nProgress: 100%\n" + finalProgressBar + 
						"\n\n[+] Module successfully installed to /.SenseiX/module/\n");
					} else {
						// Kosong
					}
					
					loadSenseiXModules();
				});
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				final String errorMsg = (e.getLocalizedMessage() != null) ? e.getLocalizedMessage() : e.toString();
				
				mainHandler.post(() -> {
					if (showShellScreen) {
						shell_output.append("\n\n[-] Extraction failed: " + errorMsg + "\n");
					} else {
						Toast.makeText(getApplicationContext(), "Gagal Ekstrak: " + errorMsg, Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}
	
	// Helper untuk membuat Progress Bar ASCII [ ===------------------ ]
	private String makeProgressBar(int percent, int totalBlocks) {
		int filledBlocks = (int) ((percent / 100.0) * totalBlocks);
		StringBuilder sb = new StringBuilder("[ ");
		for (int i = 0; i < totalBlocks; i++) {
			if (i < filledBlocks) {
				sb.append("=");
			} else {
				sb.append("-");
			}
		}
		sb.append(" ]");
		return sb.toString();
	}
	
	//=========================================
	// CODE GROUP: COLORCHANGER (MainActivity)
	//=========================================
	
	private float[] currentHsv = new float[]{217f, 0.92f, 0.63f};
	private ru.ino.senseix.DynamicThemeHelper themeHelper;
	private int btnColor, primaryBtn, strokeOffColor, bgOffColor;
	
	// Warna default tema. Dipakai saat belum ada warna tersimpan DAN saat Reset.
	private static final int DEFAULT_THEME_COLOR = 0xFF1058C8;
	
	// --- DEKLARASI DI PINDAH KE ATAS SINI AGAR TIDAK ERROR (UNDEFINED) ---
	private android.text.TextWatcher hexTextWatcher;
	// ---------------------------------------------------------------------
	
	// Hapus SEMUA preferensi warna (color + color_h/s/v), lalu kembalikan currentHsv ke default.
	// Bug sebelumnya: hanya "color" yang dihapus, sehingga color_h/s/v masih tersimpan
	// dan picker tetap terbuka di warna terakhir yang dipilih.
	private void resetPreferensiWarna() {
		user.edit()
		.remove("color")
		.remove("color_h")
		.remove("color_s")
		.remove("color_v")
		.commit();
		
		Color.colorToHSV(DEFAULT_THEME_COLOR, currentHsv);
	}
	
	private void terapkanWarnaTemaDinamis(int warnaDariPicker) {
		if (themeHelper == null) {
			themeHelper = new ru.ino.senseix.DynamicThemeHelper();
		}
		
		// 1. Hitung ulang seluruh token warna Material 3 berdasarkan input picker
		themeHelper.generateFromBaseColor((int)warnaDariPicker);
		
		// Ambil warna primary aktif, lalu kunci transparansinya di 12% (0x1F) agar selembut bawaan sistem
		int warnaTemaAsli = themeHelper.getColor("colorPrimary");
		int warnaRippleSistemDinamis = (warnaTemaAsli & 0x00FFFFFF) | 0x1F000000;
		
		int targetColor = themeHelper.getColor("colorPrimary");
		btnColor = themeHelper.getColor("colorOnSurfaceVariant");
		primaryBtn = themeHelper.getColor("colorPrimaryBtn");
		strokeOffColor = themeHelper.getColor("colorPrimaryDark");
		bgOffColor = themeHelper.getColor("colorSurfaceContainerHigh");
		
		sengame_openbtn.setColors(
		themeHelper.getColor("colorSurfaceContainer"), // Background Track
		primaryBtn, // Warna Thumb
		btnColor, // Warna Teks
		strokeOffColor  // Warna Icon
		);
		
		// 2. Warnai Latar Belakang Utama Aplikasi
		background.setBackgroundColor(themeHelper.getColor("colorSurface"));
		searchbar.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)60, themeHelper.getColor("colorSecondaryContainer")));
		shellinputbg.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)40, themeHelper.getColor("colorSecondaryContainer")));
		
		// 3. Warnai Layout-layout Hardcode menggunakan method helper bawaan package baru
		setColorRadius(devicebg, 16, themeHelper.getColor("colorSurfaceContainerHigh"));
		setColorRadius(boardbg, 16, themeHelper.getColor("colorSurfaceContainerHigh"));
		setColorRadius(archbg, 16, themeHelper.getColor("colorSurfaceContainerHigh"));
		setColorRadius(deviceinfobg, 32, themeHelper.getColor("colorSecondaryContainer"));
		themeHelper.setColorRipple(actionbtn, primaryBtn, 13f);
		
		themeHelper.setColorRipple(supportmebtn, themeHelper.getColor("colorSecondaryContainer"), 16f);
		themeHelper.setColorRipple(mecommunitybtn, themeHelper.getColor("colorSecondaryContainer"), 16f);
		
		themeHelper.setColorRipple(monitoring_feature, themeHelper.getColor("colorSurfaceContainer"), 26f);
		themeHelper.setColorRipple(customdns_feature, themeHelper.getColor("colorSurfaceContainer"), 26f);
		themeHelper.setColorRipple(customreso_feature, themeHelper.getColor("colorSurfaceContainer"), 26f);
		themeHelper.setColorRipple(customdpi_feature, themeHelper.getColor("colorSurfaceContainer"), 26f);
		themeHelper.setColorRipple(applyresobtn, primaryBtn, 13f);
		themeHelper.setColorRipple(resetresobtn, primaryBtn, 13f);
		themeHelper.setColorRipple(applydpibtn, primaryBtn, 13f);
		themeHelper.setColorRipple(resetdpibtn, primaryBtn, 13f);
		
		applyresobtntext.setTextColor(themeHelper.getColor("colorPrimaryDark"));
		applydpibtntext.setTextColor(themeHelper.getColor("colorPrimaryDark"));
		
		themeHelper.setColorRipple(itemsetting_terminaltextsize, themeHelper.getColor("colorSurfaceContainer"), 26f);
		themeHelper.setColorRipple(itemsetting_appearances, themeHelper.getColor("colorSurfaceContainer"), 26f);
		themeHelper.setColorRipple(itemsetting_language, themeHelper.getColor("colorSurfaceContainer"), 26f);
        
		currentview_title.setTextColor(themeHelper.getColor("colorPrimary"));
		currentview_subtitle.setTextColor(themeHelper.getColor("colorOnSurfaceVariant"));
		shellscreen_title.setTextColor(themeHelper.getColor("colorOnSurfaceVariant"));
		settingscreen_title.setTextColor(themeHelper.getColor("colorOnSurfaceVariant"));
		
		themeHelper.applyToEditText((android.widget.EditText) findViewById(R.id.inputsearch));
		themeHelper.applyToEditText((android.widget.EditText) findViewById(R.id.shell_input));
		
		//=== ICON ===
		actionbtn.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		resetresobtnicon.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		applyresobtnicon.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		resetdpibtnicon.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		moduleicon.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		applydpibtnicon.setColorFilter(themeHelper.getColor("colorPrimaryDark"), PorterDuff.Mode.SRC_IN);
		
		ImageView[] icons = {
			itemsetting_terminaltextsize_icon, 
            itemsetting_language_icon,
			appearanceitem_icontheme,
			appearanceitem_iconansi,
			appearanceitem_iconmodule,
			appearanceitem_iconfinish,
			appearancetitle_icon,
			customres_icon,
			customdpi_icon,
			customdns_icon,
			monitoring_icon
		};
		
		ImageView[] btnIcon = {
			infobtn, 
			morebtn,
			settingbtn,
			exec_button,
			search_icon,
			clearbutton,
			backbtn_shellscreen,
			settingbtn_shellscreen,
			backbtn_settingscreen
		};
		
		for (ImageView icon : icons) {
			icon.setColorFilter(targetColor, PorterDuff.Mode.SRC_IN);
		}
		
		for (ImageView iconBtn : btnIcon) {
			iconBtn.setColorFilter(btnColor, PorterDuff.Mode.SRC_IN);
		}
		
		//=== RIPPLE ICON ===
		int[] rippleButtonIds = {
			R.id.infobtn,
			R.id.settingbtn,
			R.id.clearbutton,
			R.id.morebtn,
			R.id.exec_button,
			R.id.backbtn_shellscreen,
			R.id.settingbtn_shellscreen,
			R.id.backbtn_settingscreen
		};
		
		for (int id : rippleButtonIds) {
			View button = findViewById(id);
			if (button != null) {
				themeHelper.setBorderlessRipple(button, warnaRippleSistemDinamis);
			}
		}
		
		if (monitor != null && monitor.isAttachedToWindow()) {
			monitor.findViewById(R.id.holder_icon).setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b, int c, int d) { this.setCornerRadius(a); this.setStroke(b, c); this.setColor(d); return this; } }.getIns((int)26, (int)1, themeHelper.getColor("colorSecondaryContainer"), themeHelper.getColor("colorPrimaryBtn")));
		}
		
		// 5. Inject warna otomatis ke Custom Views asli yang ADA di layout kamu
		themeHelper.applyDynamicColorToInoViews(bottomnavigation); 
		themeHelper.applyDynamicColorToInoViews(itemsetting_terminaltextsize_slider); 
		themeHelper.applyDynamicColorToInoViews(swiperefreshlayout); 
		
		themeHelper.applyDynamicColorToInoViews(switch_monitor); 
		themeHelper.applyDynamicColorToInoViews(inputdns1); 
		themeHelper.applyDynamicColorToInoViews(inputdns2); 
		themeHelper.applyDynamicColorToInoViews(inputwidth); 
		themeHelper.applyDynamicColorToInoViews(inputheight); 
		themeHelper.applyDynamicColorToInoViews(inputdpi); 
		themeHelper.applyDynamicColorToInoViews(switch_customdns); 
		
		themeHelper.applyDynamicColorToInoViews(switch_moduleinstall); 
		themeHelper.applyDynamicColorToInoViews(switch_onfinish); 
		themeHelper.applyDynamicColorToInoViews(switch_ansi); 
		
		themeHelper.applyDynamicColorToInoViews(indicator); 
		
		item_signalbg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
		item_bateraibg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
		item_suhubg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
		item_fpsbg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
		item_waktubg.setBackground(buatBackground(25, 1, strokeOffColor, primaryBtn));
		
		// 6. Sinkronisasi System Bar atas (Status Bar) dan bawah (Navigation Bar) Android
		themeHelper.applyToSystemBars(getWindow());
		
		if (shelloutputbg != null) {
			shelloutputbg.post(new Runnable() {
				@Override
				public void run() {
					themeHelper.applyToScrollbar(shelloutputbg);
				}
			});
		}
		
		if (listAdapter != null) {
			listAdapter.notifyDataSetChanged();
		}
		
		if (((BaseAdapter)listmodule.getAdapter()) != null) {
			((BaseAdapter)listmodule.getAdapter()).notifyDataSetChanged();
		}
	}
	
	public void tampilkanColorPickerDialog() {
		Dialog dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		
		float dp = getResources().getDisplayMetrics().density;
		android.graphics.Typeface inoFont = android.graphics.Typeface.createFromAsset(getAssets(), "fonts/main.ttf");
		
		// ROOT (Meniru persis padding & background dialog "Tentang")
		LinearLayout root = new LinearLayout(MainActivity.this);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setPadding((int)(24 * dp), (int)(24 * dp), (int)(24 * dp), (int)(18 * dp));
		
		root.setBackground(new GradientDrawable(){{
				setColor(themeHelper.getColor("colorSurfaceContainer"));
				setCornerRadius(28 * dp);
			}});
		
		// TITLE
		TextView title = new TextView(MainActivity.this);
		title.setText(LanguageLoader.get("colorpicker_title"));
		title.setTextSize(24);
		title.setTypeface(android.graphics.Typeface.create(inoFont, android.graphics.Typeface.BOLD));
		title.setTextColor(themeHelper.getColor("colorOnSurface"));
		title.setPadding(0, 0, 0, (int)(8 * dp));
		root.addView(title);
		
		// SUBTITLE
		TextView subtitle = new TextView(MainActivity.this);
		subtitle.setText(LanguageLoader.get("colorpicker_subtitle"));
		subtitle.setTextSize(13);
		subtitle.setTypeface(inoFont);
		subtitle.setTextColor(themeHelper.getColor("colorPrimary"));
		subtitle.setPadding(0, 0, 0, (int)(18 * dp));
		root.addView(subtitle);
		
		// INFO / PICKER CONTAINER
		LinearLayout pickerCard = new LinearLayout(MainActivity.this);
		pickerCard.setOrientation(LinearLayout.VERTICAL);
		pickerCard.setGravity(Gravity.CENTER_HORIZONTAL);
		pickerCard.setPadding((int)(16 * dp), (int)(16 * dp), (int)(16 * dp), (int)(16 * dp));
		pickerCard.setBackground(new GradientDrawable(){{
				setCornerRadius(20 * dp);
				setColor(themeHelper.getColor("colorSurfaceContainerHigh"));
			}});
		
		// MEMBACA POSISI WARNA SEBELUMNYA
		// Jika belum ada yang tersimpan (atau sudah di-Reset), picker mulai dari warna default.
		float[] savedHsv = new float[3];
		if (user.contains("color_h") && user.contains("color_s") && user.contains("color_v")) {
			savedHsv[0] = user.getFloat("color_h", 0f);
			savedHsv[1] = user.getFloat("color_s", 1f);
			savedHsv[2] = user.getFloat("color_v", 1f);
		} else {
			Color.colorToHSV(user.getInt("color", DEFAULT_THEME_COLOR), savedHsv);
		}
		System.arraycopy(savedHsv, 0, currentHsv, 0, 3);
		
		// HUE PICKER
		ru.ino.senseix.InoHuePicker huePicker = new ru.ino.senseix.InoHuePicker(MainActivity.this);
		LinearLayout.LayoutParams hueParams = new LinearLayout.LayoutParams((int)(200 * dp), (int)(200 * dp));
		hueParams.setMargins(0, (int)(8 * dp), 0, (int)(16 * dp));
		huePicker.setLayoutParams(hueParams);
		pickerCard.addView(huePicker);
		
		// VALUE PICKER
		ru.ino.senseix.InoSaturationValuePicker valuePicker = new ru.ino.senseix.InoSaturationValuePicker(MainActivity.this);
		LinearLayout.LayoutParams valueParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(48 * dp));
		valueParams.setMargins(0, 0, 0, (int)(16 * dp));
		valuePicker.setLayoutParams(valueParams);
		pickerCard.addView(valuePicker);
		
		// --- SEKSI INPUT FIELD HEX DENGAN BORDER ---
		EditText hexInput = new EditText(MainActivity.this);
		hexInput.setTextSize(14);
		hexInput.setTypeface(inoFont);
		hexInput.setSingleLine(true);
		hexInput.setGravity(Gravity.CENTER);
		hexInput.setHint(LanguageLoader.get("colorpicker_hex_hint"));
		hexInput.setPadding((int)(12 * dp), (int)(10 * dp), (int)(12 * dp), (int)(10 * dp));
		themeHelper.applyToEditText(hexInput);
		
		GradientDrawable borderDrawable = new GradientDrawable();
		borderDrawable.setColor(Color.TRANSPARENT);
		borderDrawable.setCornerRadius(12 * dp);
		borderDrawable.setStroke((int)(1.5f * dp), themeHelper.getColor("colorOutline"));
		hexInput.setBackground(borderDrawable);
		
		LinearLayout.LayoutParams hexParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		hexParams.setMargins(0, 0, 0, (int)(16 * dp));
		hexInput.setLayoutParams(hexParams);
		pickerCard.addView(hexInput);
		
		// PREVIEW CARD BAR
		LinearLayout previewRow = new LinearLayout(MainActivity.this);
		previewRow.setOrientation(LinearLayout.HORIZONTAL);
		previewRow.setGravity(Gravity.CENTER_VERTICAL);
		previewRow.setPadding((int)(4 * dp), 0, 0, 0);
		
		View colorPreview = new View(MainActivity.this);
		LinearLayout.LayoutParams previewParams = new LinearLayout.LayoutParams((int)(32 * dp), (int)(32 * dp));
		colorPreview.setLayoutParams(previewParams);
		
		GradientDrawable previewBg = new GradientDrawable();
		previewBg.setShape(GradientDrawable.OVAL);
		previewBg.setColor(Color.HSVToColor(currentHsv));
		colorPreview.setBackground(previewBg);
		previewRow.addView(colorPreview);
		
		TextView previewText = new TextView(MainActivity.this);
		previewText.setText(LanguageLoader.get("colorpicker_preview"));
		previewText.setTextSize(14);
		previewText.setTypeface(inoFont);
		previewText.setPadding((int)(12 * dp), 0, 0, 0);
		previewText.setTextColor(themeHelper.getColor("colorOnSurfaceVariant"));
		previewRow.addView(previewText);
		
		pickerCard.addView(previewRow);
		root.addView(pickerCard);
		
		View spacer = new View(MainActivity.this);
		spacer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(18 * dp)));
		root.addView(spacer);
		
		View divider = new View(MainActivity.this);
		divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(1 * dp)));
		divider.setBackgroundColor(themeHelper.getColor("colorOutline"));
		root.addView(divider);
		
		LinearLayout row = new LinearLayout(MainActivity.this);
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setGravity(Gravity.END);
		row.setPadding(0, (int)(14 * dp), 0, 0);
		
		// TOMBOL RESET
		TextView btnCancel = new TextView(MainActivity.this);
		btnCancel.setText(LanguageLoader.get("colorpicker_btn_reset"));
		btnCancel.setTextSize(14);
		btnCancel.setTypeface(android.graphics.Typeface.create(inoFont, android.graphics.Typeface.BOLD));
		btnCancel.setTextColor(themeHelper.getColor("colorPrimary"));
		btnCancel.setPadding((int)(16 * dp), (int)(12 * dp), (int)(16 * dp), (int)(12 * dp));
		btnCancel.setOnClickListener(v -> {
			tampilkanDialogKustom(
			LanguageLoader.get("colorpicker_reset_title"), 
			LanguageLoader.get("dialog_confirm_action"), 
			LanguageLoader.get("colorpicker_reset_message"), 
			LanguageLoader.get("btn_cancel"), 
			LanguageLoader.get("btn_proceed"), 
			() -> {},
			() -> {
				// Hapus color + color_h/s/v agar picker kembali ke warna default
				resetPreferensiWarna();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					finishAndRemoveTask();
				} else {
					finish();
				}
			}
			);
			dialog.dismiss();
		});
		row.addView(btnCancel);
		
		// TOMBOL TERAPKAN TEMA
		TextView btnOk = new TextView(MainActivity.this);
		btnOk.setText(LanguageLoader.get("colorpicker_btn_apply"));
		btnOk.setTextSize(14);
		btnOk.setTypeface(android.graphics.Typeface.create(inoFont, android.graphics.Typeface.BOLD));
		btnOk.setPadding((int)(18 * dp), (int)(12 * dp), (int)(18 * dp), (int)(12 * dp));
		btnOk.setTextColor(themeHelper.getColor("colorOnPrimary"));
		themeHelper.setColorRipple(btnOk, themeHelper.getColor("colorPrimary"), 18f);
		
		btnOk.setOnClickListener(v -> {
			int warnaFinal = Color.HSVToColor(currentHsv);
			
			user.edit()
			.putInt("color", warnaFinal)
			.putFloat("color_h", currentHsv[0])
			.putFloat("color_s", currentHsv[1])
			.putFloat("color_v", currentHsv[2])
			.commit();
			
			MainActivity.this.terapkanWarnaTemaDinamis(warnaFinal);
			dialog.dismiss();
		});
		row.addView(btnOk);
		root.addView(row);
		
		// INI INTERFACE LISTENERS & LISTENERS CODES
		hexTextWatcher = new android.text.TextWatcher() {
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					String input = s.toString().trim();
					if (!input.startsWith("#")) {
						input = "#" + input;
					}
					if (input.length() == 7) { 
						int parsedColor = Color.parseColor(input);
						Color.colorToHSV(parsedColor, currentHsv);
						
						huePicker.setHueAndSaturation(currentHsv[0], currentHsv[1]);
						valuePicker.updateHueAndSaturation(currentHsv[0], currentHsv[1]);
						valuePicker.setValue(currentHsv[2]); 
						
						((GradientDrawable) colorPreview.getBackground()).setColor(parsedColor);
					}
				} catch (Exception ignored) {}
			}
			@Override public void afterTextChanged(android.text.Editable s) {}
		};
		
		root.post(() -> {
			hexInput.removeTextChangedListener(hexTextWatcher);
			
			huePicker.setHueAndSaturation(savedHsv[0], savedHsv[1]); 
			valuePicker.updateHueAndSaturation(savedHsv[0], savedHsv[1]); 
			valuePicker.setValue(savedHsv[2]);                            
			
			String initHex = String.format("#%06X", (0xFFFFFF & Color.HSVToColor(savedHsv)));
			hexInput.setText(initHex);
			
			hexInput.addTextChangedListener(hexTextWatcher);
		});
		
		huePicker.setOnColorChangedListener(new ru.ino.senseix.InoHuePicker.OnColorChangedListener() {
			@Override
			public void onColorChanged(float hue, float saturation) {
				currentHsv[0] = hue;
				currentHsv[1] = saturation;
				
				valuePicker.updateHueAndSaturation(currentHsv[0], currentHsv[1]);
				
				int warnaSekarang = Color.HSVToColor(currentHsv);
				((GradientDrawable) colorPreview.getBackground()).setColor(warnaSekarang);
				
				String hexStr = String.format("#%06X", (0xFFFFFF & warnaSekarang));
				
				hexInput.removeTextChangedListener(hexTextWatcher);
				hexInput.setText(hexStr);
				hexInput.addTextChangedListener(hexTextWatcher);
			}
		});
		
		valuePicker.setOnValueModifierListener(new ru.ino.senseix.InoSaturationValuePicker.OnValueModifierListener() {
			@Override
			public void onValueChanged(float value) {
				currentHsv[2] = value;
				
				int warnaSekarang = Color.HSVToColor(currentHsv);
				((GradientDrawable) colorPreview.getBackground()).setColor(warnaSekarang);
				
				String hexStr = String.format("#%06X", (0xFFFFFF & warnaSekarang));
				
				hexInput.removeTextChangedListener(hexTextWatcher);
				hexInput.setText(hexStr);
				hexInput.addTextChangedListener(hexTextWatcher);
			}
		});
		
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
	
	//=========================================
	// CODE GROUP: STATUSRECEIVER (MainActivity)
	//=========================================
	
	private Handler statusHandler = new Handler(Looper.getMainLooper());
	private int lastColorStart = 0;
	private int lastColorEnd = 0;
	private int lastState = -1; // -1: Belum tahu, 0: Online, 1: Offline, 2: Looking for Shizuku
	
	private Runnable statusRunnable = new Runnable() {
		@Override
		public void run() {
			checkAndUpdateStatus();
			statusHandler.postDelayed(this, 1000); // Cek setiap 1 detik
		}
	};
	
	private void checkAndUpdateStatus() {
		boolean isChanged = false;
		
		if (inoka.isBinderReady()) {
			if (inoka.isPermissionGranted()) {
				activationbg.setEnabled(false);
				
				int targetColor = (themeHelper != null) ? themeHelper.getColor("colorPrimary") : 0xFF0D47A1;
				int targetColorEnd = (themeHelper != null) ? themeHelper.getColor("colorPrimaryContainer") : 0xFF1565C0;
				
				activation_title.setText(LanguageLoader.get("status_online_title"));
				activation_subtitle.setText(LanguageLoader.get("status_online_subtitle"));
				activation_icon.setImageResource(R.drawable.app_icon);
				
				// HANYA update background jika warna berubah agar ripple tidak ke-reset
				if (lastColorStart != targetColor || lastColorEnd != targetColorEnd) {
					lastColorStart = targetColor;
					lastColorEnd = targetColorEnd;
					setGradientRipple(activationbg, targetColor, targetColorEnd, 26, GradientDrawable.Orientation.TOP_BOTTOM);
				}
				
				if (lastState != 0) {
					lastState = 0;
					isChanged = true;
				}
			} else {
				activationbg.setEnabled(true);
				
				activation_title.setText(LanguageLoader.get("status_offline_title"));
				activation_subtitle.setText(LanguageLoader.get("status_offline_subtitle"));
				activation_icon.setImageResource(R.drawable.icon_cancel);
				
				int targetColor = 0xFF93060E;
				int targetColorEnd = 0xFFBB565C;
				
				if (lastColorStart != targetColor || lastColorEnd != targetColorEnd) {
					lastColorStart = targetColor;
					lastColorEnd = targetColorEnd;
					setGradientRipple(activationbg, targetColor, targetColorEnd, 26, GradientDrawable.Orientation.TOP_BOTTOM);
				}
				
				if (lastState != 1) {
					lastState = 1;
					isChanged = true;
				}
			}
		} else {
			activationbg.setEnabled(true);
			
			activation_title.setText(LanguageLoader.get("status_looking_title"));
			activation_subtitle.setText(LanguageLoader.get("status_looking_subtitle"));
			activation_icon.setImageResource(R.drawable.icon_security);
			
			int targetColor = themeHelper.getColor("colorSurfaceContainerHigh");
			int targetColorEnd = 0xFF202020;
			
			if (lastColorStart != targetColor || lastColorEnd != targetColorEnd) {
				lastColorStart = targetColor;
				lastColorEnd = targetColorEnd;
				setGradientRipple(activationbg, targetColor, targetColorEnd, 26, GradientDrawable.Orientation.TOP_BOTTOM);
			}
			
			if (lastState != 2) {
				lastState = 2;
				isChanged = true;
			}
		}
		
		// notifyDataSetChanged HANYA dipanggil jika status state benar-benar berubah
		if (isChanged && listAdapter != null) {
			listAdapter.notifyDataSetChanged();
		}
	}
	
	private AppListHelper helper;
	private ExecutorService executorService;
	private Future<?> currentTask; 
	private Dialog loading;
	
	private void loadApplications(final int mode) {
		// 1. DISMISS loading yang lama jika ada sebelum membatalkan task
		if (loading != null && loading.isShowing()) {
			try {
				loading.dismiss();
			} catch (Exception e) {
				// Menghindari crash jika activity dalam state tidak stabil
			}
		}
		
		// 2. Jika ada pencarian data yang sedang berjalan dari filter sebelumnya, BATALKAN!
		if (currentTask != null && !currentTask.isDone()) {
			currentTask.cancel(true); 
		}
		
		int targetColor = (themeHelper != null) ? themeHelper.getColor("colorPrimary") : 0xFF0D47A1;
		
		// Inisialisasi kembali dialog global-nya
		loading = new Dialog(MainActivity.this);
		loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loading.setContentView(R.layout.loading);
		loading.setCancelable(false);
		
		final LinearLayout background = loading.findViewById(R.id.background);
		final LinearLayout loadingbg = loading.findViewById(R.id.loadingbg);
		final ru.ino.senseix.InoCircularProgress circular = loading.findViewById(R.id.circular);
		final ru.ino.senseix.InoLoadingIndicator indicator = loading.findViewById(R.id.indicator);
		final ru.ino.senseix.CustomFillView appicon = loading.findViewById(R.id.appicon);
		
		if (indicator != null) indicator.setVisibility(View.GONE);
		if (circular != null) circular.setVisibility(View.GONE);
		
		if (appicon != null) {
			appicon.setIcon(R.drawable.app_icon);
			appicon.setIconColor(Color.parseColor("#212121"));
			appicon.setFillColor(targetColor);
			// Note: appicon.start(2000) dilepas jika kamu ingin animasi pengisian icon
			// murni mengikuti progress realtime 0-100%.
		}
		
		if (loading.getWindow() != null) {
			loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			loading.getWindow().setGravity(Gravity.CENTER);
		}
		
		loading.show(); 
		
		// 3. Jalankan tugas pencarian baru dengan Progress Listener
		currentTask = executorService.submit(new Runnable() {
			@Override
			public void run() {
				final ArrayList<HashMap<String, Object>> fetchedApps = helper.getAppList(mode, new AppListHelper.OnProgressListener() {
					@Override
					public void onProgressUpdate(final int progress) {
						// Update tampilan progress di UI Thread
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (isDestroyed() || isFinishing() || loading == null || !loading.isShowing()) return;
								
								// Update progress ke CustomFillView
								if (appicon != null) {
									appicon.setProgress(progress); // Panggil setter progress CustomFillView kamu
								}
								
								// Jika kamu juga ingin mengupdate InoCircularProgress
								if (circular != null) {
									circular.setProgress(progress);
								}
							}
						});
					}
				});
				
				if (Thread.currentThread().isInterrupted()) return;
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (isDestroyed() || isFinishing()) return;
						
						originalAppsList.clear();
						originalAppsList.addAll(fetchedApps);
						
						apps.clear();
						apps.addAll(fetchedApps);
						
						if (loading != null && loading.isShowing()) {
							loading.dismiss();
						}
						
						if (listAdapter != null) {
							listAdapter.notifyDataSetChanged();
						}
					}
				});
			}
		});
	}
	
	private final ExecutorService executor = Executors.newFixedThreadPool(4);
	private final Handler mainHandler = new Handler(Looper.getMainLooper());
	private ArrayList<HashMap<String, Object>> moduleList = new ArrayList<>();
	
	private void deleteFolderRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory()) {
			for (File child : fileOrDirectory.listFiles()) {
				deleteFolderRecursive(child);
			}
		}
		fileOrDirectory.delete();
	}
	
	private void loadSenseiXModules() {
		// 1. Bersihkan data moduleList lama (Gunakan moduleList sesuai deklarasi di atas)
		moduleList.clear(); 
		
		// 2. Gunakan path absolute yang stabil
		String path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/.SenseiX/module/";
		File moduleDir = new File(path);
		
		if (moduleDir.exists() && moduleDir.isDirectory()) {
			File[] files = moduleDir.listFiles();
			
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						HashMap<String, Object> map = new HashMap<>();
						map.put("MODULENAME", file.getName());
						// Dimasukkan ke moduleList
						moduleList.add(map);
					}
				}
			}
		}
		
		// 3. Hubungkan ke ListView menggunakan moduleList
		if (listmodule.getAdapter() == null) {
			// Jika adapter belum terpasang, pasang dengan data moduleList
			listmodule.setAdapter(new ListmoduleAdapter(moduleList));
		} else {
			// Jika adapter sudah ada, tinggal di-refresh datanya
			((BaseAdapter) listmodule.getAdapter()).notifyDataSetChanged();
		}
		
		if (moduleList.isEmpty()) {
			// Jika tidak ada modul: Sembunyikan ListView, tampilkan tampilan kosong
			listmodule.setVisibility(View.GONE);
			moduledetail.setVisibility(View.VISIBLE);
			moduleiconbg.setVisibility(View.VISIBLE);
		} else {
			// Jika ada modul: Tampilkan ListView, sembunyikan tampilan kosong
			listmodule.setVisibility(View.VISIBLE);
			moduledetail.setVisibility(View.GONE);
			moduleiconbg.setVisibility(View.GONE);
		}
	}
	
	// Method untuk menghitung total ukuran file di dalam folder (termasuk subfolder)
	private long getFolderSize(File dir) {
		long length = 0;
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					length += file.length();
				} else if (file.isDirectory()) {
					length += getFolderSize(file);
				}
			}
		}
		return length;
	}
	
	// Method untuk mengubah format byte ke B, KB, MB, atau GB
	private String formatFileSize(long size) {
		if (size <= 0) return "0 B";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new java.text.DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	public void showCustomBottomSheet() {
		Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		
		Window window = dialog.getWindow();
		if (window != null) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			
			// Buat Layout Dialog Mengisi Seluruh Layar Termasuk Nav/Status Bar
			window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				window.setStatusBarColor(Color.TRANSPARENT);
				window.setNavigationBarColor(Color.TRANSPARENT); // Transparan total
			}
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
				window.setNavigationBarContrastEnforced(false);
			}
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				window.setDecorFitsSystemWindows(false);
			} else {
				window.getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE 
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				);
			}
		}
		
		float dp = getResources().getDisplayMetrics().density;
		
		Typeface inoFont;
		try {
			inoFont = Typeface.createFromAsset(getAssets(), "fonts/main.ttf");
		} catch (Exception e) {
			inoFont = Typeface.SANS_SERIF;
		}
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View sheetContent = inflater.inflate(R.layout.developer, null);
		
		sheetContent.setBackground(new GradientDrawable() {{
				setColor(themeHelper.getColor("colorSurfaceContainer"));
				setCornerRadii(new float[]{
					28 * dp, 28 * dp,
					28 * dp, 28 * dp,
					0, 0,
					0, 0
				});
			}});
		
		View handleBar = sheetContent.findViewById(R.id.handleBar);
		ImageView avatarImage = sheetContent.findViewById(R.id.avatarImage);
		TextView title = sheetContent.findViewById(R.id.tvTitle);
		TextView subtitle = sheetContent.findViewById(R.id.tvSubtitle);
		TextView quote = sheetContent.findViewById(R.id.tvQuote);
		TextView btnGithub = sheetContent.findViewById(R.id.btnGithub);
		TextView btnTelegram = sheetContent.findViewById(R.id.btnTelegram);
		TextView btnDonasi = sheetContent.findViewById(R.id.btnDonasi);
		
		subtitle.setText(LanguageLoader.get("about_subtitle"));
		quote.setText(LanguageLoader.get("about_quote"));
		btnGithub.setText(LanguageLoader.get("about_btn_github"));
		btnTelegram.setText(LanguageLoader.get("about_btn_telegram"));
		btnDonasi.setText(LanguageLoader.get("about_btn_donation"));
		avatarImage.setContentDescription(LanguageLoader.get("about_avatar_desc"));
		
		if (handleBar != null) {
			handleBar.setBackground(new GradientDrawable() {{
					setColor(themeHelper.getColor("colorOnSurfaceVariant"));
					setCornerRadius(2 * dp);
				}});
		}
		
		avatarImage.setBackground(new GradientDrawable() {{
				setShape(GradientDrawable.OVAL);
				setColor(themeHelper.getColor("colorSurfaceContainerHigh"));
			}});
		
		try {
			java.io.InputStream is = getAssets().open("images/dev.png");
			android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(is);
			avatarImage.setImageBitmap(bitmap);
			is.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
		title.setTextColor(themeHelper.getColor("colorOnSurface"));
		subtitle.setTextColor(themeHelper.getColor("colorPrimary"));
		quote.setTextColor(themeHelper.getColor("colorOnSurfaceVariant"));
		btnGithub.setTextColor(themeHelper.getColor("colorOnSurface"));
		btnTelegram.setTextColor(themeHelper.getColor("colorOnSurface"));
		btnDonasi.setTextColor(themeHelper.getColor("colorOnPrimary"));
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			avatarImage.setClipToOutline(true);
			avatarImage.setOutlineProvider(new ViewOutlineProvider() {
				@Override
				public void getOutline(View view, Outline outline) {
					outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth() / 2f);
				}
			});
			
			if (handleBar != null) {
				handleBar.setClipToOutline(true);
				handleBar.setOutlineProvider(new ViewOutlineProvider() {
					@Override
					public void getOutline(View view, Outline outline) {
						outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getHeight() / 2f);
					}
				});
			}
		}
		
		title.setTypeface(Typeface.create(inoFont, Typeface.BOLD));
		subtitle.setTypeface(inoFont);
		quote.setTypeface(inoFont);
		btnGithub.setTypeface(Typeface.create(inoFont, Typeface.BOLD));
		btnTelegram.setTypeface(Typeface.create(inoFont, Typeface.BOLD));
		btnDonasi.setTypeface(Typeface.create(inoFont, Typeface.BOLD));
		
		themeHelper.setColorRipple(btnGithub, themeHelper.getColor("colorSurfaceContainerHigh"), 20);
		themeHelper.setColorRipple(btnTelegram, themeHelper.getColor("colorSurfaceContainerHigh"), 20);
		themeHelper.setColorRipple(btnDonasi, themeHelper.getColor("colorPrimary"), 20);
		
		// Root Container
		FrameLayout rootLayout = new FrameLayout(MainActivity.this);
		rootLayout.setLayoutParams(new ViewGroup.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, 
		ViewGroup.LayoutParams.MATCH_PARENT
		));
		
		View scrimView = new View(MainActivity.this);
		scrimView.setLayoutParams(new FrameLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, 
		ViewGroup.LayoutParams.MATCH_PARENT
		));
		scrimView.setBackgroundColor(Color.parseColor("#80000000"));
		scrimView.setAlpha(0f);
		rootLayout.addView(scrimView);
		
		FrameLayout.LayoutParams sheetParams = new FrameLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT, 
		ViewGroup.LayoutParams.WRAP_CONTENT
		);
		sheetParams.gravity = Gravity.BOTTOM;
		sheetContent.setLayoutParams(sheetParams);
		
		sheetContent.setVisibility(View.INVISIBLE);
		rootLayout.addView(sheetContent);
		
		final int initialPaddingLeft = sheetContent.getPaddingLeft();
		final int initialPaddingTop = sheetContent.getPaddingTop();
		final int initialPaddingRight = sheetContent.getPaddingRight();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
			sheetContent.setOnApplyWindowInsetsListener((v, insets) -> {
				int bottomInset = 0;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
					bottomInset = insets.getInsets(WindowInsets.Type.navigationBars()).bottom;
				} else {
					bottomInset = insets.getSystemWindowInsetBottom();
				}
				
				// Padding bawah tetap menghitung tinggi Nav Bar agar konten teks/tombol di sheet tidak tertutup garis gesture navigation
				v.setPadding(
				initialPaddingLeft,
				initialPaddingTop,
				initialPaddingRight,
				bottomInset + (int)(16 * dp)
				);
				return insets;
			});
		}
		
		dialog.setContentView(rootLayout);
		
		scrimView.setOnClickListener(v -> dismissM3Sheet(dialog, sheetContent, scrimView));
		
		rootLayout.post(() -> {
			int sheetHeight = sheetContent.getHeight();
			
			sheetContent.setTranslationY(sheetHeight);
			sheetContent.setVisibility(View.VISIBLE);
			
			// Animasi Masuk
			sheetContent.animate()
			.translationY(0)
			.setDuration(300)
			.setInterpolator(new AccelerateDecelerateInterpolator())
			.start();
			
			scrimView.animate()
			.alpha(1f)
			.setDuration(300)
			.start();
			
			// Touch listener untuk dragging
			sheetContent.setOnTouchListener(new View.OnTouchListener() {
				private float initialDownY;
				private boolean isDragging = false;
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
						initialDownY = event.getRawY();
						isDragging = true;
						return true;
						
						case MotionEvent.ACTION_MOVE:
						if (!isDragging) return false;
						float deltaY = event.getRawY() - initialDownY;
						
						// UBAH DI SINI: Kunci drag agar MENTOK di posisi paling atas (translationY = 0)
						if (deltaY > 0) {
							sheetContent.setTranslationY(deltaY);
							float progress = 1f - (deltaY / sheetHeight);
							scrimView.setAlpha(Math.max(0f, progress));
						} else {
							// Jika deltaY <= 0 (ditarik ke atas), tahan di angka 0 (tidak bisa tertarik lebih tinggi lagi)
							sheetContent.setTranslationY(0f);
						}
						return true;
						
						case MotionEvent.ACTION_UP:
						case MotionEvent.ACTION_CANCEL:
						isDragging = false;
						float totalDragged = event.getRawY() - initialDownY;
						
						if (totalDragged > sheetHeight * 0.3f) {
							dismissM3Sheet(dialog, sheetContent, scrimView);
						} else {
							sheetContent.animate()
							.translationY(0)
							.setDuration(200)
							.setInterpolator(new DecelerateInterpolator())
							.start();
							
							scrimView.animate()
							.alpha(1f)
							.setDuration(200)
							.start();
						}
						return true;
					}
					return false;
				}
			});
		});
		
		btnGithub.setOnClickListener(v -> {
			dismissM3Sheet(dialog, sheetContent, scrimView);
			try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/IqwanXD"))); } catch (Exception ignored) {}
		});
		
		btnTelegram.setOnClickListener(v -> {
			dismissM3Sheet(dialog, sheetContent, scrimView);
			try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/IqwanoINO"))); } catch (Exception ignored) {}
		});
		
		btnDonasi.setOnClickListener(v -> {
			dismissM3Sheet(dialog, sheetContent, scrimView);
			try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://saweria.co/IqwanoINO"))); } catch (Exception ignored) {}
		});
		
		dialog.show();
	}
	
	private void dismissM3Sheet(Dialog dialog, View sheetContent, View scrimView) {
		sheetContent.animate()
		.translationY(sheetContent.getHeight())
		.setDuration(250)
		.setInterpolator(new AccelerateInterpolator())
		.start();
		
		scrimView.animate()
		.alpha(0f)
		.setDuration(250)
		.withEndAction(dialog::dismiss)
		.start();
	}
	
	
	// static: bertahan selama proses app hidup, walau activity di-recreate
	private static final HashSet<String> activeModules = new HashSet<>();
	
	
	{
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
			
			HashMap<String, Object> item = _data.get(_position);
			
			icon.setImageDrawable((Drawable) item.get("icon"));
			name.setText(item.get("name").toString());
			packagename.setText(item.get("package").toString());
			version.setText(item.get("version").toString());
			
			if (themeHelper != null) {
				themeHelper.setColorRipple(background, themeHelper.getColor("colorSurfaceContainerHigh"), 16f);
				themeHelper.applyDynamicColorToInoViews(switch_mode); 
			} else {
				// Backup jika themeHelper belum terinisialisasi
				setColorRipple(background, 0xFF040D1C, 16f);
			}
			
			iconbg.setRadius(21);
			
			if (name.getText().toString().contains("Pojav")) name.setText("Pojav Launcher");
			if (name.getText().toString().contains("Minecraft")) name.setText("Minecraft");
			if (name.getText().toString().contains("Mobile Legends")) name.setText("Mobile Legends");
			
			setFont(_view); 
			
			// ========================================================
			// DETEKSI TIPE GAME & ATUR VISIBILITAS SWITCH_MODE
			// ========================================================
			boolean isGame = false;
			if (item.containsKey("isGame") && item.get("isGame") != null) {
				isGame = (boolean) item.get("isGame");
			}
			
			if (isGame) {
				switch_mode.setVisibility(View.VISIBLE);
				
				// Set status listener switch hanya untuk game
				final String currentPackage = item.get("package").toString();
				
				switch_mode.setOnCheckedChangeListener(null); // Clear listener dulu agar tidak trigger loop saat recycle view
				switch_mode.setChecked(activePackages.contains(currentPackage));
				
				switch_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							activePackages.add(currentPackage);
						} else {
							activePackages.remove(currentPackage);
						}
					}
				});
			} else {
				// Jika bukan game, sembunyikan switch dan lepas listener-nya
				switch_mode.setVisibility(View.GONE);
				switch_mode.setOnCheckedChangeListener(null);
			}
			
			
			return _view;
		}
	}
	
    private void applyLanguage(String code) {
	// shell_output ikut ditimpa LanguageLoader.apply(), jadi log terminal diselamatkan dulu
	CharSequence shellNow = shell_output.getText();
	boolean shellUntouched = shellNow.toString().equals(LanguageLoader.get("shell_output", ""));
	CharSequence shellKeep = new android.text.SpannableStringBuilder(shellNow);

	if (!LanguageLoader.set(MainActivity.this, LanguageMenu.path(code))) return;
	user.edit().putString("lang", code).apply();

	if (!shellUntouched) shell_output.setText(shellKeep);
	refreshLanguageDynamic(code);
}

private void refreshLanguageDynamic(String code) {
	String[] t = {"page_home_title", "page_apps_title", "page_module_title"};
	String[] s = {"page_home_subtitle", "page_apps_subtitle", "page_module_subtitle"};
	int p = Math.max(0, Math.min(2, viewpager.getCurrentItem()));

	currentview_title.setText(LanguageLoader.get(t[p]));
	currentview_subtitle.setText(LanguageLoader.get(s[p]));
	for (int i = 0; i < 3; i++) bottomnavigation.setTabTitle(i, LanguageLoader.get(t[i]));
	sengame_openbtn.setSliderText(LanguageLoader.get("sengame_slider"));
	itemsetting_language_value.setText(LanguageMenu.nameOf(code));

	lastState = -1;
	checkAndUpdateStatus();
}

	public class ListmoduleAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public ListmoduleAdapter(ArrayList<HashMap<String, Object>> _arr) {
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
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.module, null);
			}
			
			final ru.ino.senseix.RadiusLinearLayout background = _view.findViewById(R.id.background);
			final FrameLayout framebg = _view.findViewById(R.id.framebg);
			final ru.ino.senseix.VideoLayout video = _view.findViewById(R.id.video);
			final ImageView image = _view.findViewById(R.id.image);
			final LinearLayout gradient = _view.findViewById(R.id.gradient);
			final LinearLayout informationbg = _view.findViewById(R.id.informationbg);
			final LinearLayout detailbg = _view.findViewById(R.id.detailbg);
			final LinearLayout switchbg = _view.findViewById(R.id.switchbg);
			final LinearLayout titlebar = _view.findViewById(R.id.titlebar);
			final TextView version = _view.findViewById(R.id.version);
			final TextView author = _view.findViewById(R.id.author);
			final TextView description = _view.findViewById(R.id.description);
			final TextView size = _view.findViewById(R.id.size);
			final TextView type = _view.findViewById(R.id.type);
			final TextView action = _view.findViewById(R.id.action);
			final ru.ino.senseix.MaterialSwitch switch_mode = _view.findViewById(R.id.switch_mode);
			final LinearLayout spacer = _view.findViewById(R.id.spacer);
			final ImageView deletebtn = _view.findViewById(R.id.deletebtn);
			
			version.setText("");
			author.setText("");
			description.setText("");
			size.setText("");
			type.setText("");
			action.setText("");
			video.setVisibility(View.GONE);
			image.setVisibility(View.GONE);
			image.setImageDrawable(null);
			
			background.setRadius(40f);
			
			GradientDrawable radiasi = new GradientDrawable(
			GradientDrawable.Orientation.LEFT_RIGHT,
			new int[] {
				Color.BLACK,
				Color.TRANSPARENT
			}
			);
			
			gradient.setBackground(radiasi);
			
			final String moduleName = String.valueOf(moduleList.get(_position).get("MODULENAME"));
			
			switch_mode.setOnCheckedChangeListener(null);
			switch_mode.setChecked(activeModules.contains(moduleName));
			
			_view.setTag(moduleName);
			final View finalView = _view;
			final String folderPath = "/sdcard/.SenseiX/module/" + moduleName + "/";
			setFont(_view); 
			
			executor.execute(() -> {
				File folder = new File(folderPath);
				if (!folder.exists() || !folder.isDirectory()) return;
				
				// --- HITUNG UKURAN SELURUH FOLDER MODULE ---
				long totalSizeBytes = getFolderSize(folder);
				final String formattedSize = formatFileSize(totalSizeBytes);
				
				// 1. Cari file apa saja yang diawali kata "structur"
				File jsonFile = null;
				File[] allFiles = folder.listFiles();
				if (allFiles != null) {
					for (File f : allFiles) {
						if (f.isFile() && f.getName().toLowerCase().startsWith("structur")) {
							jsonFile = f;
							break;
						}
					}
				}
				
				if (jsonFile == null || !jsonFile.exists()) {
					android.util.Log.e("SenseiX_Adapter", "Tidak menemukan file structur.* di: " + folderPath);
					return;
				}
				
				try {
					FileInputStream fis = new FileInputStream(jsonFile);
					int fileSize = fis.available();
					byte[] buffer = new byte[fileSize];
					fis.read(buffer);
					fis.close();
					
					String jsonString = new String(buffer, "UTF-8");
					final JSONObject jsonObject = new JSONObject(jsonString);
					
					// Data dari JSON (txtSize tidak lagi diambil dari JSON)
					final String txtType = jsonObject.optString("type", "");
					final String txtAction = jsonObject.optString("action", "");
					final String txtVersion = jsonObject.optString("version", "");
					final String txtAuthor = jsonObject.optString("author", "");
					final String txtDescription = jsonObject.optString("description", "");
					final String mediaType = jsonObject.optString("type_media", jsonObject.optString("type", "")).toLowerCase(); 
					final String scriptCommand = jsonObject.optString("script", "");
					final String scriptOffCommand = jsonObject.optString("script_off", "");
					
					// Cek gambar lokal
					Bitmap bitmap = null;
					if ("image".equals(mediaType)) {
						File imgFile = new File(folderPath + "image.png");
						if (!imgFile.exists()) imgFile = new File(folderPath + "image.PNG");
						if (!imgFile.exists()) imgFile = new File(folderPath + "image.jpg");
						if (!imgFile.exists()) imgFile = new File(folderPath + "image.jpeg");
						
						if (imgFile.exists()) {
							bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
						}
					}
					
					final Bitmap finalBitmap = bitmap;
					
					// Kembalikan data ke UI Thread utama
					mainHandler.post(() -> {
						if (!finalView.getTag().equals(moduleName)) return;
						
						// Tempel data ke TextView (size diambil dari ukuran folder asli)
						size.setText(formattedSize);
						type.setText(txtType);
						action.setText(txtAction);
						version.setText(txtVersion.contains("Version") ? txtVersion : "Version : " + txtVersion);
						author.setText(txtAuthor.contains("Author") ? txtAuthor : "Author : " + txtAuthor);
						description.setText(txtDescription);
						
						// Atur visibilitas media sesuai tipenya
						if ("image".equals(mediaType)) {
							if (finalBitmap != null) {
								image.setVisibility(View.VISIBLE);
								image.setImageBitmap(finalBitmap);
							} else {
								image.setVisibility(View.VISIBLE);
								image.setImageResource(R.drawable.icon_cancel);
							}
						} else if ("video".equals(mediaType)) {
							File vidFile = new File(folderPath + "video.mp4");
							if (vidFile.exists()) {
								video.setVisibility(View.VISIBLE);
								video.setVideo(vidFile.getAbsolutePath());
								video.setMute(true);
								video.setLoop(true);
								video.play();
							}
						}
						
						switch_mode.setOnCheckedChangeListener((buttonView, isChecked) -> {
							String commandToRun = isChecked ? scriptCommand : scriptOffCommand;
							
							if (isChecked) activeModules.add(moduleName);
							else activeModules.remove(moduleName);
							
							if (!commandToRun.isEmpty()) {
								inoka.exec(commandToRun, new ShizukuHelper.Callback() {
									@Override public void onOutput(String line) {}
									@Override public void onFinish(String result) {}
								});
							}
						});
					});
					
				} catch (Exception e) {
					android.util.Log.e("SenseiX_Adapter", "File ditemukan tetapi format isinya bukan JSON yang valid.");
					e.printStackTrace();
				}
			});
			
			// Logika hapus modul
			deletebtn.setOnClickListener(v -> {
				File folderToDelete = new File(folderPath);
				if (folderToDelete.exists()) {
					activeModules.remove(moduleName);
					deleteFolderRecursive(folderToDelete);
					loadSenseiXModules();
				}
			});
			
			size.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)21, 0xFF151515));
			type.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)21, 0xFF151515));
			action.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)21, 0xFF151515));
			deletebtn.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)21, 0xFF151515));
			author.setTextColor(0x80FFFFFF);
			version.setTextColor(0xFFB1A8A9);
			description.setTextColor(0x65FFFFFF);
			themeHelper.applyDynamicColorToInoViews(switch_mode); 
			deletebtn.setColorFilter(themeHelper.getColor("colorPrimaryBtn"), PorterDuff.Mode.SRC_IN);
			
			size.setTextColor(themeHelper.getColor("colorOnSurfaceVariant"));
			type.setTextColor(themeHelper.getColor("colorPrimaryBtn"));
			action.setTextColor(themeHelper.getColor("colorOnSurfaceVariant"));
			
			
			return _view;
		}
	}
}
