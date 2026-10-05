package ru.ino.senseix;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;

/* By IqwanoINO / Fixed for CRLF Line Endings & Interactive Mode */

public class ShizukuHelper {
	
	private final Activity activity;
	private final int requestCode;
	
	private boolean binderReady = false;
	private boolean permissionGranted = false;
	
	private ShizukuRemoteProcess shizukuProcess;
	private Process normalProcess;
	private BufferedWriter processInputWriter;
	private boolean isRunning = false;
	
	private String currentDir;
	private final String homeDir;
	private final String tmpDir;
	
	private final Handler ui = new Handler(Looper.getMainLooper());
	
	/* ================= CALLBACK ================= */
	
	public interface Callback {
		void onOutput(String line);
		void onFinish(String result);
	}
	
	/* ================= CONSTRUCTOR ================= */
	
	public ShizukuHelper(Activity activity, int requestCode) {
		this.activity = activity;
		this.requestCode = requestCode;
		
		this.homeDir = "/data/local/tmp";
		this.tmpDir = activity.getCacheDir().getAbsolutePath();
		this.currentDir = homeDir;
		
		registerListeners();
		checkInitialState();
	}
	
	private void checkInitialState() {
		if (Shizuku.getBinder() != null) {
			binderReady = true;
			permissionGranted = Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
		}
	}
	
	/* ================= SHIZUKU LISTENERS ================= */
	
	private void registerListeners() {
		Shizuku.addBinderReceivedListenerSticky(() -> binderReady = true);
		
		Shizuku.addBinderDeadListener(() -> {
			binderReady = false;
			permissionGranted = false;
			toast("Shizuku Dead");
		});
		
		Shizuku.addRequestPermissionResultListener((code, result) -> {
			permissionGranted = result == PackageManager.PERMISSION_GRANTED;
			toast(permissionGranted ? "Shizuku permission granted" : "Shizuku permission denied");
		});
	}
	
	public boolean isBinderReady() { return binderReady; }
	public boolean isPermissionGranted() { return permissionGranted; }
	public boolean isShizukuReady() { return binderReady && permissionGranted; }
	public boolean isRunning() { return isRunning; }
	
	/* ================= PERMISSION ================= */
	
	public void requestPermissionIfNeeded() {
		if (!binderReady) {
			toast("Shizuku is Inactive");
			return;
		}
		if (Shizuku.isPreV11()) {
			toast("Shizuku not supported (pre v11)");
			return;
		}
		if (!permissionGranted) {
			Shizuku.requestPermission(requestCode);
		}
	}
	
	/* ================= HELPER: SAVE SCRIPT WITH CLEAN LF (\n) ================= */
	
	/**
	 * Gunakan fungsi ini jika kamu ingin menyimpan String script .sh dari Java ke dalam File.
	 * Fungsi ini menjamin karakter \r (Windows CRLF) dibersihkan total.
	 */
	public boolean createCleanScriptFile(File targetFile, String scriptContent) {
		try {
			// Hapus karakter \r bawaan Windows
			String cleanContent = scriptContent.replace("\r\n", "\n").replace("\r", "\n");
			
			FileOutputStream fos = new FileOutputStream(targetFile);
			fos.write(cleanContent.getBytes(StandardCharsets.UTF_8));
			fos.flush();
			fos.close();
			
			// Berikan izin eksekusi
			targetFile.setExecutable(true, false);
			return true;
		} catch (Exception e) {
			toast("Failed to write script file: " + e.getMessage());
			return false;
		}
	}
	
	/* ================= DIR HANDLING (CD) ================= */
	
	private String handleCd(String command) {
		command = command.trim();
		if (command.equals("cd") || command.equals("cd ~")) {
			return homeDir;
		}
		if (!command.startsWith("cd ")) return null;
		
		String target = command.substring(3).trim();
		File dir;
		
		if (target.equals("..")) {
			dir = new File(currentDir).getParentFile();
			if (dir == null) return "ERROR";
		} else if (target.startsWith("~")) {
			dir = new File(homeDir, target.replace("~", ""));
		} else if (target.startsWith("/")) {
			dir = new File(target);
		} else {
			dir = new File(currentDir, target);
		}
		
		if (dir.exists() && dir.isDirectory()) {
			return dir.getAbsolutePath();
		}
		return "ERROR";
	}
	
	private String[] getEnvironmentVariables() {
		List<String> envList = new ArrayList<>();
		for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
			envList.add(entry.getKey() + "=" + entry.getValue());
		}
		envList.add("HOME=" + homeDir);
		envList.add("TMPDIR=" + tmpDir);
		envList.add("PWD=" + currentDir);
		return envList.toArray(new String[0]);
	}
	
	/* ================= SMART EXEC (INTERACTIVE & CRLF FIXED) ================= */
	
	public void exec(String command, Callback callback) {
		// Hapus karakter \r dari masukan perintah jika ada
		command = command.replace("\r", "").trim();
		
		if (isRunning) {
			sendInputToProcess(command, callback);
			return;
		}
		
		if (command.equals("inosh-setup-storage")) {
			requestStoragePermission();
			callback.onFinish("Exit code : 0 (Storage permission requested)");
			return;
		}
		
		if (command.equals("exit")) {
			exit();
			return;
		}
		
		callback.onOutput("$ " + command);
		
		String cd = handleCd(command);
		if (cd != null) {
			if (cd.equals("ERROR")) {
				callback.onOutput("sh: cd: No such file or directory");
				callback.onFinish("Exit code : 1");
				return;
			}
			currentDir = cd;
			callback.onOutput("Directory: " + currentDir);
			callback.onFinish("Exit code : 0");
			return;
		}
		
		// AUTO-FIX: Jika user menjalankan `sh script.sh`, otomatis bersihkan \r dari file tersebut sebelum run!
		if (command.startsWith("sh ") && command.endsWith(".sh")) {
			String scriptName = command.substring(3).trim();
			File scriptFile = scriptName.startsWith("/") ? new File(scriptName) : new File(currentDir, scriptName);
			if (scriptFile.exists()) {
				// Bersihkan file dari \r (Windows carriage returns) di latar belakang sebelum mengeksekusi
				cleanFileCrLf(scriptFile);
			}
		}
		
		final String finalCommand = command;
		isRunning = true;
		
		new Thread(() -> {
			BufferedReader reader = null;
			BufferedReader errorReader = null;
			boolean useShizuku = isShizukuReady();
			
			try {
				String[] envp = getEnvironmentVariables();
				
				if (useShizuku) {
					shizukuProcess = Shizuku.newProcess(new String[]{"sh", "-c", finalCommand}, envp, currentDir);
					reader = new BufferedReader(new InputStreamReader(shizukuProcess.getInputStream()));
					errorReader = new BufferedReader(new InputStreamReader(shizukuProcess.getErrorStream()));
					processInputWriter = new BufferedWriter(new OutputStreamWriter(shizukuProcess.getOutputStream()));
				} else {
					if (!hasStoragePermission()) {
						ui.post(() -> callback.onOutput("INFO: Using normal shell (Storage access has not been granted)"));
					}
					normalProcess = Runtime.getRuntime().exec(new String[]{"sh", "-c", finalCommand}, envp, new File(currentDir));
					reader = new BufferedReader(new InputStreamReader(normalProcess.getInputStream()));
					errorReader = new BufferedReader(new InputStreamReader(normalProcess.getErrorStream()));
					processInputWriter = new BufferedWriter(new OutputStreamWriter(normalProcess.getOutputStream()));
				}
				
				// Stream output secara real-time
				String line;
				while ((line = reader.readLine()) != null) {
					final String out = line;
					ui.post(() -> callback.onOutput(out));
				}
				
				String err;
				while ((err = errorReader.readLine()) != null) {
					final String outErr = err;
					ui.post(() -> callback.onOutput(outErr));
				}
				
				int code = useShizuku ? shizukuProcess.waitFor() : normalProcess.waitFor();
				ui.post(() -> callback.onFinish("Exit code : " + code));
				
			} catch (Exception e) {
				ui.post(() -> callback.onFinish("Process interrupted / Error : " + e.getMessage()));
			} finally {
				try { if (reader != null) reader.close(); } catch (Exception ignored) {}
				try { if (errorReader != null) errorReader.close(); } catch (Exception ignored) {}
				closeInputWriter();
				isRunning = false;
			}
		}).start();
	}
	
	/* ================= AUTO-FIX CRLF FILE ================= */
	
	private void cleanFileCrLf(File file) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(file), StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			br.close();
			
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
			fos.flush();
			fos.close();
		} catch (Exception ignored) {}
	}
	
	/* ================= SEND INPUT ================= */
	
	private void sendInputToProcess(String input, Callback callback) {
		if (processInputWriter != null) {
			new Thread(() -> {
				try {
					String cleanInput = input.replace("\r", "");
					callback.onOutput(cleanInput);
					processInputWriter.write(cleanInput + "\n");
					processInputWriter.flush();
				} catch (Exception e) {
					ui.post(() -> callback.onOutput("Failed to send input: " + e.getMessage()));
				}
			}).start();
		}
	}
	
	/* ================= CTRL + C ================= */
	
	public void sendCtrlC(Callback callback) {
		if (!isRunning) {
			callback.onOutput("\n^C");
			return;
		}
		
		callback.onOutput("^C (The process was terminated by the user)");
		destroy();
	}
	
	/* ================= CLEANUP & DESTRUCTORS ================= */
	
	private void closeInputWriter() {
		if (processInputWriter != null) {
			try { processInputWriter.close(); } catch (Exception ignored) {}
			processInputWriter = null;
		}
	}
	
	public void destroy() {
		closeInputWriter();
		if (shizukuProcess != null) {
			try { shizukuProcess.destroy(); } catch (Exception ignored) {}
			shizukuProcess = null;
		}
		if (normalProcess != null) {
			try { normalProcess.destroy(); } catch (Exception ignored) {}
			normalProcess = null;
		}
		isRunning = false;
	}
	
	public void exit() {
		destroy();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			activity.finishAndRemoveTask();
		} else {
			activity.finish();
			ActivityManager am = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
			if (am != null) {
				am.moveTaskToFront(activity.getTaskId(), 0);
			}
		}
	}
	
	private void toast(String msg) {
		ui.post(() -> Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show());
	}
	
	/* ================= STORAGE PERMISSION ================= */
	
	public boolean hasStoragePermission() {
		if (Build.VERSION.SDK_INT >= 30) {
			return Environment.isExternalStorageManager();
		}
		if (Build.VERSION.SDK_INT >= 23) {
			return activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
		}
		return true;
	}
	
	public void requestStoragePermission() {
		if (Build.VERSION.SDK_INT >= 30) {
			try {
				Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
				intent.setData(Uri.parse("package:" + activity.getPackageName()));
				activity.startActivity(intent);
			} catch (Exception e) {
				Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
				activity.startActivity(intent);
			}
		} else if (Build.VERSION.SDK_INT >= 23) {
			activity.requestPermissions(
				new String[]{
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE
				},
				1001
			);
		}
	}
}
