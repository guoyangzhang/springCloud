package com.zhang.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(String str) {

        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(String str) {

        return str != null && str.trim().length() > 0;
    }

    public static String nullToStrTrim(String str) {

        if (str == null) {
            return "";
        }

        return str.trim();
    }

    public static boolean checkString(String str, String regex) {

        return str.matches(regex);
    }

    public static int getLength(String str) {

        if (str == null) {
            str = "";
        }
        return str.replaceAll("[^\\x00-\\xff]", "**").length();
    }

    public static boolean checkMD5(String md5) {

        return checkString(md5, "[0-9A-Fa-f]{32}");
    }

    public static String byte2hex(byte[] b) {

        String str = "";
        String stmp = "";

        int length = b.length;

        for (int n = 0; n < length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                str += "0";
            }
            str += stmp;
        }

        return str.toLowerCase();
    }

    public static boolean checkEmail(String email) {

        email = nullToStrTrim(email);
        if (isEmpty(email)) {
            return false;
        }
        if (email.length() < 6 || email.length() > 50) {
            return false;
        }

        String regex = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
        regex = "^[a-zA-Z0-9_.-]+@(([a-zA-Z0-9-])+.)+(?:com|cn|net)$";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            return false;
        } else {
            return true;
        }

//		return email.matches(regex);
    }

    public static boolean checkPhone(String phone) {

        return checkString(phone, "1[0-9]{10}");
    }

    public static boolean checkMobile(String mobile) {

        return checkString(mobile, "1[3,4,5,7,8][0-9]{9}");
    }

    /**
     * 获得随机验证码
     *
     * @param numberFlag 是否是数字
     * @param length     长度
     * @return 验证码
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }


    /**
     * 校验手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * uuid 32
     *
     * @return
     */
    public static String uuid32len() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * uuid 36
     *
     * @return
     */
    public static String uuid36len() {
        return UUID.randomUUID().toString();
    }
}
