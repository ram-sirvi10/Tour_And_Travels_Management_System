package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.IPackageDAO;
import com.travelmanagement.model.Packages;

public class PackageDAOImpl implements IPackageDAO {

    @Override
    public boolean addPackage(Packages pkg) throws Exception {
        String sql = "INSERT INTO packages (title, agencyId, description, price, location, duration, isActive, createdAt, updatedAt) "
                   + "VALUES (?,?,?,?,?,?,?, NOW(), NOW())";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pkg.getTitle());
            ps.setInt(2, pkg.getAgencyId());
            ps.setString(3, pkg.getDescription());
            ps.setDouble(4, pkg.getPrice());
            ps.setString(5, pkg.getLocation());
            ps.setInt(6, pkg.getDuration());
            ps.setBoolean(7, pkg.isActive());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePackage(Packages pkg) throws Exception {
        String sql = "UPDATE packages SET title=?, agencyId=?, description=?, price=?, location=?, duration=?, isActive=?, updatedAt = NOW() WHERE packageId=?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pkg.getTitle());
            ps.setInt(2, pkg.getAgencyId());
            ps.setString(3, pkg.getDescription());
            ps.setDouble(4, pkg.getPrice());
            ps.setString(5, pkg.getLocation());
            ps.setInt(6, pkg.getDuration());
            ps.setBoolean(7, pkg.isActive());
            ps.setInt(8, pkg.getPackageId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deletePackage(int packageId) throws Exception {
        String sql = "DELETE FROM packages WHERE packageId=?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean togglePackageStatus(int packageId) throws Exception {
        String sql = "UPDATE packages SET isActive = CASE WHEN isActive=1 THEN 0 ELSE 1 END, updatedAt = NOW() WHERE packageId=?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Packages> getAllPackages() throws Exception {
        String sql = "SELECT * FROM packages ORDER BY packageId DESC";
        List<Packages> list = new ArrayList<>();
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Packages pkg = new Packages();
                pkg.setPackageId(rs.getInt("packageId"));
                pkg.setTitle(rs.getString("title"));
                pkg.setAgencyId(rs.getInt("agencyId"));
                pkg.setDescription(rs.getString("description"));
                pkg.setPrice(rs.getDouble("price"));
                pkg.setLocation(rs.getString("location"));
                pkg.setDuration(rs.getInt("duration"));
                pkg.setActive(rs.getBoolean("isActive"));
                list.add(pkg);
            }
        }
        return list;
    }

    @Override
    public Packages getPackageById(int packageId) throws Exception {
        String sql = "SELECT * FROM packages WHERE packageId = ?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Packages pkg = new Packages();
                    pkg.setPackageId(rs.getInt("packageId"));
                    pkg.setTitle(rs.getString("title"));
                    pkg.setAgencyId(rs.getInt("agencyId"));
                    pkg.setDescription(rs.getString("description"));
                    pkg.setPrice(rs.getDouble("price"));
                    pkg.setLocation(rs.getString("location"));
                    pkg.setDuration(rs.getInt("duration"));
                    pkg.setActive(rs.getBoolean("isActive"));
                    return pkg;
                }
            }
        }
        return null;
    }
}
