package com.travelmanagement.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.travelmanagement.model.User;
import com.travelmanagement.model.Agency;

import java.io.IOException;

//@WebFilter(urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // Nothing needed here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();
        System.out.println("FILTER URI ==> "+uri);

        // Public pages which should be accessible without login
        boolean isPublicPage = uri.endsWith("login.jsp") || uri.endsWith("index.jsp") ||
                               uri.endsWith("registerUser.jsp") || uri.endsWith("registerAgency.jsp");

        System.out.println("ISPUBLICPAGE ==> "+isPublicPage);
        if (session != null && session.getAttribute("user") != null) {
            Object obj = session.getAttribute("user");

            // If logged in user tries to access public pages, redirect to their dashboard
            if (isPublicPage) {
                if (obj instanceof User) {
                    User user = (User) obj;
                    if ("ADMIN".equalsIgnoreCase(user.getUserRole())) {
                        res.sendRedirect(req.getContextPath() + "/template/admin/adminDashboard.jsp");
                        return;
                    } else {
                        res.sendRedirect(req.getContextPath() + "/template/user/userDashboard.jsp");
                        return;
                    }
                } else if (obj instanceof Agency) {
                    res.sendRedirect(req.getContextPath() + "/template/agency/agencyDashboard.jsp");
                    return;
                }
            }

        
            if (uri.startsWith(req.getContextPath() + "/template/admin/")) {
                if (obj instanceof User && "ADMIN".equalsIgnoreCase(((User) obj).getUserRole())) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(req.getContextPath() + "/login.jsp");
                }
            } else if (uri.startsWith(req.getContextPath() + "/template/user/")) {
                if (obj instanceof User && !"ADMIN".equalsIgnoreCase(((User) obj).getUserRole())) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(req.getContextPath() + "/login.jsp");
                }
            } else if (uri.startsWith(req.getContextPath() + "/template/agency/")) {
                if (obj instanceof Agency) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(req.getContextPath() + "/login.jsp");
                }
            } else {
                // Any other URLs
                chain.doFilter(request, response);
            }

        } else {
            // User not logged in
            if (isPublicPage) {
                chain.doFilter(request, response); // Allow public pages
            } else {
                res.sendRedirect(req.getContextPath() + "/login.jsp");
            }
        }
    }

    @Override
    public void destroy() {
        // Nothing needed here
    }
}
