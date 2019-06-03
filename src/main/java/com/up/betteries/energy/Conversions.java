package com.up.betteries.energy;

/**
 *
 * @author Ricky Talbot
 */
public class Conversions {
    public static double IC2_RATIO = 4;
    public static int BUILDCRAFT_RATIO = 50;
    
    public static double feToEu(int fe) {
        return fe / IC2_RATIO;
    }
    
    public static int euToFe(double eu) {
        return (int)(eu * IC2_RATIO);
    }
    
    public static int feToMj(int fe) {
        return fe / BUILDCRAFT_RATIO;
    }
    
    public static int mjToFe(int mj) {
        return mj * BUILDCRAFT_RATIO;
    }
}
