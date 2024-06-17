package main.java.com.Filter.database.DAO;

import main.java.com.Filter.Data.FileSrcData;
import main.java.com.Filter.service.Tools;

import java.io.File;
import java.util.List;

public class DAO{

    protected static String fileNameSrc = System.getProperty("user.dir") + File.separator + "postcodes.csv";

    protected final static String tableName = "Bewegungsdaten";

    protected static int COUNT_DIGITS_AFTER_COMMA = 10;

    protected static int LIMIT_PRINT_DATA = 10;

    public static enum ParameterInput {
        EXPECTED_DISTANCE("expected driving distance = -distance"),
        CITY_POSTAL_CODE("city postal code = -plz");

        private final String value;

        private ParameterInput(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
