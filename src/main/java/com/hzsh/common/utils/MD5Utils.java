package com.hzsh.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *  MD5加密
 **/
public class MD5Utils {

    public static String md5(String src) {//MD5加码 生成32位md5码
        return DigestUtils.md5Hex(src).toUpperCase();
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFormPass(String inputPass) {//加密
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        System.out.println(str);
        return md5(str);
    }

    public static String formPassToDBPass(String formPass, String salt) {//加密
        String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass, String saltDB) {//加密
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    /**
     * MD5验证方法
     *
     * text明文
     * key密钥
     * md5密文
     */
    // 根据传入的密钥进行验证
    public static boolean verify(String text, String md5) throws Exception {
        String md5str = md5(text);
        if (md5str.equalsIgnoreCase(md5)) {
            System.out.println("MD5验证通过");
            return true;
        }
        return false;
    }


	/*
	 * //编写主类进行测试 public static void main(String args[]) { String text =
	 * "Hzshdh@2020"; System.out.println("原始：" + text);
	 * System.out.println("加密后："+(MD5Utils.md5(text))); String
	 * body="您好，您正在尝试修改工业互联网平台用户账号密码。\r\n"+"验证码：（注意：验证码在 30 分钟内有效，超时后请重新获取）\r\n"
	 * +"如非本人操作，请忽略此邮件。"+"此邮件为系统自动发送，请勿回复。\r\n";
	 * 
	 * System.out.println(body);
	 * 
	 * try { System.out.println("是否一致：" + verify(text,MD5Utils.md5(text))); } catch
	 * (Exception e) { e.printStackTrace(); }
	 * 
	 * }
	 */



}
