package com.research.moodlevalidator.services;

import java.io.IOException;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import com.research.moodlevalidator.models.Credential;
import com.research.moodlevalidator.models.MoodleResources;
import com.research.moodlevalidator.models.Test;
import com.research.moodlevalidator.models.TopicHeading;

public class MoodleService {
	String username = "";
	String pwd = ""; 
	String loginUrl = ""; //http://courseweb.sliit.lk/login/index.php
	String pageUrl = ""; //http://courseweb.sliit.lk/course/view.php?id=18
	String credentialType = ""; //no_needs_cred or needs_cred
	String standardType = ""; //moodle_standard_validation or moodle_custom_validation
	
//	http://www.e-thaksalawa.moe.gov.lk/moodle/course/view.php?id=842&lang=si
//	http://courseweb.sliit.lk/course/view.php?id=18
	 
	// moodle fields
    String moodleTopicType = "";
    String weeklyDesc = "";
    String resourcesName = "";
    String resourcesDesc = "";
    String lecturesName = "";
    String lectureNumber = "";
    String lectureCharacter = "";
    
	public void setCredentials(Credential cred){
		this.username = cred.username;
		this.pwd = cred.pwd;
		this.loginUrl = cred.loginUrl;
		this.pageUrl = cred.pageUrl;
		this.standardType = cred.standardType;
		this.credentialType = cred.credentialType;
//	 Moodle fields
	    this.moodleTopicType = cred.moodleTopicType;
	    this.weeklyDesc = cred.weeklyDesc;
	    this.resourcesName = cred.resourcesName;
	    this.resourcesDesc = cred.resourcesDesc;
	    this.lecturesName = cred.lecturesName;
	    this.lectureNumber = cred.lectureNumber;
	    this.lectureCharacter = cred.lectureCharacter;
	}
	
	public Test MoodlePageValidator() throws IOException{
		
		Document doc2 = null;
		if(credentialType.equals("needs_cred")){
			Connection.Response res = Jsoup.connect(loginUrl).data("username", username, 
	                "password", pwd).method(Method.POST).execute();
			
			Map<String, String> sessionId = res.cookies();
			doc2 = Jsoup.connect(pageUrl)
				    .cookies(sessionId)
				    .get();	
			
		}if(credentialType.equals("no_needs_cred")){
			doc2 = Jsoup.connect(pageUrl).get();
		}
		
		Elements ele = doc2.select("div.course-content"); //main content div
		
		Elements generalSection = doc2.select("li#section-0");
		generalSection.remove(); //remove general section from the document.
		
		String type = ele.select("ul").attr("class");
		if(moodleTopicType != null && moodleTopicType.equals("topic_wise")){
			if(type.equals("topics")){	
				type = "topic_wise";
			}else{
				type = "not_topic_wise";
			}
		}else if(moodleTopicType != null && moodleTopicType.equals("date_wise")){
			if(type.equals("Weeks")){	
				type = "date_wise";
			}else{
				type = "not_date_wise";
			}
		}

		String checkNewsForum = "no forum";
		//get remove section elements to check exists of the news forum.
		for (Element elementGeneral : generalSection.select("li.activity")) {
			String checkForum = elementGeneral.select("div.activityinstance").text();
			
			if(checkForum.equalsIgnoreCase("news forum")){
				checkNewsForum = checkForum;
			}
		}
		ArrayList<MoodleResources> resourceList = new ArrayList<MoodleResources>();

		String customReg = "";
		Test testModel = new Test();
		
		String regex1 = "((lecture|Lecture|Lab Sheet|lab sheet|Quiz|quiz|Tutorial |Tute |Assignment).([0-9]|[1-9][0-9]).[-|:].[A-Z].*)";
		String regex2 = "((lecture|Lecture|Lab Sheet|lab sheet|Quiz|quiz|Tutorial |Tute |Assignment).[-|:].[A-Z].*)";
		
		if(lecturesName != null && lectureNumber != null && lectureCharacter != null){
			if(lecturesName != null && lectureNumber.equals("numNotAdd") && lectureCharacter.equals("none")){
				customReg = "(("+lecturesName+").*)";	
			}
			else if(lecturesName != null && lectureNumber.equals("numNotAdd") && lectureCharacter.equals(":")){
				customReg = "(("+lecturesName+").[:].[A-Z].*)";
			}
			else if(lecturesName != null && lectureNumber.equals("numNotAdd") && lectureCharacter.equals("-")){
				customReg = "(("+lecturesName+").[-].[A-Z].*)";
			}
			else if(lecturesName != null && lectureNumber.equals("numNotAdd") && lectureCharacter.equals("=")){
				customReg = "(("+lecturesName+").[=].[A-Z].*)";
			}
			else if(lecturesName != null && lectureNumber.equals("numAdd") && lectureCharacter.equals("none")){
				customReg = "(("+lecturesName+").([0-9]|[1-9][0-9]).[A-Z].*)";
			}
			else if(lecturesName != null && lectureNumber.equals("numAdd") && lectureCharacter.equals(":")){
				customReg = "(("+lecturesName+").([0-9]|[1-9][0-9]).[:].[A-Z].*)";
			}
			else if(lecturesName != null && lectureNumber.equals("numAdd") && lectureCharacter.equals("-")){
				customReg = "(("+lecturesName+").([0-9]|[1-9][0-9]).[-].[A-Z].*)";
			}
			else if(lecturesName != null && lectureNumber.equals("numAdd") && lectureCharacter.equals("=")){
				customReg = "(("+lecturesName+").([0-9]|[1-9][0-9]).[=].[A-Z].*)";
			}
			else
				customReg = "(("+lecturesName+").*)";
		}
		
		//find resources standards for loop
		for(Element element : ele.select("li.activity")) {
			String resource = element.select("div.activityinstance").text();
			String contentafterlink = element.select("div.contentafterlink").text();
			String resourceType = element.select("span.accesshide").text();
			
			if(resource.equals("")){
//				System.out.println(resource + " : empty");
			}else{
				MoodleResources tempResource = new MoodleResources();
				int lastIndexOfResource = resource.lastIndexOf(" ");
				resource = resource.substring(0,lastIndexOfResource);
				
				tempResource.setResourceName(resource);
				tempResource.setResourceType(resourceType);
				
				if(standardType.equals("moodle_standard_validation")){
					if(resource.matches(regex1) || resource.matches(regex2)){
						tempResource.setName("correct_name_format");
						//positiveValue += 1;
					}
					else{
						tempResource.setName("incorrect_name_format");
						//negativeValue += 1;
					}
						
					if(contentafterlink.equals("")){
						tempResource.setDescription("no_resource_desc");
					}else{
						tempResource.setDescription("resource_desc");
					}	
				}
				
				if(standardType.equals("moodle_custom_validation")){
					if(resourcesName == null || resourcesName.equals("checked")){
						if(resource.matches(customReg)){
							tempResource.setName("correct_name_format");
							//positiveValue += 1;
						}
						else{
							tempResource.setName("incorrect_name_format");
							//negativeValue += 1;
						}
					}
					
					if(resourcesDesc == null|| resourcesDesc.equals("checked")){	
						if(contentafterlink.equals("")){
							tempResource.setDescription("no_resource_desc");
						}else{
							tempResource.setDescription("resource_desc");
						}	
					}
				}
				//add resource results to the MoodleResources Model
				resourceList.add(tempResource);		
			}
			
		}
		//end for loop
		
		//find topic heading standard for loop
		ArrayList<TopicHeading> headingDetailList = new ArrayList<TopicHeading>(); 
		
		for (Element element : ele.select("li.section")) {
			String heading = element.select("h3.sectionname").text();
			String headingSummary = element.select("div.summary").text();
			
			if(heading.equals("")){
//				tempTopicHeading.setCheckHeadingName("no_topic");			
			}else{
				TopicHeading tempTopicHeading = new TopicHeading();
				tempTopicHeading.setCheckHeadingName("topic");
				tempTopicHeading.setHeadingName(heading);
				
				if(weeklyDesc == null || weeklyDesc.equals("checked")){
					if(headingSummary.equals("")){
						tempTopicHeading.setCheckheadingDesc("no_desc");
					}else{
						tempTopicHeading.setCheckheadingDesc("desc");
					}
				}
				headingDetailList.add(tempTopicHeading);
			}	
		}
		//end heading for loop
		
		testModel.setResourcesList(resourceList);
		testModel.setSectionType(type);
		testModel.setNewsForum(checkNewsForum);
		testModel.setHeadingDetailList(headingDetailList);
		
		return testModel;
	}
	
}
