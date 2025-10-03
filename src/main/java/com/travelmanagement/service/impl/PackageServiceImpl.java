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

	public List<PackageSchedule> getScheduleByPackage(int packageId) throws Exception {
	    return scheduleDAO.getScheduleByPackage(packageId);
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

	// -------------------- Update Package --------------------
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

	// -------------------- Get All Packages --------------------
	@Override
	public List<Packages> getAllPackages() throws Exception {
		return packageDAO.getAllPackages();
	}

	@Override
	public List<PackageResponseDTO> getAllPackages(int agencyId) throws Exception {
		List<Packages> list = packageDAO.getAllPackages();
		List<PackageResponseDTO> dtoList = new ArrayList<>();
		for (Packages pkg : list) {
			if (agencyId == 0 || pkg.getAgencyId() == agencyId) {
				PackageResponseDTO dto = Mapper.toResponseDTO(pkg);

				// Fetch total bookings for this package
				int totalBookings = bookingDAO.getTotalBookingsByPackage(pkg.getPackageId());
				dto.setTotalBookings(totalBookings);

				// Fetch total revenue for this package
				double totalRevenue = bookingDAO.getRevenueByPackage(pkg.getPackageId());
				dto.setTotalRevenue(totalRevenue);

				dtoList.add(dto);
			}
		}
		return dtoList;
	}

	// -------------------- Get Package by ID --------------------
	@Override
	public PackageResponseDTO getPackageById(int id) throws Exception {
		Packages pkg = packageDAO.getPackageById(id);
		if (pkg == null) {
			throw new ResourceNotFoundException("Package not found");
		}

		PackageResponseDTO dto = Mapper.toResponseDTO(pkg);
		dto.setTotalBookings(bookingDAO.getTotalBookingsByPackage(pkg.getPackageId()));
		dto.setTotalRevenue(bookingDAO.getRevenueByPackage(pkg.getPackageId()));
		return dto;
	}

	// -------------------- Search Packages --------------------
	@Override
	public List<Packages> searchPackages(String keyword) throws Exception {
		List<Packages> all = packageDAO.getAllPackages();
		List<Packages> filtered = new ArrayList<>();
		for (Packages p : all) {
			if (p.getTitle().toLowerCase().contains(keyword.toLowerCase())
					|| p.getLocation().toLowerCase().contains(keyword.toLowerCase())) {
				filtered.add(p);
			}
		}
		return filtered;
	}

	@Override
	public List<PackageResponseDTO> searchPackagesByKeyword(String keyword) throws Exception {
		List<Packages> all = packageDAO.getAllPackages();
		List<PackageResponseDTO> filtered = new ArrayList<>();
		if (keyword != null && !keyword.isEmpty()) {
			for (Packages p : all) {
				if (p.getTitle().toLowerCase().contains(keyword.toLowerCase())
						|| p.getLocation().toLowerCase().contains(keyword.toLowerCase())) {
					PackageResponseDTO dto = Mapper.toResponseDTO(p);
					dto.setTotalBookings(bookingDAO.getTotalBookingsByPackage(p.getPackageId()));
					dto.setTotalRevenue(bookingDAO.getRevenueByPackage(p.getPackageId()));
					filtered.add(dto);
				}
			}
		}
		return filtered;
	}

	// -------------------- Adjust Seats --------------------
	public boolean adjustSeats(int packageId, int seatsChange) throws Exception {
		Packages pkg = packageDAO.getPackageById(packageId);
		if (pkg == null)
			return false;

		int newSeats = pkg.getTotalSeats() + seatsChange;
		if (newSeats < 0)
			return false;

		return packageDAO.adjustSeats(packageId, newSeats);
	}

	// -------------------- Search with Filters --------------------
	@Override
	public List<PackageResponseDTO> searchPackages(String title, Integer agencyId, String location, String keyword,
			String dateFrom, String dateTo, Integer totalSeats, Boolean isActive, int limit, int offset,
			Boolean isAgencyView) throws Exception {

		List<Packages> packages = packageDAO.searchPackages(title, agencyId, location, keyword, dateFrom, dateTo,
				totalSeats, isActive, limit, offset, isAgencyView);

		List<PackageResponseDTO> dtoList = new ArrayList<>();
		for (Packages pkg : packages) {
			PackageResponseDTO dto = Mapper.toResponseDTO(pkg);
			dto.setTotalBookings(bookingDAO.getTotalBookingsByPackage(pkg.getPackageId()));
			dto.setTotalRevenue(bookingDAO.getRevenueByPackage(pkg.getPackageId()));
			dtoList.add(dto);
		}

		return dtoList;
	}

	@Override
	public int countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, Integer totalSeats, Boolean isActive, Boolean isAgencyView) throws Exception {

		return packageDAO.countPackages(title, agencyId, location, keyword, dateFrom, dateTo, totalSeats, isActive,
				isAgencyView);
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
}
