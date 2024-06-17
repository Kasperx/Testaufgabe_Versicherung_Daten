package main.java.com.Filter.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.java.com.Filter.Data.FileSrcData;
import main.java.com.Filter.database.DAO.DAO;
import main.java.com.Filter.database.Interfaces.DatabaseInterface;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@NoArgsConstructor
@Getter
@Setter
public abstract class Database
        extends DAO
        implements DatabaseInterface
{
    static Logger logger = LogManager.getLogger(Database.class.getName());

    protected boolean permitCreateDB = true;

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

    protected Connection connection;

    protected String serverIp;

    protected String path;

    boolean headerInUppercaseCharacter = true;

    static HashMap<String, String> mapFromFile;

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
            case sqlite -> new DatabaseSQLite();
            default -> {
                logger.info("Not supported yet: source '" + source.value + "'. Using '" + DatabaseType.sqlite + "'.");
                yield new DatabaseSQLite();
            }
        };
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
