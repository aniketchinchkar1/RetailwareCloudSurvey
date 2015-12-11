package com.classes;

public class TemplateAnswer
{
	int SurveyTemplateID,QuestionID, AnswerSrNo,NextQuestionScreenID;
	String AnswerText, AnswerImage,Status,AnswerBitmap;

	public TemplateAnswer(int surveyTemplateID, int questionID, int answerSrNo,
			int nextQuestionScreenID, String answerText, String answerImage,
			String status) 
	{

		this.SurveyTemplateID = surveyTemplateID;
		this.QuestionID = questionID;
		this.AnswerSrNo = answerSrNo;
		this.NextQuestionScreenID = nextQuestionScreenID;
		this.AnswerText = answerText;
		this.AnswerImage = answerImage;
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
	public int getAnswerSrNo() {
		return AnswerSrNo;
	}
	public void setAnswerSrNo(int answerSrNo) {
		AnswerSrNo = answerSrNo;
	}
	public int getNextQuestionScreenID() {
		return NextQuestionScreenID;
	}
	public void setNextQuestionScreenID(int nextQuestionScreenID) {
		NextQuestionScreenID = nextQuestionScreenID;
	}
	public String getAnswerText() {
		return AnswerText;
	}
	public void setAnswerText(String answerText) {
		AnswerText = answerText;
	}
	public String getAnswerImage() {
		return AnswerImage;
	}
	public void setAnswerImage(String answerImage) {
		AnswerImage = answerImage;
	}

	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
}
