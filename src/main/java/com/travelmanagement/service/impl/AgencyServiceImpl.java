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
		}System.out.println("agency password in dp login "+dbAgency.getPassword());
		if (!PasswordHashing.checkPassword(dto.getPassword(), dbAgency.getPassword())) {
			throw new BadRequestException("Invalid password ! ");
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

	@Override
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
	public AgencyResponseDTO getAgencyById(Integer agencyId) throws Exception {
		Agency agency = agencyDAO.getAgencyById(agencyId);
		if (agency == null) {
			throw new UserNotFoundException("Agency not found with ID: " + agencyId);
		}
		return Mapper.mapAgencyToAgencyResponseDTO(agency);
	}

	@Override
	public boolean changePassword(Integer agencyId, String newPassword) throws Exception {
		String hashedPassword = PasswordHashing.ecryptPassword(newPassword);
		return agencyDAO.changePassword(agencyId, hashedPassword);
	}
//    @Override
//    public AgencyResponseDTO getAgencyByEmail(String email) throws Exception {
//        Agency agency = agencyDAO.getAgencyByEmail(email);
//        if (agency == null) {
//            throw new UserNotFoundException("Agency not found with email: " + email);
//        }
//        return Mapper.mapAgencyToAgencyResponseDTO(agency);
//    }

//	@Override
//	public List<AgencyResponseDTO> getAllAgencies(int limit, int offset) throws Exception {
//		List<Agency> agencies = agencyDAO.getAllAgencies(limit, offset);
//		if (agencies == null) {
//			agencies = new ArrayList<>();
//		}
//		List<AgencyResponseDTO> responseList = new ArrayList<>();
//		for (Agency a : agencies) {
//			responseList.add(Mapper.mapAgencyToAgencyResponseDTO(a));
//		}
//		return responseList;
//	}
//
//	@Override
//	public List<AgencyResponseDTO> getAgenciesByStatus(String status, int limit, int offset) throws Exception {
//
//		List<Agency> agencies = agencyDAO.getAgenciesByStatus(status.toUpperCase(), limit, offset);
//		System.out.println("agency service agencyListBy status " + agencies.toString());
//		List<AgencyResponseDTO> responseList = new ArrayList<>();
//		for (Agency a : agencies) {
//			responseList.add(Mapper.mapAgencyToAgencyResponseDTO(a));
//		}
//
//		return responseList;
//	}

	@Override
	public boolean updateAgency(AgencyRegisterRequestDTO dto) throws Exception {
		System.out.println("Agency Service Update Agency dto === " + dto);
		return agencyDAO.updateAgency(Mapper.mapRegisterAgencyDtoToAgency(dto));
	}

	@Override
	public boolean deleteAgency(Integer agencyId) throws Exception {
		Agency agency = agencyDAO.getAgencyById(agencyId);
		if (agency != null) {
			if (agency.isDelete()) {
				throw new UserNotFoundException("Agency Not Found : ");
			}
		} else {

			throw new UserNotFoundException("Agency Not Found : ");
		}

		return agencyDAO.deleteAgency(agencyId);
	}

	@Override
	public boolean updateAgencyActiveState(Integer agencyId, Boolean active) throws Exception {
		Agency agency = agencyDAO.getAgencyById(agencyId);
		if (agency != null) {
			if (agency.isActive() == active) {
				String state = "ACTIVE";
				if (!active) {
					state = "INACTIVE";
				}
				throw new BadRequestException("Agency Already -> " + state);
			} else if (agency.isDelete()) {
				throw new UserNotFoundException("Agency Not Found : ");
			}
		} else {

			throw new UserNotFoundException("Agency Not Found : ");
		}

		return agencyDAO.updateAgencyActiveState(agencyId, active);
	}

	@Override
	public boolean updateAgencyStatus(Integer agencyId, String status) throws Exception {
		Agency agency = agencyDAO.getAgencyById(agencyId);
		if (agency != null) {
			if (agency.getStatus().equalsIgnoreCase(status)) {
				throw new BadRequestException("Agency Already -> " + status);
			} else if (agency.isDelete()) {
				throw new UserNotFoundException("Agency Not Found : ");
			} else if (!agency.isActive()) {
				throw new BadRequestException("Agency is blocked ! ");
			}
		} else {

			throw new UserNotFoundException("Agency Not Found : ");
		}

		return agencyDAO.updateAgencyStatus(agencyId, status);
	}

//	@Override
//	public List<AgencyResponseDTO> getDeletedAgencies(String keyword, int limit, int offset) throws Exception {
//		List<Agency> agencies = agencyDAO.getDeletedAgencies(keyword, limit, offset);
//		List<AgencyResponseDTO> responseList = new ArrayList<>();
//		for (Agency a : agencies) {
//			responseList.add(Mapper.mapAgencyToAgencyResponseDTO(a));
//		}
//		return responseList;
//	}
//
//	@Override
//	public List<AgencyResponseDTO> getAgenciesByActiveState(Boolean isActive, int limit, int offset) throws Exception {
//		List<Agency> agencies = agencyDAO.getAgenciesByActiveState(isActive, limit, offset);
//		List<AgencyResponseDTO> responseList = new ArrayList<>();
//		for (Agency a : agencies) {
//			responseList.add(Mapper.mapAgencyToAgencyResponseDTO(a));
//		}
//		return responseList;
//	}

	@Override
	public List<AgencyResponseDTO> filterAgencies(String status, Boolean active, String startDate, String endDate,
			String keyword, Boolean delete, int limit, int offset) throws Exception {
		List<Agency> agencies = agencyDAO.filterAgencies(status, active, startDate, endDate, keyword, delete, limit,
				offset);
		List<AgencyResponseDTO> responseList = new ArrayList<>();
		for (Agency a : agencies) {
			responseList.add(Mapper.mapAgencyToAgencyResponseDTO(a));
		}
		return responseList;
	}

//	@Override
//	public List<AgencyResponseDTO> searchAgenciesByKeyword(String keyword, int limit, int offset) throws Exception {
//		List<Agency> agencies = agencyDAO.searchAgenciesByKeyword(keyword, limit, offset);
//		List<AgencyResponseDTO> responseList = new ArrayList<>();
//		for (Agency a : agencies) {
//			responseList.add(Mapper.mapAgencyToAgencyResponseDTO(a));
//		}
//		return responseList;
//	}

	@Override
	public long countAgencies(String status, Boolean activeState, Boolean isDeleted, String keyword, String startDate,
			String endDate) throws Exception {
		return agencyDAO.countAgencies(status, activeState, isDeleted, keyword, startDate, endDate);
	}

}
