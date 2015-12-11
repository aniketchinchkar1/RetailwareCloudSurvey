package com.cloudservey;


import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.ksoap2.serialization.SoapObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.asyncmanager.AsyncManager;
import com.classes.QuestionType;
import com.classes.Template;
import com.classes.TemplateAnswer;
import com.classes.TemplateQuestion;
import com.classes.TemplateSubQuestion;
import com.classes.Users;
import com.databasehelper.DatabaseHelper;

public class Activation extends Activity implements OnClickListener
{
	private EditText txtName;
	private EditText txtEmailID;
	private EditText txtPassword;
	Button mActivateBtn;
	LinearLayout linearLayout;
	int SubscriptionID,DateDiff=0;
	int ActivationAttempt=0;
	boolean dataDownloaded=false;
	boolean NewtworkStatus;
	public String isActivated;
	public static int UserId;
	public static String IMEINo,CreationDate,Expiry_Date;
	String SimSrNo="";
	String LongCodeMobileNo;
	String LongCodeKeyword;
	String Keyword;
	String EulaAccepted="0";
	String RegistrationSuccess="NO";	
	String RegistrationText="";
	String performAction="";
	String listType="";
	String res = null;
	SimpleDateFormat df;
	public static String[] TemplateText;
	public static String[] TemplateLogo;
	public static String[] TemplateId;
	public static int tmplatSurveyTemplateID;
	public static Boolean fromActivation=false;
	static boolean fromact=false;
	ProgressDialog progressDialog;

	public static String strname,strpass,strUserName;

	DatabaseHelper dbHelper;
	AsyncManager manager;

	//	private MenuItem share;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activation);

		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));

		int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		if (actionBarTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
				title.setTypeface(Typeface.DEFAULT_BOLD);
			}
		}
		txtName=(EditText)findViewById(R.id.txtName);
		txtEmailID=(EditText)findViewById(R.id.txtEmailID);
		txtPassword=(EditText)findViewById(R.id.txtPassword);
		mActivateBtn=(Button)findViewById(R.id.btnActivate);
		linearLayout=(LinearLayout)findViewById(R.id.activation_main);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		txtEmailID.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		DatabaseHelper db=new DatabaseHelper(Activation.this);
		String dltStr;
		fromActivation=true;
		dltStr="DELETE FROM CS_Users";
		db.DeleteData(dltStr);

		dltStr="DELETE FROM CS_QuestionType";
		db.DeleteData(dltStr);

		db.CloseDB();

		mActivateBtn.setOnClickListener(this);

		TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Service.TELEPHONY_SERVICE);
		telephonyManager.getDeviceId();

		IMEINo=telephonyManager.getDeviceId().toString();
		SimSrNo= telephonyManager.getSimSerialNumber();
		new SimpleEula(this).show();


		manager=new AsyncManager();

		dbHelper= new DatabaseHelper(Activation.this);
		EulaAccepted=String.valueOf(dbHelper.GetSingleValue("Select EulaAccepted From Options"));
		dbHelper.CloseDB();


		if(EulaAccepted.equals("0"))
		{
			return;
		}    
		//animate();
	}
	public void btnActivate_Click()
	{
		getData();
		
		if(strUserName.length()==0)
		{
			Toast.makeText(Activation.this, "Please enter name !", Toast.LENGTH_SHORT).show();
			txtName.requestFocus();
			return;
		}
		else if(strname.length()==0)
		{
			Toast.makeText(Activation.this, "Please enter emailid !", Toast.LENGTH_SHORT).show();
			txtEmailID.requestFocus();
			return;  		
		}
		else
			if(strpass.length()==0)
			{
				Toast.makeText(Activation.this, "Please enter password !", Toast.LENGTH_SHORT).show();
				txtPassword.requestFocus();
				return;
			}
			else
				if(strname.length()!=0)
				{
					final String email = txtEmailID.getText().toString().trim();

					final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

					if (email.matches(emailPattern) && email.length() > 0)
					{ 
						NewtworkStatus=isConnected(Activation.this);
						if(NewtworkStatus==false)
						{
							Toast.makeText(Activation.this, "Network connection is not available !", Toast.LENGTH_LONG).show();
						}
						else
						{

							SoapActivationSuccess asyncvalidatesuces=new SoapActivationSuccess();
							asyncvalidatesuces.execute();
						}

					}
					else
					{
						Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
						txtEmailID.setText("");
						txtPassword.setText("");
					}

				}

	}

	public static boolean isConnected(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	/*	public void LongCodeInfo()
	{
		//        dbHelper= new DatabaseHelper(Activation.this);

		final String SOAP_ACTION = "http://www.retailware.in/GetLongCodeInfo";
		final String METHOD_NAME = "GetLongCodeInfo";

		//		SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME_LongCodeInfo);

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 

		request.addProperty("IMEINumber",IMEINo);
		request.addProperty("VersionNo",SplashActivity.versionNumber);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransport = new HttpTransportSE(RsplUrl);
		Object response=null;

		try
		{
			httpTransport.call(SOAP_ACTION, envelope);
			response = envelope.getResponse();  
			System.out.println("response "+response);
			SoapObject result = (SoapObject)envelope.bodyIn;

			if(result != null)
			{
				int RowCount = result.getPropertyCount();
				if(RowCount>0)
				{
					SoapObject Soap1 = (SoapObject) result.getProperty(0);
					SoapObject Soap2 = (SoapObject) Soap1.getProperty(1);
					SoapObject Soap3 = (SoapObject) Soap2.getProperty(0);

					RowCount = Soap3.getPropertyCount();

					for (int i=0;i<RowCount;i++)
					{
						SoapObject Soap4 = (SoapObject) Soap3.getProperty(i);

						LongCodeMobileNo=Soap4.getProperty("LongCodeMobileNo").toString();
						LongCodeKeyword=Soap4.getProperty("LongCodeKeyword").toString();
						Keyword=Soap4.getProperty("Keyword").toString();

						if(LongCodeMobileNo.equals("anyType{}"))
						{
							LongCodeMobileNo="";
						}
						if(LongCodeKeyword.equals("anyType{}"))
						{
							LongCodeKeyword="";
						}
						if(Keyword.equals("anyType{}"))
						{
							Keyword="";
						}
					}
				}
			}
			//            dbHelper.CloseDB();
			dataDownloaded=true;
		}

		catch (Exception exception)
		{
			//            dbHelper.CloseDB();
			dataDownloaded=false;
		}
	}
	 */

	private class SoapActivationSuccess extends AsyncTask<String, Void, SoapObject > {
		SoapObject response=null;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(Activation.this, "", "");
			progressDialog.setContentView(R.layout.progress_main);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		@Override
		protected SoapObject  doInBackground(String... urls) 
		{
			String encryptedPass=md5(txtPassword.getText().toString().trim());
			String strSql="SELECT  userID FROM `cs_users` WHERE Emailid ='"+txtEmailID.getText().toString().trim()+"' AND PASSWORD ='"+encryptedPass+"'";
			response= manager.getSoapData(Activation.this, "ajit", "ajit99",strSql);
			return response;
		}

		@Override
		protected void onPostExecute(SoapObject result) {

			try {

				if(result !=null)
				{
					int RowCount = result.getPropertyCount();

					if(RowCount>0)
					{
						SoapObject Soap1 = (SoapObject) result.getProperty(0);
						SoapObject Soap2 = (SoapObject) Soap1.getProperty(0);

						RowCount = Soap2.getPropertyCount();

						if(RowCount<=0)
						{
							txtEmailID.setText("");
							txtPassword.setText("");
							Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
							progressDialog.dismiss();
						}
						else
						{
							for (int i=0;i<RowCount;i++)
							{
								try
								{
									SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);
									UserId= Integer.parseInt(Soap4.getProperty("userID").toString());	
									try
									{

										new SoapAsyncDownloadUserTable().execute();
									}
									catch(Exception e)
									{
										Toast.makeText(getApplicationContext(), "Record not fetched from server", Toast.LENGTH_SHORT).show();
									}
								}
								catch(Exception e)
								{
									Toast.makeText(getApplicationContext(), "Activation Not Successfull", Toast.LENGTH_SHORT).show();
								}
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
		}
	}

	private class SoapAsyncDownloadUserTable extends AsyncTask<String, Void, SoapObject > {
		SoapObject response=null;
		String Date;
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
				df = new SimpleDateFormat("yyyy-MM-dd");
				Date= df.format(c.getTime());

				String strSql="SELECT UserID,SubscriptionID,Fullname,Companyname,Contactperson,Mobileno,Address1,Address2,City,Price,Retailwarecustomer,Emailid,Password,IsAdmin,IsActive,CreationDate,CreationTime,LastmodifiedDate,Lastmodifiedtime,Expiry_Date  FROM cs_users c Where UserID='"+UserId+"'";
				response= manager.getSoapData(Activation.this, "ajit", "ajit99",strSql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				response=null;
			}
			return response;
		}

		@Override
		protected void onPostExecute(SoapObject result) {

			try {

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
							//int UserID1;
							String Fullname;
							String Companyname;
							String Contactperson;
							String Mobileno;
							String Address1;
							String Address2;
							String City;
							String Price;
							String Retailwarecustomer;
							String Emailid;
							String Password; 
							String IsAdmin;
							String IsActive;
							String CreationTime;
							String LastmodifiedDate;
							String Lastmodifiedtime;

							SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);
							UserId= Integer.parseInt(Soap4.getProperty("UserID").toString());	
							SubscriptionID= Integer.parseInt(Soap4.getProperty("SubscriptionID").toString());

							Fullname=Soap4.getProperty("Fullname").toString();
							Companyname=Soap4.getProperty("Companyname").toString();
							Contactperson=Soap4.getProperty("Contactperson").toString();
							Mobileno=Soap4.getProperty("Mobileno").toString();
							Address1=Soap4.getProperty("Address1").toString();
							Address2=Soap4.getProperty("Address2").toString();
							City=Soap4.getProperty("City").toString();
							Price=Soap4.getProperty("Price").toString();
							Retailwarecustomer=Soap4.getProperty("Retailwarecustomer").toString();
							Emailid=Soap4.getProperty("Emailid").toString();
							Password=Soap4.getProperty("Password").toString();
							IsAdmin=Soap4.getProperty("IsAdmin").toString();
							IsActive=Soap4.getProperty("IsActive").toString();
							CreationDate=Soap4.getProperty("CreationDate").toString();
							CreationTime=Soap4.getProperty("CreationTime").toString();
							LastmodifiedDate=Soap4.getProperty("LastmodifiedDate").toString();
							Lastmodifiedtime=Soap4.getProperty("Lastmodifiedtime").toString();
							Expiry_Date=Soap4.getProperty("Expiry_Date").toString();

							if(Fullname.equals("anyType{}"))
							{
								Fullname="";
							}

							if(Companyname.equals("anyType{}"))
							{
								Companyname="";
							}

							if(Contactperson.equals("anyType{}"))
							{
								Contactperson="";
							}

							if(Mobileno.equals("anyType{}"))
							{
								Mobileno="";
							}

							if(Address1.equals("anyType{}"))
							{
								Address1="";
							}

							if(Address2.equals("anyType{}"))
							{
								Address2="";
							}

							if(City.equals("anyType{}"))
							{
								City="";
							}

							if(Price.equals("anyType{}"))
							{
								Price="";
							}

							if(Retailwarecustomer.equals("anyType{}"))
							{
								Retailwarecustomer="";
							}

							if(Emailid.equals("anyType{}"))
							{
								Emailid="";
							}

							if(Password.equals("anyType{}"))
							{
								Password="";
							}

							if(IsAdmin.equals("anyType{}"))
							{
								IsAdmin="";
							}

							if(IsActive.equals("anyType{}"))
							{
								IsActive="";
							}

							if(CreationDate.equals("anyType{}"))
							{
								CreationDate="";
							}

							if(CreationTime.equals("anyType{}"))
							{
								CreationTime="";
							}

							if(LastmodifiedDate.equals("anyType{}"))
							{
								LastmodifiedDate="";
							}

							if(Lastmodifiedtime.equals("anyType{}"))
							{
								Lastmodifiedtime="";
							}

							if(Expiry_Date.equals("anyType{}"))
							{
								Expiry_Date="";
							}

							java.util.Date d1 = df.parse( Date );
							java.util.Date d2 = df.parse( Expiry_Date );
							if ( compareTo( d1, d2 ) < 0 )
							{
								DateDiff=1;
							}
							else if ( compareTo( d1, d2 ) > 0 ) 
							{
								DateDiff=0;
							}
							else
							{
								DateDiff=1;
							}

							DatabaseHelper db1=new DatabaseHelper(Activation.this);
							Users u =new Users(UserId,SubscriptionID,Fullname, Companyname, Contactperson, Mobileno, Address1, Address2, City, Price, Retailwarecustomer, Emailid, Password, IsAdmin, IsActive, CreationDate, CreationTime, LastmodifiedDate, Lastmodifiedtime, Expiry_Date);
							db1.AddUsers(u);
							db1.CloseDB();
							if(DateDiff==1)
							{
								new SoapAsyncAlreadyRegister().execute();
							}
							else
							{
								String msg="";
								if(SubscriptionID==1)
								{
									
									try
									{
										msg="Your free trial period has been expired";
										Toast.makeText(Activation.this, msg, Toast.LENGTH_LONG).show();
										finish();
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}
								else
								{
									
									msg="Please refill / reactivate your subscription";
									Toast.makeText(Activation.this, msg, Toast.LENGTH_LONG).show();
									finish();
								}
							}

						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
		}
	}
	public class SoapAsyncDownloadQuestionTypeTable extends AsyncTask<String, Void, SoapObject > 
	{
		SoapObject response=null;
		int requestcode;
		@Override
		protected void onPreExecute() 
		{

		}

		@Override
		protected SoapObject  doInBackground(String... params)
		{
			try
			{	

				String strSql="SELECT QuestionTypeID, QuestionTypeName FROM cs_questiontype c Where IsActive='1'";
				response= manager.getSoapData(Activation.this, "ajit", "ajit99",strSql);
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
							int QuestionTypeID;                   
							String QuestionTypeName;

							SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);
							QuestionTypeID= Integer.parseInt(Soap4.getProperty("QuestionTypeID").toString());	
							QuestionTypeName=Soap4.getProperty("QuestionTypeName").toString();


							if(QuestionTypeName.equals("anyType{}"))
							{
								QuestionTypeName="";
							}


							DatabaseHelper db1=new DatabaseHelper(Activation.this);
							QuestionType qType =new QuestionType(QuestionTypeID,QuestionTypeName,"");
							db1.AddQuestionType(qType);
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

		@Override
		protected void onPreExecute() 
		{


		}

		@Override
		protected SoapObject  doInBackground(String... params)
		{
			try
			{	
				String strSql="SELECT c.SurveyTemplateID,c.CustID,c.TemplateName,c.NoOfScreens,c.HeadingText,c.ThankYouText,c.SendSMS,c.SMSText,c.SendEmail,c.EmailText,c.MainLogo FROM cs_template c Inner Join cs_device cd On cd.UserID = c.CustID and c.surveyTemplateID=cd.SurveyTemplateID Where c.CustID='"+UserId+"' and cd.DeviceIMEINo='"+IMEINo+"' and c.IsActive=1;";
				response= manager.getSoapData(Activation.this, "ajit", "ajit99",strSql);
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
								int CustID,NoOfScreens,SurveyTemplateID;
								String TemplateName,MainLogo,HeadingText,ThankyouText,SendSMS,SMSText,SendEmail,EmailText;
								SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);

								SurveyTemplateID= Integer.parseInt(Soap4.getProperty("SurveyTemplateID").toString());	
								tmplatSurveyTemplateID=SurveyTemplateID;
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
								DatabaseHelper db1=new DatabaseHelper(Activation.this);
								Template temp =new Template(SurveyTemplateID,CustID,NoOfScreens,TemplateName,MainLogo,HeadingText,ThankyouText,SendSMS,SMSText,SendEmail,EmailText,"","","","");
								db1.AddTemplate(temp);
								db1.CloseDB();

							}

							try
							{

								new SoapAsyncDownloadTemplateQuestionTable().execute();

							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
			catch(Exception e)
			{

			}

		}
	}   
	public class SoapAsyncDownloadTemplateQuestionTable extends AsyncTask<String, Void, SoapObject > 
	{
		SoapObject response=null;
		int requestcode;

		@Override
		protected void onPreExecute() 
		{

		}

		@Override
		protected SoapObject  doInBackground(String... params)
		{
			try
			{	
				String strSql="SELECT SurveyTemplateID,QuestionID,QuestionTypeID,QuestionText,ShowImage,QuestionScreenID FROM cs_templatequestion c Where SurveyTemplateID='"+tmplatSurveyTemplateID+"' and status=1 Order By SortId";
				response= manager.getSoapData(Activation.this, "ajit", "ajit99",strSql);
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
							DatabaseHelper db1=new DatabaseHelper(Activation.this);
							TemplateQuestion Qtemp =new TemplateQuestion(SurveyTemplateID,QuestionID,QuestionTypeID,QuestionText1,QuestionScreenID,ShowImage,"");
							db1.AddTemplateQuestion(Qtemp);
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
				String strSql="SELECT subquestion_id,SurveyTemplateID,QuestionID,AnswerText,Status FROM cs_subquestion c Where SurveyTemplateID='"+tmplatSurveyTemplateID+"' and Status=1";
				response= manager.getSoapData(Activation.this, "ajit", "ajit99",strSql);
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
							DatabaseHelper db1=new DatabaseHelper(Activation.this);
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

		@Override
		protected void onPreExecute() 
		{

		}

		@Override
		protected SoapObject  doInBackground(String... params)
		{
			try
			{	
				String strSql="SELECT AnswerSrNo,SurveyTemplateID,QuestionID,AnswerText,AnswerImage,NextQuestionScreenID FROM cs_templateanswer c Where SurveyTemplateID='"+tmplatSurveyTemplateID+"' and Status=1";
				response= manager.getSoapData(Activation.this, "ajit", "ajit99",strSql);
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
							String AnswerText, AnswerImage;

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

							DatabaseHelper db1=new DatabaseHelper(Activation.this);
							TemplateAnswer Atemp =new TemplateAnswer(SurveyTemplateID1,QuestionID,AnswerSrNo,NextQuestionScreenID,AnswerText,AnswerImage,"");
							db1.AddTemplateAnswer(Atemp);
							db1.CloseDB();	
						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
			progressDialog.dismiss();
			Toast.makeText(Activation.this, "Activation successfull", Toast.LENGTH_LONG).show();
			DatabaseHelper dbHelper=new DatabaseHelper(Activation.this);
			dbHelper.UpdateActivated("1");
			dbHelper.CloseDB();
			Intent intent = new Intent(Activation.this, Welcome.class);
			Activation.this.startActivity(intent);
			Activation.this.finish();
		}
	}           
	public class SoapAsyncAlreadyRegister extends AsyncTask<String, Void, SoapObject>
	{

		SoapObject response=null;
		int requestcode;

		@Override
		protected SoapObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			try
			{

				String strSql="select SurveyTemplateID from cs_device where userID='"+UserId+"' and DeviceIMEINo='"+IMEINo+"' and isActive=1;";
				response= manager.getSoapData(Activation.this, "ajit", "ajit99",strSql);
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

					String templateId;
					if(RowCount>0)
					{
						SoapObject Soap1 = (SoapObject) result.getProperty(0);
						SoapObject Soap2 = (SoapObject) Soap1.getProperty(0);

						RowCount = Soap2.getPropertyCount();
						if(RowCount>0)
						{
							for (int i=0;i<RowCount;i++)
							{

								//String
								SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);

								templateId=Soap4.getProperty("SurveyTemplateID").toString();
								tmplatSurveyTemplateID=Integer.parseInt(templateId);
								if(templateId.equals("anyType{}"))
								{
									templateId="";
								}
							}
							try
							{
								new SoapAsyncDownloadQuestionTypeTable().execute();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							new SoapAsyncDownloadTemplateName().execute();
						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
		}
	}

	public class SoapAsyncDownloadTemplateName extends AsyncTask<String, Void, SoapObject>
	{

		SoapObject response=null;
		int requestcode;
		ProgressDialog progressDialog;
		@Override
		protected SoapObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			try
			{
				DatabaseHelper db=new DatabaseHelper(Activation.this);
				String str="SELECT UserId from CS_Users";
				UserId=db.GetSingleValue(str);
				db.CloseDB();

				String strSql="select  templateName,MainLogo,SurveyTemplateID from cs_template c Where c.custID='"+UserId+"' and c.IsActive=1;";
				response= manager.getSoapData(Activation.this, "ajit", "ajit99",strSql);
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

					String templateText,templateLogo,templateId;
					if(RowCount>0)
					{
						SoapObject Soap1 = (SoapObject) result.getProperty(0);
						SoapObject Soap2 = (SoapObject) Soap1.getProperty(0);

						RowCount = Soap2.getPropertyCount();

						TemplateText=new String[RowCount];
						TemplateLogo=new String[RowCount];
						TemplateId=new String[RowCount];

						for (int i=0;i<RowCount;i++)
						{

							SoapObject Soap4 = (SoapObject) Soap2.getProperty(i);

							templateText=Soap4.getProperty("templateName").toString();
							templateLogo=Soap4.getProperty("MainLogo").toString();
							templateId=Soap4.getProperty("SurveyTemplateID").toString();
							
							templateText= URLEncoder.encode(templateText, "UTF-8");


							if(templateText.equals("anyType{}"))
							{
								templateText="";
							}
							if(templateLogo.equals("anyType{}"))
							{
								templateLogo="";
							}
							if(templateId.equals("anyType{}"))
							{
								templateId="";
							}
							TemplateText[i]=templateText;
							TemplateLogo[i]=templateLogo;
							TemplateId[i]=templateId;
						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}

			//Download noOfTemplates and Show them



		}
	}
	/*	public class AsyncValidateActivation extends AsyncTask<Void, Void, String> {

		ProgressDialog progressDialog;
		public int requestcode;

		@Override
		protected void onPreExecute() {
			/*progressDialog= new ProgressDialog(Activation.this,AlertDialog.THEME_HOLO_DARK);
			progressDialog.setIndeterminate(true);
			progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_anim));
			progressDialog.setCancelable(false);
			progressDialog.setMessage(getResources().getString(R.string.loading));
			progressDialog.show();

			progressDialog = ProgressDialog.show(Activation.this, "", "");
			progressDialog.setContentView(R.layout.progress_main);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		@Override
		protected String doInBackground(Void... params) {

			try{
				//requestcode= manager.validateRegistration(Activation.this,IMEINo);
			}
			catch(Exception e){
				e.printStackTrace();
			}

			if(requestcode!=100)
			{
				res= "Unable to activate your license. Please try again later.";
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				// Second Attempt
				//	requestcode=manager.validateRegistration(Activation.this,IMEINo);

				if(requestcode!=100)
				{
					res= "Unable to activate your license. Please try again later.";
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					// Third Attempt
					//requestcode=manager.validateRegistration(Activation.this,IMEINo);

					if(requestcode!=100)
					{
						res= "Unable to activate your license. Please try again later.";
					}

					else 
					{
						res= "License is activated";
						Intent intent = new Intent(Activation.this, MainActivity.class);
						intent.putExtra("listtype", listType);
						intent.putExtra("name", strname);  
						intent.putExtra("pass", strpass);
						Activation.this.startActivity(intent);
						Activation.this.finish();

						dbHelper=new DatabaseHelper(Activation.this);
						dbHelper.UpdateActivated("1");
						dbHelper.CloseDB();
					}
				}
				else
				{
					res= "License is activated";
					Intent intent = new Intent(Activation.this, MainActivity.class);
					intent.putExtra("listtype", listType);
					intent.putExtra("name", strname);  
					intent.putExtra("pass", strpass);

					Activation.this.startActivity(intent);
					Activation.this.finish();

					dbHelper=new DatabaseHelper(Activation.this);
					dbHelper.UpdateActivated("1");
					dbHelper.CloseDB();				
				}
			}
			else 
			{
				res= "License is activated";
				Intent intent = new Intent(Activation.this, MainActivity.class);
				intent.putExtra("listtype", listType);
				intent.putExtra("name", strname);  
				intent.putExtra("pass", strpass);

				Activation.this.startActivity(intent);
				Activation.this.finish();

				dbHelper=new DatabaseHelper(Activation.this);
				dbHelper.UpdateActivated("1");
				dbHelper.CloseDB();
			}
			return  res;
		}
		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			Toast.makeText(Activation.this, result.toString(),Toast.LENGTH_LONG).show();
		}
	}              */










	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnActivate:
			btnActivate_Click();
			break;

		default:
			break;
		}
	}
	// Async task for getting table locally.

	/*	private class SoapAsyncDownloadTables extends AsyncTask<String, Void, String> {
		//		ProgressDialog progressDialog=new ProgressDialog(Activation.this);
		ProgressDialog progressDialog;	

		String response;
		@Override
		protected void onPreExecute() {
			progressDialog= new ProgressDialog(Activation.this,AlertDialog.THEME_HOLO_DARK);
			progressDialog.setIndeterminate(true);
			progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.anim.progress_dialog_anim));
			progressDialog.setCancelable(false);
			progressDialog.setMessage(getResources().getString(R.string.loading));
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... urls) {

			try{	
				manager.getCity(Activation.this, "City");
				manager.getSpeciality(Activation.this, "Speciality");
				manager.getKeyword(Activation.this, "Keyword");
				manager.getDoctor(Activation.this, "Doctor");
				response = manager.getHospital(Activation.this, "Hospital");
			}
			catch(Exception e){

				e.printStackTrace();
				response="";
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();

			if(!result.equalsIgnoreCase("")){
				SplashActivity.EulaAcceptedBy=2;
				Intent intent = new Intent(Activation.this, MainActivity.class);
				intent.putExtra("listtype", listType);
				startActivity(intent);
				finish();
			}
			else{
				Toast.makeText(Activation.this,"Connection Fail." , Toast.LENGTH_SHORT).show();
			}
		}
	}*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activation, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_settings:
			fromact=true;
			Intent intent=new Intent(Activation.this,Settings.class);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@SuppressLint("NewApi")
	public void animate(){
		Animation animation = null ;
		animation = AnimationUtils.loadAnimation(Activation.this, R.anim.slide_in_right);

		animation.setDuration(1000);
		linearLayout.setAnimation(animation);
		linearLayout.animate();
		animation.start();
	}

	/*public void licenseActivate(){
		res= "License is activated";
		Intent intent = new Intent(Activation.this, MainActivity.class);
		intent.putExtra("listtype", listType);
		Activation.this.startActivity(intent);
		Activation.this.finish();

		dbHelper=new DatabaseHelper(Activation.this);
		dbHelper.UpdateActivated("1");
		dbHelper.CloseDB();
	}*/
	public void getData(){
		strUserName=txtName.getText().toString();
		strname = txtEmailID.getText().toString();
		strpass = txtPassword.getText().toString();
	}
	@Override
	public void onBackPressed()
	{
		CustomAlertDialog cdd=new CustomAlertDialog(Activation.this);
		cdd.show();  
	}

	public static long compareTo( java.util.Date date1, java.util.Date date2 )
	{
		//returns negative value if date1 is before date2
		//returns 0 if dates are even
		//returns positive value if date1 is after date2
		return date1.getTime() - date2.getTime();
	}
	public static final String md5(final String toEncrypt) {
		try {
			final MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(toEncrypt.getBytes());
			final byte[] bytes = digest.digest();
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(String.format("%02X", bytes[i]));
			}
			return sb.toString().toLowerCase();
		} catch (Exception exc) {
			return ""; // Impossibru!
		}
	}
}