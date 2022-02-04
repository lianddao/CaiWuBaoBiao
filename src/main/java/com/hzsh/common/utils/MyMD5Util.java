package com.hzsh.common.utils;

import java.security.MessageDigest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyMD5Util {
	// 盐，用于混交md5
	private static final String slat = "&%5123***&&%%$$#@";

	public static String encrypt(String dataStr) {
		try {
			dataStr = dataStr + slat;
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(dataStr.getBytes("UTF8"));
			byte s[] = m.digest();
			String result = "";
			for (int i = 0; i < s.length; i++) {
				result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String encode(String pass) {
		BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
		String hashPass = bcryptPasswordEncoder.encode(pass);
		return hashPass;
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * //System.out.println(encrypt("user123456"));
	 * //System.out.println(encrypt("admin123456"));
	 * //System.out.println(encrypt("1"));
	 * 
	 * 
	 * String pass = "123456"; BCryptPasswordEncoder bcryptPasswordEncoder = new
	 * BCryptPasswordEncoder(); String hashPass =
	 * bcryptPasswordEncoder.encode(pass); System.out.println("222="+hashPass);
	 * 
	 * boolean f = bcryptPasswordEncoder.matches("123456",
	 * "$2a$10$0ZR14AKXmNbhlWT7j4ATB.y3HMBKUQ/Oop0DH/n0GG.pbwzy7.eBW");
	 * System.out.println("111="+f); }
	 * 
	 */

}
