package SQL_lesson;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.*;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SQL {
    private static String URL = "jdbc:mysql://127.0.0.1:3306/tms";
    private static String USERNAME = "root";
    private static String PASSWORD = "root";
    private static final Logger logger = LogManager.getLogger(Main.class);


    private static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            System.out.println("Connection succesfull!");
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    static void workWithDB() throws SQLException {
        init();
        getConnection();
        try {
            workWithQuery();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void workWithQuery() throws SQLException, IOException {
        Connection con = getConnection();
        con.setAutoCommit(false);
        Scanner sc = new Scanner(System.in);
        Boolean work = true;
        try {
            while (work) {
                System.out.println("#######################################");
                System.out.println("1: Вывести всю информацию о студентах" + "\n"
                        + "2: Поиск студента" + "\n"
                        + "3: Добавить студента в таблицу" + "\n"
                        + "4: Удалить студента" + "\n"
                        + "5: Выход");
                System.out.println("#######################################");
                Integer choise = sc.nextInt();
                Statement stmt = con.createStatement();
                if (choise == 1) {
                    ResultSet rs = stmt.executeQuery("SELECT * FROM students");
                    while (rs.next()) {
                        String str = rs.getString("idStudents") + " " + rs.getString("FIO") + " "
                                + rs.getString("City");
                        System.out.println(str);
                        logger.info("Список студентов: " + str);
                    }
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
                    Boolean check = true;
                    while (resultSet.next()) {
                        Integer id = resultSet.getInt("idStudents");
                        String name = resultSet.getString("FIO");
                        String city = resultSet.getString("City");
                        System.out.print(id + " " + name + " " + city + "\n");
                        logger.info(id + " " + name + " " + city + "\n");
                        check = false;
                    }
                    if (check) {
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
                        String sql = "INSERT INTO students (idStudents, FIO, City) Values (?, ?, ?)";
                        PreparedStatement stm = con.prepareStatement(sql);
                        stm.setInt(1, id);
                        stm.setString(2, FIO);
                        stm.setString(3, city);
                        stm.executeUpdate();
                        con.commit();
                        stm.close();
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
                    String sql = "DELETE FROM students where idStudents = ?";
                    PreparedStatement stm = con.prepareStatement(sql);
                    stm.setString(1, id);
                    stm.executeUpdate();
                    con.commit();
                    stm.close();
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
}