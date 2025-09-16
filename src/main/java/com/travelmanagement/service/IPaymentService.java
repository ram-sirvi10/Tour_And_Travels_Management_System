package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.model.Agency;

public interface IPackageService {
	Agency createPackage(Agency p) throws Exception;
	Agency getById(int id) throws Exception;
    List<Agency> getAll() throws Exception;
    List<Agency> search(String keyword) throws Exception;
    boolean updatePackage(Agency p) throws Exception;
    boolean deletePackage(int id) throws Exception;
}
