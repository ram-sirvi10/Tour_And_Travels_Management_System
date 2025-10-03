package com.travelmanagement.dao;

import java.util.List;

public interface ILocationDAO {
    List<String> getLocations(String type, String value) throws Exception;
}
