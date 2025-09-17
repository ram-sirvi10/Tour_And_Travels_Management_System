package com.travelmanagement.dao;

import com.travelmanagement.model.Agency;

public interface IAgencyDAO {
    boolean createAgency(Agency agency) throws Exception;

	Agency getAgencyByEmail(String email) throws Exception;
}
