package com.cloudservey;


import org.ksoap2.serialization.SoapObject;

import com.asyncmanager.AsyncManager;
import com.classes.QuestionType;
import com.classes.SpinnerAdapter;
import com.classes.Template;
import com.classes.TemplateAnswer;
import com.classes.TemplateQuestion;
import com.classes.TemplateSubQuestion;
import com.cloudservey.MainActivity.MyAdapter;
import com.databasehelper.DatabaseHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class TemplateSelection extends Activity 
{
	LinearLayout layoutTemplateSelection;
	Button Registerdevice;
	EditText txtSiteName;
	DatabaseHelper db;
	AsyncManager manager;
	int Userid;
	int SurveyTempId;
	String ShopName;
	Spinner spinnerAnswer;
	Boolean allSelected=false;
	String imeino;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_selection);

		layoutTemplateSelection=(LinearLayout)findViewById(R.id.layouttemplateSelect);
		Registerdevice=(Button)findViewById(R.id.btnRegister);
		txtSiteName=(EditText)findViewById(R.id.txtSiteName);

		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#31688C")));
		getActionBar().setTitle("Select Template");

		int actionBarTitleId = Resources.getSystem().getIdentifier("Select Template", "id", "android");
		if (actionBarTitleId > 0) 
		{
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null)
			{
				title.setTextColor(Color.WHITE);
				title.setTypeface(Typeface.DEFAULT_BOLD);
			}
		}
		imeino=Activation.IMEINo;
		manager=new AsyncManager();

		spinnerAnswer = new Spinner(TemplateSelection.this);
		android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		spinnerAnswer.setLayoutParams(layoutParams);
		spinnerAnswer.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
		spinnerAnswer.setClickable(true);
		spinnerAnswer.setEnabled(true);

		addSpinner();

		db=new DatabaseHelper(TemplateSelection.this);
		db.CloseDB();


		Registerdevice.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Boolean fromCatch=false;
				try
				{
				String SpinnerAnswer=spinnerAnswer.getSelectedItem().toString();
				int index=0;
				for(int i=0;i<Activation.TemplateText.length;i++)
				{
					if(SpinnerAnswer.equalsIgnoreCase(Activation.TemplateText[i]))
					{
						index=i;
						break;
					}
				}
				SurveyTempId=Integer.parseInt(Activation.TemplateId[index]);
				ShopName=txtSiteName.getText().toString();
				if(ShopName.equalsIgnoreCase(""))
				{
					Toast.makeText(TemplateSelection.this, "Please enter site name", Toast.LENGTH_SHORT).show();
					allSelected=false;
				}
				else {
					allSelected=true;
				}

				if(allSelected==true)
				{
					new SoapAsyncActivateUser().execute();
				}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					Toast.makeText(TemplateSelection.this, "No template is avialable...please create temaplate from web", Toast.LENGTH_LONG).show();
					fromCatch=true;
				}
				if(fromCatch==true)
				{
					finish();
				}
			}
		});
	}

	public void addSpinner()
	{
		try
		{
			@SuppressWarnings("unused")
			SpinnerAdapter adapter = new SpinnerAdapter(TemplateSelection.this, R.layout.spinner_value_layout, Activation.TemplateText,Activation.TemplateLogo);
			@SuppressWarnings("unused")
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TemplateSelection.this,android.R.layout.simple_spinner_dropdown_item, Activation.TemplateText)
			{
				public View getView(int position, View convertView,ViewGroup parent)
				{
					View v = super.getView(position, convertView, parent);

					((TextView) v).setTextSize(16);
					((TextView) v).setTextColor(getResources().getColorStateList(R.color.white));
					return v;
				}

				public View getDropDownView(int position, View convertView,ViewGroup parent) 
				{
					View v = super.getDropDownView(position, convertView,parent);
					v.setBackgroundColor(Color.BLACK);
					((TextView) v).setTextColor(Color.WHITE);
					((TextView) v).setGravity(Gravity.CENTER);

					return v;
				}
			};

			spinnerAnswer.setAdapter(new MyTemplateAdapter(this, R.layout.custom_spinner,Activation.TemplateText));

			layoutTemplateSelection.addView(spinnerAnswer);
			spinnerAnswer.setOnItemSelectedListener(new OnItemSelectedListener()
			{

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,int position, long id)
				{

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
				}
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public class SoapAsyncActivateUser extends AsyncTask<String, Void, SoapObject>
	{
		SoapObject response=null;
		int requestcode;

		protected void onPreExecute()
		{
			progressDialog = ProgressDialog.show(TemplateSelection.this, "", "");
			progressDialog.setContentView(R.layout.progress_main);
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected SoapObject  doInBackground(String... urls) 
		{
			try
			{
				DatabaseHelper db=new DatabaseHelper(TemplateSelection.this);
				String str="SELECT UserId from CS_Users";
				Userid=db.GetSingleValue(str);
				db.CloseDB();

				manager=new AsyncManager();
				response= manager.ActivationUser(TemplateSelection.this, "ajit", "ajit99",imeino,SurveyTempId,Userid,ShopName);

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(SoapObject result)
		{
			try 
			{

				String resultmsg=null;
				resultmsg=result.toString();
				if(resultmsg.equalsIgnoreCase("ActivationResponse{return=SUCCESS; }") || resultmsg.equals("ActivationResponse{return=Device already Exist; }"))
				{

					DatabaseHelper dbHelper=new DatabaseHelper(TemplateSelection.this);
					dbHelper.UpdateActivated("1");
					dbHelper.CloseDB();
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
					if(resultmsg.equals("ActivationResponse{return=No device found; }"))
					{
						Toast.makeText(getApplicationContext(), "your device registration is already full...for device activation go to device setting", Toast.LENGTH_LONG).show();
						finish();
					}
				}		

			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}

		}
	}




	public class MyTemplateAdapter extends ArrayAdapter<String> 
	{

		public MyTemplateAdapter(Context ctx, int txtViewResourceId, String[] objects)
		{
			super(ctx, txtViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) 
		{
			return getCustomView(position, cnvtView, prnt);
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) 
		{
			return getCustomView(pos, cnvtView, prnt);
		}

		public View getCustomView(int position, View convertView,ViewGroup parent)
		{

			LayoutInflater inflater = getLayoutInflater();
			View mySpinner = inflater.inflate(R.layout.custom_spinner, parent,false);
			TextView main_text = (TextView) mySpinner.findViewById(R.id.text_main_seen);


			for(int l=0;l<Activation.TemplateText.length;l++)
			{

				main_text.setText(Activation.TemplateText[l]);
				try
				{
					String bmp="";
					bmp=Activation.TemplateLogo[l];
					ImageView left_icon = (ImageView) mySpinner.findViewById(R.id.left_pic);

					try
					{
						byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
						Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
						left_icon.setImageBitmap(decodedByte);
					}
					catch(Exception e)
					{
						e.getMessage();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}

			return mySpinner;
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
				response= manager.getSoapData(TemplateSelection.this, "ajit", "ajit99",strSql);
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


							DatabaseHelper db1=new DatabaseHelper(TemplateSelection.this);
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
				String strSql="SELECT c.SurveyTemplateID,c.CustID,c.TemplateName,c.NoOfScreens,c.HeadingText,c.ThankYouText,c.SendSMS,c.SMSText,c.SendEmail,c.EmailText,c.MainLogo FROM cs_template c Inner Join cs_device cd On cd.UserID = c.CustID and c.surveyTemplateID=cd.SurveyTemplateID Where c.CustID='"+Userid+"' and cd.DeviceIMEINo='"+imeino+"' and c.IsActive=1;";
				response= manager.getSoapData(TemplateSelection.this, "ajit", "ajit99",strSql);
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
								SurveyTempId=SurveyTemplateID;
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
								DatabaseHelper db1=new DatabaseHelper(TemplateSelection.this);
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
				String strSql="SELECT SurveyTemplateID,QuestionID,QuestionTypeID,QuestionText,ShowImage,QuestionScreenID FROM cs_templatequestion c Where SurveyTemplateID='"+SurveyTempId+"' and status=1 Order By SortId";
				response= manager.getSoapData(TemplateSelection.this, "ajit", "ajit99",strSql);
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
							DatabaseHelper db1=new DatabaseHelper(TemplateSelection.this);
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
				String strSql="SELECT subquestion_id,SurveyTemplateID,QuestionID,AnswerText,Status FROM cs_subquestion c Where SurveyTemplateID='"+SurveyTempId+"' and Status=1";
				response= manager.getSoapData(TemplateSelection.this, "ajit", "ajit99",strSql);
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
							DatabaseHelper db1=new DatabaseHelper(TemplateSelection.this);
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
				String strSql="SELECT AnswerSrNo,SurveyTemplateID,QuestionID,AnswerText,AnswerImage,NextQuestionScreenID FROM cs_templateanswer c Where SurveyTemplateID='"+SurveyTempId+"' and Status=1";
				response= manager.getSoapData(TemplateSelection.this, "ajit", "ajit99",strSql);
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
							String AnswerText, AnswerImage,AnswerBitmap="";

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

							DatabaseHelper db1=new DatabaseHelper(TemplateSelection.this);
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
			Toast.makeText(TemplateSelection.this, "Registration successfull", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(TemplateSelection.this, Welcome.class);
			TemplateSelection.this.startActivity(intent);
			TemplateSelection.this.finish();
		}
	}           


	@Override
	public void onBackPressed()
	{
		CustomAlertDialog cdd=new CustomAlertDialog(TemplateSelection.this);
		cdd.show();  
	}







	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template_selection, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}
}
