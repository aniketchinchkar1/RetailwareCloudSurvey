package com.cloudservey;

import static com.cloudservey.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.cloudservey.CommonUtilities.EXTRA_MESSAGE;
import static com.cloudservey.CommonUtilities.SENDER_ID;
import com.cloudservey.ServerUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import org.ksoap2.serialization.SoapObject;

import android.annotation.SuppressLint;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import android.content.Intent;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asyncmanager.AsyncManager;
import com.classes.SpinnerAdapter;
import com.databasehelper.DatabaseHelper;
import com.google.android.gcm.GCMRegistrar;
import com.utility.Utility;

public class MainActivity extends Activity 
{
	AsyncManager manager;

	DatabaseHelper db;

	protected Dialog mSplashDialog;

	LinearLayout formLayout,cmpnyNameFormLayout,contrlAddlayout,addcontrlAddlayout,addButtonLayout,MainLayout;
	ScrollView mScrollview;
	
	int QuestionId[],QuestionTypeId[],QuestionscreenId[],BackQuetionScreen[],alreadyDisplay[],sessionQuetionId[];

	int Eva,VoucherNo, No_Of_Screen,t=0,Width, No_Of_Question,i=0,NextQuetionScreenId,BackQuetionScreenId,RB_id=100,Cb_Id=200,d=0,k,ScreenWidth,ScreenHeight,cntQue=0;
	int mobilechk=0,emailchk=0,showCount=0,singlecoatcount=0;

	static int Userid;
	public static int offlineCount=0;
	static String IMEI;            
	public static String strThanku;
	static boolean fromOffline,colorFlag=false;
	
	Bitmap selectedSmilyImage=null;
	Bitmap bitmapbmp=null;
	
	boolean isImageFound=false;

	private Bitmap bmImg;

	byte AnswerImg[];

	String Question[],answerArray[],AnswerText[],subQuetionText[],imgArray[],SelectionListAnswerArray[],SelectionListAnswerIMage[],backQueAnswerArray[];

	String uniqueID,strAnsRate,strQuestionType,strCompanyName,Date,Time,img,showImage,backQuetionAnswerText=null;

	AsyncTask<Void, Void, Void> mRegisterTask;
	public  String notification_message;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

			Configuration config = getResources().getConfiguration();
			ScreenWidth=config.screenWidthDp;//320
			ScreenHeight=config.screenHeightDp;//407
			intent= getIntent();
			
			
			mScrollview=(ScrollView)findViewById(R.id.mScrollView);
			MainLayout=(LinearLayout)findViewById(R.id.MainLayout);

			formLayout=(LinearLayout)findViewById(R.id.formLayout);
			cmpnyNameFormLayout=(LinearLayout)findViewById(R.id.cmpnyNameFormLayout);
			contrlAddlayout=(LinearLayout)findViewById(R.id.cntrolFormLayout);
			addcontrlAddlayout=(LinearLayout)findViewById(R.id.addCntrolLayout);
			addButtonLayout=(LinearLayout)findViewById(R.id.addButtonLayout);

			contrlAddlayout.setGravity(Gravity.CENTER);
			addcontrlAddlayout.setGravity(Gravity.CENTER);
			addButtonLayout.setGravity(Gravity.CENTER);

			formLayout.removeAllViews();
			addcontrlAddlayout.removeAllViews();
			
			

			db=new DatabaseHelper(MainActivity.this);
			if(Activation.fromActivation==true)
			{
				Userid=Activation.UserId;
			}
			else
			{
				Userid=SplashActivity.Userid;
			}
			
			String strTemplateName="";
			strTemplateName=db.GetSingleText("SELECT TemplateName FROM CS_Template where CustId='"+Userid+"'");
			String strMainLogo=db.GetSingleText("SELECT MainLogo FROM CS_Template where CustId='"+Userid+"'");

			try
			{
				byte [] encodeByte=Base64.decode(strMainLogo,Base64.DEFAULT);
				bitmapbmp=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			db.CloseDB();



			if(bitmapbmp!=null)
			{
				ImageView imageView = new ImageView(getActionBar().getThemedContext());
				imageView.setImageBitmap(bitmapbmp);
				imageView.buildDrawingCache();
				Drawable d = new BitmapDrawable(getResources(), bitmapbmp);
				getActionBar().setIcon(d);
				getActionBar().isShowing();
			}

			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setHomeButtonEnabled(false);
			getActionBar().setDisplayShowCustomEnabled(true);
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#31688C")));
			
			if(strTemplateName.equalsIgnoreCase(""))
			{

			}
			else
			{
				getActionBar().setTitle(strTemplateName);
			}

			int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
			if (actionBarTitleId > 0) 
			{
				TextView title = (TextView) findViewById(actionBarTitleId);
				if (title != null)
				{
					title.setTextColor(Color.WHITE);
					title.setTypeface(Typeface.DEFAULT_BOLD);
				}
			}


			//offline data store

			try
			{
				try
				{
					db=new DatabaseHelper(MainActivity.this);
					String strQuery="Select OfflineFlag from CS_OfflineCount";
					Cursor cur=db.getData(strQuery);
					int cnt=cur.getCount();
					showCount=cnt;
					if(cur.getCount()>0)
					{
						if(Utility.isNetworkAvailable(MainActivity.this))
						{
							for(int l=0;l<cnt;l++)
							{
								cur.moveToNext();
								offlineCount=Integer.parseInt(cur.getString(0).toString());

								try
								{  	
									fromOffline=true;
									new SoapAccessTask_SaveFeedback().execute();

								}
								catch(Exception e)
								{
									e.printStackTrace();
								}

							}
						}
						cur.close();
						db.CloseDB();

					}
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
			
			
			if(Utility.isNetworkAvailable(MainActivity.this))
			{}
			else 
			{
				if(showCount>0)
				{
					Toast.makeText(MainActivity.this, "Offline data store count is "+showCount, Toast.LENGTH_SHORT).show();
				}
			}

			
			db=new DatabaseHelper(MainActivity.this);
			Cursor cur=db.getData("Select SurveyTemplateId from CS_Template");
			int cnt=cur.getCount();
			if(cnt==0)
			{
				Toast.makeText(MainActivity.this, "Data is not completly downloaded...please try again", Toast.LENGTH_LONG).show();
				finish();
			}
			db.CloseDB();
			
			
			uniqueID = UUID.randomUUID().toString(); 

			TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Service.TELEPHONY_SERVICE);
			telephonyManager.getDeviceId();

			IMEI=telephonyManager.getDeviceId().toString();
			getCompanyName();

			android.widget.LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

			TextView tvCompnyName = new TextView(MainActivity.this);
			tvCompnyName.setText(strCompanyName);
			layoutParams.leftMargin=40;
			layoutParams.rightMargin=40;
			tvCompnyName.setLayoutParams(layoutParams);
			tvCompnyName.setGravity(Gravity.CENTER);
			tvCompnyName.setBackgroundColor(Color.parseColor("#3879A3"));
			tvCompnyName.setTextColor(Color.WHITE);
			tvCompnyName.setTextSize(20);
			cmpnyNameFormLayout.addView(tvCompnyName);
			formLayout.addView(cmpnyNameFormLayout);

			get_NoOfScreens();
			get_TemplateData();
			alreadyDisplay=new int[Question.length];
			BackQuetionScreen=new int[Question.length];

			int j=0; 
			for(i=0;i<Question.length;i++)
			{
				alreadyDisplay[i]=0;
				BackQuetionScreen[i]=0;
			}
			while(QuestionscreenId[j]!=1)
			{
				j=j+1;
			}
			if(j<Question.length)
			{
				k=j;
				setCurntQueNo(k);
				addQuestionsOnLayout(k);

			}
			
			if(Activation.strUserName!=null && Activation.strname!=null){

				sendRegistration();
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	@SuppressLint({ "ClickableViewAccessibility", "ResourceAsColor" })

	public void addQuestionsOnLayout(int i)
	{
		try
		{
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			mScrollview.fullScroll(ScrollView.FOCUS_UP);
			android.widget.LinearLayout.LayoutParams cntrllayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

			String strQuestion=Question[i];

			strQuestionType=get_QuestionType(QuestionTypeId[i]);
			get_Answer(QuestionId[i]);      
			int No_Of_Answers=AnswerText.length;
			No_Of_Screen=QuestionscreenId[i];
			No_Of_Question=Question.length;


			//if only 1 quetion then it will display at middle
			for(cntQue=0;cntQue<QuestionscreenId.length;cntQue++)
			{
				if(QuestionscreenId[i]==No_Of_Screen)
				{
					cntQue=cntQue+1;
				}
			}
			
			LinearLayout container = new LinearLayout(MainActivity.this);
			container.setLayoutParams(cntrllayoutParams);
			container.setOrientation(LinearLayout.HORIZONTAL);
			container.setWeightSum(10);

			if(strQuestionType.equalsIgnoreCase("GridMultichoice"))
			{

				TextView tvQuestionName = new TextView(MainActivity.this);
				android.widget.LinearLayout.LayoutParams quetionlayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				String strText=strQuestion;
				tvQuestionName.setText(strText);
				quetionlayoutParams.topMargin=30;
				quetionlayoutParams.bottomMargin=10;
				tvQuestionName.setLayoutParams(quetionlayoutParams);
				if(cntQue>1)
				{
					tvQuestionName.setGravity(Gravity.CENTER);
				}
				else
				{
					tvQuestionName.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
				}
				tvQuestionName.setTextColor(Color.WHITE);
				tvQuestionName.setTypeface(null, Typeface.BOLD);
				tvQuestionName.setTextSize(20);
				contrlAddlayout.addView(tvQuestionName);
			}
			
			else
			{

				TextView tvQuestionName = new TextView(MainActivity.this);
				android.widget.LinearLayout.LayoutParams quetionlayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				String strText=strQuestion;
				tvQuestionName.setText(strText);
				quetionlayoutParams.topMargin=30;
				quetionlayoutParams.bottomMargin=10;
				tvQuestionName.setLayoutParams(quetionlayoutParams);
				if(cntQue>1)
				{
					tvQuestionName.setGravity(Gravity.CENTER);
				}
				else
				{
					tvQuestionName.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
				}
				tvQuestionName.setTextColor(Color.WHITE);
				tvQuestionName.setTypeface(null, Typeface.BOLD);
				tvQuestionName.setTextSize(20);

				contrlAddlayout.setGravity(Gravity.CENTER);
				contrlAddlayout.addView(tvQuestionName);

			}

			final RadioButton[] rbAnswer = new RadioButton[No_Of_Answers];

			if(strQuestionType.equalsIgnoreCase("Singlechoice"))
			{

				try
				{

					LinearLayout linearAddView=new LinearLayout(MainActivity.this);
					linearAddView.setLayoutParams(cntrllayoutParams);
					linearAddView.setOrientation(LinearLayout.VERTICAL);

					LinearLayout[] linearDynamic=new LinearLayout[No_Of_Answers];
					android.widget.LinearLayout.LayoutParams linearDynamicparams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

					for(int d=0;d<No_Of_Answers;d++)
					{
						linearDynamic[d]=new LinearLayout(MainActivity.this);
						linearDynamic[d].setLayoutParams(linearDynamicparams);
						linearDynamic[d].setGravity(Gravity.CENTER_VERTICAL);
						linearDynamicparams.bottomMargin=10;
						linearDynamicparams.leftMargin=ScreenWidth/3;
						linearDynamicparams.rightMargin=6;
						linearDynamic[d].setOrientation(LinearLayout.HORIZONTAL);

					}

					for(int j=0;j<No_Of_Answers;j++)
					{
						String bmp="";
						Bitmap bmpImage=null;
						get_AnswerArray(QuestionId[i]);
						try
						{
							bmp=answerArray[j];
							ImageView imgAnswer=new ImageView(MainActivity.this);
							LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
							imgAnswer.setLayoutParams(layoutParams);

							String shimg=getShowImage(QuestionId[i]);

							try
							{
								byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
								Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
								bmpImage=getCircularBitmap(strQuestionType,decodedByte,1);
								if(shimg.equalsIgnoreCase("1"))
								{
									imgAnswer.setImageBitmap(bmpImage);
								}
								imgAnswer.setVisibility(View.VISIBLE);

							}
							catch(Exception e)
							{
								e.getMessage();
							}

							linearDynamic[j].addView(imgAnswer);

							chkBackQuetionText(QuestionId[i]);
							//chkBackQueText(QuestionId[i]);

							rbAnswer[j] = new RadioButton(MainActivity.this);
							LinearLayout.LayoutParams layoutParams2 =  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
							rbAnswer[j].setLayoutParams(layoutParams2);
							rbAnswer[j].setText(AnswerText[j]);
							rbAnswer[j].setId(j+RB_id);
							rbAnswer[j].setTextColor(Color.WHITE);	
							rbAnswer[j].setGravity(Gravity.CENTER);
							if(backQuetionAnswerText!=null)
							{
								if(backQuetionAnswerText.equalsIgnoreCase(AnswerText[j]))
								{
									rbAnswer[j].setChecked(true);
									db=new DatabaseHelper(MainActivity.this);
									db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
									backQuetionAnswerText=null;
									db.CloseDB();
								}
							}

							int id = Resources.getSystem().getIdentifier("btn_radio", "drawable", "android");
							Drawable drawable = Resources.getSystem().getDrawable(id);
							drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
							rbAnswer[j].setButtonDrawable(drawable);
							linearDynamic[j].addView(rbAnswer[j]);

						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						linearAddView.addView(linearDynamic[j]);

					}
					contrlAddlayout.setGravity(Gravity.CENTER);
					contrlAddlayout.addView(linearAddView);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}     

				try
				{
					final int NOA=No_Of_Answers;
					for(int n=0;n<No_Of_Answers;n++)
					{

						final int rbindex=n;
						rbAnswer[n].setOnClickListener(new OnClickListener()
						{

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Boolean rbFlag=true;
								if(rbindex!=NOA && rbFlag==true)
								{
									for(int g=0;g<NOA;g++)
									{
										if(g==rbindex)
										{
											rbAnswer[g].setChecked(true);
										}
										else
										{
											rbAnswer[g].setChecked(false);
										}
									}
								}
							}
						});

					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

			}
			else
				if(strQuestionType.equalsIgnoreCase("Multichoice"))
				{
					try
					{

						LinearLayout linearAddView=new LinearLayout(MainActivity.this);
						linearAddView.setLayoutParams(cntrllayoutParams);
						linearAddView.setOrientation(LinearLayout.VERTICAL);

						LinearLayout[] linearDynamic=new LinearLayout[No_Of_Answers];
						android.widget.LinearLayout.LayoutParams linearDynamicparams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

						for(int d=0;d<No_Of_Answers;d++)
						{
							linearDynamic[d]=new LinearLayout(MainActivity.this);
							linearDynamic[d].setLayoutParams(linearDynamicparams);
							linearDynamic[d].setGravity(Gravity.CENTER_VERTICAL);
							linearDynamicparams.bottomMargin=10;
							linearDynamicparams.leftMargin=ScreenWidth/3;
							linearDynamicparams.rightMargin=6;
							linearDynamic[d].setOrientation(LinearLayout.HORIZONTAL);

						}

						for(int j=0;j<No_Of_Answers;j++)
						{
							String bmp="";
							Bitmap bmpImage=null;
							get_AnswerArray(QuestionId[i]);
							try
							{
								bmp=answerArray[j];
								ImageView imgAnswer=new ImageView(MainActivity.this);
								LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
								imgAnswer.setLayoutParams(layoutParams);

								String shimg=getShowImage(QuestionId[i]);

								try
								{
									byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
									Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
									bmpImage=getCircularBitmap(strQuestionType,decodedByte,1);
									if(shimg.equalsIgnoreCase("1"))
									{
										imgAnswer.setImageBitmap(bmpImage);
									}
									imgAnswer.setVisibility(View.VISIBLE);

								}
								catch(Exception e)
								{
									e.getMessage();
								}

								linearDynamic[j].addView(imgAnswer);


								chkBackQueText(QuestionId[i]);

								CheckBox cbAnswer = new CheckBox(MainActivity.this);
								android.widget.LinearLayout.LayoutParams cblinearparams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
								cbAnswer.setLayoutParams(cblinearparams);
								cbAnswer.setText(AnswerText[j]);
								cbAnswer.setId(j+Cb_Id);
								cbAnswer.setTextColor(Color.WHITE);
								if(backQueAnswerArray.length!=0)
								{
									for(int l=0;l<backQueAnswerArray.length;l++)
									{
										if(backQueAnswerArray[l].equalsIgnoreCase(AnswerText[j]))
										{
											cbAnswer.setChecked(true);
											//db=new DatabaseHelper(MainActivity.this);
											//db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
											backQueAnswerArray[l]="";
											//db.CloseDB();
										}
									}
								}
								int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
								Drawable drawable = Resources.getSystem().getDrawable(id);
								drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
								cbAnswer.setButtonDrawable(drawable);
								linearDynamic[j].addView(cbAnswer);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							linearAddView.addView(linearDynamic[j]);
						}
						contrlAddlayout.setGravity(Gravity.CENTER);
						contrlAddlayout.addView(linearAddView);
						db=new DatabaseHelper(MainActivity.this);
						db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
						db.CloseDB();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}                
				}
				else
					if(strQuestionType.equalsIgnoreCase("GridSinglechoice"))
					{
						try
						{

							LinearLayout llHeadingOption=new LinearLayout(MainActivity.this);
							android.widget.LinearLayout.LayoutParams llHeadingOptionParams = new LinearLayout.LayoutParams(ScreenWidth,LayoutParams.WRAP_CONTENT);
							llHeadingOptionParams.leftMargin=10;
							llHeadingOption.setOrientation(LinearLayout.HORIZONTAL);
							llHeadingOption.setLayoutParams(llHeadingOptionParams);

							LinearLayout llOptionSpace=new LinearLayout(MainActivity.this);
							android.widget.LinearLayout.LayoutParams llOptionSpaceParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
							llOptionSpaceParams.weight=10;
							llOptionSpaceParams.leftMargin=10;
							llOptionSpace.setLayoutParams(llOptionSpaceParams);

							LinearLayout llOptionAnswer=new LinearLayout(MainActivity.this);
							android.widget.LinearLayout.LayoutParams llOptionAnswerParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
							llOptionAnswerParams.leftMargin=10;
							llOptionAnswer.setWeightSum(10.0f);
							llOptionAnswerParams.leftMargin=llOptionSpace.getWidth();
							llOptionAnswer.setOrientation(LinearLayout.HORIZONTAL);
							llOptionAnswer.setLayoutParams(llOptionAnswerParams);

							get_SubQuetion(QuestionId[i]);

							int len=0;

							for(int j=0;j<subQuetionText.length;j++)
							{
								if(len<subQuetionText[j].length())
								{
									len=subQuetionText[j].length();
								}
							}
							String blanktext=" ";
							TextView tvblankSpace=new TextView(MainActivity.this);
							for(int u=0;u<len;u++)
							{
								blanktext=blanktext+" ";
							}
							tvblankSpace.setText(blanktext);
							//int l=tvblankSpace.length();
							tvblankSpace.setLayoutParams(llOptionSpaceParams);
							llOptionSpace.addView(tvblankSpace);

							llOptionAnswerParams.leftMargin=15;
							llOptionAnswerParams.rightMargin=15;

							for(int j=0;j<No_Of_Answers;j++)
							{
								TextView tvOptAnswer=new TextView(MainActivity.this);
								llOptionAnswerParams.weight=10.0f/(No_Of_Answers);
								tvOptAnswer.setLayoutParams(llOptionAnswerParams);
								tvOptAnswer.setText(AnswerText[j]);
								tvOptAnswer.setTextColor(Color.WHITE);
								llOptionAnswer.addView(tvOptAnswer);
							}

							llHeadingOption.addView(llOptionSpace);
							llHeadingOption.addView(llOptionAnswer);
							contrlAddlayout.addView(llHeadingOption);

							LinearLayout GridSinlgelayout=new LinearLayout(MainActivity.this);
							android.widget.LinearLayout.LayoutParams GridSinlgelayoutParams = new LinearLayout.LayoutParams(ScreenWidth,LayoutParams.WRAP_CONTENT);
							GridSinlgelayoutParams.leftMargin=10;
							GridSinlgelayout.setOrientation(LinearLayout.VERTICAL);
							GridSinlgelayout.setLayoutParams(GridSinlgelayoutParams);

							LinearLayout[] gridSingleRow=new LinearLayout[subQuetionText.length];
							android.widget.LinearLayout.LayoutParams gridSingleRowParams = new LinearLayout.LayoutParams(ScreenWidth,LayoutParams.WRAP_CONTENT);
							gridSingleRowParams.bottomMargin=20;

							LinearLayout[] llSubQuetion=new LinearLayout[subQuetionText.length];
							android.widget.LinearLayout.LayoutParams llSubQuetionParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

							LinearLayout llrbAnswer=new LinearLayout(MainActivity.this);
							android.widget.LinearLayout.LayoutParams llrbAnswerParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
							llrbAnswerParams.leftMargin=10;
							llrbAnswer.setWeightSum(10.0f);
							llrbAnswerParams.leftMargin=llOptionSpace.getWidth();
							llrbAnswer.setOrientation(LinearLayout.HORIZONTAL);
							llrbAnswer.setLayoutParams(llrbAnswerParams);

							for(int j=0;j<subQuetionText.length;j++)
							{
								gridSingleRow[j]=new LinearLayout(MainActivity.this);
								gridSingleRow[j].setOrientation(LinearLayout.HORIZONTAL);
								gridSingleRow[j].setLayoutParams(gridSingleRowParams);

								llSubQuetion[j]=new LinearLayout(MainActivity.this);
								llSubQuetion[j].setLayoutParams(llSubQuetionParams);
							}

							final RadioButton[] rbGridSinglechoice = new RadioButton[No_Of_Answers];
							int rbId=100;
							//android.widget.LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ScreenWidth,LayoutParams.WRAP_CONTENT);

							int index=-1;
							String SelectedAns="";
							chkBackQueText(QuestionId[i]);
							final RadioGroup[] rgGridSinglechoice = new RadioGroup[subQuetionText.length];
							get_Answer(QuestionId[i]);


							for(int j=0;j<subQuetionText.length;j++)
							{

								rgGridSinglechoice[j]=new RadioGroup(MainActivity.this);
								android.widget.LinearLayout.LayoutParams linearLayoutParams3 = new LinearLayout.LayoutParams(ScreenWidth,LayoutParams.WRAP_CONTENT);
								float w=(10.0f/(No_Of_Answers+1))*No_Of_Answers;
								linearLayoutParams3.weight=w;
								rgGridSinglechoice[j].setLayoutParams(linearLayoutParams3);
								rgGridSinglechoice[j].setOrientation(RadioGroup.HORIZONTAL);


								if(backQueAnswerArray.length!=0)
								{
									SelectedAns=backQueAnswerArray[j];
								}
								for(int h=0;h<AnswerText.length;h++)
								{
									if(SelectedAns.equalsIgnoreCase(AnswerText[h]))
									{
										index=h;
									}
								}


								for (int k = 0; k < No_Of_Answers; k++)
								{
									rbGridSinglechoice[k] = new RadioButton(MainActivity.this);
									rbGridSinglechoice[k].setId(rbId+2);
									rbId=rbId+2;
									int s=0;
									float cn=(float) (No_Of_Answers+2);
									int rbW=(int) (ScreenWidth/cn);
									if(No_Of_Answers==6)
									{
										s=2;
									}
									else
										if(No_Of_Answers==5)
										{
											s=5;
										}
										else
											if(No_Of_Answers==4)
											{
												s=10;
											}
											else
												if(No_Of_Answers==3)
												{
													s=15;
												}
												else
													if(No_Of_Answers==2)
													{
														s=20;
													}

									LinearLayout.LayoutParams rblayoutParams =  new LinearLayout.LayoutParams(rbW+s,LayoutParams.WRAP_CONTENT);
									float w2=w/No_Of_Answers;
									rblayoutParams.weight=w2;
									rblayoutParams.leftMargin=llOptionAnswer.getWidth();
									rblayoutParams.leftMargin=20;
									rbGridSinglechoice[k].setGravity(Gravity.CENTER);
									int id2=0;
									if(k==index)
									{
										rbGridSinglechoice[k].setChecked(true);
										rbGridSinglechoice[k].setSelected(true);
										id2=rbGridSinglechoice[k].getId();
										rgGridSinglechoice[j].check(id2);
									}

									rbGridSinglechoice[k].setLayoutParams(rblayoutParams);
									id2=rbGridSinglechoice[k].getId();
									int id = Resources.getSystem().getIdentifier("btn_radio", "drawable", "android");
									Drawable drawable = Resources.getSystem().getDrawable(id);
									drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
									rbGridSinglechoice[k].setButtonDrawable(drawable);
									rgGridSinglechoice[j].addView(rbGridSinglechoice[k]);
								}
							}
							db=new DatabaseHelper(MainActivity.this);
							db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
							backQuetionAnswerText=null;
							db.CloseDB();

							for(int j=0;j<subQuetionText.length;j++)
							{
								TextView subQue=new TextView(MainActivity.this);
								LinearLayout.LayoutParams layout =  new LinearLayout.LayoutParams(ScreenWidth,LayoutParams.WRAP_CONTENT);
								layout.leftMargin=10;
								layout.bottomMargin=10;
								//layout.weight=10.0f/(No_Of_Answers+1);
								if(No_Of_Answers==6)
								{
									layout.weight=38;
								}
								else
									if(No_Of_Answers==5)
									{
										layout.weight=30;
									}
									else
										if(No_Of_Answers==4)
										{
											layout.weight=23;
										}
										else
											if(No_Of_Answers==3)
											{
												layout.weight=15;
											}
											else
												if(No_Of_Answers==2)
												{
													layout.weight=10;
												}

								subQue.setLayoutParams(layout);
								subQue.setText(subQuetionText[j]);
								subQue.setTextColor(Color.WHITE);
								gridSingleRow[j].addView(subQue);
								gridSingleRow[j].addView(rgGridSinglechoice[j]);
								GridSinlgelayout.addView(gridSingleRow[j]);

							}
							GridSinlgelayout.setGravity(Gravity.CENTER);
							contrlAddlayout.addView(GridSinlgelayout);

						}
						catch(Exception e)
						{
							e.printStackTrace();
						}


					}
					else
						if(strQuestionType.equalsIgnoreCase("GridMultichoice"))
						{
							try
							{
								int No_Of_Layout=0;

								LinearLayout.LayoutParams layout2 =  new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
								LinearLayout AnswerContainer=new LinearLayout(MainActivity.this);
								AnswerContainer.setOrientation(LinearLayout.VERTICAL);
								AnswerContainer.setLayoutParams(layout2);

								if(No_Of_Answers%2==0)
								{
									No_Of_Layout=No_Of_Answers/2;
								}

								else
								{
									No_Of_Layout=(No_Of_Answers/2)+1;

								}

								LinearLayout[] Answer=new LinearLayout[No_Of_Layout];

								for(int j=0;j<No_Of_Layout;j++)
								{
									Answer[j]=new LinearLayout(MainActivity.this);
									Answer[j].setOrientation(LinearLayout.HORIZONTAL);
									layout2.bottomMargin=10;
									layout2.weight=8;
									Answer[j].setLayoutParams(layout2);
								}

								int k=0;
								chkBackQueText(QuestionId[i]);
								for(int j=0;j<No_Of_Answers;j++)
								{
									String bmp="";
									int ansCount=0;
									//chkBackQueText(QuestionId[i]);
									get_AnswerArray(QuestionId[i]);
									try
									{
										bmp=answerArray[j];
										ImageView imgAnswer=new ImageView(MainActivity.this);
										LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
										layoutParams.weight=2;
										imgAnswer.setLayoutParams(layoutParams);

										try
										{
											String shimg=getShowImage(QuestionId[i]);
											byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
											Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
											Bitmap bmpImage=getCircularBitmap(strQuestionType,decodedByte,1);
											if(shimg.equalsIgnoreCase("1"))
											{
												imgAnswer.setImageBitmap(bmpImage);
											}
											imgAnswer.setVisibility(View.VISIBLE);
											Answer[k].addView(imgAnswer);

											int len=0;
											for(int l=0;l<AnswerText.length;l++)
											{
												if(len<AnswerText[l].length())
												{
													len=AnswerText[l].length();
												}
											}

											String padded=" ";
											CheckBox cbAnswer = new CheckBox(MainActivity.this);
											LinearLayout.LayoutParams cblayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
											cblayoutParams.weight=2;
											String format="%-"+len+"s";
											cbAnswer.setLayoutParams(cblayoutParams);
											if(AnswerText[j].length()!=len)
											{
												padded = String.format(format, AnswerText[j]);
											}
											else
											{
												padded=AnswerText[j];
											}
											cbAnswer.setText(padded);
											cbAnswer.setTextColor(Color.WHITE);
											if(backQueAnswerArray.length!=0)
											{
												for(int l=0;l<backQueAnswerArray.length;l++)
												{
													if(backQueAnswerArray[l].equalsIgnoreCase(AnswerText[j]))
													{
														cbAnswer.setChecked(true);
														db=new DatabaseHelper(MainActivity.this);
														db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
														backQueAnswerArray[l]="";
														db.CloseDB();
													}
												}
											}
											int id = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
											Drawable drawable = Resources.getSystem().getDrawable(id);
											drawable.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
											cbAnswer.setButtonDrawable(drawable);
											Answer[k].addView(cbAnswer);
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
									if(j==1||j==3||j==5||j==7||j==9||j==11)
									{
										k++;
									}
									else
									{
										if(j==No_Of_Answers-1){break;}
									}
								}
								k=No_Of_Layout-1;
								//int childCount=Answer[k].getChildCount();
								if(Answer[k].getChildCount()!=4)
								{
									ImageView imgblankextra=new ImageView(MainActivity.this);
									LinearLayout.LayoutParams imgblankextraparams =  new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
									imgblankextraparams.weight=2;
									imgblankextra.setLayoutParams(imgblankextraparams);
									Answer[k].addView(imgblankextra);

									TextView tvblankextra=new TextView(MainActivity.this);
									LinearLayout.LayoutParams tvblankextraparams =  new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
									tvblankextraparams.weight=2;
									tvblankextra.setLayoutParams(tvblankextraparams);
									Answer[k].addView(tvblankextra);
								}
								for(k=0;k<No_Of_Layout;k++)
								{
									AnswerContainer.addView(Answer[k]);
								}

								contrlAddlayout.setGravity(Gravity.CENTER);
								contrlAddlayout.addView(AnswerContainer);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}

						else if(strQuestionType.equalsIgnoreCase("SelectionList"))
						{
							try
							{
								final Spinner spinnerAnswer = new Spinner(MainActivity.this);
								spinnerAnswer.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
								spinnerAnswer.setClickable(true);
								spinnerAnswer.setEnabled(true);
								spinnerAnswer.setLayoutParams(cntrllayoutParams);

								get_AnswerArray(QuestionId[i]);
								SelectionListAnswerArray=AnswerText;
								SelectionListAnswerIMage=answerArray;
								@SuppressWarnings("unused")
								SpinnerAdapter adapter = new SpinnerAdapter(MainActivity.this, R.layout.spinner_value_layout, SelectionListAnswerArray,SelectionListAnswerIMage);
								@SuppressWarnings("unused")
								ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item, SelectionListAnswerArray)
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

								spinnerAnswer.setAdapter(new MyAdapter(this, R.layout.custom_spinner,SelectionListAnswerArray,QuestionId[i]));

								contrlAddlayout.addView(spinnerAnswer);
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

						else
							if(strQuestionType.equalsIgnoreCase("InlineText")||strQuestionType.equalsIgnoreCase("MultilineText"))
							{
								try
								{
									LinearLayout linearDynamic=new LinearLayout(MainActivity.this);
									android.widget.LinearLayout.LayoutParams linearEditLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
									linearDynamic.setLayoutParams(linearEditLayoutParams);
									linearDynamic.setOrientation(LinearLayout.HORIZONTAL);

									LinearLayout linearImage=new LinearLayout(MainActivity.this);
									android.widget.LinearLayout.LayoutParams linearImageLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
									linearImage.setLayoutParams(linearImageLayoutParams);
									linearImage.setOrientation(LinearLayout.VERTICAL);

									for(int j=0;j<No_Of_Answers;j++)
									{
										String bmp="";
										get_AnswerArray(QuestionId[i]);
										try
										{
											bmp=answerArray[j];
											ImageView imgAnswer=new ImageView(MainActivity.this);
											LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
											imgAnswer.setLayoutParams(layoutParams);

											getShowImage(QuestionId[i]);
											if(showImage.equalsIgnoreCase("1"))
											{
												try
												{
													byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
													Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
													Bitmap bmpImage=getCircularBitmap(strQuestionType,decodedByte,1);

													imgAnswer.setImageBitmap(bmpImage);

													imgAnswer.setVisibility(View.VISIBLE);
													linearImage.addView(imgAnswer);
												}
												catch(Exception e)
												{
													e.getMessage();
												}
											}
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}

									}

									EditText edAnswer = new EditText(MainActivity.this);
									edAnswer.setLayoutParams(cntrllayoutParams); 
									edAnswer.setTextColor(Color.WHITE);

									if(strQuestionType.equalsIgnoreCase("InlineText"))
									{
										edAnswer.setMaxLines(1);
										edAnswer.setSingleLine(true);
										edAnswer.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

									}
									else
									{
										edAnswer.setEms(10);
										edAnswer.setMaxLines(10);
										//edAnswer.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
										edAnswer.setHorizontalScrollBarEnabled(false);
										edAnswer.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE |InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
										edAnswer.setSingleLine(false);
									}
									chkBackQuetionText(QuestionId[i]);
									if(backQuetionAnswerText!=null)
									{
										edAnswer.setText(backQuetionAnswerText);
										db=new DatabaseHelper(MainActivity.this);
										db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
										backQuetionAnswerText=null;
										db.CloseDB();
									}
									linearDynamic.addView(linearImage);
									linearDynamic.addView(edAnswer);
									contrlAddlayout.setGravity(Gravity.CENTER);
									contrlAddlayout.addView(linearDynamic);
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}

							else
								if(strQuestionType.equalsIgnoreCase("Date"))
								{
									try
									{
										LinearLayout linearDynamic=new LinearLayout(MainActivity.this);
										android.widget.LinearLayout.LayoutParams linearEditLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
										linearDynamic.setLayoutParams(linearEditLayoutParams);
										linearDynamic.setOrientation(LinearLayout.HORIZONTAL);

										LinearLayout linearImage=new LinearLayout(MainActivity.this);
										android.widget.LinearLayout.LayoutParams linearImageLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
										linearImage.setLayoutParams(linearImageLayoutParams);
										linearImage.setOrientation(LinearLayout.VERTICAL);

										for(int j=0;j<No_Of_Answers;j++)
										{
											String bmp="";
											get_AnswerArray(QuestionId[i]);
											try
											{
												bmp=answerArray[j];
												ImageView imgAnswer=new ImageView(MainActivity.this);
												LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
												imgAnswer.setLayoutParams(layoutParams);

												getShowImage(QuestionId[i]);
												if(showImage.equalsIgnoreCase("1"))
												{
													try
													{
														byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
														Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
														Bitmap bmpImage=getCircularBitmap(strQuestionType,decodedByte,1);

														imgAnswer.setImageBitmap(bmpImage);
														imgAnswer.setVisibility(View.VISIBLE);
														linearImage.addView(imgAnswer);
													}
													catch(Exception e)
													{
														e.getMessage();
													}
												}
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}

										}

										final TextView edDate = new TextView(MainActivity.this);
										edDate.setTextColor(getResources().getColor(R.color.white));
										edDate.setHint("Click here");
										edDate.setHintTextColor(Color.WHITE);
										edDate.setClickable(false);

										edDate.setLayoutParams(cntrllayoutParams); 
										edDate.setTextColor(Color.WHITE);

										chkBackQuetionText(QuestionId[i]);
										if(backQuetionAnswerText!=null)
										{
											edDate.setText(backQuetionAnswerText);
											db=new DatabaseHelper(MainActivity.this);
											db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
											backQuetionAnswerText=null;
											db.CloseDB();
										}

										linearDynamic.addView(linearImage);
										linearDynamic.addView(edDate);
										contrlAddlayout.setGravity(Gravity.CENTER);
										contrlAddlayout.addView(linearDynamic);

										final Calendar myCalendar = Calendar.getInstance();

										final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
										{
											@Override
											public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
											{
												myCalendar.set(Calendar.YEAR, year);
												myCalendar.set(Calendar.MONTH, monthOfYear);
												myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
												String myFormat = "MM/dd/yy";
												SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
												edDate.setText(sdf.format(myCalendar.getTime()));

											}

										};
										edDate.setOnClickListener(new OnClickListener()
										{

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method stub
												new DatePickerDialog(MainActivity.this, date, myCalendar
														.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
														myCalendar.get(Calendar.DAY_OF_MONTH)).show();
											}
										});

									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
								}

								else
									if(strQuestionType.equalsIgnoreCase("MobileNo"))
									{
										try
										{
											LinearLayout linearDynamic=new LinearLayout(MainActivity.this);
											android.widget.LinearLayout.LayoutParams linearEditLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
											linearDynamic.setLayoutParams(linearEditLayoutParams);
											linearDynamic.setOrientation(LinearLayout.HORIZONTAL);

											LinearLayout linearImage=new LinearLayout(MainActivity.this);
											android.widget.LinearLayout.LayoutParams linearImageLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
											linearImage.setLayoutParams(linearImageLayoutParams);
											linearImage.setOrientation(LinearLayout.VERTICAL);

											for(int j=0;j<No_Of_Answers;j++)
											{
												String bmp="";
												get_AnswerArray(QuestionId[i]);
												try
												{
													bmp=answerArray[j];
													ImageView imgAnswer=new ImageView(MainActivity.this);
													LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
													imgAnswer.setLayoutParams(layoutParams);

													getShowImage(QuestionId[i]);
													if(showImage.equalsIgnoreCase("1"))
													{
														try
														{
															byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
															Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
															Bitmap bmpImage=getCircularBitmap(strQuestionType,decodedByte,1);

															imgAnswer.setImageBitmap(bmpImage);
															imgAnswer.setVisibility(View.VISIBLE);
															linearImage.addView(imgAnswer);
														}
														catch(Exception e)
														{
															e.getMessage();
														}
													}
												}
												catch(Exception e)
												{
													e.printStackTrace();
												}

											}

											EditText edAnswer = new EditText(MainActivity.this);
											int maxLength = 10;
											InputFilter[] FilterArray = new InputFilter[1];
											FilterArray[0] = new InputFilter.LengthFilter(maxLength);
											edAnswer.setFilters(FilterArray);
											edAnswer.setLayoutParams(cntrllayoutParams); 
											edAnswer.setTextColor(Color.WHITE);
											edAnswer.setMaxHeight(1);
											edAnswer.setMaxEms(10);
											edAnswer.setMaxWidth(10);
											edAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);

											chkBackQuetionText(QuestionId[i]);
											if(backQuetionAnswerText!=null)
											{
												edAnswer.setText(backQuetionAnswerText);
												db=new DatabaseHelper(MainActivity.this);
												db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
												backQuetionAnswerText=null;
												db.CloseDB();
											}

											linearDynamic.addView(linearImage);
											linearDynamic.addView(edAnswer);
											contrlAddlayout.setGravity(Gravity.CENTER);
											contrlAddlayout.addView(linearDynamic);
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
									}
									else
										if(strQuestionType.equalsIgnoreCase("Email"))
										{
											try
											{
												LinearLayout linearDynamic=new LinearLayout(MainActivity.this);
												android.widget.LinearLayout.LayoutParams linearEditLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
												linearDynamic.setLayoutParams(linearEditLayoutParams);
												linearDynamic.setOrientation(LinearLayout.HORIZONTAL);

												LinearLayout linearImage=new LinearLayout(MainActivity.this);
												android.widget.LinearLayout.LayoutParams linearImageLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
												linearImage.setLayoutParams(linearImageLayoutParams);
												linearImage.setOrientation(LinearLayout.VERTICAL);
												for(int j=0;j<No_Of_Answers;j++)
												{
													String bmp="";
													get_AnswerArray(QuestionId[i]);
													try
													{
														bmp=answerArray[j];
														ImageView imgAnswer=new ImageView(MainActivity.this);
														LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
														imgAnswer.setLayoutParams(layoutParams);

														getShowImage(QuestionId[i]);
														if(showImage.equalsIgnoreCase("1"))
														{
															try
															{

																byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
																Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
																Bitmap bmpImage=getCircularBitmap(strQuestionType,decodedByte,1);

																imgAnswer.setImageBitmap(bmpImage);
																imgAnswer.setVisibility(View.VISIBLE);
																linearImage.addView(imgAnswer);
															}
															catch(Exception e)
															{
																e.getMessage();
															}
														}
													}
													catch(Exception e)
													{
														e.printStackTrace();
													}

												}
												EditText edEmail = new EditText(MainActivity.this);
												edEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
												edEmail.setLayoutParams(cntrllayoutParams); 
												edEmail.setTextColor(Color.WHITE);

												chkBackQuetionText(QuestionId[i]);
												if(backQuetionAnswerText!=null)
												{
													edEmail.setText(backQuetionAnswerText);
													db=new DatabaseHelper(MainActivity.this);
													db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
													backQuetionAnswerText=null;
													db.CloseDB();
												}

												linearDynamic.addView(linearImage);
												linearDynamic.addView(edEmail);
												contrlAddlayout.setGravity(Gravity.CENTER);
												contrlAddlayout.addView(linearDynamic);
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}
										}
										else
											if(strQuestionType.equalsIgnoreCase("StarRating"))
											{
												try
												{
													LinearLayout linearDynamic=new LinearLayout(MainActivity.this);
													android.widget.LinearLayout.LayoutParams linearEditLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
													linearDynamic.setLayoutParams(linearEditLayoutParams);
													linearDynamic.setGravity(Gravity.CENTER);
													linearDynamic.setOrientation(LinearLayout.HORIZONTAL);

													LinearLayout linearImage=new LinearLayout(MainActivity.this);
													android.widget.LinearLayout.LayoutParams linearImageLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
													linearImage.setLayoutParams(linearImageLayoutParams);
													linearImage.setOrientation(LinearLayout.VERTICAL);
													String bmp="";
													get_AnswerArray(QuestionId[i]);
													try
													{
														bmp=answerArray[0];
														ImageView imgAnswer=new ImageView(MainActivity.this);
														LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
														imgAnswer.setLayoutParams(layoutParams);

														getShowImage(QuestionId[i]);
														if(showImage.equalsIgnoreCase("1"))
														{
															try
															{
																byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
																Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
																Bitmap bmpImage=getCircularBitmap(strQuestionType,decodedByte,1);

																imgAnswer.setImageBitmap(bmpImage);
																imgAnswer.setVisibility(View.VISIBLE);

															}
															catch(Exception e)
															{
																e.getMessage();
															}
														}
														linearImage.addView(imgAnswer);
													}
													catch(Exception e)
													{
														e.printStackTrace();
													}

													final RatingBar rating =new RatingBar(MainActivity.this, null, android.R.attr.ratingBarStyleSmall);
													rating.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
													rating.setMax(5);
													rating.setEnabled(true);
													rating.setClickable(true);
													rating.setNumStars(5);
													chkBackQuetionText(QuestionId[i]);
													if(backQuetionAnswerText!=null)
													{
														rating.setRating(Float.parseFloat(backQuetionAnswerText));
														db=new DatabaseHelper(MainActivity.this);
														db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
														backQuetionAnswerText=null;
														db.CloseDB();
													}
													else
													{
														rating.setRating(Float.parseFloat("1.0"));
													}

													linearDynamic.addView(linearImage);
													linearDynamic.addView(rating);
													contrlAddlayout.setGravity(Gravity.CENTER);
													contrlAddlayout.addView(linearDynamic);

													rating.setOnTouchListener(new OnTouchListener()
													{

														@Override
														public boolean onTouch(View v, MotionEvent event) 
														{

															if (event.getAction() == MotionEvent.ACTION_DOWN) 
															{
																float touchPositionX = event.getX();
																float width = rating.getWidth();
																float starsf = (touchPositionX / width) * 5.0f;
																int stars = (int)starsf + 1;
																rating.setRating(stars);                
																v.setPressed(false);
															}

															if (event.getAction() == MotionEvent.ACTION_UP)
															{
																v.setPressed(true);
															}

															if (event.getAction() == MotionEvent.ACTION_CANCEL)
															{
																v.setPressed(false);
															}
															return true;
														}
													});
												}
												catch(Exception e)
												{
													e.printStackTrace();
												}
											}

											else
												if(strQuestionType.equalsIgnoreCase("Smiley"))
												{
													try
													{
														String bmp="";
														/*LinearLayout imageContainer=new LinearLayout(MainActivity.this);
														LinearLayout.LayoutParams imglayoutParams = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
														imageContainer.setOrientation(LinearLayout.VERTICAL);
														imageContainer.setLayoutParams(imglayoutParams);*/
														imgArray=get_AnswerArray(QuestionId[i]);
														chkBackQuetionText(QuestionId[i]);
														for(int l=0;l<imgArray.length;l++)
														{
															try
															{
																bmp=imgArray[l];
																ImageView imgSmily=new ImageView(MainActivity.this);
																LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
																imgSmily.setLayoutParams(layoutParams);

																try
																{
																	byte[] decodedString = Base64.decode(bmp, Base64.DEFAULT);
																	Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
																	//Bitmap bmpImage=getCircularBitmap(decodedByte,1);

																	imgSmily.setImageBitmap(decodedByte);
																	imgSmily.setVisibility(View.VISIBLE);


																	if(backQuetionAnswerText!=null)
																	{
																		if(backQuetionAnswerText.equalsIgnoreCase(imgArray[l]))
																		{
																			imgSmily.getDrawable().setColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP);
																			db=new DatabaseHelper(MainActivity.this);
																			db.DeleteData("Delete from CS_BackSession where QuestionID=='"+QuestionId[i]+"'");
																			backQuetionAnswerText=null;
																			selectedSmilyImage=decodedByte;
																			db.CloseDB();

																		}

																	}

																	contrlAddlayout.setGravity(Gravity.CENTER);
																	contrlAddlayout.addView(imgSmily);
																}
																catch(Exception e)
																{
																	e.getMessage();
																}

																imgSmily.setOnTouchListener(new OnTouchListener()
																{
																	String bmpp="";
																	int imgIndex;;
																	@Override
																	public boolean onTouch(View v, MotionEvent event)
																	{

																		ImageView view = (ImageView) v;
																		Bitmap bitmap = null;
																		if (view.getDrawable() instanceof BitmapDrawable)
																		{
																			bitmap = ((BitmapDrawable) view.getDrawable()).getBitmap();
																		} else 
																		{
																			Drawable d = view.getDrawable();
																			bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
																			Canvas canvas = new Canvas(bitmap);
																			d.draw(canvas);
																		}
																		for(int j=0;j<imgArray.length;j++)
																		{
																			bmpp=imgArray[j];
																			byte[] decodedString = Base64.decode(bmpp, Base64.DEFAULT);
																			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
																			Boolean cFlag=compare(bitmap,decodedByte);
																			if(cFlag==true)
																			{
																				imgIndex=j;
																				view.getDrawable().setColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP);
																				selectedSmilyImage=bitmap;

																			}
																			else
																			{
																				clearColorFilter(contrlAddlayout,imgIndex);
																			}
																		}
																		return false;
																	}
																});

															}
															catch(Exception e)
															{
																e.printStackTrace();
															}


														}
														//contrlAddlayout.addView(imageContainer);
													}
													catch(Exception e)
													{
														e.printStackTrace();
													}
												}


			android.widget.LinearLayout.LayoutParams btnNcntrllayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			Button btnNext = new Button(MainActivity.this);
			if(strQuestionType.equalsIgnoreCase("Date"))
			{
				btnNcntrllayoutParams.topMargin=10;
			}
			else
			{
				btnNcntrllayoutParams.topMargin=5;
			}
			btnNext.setBackgroundResource(R.drawable.buttons);
			btnNext.setTextAppearance(this, R.style.ButtonText);
			btnNext.setText("Next");
			btnNext.setTextColor(Color.WHITE);
			btnNext.setGravity(Gravity.CENTER);

			android.widget.LinearLayout.LayoutParams btnBcntrllayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			if(strQuestionType.equalsIgnoreCase("Date"))
			{
				btnBcntrllayoutParams.topMargin=10;
			}
			else
			{
				btnBcntrllayoutParams.topMargin=5;
			}
			Button btnBack = new Button(MainActivity.this);
			btnBack.setBackgroundResource(R.drawable.buttons);
			btnBack.setTextAppearance(this, R.style.ButtonText);
			btnBack.setText("Back");
			btnBack.setTextColor(Color.WHITE);
			btnNext.setGravity(Gravity.CENTER);

			android.widget.LinearLayout.LayoutParams btnScntrllayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			if(strQuestionType.equalsIgnoreCase("Date"))
			{
				btnScntrllayoutParams.topMargin=10;
			}
			else
			{
				btnScntrllayoutParams.topMargin=5;
			}
			Button btnSubmit = new Button(MainActivity.this);
			btnSubmit.setBackgroundResource(R.drawable.buttons);
			btnSubmit.setTextAppearance(this, R.style.ButtonText);
			btnSubmit.setText("Submit");
			btnNext.setGravity(Gravity.CENTER);
			btnSubmit.setTextColor(Color.WHITE);

			if(Eva==1)
			{
				addButtonLayout.removeAllViews();
				btnScntrllayoutParams.weight=5;
				btnSubmit.setLayoutParams(btnScntrllayoutParams);
				addButtonLayout.addView(btnSubmit);
				contrlAddlayout.setGravity(Gravity.CENTER);
				contrlAddlayout.addView(addButtonLayout);
			}
			else

				if(No_Of_Screen==1 && No_Of_Screen!=Eva)
				{
					addButtonLayout.removeAllViews();
					btnNcntrllayoutParams.weight=5;
					btnNext.setLayoutParams(btnNcntrllayoutParams);
					addButtonLayout.addView(btnNext);
					contrlAddlayout.setGravity(Gravity.CENTER);
					contrlAddlayout.addView(addButtonLayout);
				}
				else
					if(No_Of_Screen==Eva)
					{
						addButtonLayout.removeAllViews();
						btnBcntrllayoutParams.weight=5;
						btnBack.setLayoutParams(btnBcntrllayoutParams);
						btnScntrllayoutParams.weight=5;
						btnScntrllayoutParams.leftMargin=10;
						btnSubmit.setLayoutParams(btnScntrllayoutParams);
						addButtonLayout.addView(btnBack);
						addButtonLayout.addView(btnSubmit);
						contrlAddlayout.setGravity(Gravity.CENTER);
						contrlAddlayout.addView(addButtonLayout);

					}
					else
						if(No_Of_Screen>1 && No_Of_Screen<Eva ||i<No_Of_Question-1)
						{

							addButtonLayout.removeAllViews();
							btnBcntrllayoutParams.weight=5;
							btnBack.setLayoutParams(btnBcntrllayoutParams);
							btnNcntrllayoutParams.weight=5;
							btnNcntrllayoutParams.leftMargin=10;
							btnNext.setLayoutParams(btnNcntrllayoutParams);
							addButtonLayout.addView(btnBack);
							addButtonLayout.addView(btnNext);
							contrlAddlayout.setGravity(Gravity.CENTER);
							contrlAddlayout.addView(addButtonLayout);

						}
			try
			{
				contrlAddlayout.setGravity(Gravity.CENTER);
				addcontrlAddlayout.addView(contrlAddlayout);
				addcontrlAddlayout.setVisibility(View.VISIBLE);
				formLayout.addView(addcontrlAddlayout);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				int m=0;
				for(m=i+1;m<Question.length;m++)
				{
					if(QuestionscreenId[i]==QuestionscreenId[m])
					{
						contrlAddlayout.setGravity(Gravity.CENTER);
						contrlAddlayout.removeView(addButtonLayout);
						alreadyDisplay[m]=1;
						addQuestionsOnLayout(m);
						break;
					}

				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			btnSubmit.setOnClickListener(new OnClickListener()
			{
				Boolean check;
				@Override
				public void onClick(View v) {
					check=saveAnswers(contrlAddlayout);
					if(check==true)
					{
						if(Utility.isNetworkAvailable(MainActivity.this))
						{
							try
							{
								new SoapAccessTask_SaveFeedback().execute();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							offlineCount=offlineCount+1;
							db=new DatabaseHelper(MainActivity.this);
							db.UpdateData("UPDATE CS_Session SET OfflineFlag='"+offlineCount+"' WHERE GUID=='"+uniqueID+"'");
							db.InsertData("Insert into CS_OfflineCount(OfflineFlag) values('"+offlineCount+"')");
							db.CloseDB();

							Intent i = new Intent(MainActivity.this,ThankYou.class);
							startActivity(i);
							finish();

							db=new DatabaseHelper(MainActivity.this);
							int SurveyTmpId=db.GetSingleValue("Select SurveyTemplateID from CS_Template where CustID='"+Userid+"'");
							strThanku=db.GetSingleText("Select ThankyouText from CS_Template where CustID='"+Userid+"' and SurveyTemplateID='"+SurveyTmpId+"'");
							db.CloseDB();
						}
					}
					else
					{
						try
						{

							if(mobilechk==1)
							{
								Toast.makeText(MainActivity.this,"Please enter 1o digit mobile number", Toast.LENGTH_SHORT).show();
							}
							else
								if(emailchk==1)
								{
									Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
								}
								else
								{
									Toast.makeText(MainActivity.this,"Please select answer", Toast.LENGTH_SHORT).show();
								}
							db=new DatabaseHelper(MainActivity.this);
							db.DeleteData("Delete from CS_Session where QuestionScreenID='"+QuestionscreenId[k]+"'");
							db.DeleteData("Delete from CS_BackSession");
							db.CloseDB();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}

				}
			});
			btnBack.setOnClickListener(new OnClickListener()
			{

				@SuppressWarnings("unused")
				@Override
				public void onClick(View v)
				{
					int h=0;
					int Screen_No=QuestionscreenId[k];
					int backCurScreenNo;

					for(int j=0;j<Question.length;j++)
					{
						if(BackQuetionScreen[j]!=0)
						{
							h=j;
						}
					}
					backCurScreenNo=BackQuetionScreen[h];
					for(int j=0;j<Question.length;j++)
					{
						if(BackQuetionScreen[j]==backCurScreenNo)
						{
							BackQuetionScreen[j]=0;
						}
					}

					for(int j=0;j<Question.length;j++)
					{
						try
						{
							if(QuestionscreenId[j]==backCurScreenNo)
							{
								db=new DatabaseHelper(MainActivity.this);
								db.DeleteData("Delete from CS_Session where QuestionScreenID='"+QuestionscreenId[j]+"'");
								db.CloseDB();

							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}		

					for(int j=0;j<Question.length;j++)
					{
						if(QuestionscreenId[j]==backCurScreenNo)
						{
							setCurntQueNo(j);
							RemoveView();
							addQuestionsOnLayout(j);
							break;
						}
					}
					animateLeft();
				}

			});

			btnNext.setOnClickListener(new OnClickListener()
			{
				Boolean check;
				@Override
				public void onClick(View v)
				{
					check=saveAnswers(contrlAddlayout);
					if(check==true)
					{
						if(NextQuetionScreenId==0)
						{
							if(Utility.isNetworkAvailable(MainActivity.this))
							{
								try
								{
									new SoapAccessTask_SaveFeedback().execute();
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								offlineCount=offlineCount+1;
								db=new DatabaseHelper(MainActivity.this);
								db.UpdateData("UPDATE CS_Session SET OfflineFlag='"+offlineCount+"' WHERE GUID=='"+uniqueID+"'");
								db.InsertData("Insert into CS_OfflineCount(OfflineFlag) values('"+offlineCount+"')");
								db.CloseDB();

								Intent i = new Intent(MainActivity.this,ThankYou.class);
								startActivity(i);
								finish();

								db=new DatabaseHelper(MainActivity.this);
								int SurveyTmpId=db.GetSingleValue("Select SurveyTemplateID from CS_Template where CustID='"+Userid+"'");
								strThanku=db.GetSingleText("Select ThankyouText from CS_Template where CustID='"+Userid+"' and SurveyTemplateID='"+SurveyTmpId+"'");
								db.CloseDB();
							}
						}
						else
							if(NextQuetionScreenId!=-1)
							{
								try
								{
									RemoveView();
									db=new DatabaseHelper(MainActivity.this);
									String strQuery="Select QuestionText from CS_TemplateQuestion where QuestionScreenID='"+NextQuetionScreenId+"'";
									Cursor cur=db.getData(strQuery);
									int cnt=cur.getCount();
									String QueText[]=new String[cnt];

									if(cur.getCount()>0)
									{
										for(int i=0;i<cnt;i++)
										{
											cur.moveToNext();
											QueText[i]=cur.getString(0).toString();
										}
									}

									db.CloseDB();
									for(int j=0;j<Question.length;j++)
									{
										if(Question[j].equalsIgnoreCase(QueText[0]))
										{
											setCurntQueNo(j);
											NextQuetionScreenId=-1;
											addQuestionsOnLayout(j);

										}
									}
								}
								catch(Exception e)
								{
									Toast.makeText(getApplicationContext(), "No Quetion Avialable for selected ScreenId", Toast.LENGTH_SHORT).show();
								}
							}
					}
					else
					{
						if(mobilechk==1)
						{
							Toast.makeText(MainActivity.this,"Please enter 1o digit mobile number", Toast.LENGTH_SHORT).show();
							mobilechk=0;
						}
						else
							if(emailchk==1)
							{
								Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
								emailchk=0;
							}
							else if(singlecoatcount==1)
							{
								Toast.makeText(getApplicationContext(),"Single coat not allowed", Toast.LENGTH_SHORT).show();
								singlecoatcount=0;
							}
							else
								Toast.makeText(MainActivity.this,"Please select answer", Toast.LENGTH_SHORT).show();
					}
					if(NextQuetionScreenId!=0 || No_Of_Screen!=1)
					{
						animateRight();
					}
				}

			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void clearColorFilter(ViewGroup parent,int imgIndex)
	{
		try
		{
			int index=-1;
			for(int i = 0; i < parent.getChildCount(); i++)
			{
				View child = parent.getChildAt(i);
				if(child instanceof ImageView)
				{
					index=index+1;
					if(index!=imgIndex)
					{
						((ImageView) child).getDrawable().clearColorFilter();
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public Boolean saveAnswers(ViewGroup parent)
	{
		String question="",answer="",GridSingleOptionsArray[]=new String[AnswerText.length];
		int QuetionType;int checkcount=0,checkBoxCount=0,rbCount=0; 
		int Quetionid = 0,AnswersrNo=0,QuetionScrenId=0,subQuetionCount=-1;
		int sessionQuetionId=0;
		int sessionQuetionScrenId=0;
		int sessionAnswerSrNo=0,l=0;
		int chkCount=0,cngGridQue=0;
		int repeatedImageStoreCount=0,registerSpinnerCount=0;
		String sessionAnswerText = null;
		Boolean flag=true,checkBoxFlag=true,rbFlag=true;

		try
		{
			for(int i = 0; i < parent.getChildCount(); i++)
			{
				View child = parent.getChildAt(i);
				if(child instanceof LinearLayout)
				{
					int count = ((ViewGroup) child).getChildCount();
					View v = null,child2;
					for(int k=0; k<count; k++)
					{
						v = ((ViewGroup) child).getChildAt(k);
						child2=v;
						if(child2 instanceof RadioButton)
						{
							RadioButton radioGroup = (RadioButton)child2;
							get_Answer(Quetionid);
							answer=radioGroup.getText().toString();
							if(answer.equalsIgnoreCase("-1"))
							{
								checkcount=checkcount+1;
							}
							else
							{
								String selected = answer;

								db=new DatabaseHelper(MainActivity.this);
								NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerText='"+selected+"'and QuestionID='"+Quetionid+"'");

								AnswersrNo = db.GetSingleValue("Select AnswerSrNo from CS_TemplateAnswer where AnswerText='"+selected+"' and QuestionID='"+Quetionid+"'");
								db.CloseDB();
								sessionAnswerSrNo=AnswersrNo;
								sessionAnswerText=selected;

								if(sessionQuetionId!=0 && sessionAnswerSrNo!=0 )
								{
									try
									{
										db=new DatabaseHelper(MainActivity.this);
										db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+sessionAnswerSrNo+"','"+sessionAnswerText+"','"+IMEI+"','','"+offlineCount+"')");
										db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+sessionAnswerText+"')");
										db.CloseDB();
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}

									AnswersrNo=0;
								}
							}
							//break;
						}

						else if(child2 instanceof TextView && strQuestionType.equalsIgnoreCase("GridSinglechoice"))
						{
							try
							{
								TextView GridSnglOption=(TextView)child2;

								if(GridSnglOption.getText().toString().equalsIgnoreCase(""))
								{
									//do nothing
								}
								else
								{
									if(GridSingleOptionsArray.length==AnswerText.length)
									{
										break;
									}
									else
									{
										GridSingleOptionsArray[l]=GridSnglOption.getText().toString();
										l++;
									}
								}
							}

							catch(Exception e)
							{
								e.printStackTrace();
							}

						}

						else if(child2 instanceof RatingBar)
						{
							final RatingBar rbRate=(RatingBar)child2;
							final int QueId=Quetionid;
							db=new DatabaseHelper(MainActivity.this);
							int answersrNo = db.GetSingleValue("Select AnswerSrNo from CS_TemplateAnswer where QuestionID='"+QueId+"'");
							NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerSrNo='"+answersrNo+"'and QuestionID='"+Quetionid+"'");
							db.CloseDB();
							String rateValue=String.valueOf(rbRate.getRating());
							try
							{
								db=new DatabaseHelper(MainActivity.this);
								db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+answersrNo+"','"+rateValue+"','"+IMEI+"','','"+offlineCount+"')");
								db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+rateValue+"')");
								db.CloseDB();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							break;
						}

						else if(child2 instanceof EditText)
						{

							emailchk=0;
							EditText edittextEmail=(EditText)child2;
							answer=edittextEmail.getText().toString();

							if(strQuestionType.equalsIgnoreCase("Email"))
							{
								String email = answer.trim();
								String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

								if (email.matches(emailPattern))
								{
									db=new DatabaseHelper(MainActivity.this);
									AnswersrNo=db.GetSingleValue("Select AnswerSrNo from CS_TemplateAnswer where QuestionID='"+Quetionid+"'");
									NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerSrNo='"+AnswersrNo+"'and QuestionID='"+Quetionid+"'");

									db.CloseDB();
									sessionAnswerSrNo=AnswersrNo;
									sessionAnswerText=email;

									if(sessionQuetionId!=0 && sessionAnswerSrNo!=0 )
									{
										try
										{
											db=new DatabaseHelper(MainActivity.this);
											db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+sessionAnswerSrNo+"','"+sessionAnswerText+"','"+IMEI+"','','"+offlineCount+"')");
											db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+sessionAnswerText+"')");
											db.CloseDB();
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										AnswersrNo=0;
									}

								}

								else 
									if(email.equalsIgnoreCase(""))
									{
										checkcount=checkcount+1;
									}
									else
									{

										emailchk=1;
										edittextEmail.setText("");
										flag=false;

									}
								break;
							}

							else
							{
								if(strQuestionType.equalsIgnoreCase("MobileNo"))
								{
									mobilechk=0;
									if(answer.equalsIgnoreCase(""))
									{
										checkcount=checkcount+1;
									}
									else
									{
										db=new DatabaseHelper(MainActivity.this);
										AnswersrNo=db.GetSingleValue("Select AnswerSrNo from CS_TemplateAnswer where QuestionID='"+Quetionid+"'");
										NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerSrNo='"+AnswersrNo+"'and QuestionID='"+Quetionid+"'");

										db.CloseDB();
										sessionAnswerText=answer;
										sessionAnswerSrNo=AnswersrNo;
										if(sessionAnswerText.length()<10)
										{
											mobilechk=1;
											edittextEmail.setText("");
											flag=false;
										}
										if(mobilechk!=1)
										{
											if(sessionQuetionId!=0 && sessionAnswerSrNo!=0 )
											{
												try
												{
													db=new DatabaseHelper(MainActivity.this);
													db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+sessionAnswerSrNo+"','"+sessionAnswerText+"','"+IMEI+"','','"+offlineCount+"')");
													db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+sessionAnswerText+"')");
													db.CloseDB();
												}
												catch(Exception e)
												{
													e.printStackTrace();
												}
												AnswersrNo=0;
											}
										}
									}
									break;
								}
								else		
									if(answer.equalsIgnoreCase(""))
									{
										checkcount=checkcount+1;
									}
									else if(answer.equalsIgnoreCase("'"))
									{
										singlecoatcount=singlecoatcount+1;
										checkcount=checkcount+1;
										edittextEmail.setText("");
									}
									else
									{
										db=new DatabaseHelper(MainActivity.this);
										AnswersrNo=db.GetSingleValue("Select AnswerSrNo from CS_TemplateAnswer where QuestionID='"+Quetionid+"'");
										NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerSrNo='"+AnswersrNo+"'and QuestionID='"+Quetionid+"'");

										db.CloseDB();
										sessionAnswerText=answer;
										sessionAnswerSrNo=AnswersrNo;

										if(sessionQuetionId!=0 && sessionAnswerSrNo!=0 )
										{
											try
											{
												db=new DatabaseHelper(MainActivity.this);
												db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+sessionAnswerSrNo+"','"+sessionAnswerText+"','"+IMEI+"','','"+offlineCount+"')");
												db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+sessionAnswerText+"')");
												db.CloseDB();
											}
											catch(Exception e)
											{
												e.printStackTrace();
											}
											AnswersrNo=0;
										}
									}
								break;
							}

						}
						else if(child2  instanceof TextView)
						{
							if(strQuestionType.equalsIgnoreCase("Date"))
							{
								TextView edittextDate=(TextView)child2;
								answer=edittextDate.getText().toString();

								if(answer.equalsIgnoreCase("Back"))
								{
									break;
								}
								if(answer.equalsIgnoreCase(""))
								{
									checkcount=checkcount+1;
								}
								else
								{
									db=new DatabaseHelper(MainActivity.this);
									AnswersrNo=db.GetSingleValue("Select AnswerSrNo from CS_TemplateAnswer where QuestionID='"+Quetionid+"'");
									NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerSrNo='"+AnswersrNo+"'and QuestionID='"+Quetionid+"'");

									db.CloseDB();
									sessionAnswerText=answer;
									sessionAnswerSrNo=AnswersrNo;

									if(sessionQuetionId!=0 && sessionAnswerSrNo!=0 )
									{
										try
										{
											db=new DatabaseHelper(MainActivity.this);
											db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+sessionAnswerSrNo+"','"+sessionAnswerText+"','"+IMEI+"','','"+offlineCount+"')");
											db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+sessionAnswerText+"')");
											db.CloseDB();
										}
										catch(Exception e)
										{
											e.printStackTrace();
										}
										AnswersrNo=0;
									}
								}
								break;
							}
						}
						else
							if(child2 instanceof LinearLayout)
							{
								try
								{
									int count2 = ((ViewGroup) child2).getChildCount();
									View v2 = null;
									for(int k2=0; k2<count2; k2++)
									{
										v2 = ((ViewGroup) child2).getChildAt(k2);
										View childCheck=v2;
										if(strQuestionType.equalsIgnoreCase("GridSinglechoice"))
										{
											if(childCheck instanceof RadioGroup)
											{

												String selected = "";
												sessionAnswerSrNo=0;
												subQuetionCount=subQuetionCount+1;

												RadioGroup radioGroup = (RadioGroup)childCheck;											
												get_Answer(Quetionid);

												for(int j=0;j<AnswerText.length;j++)
												{
													answer=String.valueOf(radioGroup.getCheckedRadioButtonId());
													if(answer.equalsIgnoreCase("-1"))
													{
														checkcount=checkcount+1;
													}
													else
													{
														View radioButton = radioGroup.findViewById(Integer.parseInt(answer));
														int radioId = radioGroup.indexOfChild(radioButton); 

														if(radioId==-1)
														{
															chkCount=chkCount+1;
														}
														if(radioId==j)
														{
															selected=AnswerText[j];
															String selectedAnswer=subQuetionText[cngGridQue]+"-"+selected;
															cngGridQue++;
															db=new DatabaseHelper(MainActivity.this);
															NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerText='"+selected+"'and QuestionID='"+Quetionid+"'");
															db.CloseDB();
															j=AnswerText.length;
															sessionAnswerSrNo=AnswersrNo;
															sessionAnswerText=selectedAnswer;
														}
														if(sessionQuetionId!=0 && selected!="")
														{
															try
															{
																db=new DatabaseHelper(MainActivity.this);
																db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+sessionAnswerSrNo+"','"+sessionAnswerText+"','"+IMEI+"','','"+offlineCount+"')");
																db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+selected+"')");
																db.CloseDB();
																selected="";
															}
															catch(Exception e)
															{
																e.printStackTrace();
															}
															sessionAnswerSrNo=0;

														}

													}

												}
											}

										}
										else
										{
											if(childCheck instanceof CheckBox)
											{
												checkBoxFlag=false;
												CheckBox cb = (CheckBox)childCheck;
												if (cb.isChecked())
												{
													checkBoxCount=checkBoxCount+1;
													checkBoxFlag=true;
													db=new DatabaseHelper(MainActivity.this);
													sessionAnswerText=cb.getText().toString();
													NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerText='"+sessionAnswerText.trim()+"' and QuestionID='"+Quetionid+"'");

													AnswersrNo = db.GetSingleValue("Select AnswerSrNo from CS_TemplateAnswer where AnswerText='"+sessionAnswerText.trim()+"' and QuestionID='"+Quetionid+"'");
													sessionAnswerSrNo=AnswersrNo;
													db.CloseDB();
													if(sessionQuetionId!=0 && sessionAnswerSrNo!=0 )
													{
														try
														{
															db=new DatabaseHelper(MainActivity.this);
															db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+sessionAnswerSrNo+"','"+sessionAnswerText.trim()+"','"+IMEI+"','','"+offlineCount+"')");
															db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+sessionAnswerText.trim()+"')");
															db.CloseDB();
														}
														catch(Exception e)
														{
															e.printStackTrace();
														}
														AnswersrNo=0;

													}
												}
											}
											else
												if(childCheck instanceof RadioButton)
												{
													rbFlag=false;
													sessionAnswerSrNo=0;
													subQuetionCount=subQuetionCount+1;

													RadioButton radioGroup = (RadioButton)childCheck;											
													if(radioGroup.isChecked())
													{
														answer=radioGroup.getText().toString();
														rbCount=rbCount+1;
														rbFlag=true;
													}
													else
													{
														answer="";
													}
													if(answer.equalsIgnoreCase(""))
													{
														//		checkcount=checkcount+1;
													}
													else
													{
														db=new DatabaseHelper(MainActivity.this);
														NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerText='"+answer+"'and QuestionID='"+Quetionid+"'");

														db.CloseDB();
														sessionAnswerSrNo=AnswersrNo;
														sessionAnswerText=answer;
														//	}
														if(sessionQuetionId!=0 && sessionAnswerText!="")
														{
															try
															{
																db=new DatabaseHelper(MainActivity.this);
																db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+sessionAnswerSrNo+"','"+sessionAnswerText+"','"+IMEI+"','','"+offlineCount+"')");
																db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+answer+"')");
																db.CloseDB();
															}
															catch(Exception e)
															{
																e.printStackTrace();
															}
															sessionAnswerSrNo=0;

														}

													}
												}
										}

										if(strQuestionType.equalsIgnoreCase("Singlechoice"))
										{
											if(rbCount<1 && rbFlag==false)
											{
												rbFlag=false;
												rbCount=0;
											}
											else
											{
												rbFlag=true;

											}
										}
										if(strQuestionType.equalsIgnoreCase("Gridmultichoice")||strQuestionType.equalsIgnoreCase("Multichoice"))
										{
											if(checkBoxCount<1 && checkBoxFlag==false)
											{
												checkBoxFlag=false;
												checkBoxCount=0;
											}
											else
											{
												checkBoxFlag=true;

											}
										}
									}
								}catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								break;
							}
					}
				}

				else if(child instanceof Spinner)
				{
					if(registerSpinnerCount==0)
					{
						Spinner selectionList=(Spinner)child;
						String SpinnerAnswer=selectionList.getSelectedItem().toString();
						sessionAnswerText=SpinnerAnswer;
						db=new DatabaseHelper(MainActivity.this);
						NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerText='"+SpinnerAnswer+"'and QuestionID='"+Quetionid+"'");
						AnswersrNo=db.GetSingleValue("Select AnswerSrNo from CS_TemplateAnswer where QuestionID='"+Quetionid+"'");
						db.CloseDB();
						if(sessionQuetionId!=0)
						{
							try
							{
								db=new DatabaseHelper(MainActivity.this);
								registerSpinnerCount++;
								db.InsertData("Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,AnswerText,DeviceIMEINumber,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+AnswersrNo+"','"+sessionAnswerText+"','"+IMEI+"','','"+offlineCount+"')");
								db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+sessionAnswerText+"')");
								db.CloseDB();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							AnswersrNo=0;
						}
					}
					//break;
				}
				else if(child instanceof ImageView)
				{
					if(repeatedImageStoreCount==0)
					{
						String bmpp="";
						if(selectedSmilyImage==null)
						{
							checkcount=checkcount+1;
						}
						else
						{
							if(sessionQuetionId!=0)
							{
								try
								{
									for(int j=0;j<imgArray.length;j++)
									{
										bmpp=imgArray[j];
										byte[] decodedString = Base64.decode(bmpp, Base64.DEFAULT);
										Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
										Boolean cFlag=compare(selectedSmilyImage,decodedByte);
										if(cFlag==true)
										{
											db=new DatabaseHelper(MainActivity.this);
											NextQuetionScreenId=db.GetSingleValue("Select NextQuestionScreenID from CS_TemplateAnswer where AnswerImage='"+bmpp+"' and QuestionID='"+Quetionid+"'");
											AnswersrNo=db.GetSingleValue("Select AnswerSrNo from CS_TemplateAnswer where AnswerImage='"+bmpp+"' and QuestionID='"+Quetionid+"'");
											repeatedImageStoreCount++;
											db.CloseDB();
											db=new DatabaseHelper(MainActivity.this);
											String str="Insert Into CS_Session(GUID,UserID,QuestionScreenID,QuestionID,AnswerSrNo,DeviceIMEINumber,AnswerText,ImageAns,OfflineFlag) Values('"+uniqueID+"','"+Userid+"','"+sessionQuetionScrenId+"','"+sessionQuetionId+"','"+AnswersrNo+"','"+IMEI+"','','"+bmpp+"','"+offlineCount+"')";
											db.InsertData("Insert Into CS_BackSession(QuestionID, AnswerText) Values('"+sessionQuetionId+"','"+bmpp+"')");
											db.InsertData(str);
											db.CloseDB();

										}
									}

								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
								AnswersrNo=0;
							}
						}
					}
					//break;
				}

				else
					if(child instanceof TextView)
					{
						TextView tvQuetionName = (TextView)child;
						question=tvQuetionName.getText().toString();
						db=new DatabaseHelper(MainActivity.this);
						QuetionType=db.GetSingleValue("Select QuestionTypeID from CS_TemplateQuestion where QuestionText='"+question+"'");
						strQuestionType=get_QuestionType(QuetionType);
						Quetionid=db.GetSingleValue("Select QuestionID from CS_TemplateQuestion where QuestionText='"+question+"'");
						QuetionScrenId=db.GetSingleValue("Select QuestionScreenID from CS_TemplateQuestion where QuestionText='"+question+"'");
						db.CloseDB();
						for(int s=0;s<Question.length;s++)
						{
							if(BackQuetionScreen[s]==0)
							{
								BackQuetionScreen[s]=No_Of_Screen;
								break;
							}
						}
					}
				if(strQuestionType.equalsIgnoreCase("GridMultichoice")||strQuestionType.equalsIgnoreCase("Multichoice"))
				{
					if(checkBoxFlag==true)
					{
						checkBoxCount=0;
					}
					else
					{
						checkcount=checkcount+1;
					}
				}
				if(strQuestionType.equalsIgnoreCase("Singlechoice"))
				{
					if(rbFlag==true)
					{
						rbCount=0;
					}
					else
					{
						checkcount=checkcount+1;
					}
				}
				sessionQuetionId=Quetionid;
				sessionQuetionScrenId=QuetionScrenId;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(checkBoxFlag==false && strQuestionType.equalsIgnoreCase("Multichoice"))
		{
			checkcount=checkcount+1;
		}
		if(rbFlag==false && strQuestionType.equalsIgnoreCase("Singlechoice"))
		{
			checkcount=checkcount+1;
		}
		if(checkcount>0)
		{
			flag=false;
		}
		return flag;

	}
	public void setCurntQueNo(int l)
	{
		k=l;
	}
	public void RemoveView()
	{
		contrlAddlayout.removeAllViews();
		addcontrlAddlayout.removeAllViews();
		formLayout.removeView(addcontrlAddlayout);
	}

	public String[] get_AnswerArray(int QuestionId)
	{

		try
		{
			db=new DatabaseHelper(MainActivity.this);

			String strQuery="Select AnswerImage from CS_TemplateAnswer where QuestionID='"+QuestionId+"'";
			Cursor cur=db.getData(strQuery);
			int cnt=cur.getCount();

			answerArray=new String[cnt];

			if(cur.getCount()>0)
			{
				for(int s=0;s<cnt;s++)
				{
					cur.moveToNext();
					answerArray[s]=cur.getString(0);
				}
			}
			db.CloseDB();
			cur.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return answerArray;
	}


	public void get_Answer(int QuestionId)
	{
		try
		{
			db=new DatabaseHelper(MainActivity.this);

			String strQuery="Select AnswerText from CS_TemplateAnswer where QuestionID='"+QuestionId+"'";
			Cursor cur=db.getData(strQuery);
			int cnt=cur.getCount();
			AnswerText=new String[cnt];
			if(cur.getCount()>0)
			{
				for(int i=0;i<cnt;i++)
				{
					cur.moveToNext();
					AnswerText[i]=cur.getString(0).toString();
				}
				db.CloseDB();
				cur.close();
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void get_SubQuetion(int QuestionId)
	{
		try
		{
			db=new DatabaseHelper(MainActivity.this);

			String strQuery="Select AnswerText from CS_SubQuestion where QuestionID='"+QuestionId+"'";
			Cursor cur=db.getData(strQuery);
			int cnt=cur.getCount();
			subQuetionText=new String[cnt];
			if(cur.getCount()>0)
			{
				for(int i=0;i<cnt;i++)
				{
					cur.moveToNext();
					subQuetionText[i]=cur.getString(0).toString();
				}
				db.CloseDB();
				cur.close();
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public String get_QuestionType(int QuestionTypeId)
	{
		try
		{
			db=new DatabaseHelper(MainActivity.this);
			strQuestionType=db.GetSingleText("Select QuestionTypeName from CS_QuestionType where QuestionTypeID='"+QuestionTypeId+"'");
			db.CloseDB();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strQuestionType;
	}

	public void get_TemplateData()
	{
		try
		{
			db=new DatabaseHelper(MainActivity.this);

			String strQuery="Select QuestionID,QuestionTypeID,QuestionText,QuestionScreenID from CS_TemplateQuestion";
			Cursor cur=db.getData(strQuery);
			int cnt=cur.getCount();
			QuestionId=new int[cnt];
			QuestionTypeId=new int[cnt];
			QuestionscreenId=new int[cnt];
			Question=new String[cnt];

			if(cur.getCount()>0)
			{
				for(int i=0;i<cnt;i++)
				{
					cur.moveToNext();
					QuestionId[i]=Integer.parseInt(cur.getString(0).toString());
					QuestionTypeId[i]=Integer.parseInt(cur.getString(1).toString());
					QuestionscreenId[i]=Integer.parseInt(cur.getString(3).toString());
					Question[i]=cur.getString(2).toString();
				}
			}
			db.CloseDB();
			cur.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void get_NoOfScreens()
	{
		try
		{
			db=new DatabaseHelper(MainActivity.this);
			Eva=db.GetSingleValue("Select NoOfScreens from CS_Template");
			db.CloseDB();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void getCompanyName()
	{
		try
		{
			db=new DatabaseHelper(MainActivity.this);

			String strQuery="Select UserID,Companyname from CS_Users";
			Cursor cur=db.getData(strQuery);
			int cnt=cur.getCount();
			if(cur.getCount()>0)
			{
				for(int i=0;i<cnt;i++)
				{
					cur.moveToFirst();
					Userid=Integer.parseInt(cur.getString(0).toString());
					strCompanyName=cur.getString(1).toString();
				}
				cur.close();
				db.CloseDB();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Something get Wrong", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public class SoapAccessTask_SaveFeedback extends AsyncTask<String, Void, SoapObject > 
	{
		SoapObject response=null;
		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute()
		{
			if(fromOffline==true)
			{

			}
			else
			{
				progressDialog = ProgressDialog.show(MainActivity.this, "", "");
				progressDialog.setContentView(R.layout.progress_main);
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

		}
		@SuppressLint("SimpleDateFormat")

		@Override
		protected SoapObject  doInBackground(String... urls) 
		{
			try
			{
				String Query= " SET @Date=CAST(CURRENT_DATE() AS CHAR(50)); "; 
				Query= Query+ " SET @Time=CAST(CURRENT_TIME() AS CHAR(50)); ";

				Query= Query+ " Select IfNull( Max(VoucherNo),0) VoucherNo Into @VoucherNo From cs_logmaster; ";
				Query= Query+ " Set @VoucherNo=@VoucherNo+1; ";


				db=new DatabaseHelper(MainActivity.this);
				int qid,qidNext,AnswerSrNo;
				String answertext,answerTextNext,strSQL;

				if(fromOffline==true)
				{
					strSQL="Select QuestionID,AnswerText,AnswerSrNo from CS_Session where OfflineFlag=='"+offlineCount+"'";
				}
				else
				{
					strSQL="Select QuestionID,AnswerText,AnswerSrNo from CS_Session where GUID='"+uniqueID+"' and OfflineFlag=='"+0+"'";
				}

				Cursor cur=db.getData(strSQL);
				int cnt=cur.getCount();
				if(cur.getCount()>0)
				{
					for(int i=0;i<cnt;i++)
					{
						cur.moveToNext();
						qid=Integer.parseInt(cur.getString(0).toString());
						answertext=cur.getString(1).toString();
						AnswerSrNo=Integer.parseInt(cur.getString(2).toString());
						for(int j=i;j<cnt;j++)
						{
							try
							{
								cur.moveToNext();
								qidNext=Integer.parseInt(cur.getString(0).toString());
								answerTextNext=cur.getString(1).toString();
								if(qid==qidNext)
								{
									answertext=answertext+","+answerTextNext;
									i++;
								}
								else
								{
									cur.moveToPrevious();
								}

							}
							catch (Exception e) 
							{

								cur.moveToPrevious();
							}
						}
						try
						{
							Query= Query+ " Insert Into cs_logdetail(VoucherNo,QuestionID,AnswerText,ansid) Values(@VoucherNo,'"+qid+"','"+answertext+"','"+AnswerSrNo+"'); ";
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					cur.close();
					db.CloseDB();
				}

				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date = df.format(c.getTime());
				Time=Date.substring(11);

				Query= Query + " Insert Into cs_logmaster(VoucherNo,GUID,UserID,DeviceIMEINo,SurveyDate,SurveyTime) Values(@VoucherNo,'"+uniqueID+"','"+Userid+"','"+IMEI+"','"+Date+"','"+Time+"'); ";
				if(fromOffline==true)
				{
					offlineCount=offlineCount-1;
				}
				manager=new AsyncManager();
				response= manager.insertRecords(MainActivity.this, "ajit", "ajit99",Query);

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
				if(fromOffline!=true)
				{
					progressDialog.dismiss();
				}

				String resultmsg=null;
				resultmsg=result.toString();
				if(resultmsg.equals("insertDataResponse{return=success; }"))
				{
					db=new DatabaseHelper(MainActivity.this);

					if(fromOffline==true)
					{
						//do nothing
					}
					else
					{
						Intent i = new Intent(MainActivity.this,ThankYou.class);
						startActivity(i);
						finish();

						db.DeleteData("Delete from CS_Session where OfflineFlag=='"+0+"'");
						int SurveyTmpId=db.GetSingleValue("Select SurveyTemplateID from CS_Template where CustID='"+Userid+"'");
						strThanku=db.GetSingleText("Select ThankyouText from CS_Template where CustID='"+Userid+"' and SurveyTemplateID='"+SurveyTmpId+"'");
						db.CloseDB();
					}

				}
				else
				{
					Toast.makeText(getApplicationContext(),"Records not Successfully Uploaded!!",Toast.LENGTH_LONG).show();
				}

				if(offlineCount==0)
				{
					db=new DatabaseHelper(MainActivity.this);
					db.DeleteData("Delete from CS_OfflineCount where OfflineFlag!='"+0+"'");
					db.DeleteData("Delete from CS_Session where OfflineFlag!='"+0+"'");
					db.CloseDB();
					showCount=0;
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}

		}
	}

	private String getShowImage(int Qid)
	{
		try
		{
			db=new DatabaseHelper(MainActivity.this);
			String strQuery="Select ShowImage from CS_TemplateQuestion where QuestionID=='"+Qid+"'";
			Cursor cur=db.getData(strQuery);
			int cnt=cur.getCount();
			if(cur.getCount()>0)
			{
				for(int l=0;l<cnt;l++)
				{
					cur.moveToFirst();
					showImage=cur.getString(0).toString();
				}
				cur.close();
				db.CloseDB();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return showImage;
	}

	private void chkBackQueText(int queId)
	{
		try
		{
			db=new DatabaseHelper(MainActivity.this);
			String strQuery="Select AnswerText from CS_BackSession where QuestionID=='"+queId+"'";
			Cursor cur=db.getData(strQuery);
			int cnt=cur.getCount();
			backQueAnswerArray=new String[cnt];
			if(cur.getCount()>0)
			{
				for(int l=0;l<cnt;l++)
				{
					cur.moveToNext();
					backQueAnswerArray[l]=cur.getString(0).toString();
				}
				cur.close();
				db.CloseDB();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private void chkBackQuetionText(int queId)
	{
		try
		{
			db=new DatabaseHelper(MainActivity.this);
			String strQuery="Select AnswerText from CS_BackSession where QuestionID=='"+queId+"'";
			Cursor cur=db.getData(strQuery);
			int cnt=cur.getCount();
			if(cur.getCount()>0)
			{
				for(int l=0;l<cnt;l++)
				{
					cur.moveToFirst();
					backQuetionAnswerText=cur.getString(0).toString();
				}
				cur.close();
				db.CloseDB();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static boolean compare(Bitmap b1, Bitmap b2) 
	{

		if (b1.getWidth() == b2.getWidth() && b1.getHeight() == b2.getHeight())
		{

			int[] pixels1 = new int[b1.getWidth() * b1.getHeight()];
			int[] pixels2 = new int[b2.getWidth() * b2.getHeight()];
			b1.getPixels(pixels1, 0, b1.getWidth(), 0, 0, b1.getWidth(), b1.getHeight());
			b2.getPixels(pixels2, 0, b2.getWidth(), 0, 0, b2.getWidth(), b2.getHeight());
			if (Arrays.equals(pixels1, pixels2))
			{
				return true;
			}
			else 
			{
				return false;
			}
		} 
		else
		{
			return false;
		}


	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
		case R.id.action_settings:
			Intent intent=new Intent(MainActivity.this,Settings.class);
			startActivity(intent);
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}


	public class MyAdapter extends ArrayAdapter<String> 
	{
		int Qid;
		public MyAdapter(Context ctx, int txtViewResourceId, String[] objects, int questionId)
		{
			super(ctx, txtViewResourceId, objects);
			Qid=questionId;
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


			chkBackQuetionText(Qid);
			if(backQuetionAnswerText!=null)
			{
				try
				{
					for(int l=0;l<SelectionListAnswerArray.length;l++)
					{
						if(backQuetionAnswerText.equalsIgnoreCase(SelectionListAnswerArray[l]))
						{
							main_text.setText(SelectionListAnswerArray[l]);
							try
							{
								String bmp="";
								bmp=SelectionListAnswerIMage[l];
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
							db=new DatabaseHelper(MainActivity.this);
							db.DeleteData("Delete from CS_BackSession where QuestionID=='"+Qid+"'");
							backQuetionAnswerText=null;
							db.CloseDB();
						}
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				main_text.setText(SelectionListAnswerArray[position]);

				try
				{
					String bmp="";
					bmp=SelectionListAnswerIMage[position];
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


	void downloadFile(String fileUrl)
	{
		URL myFileUrl =null;          
		try 
		{
			myFileUrl= new URL(fileUrl);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		try 
		{
			HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();

			bmImg = BitmapFactory.decodeStream(is);
			bmImg.getHeight();
			isImageFound=true;
		} 
		catch (IOException e) 
		{
			isImageFound=false;
			e.printStackTrace();
		}
	}

	public void animateLeft(){
		Animation animation = null ;
		animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_left);

		animation.setDuration(1000);
		MainLayout.setAnimation(animation);
		MainLayout.animate();
		animation.start();
	}
	public void animateRight(){
		Animation animation = null ;
		animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_right);

		animation.setDuration(1000);
		MainLayout.setAnimation(animation);
		MainLayout.animate();
		animation.start();
	}
	public static Bitmap getCircularBitmap(String strQueType,Bitmap bitmap, int borderWidth) 
	{
		if (bitmap == null || bitmap.isRecycled()) 
		{
			return null;
		}
		Bitmap resizedBitmap;
		if(strQueType.equalsIgnoreCase("GridMultichoice"))
		{
			resizedBitmap = getResizedBitmap(bitmap, 60, 60);
		}
		else
		{
			resizedBitmap = getResizedBitmap(bitmap, 100, 100);
		}
		int width = resizedBitmap.getWidth() + borderWidth;
		int height = resizedBitmap.getHeight() + borderWidth;

		Bitmap canvasBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
		BitmapShader shader = new BitmapShader(resizedBitmap, TileMode.CLAMP,  TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(shader);

		Canvas canvas = new Canvas(canvasBitmap);
		float radius = width > height ? ((float) height) / 2f: ((float) width) / 2f;
		canvas.drawCircle(width / 2, height / 2, radius, paint);
		paint.setShader(null);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(borderWidth);
		canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2,  paint);
		return canvasBitmap;
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
	{
		int     width           = bm.getWidth();
		int     height          = bm.getHeight();
		float   scaleWidth      = ((float) newWidth) / width;
		float   scaleHeight     = ((float) newHeight) / height;

		Matrix matrix = new Matrix();	
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return resizedBitmap;
	}

	@Override
	public void onBackPressed()
	{
		CustomAlertDialog cdd=new CustomAlertDialog(MainActivity.this);
		cdd.show();  
	}
	

	public void sendRegistration(){
		
		try{
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);


		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM			
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.				
				//				Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, Activation.strUserName, Activation.strname, regId,Activation.IMEINo);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}
				};
				mRegisterTask.execute(null, null, null);
			}
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */

			//			String performAction="notification_alert";
			//			CustomDialogClass customdialogclass = new CustomDialogClass(MainActivity.this,"GJF", "" +GCMIntentService.msg,R.drawable.ic_launcher, performAction,"");
			//			customdialogclass.show();
			//			CustomDialogClass.hideCancelButton();

			//			Intent intent1=new Intent(MainActivity.this, MainActivity.class);
			//			intent1.putExtra("notification_message", GCMIntentService.msg);
			//			startActivity(intent1);
			//			finish();



			//			// Showing received message
			//			//			lblMessage.append(newMessage + "\n");			
						Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
	
}