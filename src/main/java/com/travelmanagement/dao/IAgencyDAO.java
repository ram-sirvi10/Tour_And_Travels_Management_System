package com.travelmanagement.dao;

import com.travelmanagement.model.Agency;
import java.util.List;

public interface IAgencyDAO {
	boolean createAgency(Agency agency) throws Exception;

	Agency getAgencyById(int id) throws Exception;

	boolean updateAgency(Agency agency) throws Exception;

	boolean deleteAgency(int id) throws Exception;

	List<Agency> searchAgencies(String keyword,int limit, int offset) throws Exception;

	List<Agency> getAgenciesByStatus(String status,int limit, int offset) throws Exception;

	List<Agency> getAgenciesByActiveState(boolean key,int limit, int offset) throws Exception;

	long countAgencies(String key) throws Exception;

	List<Agency> getAllAgencies(int limit, int offset);

	boolean updateAgencyStatus(int agencyId, String status) throws Exception;

	boolean updateAgencyActiveState(int agencyId, boolean active) throws Exception;

	Agency getAgencyByField(String fieldName, String value) throws Exception;

	

}
