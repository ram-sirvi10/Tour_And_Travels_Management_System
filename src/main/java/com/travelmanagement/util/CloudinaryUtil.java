package com.travelmanagement.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.http.Part;

public class CloudinaryUtil {
	   private static Cloudinary cloudinary;

	    static {
	        try {
	            Properties props = new Properties();
	            try (InputStream is = CloudinaryUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
	                props.load(is);
	            }
	            String cloudName = props.getProperty("cloudinary.cloud_name");
	            String apiKey = props.getProperty("cloudinary.api_key");
	            String apiSecret = props.getProperty("cloudinary.api_secret");
	            boolean secure = Boolean.parseBoolean(props.getProperty("cloudinary.secure"));

	            cloudinary = new Cloudinary(ObjectUtils.asMap(
	                    "cloud_name", cloudName,
	                    "api_key", apiKey,
	                    "api_secret", apiSecret,
	                    "secure", secure
	            ));
	        } catch (Exception e) {
	            throw new RuntimeException("Failed to initialize Cloudinary", e);
	        }
	    }
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

		File tempFile = File.createTempFile("upload-", fileName);
		try (InputStream inputStream = filePart.getInputStream()) {
			Files.copy(inputStream, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		}

		Map<?, ?> uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap("resource_type", "auto"));

		tempFile.delete();

		return (String) uploadResult.get("secure_url");
	}
}
