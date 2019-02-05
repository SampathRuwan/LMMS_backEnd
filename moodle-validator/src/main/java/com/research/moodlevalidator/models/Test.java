package com.research.moodlevalidator.models;

import java.util.ArrayList;
import java.util.List;

public class Test {
	private List<MoodleResources> resourcesList;
	private String sectionType;
	private String newsForum;
	private List<TopicHeading> headingDetailList;
	int size;
	int sizeP,sizeN;
	
	public Test() {
		this.resourcesList = new ArrayList<MoodleResources>();
		this.headingDetailList = new ArrayList<TopicHeading>();
	}

	public int getSizeP() {
		return sizeP;
	}

	public void setSizeP(int sizeP) {
		this.sizeP = sizeP;
	}

	public int getSizeN() {
		return sizeN;
	}

	public void setSizeN(int sizeN) {
		this.sizeN = sizeN;
	}
	
	public String getNewsForum() {
		return newsForum;
	}

	public void setNewsForum(String newsForum) {
		this.newsForum = newsForum;
	}

	public String getSectionType() {
		return sectionType;
	}

	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}

	public List<MoodleResources> getResourcesList() {
		return resourcesList;
	}

	public void setResourcesList(List<MoodleResources> resourcesList) {
		this.resourcesList = resourcesList;
	}
	
	public List<TopicHeading> getHeadingDetailList() {
		return headingDetailList;
	}

	public void setHeadingDetailList(List<TopicHeading> headingDetailList) {
		this.headingDetailList = headingDetailList;
	}
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
