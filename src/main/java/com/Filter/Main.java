package main.java.com.Filter;

import main.java.com.Filter.database.DAO.DAO;
import main.java.com.Filter.database.Database;
import main.java.com.Filter.service.FileSrcDataFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Main extends DAO {

    static Logger logger = LogManager.getLogger(Main.class.getName());

    static HashMap<ParameterInput, Boolean> foundInputParameter = new HashMap<>(){{
            put(ParameterInput.EXPECTED_DISTANCE, false);
            put(ParameterInput.CITY_POSTAL_CODE, false);
            put(ParameterInput.VEHICLE_FORM, false);
    }};

    static FileSrcDataFilter fileSrcDataFilter;

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
        fileSrcDataFilter = new FileSrcDataFilter(database);
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
                filterParameter(ParameterInput.EXPECTED_DISTANCE, args, i);
            } if(args[i].equalsIgnoreCase(ParameterInput.CITY_POSTAL_CODE_OPTION.toString())){
                foundSomething = true;
                filterParameter(ParameterInput.CITY_POSTAL_CODE, args, i);
            } if(args[i].equalsIgnoreCase(ParameterInput.VEHICLE_FORM_OPTION.toString())){
                foundSomething = true;
                filterParameter(ParameterInput.VEHICLE_FORM, args, i);
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

    static void filterParameter(ParameterInput input, String[] args, int position){
        if(showOtherinfo) {
            logger.info("Found option for " + input.toString() + " = '" +
                    (input == ParameterInput.CITY_POSTAL_CODE
                            ? ParameterInput.CITY_POSTAL_CODE_OPTION.toString()
                            : input == ParameterInput.EXPECTED_DISTANCE
                                    ? ParameterInput.EXPECTED_DISTANCE_OPTION.toString()
                                    : ParameterInput.VEHICLE_FORM_OPTION.toString())
                    + "'.");
        }
        int expectedDrivingDistance = -1;
        int cityPostalCode = -1;
        int vehicleForm = -1;
        if(isLastPosition(args, position)) {
            logger.error("Error: Number for " + input.toString() + " missing.");
            showHelp();
            System.exit(1);
        } else {
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
                                    logger.info("Calculated factor for " + ParameterInput.EXPECTED_DISTANCE + ": " + fileSrcDataFilter.getFactorDrivingDistance(expectedDrivingDistance));
                                    foundInputParameter.put(ParameterInput.EXPECTED_DISTANCE, true);
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
                                    logger.info("Known " + ParameterInput.CITY_POSTAL_CODE.toString() + "s: " + fileSrcDataFilter.getAllCityPostalCodes());
                                } else {
                                    String cityName = fileSrcDataFilter.getCityName(cityPostalCode);
                                    if(cityName == null) {
                                        logger.info("No cityname found with that code");
                                    } else {
                                        logger.info("Cityname with that code: '" + cityName + "'");
                                        foundInputParameter.put(ParameterInput.CITY_POSTAL_CODE, true);
                                    }
                                }
                            } else {
                                logger.info("Error: Parameter is '" + parameter + "': must be a number.");
                                logger.info("Known " + ParameterInput.CITY_POSTAL_CODE.toString() + "s: " + fileSrcDataFilter.getAllCityPostalCodes());
                            }
                            break;
                        case VEHICLE_FORM:
                            if (NumberUtils.isDigits(parameter)) {
                                vehicleForm = Integer.parseInt(parameter);
                                if(showOtherinfo) {
                                    logger.info("Input number for " + ParameterInput.VEHICLE_FORM_OPTION.toString() + ": " + vehicleForm);
                                }
                                if (vehicleForm <= 0) {
                                    logger.info("Error: Number must be greater than 0 and smaller than 5.");
                                } else {
                                    logger.info("Calculated discount for " + ParameterInput.VEHICLE_FORM + ": " + fileSrcDataFilter.getDiscount(vehicleForm) + "%");
                                    foundInputParameter.put(ParameterInput.VEHICLE_FORM, true);
                                }
                            } else {
                                logger.info("Error: Parameter is '" + parameter + "': must be a number.");
                            }
                            break;
                    }
            }
        }
        /*
        if(foundInputParameter.get(ParameterInput.EXPECTED_DISTANCE)
                && foundInputParameter.get(ParameterInput.CITY_POSTAL_CODE)){
            logger.info("You will have to pay " + fileSrcDataFilter.calculateOption(expectedDrivingDistance, cityPostalCode) + " € / month.");
        }
         */
        if(foundInputParameter.size() >= 3
                &&foundInputParameter.get(ParameterInput.EXPECTED_DISTANCE)
                && foundInputParameter.get(ParameterInput.CITY_POSTAL_CODE)
                && foundInputParameter.get(ParameterInput.VEHICLE_FORM)){
            logger.info("You get the chance to save " + fileSrcDataFilter.calculateOption(expectedDrivingDistance, cityPostalCode, vehicleForm) + " % / month of your payment.");
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
        logger.info("Syntax: [-help | -h | -? | ?] [-print | print-data | print-all] "
                + "[" + ParameterInput.EXPECTED_DISTANCE_OPTION.toString()  + " <number for " + ParameterInput.EXPECTED_DISTANCE.toString() +   " in km>] "
                + "[" + ParameterInput.CITY_POSTAL_CODE_OPTION.toString()   + " <number for " + ParameterInput.CITY_POSTAL_CODE.toString() +    ">] "
                + "[" + ParameterInput.VEHICLE_FORM_OPTION.toString()       + " <number for " + ParameterInput.VEHICLE_FORM.toString() +        " selection>]");
        logger.info("\t Options");
        logger.info("\t\t -h/-help/-?/?         show this help and exit");
        logger.info("\t\t -print | print-info   print info of stored data");
        logger.info("\t\t -print-data           print data from database (with limit "+LIMIT_PRINT_DATA+")");
        logger.info("\t\t -print-all            print data from database");
        logger.info("\t\t -distance             input expected driving distance per year in km (as next parameter [0-9]{1-})");
        logger.info("\t\t -plz                  input postal city code (as next parameter [0-9]{1-})");
        logger.info("\t\t -form                 input vehicle form (as next parameter [1 / 2 / 3 / 4])");
        logger.info("\t\t                       1 = small, 2 = combi, 3 = cabrio, 4 = tank)");
        logger.info("\nBye");
    }
}
