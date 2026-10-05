package ru.ino.senseix;

import android.content.Context;
import android.net.Uri;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ArchiveExtractor {
	
	private static final int BUFFER_SIZE = 8192;
	
	public interface ExtractCallback {
		void onProgress(String currentFile, int progressPercent, long extractedBytes);
		void onSuccess(File destinationDir);
		void onError(Exception e);
	}
	
	// ==========================================
	// 1. EKSTRAKSI DARI FILE PATH (Background Thread)
	// ==========================================
	
	public static void extract(File archiveFile, File outputDir, ExtractCallback callback) {
		new Thread(() -> {
			try {
				long totalSize = archiveFile.length();
				String name = archiveFile.getName().toLowerCase();
				
				if (name.endsWith(".tar.gz") || name.endsWith(".tgz")) {
					extractTarGz(new FileInputStream(archiveFile), totalSize, outputDir, callback);
				} else if (name.endsWith(".tar")) {
					extractTar(new FileInputStream(archiveFile), totalSize, outputDir, callback);
				} else if (name.endsWith(".gz")) {
					extractGzOnly(archiveFile, outputDir, callback);
				} else if (name.endsWith(".7z")) {
					extract7zNative(archiveFile, outputDir, callback);
				} else {
					extractZip(new FileInputStream(archiveFile), totalSize, outputDir, callback);
				}
			} catch (Exception e) {
				if (callback != null) callback.onError(e);
			}
		}).start();
	}
	
	// ==========================================
	// 2. EKSTRAKSI DARI ASSETS
	// ==========================================
	
	public static void extractFromAssets(Context context, String assetPath, File outputDir, ExtractCallback callback) {
		new Thread(() -> {
			try (InputStream is = context.getAssets().open(assetPath)) {
				long totalSize = 0;
				try { totalSize = is.available(); } catch (Exception ignored) {}
				
				String name = assetPath.toLowerCase();
				if (name.endsWith(".tar.gz") || name.endsWith(".tgz")) {
					extractTarGz(is, totalSize, outputDir, callback);
				} else if (name.endsWith(".tar")) {
					extractTar(is, totalSize, outputDir, callback);
				} else {
					extractZip(is, totalSize, outputDir, callback);
				}
			} catch (Exception e) {
				if (callback != null) callback.onError(e);
			}
		}).start();
	}
	
	// ==========================================
	// 3. EKSTRAKSI DARI URI (SAF / Storage Access Framework)
	// ==========================================
	
	public static void extractFromUri(Context context, Uri uri, File outputDir, ExtractCallback callback) {
		new Thread(() -> {
			try {
				long totalSize = 0;
				try (InputStream sizeStream = context.getContentResolver().openInputStream(uri)) {
					if (sizeStream != null) {
						totalSize = sizeStream.available();
					}
				} catch (Exception ignored) {}
				
				try (InputStream is = context.getContentResolver().openInputStream(uri)) {
					if (is == null) throw new IOException("Gagal membuka InputStream dari Uri");
					extractZip(is, totalSize, outputDir, callback);
				}
			} catch (Exception e) {
				if (callback != null) callback.onError(e);
			}
		}).start();
	}
	
	// ==========================================
	// ENGINE 1: ZIP / JAR / APK
	// ==========================================
	
	private static void extractZip(InputStream inputStream, long totalSize, File outputDir, ExtractCallback callback) throws IOException {
		try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(inputStream))) {
			ZipEntry entry;
			byte[] buffer = new byte[BUFFER_SIZE];
			long totalBytesRead = 0;
			
			while ((entry = zis.getNextEntry()) != null) {
				File targetFile = buildSafeFile(outputDir, entry.getName());
				if (entry.isDirectory()) {
					targetFile.mkdirs();
				} else {
					targetFile.getParentFile().mkdirs();
					try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile))) {
						int count;
						while ((count = zis.read(buffer)) != -1) {
							bos.write(buffer, 0, count);
							totalBytesRead += count;
						}
					}
					
					// Panggil callback HANYA SEKALI setelah file selesai ditulis
					int percent = calculatePercent(totalBytesRead, totalSize);
					if (callback != null) callback.onProgress(entry.getName(), percent, totalBytesRead);
				}
				zis.closeEntry();
			}
		}
		if (callback != null) callback.onSuccess(outputDir);
	}
	
	// ==========================================
	// ENGINE 2: TAR & TAR.GZ
	// ==========================================
	
	private static void extractTarGz(InputStream inputStream, long totalSize, File outputDir, ExtractCallback callback) throws IOException {
		GZIPInputStream gzipIn = new GZIPInputStream(new BufferedInputStream(inputStream));
		extractTar(gzipIn, totalSize, outputDir, callback);
	}
	
	private static void extractTar(InputStream inputStream, long totalSize, File outputDir, ExtractCallback callback) throws IOException {
		byte[] header = new byte[512];
		byte[] buffer = new byte[BUFFER_SIZE];
		long totalBytesRead = 0;
		
		InputStream is = new BufferedInputStream(inputStream);
		
		while (is.read(header, 0, 512) == 512) {
			if (header[0] == 0) break;
			
			String fileName = new String(header, 0, 100).trim();
			if (fileName.isEmpty()) continue;
			
			String sizeStr = new String(header, 124, 12).trim();
			long fileSize = parseOctal(sizeStr);
			
			byte linkFlag = header[156];
			File targetFile = buildSafeFile(outputDir, fileName);
			
			if (linkFlag == '5' || fileName.endsWith("/")) {
				targetFile.mkdirs();
			} else {
				targetFile.getParentFile().mkdirs();
				try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile))) {
					long bytesToRead = fileSize;
					while (bytesToRead > 0) {
						int readLen = (int) Math.min(buffer.length, bytesToRead);
						int count = is.read(buffer, 0, readLen);
						if (count == -1) break;
						bos.write(buffer, 0, count);
						bytesToRead -= count;
						totalBytesRead += count;
					}
				}
				
				long padding = (512 - (fileSize % 512)) % 512;
				if (padding > 0) {
					is.skip(padding);
				}
				
				// Panggil callback HANYA SEKALI per file selesai
				int percent = calculatePercent(totalBytesRead, totalSize);
				if (callback != null) callback.onProgress(fileName, percent, totalBytesRead);
			}
		}
		if (callback != null) callback.onSuccess(outputDir);
	}
	
	// ==========================================
	// ENGINE 3: GZ SINGLE FILE
	// ==========================================
	
	private static void extractGzOnly(File archiveFile, File outputDir, ExtractCallback callback) throws IOException {
		String outFileName = archiveFile.getName().substring(0, archiveFile.getName().length() - 3);
		File targetFile = buildSafeFile(outputDir, outFileName);
		long totalSize = archiveFile.length();
		
		try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(archiveFile));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile))) {
			
			byte[] buffer = new byte[BUFFER_SIZE];
			int count;
			long totalBytesRead = 0;
			while ((count = gzis.read(buffer)) != -1) {
				bos.write(buffer, 0, count);
				totalBytesRead += count;
			}
			
			int percent = calculatePercent(totalBytesRead, totalSize);
			if (callback != null) callback.onProgress(outFileName, percent, totalBytesRead);
		}
		if (callback != null) callback.onSuccess(outputDir);
	}
	
	// ==========================================
	// ENGINE 4: 7Z (Fallback System Exec)
	// ==========================================
	
	private static void extract7zNative(File archiveFile, File outputDir, ExtractCallback callback) throws Exception {
		try {
			Process process = new ProcessBuilder("7z", "x", archiveFile.getAbsolutePath(), "-o" + outputDir.getAbsolutePath(), "-y")
			.redirectErrorStream(true)
			.start();
			
			int exitCode = process.waitFor();
			if (exitCode == 0) {
				if (callback != null) {
					callback.onProgress(archiveFile.getName(), 100, archiveFile.length());
					callback.onSuccess(outputDir);
				}
			} else {
				throw new IOException("7z Process exited with code: " + exitCode);
			}
		} catch (Exception e) {
			throw new UnsupportedOperationException("Ekstraksi .7z tanpa library butuh biner 7z sistem. Gunakan format .zip/.tar/.gz untuk dukungan murni SDK.");
		}
	}
	
	// ==========================================
	// HELPER UTILS
	// ==========================================
	
	private static int calculatePercent(long current, long total) {
		if (total <= 0) return 0;
		int percent = (int) ((current * 100) / total);
		return Math.min(percent, 100);
	}
	
	private static long parseOctal(String octalStr) {
		try {
			return Long.parseLong(octalStr.trim(), 8);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	private static File buildSafeFile(File destinationDir, String entryName) throws IOException {
		File destFile = new File(destinationDir, entryName);
		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();
		
		if (!destFilePath.startsWith(destDirPath + File.separator) && !destFilePath.equals(destDirPath)) {
			throw new IOException("Zip Slip detected: " + entryName);
		}
		return destFile;
	}
}
