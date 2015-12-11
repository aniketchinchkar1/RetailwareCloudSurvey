package com.classes;

public class TemplateSubQuestion 
{
	int surveyTemplateID,subquestion_Id,questionID;String questionText1,Status;
	public TemplateSubQuestion(int surveyTemplateID, int subquestion_Id,
			int questionID, String questionText1,
			String status) 
	{
		// TODO Auto-generated constructor stub
		this.surveyTemplateID=surveyTemplateID;
		this.subquestion_Id=subquestion_Id;
		this.questionID=questionID;
		this.questionText1=questionText1;
		this.Status=status;
	}
	public int getSurveyTemplateID() {
		return surveyTemplateID;
	}
	public void setSurveyTemplateID(int surveyTemplateID) {
		this.surveyTemplateID = surveyTemplateID;
	}
	public int getSubquestion_Id() {
		return subquestion_Id;
	}
	public void setSubquestion_Id(int subquestion_Id) {
		this.subquestion_Id = subquestion_Id;
	}
	public int getQuestionID() {
		return questionID;
	}
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public String getQuestionText1() {
		return questionText1;
	}
	public void setQuestionText1(String questionText1) {
		this.questionText1 = questionText1;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}

}
