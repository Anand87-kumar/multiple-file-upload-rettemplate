package com.anand.controller;

//Java Program to Create Rest Controller 
//that Defines various API for file handling

//Importing required classes
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//Annotation
@RestController
public class FileController {
	
	// Uploading a file
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(@RequestParam("file") MultipartFile file){

		// Setting up the path of the file
		String filePath = System.getProperty("user.home") + "/Pictures/Task 27 - Crud Operations.pdf";
		String fileUploadStatus;
		
		// Try block to check exceptions
		try {
			
			// Creating an object of FileOutputStream class 
			FileOutputStream fout = new FileOutputStream(filePath);
			fout.write(file.getBytes());
			
			// Closing the connection 
			fout.close();
			fileUploadStatus = "File Uploaded Successfully";
			
		} 
	
		// Catch block to handle exceptions
		catch (Exception e) {
			e.printStackTrace();
			fileUploadStatus = "Error in uploading file: " + e;
		}
		return fileUploadStatus;
	}
	
	// Getting list of filenames that have been uploaded
	@RequestMapping(value = "/getFiles", method = RequestMethod.GET)
	public String[] getFiles()
	{
		String folderPath = System.getProperty("user.home") +"/Pictures/Task 27 - Crud Operations.pdf";
		
		// Creating a new File instance
		File directory= new File(folderPath);
		
		// list() method returns an array of strings 
		// naming the files and directories 
		// in the directory denoted by this abstract pathname
		String[] filenames = directory.list();
		
		// returning the list of filenames
		return filenames;
		
	}
	
	@RequestMapping(value = "/download/{path:.+}", method = RequestMethod.GET)
	public ResponseEntity downloadFile(@PathVariable("path") String filename) {
	    // Directory path where files are uploaded
	    String fileUploadDir = System.getProperty("user.home") + "/Pictures/";

	    // Construct the file path to the requested file
	    String filePath = fileUploadDir + filename;

	    try {
	        // Create a File object
	        File file = new File(filePath);

	        // Check if the file exists and is a file (not a directory)
	        if (file.exists() && file.isFile()) {
	            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

	            HttpHeaders headers = new HttpHeaders();
	            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

	            return ResponseEntity.ok()
	                    .headers(headers)
	                    .contentLength(file.length())
	                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                    .body(resource);
	        } else {
	            return new ResponseEntity<>("File Not Found", HttpStatus.NOT_FOUND);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace(); 
	        return new ResponseEntity<>("File Not Found", HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	        e.printStackTrace(); 
	        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
