package com.travelmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.dao.IBookingDAO;
import com.travelmanagement.dao.IPackageDAO;
import com.travelmanagement.dao.IPackageScheduleDAO;
import com.travelmanagement.dao.impl.BookingDAOImpl;
import com.travelmanagement.dao.impl.PackageDAOImpl;
import com.travelmanagement.dao.impl.PackageScheduleDAOImpl;
import com.travelmanagement.dto.requestDTO.PackageRegisterDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.exception.ResourceNotFoundException;
import com.travelmanagement.model.PackageSchedule;
import com.travelmanagement.model.Packages;
import com.travelmanagement.service.IPackageService;
import com.travelmanagement.util.Mapper;
import com.travelmanagement.util.ValidationUtil;

public class PackageServiceImpl implements IPackageService {

	private IPackageDAO packageDAO = new PackageDAOImpl();
	private IBookingDAO bookingDAO = new BookingDAOImpl();
	private IPackageScheduleDAO scheduleDAO = new PackageScheduleDAOImpl();

	public boolean addSchedule(PackageSchedule schedule) throws Exception {
		return scheduleDAO.addSchedule(schedule);
	}

	public List<PackageSchedule> getScheduleByPackage(int packageId) {
		try {
			return scheduleDAO.getScheduleByPackage(packageId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// -------------------- Create Package --------------------
	public boolean addPackage(Packages pkg) throws Exception {
		validatePackage(pkg);
		return packageDAO.addPackage(pkg);
	}

	public void createPackage(PackageRegisterDTO dto) throws Exception {
		Packages pkg = Mapper.mapToModel(dto);
		validatePackage(pkg);
		packageDAO.addPackage(pkg);
	}

	@Override
	public boolean updatePackage(Packages pkg) throws Exception {
		validatePackage(pkg);
		return packageDAO.updatePackage(pkg);
	}

	// -------------------- Delete Package --------------------
	@Override
	public boolean deletePackage(int id) throws Exception {
		return packageDAO.deletePackage(id);
	}

	// -------------------- Toggle Package Status --------------------
	public boolean togglePackageStatus(int packageId) throws Exception {
		return packageDAO.togglePackageStatus(packageId);
	}

	// -------------------- Private Validation --------------------
	private void validatePackage(Packages pkg) throws Exception {
		if (!ValidationUtil.isValidName(pkg.getTitle()))
			throw new Exception("Invalid Title");
		if (!ValidationUtil.isValidPrice(String.valueOf(pkg.getPrice())))
			throw new Exception("Invalid Price");
		if (!ValidationUtil.isValidName(pkg.getLocation()))
			throw new Exception("Invalid Location");
		if (pkg.getDuration() <= 0)
			throw new Exception("Invalid Duration");
	}

	@Override
	public List<PackageResponseDTO> getAllPackages(int agencyId) throws Exception {
		List<Packages> list = packageDAO.getAllPackages();
		List<PackageResponseDTO> dtoList = new ArrayList<>();
		for (Packages p : list) {
			if (agencyId == 0 || p.getAgencyId() == agencyId) {
				dtoList.add(Mapper.toResponseDTO(p));
			}
		}
		return dtoList;
	}

	@Override
	public List<PackageResponseDTO> searchPackagesByKeyword(String keyword) throws Exception {
		List<Packages> all = packageDAO.getAllPackages();
		List<PackageResponseDTO> filtered = new ArrayList<>();
		if (keyword != null && !keyword.isEmpty()) {
			for (Packages p : all) {
				if (p.getTitle().toLowerCase().contains(keyword.toLowerCase())
						|| p.getLocation().toLowerCase().contains(keyword.toLowerCase())) {
					filtered.add(Mapper.toResponseDTO(p));
				}
			}
		}
		return filtered;
	}

	@Override
	public PackageResponseDTO getPackageById(int id) throws Exception {
		Packages packages = packageDAO.getPackageById(id);
		if (packages == null) {
			throw new ResourceNotFoundException("Package not found ");
		}

		return Mapper.toResponseDTO(packages);
	}

	public boolean adjustSeats(int packageId, int seatsChange) throws Exception {
		if (seatsChange == 0)
			return true;
		System.out.println("Package service adjust seat = "+(seatsChange));
		return packageDAO.adjustSeats(packageId, seatsChange);
	}

	@Override
	public List<PackageResponseDTO> searchPackages(String title, Integer agencyId, String location, String keyword,
			String dateFrom, String dateTo, Integer totalSeats, Boolean isActive, int limit, int offset,
			Boolean isAgencyView) throws Exception {

		List<Packages> packages = packageDAO.searchPackages(title, agencyId, location, keyword, dateFrom, dateTo,
				totalSeats, isActive, limit, offset, isAgencyView);

		List<PackageResponseDTO> dtoList = new ArrayList<>();
		for (Packages pkg : packages) {
			dtoList.add(Mapper.toResponseDTO(pkg));
		}

		return dtoList;
	}

	@Override
	public int countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, Integer totalSeats, Boolean isActive, Boolean isAgencyView) throws Exception {

		return packageDAO.countPackages(title, agencyId, location, keyword, dateFrom, dateTo, totalSeats, isActive,
				isAgencyView);
	}

//	public boolean adjustSeatsOptimistic(int packageId, int seatsToBook) throws Exception {
//	    PackageResponseDTO pkg = getPackageById(packageId);
//	    if (pkg == null) return false;
//
//	    int updatedRows =
//	    return updatedRows > 0;
//	}

	public int updateSeatsOptimistic(int packageId, int noOfTravellers, int version) {
		// TODO Auto-generated method stub
		try {
			return packageDAO.updateSeatsOptimistic(packageId, noOfTravellers, version);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
