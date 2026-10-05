package ru.ino.senseix;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.os.Handler;
import android.os.Looper;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import android.net.NetworkCapabilities; 

public class PingHelper {
	
	private static Handler loopHandler;
	private static Runnable loopRunnable;
	private static boolean isProcessing = false; // Pengaman agar proses tidak menumpuk
	
	public interface PingListener {
		void onResult(String ms);
	}
	
	public static void mulaiLoopPing(Context context, int jedaMilidetik, PingListener listener) {
		stopLoopPing();
		
		loopHandler = new Handler(Looper.getMainLooper());
		isProcessing = false;
		
		loopRunnable = new Runnable() {
			@Override
			public void run() {
				if (isProcessing) {
					if (loopHandler != null && loopRunnable != null) {
						loopHandler.postDelayed(this, jedaMilidetik);
					}
					return;
				}
				
				isProcessing = true;
				
				new Thread(() -> {
					String hasilPing = ambilPingSatuKali(context);
					
					if (loopHandler != null) {
						loopHandler.post(() -> {
							isProcessing = false; // Buka kunci pengaman setelah hasil didapat
							if (listener != null && loopHandler != null) {
								listener.onResult(hasilPing);
							}
						});
					}
				}).start();
				
				if (loopHandler != null) {
					loopHandler.postDelayed(this, jedaMilidetik);
				}
			}
		};
		
		loopHandler.post(loopRunnable);
	}
	
	public static void stopLoopPing() {
		if (loopHandler != null && loopRunnable != null) {
			loopHandler.removeCallbacks(loopRunnable);
			loopHandler = null;
			loopRunnable = null;
		}
		isProcessing = false;
	}
	
	private static String ambilPingSatuKali(Context context) {
		double hasilMs = 0;
		List<String> daftarIpDns = new ArrayList<>();
		
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
					android.net.Network activeNet = cm.getActiveNetwork();
					if (activeNet != null) {
						LinkProperties lp = cm.getLinkProperties(activeNet);
						if (lp != null && !lp.getDnsServers().isEmpty()) {
							for (java.net.InetAddress dnsServer : lp.getDnsServers()) {
								daftarIpDns.add(dnsServer.getHostAddress());
							}
						}
					}
				}
			}
		} catch (Exception ignored) {}
		
		// DNS Cadangan Utama yang di-ping kalau di luar aplikasi
		daftarIpDns.add("1.1.1.1"); 
		daftarIpDns.add("8.8.8.8"); 
		
		for (String ip : daftarIpDns) {
			Socket socket = null;
			try {
				socket = new Socket();
				// Port 53 adalah port standar untuk DNS handshake
				InetSocketAddress socketAddress = new InetSocketAddress(ip, 53);
				
				long waktuMulai = System.nanoTime();
				// Coba hubungkan dengan batas waktu (timeout) 800ms
				socket.connect(socketAddress, 800);
				long waktuSelesai = System.nanoTime();
				
				// Hitung selisih waktu dalam milidetik (ms)
				hasilMs = (waktuSelesai - waktuMulai) / 1_000_000.0;
				break; // Jika sukses, langsung keluar dari loop
			} catch (Exception ignored) {
				hasilMs = 0; // Jika gagal/RTO, lanjut coba IP berikutnya
			} finally {
				if (socket != null) {
					try { socket.close(); } catch (Exception ignored) {}
				}
			}
		}
		
		// Jika sukses mendapat respons angka, format hasilnya ke "xx.x.ms"
		if (hasilMs > 0) {
			return String.format("%.1f.ms", hasilMs);
		}
		
		return "0.ms";
	}
	
	public static boolean isConnectedWifi(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
					android.net.Network activeNet = cm.getActiveNetwork();
					if (activeNet != null) {
						NetworkCapabilities nc = cm.getNetworkCapabilities(activeNet);
						return nc != null && nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
					}
				} else {
					// Fallback untuk Android lama (di bawah API 23)
					android.net.NetworkInfo netInfo = cm.getActiveNetworkInfo();
					return netInfo != null && netInfo.isConnected() && netInfo.getType() == ConnectivityManager.TYPE_WIFI;
				}
			}
		} catch (Exception ignored) {}
		return false;
	}
	
	public static boolean isConnectedMobileData(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm != null) {
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
					android.net.Network activeNet = cm.getActiveNetwork();
					if (activeNet != null) {
						NetworkCapabilities nc = cm.getNetworkCapabilities(activeNet);
						return nc != null && nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
					}
				} else {
					// Fallback untuk Android lama (di bawah API 23)
					android.net.NetworkInfo netInfo = cm.getActiveNetworkInfo();
					return netInfo != null && netInfo.isConnected() && netInfo.getType() == ConnectivityManager.TYPE_MOBILE;
				}
			}
		} catch (Exception ignored) {}
		return false;
	}
}
