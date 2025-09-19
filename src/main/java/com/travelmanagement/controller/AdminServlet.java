package com.travelmanagement.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.service.impl.UserServiceImpl;
import com.travelmanagement.service.impl.AgencyServiceImpl;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserServiceImpl userService = new UserServiceImpl();
    private AgencyServiceImpl agencyService = new AgencyServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("button");
        System.out.println("ADMIN ACTION => " + action);

        try {
            switch (action) {
                case "dashboard":
                    dashboard(request, response);
                    break;

                case "manageUsers":
                    manageUsers(request, response);
                    break;

                case "userAction": 
                    userAction(request, response);
                    break;

                case "manageAgencies":
                    manageAgencies(request, response);
                    break;

                case "agencyAction":
                    agencyAction(request, response);
                    break;
                case "pendingAgencies":
                	
                	pendingAgencies(request, response);
                  
                    break;

                default:
                    throw new IllegalArgumentException("Unexpected value: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("template/admin/adminDashboard.jsp");
        }
    }

    private void pendingAgencies(HttpServletRequest request, HttpServletResponse response)throws Exception  {
    	  List<AgencyResponseDTO> pendingAgencies = agencyService.getAgenciesByStatus("PENDING", 100, 0);
          request.setAttribute("agenciesList", pendingAgencies);
          System.out.println("ADmin servlet panding agency request==="+pendingAgencies.toString());
          request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
	}


    private void dashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("template/admin/adminDashboard.jsp").forward(request, response);
    }

   
    private void manageUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<UserResponseDTO> usersList = userService.getAll(1000, 0); 
        request.setAttribute("usersList", usersList);
        request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
    }

 
    private void userAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String actionType = request.getParameter("action");
        String userIdStr = request.getParameter("userId");
        if (actionType != null && userIdStr != null) {
            int userId = Integer.parseInt(userIdStr);
            switch (actionType) {
                case "activate":
                    userService.updateUserActiveState(userId, true);
                    break;
                case "deactivate":
                    userService.updateUserActiveState(userId, false);
                    break;
                case "delete":
                    userService.delete(userId);
                    break;
               
    
                default:
                    break;
            }
        }
        manageUsers(request, response);
    }

  
    private void manageAgencies(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<AgencyResponseDTO> agenciesList = agencyService.getAllAgencies(1000, 0);
        request.setAttribute("agenciesList", agenciesList);
        request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
    }

 
    private void agencyAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String actionType = request.getParameter("action");
        String agencyIdStr = request.getParameter("agencyId");

        if (actionType != null && agencyIdStr != null) {
            int agencyId = Integer.parseInt(agencyIdStr);

            switch (actionType) {
                case "activate":
                    agencyService.updateAgencyActiveState(agencyId, true);
                    break;
                case "deactivate":
                    agencyService.updateAgencyActiveState(agencyId, false);
                    break;
                case "delete":
                    agencyService.deleteAgency(agencyId);
                    break;
                case "approve":
                    agencyService.updateAgencyStatus(agencyId, "APPROVED"); 
                    break;
                case "reject":
                    agencyService.updateAgencyStatus(agencyId, "REJECTED"); 
                    break;
                default:
                    break;
            }
        }
        if ("approve".equalsIgnoreCase(actionType)||"reject".equalsIgnoreCase(actionType)) {
            pendingAgencies(request, response);
        } else {
            manageAgencies(request, response);
        }
    }


}
