package com.travelmanagement.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.http.Part;



public class CloudinaryUtil {

	private static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dmjrgxq7c", "api_key",
			"425932696452778", "api_secret", "7r6lS-Cho24EIGyYdzFG_Y1Wsus", "secure", true));

	public static Cloudinary getCloudinary() {
		return cloudinary;
	}

	public static String uploadImage(Part filePart) throws Exception {
		if (filePart == null || filePart.getSize() == 0) {
			return null;
		}

		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		String contentType = filePart.getContentType();
		String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

		List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
		List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png");

		if (!allowedExtensions.contains(fileExtension) || !allowedMimeTypes.contains(contentType)) {
			throw new Exception("Invalid image format! Only JPG, JPEG, PNG");
		}

		// Convert InputStream to temporary file
		File tempFile = File.createTempFile("upload-", fileName);
		try (InputStream inputStream = filePart.getInputStream()) {
			Files.copy(inputStream, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		}

		// Upload temp file to Cloudinary
		Map uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap("resource_type", "auto"));

		// Delete temp file after upload
		tempFile.delete();

		return (String) uploadResult.get("secure_url");
	}
}
