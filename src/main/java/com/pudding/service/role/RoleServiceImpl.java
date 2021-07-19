package com.pudding.service.role;

import com.pudding.dao.BaseDao;
import com.pudding.dao.role.RoleDao;
import com.pudding.dao.role.RoleDaoImpl;
import com.pudding.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao;

    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() throws SQLException {
        Connection connection = BaseDao.getConnection();
        List<Role> roleList= roleDao.getRoleList(connection);
        BaseDao.closeResource(connection,null,null);
        return roleList;
    }

    @Test
    public void Test() throws Exception{
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        System.out.println(Arrays.toString(roleList.toArray()));
    }


}
