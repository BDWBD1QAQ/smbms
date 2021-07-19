package com.pudding.dao.user;

import com.mysql.cj.util.StringUtils;
import com.pudding.dao.BaseDao;
import com.pudding.pojo.Role;
import com.pudding.pojo.User;
import org.apache.taglibs.standard.lang.jstl.NullLiteral;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if (connection != null) {
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreateBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getDate("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getDate("modifyDate"));
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return user;
    }

    @Override
    public int updatePwd(Connection connection, int id, String password) throws SQLException {
        PreparedStatement PreparedStatement = null;
        String sql = "update smbms_user set userPassword=? where id=?";
        Object[] params = {password, id};
        int execute = 0;
        if (connection != null) {
            execute = BaseDao.execute(connection, PreparedStatement, sql, params);
            BaseDao.closeResource(null, PreparedStatement, null);
        }
        return execute;


    }

    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<Object> list = new ArrayList<Object>(); //存放参数
        StringBuffer sql = new StringBuffer();
        int count = 0;
        if (connection != null) {
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id ");
            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append("and u.username like ?");
                list.add("%" + username + "%");
            }
            if (userRole > 0) {
                sql.append("and u.userRole = ?");
                list.add(userRole);
            }
            Object[] objects = list.toArray();
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), objects);
            if (rs.next()) {
                count = rs.getInt("count"); // 从结果集中获取数量
            }
            BaseDao.closeResource(null, pstm, rs);
        }

        return count;
    }

    @Override
    public List<User> getUsesrList(Connection connection, String username, int userRole, int currentPageNo, int pageSize) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<User> user_list = new ArrayList<User>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            List<Object> list = new ArrayList<Object>();
            sql.append("select u.* from smbms_user u,smbms_role r where u.userRole=r.id ");
            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append("and u.userName like ? ");
                list.add("%" + username + "%");
            }
            if (userRole > 0) {
                sql.append("and u.userRole=? ");
                list.add(userRole);
            }
            sql.append("order by u.creationDate DESC limit ?,? ");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);
            Object[] params = list.toArray();
            ResultSet rs = BaseDao.execute(connection, preparedStatement, resultSet, sql.toString(), params);
            while (rs.next()) {
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setCreateBy(rs.getInt("createdBy"));
                _user.setCreationDate(rs.getDate("creationDate"));
                _user.setModifyBy(rs.getInt("modifyBy"));
                _user.setModifyDate(rs.getDate("modifyDate"));
                user_list.add(_user);
            }
            BaseDao.closeResource(null, preparedStatement, resultSet);
        }
        return user_list;
    }

    @Override
    public int addUser(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int updateRow = 0;
        if( connection!=null){
            String sql = "insert into smbms_user " +
                    "(userCode,userName,userPassword,gender,birthday,phone,address,userRole,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(), user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),user.getUserRole(),user.getCreateBy(),user.getCreationDate()};
            updateRow = BaseDao.execute(connection,pstm,sql,params);
            BaseDao.closeResource(null,pstm,null);
        }
        return updateRow;
    }
}
