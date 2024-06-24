package main.java.com.Filter.database;

import java.util.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.java.com.Filter.Data.FileSrcData;
import main.java.com.Filter.database.DAO.DAO;
import main.java.com.Filter.database.Interfaces.IDatabase;
import main.java.com.Filter.service.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@NoArgsConstructor
@Getter
@Setter
public abstract class Database
        extends DAO
        implements IDatabase
{
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
                logger.info("Not supported yet: source '" + source.value + "'. Using '" + DatabaseType.sqlite + "'.");
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
                    database.insertData(fileSrcData, true);
                    return DataSrc.OK;
                } else {
                    logger.error(DataSrc.DB_DOES_NOT_EXIST.toString());
                    return DataSrc.DB_DOES_NOT_EXIST;
                }
            }
        } else {
            logger.info("Database knows " + database.getCountOfData() + " data");
            return DataSrc.OK;
        }
    }

    public abstract void connect();

    public abstract boolean createDatabaseIfNotExists();

    public abstract ArrayList<FileSrcData> getData();

    public abstract ArrayList<FileSrcData> getAllData();

    public abstract void insertData(List<FileSrcData> data);

    public abstract void insertData(List<FileSrcData> data, boolean test);

    public abstract int getCountOfData();

    public abstract int getCalcFactor(int cityPostalCode);

    public abstract String getCityByPostalCode(int postalCode);
}
