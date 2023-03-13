package SQL_createTable;

import SQL_lesson.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/*
Написать скрипт по созданию 2 таблиц: author(id,name,surname), book(id, title, authod_id, pages).
Заполнить таблицы данными(через скрипт)
Вывести данные о всех авторах и его книгах(использовать join).
Вывести все книги, где количество страниц больше 300.

Доп.задание (со *)
Вывести авторов у которых есть хотя бы 1 книги, где количество страниц больше 300.
 */
public class SQL {
    private static String URL = "jdbc:mysql://127.0.0.1:3306/tms";
    private static String LOGIN = "root";
    private static String PASSWORD = "root";
    private static final Logger logger = LogManager.getLogger(Main.class);

    private static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            logger.info("Connection succesfull!");
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            logger.error("Connection failed..." + ex);
        }
        return DriverManager.getConnection(URL, LOGIN, PASSWORD);
    }

    protected void initConn() throws SQLException {
        getConnection();
        createTableAuthor();
        createTableBook();
        insertValuesIntoTableAuthor();
        insertValuesIntoTableBook();
        displayAuthorAndBooksWherePagesMore300();
        displayBookPages();
    }

    private static void createTableAuthor() throws SQLException {
        Connection connection = null;
        Statement stm = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE author (id INT PRIMARY KEY , name TEXT(10), surname TEXT(10))";
            statement.executeUpdate(sql);
            connection.commit();

        } catch (SQLException | RuntimeException e) {
            System.err.println(e);
            logger.error(e);
        } finally {
            stm.close();
            connection.close();
        }
    }

    private static void createTableBook() throws SQLException {
        Connection connection = null;
        Statement stm = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            stm = connection.createStatement();
            String sql = "CREATE TABLE book(id INT PRIMARY KEY, title TEXT(10),author_id INT, pages INT)";
            stm.executeUpdate(sql);
            connection.commit();

        } catch (SQLException | RuntimeException e) {
            System.err.println(e);
            logger.error(e);
        } finally {
            stm.close();
            connection.close();
            logger.info("Resources are closed.");
        }
    }

    private static void insertValuesIntoTableAuthor() throws SQLException {
        Connection connection = null;
        Statement stm = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            stm = connection.createStatement();
            String sql = "INSERT INTO author (id, name ,surname) VALUES (1,'Сергей','Есенин')," +
                    "(2,'Владимир','Высоцкий')," +
                    "(3,'Александр','Пушкин')";
            stm.executeUpdate(sql);
            connection.commit();

        } catch (SQLException | RuntimeException e) {
            System.err.println(e);
            logger.error(e);
        } finally {
            stm.close();
            connection.close();
            logger.info("Resources are closed.");
        }
    }

    private static void insertValuesIntoTableBook() throws SQLException {
        Connection connection = null;
        Statement stm = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            stm = connection.createStatement();
            String sql = "INSERT INTO book (id, title, author_id, pages) VALUES (1,'Письмо к женщине',1,100)," +
                    "(2,'Роман о девочках',2,132)," +
                    "(3,'Зимнее утро',3,245)";
            stm.executeUpdate(sql);
            connection.commit();

        } catch (SQLException | RuntimeException e) {
            System.err.println(e);
            logger.error(e);
        } finally {
            stm.close();
            connection.close();
            logger.info("Resources are closed.");
        }
    }

    private static void displayBookPages() throws SQLException {
        Connection connection = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            stm = connection.createStatement();
            rs = stm.executeQuery("select title AS Book, pages AS Pages from book\n" +
                    "where pages > 100");
            while (rs.next()) {
                String str = rs.getString("Book") + " " + rs.getString("Pages");
                System.out.println(str);
            }

        } catch (SQLException | RuntimeException e) {
            System.err.println(e);
            logger.error(e);
        } finally {
            connection.close();
            stm.close();
            rs.close();
            logger.info("Resources are closed.");
        }
    }

    private static void displayAuthorAndBooksWherePagesMore300() throws SQLException {
        Connection connection = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            stm = connection.createStatement();
            rs = stm.executeQuery("select a1.name AS Author, a1.surname, a2.title AS Book from author a1\n" +
                    "join book a2 on a1.id = a2.author_id\n" +
                    "where a2.pages > 100");
            while (rs.next()) {
                String str = rs.getString("Author") + " " + rs.getString("surname") + " " + rs.getString("Book");
                System.out.println(str);
            }

        } catch (SQLException | RuntimeException e) {
            System.err.println(e);
            logger.error(e);
        } finally {
            connection.close();
            stm.close();
            rs.close();
            logger.info("Resources are closed.");
        }
    }
}
