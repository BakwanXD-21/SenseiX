package ru.ino.senseix;

import android.app.ActivityManager;
import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GhzMonitor {
	
	// Membaca kecepatan CPU saat ini (MHz)[span_0](start_span)[span_0](end_span)
	public static int getCpuSpeedMhz() {
		int freqMhz = 0;
		try {
			File file = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			if (!file.exists()) {
				file = new File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq");
			}
			if (file.exists()) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				reader.close();
				if (line != null) {
					long freqKhz = Long.parseLong(line.trim());
					freqMhz = (int) (freqKhz / 1000);
				}
			}
		} catch (Exception e) {
			// Aman diabaikan jika berjalan di background thread/service[span_1](start_span)[span_1](end_span)
		}
		return freqMhz;
	}
	
	// Menggabungkan pengambilan informasi CPU dan RAM sekaligus dalam format teks
	public static String getCpuAndRamInfo(Context context) {
		int cpuMhz = getCpuSpeedMhz();
		float ramUsage = getRamUsageGB(context);
		float totalRam = getTotalRamGB(context);
		
		return "CPU: " + cpuMhz + " MHz | RAM: " + String.format("%.2f", ramUsage) + " GB / " + String.format("%.2f", totalRam) + " GB";
	}
	
	// Membaca penggunaan RAM saat ini dalam GB[span_2](start_span)[span_2](end_span)
	public static float getRamUsageGB(Context context) {
		try {
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
			am.getMemoryInfo(memInfo);
			long usedBytes = memInfo.totalMem - memInfo.availMem;
			return usedBytes / (1024f * 1024f * 1024f);
		} catch (Exception e) {
			// Aman diabaikan[span_3](start_span)[span_3](end_span)
		}
		return 0f;
	}
	
	// Membaca total RAM dalam GB[span_4](start_span)[span_4](end_span)
	public static float getTotalRamGB(Context context) {
		try {
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
			am.getMemoryInfo(memInfo);
			return memInfo.totalMem / (1024f * 1024f * 1024f);
		} catch (Exception e) {
			// Aman diabaikan[span_5](start_span)[span_5](end_span)
		}
		return 8f; // fallback default 8GB[span_6](start_span)[span_6](end_span)
	}
}
