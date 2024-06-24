package main.java.com.Filter.database;

import java.util.*;

import lombok.Getter;
import lombok.Setter;
import main.java.com.Filter.Data.FileSrcData;
import main.java.com.Filter.database.DAO.DAO;
import main.java.com.Filter.database.Interfaces.IDatabase;
import main.java.com.Filter.service.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
@Setter
public abstract class Database
        extends DAO
        implements IDatabase{

    static Logger logger = LogManager.getLogger(Database.class.getName());

    static DatabaseType databaseType;

    public enum DatabaseType{
        sqlite("sqlite"),
        file("file");
        String value = null;
        DatabaseType(String value)
        {
        	this.value = value;
        }
        public static DatabaseType getValue()
        {
        	return sqlite;
        }
    };

    /**
     * get instance
     * @return
     */
    public static Database getInstance()
    {
        return getInstance(DatabaseType.getValue());
    }

    /**
     * get instance
     * @param source
     * @return
     */
    public static Database getInstance(DatabaseType source){
        return switch (source) {
            case file -> new DatabaseFile();
            case sqlite -> new DatabaseSqlite();
            default -> {
                logger.info("Not supported: source '" + source.value + "'. Using '" + DatabaseType.sqlite + "'.");
                yield new DatabaseSqlite();
            }
        };
    }

    public DataSrc initDataSrc(Database database){
        if(database.isDBEmpty()) {
            List<FileSrcData> fileSrcData = Tools.getDataFromFile(FILE_NAME_SRC);
            if(fileSrcData == null || fileSrcData.isEmpty()){
                logger.error(DataSrc.SRC_FILE_NOT_FOUND.toString());
                return DataSrc.SRC_FILE_NOT_FOUND;
            } else {
                if(database.createDatabaseIfNotExists()){
                    database.insertData(fileSrcData);
                    return DataSrc.OK;
                } else {
                    logger.error(DataSrc.DB_DOES_NOT_EXIST.toString());
                    return DataSrc.DB_DOES_NOT_EXIST;
                }
            }
        } else {
            if(showOtherinfo) {
                logger.info("Database knows " + database.getCountOfData() + " data");
            }
            return DataSrc.OK;
        }
    }

    @Override
    public abstract void connect();

    @Override
    public abstract boolean createDatabaseIfNotExists();

    @Override
    public abstract ArrayList<FileSrcData> getData();

    @Override
    public abstract ArrayList<FileSrcData> getAllData();

    @Override
    public abstract boolean insertData(List<FileSrcData> data);

    @Override
    public abstract int getCountOfData();

    @Override
    public abstract void printInfo();

    @Override
    public abstract void printData();

    @Override
    public abstract void printAllData();

    @Override
    public abstract boolean isDBEmpty();

    public abstract int getCalcFactor(int cityPostalCode);

    public abstract String getCityByPostalCode(int postalCode);

    public abstract List<Integer> getAllCityPostalCodes();
}
