package com.openclassrooms.chatop.service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadService {

	private final Cloudinary cloudinary;

	/**
	 * Upload a file to Cloudinary.
	 * 
	 * @param multipartFile - The file to be uploaded.
	 * @return String - The URL of the uploaded file.
	 * @throws IOException - If an I/O exception occurs during file upload.
	 */
	public String uploadFile(MultipartFile multipartFile) throws IOException {
		return cloudinary.uploader() // Upload the file using the Cloudinary uploader
				// upload() takes the file's bytes and the public ID as parameters
				.upload(multipartFile.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
				.get("url") // Get the URL of the uploaded file
				.toString();
	}

}
