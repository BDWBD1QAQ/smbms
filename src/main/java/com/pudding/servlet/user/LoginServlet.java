package com.pudding.servlet.user;

import com.pudding.pojo.User;
import com.pudding.service.user.UserServiceImpl;
import com.pudding.util.Contents;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet--start......");
        // 获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        User user = userServiceImpl.login(userCode,userPassword);
        if(user!=null){  //查有此人
            req.getSession().setAttribute(Contents.USER_SESSION,user);
            resp.sendRedirect("jsp/frame.jsp");
        }else{ //查无此人,无法登陆
            // 转发会登录页面,并提示错误
            req.setAttribute("error","用户名或者密码不存在");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
