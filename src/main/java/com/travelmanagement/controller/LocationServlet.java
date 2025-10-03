package com.travelmanagement.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.travelmanagement.service.ILocationService;
import com.travelmanagement.service.impl.LocationServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/location")
public class LocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ILocationService service = new LocationServiceImpl();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		String type = request.getParameter("type");
		String value = request.getParameter("value");

		try {
			List<String> resultList = service.getLocations(type, value);

			String json = "[" + String.join(",", resultList.stream().map(s -> "\"" + s + "\"").toList()) + "]";
			out.print(json);

		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"error\":\"Database error\"}");
		}
	}
}
