package com.pudding.filter;

import com.pudding.pojo.User;
import com.pudding.util.Contents;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        User userSession = (User) req.getSession().getAttribute(Contents.USER_SESSION);
        if (userSession==null){  // 已经被移除或者注销了,或者未登录
            resp.sendRedirect("/smbms/error.jsp");
        }
        chain.doFilter(req,resp);

    }

    @Override
    public void destroy() {

    }
}
