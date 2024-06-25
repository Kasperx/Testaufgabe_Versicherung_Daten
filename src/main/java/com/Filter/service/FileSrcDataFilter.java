package main.java.com.Filter.service;

import lombok.AllArgsConstructor;
import main.java.com.Filter.database.Database;

import java.util.List;

@AllArgsConstructor
public class FileSrcDataFilter {

    Database database;

    public float getFactorDrivingDistance(int expectedDrivingDistance){
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

    public int getDiscount(int form){
        return (float)
                form <= 0
                ? 0
                // = small size (car) = x/4
                : form == 1
                ? 50
                // = mittle size (car) = x/2
                : form == 2
                ? 25
                // = big size (car) = x/3
                : form == 3
                ? 12
                : 0;
    }

    public String getCityName(int cityPostalCode){
        return database != null
                ? database.getCityByPostalCode(cityPostalCode)
                : null;
    }

    public List<Integer> getAllCityPostalCodes(){
        return database.getAllCityPostalCodes();
    }

    public float calculateOption(int expectedDrivingDistance, int cityPostalCode, int vehicleForm){
        return 3f;
    }
}
