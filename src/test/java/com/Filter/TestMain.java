package test.java.com.Filter;

import main.java.com.Filter.Main;
import main.java.com.Filter.database.Database;
import main.java.com.Filter.database.DatabaseSQLite;
import main.java.com.Filter.service.Filter;
import main.java.com.Filter.service.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

public class TestMain {

    private static Database database;

    private static final Logger log = LogManager.getLogger(TestMain.class);

    private static Main main;

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
    @DisplayName("Test Filter: return null")
    public void testClassFilterReturnNull(){
        assertEquals(Filter.getFactor(-1), 0);
    }

    @Test
    @DisplayName("Test Filter: Parameter (1111) -> return number")
    public void testClassFilterReturnNumber(){
        assertTrue(Filter.getFactor(1111) > 0);
    }

    @Test
    @DisplayName("Test Filter: Parameter (null, 0) -> return null")
    public void testClassFilterReturnCityNameNull(){
        assertNull(Filter.getCityName(null, 0));
    }

    @Test
    @DisplayName("Test Filter: Parameter (null, 99510) -> return null")
    public void testClassFilterReturnCityNameNull2(){
        assertNull(Filter.getCityName(null, 99510));
    }

    @Test
    @DisplayName("Test Filter: Parameter (?, 99510) -> return null")
    public void testClassFilterReturnCityNameNotNull(){
        assertNotNull(Filter.getCityName(Database.getInstance(), 99510));
    }

    @Test
    @DisplayName("Test Database: start -> use type sqlite")
    public void testClassDatabaseReturnDB(){
        assertInstanceOf(DatabaseSQLite.class, database);
    }

    @Test
    @DisplayName("Test Database: start -> not null")
    public void testClassDatabaseReturnNotNull(){
        assertNotNull(database);
    }
}
