package main.java.com.Filter.database.DAO;

import lombok.Setter;

import java.io.File;

public class DAO{

    protected final static String FILE_NAME_SRC = System.getProperty("user.dir") + File.separator + "postcodes.csv";

    protected final static String TABLE_NAME = "Bewegungsdaten";

    protected final static int COUNT_DIGITS_AFTER_COMMA = 10;

    protected final static int LIMIT_PRINT_DATA = 10;

    protected boolean permitCreateDB = true;

    protected String path = null;

    @Setter
    protected static boolean showOtherinfo = false;

    public static enum ParameterInput {
        EXPECTED_DISTANCE("expected driving distance"),
        EXPECTED_DISTANCE_OPTION("-distance"),
        CITY_POSTAL_CODE("city postal code"),
        CITY_POSTAL_CODE_OPTION("-plz"),
        VEHICLE_FORM("vehicle form"),
        VEHICLE_FORM_OPTION("-form");

        private final String value;

        private ParameterInput(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static enum DataSrc {
        OK(""),
        IS_EMPTY("Database is empty."),
        DB_DOES_NOT_EXIST("Database is empty and program is not allowed to create data."),
        SRC_FILE_NOT_FOUND("Database is empty and src file not found"),
        SOMETHING_ELSE("Something else");

        private final String value;

        private DataSrc(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
