package com.research.moodlevalidator.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



//import com.fasterxml.jackson.annotation.JsonProperty;
import com.research.moodlevalidator.models.Credential;
import com.research.moodlevalidator.models.QuizResources;
import com.research.moodlevalidator.models.QuizStandards;
import com.research.moodlevalidator.models.Test;
import com.research.moodlevalidator.services.MoodleService;
import com.research.moodlevalidator.services.QuizService;
import com.research.moodlevalidator.services.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

//@CrossOrigin(origins = "http://localhost:4200", maxAge=3200)
@CrossOrigin(origins = "*", maxAge=3200)
@RestController
public class MoodleController {
	
	public MoodleService moodleService;
	public QuizService quizService;
	public StorageService storageService;
	List<String> files = new ArrayList<String>();
	
	public MoodleController() {
		moodleService = new MoodleService();
		storageService = new StorageService();
	}
//	start moodle page analyzer	
	@GetMapping("/moodle/validateresult")
	public Test getFile() throws IOException{
		return moodleService.MoodlePageValidator();
	}
	
	@GetMapping("/")
    public String openHome() throws IOException{
		return "index";
    }
	
	@PostMapping("/moodle/savecredentials")
	@ResponseBody
	public String getCredentials(@RequestBody Credential creds){
		moodleService.setCredentials(creds);
		return "got credentials !! ";
	}
//	end moodle page analyzer
//	start Moodle quiz file analyze
	@PostMapping("/moodle/upload")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			if(file.getContentType().equalsIgnoreCase("text/xml")){
				storageService.store(file);
				files.add(file.getOriginalFilename());
				message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			}else{
				message = "invalid";
			}
			return ResponseEntity.status(HttpStatus.OK).body(message);
			
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}
	
	@PostMapping("/moodle/savequizsettings")
	@ResponseBody
	public String getQuizStandards(@RequestBody QuizStandards quizSettings){
		storageService.setQuizStandards(quizSettings);
		return "got settings !!";
	}
	
	@GetMapping("/moodle/quizresults")
    public QuizResources Test() throws ParserConfigurationException, SAXException, IOException {
		return storageService.MoodleQuizValidator();
    }
		
//	end Moodle quiz file analyze
	
}
