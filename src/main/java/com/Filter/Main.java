package main.java.com.Filter;

import main.java.com.Filter.Data.FileSrcData;
import main.java.com.Filter.database.DAO.DAO;
import main.java.com.Filter.database.Database;
import main.java.com.Filter.service.Tools;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class Main extends DAO {

    static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args){
        logger = LogManager.getLogger(Main.class.getName());
        logger.info("Program start ...");
        Database database = Database.getInstance();
        if(database.isDBEmpty()) {
            List<FileSrcData> fileSrcData = Tools.getDataFromFile(System.getProperty("user.dir") + File.separator + "postcodes.csv");
            database.createDatabaseIfNotExists();
            database.insertData(fileSrcData, true);
        }

        int expectedDrivingDistance;
        int cityPostalCode;

        //for(String arg: args){
        for(int i=0; i<args.length; i++){
            if(args[i].equalsIgnoreCase("-help")
                    || args[i].equalsIgnoreCase("-h")
                    || args[i].equalsIgnoreCase("-?")
                    || args[i].equalsIgnoreCase("?")){
                    showHelp();
            } else if(args[i].equalsIgnoreCase("-print")
                    || args[i].equalsIgnoreCase("print-info")){
                database.printInfo();
            } else if(args[i].equalsIgnoreCase("-print-data")){
                database.printData();
            } else if(args[i].equalsIgnoreCase("-print-all")){
                database.printAllData();
            } if(args[i].equalsIgnoreCase("-distance")){
                if(isLastPosition(args, i)) {
                    logger.info("Error: Number for expected driving distance missing.");
                    System.exit(1);
                } else {
                    if (NumberUtils.isDigits(args[i + 1])) {
                        expectedDrivingDistance = Integer.parseInt(args[i + 1]);
                        logger.info("Expected driving distance: " + expectedDrivingDistance);
                        if (expectedDrivingDistance < 0) {
                            logger.info("Error: Number must be greater than 0.");
                        } else {
                            float calcFactor = (float)
                                    expectedDrivingDistance > 0 && expectedDrivingDistance <= 5000
                                    ? 0.5f
                                    : expectedDrivingDistance > 5000 && expectedDrivingDistance <= 10000
                                    ? 1.0f
                                    : expectedDrivingDistance > 10000 && expectedDrivingDistance <= 20000
                                    ? 1.5f
                                    : 2.0f;
                            logger.info("Calculated factor: " + calcFactor);
                        }
                    }
                }
            } if(args[i].equalsIgnoreCase("-plz")){
                if(isLastPosition(args, i)) {
                    logger.info("Error: Number for city postal code.");
                    System.exit(1);
                } else {
                    if (NumberUtils.isDigits(args[i + 1])) {
                        cityPostalCode = Integer.parseInt(args[i + 1]);
                        logger.info("City postal code: " + cityPostalCode);
                        if (cityPostalCode < 0) {
                            logger.info("Error: Number must be greater than 0.");
                        } else {
                        }
                    }
                }
            }
        }
        logger.info("Bye");
        System.exit(0);
    }

    static boolean isLastPosition(String[] array, int position){
        return position == (array.length-1);
    }

    private static void showHelp (){
        logger.info("");
        logger.info("### This program is a test program for filtering location data and deciding some insurance parameter with those data ###");
        logger.info(" It will read a src file, store the information to a database and manage those data for parameter decisions.");
        logger.info("Syntax: [-help | -h | -? | ?] [-print | print-data | print-all] [-distance <number for distance>] [plz <number for plz>]");
        logger.info("\t Options");
        logger.info("\t\t -h/-help/-?/?         show this help and exit");
        logger.info("\t\t -print | print-info   print info of stored data");
        logger.info("\t\t -print-data           print data from database (with limit "+LIMIT_PRINT_DATA+")");
        logger.info("\t\t -print-all            print data from database");
        logger.info("\t\t -distance             input expected driving distance per year in km (as next parameter [0-9]{1-})");
        logger.info("\t\t -plz                  input postal city code (as next parameter [0-9]{1-})");
        logger.info("\nBye");
        System.exit(0);
    }
}
