package SQL_lesson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        try {
//            SQL.workWithDB();
            SQL sql= new SQL();
            sql.workWithDB();
        } catch (SQLException e) {
            logger.error("Ошибка запуска основного метода", e);
            System.exit(0);
        }
    }
}
