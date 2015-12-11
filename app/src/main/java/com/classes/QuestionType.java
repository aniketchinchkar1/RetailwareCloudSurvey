package com.classes;

public class QuestionType {
	int QuestionTypeID;
	String QuestionTypeName,IsActive;
	
	public QuestionType(int questionTypeID, String questionTypeName,
			String isActive)
	{
		
		this.QuestionTypeID = questionTypeID;
		this.QuestionTypeName = questionTypeName;
		this.IsActive = isActive;
	}

	public int getQuestionTypeID() {
		return QuestionTypeID;
	}

	public void setQuestionTypeID(int questionTypeID) {
		QuestionTypeID = questionTypeID;
	}

	public String getQuestionTypeName() {
		return QuestionTypeName;
	}

	public void setQuestionTypeName(String questionTypeName) {
		QuestionTypeName = questionTypeName;
	}

	public String getIsActive() {
		return IsActive;
	}

	public void setIsActive(String isActive) {
		IsActive =isActive;
	}
}
