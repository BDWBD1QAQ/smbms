package com.pudding.service.user;

import com.pudding.dao.BaseDao;
import com.pudding.dao.user.UserDao;
import com.pudding.dao.user.UserDaoImpl;
import com.pudding.pojo.User;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }
    @Override
    public User login(String userCode, String userPassword){
        Connection connection = null;
        User user = null;
        connection = BaseDao.getConnection();
        // 通过业务层调用对应的具体数据库操作
        try {
            user = userDao.getLoginUser(connection,userCode);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return user;
    }

    @Override
    public boolean updatePwd(int id, String password) {
        Connection connection;
        connection = BaseDao.getConnection();
        boolean flag = false;
        try {
            if(userDao.updatePwd(connection,id,password)>0){
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String username, int userRole) throws SQLException {
        Connection connection = BaseDao.getConnection();
        int count = userDao.getUserCount(connection,username,userRole);
        BaseDao.closeResource(connection,null,null);
        return count;
    }

    @Override
    public List<User> getUserList(String username, int userRole, int currentPageNo, int pageSize) throws SQLException {
        Connection connection = BaseDao.getConnection();
        List<User> user_list = userDao.getUsesrList(connection,username,userRole,currentPageNo,pageSize);
        BaseDao.closeResource(connection,null,null);
        return user_list;
    }

    @Override
    public int add(User user) throws SQLException {
        Connection connection = BaseDao.getConnection();
        int result = userDao.addUser(connection,user);
        BaseDao.closeResource(connection,null,null);
        return result;
    }
}
