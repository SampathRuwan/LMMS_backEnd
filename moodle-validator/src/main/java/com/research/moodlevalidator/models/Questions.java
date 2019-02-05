package com.research.moodlevalidator.models;

import java.util.List;

public class Questions {
	private String questionName;
	private String questionType;
	private String mcqType;
	private List<String> quesAnswers;
	private List<String> answerMarks;
//	private List<String> correctAnswers;
//	private List<String> incorrectAnswers;
//	private List<>
	
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public List<String> getQueAnswers() {
		return quesAnswers;
	}
	public void setQueAnswers(List<String> queAnswers) {
		this.quesAnswers = queAnswers;
	}
	public List<String> getAnswerMarks() {
		return answerMarks;
	}
	public void setAnswerMarks(List<String> answerMarks) {
		this.answerMarks = answerMarks;
	}
	public String getMcqType() {
		return mcqType;
	}
	public void setMcqType(String mcqType) {
		this.mcqType = mcqType;
	}
	
	
}
