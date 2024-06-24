
package main.java.com.Filter.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

import main.java.com.Filter.Data.FileSrcData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseFile
        extends Database{

    int id;

    String name;

    String lastname;

    String pw;

    boolean admin;

    final static Logger logger = LogManager.getLogger(DatabaseFile.class.getName());

    public DatabaseFile(){
        path = System.getProperty("user.dir")+"/test";
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

    public DatabaseFile(int id, String name, String lastname, String pw, boolean admin){
        path = System.getProperty("user.dir")+"/test";
        File dbFile = new File(path);
        try{
            if(!dbFile.exists()){
                dbFile.createNewFile();
            }
        } catch(Exception e) {
            logger.error(e);
        }
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.pw = pw;
        this.admin = admin;
    }

	public ArrayList<FileSrcData> getData(){
        ArrayList<FileSrcData> data = new ArrayList<>();
        try(ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path))){
            DatabaseFile db;
            Object obj;
            FileSrcData fileSrcData = new FileSrcData();
            while((obj = inFile.readObject()) != null){
                data.add(fileSrcData);
            }
            return data;
        } catch(ClassNotFoundException | IOException e) {
            logger.error(e);
        }
        return data;
    }

    public int getId(String name)
    {
        return -1;
    }
    public ArrayList<FileSrcData> getAllData(){
        ArrayList<FileSrcData> data = new ArrayList<>();
        try(ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path))){
            return data;
        } catch(Exception e) {
            logger.error(e);
        }
        return data;
    }

    @Override
    public boolean insertData(List<FileSrcData> data) {
        return false;
    }

    @Override
    public int getCountOfData() {
        return 0;
    }

    @Override
    public int getCalcFactor(int cityPostalCode) {
        return 0;
    }

    @Override
    public String getCityByPostalCode(int postalCode) {
        return null;
    }

    @Override
    public List<Integer> getAllCityPostalCodes() {
        return null;
    }

    @Override
    public boolean insertData(FileSrcData data) {
        return false;
    }

    @Override
    public void printInfo() {}

    @Override
    public void printData() {
        printData(LIMIT_PRINT_DATA);
    }

    @Override
    public void printData(int countData) {}

    @Override
    public void printAllData() {}

    @Override
    public boolean isDBEmpty() {
        return getData().isEmpty();
    }

    @Override
	public void connect(){}

    @Override
    public boolean createDatabaseIfNotExists() {
        return false;
    }
}

