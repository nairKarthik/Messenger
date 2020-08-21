package io.karthik.Messanger.db;

import java.sql.*;

public class Database {

    private static volatile Connection connection;

    public static Connection getConnection() throws SQLException {
        if(connection == null)
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/messenger", "root", "Root@123");
        return connection;
    }

    public static void createUserTable(String name) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE " + name + " (id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50)," +
                "joining_date_time DATE)";
        statement.execute(sql);
        connection.close();
    }

    public static void createChatTable(String name) throws SQLException {
        getConnection();
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE " + name + " (msg_id INT PRIMARY KEY," +
                "name VARCHAR(50)," +
                "msg VARCHAR(200))";
        statement.execute(sql);
        connection.close();
    }

    public static void addUserToDb(String user) throws SQLException {
        getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users VALUE (null, ?, ?)");
        preparedStatement.setString(1,user);
        preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
        int row_affected = preparedStatement.executeUpdate();
        if(row_affected > 0)
            System.out.println("Successfully inserted the msg in DB!");
        else
            System.out.println("Unable to insert the msg in DB!!");
        connection.close();
    }

    public static void chatBackUp(String user, String msg_id, String msg) throws SQLException {
        getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chat_backup VALUES (?,?,?)");
        preparedStatement.setString(1, msg_id);
        preparedStatement.setString(2, user);
        preparedStatement.setString(3, msg);
        int row_affected = preparedStatement.executeUpdate();
        if(row_affected > 0)
            System.out.println("Successfully inserted the msg in DB!");
        else
            System.out.println("Unable to insert the msg in DB!!");
        connection.close();
    }
}
