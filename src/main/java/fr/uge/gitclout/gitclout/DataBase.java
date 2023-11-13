package fr.uge.gitclout.gitclout;

import java.sql.*;

public class DataBase {
    private Connection conn;


    public DataBase(){
        try{
            connectionToDerby();
            createTables();
            System.out.println("TESTSUCESS");
        }catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void connectionToDerby() throws SQLException {
        try{
            String dbURL = "jdbc:derby:memory:local;create=true";
            conn = DriverManager.getConnection(dbURL);
        } catch (SQLException e) {
            handleSQLException(e);
        }

    }

    public void insertInTableContributeur(String pseudo, String name) {
        try (Statement stmt = conn.createStatement()) {
            String insertSQL = "INSERT INTO contributeur (pseudo, name) VALUES ('" + pseudo + "', '" + name + "')";
            stmt.execute(insertSQL);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void insertInTableProjet(int idprojet, String nomprojet, String description, String pseudo) {
        try (Statement stmt = conn.createStatement()) {
            String insertSQL = "INSERT INTO projet (idprojet, nomprojet, description, pseudo) VALUES ('" + idprojet + "', '" + nomprojet + "', '" + description + "', '" + pseudo + "')";
            stmt.execute(insertSQL);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void insertInTableTechnologie(String techno, String type) {
        try (Statement stmt = conn.createStatement()) {
            String insertSQL = "INSERT INTO technologie (techno, type) VALUES ('" + techno + "', '" + type + "')";
            stmt.execute(insertSQL);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void insertInTableTravaille(String pseudo, int idprojet, String techno, int nbligne) {
        try (Statement stmt = conn.createStatement()) {
            String insertSQL = "INSERT INTO travaille (pseudo, idprojet, techno, nbligne) VALUES ('" + pseudo + "', '" + idprojet + "', '" +  techno + "', '" + nbligne + "')";
            stmt.execute(insertSQL);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void createTables() {
        try(Statement stmt = conn.createStatement()){
            stmt.executeUpdate("CREATE TABLE contributeur (pseudo varchar(50) PRIMARY KEY, name varchar(50))");
            stmt.executeUpdate("CREATE TABLE projet (idprojet int PRIMARY KEY, nomprojet varchar(50), description varchar(250))");
            stmt.executeUpdate("CREATE TABLE technologie (techno varchar(50) PRIMARY KEY, type varchar(50))");
            stmt.executeUpdate("CREATE TABLE travaille (pseudo varchar(50), idprojet int, techno varchar(50), pseudoTag varchar(50), nbligne int, descriptionTag varchar(250), PRIMARY KEY (pseudo, idprojet, techno, pseudoTag))");
            stmt.executeUpdate("ALTER TABLE travaille ADD CONSTRAINT foreign_key_technologie FOREIGN KEY (techno) REFERENCES technologie (techno)");
            stmt.executeUpdate("ALTER TABLE travaille ADD CONSTRAINT foreign_key_contributeur FOREIGN KEY (pseudo) REFERENCES contributeur (pseudo)");
            stmt.executeUpdate("ALTER TABLE travaille ADD CONSTRAINT foreign_key_projet FOREIGN KEY (idprojet) REFERENCES projet (idprojet)");
        }catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void queryTest() {
        try (Statement stmt = conn.createStatement()) {
            String querySQL = "SELECT * FROM contributeur";
            ResultSet resultSet = stmt.executeQuery(querySQL);
            while (resultSet.next()) {
                String pseudo = resultSet.getString("pseudo");
                String name = resultSet.getString("name");
                System.out.println("pseudo : " + pseudo + ", Name : " + name);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void handleSQLException(SQLException e) {
        String sqlState = e.getSQLState();
        int errorCode = e.getErrorCode();
        String errorMessage = e.getMessage();

        if (sqlState != null) {
            if (sqlState.startsWith("08")) {
                System.err.println("Connection issue: " + errorMessage);
            } else if (sqlState.equals("23505")) {
                System.err.println("Unique constraint violation: " + errorMessage);
            } else {
                System.err.println("SQL State: " + sqlState);
                System.err.println("Error Code: " + errorCode);
                System.err.println("Error Message: " + errorMessage);
            }
        } else {

            System.err.println("Error Code: " + errorCode);
            System.err.println("Error Message: " + errorMessage);
        }
    }
}
