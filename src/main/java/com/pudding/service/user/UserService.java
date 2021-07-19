package com.pudding.service.user;
import com.pudding.pojo.User;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    public User login(String userCode, String userPassword);
    // 根据用户id修改密码
    public boolean updatePwd(int id,String password);
    // 查询记录数
    public int getUserCount(String username,int userRole) throws SQLException;
    //根据条件查询用户列表
    public List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize)  throws SQLException;
    //添加用户
    public int add(User user) throws SQLException;
}

