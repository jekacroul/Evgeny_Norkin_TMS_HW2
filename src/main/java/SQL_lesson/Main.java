package SQL_lesson;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            SQL.workWithDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
