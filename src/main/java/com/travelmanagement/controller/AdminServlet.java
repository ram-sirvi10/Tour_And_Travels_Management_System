package com.travelmanagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.service.impl.AgencyServiceImpl;
import com.travelmanagement.service.impl.AuthServiceImpl;
import com.travelmanagement.service.impl.UserServiceImpl;
import com.travelmanagement.util.CloudinaryUtil;
import com.travelmanagement.util.PasswordHashing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/admin")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 5, // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserServiceImpl userService = new UserServiceImpl();
    private AgencyServiceImpl agencyService = new AgencyServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

            case "deletedAgencies":
                deletedAgencies(request, response);
                break;

            case "deletedUsers":
                deletedUsers(request, response);
                break;

            case "updateProfile":
                updateProfile(request, response);
                break;

            case "changePassword":
                changePassword(request, response);
                break;

            default:
                request.setAttribute("errorMessage", "Invalid action: " + action);
                request.getRequestDispatcher("template/admin/adminDashboard.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("template/admin/adminDashboard.jsp").forward(request, response);
        }
    }

    // ===================== changePassword =====================
    public void changePassword(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            String oldPassword = request.getParameter("oldPassword");
            HttpSession session = request.getSession();
            AuthServiceImpl authService = new AuthServiceImpl();
            UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
            UserResponseDTO dbUser = userService.getByEmail(user.getUserEmail());

            Map<String, String> errors = authService.validateChangePassword(newPassword, confirmPassword, oldPassword);

            if (oldPassword != null && !oldPassword.isEmpty()) {
                if (!PasswordHashing.checkPassword(oldPassword, dbUser.getUserPassword())) {
                    errors.put("oldPassword", "Old password is incorrect");
                } else if (oldPassword.equals(newPassword)) {
                    errors.put("newPassword", "New Password must be different from old password");
                }
            }

            if (!errors.isEmpty()) {
                request.setAttribute("actionType", "changePassword");
                request.setAttribute("errors", errors);
                if ("USER".equalsIgnoreCase(user.getUserRole())) {
                    request.getRequestDispatcher("template/user/profileManagement.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
                }
                return;
            }

            boolean updated = userService.changePassword(user.getUserId(), newPassword);
            if (updated) {
                request.setAttribute("successMessage", "Password changed successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to change password!");
            }

            request.setAttribute("actionType", "changePassword");
            if ("USER".equalsIgnoreCase(user.getUserRole())) {
                request.getRequestDispatcher("template/user/profileManagement.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
        }
    }

    // ===================== updateProfile =====================
    public void updateProfile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            HttpSession session = request.getSession();
            UserResponseDTO currentUser = (UserResponseDTO) session.getAttribute("user");

            RegisterRequestDTO dto = new RegisterRequestDTO();
            dto.setUserId(currentUser.getUserId());
            dto.setUsername(request.getParameter("userName"));
            dto.setEmail(request.getParameter("userEmail"));

            AuthServiceImpl authService = new AuthServiceImpl();
            Map<String, String> errors = authService.validateUpdateUser(dto);

            if (!request.getContentType().startsWith("multipart/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Form must be multipart/form-data");
                return;
            }

            String imageUrl = null;
            try {
                Part filePart = request.getPart("profileImage");
                imageUrl = CloudinaryUtil.uploadImage(filePart);
            } catch (Exception e) {
                e.printStackTrace();
                errors.put("profileImage", e.getMessage());
            }
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = currentUser.getImageurl();
            }
            dto.setImageurl(imageUrl);

            if (!errors.isEmpty()) {
                request.setAttribute("actionType", "updateProfile");
                request.setAttribute("errors", errors);
                request.setAttribute("formData", dto);
                if ("USER".equalsIgnoreCase(currentUser.getUserRole())) {
                    request.getRequestDispatcher("template/user/profileManagement.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
                }
                return;
            }

            boolean updated = userService.update(dto);
            if (updated) {
                UserResponseDTO updatedUser = userService.getById(dto.getUserId());
                session.setAttribute("user", updatedUser);
                request.setAttribute("successMessage", "Profile updated successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to update profile!");
            }

            request.setAttribute("actionType", "updateProfile");
            if ("USER".equalsIgnoreCase(currentUser.getUserRole())) {
                request.getRequestDispatcher("template/user/profileManagement.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
        }
    }

    // ===================== dashboard =====================
    private void dashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long totalUsers = userService.countUser(true, false, "");
            long totalAgencies = agencyService.countAgencies("APPROVED", null, false, "", null, null);
            long pendingAgencies = agencyService.countAgencies("PENDING", false, true, "", null, null);
            long rejectedAgencies = agencyService.countAgencies("REJECTED", false, true, "", null, null);

            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalAgencies", totalAgencies);
            request.setAttribute("pendingAgencies", pendingAgencies);
            request.setAttribute("rejectedAgencies", rejectedAgencies);

            request.getRequestDispatcher("template/admin/adminDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("template/admin/adminDashboard.jsp").forward(request, response);
        }
    }

    // ===================== manageUsers =====================
    private void manageUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String activeParam = request.getParameter("active");
            Boolean active = null;
            String keyword = request.getParameter("keyword");
            if (keyword != null) {
                keyword = keyword.trim();
            }

            if ("true".equals(activeParam)) {
                active = true;
            } else if ("false".equals(activeParam)) {
                active = false;
            }

            int page = 1;
            int pageSize = 10;
            try {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1)
                    page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }

            try {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
                if (pageSize < 1)
                    pageSize = 10;
            } catch (NumberFormatException e) {
                pageSize = 10;
            }

            int offset = (page - 1) * pageSize;
            List<UserResponseDTO> usersList = userService.getAll(active, false, keyword, pageSize, offset);
            List<Integer> validUserIds = new ArrayList<>();
            for (UserResponseDTO u : usersList)
                validUserIds.add(u.getUserId());
            session.setAttribute("validUserIds", validUserIds);
            long totalUsers = userService.countUser(active, false, keyword);
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            if (totalPages < 1)
                totalPages = 1;

            request.setAttribute("usersList", usersList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("keyword", keyword);
            request.setAttribute("listType", "Manage Users");

            request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
        }
    }

    // ===================== userAction =====================
    private void userAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String actionType = request.getParameter("action");
            String userIdStr = request.getParameter("userId");
            HttpSession session = request.getSession();
            UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");

            if (actionType != null && userIdStr != null) {
                int userId = Integer.parseInt(userIdStr);
                List<Integer> validIds = (List<Integer>) session.getAttribute("validUserIds");
                if (validIds == null || !validIds.contains(userId) || (user != null && user.getUserId() == userId)) {
                    request.setAttribute("errorMessage", "Invalid User ID or action not allowed.");
                    request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
                    return;
                }

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
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
        }
    }

    // ===================== manageAgencies =====================
    private void manageAgencies(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String status = request.getParameter("status");
            String activeParam = request.getParameter("active");
            Boolean active = null;
            if ("true".equals(activeParam))
                active = true;
            else if ("false".equals(activeParam))
                active = false;

            String keyword = request.getParameter("keyword");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            if (keyword != null)
                keyword = keyword.trim();

            int page = 1;
            int pageSize = 10;
            try {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1)
                    page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }

            try {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
                if (pageSize < 1 || pageSize > 50)
                    pageSize = 10;
            } catch (NumberFormatException e) {
                pageSize = 10;
            }

            int offset = (page - 1) * pageSize;
            List<AgencyResponseDTO> agenciesList = agencyService.filterAgencies(status, active, startDate, endDate,
                    keyword, false, pageSize, offset);

            List<Integer> validAgencyIds = new ArrayList<>();
            for (AgencyResponseDTO a : agenciesList)
                validAgencyIds.add(a.getAgencyId());
            session.setAttribute("validAgencyIds", validAgencyIds);

            long totalItems = agencyService.countAgencies(status, active, false, keyword, startDate, endDate);
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            if (totalPages < 1)
                totalPages = 1;

            request.setAttribute("agenciesList", agenciesList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("keyword", keyword);
            request.setAttribute("status", status);
            request.setAttribute("active", activeParam);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("listType", "Manage Agencies");

            request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
        }
    }

    // ===================== agencyAction =====================
    private void agencyAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String actionType = request.getParameter("action");
            String agencyIdStr = request.getParameter("agencyId");
            HttpSession session = request.getSession();

            if (actionType != null && agencyIdStr != null) {
                int agencyId = Integer.parseInt(agencyIdStr);
                List<Integer> validIds = (List<Integer>) session.getAttribute("validAgencyIds");
                if (validIds == null || !validIds.contains(agencyId)) {
                    request.setAttribute("errorMessage", "Invalid Agency ID provided.");
                    request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
                    return;
                }

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

            manageAgencies(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
        }
    }

    // ===================== pendingAgencies =====================
    private void pendingAgencies(HttpServletRequest request, HttpServletResponse response) {
        try {
            String status = request.getParameter("status");
            Boolean active = null;
            if (request.getParameter("active") != null) {
                active = Boolean.parseBoolean(request.getParameter("active"));
            }
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String keyword = request.getParameter("keyword");
            if (keyword != null) {
                keyword = keyword.trim();
            }
            if (status == null || status.isEmpty()) {
                status = "PENDING";
            }

            int page = 1;
            int pageSize = 10;
            String pageParam = request.getParameter("page");
            String sizeParam = request.getParameter("pageSize");

            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            if (sizeParam != null) {
                try {
                    pageSize = Integer.parseInt(sizeParam);
                    if (pageSize < 1) pageSize = 10;
                } catch (NumberFormatException e) {
                    pageSize = 10;
                }
            }

            int offset = (page - 1) * pageSize;

            long totalItems;
            if ("REJECTED".equalsIgnoreCase(status)) {
                totalItems = agencyService.countAgencies(status, false, true, keyword, startDate, endDate);
            } else {
                totalItems = agencyService.countAgencies(status, null, false, keyword, startDate, endDate);
            }

            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            if (totalPages < 1) totalPages = 1;

            List<AgencyResponseDTO> agenciesList = new ArrayList<>();
            if ("REJECTED".equalsIgnoreCase(status)) {
                agenciesList = agencyService.filterAgencies(status, false, startDate, endDate, keyword, true, pageSize, offset);
            } else if ("PENDING".equalsIgnoreCase(status)) {
                agenciesList = agencyService.filterAgencies(status, null, startDate, endDate, keyword, true, pageSize, offset);
            }

            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("keyword", keyword);
            request.setAttribute("status", status);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("agenciesList", agenciesList);
            request.setAttribute("listType", status + " Agencies");

            request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                request.setAttribute("errorMessage", e.getMessage());
                request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    // ===================== deletedAgencies =====================
    private void deletedAgencies(HttpServletRequest request, HttpServletResponse response) {
        try {
            String keyword = request.getParameter("keyword");
            if (keyword != null) {
                keyword = keyword.trim();
            }
            int page = 1;
            int pageSize = 10;
            try {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
            try {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
                if (pageSize < 1 || pageSize > 50) pageSize = 10;
            } catch (NumberFormatException e) {
                pageSize = 10;
            }
            int offset = (page - 1) * pageSize;

            long totalItems = agencyService.countAgencies("APPROVED", false, true, keyword, null, null);
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            if (totalPages < 1) totalPages = 1;

            List<AgencyResponseDTO> deletedAgencies = agencyService.getDeletedAgencies(keyword, pageSize, offset);

            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("keyword", keyword);
            request.setAttribute("agenciesList", deletedAgencies);
            request.setAttribute("listType", "Deleted Agencies");

            request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                request.setAttribute("errorMessage", e.getMessage());
                request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    // ===================== deletedUsers =====================
    private void deletedUsers(HttpServletRequest request, HttpServletResponse response) {
        try {
            String keyword = request.getParameter("keyword");
            if (keyword != null) {
                keyword = keyword.trim();
            }
            int page = 1;
            int pageSize = 10;
            String pageParam = request.getParameter("page");
            String sizeParam = request.getParameter("pageSize");

            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            if (sizeParam != null) {
                try {
                    pageSize = Integer.parseInt(sizeParam);
                    if (pageSize < 1) pageSize = 10;
                } catch (NumberFormatException e) {
                    pageSize = 10;
                }
            }

            int offset = (page - 1) * pageSize;

            List<UserResponseDTO> usersList = userService.getDeletedUsers(keyword, pageSize, offset);

            long totalUsers = userService.countUser(false, true, keyword);
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
            if (totalPages < 1) totalPages = 1;

            request.setAttribute("usersList", usersList);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("keyword", keyword);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("listType", "Deleted Users");

            request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                request.setAttribute("errorMessage", e.getMessage());
                request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
