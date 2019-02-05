package com.research.moodlevalidator.models;

import java.util.ArrayList;
import java.util.List;

public class QuizResources {

	private String checkFileFormat;
	private String analyzeType;
	private int totalQuestions;
	private int multiChoice;
	private int trueFalse;
	private int essayQues;
	private int shortAnsQues;
	private int matchingQues;
	private int numericalQues;
	private int noOfSingleAnswers;
	private int noOfMultipleAnswers;
	private List<Questions> AllQues;
//	private List<Questions> AllSingleQues;
//	private List<Questions> AllMultiQues;
	private List<Questions> correctAnsQ;
	private List<Questions> incorrectAnsQ;
	private List<String> userSelections;
	
	public QuizResources(){
		this.AllQues = new ArrayList<Questions>();
		this.correctAnsQ = new ArrayList<Questions>();
		this.incorrectAnsQ = new ArrayList<Questions>();
		this.userSelections = new ArrayList<String>();
	}
	public String getCheckFileFormat() {
		return checkFileFormat;
	}
	public void setCheckFileFormat(String checkFileFormat) {
		this.checkFileFormat = checkFileFormat;
	}
	public String getAnalyzeType() {
		return analyzeType;
	}

	public void setAnalyzeType(String analyzeType) {
		this.analyzeType = analyzeType;
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	
	public int getMultiChoice() {
		return multiChoice;
	}

	public void setMultiChoice(int multiChoice) {
		this.multiChoice = multiChoice;
	}

	public int getTrueFalse() {
		return trueFalse;
	}

	public void setTrueFalse(int truefalse) {
		this.trueFalse = truefalse;
	}

	public int getEssayQues() {
		return essayQues;
	}

	public void setEssayQues(int essayQues) {
		this.essayQues = essayQues;
	}

	public int getShortAnsQues() {
		return shortAnsQues;
	}

	public void setShortAnsQues(int shortAnsQues) {
		this.shortAnsQues = shortAnsQues;
	}

	public int getMatchingQues() {
		return matchingQues;
	}

	public void setMatchingQues(int matchingQues) {
		this.matchingQues = matchingQues;
	}

	public int getNumericalQues() {
		return numericalQues;
	}

	public void setNumericalQues(int numericalQues) {
		this.numericalQues = numericalQues;
	}
	
	public List<Questions> getCorrectAnsQ() {
		return correctAnsQ;
	}
	
	public void setCorrectAnsQ(List<Questions> correctAnsQ) {
		this.correctAnsQ = correctAnsQ;
	}
	
	public List<Questions> getIncorrectAnsQ() {
		return incorrectAnsQ;
	}
	
	public void setIncorrectAnsQ(List<Questions> incorrectAnsQ) {
		this.incorrectAnsQ = incorrectAnsQ;
	}
	
	public int getNoOfSingleAnswers() {
		return noOfSingleAnswers;
	}
	
	public void setNoOfSingleAnswers(int noOfSingleAnswers) {
		this.noOfSingleAnswers = noOfSingleAnswers;
	}
	
	public int getNoOfMultipleAnswers() {
		return noOfMultipleAnswers;
	}
	
	public void setNoOfMultipleAnswers(int noOfMultipleAnswers) {
		this.noOfMultipleAnswers = noOfMultipleAnswers;
	}

	public List<Questions> getAllQues() {
		return AllQues;
	}

	public void setAllQues(List<Questions> allQues) {
		AllQues = allQues;
	}
	public List<String> getUserSelections() {
		return userSelections;
	}
	public void setUserSelections(List<String> userSelections) {
		this.userSelections = userSelections;
	}
	
	
}
