package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.IAgencyDAO;
import com.travelmanagement.model.Agency;

public class AgencyDAOImpl implements IAgencyDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public boolean createAgency(Agency agency) throws Exception {
        try {
            connection = DatabaseConfig.getConnection();
            String sql = "INSERT INTO travelAgency (agency_name, owner_name, email, phone, city, state, country, pincode, registration_number, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, agency.getAgencyName());
            preparedStatement.setString(2, agency.getOwnerName());
            preparedStatement.setString(3, agency.getEmail());
            preparedStatement.setString(4, agency.getPhone());
            preparedStatement.setString(5, agency.getCity());
            preparedStatement.setString(6, agency.getState());
            preparedStatement.setString(7, agency.getCountry());
            preparedStatement.setString(8, agency.getPincode());
            preparedStatement.setString(9, agency.getRegistrationNumber());
            preparedStatement.setString(10, agency.getPassword());
         

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Agency getAgencyByEmail(String email) throws Exception {
        Agency agency = null;
        try {
            connection = DatabaseConfig.getConnection();
            String sql = "SELECT * FROM travelAgency WHERE email=? AND is_delete=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setBoolean(2, false);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                agency = new Agency();
                agency.setAgencyId(resultSet.getInt("agency_id"));
                agency.setAgencyName(resultSet.getString("agency_name"));
                agency.setOwnerName(resultSet.getString("owner_name"));
                agency.setEmail(resultSet.getString("email"));
                agency.setPhone(resultSet.getString("phone"));
                agency.setCity(resultSet.getString("city"));
                agency.setState(resultSet.getString("state"));
                agency.setCountry(resultSet.getString("country"));
                agency.setPincode(resultSet.getString("pincode"));
                agency.setRegistrationNumber(resultSet.getString("registration_number"));
                agency.setPassword(resultSet.getString("password"));
                agency.setActive(resultSet.getBoolean("is_active"));
                agency.setDelete(resultSet.getBoolean("is_delete"));
                if (resultSet.getDate("created_at") != null)
                    agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
                if (resultSet.getDate("updated_at") != null)
                    agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agency;
    }

    @Override
    public Agency getAgencyById(int id) throws Exception {
        Agency agency = null;
        try {
            connection = DatabaseConfig.getConnection();
            String sql = "SELECT * FROM travelAgency WHERE agency_id=? AND is_delete=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setBoolean(2, false);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                agency = new Agency();
                agency.setAgencyId(resultSet.getInt("agency_id"));
                agency.setAgencyName(resultSet.getString("agency_name"));
                agency.setOwnerName(resultSet.getString("owner_name"));
                agency.setEmail(resultSet.getString("email"));
                agency.setPhone(resultSet.getString("phone"));
                agency.setCity(resultSet.getString("city"));
                agency.setState(resultSet.getString("state"));
                agency.setCountry(resultSet.getString("country"));
                agency.setPincode(resultSet.getString("pincode"));
                agency.setRegistrationNumber(resultSet.getString("registration_number"));
                agency.setPassword(resultSet.getString("password"));
                agency.setActive(resultSet.getBoolean("is_active"));
                agency.setDelete(resultSet.getBoolean("is_delete"));
                if (resultSet.getDate("created_at") != null)
                    agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
                if (resultSet.getDate("updated_at") != null)
                    agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agency;
    }

    @Override
    public boolean updateAgency(Agency agency) throws Exception {
        try {
            connection = DatabaseConfig.getConnection();
            String sql = "UPDATE travelAgency SET agency_name=?, owner_name=?, email=?, phone=?, city=?, state=?, country=?, pincode=?, registration_number=?, password=? WHERE agency_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, agency.getAgencyName());
            preparedStatement.setString(2, agency.getOwnerName());
            preparedStatement.setString(3, agency.getEmail());
            preparedStatement.setString(4, agency.getPhone());
            preparedStatement.setString(5, agency.getCity());
            preparedStatement.setString(6, agency.getState());
            preparedStatement.setString(7, agency.getCountry());
            preparedStatement.setString(8, agency.getPincode());
            preparedStatement.setString(9, agency.getRegistrationNumber());
            preparedStatement.setString(10, agency.getPassword());
            preparedStatement.setInt(11, agency.getAgencyId());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAgency(int id) throws Exception {
        try {
            connection = DatabaseConfig.getConnection();
            String sql = "UPDATE travelAgency SET is_delete=? , is_active=? WHERE agency_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setBoolean(2, false);
            preparedStatement.setInt(3, id);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean approveAgency(int agencyId) throws Exception {
        try {
            connection = DatabaseConfig.getConnection();
            String sql = "UPDATE travelAgency SET status = ? WHERE agency_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "APPROVED");
 
            preparedStatement.setInt(2, agencyId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean declineAgency(int agencyId) throws Exception {
    	try {
            connection = DatabaseConfig.getConnection();
            String sql = "UPDATE travelAgency SET status = ? WHERE agency_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "REJECTED");
            preparedStatement.setInt(2, agencyId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean enableAgency(int agencyId) throws Exception {
        try {
            connection = DatabaseConfig.getConnection();
            String sql = "UPDATE travelAgency SET is_active=? WHERE agency_id=? AND is_delete=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, agencyId);
            preparedStatement.setBoolean(3, false);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean disableAgency(int agencyId) throws Exception {
        try {
            connection = DatabaseConfig.getConnection();
            String sql = "UPDATE travelAgency SET is_active=? WHERE agency_id=? AND is_delete=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setInt(2, agencyId);
            preparedStatement.setBoolean(3, false);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0; 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

	
	


	@Override
	public List<Agency> searchAgencies(String keyword, int limit, int offset) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Agency> getAgenciesByStatus(String status, int limit, int offset) throws Exception {
		 List<Agency> pendingList = new ArrayList<>();
	        try {
	            connection = DatabaseConfig.getConnection();
	            String sql = "SELECT * FROM travelAgency  WHERE  status = ? limit ? offset ?";
	            preparedStatement = connection.prepareStatement(sql);
	        
	            preparedStatement.setString(1, status);
	            preparedStatement.setInt(2, limit);
	            preparedStatement.setInt(3, offset);
	            resultSet = preparedStatement.executeQuery();

	            while (resultSet.next()) {
	                Agency agency = new Agency();
	                agency.setAgencyId(resultSet.getInt("agency_id"));
	                agency.setAgencyName(resultSet.getString("agency_name"));
	                agency.setOwnerName(resultSet.getString("owner_name"));
	                agency.setEmail(resultSet.getString("email"));
	                agency.setPhone(resultSet.getString("phone"));
	                agency.setCity(resultSet.getString("city"));
	                agency.setState(resultSet.getString("state"));
	                agency.setCountry(resultSet.getString("country"));
	                agency.setPincode(resultSet.getString("pincode"));
	                agency.setRegistrationNumber(resultSet.getString("registration_number"));
	                agency.setPassword(resultSet.getString("password"));
	                agency.setActive(resultSet.getBoolean("is_active"));
	                agency.setDelete(resultSet.getBoolean("is_delete"));
	                if (resultSet.getDate("created_at") != null)
	                    agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
	                if (resultSet.getDate("updated_at") != null)
	                    agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());

	                pendingList.add(agency);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return pendingList;
	}

	@Override
	public List<Agency> getAgenciesByStatus(boolean key, int limit, int offset) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countAgencies(String key) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Agency> getPendingAgencies(int limit, int offset) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

    
}
