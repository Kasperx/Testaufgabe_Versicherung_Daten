package main.java.com.Filter;

import main.java.com.Filter.database.DAO.DAO;
import main.java.com.Filter.database.Database;
import main.java.com.Filter.service.Filter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends DAO {

    static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args){
        logger = LogManager.getLogger(Main.class.getName());
        logger.info("Program start ...");
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
                    || args[i].equalsIgnoreCase("?")){
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
            } if(args[i].equalsIgnoreCase("-distance")){
                foundSomething = true;
                if(isLastPosition(args, i)) {
                    logger.info("Found option for expected " + ParameterInput.EXPECTED_DISTANCE.toString() + ".");
                    logger.error("Error: Number for expected " + ParameterInput.EXPECTED_DISTANCE.toString() + " missing.");
                    showHelp();
                    System.exit(1);
                } else {
                    logger.info("Found option for " + ParameterInput.EXPECTED_DISTANCE.toString() + ".");
                    filterParameter(ParameterInput.EXPECTED_DISTANCE, args[i + 1], database);
                }
            } if(args[i].equalsIgnoreCase("-plz")){
                foundSomething = true;
                if(isLastPosition(args, i)) {
                    logger.info("Found option for " + ParameterInput.CITY_POSTAL_CODE.toString() + ".");
                    logger.error("Error: Number for " + ParameterInput.CITY_POSTAL_CODE.toString() + " missing.");
                    showHelp();
                    System.exit(1);
                } else {
                    logger.info("Found option for " + ParameterInput.CITY_POSTAL_CODE.toString() + ".");
                    filterParameter(ParameterInput.CITY_POSTAL_CODE, args[i + 1], database);
                }
            }
        }
        // Show info (invalid parameter) if input is not known
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
        logger.info("Bye");
    }

    static void filterParameter(ParameterInput input, String parameter, Database database){

        int expectedDrivingDistance;
        int cityPostalCode;
        // Normal check, but by calling from main method this cannot be null
        if (StringUtils.isNotBlank(parameter)) {
            if (NumberUtils.isDigits(parameter)) {
                switch (input) {
                    case EXPECTED_DISTANCE:
                        expectedDrivingDistance = Integer.parseInt(parameter);
                        logger.info("Input number for expected driving distance: " + expectedDrivingDistance);
                        if (expectedDrivingDistance < 0) {
                            logger.info("Error: Number must be greater than 0.");
                        } else {
                            logger.info("Calculated factor: " + Filter.getFactor(expectedDrivingDistance));
                        }
                        break;
                    case CITY_POSTAL_CODE:
                        cityPostalCode = Integer.parseInt(parameter);
                        logger.info("Input number for city postal code: " + cityPostalCode);
                        if (cityPostalCode < 0) {
                            logger.info("Error: Number must be greater than 0 and a valid postal code.");
                        } else {
                            Filter.getCityName(database, cityPostalCode);
                        }
                        break;
                }
            } else {
                logger.info("Error: Parameter is '" + parameter + "': must be a number.");
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
