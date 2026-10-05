package ru.ino.senseix;

import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.*;
import android.os.Build;
import android.os.Process;

import java.io.File;
import java.util.*;

public class AppListHelper {
	
	public static boolean showGB = false;
	
	public static final int MODE_ALL = 0;
	public static final int MODE_SYSTEM = 1;
	public static final int MODE_USER = 2;
	public static final int MODE_GAMES = 3;
	
	private Context context;
	
	// Listener untuk progress update realtime
	public interface OnProgressListener {
		void onProgressUpdate(int progress);
	}
	
	public AppListHelper(Context ctx) {
		this.context = ctx;
	}
	
	/* ===============================
       GET LIST APP (HASHMAP VIA MODE)
       =============================== */	
	public ArrayList<HashMap<String, Object>> getAppList(int mode) {
		return getAppList(mode, null);
	}

	public ArrayList<HashMap<String, Object>> getAppList(int mode, OnProgressListener listener) {
		
		ArrayList<HashMap<String, Object>> list = new ArrayList<>();
		PackageManager pm = context.getPackageManager();
		
		List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		List<String> gamePackages = getSystemGamePackages();

		int totalApps = apps.size();
		
		for (int i = 0; i < totalApps; i++) {
			
			// Cek interupsi thread jika task dibatalkan dari luar
			if (Thread.currentThread().isInterrupted()) {
				return list;
			}

			ApplicationInfo info = apps.get(i);
			
			boolean isSystem = (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
			boolean isGame = false;
			
			// GAME DETECTION
			if (gamePackages.contains(info.packageName)) {
				isGame = true;
			} else {
				try {
					if (info.category == ApplicationInfo.CATEGORY_GAME)
						isGame = true;
					else if ((info.flags & ApplicationInfo.FLAG_IS_GAME) != 0)
						isGame = true;
				} catch (Throwable ignore) {}
			}
			
			// FILTER
			boolean filterMatch = true;
			if (mode == MODE_SYSTEM && !isSystem) filterMatch = false;
			if (mode == MODE_USER && isSystem) filterMatch = false;
			if (mode == MODE_GAMES && !isGame) filterMatch = false;

			if (filterMatch) {
				try {
					PackageInfo pkgInfo = pm.getPackageInfo(info.packageName, 0);
					
					HashMap<String, Object> map = new HashMap<>();
					map.put("name", pm.getApplicationLabel(info).toString());
					map.put("package", info.packageName);
					map.put("icon", pm.getApplicationIcon(info));
					map.put("version", pkgInfo.versionName != null ? pkgInfo.versionName : "—");
					map.put("isSystem", isSystem);
					map.put("isGame", isGame);
					
					// REAL SIZE
					long size = getRealAppSize(info);
					map.put("size", size);
					
					list.add(map);
					
				} catch (Exception ignored) {}
			}

			// Kirim progress 0 - 100
			if (listener != null && totalApps > 0) {
				int progress = (int) (((i + 1) / (float) totalApps) * 100);
				listener.onProgressUpdate(progress);
			}
		}
		
		return list;
	}
	
	/* ===============================
       GET LIST APP FROM PACKAGES ARRAY
       =============================== */	
	public ArrayList<HashMap<String, Object>> getAppListFromPackages(String[] packages) {
		return getAppListFromPackages(packages, null);
	}

	public ArrayList<HashMap<String, Object>> getAppListFromPackages(String[] packages, OnProgressListener listener) {
		ArrayList<HashMap<String, Object>> list = new ArrayList<>();
		if (packages == null || packages.length == 0) return list;
		
		PackageManager pm = context.getPackageManager();
		List<String> gamePackages = getSystemGamePackages();
		HashSet<String> gameSet = new HashSet<>(gamePackages);

		int totalPackages = packages.length;
		
		for (int i = 0; i < totalPackages; i++) {
			
			if (Thread.currentThread().isInterrupted()) {
				return list;
			}

			String packageName = packages[i];
			if (packageName != null && !packageName.isEmpty()) {
				try {
					ApplicationInfo info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
					PackageInfo pkgInfo = pm.getPackageInfo(packageName, 0);
					
					boolean isSystem = (info.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
					boolean isGame = false;
					
					// GAME DETECTION
					if (gameSet.contains(info.packageName)) {
						isGame = true;
					} else {
						try {
							if (info.category == ApplicationInfo.CATEGORY_GAME)
								isGame = true;
							else if ((info.flags & ApplicationInfo.FLAG_IS_GAME) != 0)
								isGame = true;
						} catch (Throwable ignore) {}
					}
					
					HashMap<String, Object> map = new HashMap<>();
					map.put("name", pm.getApplicationLabel(info).toString());
					map.put("package", info.packageName);
					map.put("icon", pm.getApplicationIcon(info));
					map.put("version", pkgInfo.versionName != null ? pkgInfo.versionName : "—");
					map.put("isSystem", isSystem);
					map.put("isGame", isGame);
					
					// REAL SIZE
					long size = getRealAppSize(info);
					map.put("size", size);
					
					list.add(map);
					
				} catch (PackageManager.NameNotFoundException e) {
					// Dilewati jika aplikasi tidak terpasang di HP
				} catch (Exception ignored) {}
			}

			// Kirim progress 0 - 100
			if (listener != null && totalPackages > 0) {
				int progress = (int) (((i + 1) / (float) totalPackages) * 100);
				listener.onProgressUpdate(progress);
			}
		}
		
		return list;
	}
	
	/* ===============================
       OVERLOAD GET LIST APP VIA ARRAY
       =============================== */	
	public ArrayList<HashMap<String, Object>> getAppList(String[] packages) {
		return getAppListFromPackages(packages, null);
	}

	public ArrayList<HashMap<String, Object>> getAppList(String[] packages, OnProgressListener listener) {
		return getAppListFromPackages(packages, listener);
	}
	
	/* ===============================
       REAL APP SIZE (AKURAT)
       =============================== */	
	private long getRealAppSize(ApplicationInfo info) {
		
		// Android 8+
		if (Build.VERSION.SDK_INT >= 26) {
			try {
				StorageStatsManager ssm =
				(StorageStatsManager) context.getSystemService(Context.STORAGE_STATS_SERVICE);
				
				StorageStats stats = ssm.queryStatsForPackage(
				info.storageUuid,
				info.packageName,
				Process.myUserHandle()
				);
				
				long apk = stats.getAppBytes();
				long data = stats.getDataBytes();
				
				return apk + data;
				
			} catch (Exception ignored) {
				return getFallbackSize(info); // fallback
			}
		}
		
		// Android 5–7 fallback
		return getFallbackSize(info);
	}
	
	/* ===============================
       FALLBACK SIZE (APK + /data/data)
       =============================== */	
	private long getFallbackSize(ApplicationInfo info) {
		long total = 0;
		
		// APK size
		try { total += new File(info.sourceDir).length(); } catch (Throwable ignore) {}
		
		// dataDir size
		try { total += folderSize(new File(info.dataDir)); } catch (Throwable ignore) {}
		
		return total;
	}
	
	private long folderSize(File dir) {
		long size = 0;
		
		if (dir == null || !dir.exists()) return 0;
		
		try {
			File[] files = dir.listFiles();
			if (files == null) return 0;
			
			for (File f : files) {
				if (f.isDirectory()) size += folderSize(f);
				else size += f.length();
			}
		} catch (Throwable ignore) {}
		
		return size;
	}
	
	/* ===============================
       DETECT GAME VIA USAGESTATS
       =============================== */	
	private List<String> getSystemGamePackages() {
		List<String> result = new ArrayList<>();
		
		try {
			UsageStatsManager usm =
			(UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
			
			long now = System.currentTimeMillis();
			List<UsageStats> stats =
			usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, now);
			
			if (stats != null) {
				PackageManager pm = context.getPackageManager();
				
				for (UsageStats stat : stats) {
					try {
						ApplicationInfo info =
						pm.getApplicationInfo(stat.getPackageName(), 0);
						
						if (info.category == ApplicationInfo.CATEGORY_GAME)
							result.add(info.packageName);
						
					} catch (Exception ignored) {}
				}
			}
		} catch (Throwable ignore) {}
		
		return result;
	}
	
	/* ===============================
       FORMAT SIZE (MB/GB)
       =============================== */	
	public static String readableSize(long bytes) {
		if (bytes <= 0) return "0 MB";
		
		double mb = bytes / 1024.0 / 1024.0;
		
		if (showGB && mb >= 1024) {
			double gb = mb / 1024.0;
			return String.format(Locale.US, "%.2f GB", gb);
		}
		
		return String.format(Locale.US, "%.1f MB", mb);
	}
}
