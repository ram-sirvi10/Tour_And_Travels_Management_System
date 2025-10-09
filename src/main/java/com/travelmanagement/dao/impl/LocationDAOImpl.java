package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.ILocationDAO;

public class LocationDAOImpl implements ILocationDAO {

	@Override
	public List<String> getLocations(String type, String value) throws Exception {
		List<String> list = new ArrayList<>();
		String query = null;

		switch (type) {
		case "countries":
			query = "SELECT country_name FROM countries";
			break;
		case "states":
			query = "SELECT s.state_name FROM states s "
					+ "JOIN countries c ON s.country_id=c.country_id WHERE c.country_name=?";
			break;
		case "cities":
			query = "SELECT ci.city_name FROM cities ci "
					+ "JOIN states s ON ci.state_id=s.state_id WHERE s.state_name=?";
			break;
		case "areas":
			query = "SELECT a.area_name FROM areas a " + "JOIN cities ci ON a.city_id=ci.city_id WHERE ci.city_name=?";
			break;
		case "pincode":
			query = "SELECT p.pincode FROM pincodes p " + "JOIN areas a ON p.area_id=a.area_id WHERE a.area_name=?";
			break;
		default:
			return list;
		}

		try {
			Connection conn = DatabaseConfig.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			if (!type.equals("countries")) {
				ps.setString(1, value);
			}
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(rs.getString(1));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
