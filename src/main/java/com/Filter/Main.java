package main.java.com.Filter;

import main.java.com.Filter.database.DAO.DAO;
import main.java.com.Filter.database.Database;
import main.java.com.Filter.service.FileSrcDataFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends DAO {

    static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args){
        logger = LogManager.getLogger(Main.class.getName());
        for(String temp: args) {
            if (temp.equalsIgnoreCase("-v")) {
                new DAO().setShowOtherinfo(true);
            }
        }
        if(showOtherinfo) {
            logger.info("Program start ...");
        }
        Database database = Database.getInstance();
        if(database.initDataSrc(database) != DataSrc.OK){
            showHelp(false);
            System.exit(1);
        }
        boolean foundSomething = false;
        for(int i=0; i<args.length; i++){
            if(args[i].equalsIgnoreCase("-help")
                    || args[i].equalsIgnoreCase("-h")
                    || args[i].equalsIgnoreCase("-?")
                    || args[i].equalsIgnoreCase("?")) {
                foundSomething = true;
                showHelp(true);
                System.exit(0);
            } else if(args[i].equalsIgnoreCase("-print")
                    || args[i].equalsIgnoreCase("print-info")){
                foundSomething = true;
                database.printInfo();
                System.exit(0);
            } else if(args[i].equalsIgnoreCase("-print-data")){
                foundSomething = true;
                database.printData();
                System.exit(0);
            } else if(args[i].equalsIgnoreCase("-print-all")){
                foundSomething = true;
                database.printAllData();
                System.exit(0);
            } if(args[i].equalsIgnoreCase(ParameterInput.EXPECTED_DISTANCE_OPTION.toString())){
                foundSomething = true;
                filterParameter(ParameterInput.EXPECTED_DISTANCE, database, args, i);
            } if(args[i].equalsIgnoreCase(ParameterInput.CITY_POSTAL_CODE_OPTION.toString())){
                foundSomething = true;
                filterParameter(ParameterInput.CITY_POSTAL_CODE, database, args, i);
            }
        }
        // Show info (invalid parameter) if input is not known
        showInfoForParameter(args, foundSomething);
        if(showOtherinfo) {
            logger.info("Bye");
        }
    }

    static void showInfoForParameter(String[] args, boolean foundSomething){
        if(! foundSomething && args.length > 0) {
            if(args.length == 1) {
                logger.info("Parameter invalid: '" + args[0] + "'");
            } else {
                logger.info("Parameter(s) invalid:");
                for(String temp: args){
                    logger.info("'" + temp + "'");
                }
            }
        }
    }

    static void filterParameter(ParameterInput input, Database database, String[] args, int position){
        if(showOtherinfo) {
            logger.info("Found option for " + input.toString() + " = '" +
                    (input == ParameterInput.CITY_POSTAL_CODE
                            ? ParameterInput.CITY_POSTAL_CODE_OPTION.toString()
                            : ParameterInput.EXPECTED_DISTANCE_OPTION.toString())
                    + "'.");
        }
        if(isLastPosition(args, position)) {
            logger.error("Error: Number for " + input.toString() + " missing.");
            showHelp();
            System.exit(1);
        } else {
            //filterParameter(ParameterInput.EXPECTED_DISTANCE, args[position + 1], database);
            int expectedDrivingDistance;
            int cityPostalCode;
            String parameter = args[position + 1];
            // Normal check, but by calling from main method this cannot be null
            if (StringUtils.isNotBlank(parameter)) {
                    switch (input) {
                        case EXPECTED_DISTANCE:
                            if (NumberUtils.isDigits(parameter)) {
                                expectedDrivingDistance = Integer.parseInt(parameter);
                                if(showOtherinfo) {
                                    logger.info("Input number for " + ParameterInput.EXPECTED_DISTANCE.toString() + ": " + expectedDrivingDistance);
                                }
                                if (expectedDrivingDistance < 0) {
                                    logger.info("Error: Number must be greater than 0.");
                                } else {
                                    logger.info("Calculated factor: " + FileSrcDataFilter.getFactor(expectedDrivingDistance));
                                }
                            } else {
                                logger.info("Error: Parameter is '" + parameter + "': must be a number.");
                            }
                            break;
                        case CITY_POSTAL_CODE:
                            if (NumberUtils.isDigits(parameter)) {
                                cityPostalCode = Integer.parseInt(parameter);
                                if(showOtherinfo) {
                                    logger.info("Input number for " + ParameterInput.CITY_POSTAL_CODE.toString() + ": " + cityPostalCode);
                                }
                                if (cityPostalCode <= 0) {
                                    logger.info("Error: Number must be greater than 0 and a valid " + ParameterInput.CITY_POSTAL_CODE.toString() + ".");
                                    logger.info("Known " + ParameterInput.CITY_POSTAL_CODE.toString() + "s: " + FileSrcDataFilter.getAllCityPostalCodes(database));
                                } else {
                                    String cityName = FileSrcDataFilter.getCityName(database, cityPostalCode);
                                    if(cityName == null) {
                                        logger.info("No cityname found with that code");
                                    } else {
                                        logger.info("Cityname with that code: '" + cityName + "'");
                                    }
                                }
                            } else {
                                logger.info("Error: Parameter is '" + parameter + "': must be a number.");
                                logger.info("Known " + ParameterInput.CITY_POSTAL_CODE.toString() + "s: " + FileSrcDataFilter.getAllCityPostalCodes(database));
                            }
                            break;
                    }
            }
        }
    }

    static boolean isLastPosition(String[] array, int position){
        return position == (array.length-1);
    }

    private static void showHelp (){
        showHelp(false);
    }

    private static void showHelp (boolean showDescription){
        if(showDescription) {
            //logger.info("");
            logger.info("### This program is a test program for filtering location data and deciding some insurance parameter with those data ###");
            logger.info(" It will read a src file, store the information to a database and manage those data for parameter decisions.");
        } else {
            logger.info("");
        }
        logger.info("Syntax: [-help | -h | -? | ?] [-print | print-data | print-all] [-distance <number for distance in km>] [plz <number for plz>]");
        logger.info("\t Options");
        logger.info("\t\t -h/-help/-?/?         show this help and exit");
        logger.info("\t\t -print | print-info   print info of stored data");
        logger.info("\t\t -print-data           print data from database (with limit "+LIMIT_PRINT_DATA+")");
        logger.info("\t\t -print-all            print data from database");
        logger.info("\t\t -distance             input expected driving distance per year in km (as next parameter [0-9]{1-})");
        logger.info("\t\t -plz                  input postal city code (as next parameter [0-9]{1-})");
        logger.info("\nBye");
    }
}
