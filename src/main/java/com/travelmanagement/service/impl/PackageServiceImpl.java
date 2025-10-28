package com.travelmanagement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dao.IPackageDAO;
import com.travelmanagement.dao.IPackageScheduleDAO;
import com.travelmanagement.dao.impl.PackageDAOImpl;
import com.travelmanagement.dao.impl.PackageScheduleDAOImpl;
import com.travelmanagement.dto.requestDTO.PackageRegisterDTO;
import com.travelmanagement.dto.requestDTO.PackageScheduleRequestDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageScheduleResponseDTO;
import com.travelmanagement.exception.ResourceNotFoundException;
import com.travelmanagement.model.PackageSchedule;
import com.travelmanagement.model.Packages;
import com.travelmanagement.service.IPackageService;
import com.travelmanagement.util.Mapper;

public class PackageServiceImpl implements IPackageService {

	private IPackageDAO packageDAO = new PackageDAOImpl();
//	private IBookingDAO bookingDAO = new BookingDAOImpl();
	private IPackageScheduleDAO scheduleDAO = new PackageScheduleDAOImpl();

	@Override
	public int addPackage(PackageRegisterDTO dto) throws Exception {

		int packageId = 0;
		try {

			Packages pkg = Mapper.mapToModel(dto);
			pkg.setVersion(0);
			packageId = packageDAO.addPackage(pkg);

			if (packageId <= 0) {
				throw new Exception("Failed to insert package");
			}

			List<PackageScheduleRequestDTO> schedules = dto.getPackageSchedule();
			List<PackageSchedule> packageSchedules = new ArrayList<PackageSchedule>();
			for (PackageScheduleRequestDTO schedule : schedules) {
				System.err.println("<----- in PackageServiceImpl package id after package  add ------- > ");
				System.out.println(packageId);
				PackageSchedule packageSchedule = Mapper.mapPackageScheduleRequestDTOToModel(schedule);
				packageSchedule.setPackageId(packageId);
				System.err.println("(packageservice) Package IDs in package Schudle for add package schedule  " + schedule.getPackageId());
				packageSchedules.add(packageSchedule);
			}

			boolean scheduleCreated = scheduleDAO.addSchedules(packageSchedules);
			if (!scheduleCreated) {
				packageDAO.deletePackage(packageId);
				throw new Exception("Failed to add package schedules");
			}

			return packageId;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<PackageScheduleResponseDTO> getScheduleByPackage(Integer packageId) {
		List<PackageScheduleResponseDTO> packageScheduleResponseDTOs = new ArrayList<PackageScheduleResponseDTO>();
		try {
			List<PackageSchedule> packageSchedule = scheduleDAO.getScheduleByPackage(packageId);
			for (PackageSchedule pkg : packageSchedule) {
				packageScheduleResponseDTOs.add(Mapper.mapPackageScheduleToDTO(pkg));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return packageScheduleResponseDTOs;
	}

//	public void createPackage(PackageRegisterDTO dto) throws Exception {
//		Packages pkg = Mapper.mapToModel(dto);
//		validatePackage(pkg);
//		packageDAO.addPackage(pkg);
//	}

	public boolean updatePackage(PackageRegisterDTO dto) throws Exception {

		Packages pkg = Mapper.mapToModel(dto);
		System.out.println("Package details in packageservice update --> " + pkg);

		boolean packageUpdated = packageDAO.updatePackage(pkg);
		System.out.println("Update Package In  Package service--> " + packageUpdated);
		if (!packageUpdated)
			return false;

		List<PackageScheduleRequestDTO> newSchedules = dto.getPackageSchedule();
		List<PackageSchedule> existingSchedules = scheduleDAO.getScheduleByPackage(dto.getPackageId());

		Map<Integer, PackageSchedule> existingMap = new HashMap<>();
		for (PackageSchedule ps : existingSchedules) {
			existingMap.put(ps.getDayNumber(), ps);
		}

		List<PackageSchedule> toUpdate = new ArrayList<>();
		List<PackageSchedule> toAdd = new ArrayList<>();
		List<Integer> toDelete = new ArrayList<>();

		for (PackageScheduleRequestDTO s : newSchedules) {
			if (existingMap.containsKey(s.getDayNumber())) {
				PackageSchedule ps = existingMap.get(s.getDayNumber());
				ps.setActivity(s.getActivity());
				ps.setDescription(s.getDescription());
				toUpdate.add(ps);
				existingMap.remove(s.getDayNumber());
			} else {
				PackageSchedule ps = Mapper.mapPackageScheduleRequestDTOToModel(s);
				ps.setPackageId(dto.getPackageId());
				toAdd.add(ps);
			}
		}
		toDelete.addAll(existingMap.keySet());

		boolean updatePackageSchedules = scheduleDAO.updatePackageSchedules(dto.getPackageId(), toUpdate, toAdd,
				toDelete);
		System.out.println("Update package schedule in package service --> " + updatePackageSchedules);
		return updatePackageSchedules;
	}

	@Override
	public boolean deletePackage(Integer id) throws Exception {
		return packageDAO.deletePackage(id);
	}

	public boolean togglePackageStatus(Integer packageId) throws Exception {
		return packageDAO.togglePackageStatus(packageId);
	}

//	private void validatePackage(Packages pkg) throws Exception {
//		if (!ValidationUtil.isValidName(pkg.getTitle()))
//			throw new Exception("Invalid Title");
//		if (!ValidationUtil.isValidPrice(String.valueOf(pkg.getPrice())))
//			throw new Exception("Invalid Price");
//		if (!ValidationUtil.isValidName(pkg.getLocation()))
//			throw new Exception("Invalid Location");
//		if (pkg.getDuration() <= 0)
//			throw new Exception("Invalid Duration");
//	}

//	@Override
//	public List<PackageResponseDTO> getAllPackages(Integer agencyId) throws Exception {
//		List<Packages> list = packageDAO.getAllPackages();
//		List<PackageResponseDTO> dtoList = new ArrayList<>();
//		for (Packages p : list) {
//			if (agencyId == 0 || p.getAgencyId() == agencyId) {
//				dtoList.add(Mapper.toResponseDTO(p));
//			}
//		}
//		return dtoList;
//	}

//	@Override
//	public List<PackageResponseDTO> searchPackagesByKeyword(String keyword) throws Exception {
//		List<Packages> all = packageDAO.getAllPackages();
//		List<PackageResponseDTO> filtered = new ArrayList<>();
//		if (keyword != null && !keyword.isEmpty()) {
//			for (Packages p : all) {
//				if (p.getTitle().toLowerCase().contains(keyword.toLowerCase())
//						|| p.getLocation().toLowerCase().contains(keyword.toLowerCase())) {
//					filtered.add(Mapper.toResponseDTO(p));
//				}
//			}
//		}
//		return filtered;
//	}

	@Override
	public PackageResponseDTO getPackageById(Integer id) throws Exception {
		Packages packages = packageDAO.getPackageById(id);
		if (packages == null) {
			throw new ResourceNotFoundException("Package not found ");
		}

		return Mapper.toResponseDTO(packages);
	}

	public boolean adjustSeats(Integer packageId, Integer seatsChange) throws Exception {
		if (seatsChange == 0)
			return true;
		System.out.println("Package service adjust seat = " + (seatsChange));
		return packageDAO.adjustSeats(packageId, seatsChange);
	}

	@Override
	public List<PackageResponseDTO> searchPackages(String title, Integer agencyId, String location, String keyword,
			String dateFrom, String dateTo, Integer totalSeats, Boolean isActive, int limit, int offset,
			Boolean isAgencyView,Boolean includePast) throws Exception {

		List<Packages> packages = packageDAO.searchPackages(title, agencyId, location, keyword, dateFrom, dateTo,
				totalSeats, isActive, limit, offset, isAgencyView,includePast);

		List<PackageResponseDTO> dtoList = new ArrayList<>();
		for (Packages pkg : packages) {
			dtoList.add(Mapper.toResponseDTO(pkg));
		}

		return dtoList;
	}

	@Override
	public long countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, Integer totalSeats, Boolean isActive, Boolean isAgencyView,Boolean includePast) throws Exception {

		return packageDAO.countPackages(title, agencyId, location, keyword, dateFrom, dateTo, totalSeats, isActive,
				isAgencyView,includePast);
	}

	public int updateSeatsOptimistic(int packageId, int noOfTravellers, int version) {

		try {
			return packageDAO.updateSeatsOptimistic(packageId, noOfTravellers, version);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}



}
