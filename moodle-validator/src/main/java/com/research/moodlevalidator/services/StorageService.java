package com.research.moodlevalidator.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
//import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.research.moodlevalidator.models.Questions;
import com.research.moodlevalidator.models.QuizResources;
import com.research.moodlevalidator.models.QuizStandards;

@Service
public class StorageService {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final Path rootLocation = Paths.get("Quiz-Files");
	public Path filePath;
	
//	checking questions standards
	public String analyzeType = ""; // basic_quiz_analyze , custom_quiz_analyze
	public int noOfAllQues = 0;
	public int noOfMCQs = 0;
	public int noOfMultiAns = 0;
	public int noOfSingleAns = 0;
	public String stdNoOfAns = "";
	public String checkShuffled = "";
	
	public void setQuizStandards(QuizStandards quiz){
		this.analyzeType = quiz.analyzeType;
		this.noOfAllQues = quiz.checkNoOfAllQues;
		this.noOfMCQs = quiz.checkNoOfMCQs;
		this.noOfMultiAns = quiz.checkNoOfMultiAns;
		this.noOfSingleAns = quiz.checkNoOfSingleAns;
		this.stdNoOfAns = quiz.checkNoOfAns;
		this.checkShuffled = quiz.checkShuffled;		
	}
	
	public void store(MultipartFile file) {	
		try {
			Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
			filePath = this.rootLocation.resolve(file.getOriginalFilename());
			
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
	
	public void deleteFile(){
		try{
			FileSystemUtils.deleteRecursively(filePath);	
		}catch (Exception e){
			System.out.println(e);
		}
	}
 	
	public void init() {
		try {
			Files.createDirectory(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}
	
//	returns the XML File path
	public String getFilePath(){
		if(filePath == null){
			return "empty File path!!";
		}else{
			return filePath.toString();
		}
	}
	public QuizResources MoodleQuizValidator() throws ParserConfigurationException, SAXException, IOException {
		String filePath = getFilePath();
		int multiCount = 0;
		int trueFalseCount = 0;
		int totalQuestions = 0;
		int shortAnsCount = 0;
		int matchingCount = 0;
		int essayCount = 0;
		int numericalCount = 0;
		int singleAnsCount = 0;
		int multiAnsCount = 0;
		
		QuizResources quizResource = new QuizResources();
		
		ArrayList<Questions> wholeQuesArray = new ArrayList<Questions>();
		ArrayList<Questions> stdAnswersArray = new ArrayList<Questions>();
		ArrayList<Questions> notStdAnswersArray = new ArrayList<Questions>();
		
		
		File xmlFile = new File(filePath);
		//sample location
//		File xmlFile = new File("C:\\Users\\SM_MYPC\\Desktop\\testLoc\\sample1.xml");
		
		if(!xmlFile.exists()){ //check file is exists or not
			return quizResource;
			
		}else{
//			read the xml document	
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document document = db.parse(xmlFile);
		
			NodeList list = document.getElementsByTagName("question");
			
			if(list.getLength() == 0){ //checks document is in moodle xml format
				quizResource.setCheckFileFormat("not_moodle_xml_format");
			}else{
				quizResource.setCheckFileFormat("moodle_xml_format");	
			}
			
//			starts analyzing
			quizResource.setAnalyzeType(analyzeType);
			
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				
				if(node.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element) node;
					
					String questionType = element.getAttribute("type");
					
					if(!questionType.equals("category")){
						totalQuestions += 1; //count the total questions
						//String name = element.getElementsByTagName("name").item(0).getTextContent();
						String questionName = element.getElementsByTagName("questiontext").item(0).getTextContent();
						
						int noOfAnswes = element.getElementsByTagName("answer").getLength();//get no of answers(each question)
						
						questionName = questionName.replaceAll("\\<.*?\\>", "").replace("\n", ""); //remove tags and new lines

						ArrayList<String> questionAnsArray = new ArrayList<String>();

//						get the answers marks 
						NodeList childNodes = element.getChildNodes();
						for (int j = 0; j < childNodes.getLength(); j++) {
							Node n = childNodes.item(j);
												
							if(n.getNodeType()== Node.ELEMENT_NODE){
								Element eleName = (Element) n;								
								if(eleName.getTagName().equals("answer")){
									String ansMarks = eleName.getAttribute("fraction");		
									questionAnsArray.add(ansMarks);
								}
							}
						}
												
//						create questions temporary array	
						Questions tempQues = new Questions();

						ArrayList<String> questionAnswers = new ArrayList<String>();
						
						for(int a = 0; a < noOfAnswes; a++){
							String t =element.getElementsByTagName("answer").item(a).getTextContent();
							t =t.replaceAll("\\<.*?\\>", "").replace("\n", ""); //remove all tags and new lines
							questionAnswers.add(t);
						}
						
						if(questionType.equalsIgnoreCase("multichoice") && 
								element.getElementsByTagName("single").item(0).getTextContent().
								equalsIgnoreCase("true")){
							String mcq = "single";
							tempQues.setMcqType(mcq);
						}
						if(questionType.equalsIgnoreCase("multichoice") && 
								element.getElementsByTagName("single").item(0).getTextContent().
								equalsIgnoreCase("false")){
							String mcq = "multiple";
							tempQues.setMcqType(mcq);
						}
						
//						add all questions to temporary array
						tempQues.setQuestionName(questionName);
						tempQues.setQueAnswers(questionAnswers);
						tempQues.setQuestionType(questionType);
						tempQues.setAnswerMarks(questionAnsArray);
						
//						add to Question Model
						wholeQuesArray.add(tempQues);
						
//						create standard answer's questions temporary array	
						Questions tempStdAnswers = new Questions();
						Questions tempNotStdAnswers = new Questions();
						
						ArrayList<String> stdAnswers = new ArrayList<String>();
						ArrayList<String> notStdAnswers = new ArrayList<String>();
						
						if(questionType.equals("multichoice")){ //check multiple question answer's count if
							if(analyzeType.equals("custom_quiz_analyze")){
								if(noOfAnswes == Integer.parseInt(stdNoOfAns)){//check with user's answer count 		
									questionName = questionName.replaceAll("\\<.*?\\>", "").replace("\n", ""); //remove tags and new lines
									
									for(int a = 0; a < noOfAnswes; a++){
										String stdAns =element.getElementsByTagName("answer").item(a).getTextContent();
										stdAns = stdAns.replaceAll("\\<.*?\\>", "").replace("\n", ""); 
										stdAnswers.add(stdAns);
									}
//									add all true questions to temporary array
									tempStdAnswers.setQuestionName(questionName);
									tempStdAnswers.setQueAnswers(stdAnswers);
//									tempStdAnswers.setAnswerMarks(testAns);
									
//									add to true Question Model
									stdAnswersArray.add(tempStdAnswers);

								}else{
									questionName = questionName.replaceAll("\\<.*?\\>", "").replace("\n", ""); //remove tags and new lines
									for(int a = 0; a < noOfAnswes; a++){
										String notStdAns =element.getElementsByTagName("answer").item(a).getTextContent();
										notStdAns = notStdAns.replaceAll("\\<.*?\\>", "").replace("\n", ""); 
										notStdAnswers.add(notStdAns);
									}
//									add all false questions to temporary array
									tempNotStdAnswers.setQuestionName(questionName);
									tempNotStdAnswers.setQueAnswers(notStdAnswers);
									
//									add to false Question Model
									notStdAnswersArray.add(tempNotStdAnswers);
								}
							}
							
//							NodeList chldNodes = element.getChildNodes();
//							for (int j = 0; j < childNodes.getLength(); j++) {
//								Node n = childNodes.item(j);
//													
//								if(n.getNodeType()== Node.ELEMENT_NODE){
//									Element eleName = (Element) n;
//									
//									if(eleName.getTagName().equals("answer")){
////										System.out.println(eleName.getAttribute("fraction"));
////										if(eleName.getAttribute("fraction").equals("100")){
////											System.out.println(eleName.getAttribute("fraction"));
////										}else if(eleName.getAttribute("fraction").equals("50")){
//////											multiAnsCount+=1;
////										}
//									}
//								}
//							}
						}//end if(multiChoice)				
					}
				
//					String questionType = element.getAttribute("type");
//					type can be multichoice |truefalse| shortanswer| matching| essay| numerical
					if(questionType.equalsIgnoreCase("multichoice")){
						multiCount +=1;	
					}
					if(questionType.equalsIgnoreCase("truefalse")){
						trueFalseCount +=1;	
					}
					if(questionType.equalsIgnoreCase("shortanswer")){
						shortAnsCount+=1;	
					}
					if(questionType.equalsIgnoreCase("matching")){
						matchingCount +=1;	
					}
					if(questionType.equalsIgnoreCase("essay")){
						essayCount +=1;	
					}
					if(questionType.equalsIgnoreCase("numerical")){
						numericalCount +=1;	
					}
					
					//check MCQ's single answer or multiple answers
					if(questionType.equalsIgnoreCase("multichoice") && 
						element.getElementsByTagName("single").item(0).getTextContent().
						equalsIgnoreCase("true")){
						
						 singleAnsCount+=1;	
					}
					if(questionType.equalsIgnoreCase("multichoice") && 
							element.getElementsByTagName("single").item(0).getTextContent().
							equalsIgnoreCase("false")){
						
						multiAnsCount +=1;
					}
					//end checks single/multiples
				}	
			}//end analyzing
			
//			System.out.println("Total Questions : " +noOfAllQues);	
//			System.out.println("no of multiple choice Q : "+ multiCount);
			
			ArrayList<String> selections = new ArrayList<String>();
			
//			send the user selections to front end
			selections.add(Integer.toString(noOfMCQs));
			selections.add(Integer.toString(noOfSingleAns));
			selections.add(Integer.toString(noOfMultiAns));
			selections.add(stdNoOfAns);
			selections.add(checkShuffled);
			quizResource.setUserSelections(selections);
			
			quizResource.setTotalQuestions(totalQuestions);
			
			quizResource.setMultiChoice(multiCount);
			quizResource.setTrueFalse(trueFalseCount);
			quizResource.setShortAnsQues(shortAnsCount);
			quizResource.setMatchingQues(matchingCount);
			quizResource.setEssayQues(essayCount);
			quizResource.setNumericalQues(numericalCount);
			quizResource.setCorrectAnsQ(stdAnswersArray);
			quizResource.setIncorrectAnsQ(notStdAnswersArray);
			quizResource.setNoOfSingleAnswers(singleAnsCount);
			quizResource.setNoOfMultipleAnswers(multiAnsCount);
			quizResource.setAllQues(wholeQuesArray);
			deleteFile(); //delete the uploaded file after analyze....
			
			return quizResource;
//			end	
		}
	}	

//	Moodle quiz validation/analyzing ends here
}
