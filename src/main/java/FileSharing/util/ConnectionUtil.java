package FileSharing.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static final String DB_PROPERTY_PATH = "src/main/resources/configuration/db.properties";
    private static final String DRIVER_PROPERTY_NAME = "jdbc.driver";
    private static final String URL_PROPERTY_NAME = "jdbc.url";

    static {
        try {
            Class.forName(getProperty(DRIVER_PROPERTY_NAME));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find MySQL Driver", e);
        }
    }

    public static Connection getConnection() {
        String connection = getProperty(URL_PROPERTY_NAME);
        try {
            return DriverManager.getConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Can't established connection to DB", e);
        }
    }

    private static String getProperty(String propertyName) {
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream(DB_PROPERTY_PATH);
            props.load(in);
            in.close();
            return props.getProperty(propertyName);
        } catch (IOException e) {
            throw new RuntimeException("Can't get property " + propertyName, e);
        }

    }
}
