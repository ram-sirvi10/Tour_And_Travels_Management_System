package com.travelmanagement.dao;

import com.travelmanagement.model.Agency;
import java.util.List;

public interface IAgencyDAO {
	boolean createAgency(Agency agency) throws Exception;

	Agency getAgencyByEmail(String email) throws Exception;

	Agency getAgencyById(int id) throws Exception;

	boolean updateAgency(Agency agency) throws Exception;

	boolean deleteAgency(int id) throws Exception;

	boolean approveAgency(int agencyId) throws Exception;

	boolean declineAgency(int agencyId) throws Exception;

	List<Agency> getPendingAgencies(int limit, int offset) throws Exception;

	boolean enableAgency(int agencyId) throws Exception;

	boolean disableAgency(int agencyId) throws Exception;

	List<Agency> searchAgencies(String keyword,int limit, int offset) throws Exception;

	List<Agency> getAgenciesByStatus(String status,int limit, int offset) throws Exception;//approve, panding ,reject

	List<Agency> getAgenciesByStatus(boolean key,int limit, int offset) throws Exception;// Active or inactive

	long countAgencies(String key) throws Exception;

	

}
