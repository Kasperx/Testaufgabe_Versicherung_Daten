package test.java.com.Filter;

import main.java.com.Filter.Main;
import main.java.com.Filter.database.Database;
import main.java.com.Filter.database.DatabaseSqlite;
import main.java.com.Filter.service.FileSrcDataFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMain {

    static Database database;

    static Main main;

    @BeforeAll
    @DisplayName("Test Main: instance != null")
    public static void start()
            throws Exception {
        main = new Main();
        database = Database.getInstance();
    }

    @Test
    @DisplayName("Test objects != null")
    public void testObjectsNotNull(){
        assertNotNull(main);
        assertNotNull(database);
    }

    @Test
    @DisplayName("Test FileSrcDataFilter: return null")
    public void testClassFilterReturnNull(){
        assertEquals(FileSrcDataFilter.getFactor(-1), 0);
    }

    @Test
    @DisplayName("Test FileSrcDataFilter: Parameter (1111) -> return number")
    public void testClassFilterReturnNumber(){
        assertTrue(FileSrcDataFilter.getFactor(1111) > 0);
    }

    @Test
    @DisplayName("Test FileSrcDataFilter: Parameter (null, 0) -> return null")
    public void testClassFilterReturnCityNameNull(){
        assertNull(FileSrcDataFilter.getCityName(null, 0));
    }

    @Test
    @DisplayName("Test FileSrcDataFilter: Parameter (null, 99510) -> return null")
    public void testClassFilterReturnCityNameNull2(){
        assertNull(FileSrcDataFilter.getCityName(null, 99510));
    }

    @Test
    @DisplayName("Test FileSrcDataFilter: Parameter (?, 99510) -> return null")
    public void testClassFilterReturnCityNameNotNull(){
        assertNotNull(FileSrcDataFilter.getCityName(Database.getInstance(), 99510));
    }

    @Test
    @DisplayName("Test Database: start -> use type sqlite")
    public void testClassDatabaseReturnDB(){
        assertInstanceOf(DatabaseSqlite.class, database);
    }

    @Test
    @DisplayName("Test Database: start -> not null")
    public void testClassDatabaseReturnNotNull(){
        assertNotNull(database);
    }
}
