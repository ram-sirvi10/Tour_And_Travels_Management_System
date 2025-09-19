package com.travelmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.dao.IAgencyDAO;
import com.travelmanagement.dao.impl.AgencyDAOImpl;
import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.exception.BadRequestException;
import com.travelmanagement.exception.UserNotFoundException;
import com.travelmanagement.model.Agency;
import com.travelmanagement.service.IAgencyService;
import com.travelmanagement.util.Mapper;
import com.travelmanagement.util.PasswordHashing;

public class AgencyServiceImpl implements IAgencyService {

    private IAgencyDAO agencyDAO = new AgencyDAOImpl();

    
    @Override
    public AgencyResponseDTO login(LoginRequestDTO dto) throws Exception {
        Agency dbAgency = agencyDAO.getAgencyByField("email", dto.getEmail());
        if (dbAgency == null) {
            throw new UserNotFoundException("Agency not found!");
        }
        if (!PasswordHashing.checkPassword(dto.getPassword(), dbAgency.getPassword())) {
            throw new BadRequestException("Invalid credentials!");
        }
        return Mapper.mapAgencyToAgencyResponseDTO(dbAgency);
    }

    @Override
    public AgencyResponseDTO getAgencyByEmail(String email) throws Exception {
        Agency agency = agencyDAO.getAgencyByField("email", email);
        if (agency == null) {
            throw new UserNotFoundException("Agency not found with email: " + email);
        }
        return Mapper.mapAgencyToAgencyResponseDTO(agency);
    }

    public AgencyResponseDTO getAgencyByRegistrationNumber(String regNo) throws Exception {
        Agency agency = agencyDAO.getAgencyByField("registration_number", regNo);
        if (agency == null) {
            throw new UserNotFoundException("Agency not found with registration number: " + regNo);
        }
        return Mapper.mapAgencyToAgencyResponseDTO(agency);
    }

    
    
    
    @Override
    public AgencyResponseDTO register(AgencyRegisterRequestDTO dto) throws Exception {
        Agency agency = Mapper.mapRegisterAgencyDtoToAgency(dto);
        agency.setPassword(PasswordHashing.ecryptPassword(agency.getPassword()));

        boolean created = agencyDAO.createAgency(agency);
        if (!created) {
            throw new BadRequestException("Agency registration failed!");
        }

        return Mapper.mapAgencyToAgencyResponseDTO(agency);
    }

//    @Override
//    public AgencyResponseDTO login(LoginRequestDTO dto) throws Exception {
//        Agency dbAgency = agencyDAO.getAgencyByEmail(dto.getEmail());
//        if (dbAgency == null) {
//            throw new UserNotFoundException("Agency not found!");
//        }
//
//        if (!PasswordHashing.checkPassword(dto.getPassword(), dbAgency.getPassword())) {
//            throw new BadRequestException("Invalid credentials!");
//        }
//
//        return Mapper.mapAgencyToAgencyResponseDTO(dbAgency);
//    }
//
//  

    @Override
    public AgencyResponseDTO getAgencyById(int agencyId) throws Exception {
        Agency agency = agencyDAO.getAgencyById(agencyId);
        if (agency == null) {
            throw new UserNotFoundException("Agency not found with ID: " + agencyId);
        }
        return Mapper.mapAgencyToAgencyResponseDTO(agency);
    }

//    @Override
//    public AgencyResponseDTO getAgencyByEmail(String email) throws Exception {
//        Agency agency = agencyDAO.getAgencyByEmail(email);
//        if (agency == null) {
//            throw new UserNotFoundException("Agency not found with email: " + email);
//        }
//        return Mapper.mapAgencyToAgencyResponseDTO(agency);
//    }

    @Override
    public List<AgencyResponseDTO> getAllAgencies(int limit,int offset) throws Exception {
        List<Agency> agencies = agencyDAO.getAllAgencies(limit, offset);
        if (agencies == null) {
            agencies = new ArrayList<>(); // safety check
        }
        List<AgencyResponseDTO> responseList = new ArrayList<>();
        for (Agency a : agencies) {
            responseList.add(Mapper.mapAgencyToAgencyResponseDTO(a));
        }
        return responseList;
    }

    @Override
    public List<AgencyResponseDTO> getAgenciesByStatus(String status,int limit,int offset) throws Exception {
       
        List<Agency> agencies = agencyDAO.getAgenciesByStatus(status.toUpperCase(), limit, offset); 
        System.out.println("agency servliec agencyListBy status "+agencies.toString());
        List<AgencyResponseDTO> responseList = new ArrayList<>();
        for (Agency a : agencies) {
            responseList.add(Mapper.mapAgencyToAgencyResponseDTO(a));
        }
        
        return responseList;
    }

    @Override
    public boolean updateAgency(Agency agency) throws Exception {
        return agencyDAO.updateAgency(agency);
    }

    @Override
    public boolean deleteAgency(int agencyId) throws Exception {
        return agencyDAO.deleteAgency(agencyId);
    }

    
    @Override
    public boolean updateAgencyActiveState(int agencyId, boolean active) throws Exception {
        return agencyDAO.updateAgencyActiveState(agencyId, active);
    }
    @Override
    public boolean updateAgencyStatus(int agencyId, String status) throws Exception {
        return agencyDAO.updateAgencyStatus(agencyId, status); // call DAO method
    }


}
