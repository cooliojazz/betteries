package com.up.betteries.util;

/**
 *
 * @author Ricky Talbot
 */
public class GuiUtils {

    private static final String[] abvs = {"", "k", "M", "G", "T", "P", "E", "Z", "Y", "?"};
    
    public static String abbreviate(int i) {
        int exp = Math.max(Math.min((int)(Math.log(i) / Math.log(1000)), abvs.length - 1), 0);
        return String.format("%.2f", i / Math.pow(1000, exp)) + abvs[exp];
    }
    public static String abbreviate(long i) {
        int exp = Math.max(Math.min((int)(Math.log(i) / Math.log(1000)), abvs.length - 1), 0);
        return String.format("%.2f", i / Math.pow(1000, exp)) + abvs[exp];
    }
    
    public static String suffixTime(long t) {
        long s = t /20;
        if (s < 10) return t + "t";
        long m = s / 60;
        if (m < 2) return s + "s";
        long h = m / 60;
        if (h < 2) return m + "m";
        long d = h / 24;
        if (h < 2) return d + "d";
        return h + "h";
    }
}
