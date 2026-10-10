package ru.ino.senseix;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.Locale;

/**
 * SenSpace: floating menu (menu.xml kosong) yang menangkap gesture geser
 * dari kiri ke kanan untuk memunculkan panel left.xml dan right.xml.
 * Diaktifkan saat game yang sudah di-set SenSpace dibuka dari SengameActivity.
 */
public class SenSpaceService extends Service {

	public static final String EXTRA_PACKAGE = "package";
	public static final String ACTION_STOP = "ru.ino.senseix.action.SENSPACE_STOP";

	private static final String CHANNEL_ID = "senspace";
	private static final int NOTIF_ID = 2301;
	private static final float STRIP_WIDTH_DP = 24f;
	private static final float SWIPE_MIN_DP = 60f;

	private WindowManager windowManager;
	private View menuView;
	private FrameLayout panelContainer;
	private View leftView;
	private View rightView;
	private boolean panelsAnimating = false;
	private float density = 1f;

	private final Handler handler = new Handler(Looper.getMainLooper());
	private final Runnable updateRunnable = new Runnable() {
		@Override
		public void run() {
			if (panelContainer == null) return;
			if (leftView != null) {
				TextView cpu = leftView.findViewById(R.id.cpu_value);
				if (cpu != null) {
					cpu.setText(String.format(Locale.US, "%.2f", GhzMonitor.getCpuSpeedMhz() / 1000f));
				}
			}
			if (rightView != null) {
				TextView ram = rightView.findViewById(R.id.ram_value);
				if (ram != null) {
					ram.setText(String.format(Locale.US, "%.2f", GhzMonitor.getRamUsageGB(SenSpaceService.this)));
				}
			}
			handler.postDelayed(this, 800);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		density = getResources().getDisplayMetrics().density;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && ACTION_STOP.equals(intent.getAction())) {
			stopSelf();
			return START_NOT_STICKY;
		}

		// Harus dipanggil cepat setelah startForegroundService.
		startAsForeground();

		if (menuView == null) {
			addGestureStrip();
		}
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		removePanelContainer();
		removeGestureStrip();
		super.onDestroy();
	}

	// ---------------------------------------------------------------- foreground

	@SuppressWarnings("deprecation")
	private void startAsForeground() {
		NotificationManager nm = getSystemService(NotificationManager.class);
		Notification.Builder builder;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(
			CHANNEL_ID, "SenSpace", NotificationManager.IMPORTANCE_LOW);
			nm.createNotificationChannel(channel);
			builder = new Notification.Builder(this, CHANNEL_ID);
		} else {
			builder = new Notification.Builder(this);
		}

		Intent stopIntent = new Intent(this, SenSpaceService.class).setAction(ACTION_STOP);
		PendingIntent stopPending = PendingIntent.getService(
		this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = builder
		.setSmallIcon(R.mipmap.ic_launcher)
		.setContentTitle(LanguageLoader.get("sg_notif_title", "SenSpace active"))
		.setContentText(LanguageLoader.get("sg_notif_text", "Swipe from the left edge to open panels"))
		.setOngoing(true)
		.addAction(0, LanguageLoader.get("sg_notif_stop", "Stop"), stopPending)
		.build();

		startForeground(NOTIF_ID, notification);
	}

	// ---------------------------------------------------------------- gesture strip

	private int overlayType() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
		? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
		: WindowManager.LayoutParams.TYPE_PHONE;
	}

	private void addGestureStrip() {
		try {
			menuView = LayoutInflater.from(this).inflate(R.layout.menu, null);

			WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
			(int) (STRIP_WIDTH_DP * density),
			WindowManager.LayoutParams.MATCH_PARENT,
			overlayType(),
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
			| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
			| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
			| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
			PixelFormat.TRANSLUCENT
			);
			lp.gravity = Gravity.START | Gravity.TOP;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
				lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
			}

			menuView.setOnTouchListener(new View.OnTouchListener() {
				private float downX;
				private float downY;
				private boolean fired;

				@Override
				public boolean onTouch(View v, MotionEvent e) {
					switch (e.getActionMasked()) {
						case MotionEvent.ACTION_DOWN:
							downX = e.getRawX();
							downY = e.getRawY();
							fired = false;
							return true;
						case MotionEvent.ACTION_MOVE:
							if (!fired) {
								float dx = e.getRawX() - downX;
								float dy = e.getRawY() - downY;
								// Geser ke kanan, dominan horizontal
								if (dx > SWIPE_MIN_DP * density && Math.abs(dy) < dx * 0.6f) {
									fired = true;
									showPanels();
								}
							}
							return true;
						default:
							return true;
					}
				}
			});

			windowManager.addView(menuView, lp);
		} catch (Exception e) {
			// Biasanya izin "Display over other apps" belum diberikan.
			e.printStackTrace();
			menuView = null;
			stopSelf();
		}
	}

	private void removeGestureStrip() {
		if (menuView != null && windowManager != null) {
			try {
				windowManager.removeViewImmediate(menuView);
			} catch (Exception ignored) {
			}
		}
		menuView = null;
	}

	// ---------------------------------------------------------------- panels

	private void showPanels() {
		if (panelContainer != null || panelsAnimating || windowManager == null) return;
		panelsAnimating = true;

		panelContainer = new FrameLayout(this);
		panelContainer.setOnClickListener(v -> hidePanels());

		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
		WindowManager.LayoutParams.MATCH_PARENT,
		WindowManager.LayoutParams.MATCH_PARENT,
		overlayType(),
		WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
		| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
		| WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN
		| WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
		PixelFormat.TRANSLUCENT
		);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
		}

		try {
			windowManager.addView(panelContainer, params);
		} catch (Exception e) {
			e.printStackTrace();
			panelContainer = null;
			panelsAnimating = false;
			return;
		}

		// Panel kiri
		leftView = LayoutInflater.from(this).inflate(R.layout.left, null);
		FrameLayout.LayoutParams leftParams = new FrameLayout.LayoutParams(
		ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		leftParams.gravity = Gravity.START | Gravity.FILL_VERTICAL;
		panelContainer.addView(leftView, leftParams);
		leftView.setClickable(true);
		leftView.setVisibility(View.GONE);
		TextView cpuValue = leftView.findViewById(R.id.cpu_value);
		cpuValue.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/value.ttf"), 0);

		// Panel kanan
		rightView = LayoutInflater.from(this).inflate(R.layout.right, null);
		FrameLayout.LayoutParams rightParams = new FrameLayout.LayoutParams(
		ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		rightParams.gravity = Gravity.END | Gravity.FILL_VERTICAL;
		panelContainer.addView(rightView, rightParams);
		rightView.setClickable(true);
		rightView.setVisibility(View.GONE);
		VolumeBarView volumeBar = rightView.findViewById(R.id.volume_bar);
		if (volumeBar != null) volumeBar.rightMode();
		TextView ramValue = rightView.findViewById(R.id.ram_value);
		ramValue.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/value.ttf"), 0);

		handler.post(updateRunnable);

		panelContainer.post(() -> {
			if (leftView == null || rightView == null) {
				panelsAnimating = false;
				return;
			}
			float leftWidth = leftView.getWidth() > 0 ? leftView.getWidth() : 500f;
			float rightWidth = rightView.getWidth() > 0 ? rightView.getWidth() : 500f;

			leftView.setTranslationX(-leftWidth);
			leftView.setVisibility(View.VISIBLE);
			leftView.animate()
			.translationX(0)
			.setDuration(270)
			.setInterpolator(new DecelerateInterpolator())
			.start();

			rightView.setTranslationX(rightWidth);
			rightView.setVisibility(View.VISIBLE);
			rightView.animate()
			.translationX(0)
			.setDuration(300)
			.setInterpolator(new DecelerateInterpolator())
			.withEndAction(() -> panelsAnimating = false)
			.start();
		});
	}

	private void hidePanels() {
		if (panelContainer == null || panelsAnimating || leftView == null || rightView == null) return;
		panelsAnimating = true;
		handler.removeCallbacks(updateRunnable);

		float leftWidth = leftView.getWidth() > 0 ? leftView.getWidth() : 500f;
		float rightWidth = rightView.getWidth() > 0 ? rightView.getWidth() : 500f;

		leftView.animate()
		.translationX(-leftWidth)
		.setDuration(250)
		.setInterpolator(new AccelerateInterpolator())
		.start();

		rightView.animate()
		.translationX(rightWidth)
		.setDuration(250)
		.setInterpolator(new AccelerateInterpolator())
		.withEndAction(() -> {
			removePanelContainer();
			panelsAnimating = false;
		})
		.start();
	}

	private void removePanelContainer() {
		handler.removeCallbacks(updateRunnable);
		if (panelContainer != null && windowManager != null) {
			try {
				windowManager.removeViewImmediate(panelContainer);
			} catch (Exception ignored) {
			}
		}
		panelContainer = null;
		leftView = null;
		rightView = null;
	}
}
