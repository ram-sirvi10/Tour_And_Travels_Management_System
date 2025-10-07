package com.travelmanagement.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dto.requestDTO.PackageRegisterDTO;
import com.travelmanagement.dto.requestDTO.PackageScheduleRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageScheduleResponseDTO;
import com.travelmanagement.service.impl.AuthServiceImpl;
import com.travelmanagement.service.impl.BookingServiceImpl;
import com.travelmanagement.service.impl.PackageServiceImpl;
import com.travelmanagement.service.impl.TravelerServiceImpl;
import com.travelmanagement.util.Constants;
import com.travelmanagement.util.Mapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/agency")
public class AgencyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PackageServiceImpl packageService = new PackageServiceImpl();
	private BookingServiceImpl bookingService = new BookingServiceImpl();
	private TravelerServiceImpl travelerService = new TravelerServiceImpl();
	private AuthServiceImpl authServiceImpl = new AuthServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("button");
		if (action == null || action.isEmpty()) {
			action = "dashboard";
		}

		AgencyResponseDTO agency = (AgencyResponseDTO) request.getSession().getAttribute("agency");
		if (agency == null) {
			response.sendRedirect("login.jsp");
			return;
		}

		try {
			switch (action) {
			case "dashboard":
				showDashboard(request, response, agency);
				break;
			case "addPackage":
				addPackage(request, response, agency);
				break;
			case "viewPackages":
				viewPackages(request, response, agency);
				break;
			case "viewBookings":
				viewBookings(request, response, agency);
				break;
			case "packageAction":
				packageAction(request, response, agency);
				break;
			case "editPackageForm":
				editPackageForm(request, response, agency);
				break;
			case "updatePackage":
				updatePackage(request, response, agency);
				break;

			default:
				showDashboard(request, response, agency);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Something went wrong: " + e.getMessage());
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

	private void editPackageForm(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws ServletException, IOException {
		String packageIdParam = request.getParameter("id");
		if (packageIdParam == null || packageIdParam.isEmpty()) {
			request.getSession().setAttribute("errorMessage", "Package ID is missing.");
			response.sendRedirect("agency?button=viewPackages");
			return;
		}

		try {
			int packageId = Integer.parseInt(packageIdParam);
			PackageResponseDTO packageData = packageService.getPackageById(packageId);

			if (packageData == null || packageData.getAgencyId() != agency.getAgencyId()) {
				request.getSession().setAttribute("errorMessage", "Package not found or unauthorized.");
				response.sendRedirect("agency?button=viewPackages");
				return;
			}

			List<PackageScheduleResponseDTO> scheduleList = packageService.getScheduleByPackage(packageId);

			request.setAttribute("packageData", packageData);
			request.setAttribute("scheduleList", scheduleList);

			request.getRequestDispatcher("template/agency/addPackage.jsp").forward(request, response);
			return;
		} catch (NumberFormatException e) {
			request.getSession().setAttribute("errorMessage", "Invalid package ID format.");
			response.sendRedirect("agency?button=viewPackages");
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errorMessage", "Something went wrong while fetching package.");
			response.sendRedirect("agency?button=viewPackages");
		}
	}

	private void updatePackage(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {

		String packageIdParam = request.getParameter("packageId");
		if (packageIdParam == null || packageIdParam.isEmpty()) {
			request.getSession().setAttribute("errorMessage", "Package ID missing.");
			response.sendRedirect("agency?button=viewPackages");
			return;
		}

		int packageId = Integer.parseInt(packageIdParam);
		PackageResponseDTO existingPackage = packageService.getPackageById(packageId);

		if (existingPackage == null || existingPackage.getAgencyId() != agency.getAgencyId()) {
			request.getSession().setAttribute("errorMessage", "Unauthorized action!");
			response.sendRedirect("agency?button=viewPackages");
			return;
		}

		boolean isScheduleSubmit = "true".equals(request.getParameter("isScheduleSubmit"));

		PackageRegisterDTO dto = new PackageRegisterDTO();
		dto.setPackageId(existingPackage.getPackageId());
		dto.setAgencyId(agency.getAgencyId());
		dto.setTitle(trimOrNull(request.getParameter("title")));
		dto.setLocation(trimOrNull(request.getParameter("location")));
		dto.setPrice(request.getParameter("price") != null && !request.getParameter("price").isEmpty()
				? Double.parseDouble(request.getParameter("price"))
				: null);
		dto.setDuration(request.getParameter("duration") != null && !request.getParameter("duration").isEmpty()
				? Integer.parseInt(request.getParameter("duration"))
				: null);
		dto.setTotalSeats(request.getParameter("totalseats") != null && !request.getParameter("totalseats").isEmpty()
				? Integer.parseInt(request.getParameter("totalseats"))
				: null);
		dto.setDescription(trimOrNull(request.getParameter("description")));
		dto.setIsActive("on".equals(request.getParameter("isActive")));
		dto.setDepartureDate(
				request.getParameter("departure_date") != null && !request.getParameter("departure_date").isEmpty()
						? LocalDateTime.parse(request.getParameter("departure_date"))
						: null);
		dto.setLastBookingDate(request.getParameter("last_booking_date") != null
				&& !request.getParameter("last_booking_date").isEmpty()
						? LocalDateTime.parse(request.getParameter("last_booking_date"))
						: null);

		List<PackageScheduleRequestDTO> schedules = new ArrayList<>();
		if (dto.getDuration() != null) {
			for (int i = 1; i <= dto.getDuration(); i++) {
				PackageScheduleRequestDTO s = new PackageScheduleRequestDTO();
				s.setPackageId(packageId);
				s.setDayNumber(i);
				s.setActivity(trimOrNull(request.getParameter("day" + i + "_activity")));
				s.setDescription(trimOrNull(request.getParameter("day" + i + "_desc")));
				schedules.add(s);
			}
		}
		dto.setPackageSchedule(schedules);

		if (!isScheduleSubmit) {
			PackageResponseDTO respDto = Mapper.convertToResponseDTO(dto, schedules);
			request.setAttribute("packageData", respDto);
			request.setAttribute("scheduleList", respDto.getPackageSchedule());
			request.getRequestDispatcher("template/agency/addPackage.jsp").forward(request, response);
			return;
		}

		Map<String, String> errors = authServiceImpl.validatePackageFields(dto);
		if (!errors.isEmpty()) {
			PackageResponseDTO respDto = Mapper.convertToResponseDTO(dto, schedules);
			request.setAttribute("errors", errors);
			request.setAttribute("packageData", respDto);
			request.setAttribute("scheduleList", respDto.getPackageSchedule());
			request.getRequestDispatcher("template/agency/addPackage.jsp").forward(request, response);
			return;
		}
		System.err.println(dto);
		boolean updated = packageService.updatePackage(dto);
		if (updated) {
			request.getSession().setAttribute("successMessage", "Package updated successfully!");
		} else {
			request.getSession().setAttribute("errorMessage", "Failed to update package!");
		}

		response.sendRedirect("agency?button=viewPackages");
	}

	private void packageAction(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {

		String actionType = request.getParameter("actionType");
		String packageIdParam = request.getParameter("packageId");

		if (actionType == null || actionType.trim().isEmpty()) {
			request.getSession().setAttribute("errorMessage", "Invalid action: action type missing.");
			response.sendRedirect("agency?button=viewPackages");
			return;
		}

		if (packageIdParam == null || packageIdParam.trim().isEmpty()) {
			request.getSession().setAttribute("errorMessage", "Invalid action: package ID missing.");
			response.sendRedirect("agency?button=viewPackages");
			return;
		}

		int packageId = 0;
		try {
			packageId = Integer.parseInt(packageIdParam);
		} catch (NumberFormatException e) {
			request.getSession().setAttribute("errorMessage", "Invalid package ID format.");
			response.sendRedirect("agency?button=viewPackages");
			return;
		}

		PackageResponseDTO packageBelongsToAgency = packageService.getPackageById(packageId);

		if (packageBelongsToAgency.getAgencyId() != agency.getAgencyId()) {
			request.getSession().setAttribute("errorMessage", "You are not authorized to modify this package.");
			response.sendRedirect("agency?button=viewPackages");
			return;
		}

		try {
			switch (actionType.toLowerCase()) {
			case "toggle":
				boolean toggled = packageService.togglePackageStatus(packageId);
				if (toggled)
					request.getSession().setAttribute("successMessage", "Package status updated successfully.");
				else
					request.getSession().setAttribute("errorMessage", "Failed to update package status.");
				break;

			case "delete":
				boolean deleted = packageService.deletePackage(packageId);
				if (deleted)
					request.getSession().setAttribute("successMessage", "Package deleted successfully.");
				else
					request.getSession().setAttribute("errorMessage", "Failed to delete package.");
				break;

			default:
				request.getSession().setAttribute("errorMessage", "Invalid action type: " + actionType);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errorMessage", "Something went wrong: " + e.getMessage());
		}

		String keyword = trimOrNull(request.getParameter("keyword"));
//		String location = trimOrNull(request.getParameter("location"));
//		String dateFrom = trimOrNull(request.getParameter("dateFrom"));
//		String dateTo = trimOrNull(request.getParameter("dateTo"));
		String page = trimOrNull(request.getParameter("page"));
		String pageSize = trimOrNull(request.getParameter("pageSize"));
		String active = trimOrNull(request.getParameter("active"));

		StringBuilder redirectURL = new StringBuilder("agency?button=viewPackages");

		if (keyword != null && !keyword.isEmpty())
			redirectURL.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
//		if (location != null && !location.isEmpty())
//			redirectURL.append("&location=").append(URLEncoder.encode(location, "UTF-8"));
//		if (dateFrom != null && !dateFrom.isEmpty())
//			redirectURL.append("&dateFrom=").append(URLEncoder.encode(dateFrom, "UTF-8"));
//		if (dateTo != null && !dateTo.isEmpty())
//			redirectURL.append("&dateTo=").append(URLEncoder.encode(dateTo, "UTF-8"));
		if (page != null && !page.isEmpty())
			redirectURL.append("&page=").append(page);
		if (pageSize != null && !pageSize.isEmpty())
			redirectURL.append("&pageSize=").append(pageSize);
		if (active != null && !active.isEmpty())
			redirectURL.append("&active=").append(active);

		response.sendRedirect(redirectURL.toString());

	}

	private void showDashboard(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {
		int totalPackages = packageService.countPackages(null, agency.getAgencyId(), null, null, null, null, null, null,
				true);
		int activePackages = packageService.countPackages(null, agency.getAgencyId(), null, null, null, null, null,
				true, true);
		int totalBookings = bookingService.getAllBookingsCount(agency.getAgencyId(), null, null, null, null, null,
				null);
		request.setAttribute("totalPackages", totalPackages);
		request.setAttribute("activePackages", activePackages);
		request.setAttribute("totalBookings", totalBookings);
		request.getRequestDispatcher("template/agency/agencyDashboard.jsp").forward(request, response);
	}

	private void addPackage(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {
		String isScheduleSubmitParam = request.getParameter("isScheduleSubmit");
		boolean isScheduleSubmit = "true".equals(isScheduleSubmitParam);

		PackageRegisterDTO packageRegisterDTO = new PackageRegisterDTO();
		packageRegisterDTO.setTitle(request.getParameter("title"));
		packageRegisterDTO.setLocation(request.getParameter("location"));
		packageRegisterDTO.setPrice(request.getParameter("price") != null && !request.getParameter("price").isEmpty()
				? Double.parseDouble(request.getParameter("price"))
				: null);
		packageRegisterDTO
				.setDuration(request.getParameter("duration") != null && !request.getParameter("duration").isEmpty()
						? Integer.parseInt(request.getParameter("duration"))
						: null);
		packageRegisterDTO.setTotalSeats(
				request.getParameter("totalseats") != null && !request.getParameter("totalseats").isEmpty()
						? Integer.parseInt(request.getParameter("totalseats"))
						: null);
		packageRegisterDTO.setDescription(request.getParameter("description"));
		packageRegisterDTO.setAgencyId(agency.getAgencyId());

		boolean isActive = "on".equals(request.getParameter("isActive"));
		packageRegisterDTO.setIsActive(isActive);

		if (request.getParameter("departure_date") != null && !request.getParameter("departure_date").isEmpty())
			packageRegisterDTO.setDepartureDate(LocalDateTime.parse(request.getParameter("departure_date")));
		if (request.getParameter("last_booking_date") != null && !request.getParameter("last_booking_date").isEmpty())
			packageRegisterDTO.setLastBookingDate(LocalDateTime.parse(request.getParameter("last_booking_date")));

		List<PackageScheduleRequestDTO> schedules = new ArrayList<>();
		if (packageRegisterDTO.getDuration() != null) {
			for (int i = 1; i <= packageRegisterDTO.getDuration(); i++) {
				PackageScheduleRequestDTO schedule = new PackageScheduleRequestDTO();
				schedule.setDayNumber(i);
				schedule.setActivity(request.getParameter("day" + i + "_activity"));
				schedule.setDescription(request.getParameter("day" + i + "_desc"));
				schedules.add(schedule);
			}
		}
		packageRegisterDTO.setPackageSchedule(schedules);

		if (!isScheduleSubmit) {
//			request.setAttribute("oldData", packageRegisterDTO);
			request.getRequestDispatcher("template/agency/addPackage.jsp").forward(request, response);

			return;
		}

		Map<String, String> errors = authServiceImpl.validatePackageFields(packageRegisterDTO);
		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.setAttribute("oldData", packageRegisterDTO);
			request.getRequestDispatcher("template/agency/addPackage.jsp").forward(request, response);
			return;
		}

		int packageId = packageService.addPackage(packageRegisterDTO);
		response.sendRedirect("agency?button=managePackages");
		return;
	}

	private void viewPackages(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {

		String keyword = trimOrNull(request.getParameter("keyword"));
		String title = trimOrNull(request.getParameter("title"));
		String location = trimOrNull(request.getParameter("location"));
		String startDate = trimOrNull(request.getParameter("dateFrom"));
		String endDate = trimOrNull(request.getParameter("dateTo"));
		String departureDate = trimOrNull(request.getParameter("departureDate"));

		String activeParam = request.getParameter("active");
		Boolean active = null;
		if ("true".equals(activeParam))
			active = true;
		else if ("false".equals(activeParam))
			active = false;
		int page = parseIntSafe(request.getParameter("page"), 1);
		int limit = parseIntSafe(request.getParameter("pageSize"), 10);
		if (limit <= 0)
			limit = 10;
		if (page < 1)
			page = 1;
		int offset = (page - 1) * limit;
		LocalDate start = null, end = null;
		try {
			if (startDate != null && !startDate.isEmpty())
				start = LocalDate.parse(startDate);
			if (endDate != null && !endDate.isEmpty())
				end = LocalDate.parse(endDate);
		} catch (Exception e) {

		}

		if (start != null && end != null && start.isAfter(end)) {
			request.setAttribute("errorMessage", Constants.ERROR_START_AFTER_END_DATE);
			start = null;
			end = null;
		}

		List<PackageResponseDTO> packages = packageService.searchPackages(null, agency.getAgencyId(), null, keyword,
				start != null ? start.toString() : null, end != null ? end.toString() : null, null, active, limit,
				offset, true);
		for (PackageResponseDTO pkg : packages) {
			List<PackageScheduleResponseDTO> schedule = packageService.getScheduleByPackage(pkg.getPackageId());
			System.out.println("View Package --->  ");
			System.out.println("Package Schedule --> " + schedule);
			pkg.setPackageSchedule(schedule);
		}

		int totalPackages = packageService.countPackages(title, agency.getAgencyId(), location, keyword,
				start != null ? start.toString() : null, end != null ? end.toString() : null, null, active, true);
		System.out.println("Manage package Total pakages = -> " + totalPackages);
		int totalPages = (int) Math.ceil((double) totalPackages / limit);
		System.out.println("Manage package Total page = -> " + totalPages);
		request.setAttribute("packages", packages);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("pageSize", limit);
		request.setAttribute("keyword", keyword);
		request.setAttribute("title", title);
		request.setAttribute("location", location);
		request.setAttribute("dateFrom", startDate);
		request.setAttribute("dateTo", endDate);
		request.setAttribute("departureDate", departureDate);
		request.getRequestDispatcher("template/agency/managePackages.jsp").forward(request, response);
		return;
	}

	private void viewBookings(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {

		String packageIdParam = request.getParameter("packageId");
		Integer packageId = null;
		if (packageIdParam != null && !packageIdParam.isEmpty()) {
			packageId = Integer.parseInt(packageIdParam);
		}

		List<BookingResponseDTO> bookings = bookingService.getAllBookings(agency.getAgencyId(), null, packageId, null,
				null, null, null, 50, 0);

		request.setAttribute("bookings", bookings);
		request.setAttribute("packageId", packageId);
		request.getRequestDispatcher("template/agency/manageBooking.jsp").forward(request, response);
	}

	private String trimOrNull(String param) {
		return (param != null && !param.trim().isEmpty()) ? param.trim() : null;
	}

	private Integer parseIntSafe(String param, Integer defaultValue) {
		try {
			if (param != null && !param.trim().isEmpty()) {
				return Integer.parseInt(param.trim());
			}
		} catch (NumberFormatException e) {
			return defaultValue;
		}
		return defaultValue;
	}
}
