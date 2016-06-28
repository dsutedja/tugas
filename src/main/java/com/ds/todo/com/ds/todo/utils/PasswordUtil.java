package com.ds.todo.com.ds.todo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dsutedja on 6/21/16.
 */
public class PasswordUtil {
    private static final String HEX_VALUES = "0123456789abcdef";
    public static final String BAD_PASSWORD = "**invalid**";

    public static String md5(String password, String salt) {
        String ret;
        byte[] raw;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bb = (password + salt).getBytes();
            digest.update(bb, 0, bb.length);
            raw = digest.digest();
            ret = hexify(raw);
        } catch (SecurityException | NoSuchAlgorithmException e) {
            ret = BAD_PASSWORD;
            e.printStackTrace();
        }
        return ret;
    }

    public static String hexify(byte[] raw) {
        StringBuilder ret = new StringBuilder();
        String hex = HEX_VALUES;
        if (raw != null) {
            for (int i = 0; i < raw.length; i++) {
                int h2 = (raw[i] & 0x0F);
                int h1 = (raw[i] >> 4) & 0x0F;
                ret.append(hex.charAt(h1));
                ret.append(hex.charAt(h2));
            }
        }

        return ret.toString();
    }
}
