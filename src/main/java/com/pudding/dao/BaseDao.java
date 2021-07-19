package com.pudding.dao;

import com.mysql.cj.jdbc.exceptions.SQLError;
import com.mysql.jdbc.Driver;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

// 操作数据库的公共类
public class BaseDao {
    private static final String driver;
    private static final String url;
    private static final String username;
    private static final String password;

    static {
        Properties properties = new Properties();
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");

    }

    // 数据库链接
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    // 编写查询公共类
    public static ResultSet execute(Connection connection, PreparedStatement prepareStatement, ResultSet resultSet, String sql, Object[] params) throws SQLException {
        // 预编译的sql在后面直接执行就可以了
        prepareStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            prepareStatement.setObject(i + 1, params[i]);
        }
        resultSet = prepareStatement.executeQuery();
        return resultSet;

    }

    // 编写增删改
    public static int execute(Connection connection, PreparedStatement prepareStatement, String sql, Object[] params) throws SQLException {
        prepareStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            prepareStatement.setObject(i + 1, params[i]);
        }
        return prepareStatement.executeUpdate();
    }

    // 释放资源
    public static boolean closeResource(Connection connection,PreparedStatement prepareStatement,ResultSet resultSet){
        boolean flag = true;
        if (resultSet!=null){
            try {
                resultSet.close();
                resultSet = null;
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (resultSet!=null){
            try {
                prepareStatement.close();
                prepareStatement = null;
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (resultSet!=null){
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
