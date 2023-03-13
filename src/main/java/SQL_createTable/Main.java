package SQL_createTable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        SQL sql = new SQL();
        try {
            sql.initConn();
        } catch (SQLException e) {
            logger.error(e);
        }
    }
}
