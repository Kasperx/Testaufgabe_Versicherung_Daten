package main.java.com.Filter.database;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.java.com.Filter.Data.FileSrcData;
import main.java.com.Filter.database.DAO.DAO;
import main.java.com.Filter.service.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.tablesaw.api.Table;

public class DatabaseSqlite extends Database{
    String path = System.getProperty("user.dir")+"/database.db";

    Connection connection = null;

    boolean test = true;

    static Logger logger = LogManager.getLogger(DatabaseSqlite.class.getName());

    public DatabaseSqlite(){
        File dbFile = new File(path);
        try{
            if(!dbFile.exists()){
                dbFile.createNewFile();
            }
            connect();
        } catch(IOException e) {
            logger.error(e);
        }
    }

    public void connect(){
        try{
        	if(connection == null || connection.isClosed()){
        		Class.forName("org.sqlite.JDBC");
        		connection = DriverManager.getConnection("jdbc:sqlite:"+path);
                logger.debug("Connected to database '"+path+"'.");
        	}
        } catch(Exception e) {
            logger.error(e);
        }
    }

    @Override
    public ArrayList<FileSrcData> getData(){
        return getDataFromDB("SELECT * FROM " + Database.TABLE_NAME + " limit " + LIMIT_PRINT_DATA + ";");
    }

    /**
     * get data with all information
     */
    @Override
    public ArrayList<FileSrcData> getAllData(){
        return getDataFromDB("SELECT * FROM " + Database.TABLE_NAME + ";");
    }

    /**
     * if permitted: create Database If Not Exists
     */
    public boolean createDatabaseIfNotExists(){
        connect();
        if(permitCreateDB && getCountOfData() == 0) {
            //executeSet("remove from table test");
            //executeSet("drop database if exists test");
            //////////////////////////////
            executeSet("create table if not exists " + DAO.TABLE_NAME + " ("
                    + "id integer primary key autoincrement,"
                    + "ISO_3166_1_ALPHA_2 text,"
                    + "ISO_3166_1_ALPHA_2_REGION_CODE text,"
                    + "REGION1 text,"
                    + "REGION2 text,"
                    + "REGION3 text,"
                    + "REGION4 text,"
                    + "POSTLEITZAHL integer,"
                    + "ORT text,"
                    + "AREA1 text,"
                    + "AREA2 text,"
                    + "LATITUDE real,"
                    + "LONGITUDE real,"
                    + "ZEITZONE text,"
                    + "CALC_LOCALE integer,"
                    + "SOMMERZEIT integer,"
                    + "ACTIVE text"
                    + ");");
            /*
             *  table login with boolean.. but seems problem with jdbc -> sqlite
             *  sometimes boolean = int, sometimes not :(
             *  so boolean = int and works. remember for other dbs
             */
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void insertData(List<FileSrcData> data) {
        logger.info("Storing object to db ...");
        for(FileSrcData fileSrcData: data) {
            insertData(fileSrcData);
        }
    }

    @Override
    public void insertData(List<FileSrcData> data, boolean test) {
        logger.info("Storing object to db ...");
        // to see progress ... (remove after test)
        if(test) {
            for (int i = 0; i < data.size(); i++) {
                if (i % 1000 == 0) {
                    logger.info("Storing object #"+i+" to db -> " + data.get(i).toString());
                }
                insertData(data.get(i));
            }
        }
    }

    @Override
    public int getCountOfData() {
        connect();
        if(dbTableExists()){
            try(ResultSet resultSet = executeGet("select count(*) from " + TABLE_NAME + ";");){
                if(resultSet != null && resultSet.next()){
                    return resultSet.getInt(1);
                }
            } catch (Exception e) {
                logger.error(e);
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int getCalcFactor(int cityPostalCode) {
        return 0;
    }

    @Override
    public String getCityByPostalCode(int postalCode) {
        return getCityFromDB(postalCode);
    }

    @Override
    public boolean insertData(FileSrcData fileSrcData)
    {
        try {
            String sql = ""
                    +" insert into " + DAO.TABLE_NAME + " ("
                    + "ISO_3166_1_ALPHA_2,"
                    + "ISO_3166_1_ALPHA_2_REGION_CODE,"
                    + "REGION1,"
                    + "REGION2,"
                    + "REGION3,"
                    + "REGION4,"
                    + "POSTLEITZAHL,"
                    + "ORT,"
                    + "AREA1,"
                    + "AREA2,"
                    + "LATITUDE,"
                    + "LONGITUDE,"
                    + "ZEITZONE,"
                    //+ "CALC_LOCALE,"
                    //+ "SOMMERZEIT,"
                    + "ACTIVE"
                    + ") values ("
                    + "";
            connect();
            int count = 1;
            PreparedStatement stmt = connection.prepareStatement(sql+
                    "?,?,?," +
                    "?,?,?," +
                    "?,?,?," +
                    "?,?,?," +
                    "?,?" +
                    ""+");");
            stmt.setString(count++, fileSrcData.getISO_3166_1_ALPHA_2());
            stmt.setString(count++, fileSrcData.getISO_3166_1_ALPHA_2_REGION_CODE());
            stmt.setString(count++, fileSrcData.getREGION1());
            stmt.setString(count++, fileSrcData.getREGION2());
            stmt.setString(count++, fileSrcData.getREGION3());
            stmt.setString(count++, fileSrcData.getREGION4());
            stmt.setInt(count++, fileSrcData.getPOSTLEITZAHL());
            stmt.setString(count++, fileSrcData.getORT());
            stmt.setString(count++, fileSrcData.getAREA1());
            stmt.setString(count++, fileSrcData.getAREA2());
            stmt.setString(count++, String.format("%."+COUNT_DIGITS_AFTER_COMMA+"f", fileSrcData.getLATITUDE()));
            stmt.setString(count++, String.format("%."+COUNT_DIGITS_AFTER_COMMA+"f", fileSrcData.getLONGITUDE()));
            //stmt.setInt(count++, fileSrcData.getCALC_LOCALE());
            //stmt.setInt(count++, fileSrcData.getSOMMERZEIT());
            stmt.setString(count++, fileSrcData.getACTIVE());
            logger.debug(stmt.toString());
            stmt.execute();
            close(null);
        } catch (Exception e) {
            logger.error("insert into " + DAO.TABLE_NAME, e);
        }
        return false;
    }

    @Override
    public void printData(int countData) {
        logger.info("Printing first " + countData + " objects from db.");
        logger.info("");
        try {
            Table table = Table.read()
                    .db(executeGet("SELECT * FROM " + Database.TABLE_NAME + "  limit " + countData + ";"));
            logger.info(table.print());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printData() {
        printData(LIMIT_PRINT_DATA);
    }

    @Override
    public void printAllData() {
        //logger.info("Printing first 1000 objects from db.");
        logger.info("");
        try {
            Table table = Table.read()
                    .db(executeGet("SELECT * FROM " + Database.TABLE_NAME + ";"));
            logger.info(table.print());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isDBEmpty() {
        return getCountOfData() <= 0;
    }

    @Override
    public void printInfo() {
        logger.info("Database has " + getCountOfData() + " count of data saved.");
    }

    /**
     * execute sql cmd
     * @param sql
     * @return
     */
    ResultSet executeGet(String sql){
        try{
            logger.debug(sql);
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            return stmt.executeQuery();
        } catch(SQLException e) {
            logger.error(e);
            return null;
        }
    }

    boolean dbTableExists(){
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, TABLE_NAME, new String[] {"TABLE"});
            return resultSet.next();
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }

    /**
     * get meta data
     * @param sql
     * @return ResultSetMetaData
     */
    ResultSetMetaData getMetaData(String sql){
    	try {
    		logger.info(sql);
    		PreparedStatement stmt = connection.prepareStatement(sql);
    		return stmt.getMetaData();
    	} catch(SQLException e) {
    		logger.error(e);
    		return null;
    	}
    }

    /**
     * execute sql cms
     * @param sql
     */
    void executeSet(String sql){
        try {
            logger.debug(sql);
            connect();
            connection.prepareStatement(sql).executeUpdate();
            close(null);
        } catch(SQLException e) {
        	logger.error(e);
        }
    }

    ArrayList<FileSrcData> getDataFromDB(String sql){
        ResultSet resultSet = executeGet(sql);
    	ArrayList<FileSrcData> data = new ArrayList<FileSrcData>();
        try{
            FileSrcData fileSrcData;
            while(resultSet != null && resultSet.next()){
                fileSrcData = new FileSrcData();
                fileSrcData.setISO_3166_1_ALPHA_2(
                        resultSet.getString("ISO_3166_1_ALPHA_2")
                );
                fileSrcData.setISO_3166_1_ALPHA_2_REGION_CODE(
                        resultSet.getString("ISO_3166_1_ALPHA_2_REGION_CODE")
                );
                fileSrcData.setREGION1(
                        resultSet.getString("REGION1")
                );
                fileSrcData.setREGION2(
                        resultSet.getString("REGION2")
                );
                fileSrcData.setREGION3(
                        resultSet.getString("REGION3")
                );
                fileSrcData.setREGION4(
                        resultSet.getString("REGION4")
                );
                fileSrcData.setPOSTLEITZAHL(
                        resultSet.getInt("POSTLEITZAHL")
                );
                fileSrcData.setORT(
                        resultSet.getString("ORT")
                );
                fileSrcData.setAREA1(
                        resultSet.getString("AREA1")
                );
                fileSrcData.setAREA2(
                        resultSet.getString("AREA2")
                );
                fileSrcData.setLATITUDE(
                        resultSet.getDouble("LATITUDE")
                );
                fileSrcData.setLONGITUDE(
                        resultSet.getDouble("LONGITUDE")
                );
                fileSrcData.setZEITZONE(
                        resultSet.getString("ZEITZONE")
                );
                fileSrcData.setCALC_LOCALE(
                        resultSet.getInt("CALC_LOCALE")
                );
                fileSrcData.setSOMMERZEIT(
                        Tools.intToBoolean(resultSet.getInt("SOMMERZEIT"))
                );
                fileSrcData.setACTIVE(
                        resultSet.getString("ACTIVE")
                );
                data.add(fileSrcData);
            }
            close(resultSet);
        } catch(SQLException e) {
        	logger.error(e);
        }
        return data;
    }

    String getCityFromDB(int postalCode){
        logger.info(getCityWherePostalCodeNotNull());
        ResultSet resultSet = executeGet("select REGION2 from " + TABLE_NAME + " where POSTLEITZAHL = " + postalCode + ";");
        String result = null;
        try{
            if(resultSet != null && resultSet.next()){
                result = resultSet.getString(1);
            }
            close(resultSet);
        } catch(SQLException e) {
            logger.error(e);
        }
        return result;
    }

    List<String> getCityWherePostalCodeNotNull(){
        ResultSet resultSet = executeGet("select REGION2 from " + TABLE_NAME + " where POSTLEITZAHL != null;");
        List<String> result = new ArrayList<>();
        try{
            while(resultSet != null && resultSet.next()){
                result.add(resultSet.getString(1));
            }
            close(resultSet);
        } catch(SQLException e) {
            logger.error(e);
        }
        return result;
    }

    /**
     * close db connection
     * @param resultSet
     */
    private void close(ResultSet resultSet){
    	try{
    		if(resultSet != null && !resultSet.isClosed()){
    			resultSet.close();
    		} if(connection != null){
    			connection.close();
    		}
		} catch (SQLException e) {
    		logger.error(e);
		}
	}
}
