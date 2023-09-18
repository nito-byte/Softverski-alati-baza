/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DB {

    private static final String username = "sa";
    private static final String password = "123";
    private static final String database = "BB"; //UPISI IME BAZE
    private static final int port = 1433;
    private static final String serverName = "localhost";

    private static final String connectionString = "jdbc:sqlserver://" + serverName + ":" + port + ";"
            + "database=" + database + ";user=" + username + ";password=" + password;

    private Connection connection;

    private DB() {
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static DB db = null;

    public static DB getInstance() {
        if (db == null) {
            db = new DB();
        }
        System.out.println("Dohvatim konekciju!");
        return db;
    }

    public Connection getConnection() {
        return connection;
    }
}
