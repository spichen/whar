package com.salah.geohash.util;

/**
 * Created by salah on 20/1/18.
 */

public class GeoUtils {
    public static boolean coordinatesValid(double latitude, double longitude) {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }
}
