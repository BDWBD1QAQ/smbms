package com.pudding.dao.user;

import com.pudding.pojo.Role;
import com.pudding.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到登良的用户
    public User getLoginUser(Connection connection, String userCode) throws SQLException;

    // 修改用户密码
    public int updatePwd(Connection connection, int id, String password) throws SQLException;

    // 根据用户名或者角色查询用户总数
    public int getUserCount(Connection connection,String username,int userRole) throws SQLException;

    // 通过条件查询-userList
    public List<User> getUsesrList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws SQLException;

    // 添加新用户
    public int addUser(Connection connection,User user) throws SQLException;
}
