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
import com.travelmanagement.util.PasswordHashing;

public class AgencyServiceImpl implements IAgencyService {

    private IAgencyDAO agencyDAO = new AgencyDAOImpl();
    private AuthServiceImpl authService = new AuthServiceImpl();

    @Override
    public AgencyResponseDTO register(AgencyRegisterRequestDTO dto) throws Exception {
        Agency agency = authService.mapRegisterAgencyDtoToAgency(dto);
        agency.setPassword(PasswordHashing.ecryptPassword(agency.getPassword()));

        boolean created = agencyDAO.createAgency(agency);
        if (!created) {
            throw new BadRequestException("Agency registration failed!");
        }

        return authService.mapAgencyToAgencyResponseDTO(agency);
    }

    @Override
    public AgencyResponseDTO login(LoginRequestDTO dto) throws Exception {
        Agency dbAgency = agencyDAO.getAgencyByEmail(dto.getEmail());
        if (dbAgency == null) {
            throw new UserNotFoundException("Agency not found!");
        }

        if (!PasswordHashing.checkPassword(dto.getPassword(), dbAgency.getPassword())) {
            throw new BadRequestException("Invalid credentials!");
        }

        return authService.mapAgencyToAgencyResponseDTO(dbAgency);
    }

  

    @Override
    public AgencyResponseDTO getAgencyById(int agencyId) throws Exception {
        Agency agency = agencyDAO.getAgencyById(agencyId);
        if (agency == null) {
            throw new UserNotFoundException("Agency not found with ID: " + agencyId);
        }
        return authService.mapAgencyToAgencyResponseDTO(agency);
    }

    @Override
    public AgencyResponseDTO getAgencyByEmail(String email) throws Exception {
        Agency agency = agencyDAO.getAgencyByEmail(email);
        if (agency == null) {
            throw new UserNotFoundException("Agency not found with email: " + email);
        }
        return authService.mapAgencyToAgencyResponseDTO(agency);
    }

    @Override
    public List<AgencyResponseDTO> getAllAgencies() throws Exception {
        List<Agency> agencies = agencyDAO.getAllAgencies();
        List<AgencyResponseDTO> responseList = new ArrayList<>();
        for (Agency a : agencies) {
            responseList.add(authService.mapAgencyToAgencyResponseDTO(a));
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
    public List<AgencyResponseDTO> getPendingAgencies() throws Exception {
        List<Agency> pending = agencyDAO.getPendingAgencies();
        List<AgencyResponseDTO> responseList = new ArrayList<>();
        for (Agency a : pending) {
            responseList.add(authService.mapAgencyToAgencyResponseDTO(a));
        }
        return responseList;
    }

    @Override
    public boolean approveAgency(int agencyId) throws Exception {
        return agencyDAO.approveAgency(agencyId);
    }

    @Override
    public boolean declineAgency(int agencyId) throws Exception {
        return agencyDAO.declineAgency(agencyId);
    }

    @Override
    public boolean enableAgency(int agencyId) throws Exception {
        return agencyDAO.enableAgency(agencyId);
    }

    @Override
    public boolean disableAgency(int agencyId) throws Exception {
        return agencyDAO.disableAgency(agencyId);
    }

}
