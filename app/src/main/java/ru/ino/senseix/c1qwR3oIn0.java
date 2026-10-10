package ru.ino.senseix;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Base64;

import java.io.InputStream;

public class c1qwR3oIn0 {
	
	// ==========================================
	// 1. ELPIS METHOD (XOR ENCODER & DECODER)
	// ==========================================
	
	public static String elpis(String a) {
		char[] b = a.toCharArray();
		for (int i = 0; i < b.length; i++) {
			b[i] = (char)(b[i] ^ 7);
		}
		return new String(b);
	}
	
	public static String encodeElpis(String a) {
		// XOR bersifat simetris, logikanya sama persis dengan elpis
		char[] b = a.toCharArray();
		for (int i = 0; i < b.length; i++) {
			b[i] = (char)(b[i] ^ 7);
		}
		return new String(b);
	}
	
	// ==========================================
	// 2. ASSET MANAGER METHOD
	// ==========================================
	
	public static String AdaWong(Context a, String b) {
		try {
			AssetManager c = a.getAssets();
			InputStream d = c.open(b);
			
			byte[] e = new byte[d.available()];
			int f = d.read(e);
			
			d.close();
			
			if (f <= 0) return "";
			
			return new String(e).trim();
			
		} catch (Exception x) {
			return "";
		}
	}
	
	// ==========================================
	// 3. LEON S. KENNEDY DECODER METHODS
	// ==========================================
	
	public static String LeonSKenedey(String a) {
		String b = m1(a);
		String c = m2(b);
		String d = m3(c);
		String e = m4(d);
		return m5(e);
	}
	
	private static String m1(String a) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i + 2 <= a.length(); i++) {
			char c1 = a.charAt(i);
			char c2 = a.charAt(i + 1);
			
			if (c1 == '2' && c2 == '1') {
				b.append('.');
				i++;
			}
			else if (c1 == '6' && c2 == '7') {
				b.append('-');
				i++;
			}
		}
		return b.toString();
	}
	
	private static String m2(String a) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < a.length(); i++) {
			char c = a.charAt(i);
			if (c == '.') b.append('0');
			else if (c == '-') b.append('1');
		}
		return b.toString();
	}
	
	private static String m3(String a) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i + 8 <= a.length(); i += 8) {
			String c = a.substring(i, i + 8);
			int d = Integer.parseInt(c, 2);
			String e = Integer.toHexString(d);
			if (e.length() == 1) b.append('0');
			b.append(e);
		}
		return b.toString();
	}
	
	private static String m4(String a) {
		int b = a.length();
		byte[] c = new byte[b / 2];
		int d = 0;
		for (int i = 0; i + 1 < b; i += 2) {
			int e = Character.digit(a.charAt(i), 16);
			int f = Character.digit(a.charAt(i + 1), 16);
			c[d++] = (byte)((e << 4) + f);
		}
		return new String(c);
	}
	
	private static String m5(String a) {
		try {
			byte[] b = Base64.decode(a, Base64.DEFAULT);
			return new String(b);
		} catch (Exception x) {
			return "";
		}
	}

	// ==========================================
	// 4. LEON S. KENNEDY ENCODER METHODS
	// ==========================================

	public static String EncodeLeon(String a) {
		String b = em5(a);
		String c = em4(b);
		String d = em3(c);
		String e = em2(d);
		return em1(e);
	}

	private static String em5(String a) {
		try {
			return Base64.encodeToString(a.getBytes(), Base64.NO_WRAP);
		} catch (Exception x) {
			return "";
		}
	}

	private static String em4(String a) {
		StringBuilder b = new StringBuilder();
		byte[] bytes = a.getBytes();
		for (byte c : bytes) {
			String hex = Integer.toHexString(c & 0xFF);
			if (hex.length() == 1) b.append('0');
			b.append(hex);
		}
		return b.toString();
	}

	private static String em3(String a) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < a.length(); i += 2) {
			String hexPair = a.substring(i, i + 2);
			int val = Integer.parseInt(hexPair, 16);
			String bin = Integer.toBinaryString(val);
			while (bin.length() < 8) {
				bin = "0" + bin;
			}
			b.append(bin);
		}
		return b.toString();
	}

	private static String em2(String a) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < a.length(); i++) {
			char c = a.charAt(i);
			if (c == '0') b.append('.');
			else if (c == '1') b.append('-');
		}
		return b.toString();
	}

	private static String em1(String a) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < a.length(); i++) {
			char c = a.charAt(i);
			if (c == '.') b.append("21");
			else if (c == '-') b.append("67");
		}
		return b.toString();
	}
}
