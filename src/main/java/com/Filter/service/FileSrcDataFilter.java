package main.java.com.Filter.service;

import main.java.com.Filter.database.Database;

import java.util.List;

public class FileSrcDataFilter {

    public static float getFactor(int expectedDrivingDistance){
        return (float)
                expectedDrivingDistance <= 0
                ? 0f
                : expectedDrivingDistance > 0 && expectedDrivingDistance <= 5000
                ? 0.5f
                : expectedDrivingDistance > 5000 && expectedDrivingDistance <= 10000
                ? 1.0f
                : expectedDrivingDistance > 10000 && expectedDrivingDistance <= 20000
                ? 1.5f
                : 2.0f;
    }

    public static String getCityName(Database database, int cityPostalCode){
        return database != null
                ? database.getCityByPostalCode(cityPostalCode)
                : null;
    }

    public static List<Integer> getAllCityPostalCodes(Database database){
        return database.getAllCityPostalCodes();
    }
}
