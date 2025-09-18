package com.travelmanagement.filter;

import java.io.IOException;

import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;

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

//@WebFilter(urlPatterns = { "/*" })
//public class AuthenticationFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig fConfig) throws ServletException {
//        // Nothing needed here
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//        HttpSession session = req.getSession(false);
//        String uri = req.getRequestURI();
//        System.out.println("FILTER URI ==> " + uri);
//
//        // Allow AuthServlet always (login, logout, register handled here)
//        if (uri.contains("/AuthServlet")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // Public pages (accessible without login)
//        boolean isPublicPage = uri.contains("login.jsp") || uri.contains("index.jsp") ||
//                               uri.contains("registerUser.jsp") || uri.contains("registerAgency.jsp");
//
//        if (session != null && session.getAttribute("user") != null) {
//            Object obj = session.getAttribute("user");
//
//            // If logged in user tries to access public pages, redirect to their dashboard
//            if (isPublicPage) {
//                if (obj instanceof UserResponseDTO) {
//                	UserResponseDTO user = (UserResponseDTO) obj;
//                    if ("ADMIN".equalsIgnoreCase(user.getUserRole())) {
//                        res.sendRedirect(req.getContextPath() + "/template/admin/adminDashboard.jsp");
//                        return;
//                    } else {
//                        res.sendRedirect(req.getContextPath() + "/template/user/userDashboard.jsp");
//                        return;
//                    }
//                } else if (obj instanceof AgencyResponseDTO) {
//                    res.sendRedirect(req.getContextPath() + "/template/agency/agencyDashboard.jsp");
//                    return;
//                }
//            }
//
//            // Role-based access control
//            if (uri.contains("/template/admin/")) {
//                if (obj instanceof UserResponseDTO && "ADMIN".equalsIgnoreCase(((UserResponseDTO) obj).getUserRole())) {
//                    chain.doFilter(request, response);
//                } else {
//                    res.sendRedirect(req.getContextPath() + "/index.jsp");
//                }
//            } else if (uri.contains("/template/user/")) {
//                if (obj instanceof UserResponseDTO && !"ADMIN".equalsIgnoreCase(((UserResponseDTO) obj).getUserRole())) {
//                    chain.doFilter(request, response);
//                } else {
//                    res.sendRedirect(req.getContextPath() + "/index.jsp");
//                }
//            } else if (uri.contains("/template/agency/")) {
//                if (obj instanceof AgencyResponseDTO) {
//                    chain.doFilter(request, response);
//                } else {
//                    res.sendRedirect(req.getContextPath() + "/index.jsp");
//                }
//            } else {
//                // Any other URLs
//                chain.doFilter(request, response);
//            }
//
//        } else {
//            // User not logged in
//            if (isPublicPage) {
//                chain.doFilter(request, response); // Allow public pages
//            } else {
//                res.sendRedirect(req.getContextPath() + "/index.jsp");
//            }
//        }
//    }
//
//
//    @Override
//    public void destroy() {
//        // Nothing needed here
//    }
//}

//@WebFilter(urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // nothing needed here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();

        System.out.println("FILTER URI ==> " + uri);

        // ✅ Always allow AuthServlet (for login & logout)
        if (uri.contains("/AuthServlet")) {
            chain.doFilter(request, response);
            return;
        }

        // public pages
        boolean isPublicPage = uri.endsWith("login.jsp") || uri.endsWith("index.jsp")
                || uri.endsWith("registerUser.jsp") || uri.endsWith("registerAgency.jsp");

        UserResponseDTO user = (session != null) ? (UserResponseDTO) session.getAttribute("user") : null;
        AgencyResponseDTO agency = (session != null) ? (AgencyResponseDTO) session.getAttribute("agency") : null;

        if (user != null || agency != null) {
            // prevent access to login/register if already logged in
            if (isPublicPage) {
                if (user != null) {
                    if ("ADMIN".equalsIgnoreCase(user.getUserRole())) {
                        res.sendRedirect(req.getContextPath() + "/template/admin/adminDashboard.jsp");
                        return;
                    } else {
                        res.sendRedirect(req.getContextPath() + "/template/user/userDashboard.jsp");
                        return;
                    }
                } else if (agency != null) {
                    res.sendRedirect(req.getContextPath() + "/template/agency/agencyDashboard.jsp");
                    return;
                }
            }

            // role-based access control
            if (uri.contains("/template/admin/")) {
                if (user != null && "ADMIN".equalsIgnoreCase(user.getUserRole())) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(req.getContextPath() + "/index.jsp");
                }
            } else if (uri.contains("/template/user/")) {
                if (user != null && !"ADMIN".equalsIgnoreCase(user.getUserRole())) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(req.getContextPath() + "/index.jsp");
                }
            } else if (uri.contains("/template/agency/")) {
                if (agency != null) {
                    chain.doFilter(request, response);
                } else {
                    res.sendRedirect(req.getContextPath() + "/index.jsp");
                }
            } else {
                chain.doFilter(request, response); // allow other resources
            }

        } else {
            // not logged in
            if (isPublicPage) {
                chain.doFilter(request, response); // allow public pages
            } else {
                res.sendRedirect(req.getContextPath() + "/index.jsp");
            }
        }
    }

    @Override
    public void destroy() {
        // nothing needed here
    }
}
