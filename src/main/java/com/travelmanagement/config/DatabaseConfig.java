package com.travelmanagement.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;



public class DatabaseConfig {
    private static Connection connection = null;

    
    
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        try {
           
            InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties");
         
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }

            Properties props = new Properties();
            props.load(input);

            String driver = props.getProperty("db.driver");
            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");       
            Class.forName(driver);       
            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
    
}
