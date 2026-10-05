package ru.ino.senseix;

import android.content.Intent;
import android.net.VpnService;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;

public class MyVpnService extends VpnService {
	
	private ParcelFileDescriptor vpn;
	
	// ================= DNS =================
	
	/*private static String dns1 =
"94.140.14.14";
private static String dns2 =
"94.140.15.15";*/	
	
	private static String dns1 =
	"185.228.168.9";
	private static String dns2 =
	"185.228.169.9";
	
	// ================= START =================
	
	@Override
	public int onStartCommand(
	Intent intent,
	int flags,
	int startId
	) {
		
		// stop action
		if (intent != null) {
			
			String action =
			intent.getAction();
			
			if ("STOP".equals(action)) {
				
				stopVpn();
				
				return START_NOT_STICKY;
			}
		}
		
		startVpn();
		
		return START_STICKY;
	}
	
    // ================= START VPN =================
    
    private void startVpn() {
        try {
            if (vpn != null) {
                vpn.close();
                vpn = null;
            }
            
            Builder builder = new Builder();
            builder.setSession("IqwanoINO DNS")
                   .addAddress("10.0.0.2", 24)
                   .addDnsServer(dns1)
                   .addDnsServer(dns2);
            
            /* * SOLUSI JITU: Jangan rute IP DNS publiknya! 
             * Kita berikan rute pseudo/lokal kosong agar Android tahu VPN ini aktif,
             * tetapi lalu lintas data asli & DNS asli akan di-resolve langsung oleh sistem Android 
             * ke server DNS yang kita set di atas via jalur internet utama (tanpa masuk interface VPN).
             */
            builder.addRoute("10.0.0.0", 32); 

            // Izinkan aplikasi sendiri bypass VPN
            try {
                builder.addDisallowedApplication(getPackageName());
            } catch (Exception ignored) {}
            
            vpn = builder.establish();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	// ================= STOP VPN =================
	
	private void stopVpn() {
		
		try {
			
			if (vpn != null) {
				
				vpn.close();
				vpn = null;
			}
			
		} catch (Exception ignored) {
		}
		
		stopForeground(true);
		
		stopSelf();
	}
	
	// ================= CHANGE DNS =================
	
	public static void setDns(
	String primary,
	String secondary
	) {
		
		dns1 = primary;
		dns2 = secondary;
	}
	
	// ================= GET DNS =================
	
	public static String getPrimaryDns() {
		return dns1;
	}
	
	public static String getSecondaryDns() {
		return dns2;
	}
	
	// ================= DESTROY =================
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		
		try {
			
			if (vpn != null) {
				
				vpn.close();
				vpn = null;
			}
			
		} catch (Exception ignored) {
		}
	}
	
	// ================= BIND =================
	
	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}
}
