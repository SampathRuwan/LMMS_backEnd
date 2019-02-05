package com.research.moodlevalidator.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class QuizService {

	public StorageService storageService;
	
	public QuizService(){
		storageService = new StorageService();
	}
	
	public void MoodleQuizValidator() {
		String filePath = "";
		int multiCount = 0;
		int trueFalseCount = 0;
		int totalQuestions = 0;
		
		if(storageService.getFilePath()!= null){
			filePath = storageService.getFilePath();	
		}
		
//		filePath = storageService.getFilePath().toString();
		System.out.println("file path is : "+filePath);
		
		File xmlFile = new File(filePath);
		
		if(xmlFile.exists()){
			System.out.println("File exists");
//			System.out.println(filePath);
		}else{
			System.out.println("File does not exists");
		}
	}
	
}
