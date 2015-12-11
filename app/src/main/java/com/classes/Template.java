package com.classes;

public class Template
{
	int SurveyTemplateID,CustID,NoOfScreens;
	String TemplateName,MainLogo,HeadingText,ThankyouText,SendSMS,SMSText,SendEmail,EmailText,CreateCustomer,CreditLoyaltyPoint,NoOfPoints,IsActive;
	
	public Template(int surveyTemplateID, int custID, int noOfScreens,
			String templateName, String mainLogo, String headingText,
			String thankyouText, String sendSMS, String sMSText,
			String sendEmail, String emailText, String createCustomer,
			String creditLoyaltyPoint, String noOfPoints, String isActive)
	{
		
		this.SurveyTemplateID = surveyTemplateID;
		this.CustID = custID;
		this.NoOfScreens = noOfScreens;
		this.TemplateName = templateName;
		this.MainLogo = mainLogo;
		this.HeadingText = headingText;
		this.ThankyouText = thankyouText;
		this.SendSMS = sendSMS;
		this.SMSText = sMSText;
		this.SendEmail = sendEmail;
		this.EmailText = emailText;
		this.CreateCustomer = createCustomer;
		this.CreditLoyaltyPoint = creditLoyaltyPoint;
		this.NoOfPoints = noOfPoints;
		this.IsActive = isActive;
	}

	public int getSurveyTemplateID() {
		return SurveyTemplateID;
	}

	public void setSurveyTemplateID(int surveyTemplateID) {
		SurveyTemplateID = surveyTemplateID;
	}

	public int getCustID() {
		return CustID;
	}

	public void setCustID(int custID) {
		CustID = custID;
	}

	public int getNoOfScreens() {
		return NoOfScreens;
	}

	public void setNoOfScreens(int noOfScreens) {
		NoOfScreens = noOfScreens;
	}

	public String getTemplateName() {
		return TemplateName;
	}

	public void setTemplateName(String templateName) {
		TemplateName = templateName;
	}

	public String getMainLogo() {
		return MainLogo;
	}

	public void setMainLogo(String mainLogo) {
		MainLogo = mainLogo;
	}

	public String getHeadingText() {
		return HeadingText;
	}

	public void setHeadingText(String headingText) {
		HeadingText = headingText;
	}

	public String getThankyouText() {
		return ThankyouText;
	}

	public void setThankyouText(String thankyouText) {
		ThankyouText = thankyouText;
	}

	public String getSendSMS() {
		return SendSMS;
	}

	public void setSendSMS(String sendSMS) {
		SendSMS = sendSMS;
	}

	public String getSMSText() {
		return SMSText;
	}

	public void setSMSText(String sMSText) {
		SMSText = sMSText;
	}

	public String getSendEmail() {
		return SendEmail;
	}

	public void setSendEmail(String sendEmail) {
		SendEmail = sendEmail;
	}

	public String getEmailText() {
		return EmailText;
	}

	public void setEmailText(String emailText) {
		EmailText = emailText;
	}

	public String getCreateCustomer() {
		return CreateCustomer;
	}

	public void setCreateCustomer(String createCustomer) {
		CreateCustomer = createCustomer;
	}

	public String getCreditLoyaltyPoint() {
		return CreditLoyaltyPoint;
	}

	public void setCreditLoyaltyPoint(String creditLoyaltyPoint) {
		CreditLoyaltyPoint = creditLoyaltyPoint;
	}

	public String getNoOfPoints() {
		return NoOfPoints;
	}

	public void setNoOfPoints(String noOfPoints) {
		NoOfPoints = noOfPoints;
	}

	public String getIsActive() {
		return IsActive;
	}

	public void setIsActive(String isActive) {
		IsActive = isActive;
	}
}
