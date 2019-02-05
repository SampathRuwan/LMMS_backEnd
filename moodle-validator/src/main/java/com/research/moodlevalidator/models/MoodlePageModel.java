package com.research.moodlevalidator.models;

import java.util.ArrayList;
import java.util.List;

public class MoodlePageModel {
	private List<String> positiveResource;
	private List<String> negativeReources;
	private List<String> positiveDescription;
	private List<String> negativeDescription;
	
	public MoodlePageModel() {
		this.positiveResource = new ArrayList<String>();
		this.negativeReources = new ArrayList<String>();
		this.positiveDescription = new ArrayList<String>();
		this.negativeDescription = new ArrayList<String>();
	}

	public List<String> getPositiveResource() {
		return positiveResource;
	}

	public void setPositiveResource(List<String> positiveResource) {
		this.positiveResource = positiveResource;
	}

	public List<String> getNegativeReources() {
		return negativeReources;
	}

	public void setNegativeReources(List<String> negativeReources) {
		this.negativeReources = negativeReources;
	}

	public List<String> getPositiveDescription() {
		return positiveDescription;
	}

	public void setPositiveDescription(List<String> positiveDescription) {
		this.positiveDescription = positiveDescription;
	}

	public List<String> getNegativeDescription() {
		return negativeDescription;
	}

	public void setNegativeDescription(List<String> negativeDescription) {
		this.negativeDescription = negativeDescription;
	}

	public void addValuesToNegativeResource(String value){
		this.negativeReources.add(value);
	}
	
	public void addValuesToPositiveResource(String value){
		this.positiveResource.add(value);
	}
	
	public void addValuesToNegativeDescription(String value){
		this.negativeDescription.add(value);
	}
	
	public void addValuesToPositiveDescription(String value){
		this.positiveDescription.add(value);
	}
}
