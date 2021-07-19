package com.pudding.service.role;

import com.pudding.pojo.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    public List<Role> getRoleList()  throws SQLException;
}
