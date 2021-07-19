package com.pudding.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.mysql.cj.xdevapi.JsonArray;
import com.pudding.pojo.Role;
import com.pudding.pojo.User;
import com.pudding.service.role.RoleService;
import com.pudding.service.role.RoleServiceImpl;
import com.pudding.service.user.UserService;
import com.pudding.service.user.UserServiceImpl;
import com.pudding.util.Contents;
import com.pudding.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        switch (method) {
            case "savePwd":
                this.updataPwd(req, resp);
                break;
            case "isPwd":
                this.isPwd(req, resp);
                break;
            case "query":
                try {
                    this.query(req, resp);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "add":
                this.add(req,resp);
            case "showUserRole":
                this.showUserRole(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void updataPwd(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        String userpassword = req.getParameter("userpassword");
        String id = req.getParameter("id");
        boolean flag = false;
        int is = 0;
        if(!StringUtils.isNullOrEmpty(userpassword) && !StringUtils.isNullOrEmpty(id)){
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(Integer.parseInt(id),userpassword);
            if(flag){
                System.out.println();
                req.setAttribute("message","修改密码成功,请退出使用新密码登录");
                req.getSession().removeAttribute(Contents.USER_SESSION);
                is = 1;
            }else{
                req.setAttribute("message","密码修改失败");
            }
        }else {
            req.setAttribute("message", "新密码有问题");
        }
        resp.getWriter().print(is);
    }
    public void isPwd(HttpServletRequest req,HttpServletResponse resp) throws  IOException{
        String pwd = req.getParameter("pwd");
        Object user = req.getSession().getAttribute(Contents.USER_SESSION);
        Map<String,String> resultMap = new HashMap<String,String>();
        if (user==null){ //session失效了
            resultMap.put("msg","session已经失效");
            resultMap.put("flag","1");
        }else if(StringUtils.isNullOrEmpty(pwd)){ //密码输入为空
            resultMap.put("msg","输入密码为空");
            resultMap.put("flag","2");
        }else{
            String password = ((User) user).getUserPassword();
            if (pwd.equals(password)){
                resultMap.put("msg","密码校验成功");
                resultMap.put("flag","0");
            }else{
                resultMap.put("msg","密码校验失败");
                resultMap.put("flag","4");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }
    public void query(HttpServletRequest req,HttpServletResponse resp) throws  IOException, SQLException {
        //查询用户列表
        String username = req.getParameter("username");
        String rolename = req.getParameter("rolename");
        String n = req.getParameter("n");
        int queryRoleName = 0;
        UserServiceImpl userService = new UserServiceImpl();
        RoleServiceImpl roleService = new RoleServiceImpl();
        int pageSize = 5;
        int currentPageNo = 1;
        if (username==null){
            username = "";
        }
        if (rolename!=null && !rolename.equals("")){
            queryRoleName = Integer.parseInt(rolename);
        }
        if(n !=null){
            try {
                currentPageNo = Integer.parseInt(n);
            } catch (Exception e) {
                resp.sendRedirect("error.jsp");
            }
        }
        int totalCount = userService.getUserCount(username,queryRoleName);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        //控制首页和尾页
        int totalPageCount = pageSupport.getTotalPageCount();
        if (currentPageNo<1){
            currentPageNo = 1;
        }else if (currentPageNo>totalPageCount){
            currentPageNo = totalPageCount;
        }
        // 获取用户列表
        List<User> userList = userService.getUserList(username,queryRoleName,currentPageNo,pageSize);
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("list",userList);
        req.setAttribute("roleNames",roleList);
        req.setAttribute("total",totalCount);
        req.setAttribute("pageNum",currentPageNo);
        req.setAttribute("pages",totalPageCount);
        req.setAttribute("nextPage",currentPageNo+1);
        req.setAttribute("prePage",currentPageNo-1);
        try {
            req.getRequestDispatcher("/jsp/userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }
    public void add(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        String userCode = req.getParameter("usercode");
        String userName = req.getParameter("username");
        String userPassword = req.getParameter("userpassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userrole");
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreateBy(((User) req.getSession().getAttribute(Contents.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        UserService userService = new UserServiceImpl();
        int result = 0;
        try {
            result = userService.add(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result>0){
            try {
                this.query(req,resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            req.setAttribute("error","添加用户失败");
            try {
                req.getRequestDispatcher("/jsp/useradd.jsp").forward(req,resp);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }

    }
    public void showUserRole(HttpServletRequest req,HttpServletResponse resp) throws IOException{
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = null;
        try {
            roleList = roleService.getRoleList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        req.setAttribute("roles",roleList);
        try {
            req.getRequestDispatcher("/jsp/useradd.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

}
