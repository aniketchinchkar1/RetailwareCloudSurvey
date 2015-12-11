package com.classes;

public class TemplateQuestion
{
	int SurveyTemplateID,QuestionID, QuestionTypeID, QuestionScreenID;
	String QuestionText;
	String ShowImage, Status;
	public TemplateQuestion(int surveyTemplateID, int questionID,
			int questionTypeID, String questionText, int questionScreenID,
			String showImage, String status)
	{
		
		this.SurveyTemplateID = surveyTemplateID;
		this.QuestionID = questionID;
		this.QuestionTypeID = questionTypeID;
		this.QuestionText = questionText;
		this.QuestionScreenID = questionScreenID;
		this.ShowImage = showImage;
		this.Status = status;
	}
	public int getSurveyTemplateID() {
		return SurveyTemplateID;
	}
	public void setSurveyTemplateID(int surveyTemplateID) {
		SurveyTemplateID = surveyTemplateID;
	}
	public int getQuestionID() {
		return QuestionID;
	}
	public void setQuestionID(int questionID) {
		QuestionID = questionID;
	}
	public int getQuestionTypeID() {
		return QuestionTypeID;
	}
	public void setQuestionTypeID(int questionTypeID) {
		QuestionTypeID = questionTypeID;
	}
	public String getQuestionText() {
		return QuestionText;
	}
	public void setQuestionText(String questionText) {
		QuestionText = questionText;
	}
	public int getQuestionScreenID() {
		return QuestionScreenID;
	}
	public void setQuestionScreenID(int questionScreenID) {
		QuestionScreenID = questionScreenID;
	}
	public String getShowImage() {
		return ShowImage;
	}
	public void setShowImage(String showImage) {
		ShowImage = showImage;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
}
