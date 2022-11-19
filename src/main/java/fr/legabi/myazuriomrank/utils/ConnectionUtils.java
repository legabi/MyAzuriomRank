package fr.legabi.myazuriomrank.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {
    public static Connection getMyConnection(String s, String azuriom, String root, String s1) throws SQLException,
            ClassNotFoundException {

        return MysqlConnUtils.getMySQLConnection(s, azuriom, root, s1);
    }
}
