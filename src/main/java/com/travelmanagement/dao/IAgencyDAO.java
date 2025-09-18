package com.travelmanagement.dao;

import com.travelmanagement.model.Agency;
import java.util.List;

public interface IAgencyDAO {
	boolean createAgency(Agency agency) throws Exception;

	Agency getAgencyByEmail(String email) throws Exception;

	Agency getAgencyById(int id) throws Exception;

	List<Agency> getAllAgencies() throws Exception;

	boolean updateAgency(Agency agency) throws Exception;

	boolean deleteAgency(int id) throws Exception;

	boolean approveAgency(int agencyId) throws Exception;

	boolean declineAgency(int agencyId) throws Exception;

	List<Agency> getPendingAgencies() throws Exception;

	boolean enableAgency(int agencyId) throws Exception;

	boolean disableAgency(int agencyId) throws Exception;

	List<Agency> searchAgenciesByName(String keyword) throws Exception;

	List<Agency> getAgenciesByLocation(String location) throws Exception;

	List<Agency> getAgenciesByStatus(String status) throws Exception;

	List<Agency> getActiveAgencies() throws Exception;

	List<Agency> getInactiveAgencies() throws Exception;

	long countAgencies() throws Exception;

	long countPendingAgencies() throws Exception;

	long countApprovedAgencies() throws Exception;

	long countDeclinedAgencies() throws Exception;

	List<Agency> getRecentlyRegisteredAgencies(int limit) throws Exception;

	List<Agency> getAgenciesRegisteredBetween(String startDate, String endDate) throws Exception;

}
