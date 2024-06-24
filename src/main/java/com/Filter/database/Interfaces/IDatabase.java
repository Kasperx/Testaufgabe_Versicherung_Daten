package main.java.com.Filter.database.Interfaces;

import java.util.ArrayList;

import main.java.com.Filter.Data.FileSrcData;

public interface IDatabase
{
    /**
     * connect to database
     */
    public void connect();
    /**
     * create new database if not exists
     * @return true if success, false otherwise
     */
    public boolean createDatabaseIfNotExists();
    /**
     * get data, but only user basic data
     * @return list with all data, without sensible information like passwords
     */
    public ArrayList<FileSrcData> getData();
    /**
     * get all data including passwords
     * @return list with all data
     */
    public ArrayList<FileSrcData> getAllData();

    public boolean insertData(FileSrcData fileSrcData);

    public void printInfo();
    public void printData();

    public void printData(int countData);

    public void printAllData();

    public boolean isDBEmpty();
}
