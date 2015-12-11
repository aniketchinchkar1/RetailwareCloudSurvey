package com.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.classes.QuestionType;
import com.classes.Template;
import com.classes.TemplateAnswer;
import com.classes.TemplateQuestion;
import com.classes.TemplateSubQuestion;
import com.classes.Users;


public class DatabaseHelper extends SQLiteOpenHelper 
{
	private SQLiteDatabase db= null;
	public DatabaseHelper(Context context) 
	{
		super(context, "RetailwareSurvey", null,1);
		db=this.getWritableDatabase();
	}	

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(" CREATE TABLE CS_Users(_id INTEGER PRIMARY KEY AUTOINCREMENT,UserID Numeric,SubscriptionID Numeric, Fullname TEXT, Companyname TEXT, Contactperson TEXT, Mobileno TEXT, Address1 TEXT, Address2 TEXT, City TEXT, Price TEXT, Retailwarecustomer TEXT,  Emailid TEXT, Password TEXT, IsAdmin TEXT, IsActive TEXT, CreationDate TEXT, CreationTime TEXT, LastmodifiedDate TEXT, Lastmodifiedtime TEXT, Expiry_Date TEXT) ");
		db.execSQL(" CREATE TABLE CS_QuestionType(_id INTEGER PRIMARY KEY AUTOINCREMENT,QuestionTypeID Numeric,QuestionTypeName Text, IsActive Text) ");
		db.execSQL(" CREATE TABLE CS_Template(_id INTEGER PRIMARY KEY AUTOINCREMENT,SurveyTemplateID Numeric, CustID Numeric,TemplateName TEXT,NoOfScreens Numeric, MainLogo TEXT, HeadingText TEXT, ThankyouText TEXT, SendSMS TEXT, SMSText TEXT, SendEmail TEXT, EmailText TEXT, CreateCustomer TEXT, CreditLoyaltyPoint TEXT, NoOfPoints Numeric, IsActive TEXT)");
		db.execSQL(" CREATE TABLE CS_TemplateQuestion (_id INTEGER PRIMARY KEY AUTOINCREMENT,SurveyTemplateID Numeric,QuestionID Numeric, QuestionTypeID Numeric, QuestionText Text, ShowImage Text,QuestionScreenID Numeric, Status Text) ");
		db.execSQL(" CREATE TABLE CS_TemplateAnswer(_id INTEGER PRIMARY KEY AUTOINCREMENT,SurveyTemplateID Numeric,QuestionID Numeric,AnswerSrNo Numeric,AnswerText Text,AnswerImage Text,NextQuestionScreenID Numeric, Status Text) ");
		db.execSQL(" CREATE TABLE CS_SubQuestion(_id INTEGER PRIMARY KEY AUTOINCREMENT,SurveyTemplateID Numeric,SubQuestionID Numeric, QuestionID Numeric, AnswerText Text,Status Text) ");
		db.execSQL(" CREATE TABLE Options(HTTPUrl Text,HTTPPortNo Text,DomainName Text,EulaAccepted Text,Activated Text)");
		db.execSQL(" CREATE TABLE CS_Session(_id INTEGER PRIMARY KEY AUTOINCREMENT,GUID TEXT,UserID INTEGER,QuestionScreenID Numeric,QuestionID INTEGER, AnswerSrNo TEXT, AnswerText TEXT, DeviceIMEINumber TEXT,ImageAns Text,OfflineFlag Text) ");
		db.execSQL(" CREATE TABLE CS_BackSession(_id INTEGER PRIMARY KEY AUTOINCREMENT,QuestionID INTEGER, AnswerText TEXT)");
		db.execSQL(" CREATE TABLE CS_OfflineCount(_id INTEGER PRIMARY KEY AUTOINCREMENT,OfflineFlag Text)");
		db.execSQL(" INSERT INTO Options(HTTPUrl,HTTPPortNo,DomainName,EulaAccepted,Activated) VALUES('http://www.google.com/glm/mmap',null,'http://docontouch.com/','0','0')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS CS_Users;");
		db.execSQL("DROP TABLE IF EXISTS CS_QuestionType;");
		db.execSQL("DROP TABLE IF EXISTS CS_Template;");
		db.execSQL("DROP TABLE IF EXISTS CS_TemplateQuestion;");
		db.execSQL("DROP TABLE IF EXISTS CS_TemplateAnswer;");
		db.execSQL("DROP TABLE IF EXISTS CS_Session;");
		db.execSQL("DROP TABLE IF EXISTS Options;");
		db.execSQL("DROP TABLE IF EXISTS CS_OfflineCount;");
		onCreate(db);
	}
	//-------------------------------------------------
	public void InsertData(String StrSQL)
	{
		db.execSQL(StrSQL);
	}

	public void UpdateData(String StrSQL)
	{
		db.execSQL(StrSQL);
	}

	public void DeleteData(String StrSQL)
	{
		db.execSQL(StrSQL);
	}


	//-------------------------------------------------

	/*void AddCustomers(Customers c)
	{
		 SQLiteDatabase db= this.getWritableDatabase();
		 ContentValues cv=new ContentValues();
		 cv.put( "CustID", c.GetCustID());
		 cv.put( "DisplayName", c.GetDisplaytName());
		 cv.put( "MobileNo", c.GetMobileNo());
		 cv.put( "Outstanding", c.GetOutstanding());
		 cv.put( "AMCDueDate", c.GetAMCDueDate());
		 cv.put( "ContactPerson", c.GetContactPerson());
		 cv.put( "City", c.GetCity());
		 cv.put( "AsOnDate", c.GetAsOnDate());

		 db.insert("Customers", null, cv);
		 db.close();
	}*/

	public void AddUsers(Users u)
	{
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put( "UserID", u.GetUserID());
		cv.put( "SubscriptionID", u.GetSubscriptionID());
		cv.put( "Fullname", u.GetFullname());
		cv.put( "Companyname", u.GetCompanyname());
		cv.put( "Contactperson", u.GetContactperson());
		cv.put( "Mobileno", u.GetMobileno());
		cv.put( "Address1", u.GetAddress1());
		cv.put( "Address2", u.GetAddress2());
		cv.put( "City", u.GetCity());
		cv.put( "Price", u.GetPrice());
		cv.put( "Retailwarecustomer", u.GetRetailwarecustomer());
		cv.put( "Emailid", u.GetEmailid());
		cv.put( "Password", u.GetPassword());
		cv.put( "IsActive", u.GetIsActive());
		cv.put( "CreationDate", u.GetCreationDate());
		cv.put( "CreationTime", u.GetCreationTime());
		cv.put( "LastmodifiedDate", u.GetLastmodifiedDate());
		cv.put( "Lastmodifiedtime", u.GetLastmodifiedtime());
		cv.put( "Expiry_Date", u.GetExpiry_Date());

		db.insert("CS_Users", null, cv);
		db.close();
	}

	public void AddTemplateQuestion(TemplateQuestion Qtemp) 
	{
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put( "SurveyTemplateID", Qtemp.getSurveyTemplateID());
		cv.put( "QuestionID", Qtemp.getQuestionID());
		cv.put( "QuestionTypeID", Qtemp.getQuestionTypeID());
		cv.put( "QuestionText", Qtemp.getQuestionText());
		cv.put( "QuestionScreenID", Qtemp.getQuestionScreenID());
		cv.put( "ShowImage", Qtemp.getShowImage());
		cv.put( "Status", Qtemp.getStatus());
		db.insert("CS_TemplateQuestion", null, cv);
		db.close();

	}
	public void AddTemplateSubQuestion(TemplateSubQuestion Qtemp) 
	{
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put( "SurveyTemplateID", Qtemp.getSurveyTemplateID());
		cv.put( "SubQuestionID", Qtemp.getSubquestion_Id());
		cv.put( "QuestionID", Qtemp.getQuestionID());
		cv.put( "AnswerText", Qtemp.getQuestionText1());
		cv.put( "Status", Qtemp.getStatus());
		db.insert("CS_SubQuestion", null, cv);
		db.close();

	}
	public void AddTemplateAnswer(TemplateAnswer Atemp)
	{
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put( "SurveyTemplateID", Atemp.getSurveyTemplateID());
		cv.put( "QuestionID", Atemp.getQuestionID());
		cv.put( "AnswerSrNo", Atemp.getAnswerSrNo());
		cv.put( "NextQuestionScreenID", Atemp.getNextQuestionScreenID());
		cv.put( "AnswerText", Atemp.getAnswerText());
		cv.put( "AnswerImage", Atemp.getAnswerImage());
		cv.put( "Status", Atemp.getStatus());
		db.insert("CS_TemplateAnswer", null, cv);
		db.close();

	}
	public void AddTemplate(Template temp)
	{
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put( "SurveyTemplateID", temp.getSurveyTemplateID());
		cv.put( "CustID", temp.getCustID());
		cv.put( "NoOfScreens", temp.getNoOfScreens());
		cv.put( "TemplateName", temp.getTemplateName());
		cv.put( "MainLogo", temp.getMainLogo());
		cv.put( "HeadingText", temp.getHeadingText());
		cv.put( "ThankyouText", temp.getThankyouText());
		cv.put( "SendSMS", temp.getSendSMS());
		cv.put( "SMSText", temp.getSMSText());
		cv.put( "SendEmail", temp.getSendEmail());
		cv.put( "EmailText", temp.getEmailText());
		cv.put( "CreateCustomer", temp.getCreateCustomer());
		cv.put( "CreditLoyaltyPoint", temp.getCreditLoyaltyPoint());
		cv.put( "NoOfPoints", temp.getNoOfPoints());
		cv.put( "IsActive", temp.getIsActive());


		db.insert("CS_Template", null, cv);
		db.close();
	}
	public void AddQuestionType(QuestionType qType) 
	{
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put( "QuestionTypeID", qType.getQuestionTypeID());
		cv.put( "QuestionTypeName", qType.getQuestionTypeName());
		cv.put( "IsActive", qType.getIsActive());
		db.insert("CS_QuestionType", null, cv);
		db.close();
	}
	public void UpdateHTTPUrl(String Value)
	{
		db.execSQL(" Update Options Set HTTPUrl='"+ Value +"' ");
	}

	public void UpdateHTTPPortNo(String Value)
	{
		db.execSQL(" Update Options Set HTTPPortNo='"+ Value +"' ");
	}

	public void UpdateDomainName(String Value)
	{
		db.execSQL(" Update Options Set DomainName='"+ Value +"' ");
	}

	public void UpdateEulaAccepted(String Value)
	{
		db.execSQL(" Update Options Set EulaAccepted='"+ Value +"' ");
	}

	public void UpdateActivated(String Value)
	{
		db.execSQL(" Update Options Set Activated='"+ Value +"' ");
	}

	public int GetOptionsAll()
	{
		int Activated;
		try
		{
			//SQLiteDatabase db=this.getWritableDatabase();
			//Cursor cur=db.rawQuery("Select SurveyTemplateID from CS_Template where SurveyTemplateID='"+Name+"' ",null);
			Cursor cur= db.rawQuery(" Select Activated From Options ",null);
			//String Value;

			if (cur.getCount()>0)
			{
				cur.moveToFirst();

				Activated=Integer.parseInt(cur.getString(0).toString());
				//db.close();
				cur.close();
			}
			else
			{
				Activated=0; 
				//db.close();
				cur.close();
			}
		}
		catch(Exception ex)
		{
			return 0;
		}
		return Activated;
	}

	/*	public String GetDBPath()
	{
		SQLiteDatabase db=this.getWritableDatabase();
		String s =db.getPath();
		db.close();
		return s; 
	}   */

	public void CloseDB()
	{
		if(db!=null)
		{
			db.close();
		}
	}


	/*	public Cursor GetSugarReport()
	{
		try
		{
			SQLiteDatabase db=this.getWritableDatabase();
			Cursor cur= db.rawQuery("SELECT _id,ReportDate,Fasting,PostPartum,Random,Remark,FastingTime,PostPrandialTime,RandomTime From SugarlevelReport order by date(ReportDate) ",null);
			db.close();
			return cur;

		}
		catch(Exception ex)
		{
			return null;
		}
	}   */

	/*	public List<String> getList(String flag){
		List<String> labels = null ;
		try{

			labels = new ArrayList<String>();
			String selectQuery = null ;
			int colomNumber = 0;
			// Select All Query

			if(flag.equalsIgnoreCase("city")){
				selectQuery = "SELECT * FROM City";
				colomNumber=1;
			}
			if(flag.equalsIgnoreCase("speciality")){
				selectQuery = "SELECT * FROM Speciality";
				colomNumber=2;
			}

			// Change for to remove keyword dependancy from speciality.

			/*if(flag.equalsIgnoreCase("keyword")){
			int specialityid=Integer.parseInt(HomeFragment.SpecialityID);
			selectQuery = "SELECT * FROM Keywords where SpecialityID = " + specialityid;
			colomNumber=3;
		}

			if(flag.equalsIgnoreCase("Area")){
				selectQuery = "SELECT * FROM City";
				colomNumber=1;
			}

			if(flag.equalsIgnoreCase("keyword")){
				selectQuery = "SELECT * FROM Keywords ";
				colomNumber=3;
			}
			if(flag.equalsIgnoreCase("doctor")){
				selectQuery = "SELECT * FROM Doctor ";
				colomNumber=2;
			}
			if(flag.equalsIgnoreCase("hospital")){
				selectQuery = "SELECT * FROM Hospital ";
				colomNumber=2;
			}
			if(flag.equalsIgnoreCase("feedbacktype")){
				selectQuery = "SELECT * FROM FeedbackType ";
				colomNumber=2;
			}
			if(flag.equalsIgnoreCase("bloodgroup")){
				selectQuery = "SELECT * FROM BloodGroup ";
				colomNumber=2;
			}
			if(flag.equalsIgnoreCase("InsuranceCompanies")){
				selectQuery = "SELECT * FROM InsuranceCompanies ";
				colomNumber=2;
			}

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst())
			{
				do {
					labels.add(cursor.getString(colomNumber));
				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return labels;
	}	
	 */

	public int GetSingleValue(String SQL)
	{
		try
		{
			SQLiteDatabase db=this.getWritableDatabase();
			Cursor cur= db.rawQuery(SQL,null);
			int Value;
			if (cur.getCount()>0)
			{
				cur.moveToFirst();
				Value=Integer.parseInt(cur.getString(0).toString());
			}
			else
			{
				Value=0; 
			}
			db.close();
			cur.close();
			return Value;
		}
		catch(Exception ex)
		{
			return 0;
		}
	}
	public String GetSingleText(String SQL)
	{
		try
		{
			SQLiteDatabase db=this.getWritableDatabase();
			Cursor cur= db.rawQuery(SQL,null);
			String Value;
			if (cur.getCount()>0)
			{
				cur.moveToFirst();
				Value=cur.getString(0).toString();
			}
			else
			{
				Value=null; 
			}
			db.close();
			cur.close();
			return Value;
		}
		catch(Exception ex)
		{
			return null;
		}
	}

	/*	public int getProfilesCount() {
		String countQuery = "SELECT  * FROM InsuranceCompanies ";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		db.close();
		return cnt;
	}   */
	/*	public void addNotifications(String Notification,String CurrentDate)
	{
		try{
			SQLiteDatabase db = this.getReadableDatabase();
			db.execSQL("Insert Into Notifications(Notification,DateTime)Values('"+Notification.toString()+"','"+CurrentDate.toString()+"')");
			System.out.println("Notification " + Notification + " Date " + CurrentDate);
			db.close();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Error " + e);
		}
	}   */

	/*	public Cursor GetNotifications()
	{
		try
		{
			SQLiteDatabase db = this.getReadableDatabase();
			String query="SELECT * FROM Notifications order by _id desc";
			Cursor cur= db.rawQuery(query,null);
			db.close();
			return cur; 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}   */

	/*	public void deleteNotificationDaily()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		DatabaseHelper dbhelper;
		if(db==null || !db.isOpen()) 
			db =  this.getReadableDatabase();
		String sql = "DELETE FROM Notifications WHERE DateTime <= date('now','-1 day')"; 
		db.execSQL(sql);
		db.close();
	}   */

	public Cursor getData(String strQuery) 
	{
		SQLiteDatabase db=this.getReadableDatabase();

		if(db==null || !db.isOpen()) 
		{
			db =  this.getReadableDatabase();
		}
		Cursor cursor = db.rawQuery(strQuery, null);
		if(cursor!=null) 
		{
			if(cursor.getCount()>0)
			{
				return cursor;
			}

			else
			{
				cursor.close();
				db.close();
			} 
		}
		cursor.close();
		return cursor;
	}
}
