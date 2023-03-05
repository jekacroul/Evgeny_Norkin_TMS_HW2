package SQL_lesson;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.*;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SQL {
    private final String URL = "jdbc:mysql://127.0.0.1:3306/tms";
    private final String USERNAME = "root";
    private final String PASSWORD = "root";

    private Connection con;
    private final Logger logger = LogManager.getLogger(Main.class);

    private final String menu = "#######################################\n" +
            "1: Вывести всю информацию о студентах" + "\n"
            + "2: Поиск студента" + "\n"
            + "3: Добавить студента в таблицу" + "\n"
            + "4: Удалить студента" + "\n"
            + "5: Выход\n" +
            "#######################################";

    private void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            logger.info("Connection succesfull!");
        } catch (Exception ex) {
            logger.error("ОШИБКА");
            logger.error("Connection failed...",ex);
        }
    }

    private Connection getConnection() throws SQLException {
        if(con==null || con.isClosed())
            this.con= DriverManager.getConnection(URL, USERNAME, PASSWORD);
        return this.con;
    }

    void workWithDB() throws SQLException {
        init();
        getConnection();
        try {
            workWithQuery();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    private void workWithQuery() throws SQLException, IOException {
        Connection con = getConnection();
        con.setAutoCommit(false);
        Scanner sc = new Scanner(System.in);
        Boolean work = true;
        try {
            while (work) {
                System.out.println(menu);
                Integer choise = sc.nextInt();
                Statement stmt = con.createStatement();
                if (choise == 1) {
                    ResultSet rs = stmt.executeQuery("SELECT * FROM students");
                    printStudents(rs);
                    rs.close();
                }
                if (choise == 2) {
                    System.out.println("Введите ФИО студента: ");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    String FIO = bufferedReader.readLine();
                    String sql = "SELECT * FROM students where FIO = ?";
                    PreparedStatement stm = con.prepareStatement(sql);
                    stm.setString(1, FIO);
                    ResultSet resultSet = stm.executeQuery();
                    if (printStudents(resultSet)) {
                        System.err.println("Такого пользователя не найдено.");
                        logger.info("Такого пользователя не найдено." + FIO);
                    }
                    resultSet.close();
                }
                if (choise == 3) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    try {
                        System.out.print("Введите порядковый номер студента для добавления: ");
                        Integer id = Integer.valueOf(bufferedReader.readLine());
                        System.err.print("Введен неверный символ!" + "\n");
                        System.out.print("Введите ФИО студента для добавления: ");
                        String FIO = bufferedReader.readLine();
                        System.out.print("Введите город студента: ");
                        String city = bufferedReader.readLine();
                        addStudent(con, id, FIO, city);
                        System.out.println("OK");
                        logger.info("Пользователь добавлен: " + id + FIO + city);
                    } catch (SQLIntegrityConstraintViolationException | NumberFormatException exception) {
                        System.err.print(exception + "\n");
                        logger.error(exception);
                    }
                }
                if (choise == 4) {
                    System.out.print("Введите номер студента для удаления: ");
                    String id = sc.next();
                    dellStudent(con, id);
                    System.out.println("OK");
                    logger.info("Пользватель удален: " + id);
                }
                if (choise == 5) {
                    work = false;
                }
                stmt.close();
            }
        } catch (InputMismatchException exception) {
            System.err.println("Ошибка ввода");
        }
    }

    private boolean printStudents(ResultSet rs) throws SQLException {
        boolean isEmpty=true;
        while (rs.next()) {
            isEmpty=false;
            String str = rs.getString("idStudents") + " "
                    + rs.getString("FIO") + " "
                    + rs.getString("City");
            System.out.println(str);
            logger.info("Список студентов: " + str);
        }
        return isEmpty;
    }

    private void dellStudent(Connection con, String id) throws SQLException {
        String sql = "DELETE FROM students where idStudents = ?";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setString(1, id);
        stm.executeUpdate();
        con.commit();
        stm.close();
    }

    private void addStudent(Connection con, Integer id, String FIO, String city) throws SQLException {
        String sql = "INSERT INTO students (idStudents, FIO, City) Values (?, ?, ?)";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.setInt(1, id);
        stm.setString(2, FIO);
        stm.setString(3, city);
        stm.executeUpdate();
        con.commit();
        stm.close();
    }
}