package com.sapient.csvtoxml.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sapient.csvtoxml.common.util.CXCommonConstant;
import com.sapient.csvtoxml.common.util.CXCommonUtil;
import com.sapient.csvtoxml.error.CXInvalidDSException;
import com.sapient.csvtoxml.error.CXInvalidFormatException;
import com.sapient.csvtoxml.error.CXServerException;
import com.sapient.csvtoxml.mapper.CXDataMapperException;
import com.sapient.csvtoxml.service.CXUploadService;

@RestController
@RequestMapping(value = "/api/userfiles")
public class CXUploadController {

	@Autowired
	CXCommonUtil commonUtil;

	@Autowired
	CXUploadService uploadService;

	private static final Logger logger = LoggerFactory.getLogger(CXUploadController.class);

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<?> upload(@PathVariable("file") MultipartFile file) {
		System.out.println("UploadController-upload");
		logger.debug("Inside upload controller");
		logger.info("inside the first controll****************************************");
		String msg = "text";
		String fileName = null;
		System.out.println("file " + file);
		if (!file.isEmpty()) {
			System.out.println("file is not empty");
			fileName = file.getOriginalFilename();
			System.out.println("fileName" + fileName);

			System.out.println(commonUtil.getFileExtn(fileName));
			String fileExtn = commonUtil.getFileExtn(fileName);
			if (!fileExtn.equalsIgnoreCase(CXCommonConstant.FILE_EXTN)) {
				throw new CXInvalidFormatException("Invalid File Format");
			}
			String csvType = commonUtil.getCSVType(fileName);
			InputStream stream;
			try {
				stream = file.getInputStream();
				java.util.Scanner scanner = new java.util.Scanner(stream, "UTF-8");
				String headerLine = scanner.hasNext() ? scanner.next() : "";
				System.out.println("header is " + headerLine);
				while (scanner.hasNext()) {
					// System.out.println("file line " + scanner.next());
					String dataLine = scanner.next();
					HashMap<String, String> dataMap = (HashMap<String, String>) commonUtil.createReqData(headerLine,
							dataLine);
					System.out.println("dataMap" + dataMap);
					uploadService.execute(csvType, dataMap);

				}

				// call the service class

			} catch (IOException | CXDataMapperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new CXServerException("Server Processing failed");
			}

			msg = "You have successfully uploaded " + fileName;
			System.out.println("done successfully");

		} else {
			System.out.println("file is empty");
			throw new CXInvalidDSException("File is empty");
		}
		// return new ResponseEntity<>(msg, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
