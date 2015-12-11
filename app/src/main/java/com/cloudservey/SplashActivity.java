package com.cloudservey;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.ksoap2.serialization.SoapObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.asyncmanager.AsyncManager;
import com.classes.Template;
import com.classes.TemplateAnswer;
import com.classes.TemplateQuestion;
import com.classes.TemplateSubQuestion;
import com.databasehelper.DatabaseHelper;
import com.utility.Utility;

public class SplashActivity extends Activity
{
	ProgressBar progressbar;

	boolean NewtworkStatus;
	public static int EulaAcceptedBy=0;
	public static String IMEI;
	public static String versionNumber="103";
	public int Activated=0;
	//private String listType;
	public static int deviceIsActive=0,offlintCnt=0;;
	public String tempIsActive="0";
	public String AppVersion="V1.040815";
	public static int fromsplash;
	public static double Latitude;
	public static double Longitude;
	int LAC=0;
	int CellID=0;
	public static int Userid;
	String RegistrationSuccess="NO";	
	String RegistrationText="";
	String performAction="";
	int status=0,DateDiff=0;
	DatabaseHelper dbHelper;
	AsyncManager manager=new AsyncManager();;
	Utility utility;
	public static String Creation_Date,Expiry_Date,SubscriptionID;
	//	public static String DomainName="http://docontouch.com/";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.activity_splash);

		fromsplash=1;
		progressbar=(ProgressBar)findViewById(R.id.pbLoading);
		manager=new AsyncManager();
		utility=new Utility();
		try
		{
			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			IMEI=telephonyManager.getDeviceId().toString();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		dbHelper=new DatabaseHelper(SplashActivity.this);
		int isActivatedUser=dbHelper.GetOptionsAll();
		dbHelper.CloseDB();

		if(Utility.isNetworkAvailable(SplashActivity.this))
		{
			DatabaseHelper db=new DatabaseHelper(SplashActivity.this);
			String dltStr;

			dltStr="DELETE FROM CS_Template";
			db.DeleteData(dltStr);

			dltStr="DELETE FROM CS_TemplateQuestion";
			db.DeleteData(dltStr);

			dltStr="DELETE FROM CS_SubQuestion";
			db.DeleteData(dltStr);

			dltStr="DELETE FROM CS_TemplateAnswer";
			db.DeleteData(dltStr);

			dltStr="DELETE FROM CS_BackSession";
			db.DeleteData(dltStr);

			db.CloseDB();

			try
			{
				new SoapAsyncGetLatLong().execute();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}       
		}
		else
		{
			if(isActivatedUser==0)
			{
				Toast.makeText(SplashActivity.this, "No internet connection avialble", Toast.LENGTH_SHORT).show();
				finish();
			}
			else
			{
				if(isActivatedUser==1)
				{
					try
					{
						//new SoapAsyncDownloadUserInfo().execute();
						new SoapAsyncGetLatLong().execute();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}       
				}
			}
		}
		/*	if(Utility.isNetworkAvailable(SplashActivity.this))
		{
			DatabaseHelper db=new DatabaseHelper(SplashActivity.this);
			String dltStr;

			dltStr="DELETE FROM CS_Template";
			db.DeleteData(dltStr);

			dltStr="DELETE FROM CS_TemplateQuestion";
			db.DeleteData(dltStr);

			dltStr="DELETE FROM CS_SubQuestion";
			db.DeleteData(dltStr);

			dltStr="DELETE FROM CS_TemplateAnswer";
			db.DeleteData(dltStr);

			dltStr="DELETE FROM CS_BackSession";
			db.DeleteData(dltStr);

			db.CloseDB();
		}

		try
		{
			new SoapAsyncGetLatLong().execute();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}      */ 
		/*	if(Utility.isNetworkAvailable(SplashActivity.this))
		{
			try
			{
				try
				{
					new SoapAsyncGetLatLong().execute();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}       
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(SplashActivity.this, "No internet connection available.", Toast.LENGTH_LONG).show();
			progressbar.setVisibility(View.GONE);

		}*/
	}

	private class SoapAsyncGetLatLong extends AsyncTask<String, Void, String> {

		String response="";
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... urls) {

			try{	
				//TelephonyManager telManager = (TelephonyManager)getSystemService(Service.TELEPHONY_SERVICE);
				//GsmCellLocation cellLocation = (GsmCellLocation)telManager.getCellLocation();  
				//LAC=cellLocation.getLac();
				//CellID=cellLocation.getCid();
				//RqsLocation(CellID,LAC);
			}
			catch(Exception e){

				e.printStackTrace();
				response="";
			}
			return response;
		}    

		@Override
		protected void onPostExecute(String result) {

			Thread background = new Thread() {
				public void run() {

					try {
						DatabaseHelper db=new DatabaseHelper(SplashActivity.this);
						Activated=db.GetOptionsAll();
						db.CloseDB();
						sleep(2*1000);

						if(Activated==1)
						{
							if(Utility.isNetworkAvailable(SplashActivity.this))
							{
								try
								{
									try
									{
										new SoapAsyncDownloadUserInfo().execute();
										//new SoapAsyncDownloadTemplateTable().execute();
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}       
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								Intent i = new Intent(SplashActivity.this,Welcome.class);
								startActivity(i);
								finish();

							}
						}
						else
						{
							if(Utility.isNetworkAvailable(SplashActivity.this))
							{
								try
								{
									try
									{
										Intent i = new Intent(SplashActivity.this,Activation.class);
										startActivity(i);
										finish();
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}       
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								Toast.makeText(SplashActivity.this, "No internet connection available.", Toast.LENGTH_LONG).show();
								progressbar.setVisibility(View.GONE);
								finish();
							}

						}
					} 
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			};
			background.start();
		}
	}

	/*class AsyncTask_ValidateLicense extends AsyncTask<Void, Void, Integer> {
		int resultCode;
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Integer doInBackground(Void... params) {
			try{
				resultCode=	manager.validateRegistration(SplashActivity.this,IMEI);
			}catch(Exception e){
				e.printStackTrace();
			}
			return  resultCode;
		}
		@Override
		protected void onPostExecute(Integer result) {

			if(resultCode==100)
			{
				if(Utility.isNetworkAvailable(SplashActivity.this)){
					SoapAsyncDownloadTables asynctask=new SoapAsyncDownloadTables();
					asynctask.execute();
				}
				else{
					Toast.makeText(SplashActivity.this,"No internet connection.", Toast.LENGTH_LONG).show();
				}
			} 
			else{
				EulaAcceptedBy=1;
				Intent intent = new Intent(SplashActivity.this, Activation.class);
				startActivity(intent);
				finish();
			}
		}
	}*/

	@SuppressWarnings("unused")
	private Boolean RqsLocation(int cid, int lac){

		Boolean result = false;
		String urlmmap = "http://www.google.com/glm/mmap";

		try {
			URL url = new URL(urlmmap);
			URLConnection conn = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;      
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.connect();

			OutputStream outputStream = httpConn.getOutputStream();
			//WriteData(outputStream, cid, lac);

			InputStream inputStream = httpConn.getInputStream();
			DataInputStream dataInputStream = new DataInputStream(inputStream);

			dataInputStream.readShort();
			dataInputStream.readByte();
			int code = dataInputStream.readInt();
			if (code == 0) {
				Latitude = dataInputStream.readInt();
				Longitude = dataInputStream.readInt();
				Latitude = (Latitude/1000000);
				Longitude = (Longitude/1000000);
				result = true;
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return result;
	}

	public class SoapAsyncDownloadUserInfo extends AsyncTask<String, Void, SoapObject>
	{

		SoapObject response=null;
		int requestcode;
		ProgressDialog progressDialog;
		@Override
		protected SoapObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			try
			{
				dbHelper=new DatabaseHelper(SplashActivity.this);
				String str="SELECT UserId from CS_Users";
				Userid=dbHelper.GetSingleValue(str);
				dbHelper.CloseDB();

				try
				{
					DatabaseHelper db;
					db=new DatabaseHelper(SplashActivity.this);
					String strQuery="Select OfflineFlag from CS_OfflineCount";
					Cursor cur=db.getData(strQuery);
					offlintCnt=cur.getCount();
					db.CloseDB();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}       

				String strSql="Select CreationDate,Expiry_Date,SubscriptionID from cs_users c Where c.UserID='"+Userid+"' and c.IsActive=1;";
				response= manager.getSoapData(SplashActivity.this, "ajit", "ajit99",strSql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				response=null;
			}
			return response;
		}

		@Override
		protected void onPostExecute(SoapObject result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try
			{
				if(result !=null)
				{
					int RowCount = result.getPropertyCount();

					if(RowCount>0)
					{
						SoapObject Soap1 = (SoapObject) result.getProperty(0);
						SoapObject Soap2 = (SoapObject) Soap1.getProperty(0);

						RowCount = Soap2.getPropertyCount();
						if(RowCount>0)
						{
							for (int i=0;i<RowCount;i++)
							{

								//String CreationDate,Expiry_Date;
								SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);

								Creation_Date=Soap4.getProperty("CreationDate").toString();
								Expiry_Date=Soap4.getProperty("Expiry_Date").toString();
								SubscriptionID=Soap4.getProperty("SubscriptionID").toString();

								if(Creation_Date.equals("anyType{}"))
								{
									Creation_Date="";
								}
								if(Expiry_Date.equals("anyType{}"))
								{
									Expiry_Date="";
								}
								if(SubscriptionID.equals("anyType{}"))
								{
									SubscriptionID="";
								}


							}
						}
						else
						{
							Toast.makeText(SplashActivity.this, "Poor internet connection...try again later", Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
			try
			{

				new SoapAsyncDownloadTemplateTable().execute();


			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public class SoapAsyncDownloadTemplateTable extends AsyncTask<String, Void, SoapObject > 
	{
		SoapObject response=null;
		int requestcode;
		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() 
		{

		}
		@Override
		protected SoapObject  doInBackground(String... params)
		{
			try
			{	
				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String Date = df.format(c.getTime());
				java.util.Date d1 = df.parse( Date );
				java.util.Date d2 = df.parse( Expiry_Date );
				if ( compareTo( d1, d2 ) < 0 )
				{
					DateDiff=1;
				}
				else if ( compareTo( d1, d2 ) > 0 ) 
				{
					DateDiff=0;
					status=1;
				}
				else
				{
					DateDiff=1;
				}

				dbHelper=new DatabaseHelper(SplashActivity.this);
				String str="SELECT UserId from CS_Users";
				Userid=dbHelper.GetSingleValue(str);
				dbHelper.CloseDB();

				if(DateDiff==1)
				{
					String strSql="SELECT c.SurveyTemplateID,c.CustID,c.TemplateName,c.NoOfScreens,c.HeadingText,c.ThankYouText,c.SendSMS,c.SMSText,c.SendEmail,c.EmailText,c.MainLogo,cd.isActive FROM cs_template c Inner Join cs_device cd On cd.UserID = c.CustID and c.surveyTemplateID=cd.SurveyTemplateID Where c.CustID='"+Userid+"' and cd.DeviceIMEINo='"+IMEI+"' and c.IsActive=1";
					response= manager.getSoapData(SplashActivity.this, "ajit", "ajit99",strSql);
				}

			}
			catch(Exception e)
			{
				e.printStackTrace();
				response=null;
			}
			return response;
		}

		@Override
		protected void onPostExecute(SoapObject result)
		{
			try
			{

				if(result !=null)
				{
					int RowCount = result.getPropertyCount();

					if(RowCount>0)
					{
						SoapObject Soap1 = (SoapObject) result.getProperty(0);
						SoapObject Soap2 = (SoapObject) Soap1.getProperty(0);

						RowCount = Soap2.getPropertyCount();
						if(RowCount>0)
						{
							for (int i=0;i<RowCount;i++)
							{
								int SurveyTemplateID,CustID,NoOfScreens;
								String TemplateName,MainLogo,HeadingText,ThankyouText,SendSMS,SMSText,SendEmail,EmailText,isActive;
								SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);

								SurveyTemplateID= Integer.parseInt(Soap4.getProperty("SurveyTemplateID").toString());	
								CustID= Integer.parseInt(Soap4.getProperty("CustID").toString());
								NoOfScreens= Integer.parseInt(Soap4.getProperty("NoOfScreens").toString());

								TemplateName=Soap4.getProperty("TemplateName").toString();
								MainLogo=Soap4.getProperty("MainLogo").toString();
								HeadingText=Soap4.getProperty("HeadingText").toString();
								ThankyouText=Soap4.getProperty("ThankYouText").toString();
								SendSMS=Soap4.getProperty("SendSMS").toString();
								SMSText=Soap4.getProperty("SMSText").toString();
								SendEmail=Soap4.getProperty("SendEmail").toString();
								EmailText=Soap4.getProperty("EmailText").toString();
								isActive=Soap4.getProperty("isActive").toString();
								tempIsActive=isActive;

								if(TemplateName.equals("anyType{}"))
								{
									TemplateName="";
								}
								if(MainLogo.equals("anyType{}"))
								{
									MainLogo="";
								}
								if(HeadingText.equals("anyType{}"))
								{
									HeadingText="";
								}
								if(ThankyouText.equals("anyType{}"))
								{
									ThankyouText="";
								}
								if(SendSMS.equals("anyType{}"))
								{
									SendSMS="";
								}
								if(SMSText.equals("anyType{}"))
								{
									SMSText="";
								}
								if(SendEmail.equals("anyType{}"))
								{
									SendEmail="";
								}
								if(EmailText.equals("anyType{}"))
								{
									EmailText="";
								}
								if(tempIsActive.equals("anyType{}"))
								{
									tempIsActive="";
								}
								
								DatabaseHelper db1=new DatabaseHelper(SplashActivity.this);
								Template temp =new Template(SurveyTemplateID,CustID,NoOfScreens,TemplateName,MainLogo,HeadingText,ThankyouText,SendSMS,SMSText,SendEmail,EmailText,"","","","");
								db1.AddTemplate(temp);
								db1.CloseDB();

							}
						}

					}

				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
			if(DateDiff==0 && status==1)
			{

				String msg="";
				if(SubscriptionID.equalsIgnoreCase("1"))
				{
					deviceIsActive=0;
					try
					{
						msg="Your free trial period has been expired";
						AlertBoxDialog cdd=new AlertBoxDialog(SplashActivity.this,msg,offlintCnt);
						cdd.show();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					deviceIsActive=0;
					msg="Please refill / reactivate your subscription";
					AlertBoxDialog cdd=new AlertBoxDialog(SplashActivity.this,msg,offlintCnt);
					cdd.show(); 
				}
			}
			else
				if(status==0)
				{
					if(tempIsActive.equalsIgnoreCase("1"))
					{
						try
						{
							new SoapAsyncDownloadTemplateQuestionTable().execute();

						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					else
					{
						deviceIsActive=1;
						if(offlintCnt==0)
						{
							String msg=" Your subscription is deactivated by admin..please contact with administrator";
							Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_LONG).show();
							finish();
						}
						else
							if(offlintCnt>0)
							{
								String msg="Activate your device for upload pending records";
								AlertBoxDialog cdd=new AlertBoxDialog(SplashActivity.this,msg,MainActivity.offlineCount);
								cdd.show();
							}
					}
				}

		}
	}   
	public class SoapAsyncDownloadTemplateQuestionTable extends AsyncTask<String, Void, SoapObject > 
	{
		SoapObject response=null;
		int requestcode;
		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() 
		{

		}
		@Override
		protected SoapObject  doInBackground(String... params)
		{
			try
			{	
				dbHelper=new DatabaseHelper(SplashActivity.this);

				String str="SELECT SurveyTemplateID from CS_Template WHERE CustID='"+Userid+"'";
				int Templateid=dbHelper.GetSingleValue(str);
				dbHelper.CloseDB();
				String strSql="SELECT SurveyTemplateID,QuestionID,QuestionTypeID,QuestionText,ShowImage,QuestionScreenID FROM cs_templatequestion c Where SurveyTemplateID='"+Templateid+"' and status=1 Order By SortId";
				response= manager.getSoapData(SplashActivity.this, "ajit", "ajit99",strSql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				response=null;
			}
			return response;
		}

		@Override
		protected void onPostExecute(SoapObject result)
		{
			try
			{
				if(result !=null)
				{
					int RowCount = result.getPropertyCount();

					if(RowCount>0)
					{
						SoapObject Soap1 = (SoapObject) result.getProperty(0);
						SoapObject Soap2 = (SoapObject) Soap1.getProperty(0);

						RowCount = Soap2.getPropertyCount();

						for (int i=0;i<RowCount;i++)
						{
							int SurveyTemplateID,QuestionID, QuestionTypeID,QuestionScreenID;
							String ShowImage,QuestionText1;
							SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);

							SurveyTemplateID= Integer.parseInt(Soap4.getProperty("SurveyTemplateID").toString());	
							QuestionID= Integer.parseInt(Soap4.getProperty("QuestionID").toString());	
							QuestionTypeID= Integer.parseInt(Soap4.getProperty("QuestionTypeID").toString());	
							QuestionText1= Soap4.getProperty("QuestionText").toString();	
							QuestionScreenID= Integer.parseInt(Soap4.getProperty("QuestionScreenID").toString());	
							ShowImage=Soap4.getProperty("ShowImage").toString();
							if(QuestionText1.equals("anyType{}"))
							{
								QuestionText1="";
							}
							if(ShowImage.equals("anyType{}"))
							{
								ShowImage="";
							}
							DatabaseHelper db1=new DatabaseHelper(SplashActivity.this);
							TemplateQuestion Qtemp =new TemplateQuestion(SurveyTemplateID,QuestionID,QuestionTypeID,QuestionText1,QuestionScreenID,ShowImage,"");
							db1.AddTemplateQuestion(Qtemp);
							db1.CloseDB();

						}
					}
					else
					{
						if(tempIsActive.equalsIgnoreCase("1"))
						{
							Toast.makeText(SplashActivity.this, "Poor internet connection...try again later", Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
			try
			{
				new SoapAsyncDownloadTemplateSubQuetionTable().execute();

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}     
	public class SoapAsyncDownloadTemplateSubQuetionTable extends AsyncTask<String, Void, SoapObject>
	{
		SoapObject response=null;
		int requestcode;
		protected void onPreExecute() 
		{

		}

		@Override
		protected SoapObject  doInBackground(String... params)
		{
			try
			{	
				dbHelper=new DatabaseHelper(SplashActivity.this);
				String str="SELECT SurveyTemplateID from CS_Template WHERE CustID='"+Userid+"'";
				int Templateid=dbHelper.GetSingleValue(str);
				dbHelper.CloseDB();

				String strSql="SELECT subquestion_id,SurveyTemplateID,QuestionID,AnswerText,Status FROM cs_subquestion c Where SurveyTemplateID='"+Templateid+"' and Status=1";
				response= manager.getSoapData(SplashActivity.this, "ajit", "ajit99",strSql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				response=null;
			}
			return response;
		}

		@Override
		protected void onPostExecute(SoapObject result)
		{
			try
			{
				if(result !=null)
				{
					int RowCount = result.getPropertyCount();

					if(RowCount>0)
					{
						SoapObject Soap1 = (SoapObject) result.getProperty(0);
						SoapObject Soap2 = (SoapObject) Soap1.getProperty(0);

						RowCount = Soap2.getPropertyCount();

						for (int i=0;i<RowCount;i++)
						{
							int subquestion_Id,SurveyTemplateID,QuestionID;
							String QuestionText1,Status;
							SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);

							subquestion_Id= Integer.parseInt(Soap4.getProperty("subquestion_id").toString());
							SurveyTemplateID= Integer.parseInt(Soap4.getProperty("SurveyTemplateID").toString());	
							QuestionID= Integer.parseInt(Soap4.getProperty("QuestionID").toString());	

							QuestionText1= Soap4.getProperty("AnswerText").toString();	
							Status= Soap4.getProperty("Status").toString();	
							if(QuestionText1.equals("anyType{}"))
							{
								QuestionText1="";
							}
							if(Status.equals("anyType{}"))
							{
								Status="";
							}
							DatabaseHelper db1=new DatabaseHelper(SplashActivity.this);
							TemplateSubQuestion Qtemp =new TemplateSubQuestion(SurveyTemplateID,subquestion_Id,QuestionID,QuestionText1,Status);
							db1.AddTemplateSubQuestion(Qtemp);
							db1.CloseDB();

						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
			try
			{
				new SoapAsyncDownloadTemplateAnsTable().execute();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public class SoapAsyncDownloadTemplateAnsTable extends AsyncTask<String, Void, SoapObject > 
	{
		SoapObject response=null;
		int requestcode;
		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute() 
		{

		}
		@Override
		protected SoapObject  doInBackground(String... params)
		{
			try
			{
				dbHelper=new DatabaseHelper(SplashActivity.this);

				String str="SELECT SurveyTemplateID from CS_Template WHERE CustID='"+Userid+"'";
				int Templateid=dbHelper.GetSingleValue(str);
				dbHelper.CloseDB();
				String strSql="SELECT AnswerSrNo,SurveyTemplateID,QuestionID,AnswerText,AnswerImage,NextQuestionScreenID FROM cs_templateanswer c Where SurveyTemplateID='"+Templateid+"' and Status=1";
				response= manager.getSoapData(SplashActivity.this, "ajit", "ajit99",strSql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				response=null;
			}
			return response;
		}

		@Override
		protected void onPostExecute(SoapObject result)
		{
			try
			{
				if(result !=null)
				{
					int RowCount = result.getPropertyCount();

					if(RowCount>0)
					{
						SoapObject Soap1 = (SoapObject) result.getProperty(0);
						SoapObject Soap2 = (SoapObject) Soap1.getProperty(0);

						RowCount = Soap2.getPropertyCount();

						for (int i=0;i<RowCount;i++)
						{
							int SurveyTemplateID1,QuestionID, AnswerSrNo,NextQuestionScreenID;
							String AnswerText,AnswerImage,AnswerBitmap="";
							//				String enCodeStr="";

							SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);

							SurveyTemplateID1= Integer.parseInt(Soap4.getProperty("SurveyTemplateID").toString());	
							QuestionID= Integer.parseInt(Soap4.getProperty("QuestionID").toString());	
							AnswerSrNo= Integer.parseInt(Soap4.getProperty("AnswerSrNo").toString());	
							NextQuestionScreenID= Integer.parseInt(Soap4.getProperty("NextQuestionScreenID").toString());	

							AnswerText=Soap4.getProperty("AnswerText").toString();
							AnswerImage=Soap4.getProperty("AnswerImage").toString();

							if(AnswerText.equals("anyType{}"))
							{
								AnswerText="";
							}
							if(AnswerImage.equals("anyType{}"))
							{
								AnswerImage="";
							}

							DatabaseHelper db1=new DatabaseHelper(SplashActivity.this);
							TemplateAnswer Atemp =new TemplateAnswer(SurveyTemplateID1,QuestionID,AnswerSrNo,NextQuestionScreenID,AnswerText,AnswerImage,"");
							db1.AddTemplateAnswer(Atemp);
							db1.CloseDB();	
						}
					}
				}
				try
				{
					Intent intent = new Intent(SplashActivity.this,Welcome.class);
					startActivity(intent);
					finish();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
		}
	}           
	public static long compareTo( java.util.Date date1, java.util.Date date2 )
	{
		//returns negative value if date1 is before date2
		//returns 0 if dates are even
		//returns positive value if date1 is after date2
		return date1.getTime() - date2.getTime();
	}
	/*	private void WriteData(OutputStream out, int cid, int lac)
			throws IOException
			{    
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		dataOutputStream.writeShort(21);
		dataOutputStream.writeLong(0);
		dataOutputStream.writeUTF("en");
		dataOutputStream.writeUTF("Android");
		dataOutputStream.writeUTF("1.0");
		dataOutputStream.writeUTF("Web");
		dataOutputStream.writeByte(27);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(3);
		dataOutputStream.writeUTF("");

		dataOutputStream.writeInt(cid);
		dataOutputStream.writeInt(lac);   

		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.writeInt(0);
		dataOutputStream.flush();    

			}*/

	/*class AsyncTask_ValidateVersion extends AsyncTask<Void, Void, SoapObject> {
		SoapObject result;
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected SoapObject doInBackground(Void... params) {
			try{
				result=	manager.validateVersion(SplashActivity.this,versionNumber);
			}catch(Exception e){
				e.printStackTrace();
			}
			return  result;
		}
		@Override
		protected void onPostExecute(SoapObject result) {

			try{

				if(result != null)
				{
					int RowCount = result.getPropertyCount();
					if(RowCount>0)
					{
						SoapObject Soap1 = (SoapObject) result.getProperty(0);
						SoapObject Soap2 = (SoapObject) Soap1.getProperty(1);
						//SoapObject Soap3 = (SoapObject) Soap2.getProperty(0);

						RowCount = Soap2.getPropertyCount();


						for (int i=0;i<RowCount;i++)
						{

							String Disabled,ResultMsg;

							SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);
							Disabled=Soap4.getProperty("Disabled").toString();
							ResultMsg=Soap4.getProperty("MessageText").toString();

							if(Disabled.equalsIgnoreCase("false")){

								Intent intent = new Intent(SplashActivity.this, MainActivity.class);
								intent.putExtra("listtype", listType);
								SplashActivity.this.startActivity(intent);
								SplashActivity.this.finish();
							}
							else{
								Toast.makeText(SplashActivity.this, " "+ResultMsg, Toast.LENGTH_LONG).show();

								finish();
							}
						}
					}
				}
				else{
					Toast.makeText(SplashActivity.this, "Webserver is temporarry down.", Toast.LENGTH_LONG).show();
				}	
			}
			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(SplashActivity.this, ""+e, Toast.LENGTH_SHORT).show();

			}
		}
	}*/
}
