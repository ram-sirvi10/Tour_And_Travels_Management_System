package com.travelmanagement.service.impl;

import com.travelmanagement.dao.IAgencyDAO;
import com.travelmanagement.dao.impl.AgencyDAOImpl;
import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.exception.BadRequestException;
import com.travelmanagement.exception.UserNotFoundException;
import com.travelmanagement.model.Agency;
import com.travelmanagement.service.IAgencyService;
import com.travelmanagement.util.PasswordHashing;

public class AgencyServiceImpl implements IAgencyService {

	private IAgencyDAO agencyDAO = new AgencyDAOImpl();

	@Override
	public void register(AgencyRegisterRequestDTO dto) throws Exception {
		Agency agency = new AuthServiceImpl().mapRegisterAgencyDtoToAgency(dto);
		agency.setPassword(PasswordHashing.ecryptPassword(agency.getPassword()));

		boolean created = agencyDAO.createAgency(agency);
		if (!created) {
			throw new BadRequestException("Agency registration failed!");
		}
	}

	@Override
	public Agency login(LoginRequestDTO dto) throws Exception {

		Agency dbAgency = ((AgencyDAOImpl) agencyDAO).getAgencyByEmail(dto.getEmail());
		if (dbAgency == null) {
			throw new UserNotFoundException("Agency not found!");
		}

		if (!PasswordHashing.checkPassword(dto.getPassword(), dbAgency.getPassword())) {
			throw new BadRequestException("Invalid credentials!");
		}

		return dbAgency;
	}
}
