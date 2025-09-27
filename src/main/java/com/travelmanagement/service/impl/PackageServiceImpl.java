package com.travelmanagement.service.impl;

import com.travelmanagement.dao.IPackageDAO;
import com.travelmanagement.dao.impl.PackageDAOImpl;
import com.travelmanagement.dto.requestDTO.PackageRegisterDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.exception.ResourceNotFoundException;
import com.travelmanagement.model.Agency;
import com.travelmanagement.model.Packages;
import com.travelmanagement.service.IPackageService;
import com.travelmanagement.util.Mapper;
import com.travelmanagement.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class PackageServiceImpl implements IPackageService {

	private IPackageDAO packageDAO = new PackageDAOImpl();

	// ---------------- Existing methods ----------------

	public Agency createPackage(PackageRegisterDTO dto) throws Exception {
		Packages pkg = Mapper.mapToModel(dto);
		validatePackage(pkg);
		packageDAO.addPackage(pkg);
		return null; // keep as original
	}

	public Agency getById(int id) throws Exception {
		return null;
	}

	public List<Agency> getAll() throws Exception {
		return null;
	}

	public List<Agency> search(String keyword) throws Exception {
		return null;
	}

	public boolean updatePackage(Agency p) throws Exception {
		return false;
	}

	@Override
	public boolean deletePackage(int id) throws Exception {
		return packageDAO.deletePackage(id);
	}

	public boolean togglePackageStatus(int packageId) throws Exception {
		return packageDAO.togglePackageStatus(packageId);
	}

	public Agency createPackage(Agency p) throws Exception {
		return null;
	}

//    public List<PackageResponseDTO> getAllPackages(int agencyId) throws Exception {
//        List<Packages> list = packageDAO.getAllPackages();
//        List<PackageResponseDTO> dtoList = new ArrayList<>();
//        for(Packages p : list) {
//            if(p.getAgencyId() == agencyId) {
//                dtoList.add(Mapper.toResponseDTO(p));
//            }
//        }
//        return dtoList;
//    }

	@Override
	public boolean addPackage(Packages pkg) throws Exception {
		validatePackage(pkg);
		return packageDAO.addPackage(pkg);
	}

//    @Override
//    public Packages getPackageById(int id) throws Exception {
//        return packageDAO.getPackageById(id);
//    }

	@Override
	public List<Packages> getAllPackages() throws Exception {
		return packageDAO.getAllPackages();
	}

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
	public boolean updatePackage(Packages pkg) throws Exception {
		validatePackage(pkg);
		return packageDAO.updatePackage(pkg);
	}

	// ---------------- Private helper ----------------

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
		Packages pkg = packageDAO.getPackageById(packageId);
		if (pkg == null)
			return false;

		int newSeats = pkg.getTotalSeats() + seatsChange;
		if (newSeats < 0)
			return false;

		System.out.println(
				"Adjusting seats: current=" + pkg.getTotalSeats() + ", change=" + seatsChange + ", new=" + newSeats);

		return packageDAO.adjustSeats(packageId, newSeats);
	}

	@Override
	public List<PackageResponseDTO> searchPackages(String title, Integer agencyId, String location, String keyword,
			String dateFrom, String dateTo, Integer totalSeats, Boolean isActive, int limit,
			int offset, Boolean isAgencyView) throws Exception {

		List<Packages> packages = packageDAO.searchPackages(title, agencyId, location, keyword, dateFrom, dateTo,
				totalSeats,  isActive, limit, offset, isAgencyView);

		List<PackageResponseDTO> dtoList = new ArrayList<>();
		for (Packages pkg : packages) {
			dtoList.add(Mapper.toResponseDTO(pkg));
		}

		return dtoList;
	}

	@Override
	public int countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, Integer totalSeats,  Boolean isActive, Boolean isAgencyView)
			throws Exception {

		return packageDAO.countPackages(title, agencyId, location, keyword, dateFrom, dateTo, totalSeats, 
				isActive, isAgencyView);
	}
	
	
	public boolean adjustSeatsOptimistic(int packageId, int seatsToBook) throws Exception {
	    PackageResponseDTO pkg = getPackageById(packageId);
	    if (pkg == null) return false;

	    int updatedRows = packageDAO.updateSeatsOptimistic(packageId, seatsToBook, pkg.getVersion());
	    return updatedRows > 0;
	}



}
