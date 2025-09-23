package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.Agency;

public interface IAgencyDAO {
	boolean createAgency(Agency agency) throws Exception;

	Agency getAgencyById(int id) throws Exception;

	boolean updateAgency(Agency agency) throws Exception;

	boolean deleteAgency(int id) throws Exception;

	List<Agency> getAgenciesByStatus(String status, int limit, int offset) throws Exception;

	List<Agency> getAgenciesByActiveState(boolean key, int limit, int offset) throws Exception;

	List<Agency> getAllAgencies(int limit, int offset);

	boolean updateAgencyStatus(int agencyId, String status) throws Exception;

	boolean updateAgencyActiveState(int agencyId, boolean active) throws Exception;

	Agency getAgencyByField(String fieldName, String value) throws Exception;

	List<Agency> searchAgenciesByKeyword(String keyword, int limit, int offset) throws Exception;

	List<Agency> getDeletedAgencies(String keyword, int limit, int offset) throws Exception;

	List<Agency> filterAgencies(String status, Boolean active, String startDate, String endDate, String keyword,
			Boolean delete, int limit, int offset) throws Exception;

	long countAgencies(String status, Boolean activeState, Boolean isDeleted, String keyword, String startDate,
			String endDate) throws Exception;

}
