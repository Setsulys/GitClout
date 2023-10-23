package fr.uge.gitclout.gitclout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class DataBase {
//    private Connection conn;
//
//    public static void main(String[] args) throws SQLException {
//        DataBase db = new DataBase();
//        db.connectionToDerby();
//    }
//
//    public void connectionToDerby() throws SQLException{
//        String dbURL = "jdbc:derby:memory:local;create=true";
//        conn = DriverManager.getConnection(dbURL);
//    }
//
//    public void normalDbUsage()throws SQLException{
//        Statement stmt = conn.createStatement();
//
//        // drop table
//        // stmt.executeUpdate("Drop Table users");
//
//        // create table
//        stmt.executeUpdate("Create table users (id int primary key, name varchar(30))");
//
//        // insert 2 rows
//        stmt.executeUpdate("insert into users values (1,'tom')");
//        stmt.executeUpdate("insert into users values (2,'peter')");
//
//        // query
//        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
//
//        // print out query result
//        while (rs.next()) {
//            System.out.printf("%d\t%s\n", rs.getInt("id"), rs.getString("name"));
//        }
//    }
}
