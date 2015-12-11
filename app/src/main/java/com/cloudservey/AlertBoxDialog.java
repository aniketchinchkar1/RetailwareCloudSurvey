package com.cloudservey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.ksoap2.serialization.SoapObject;

import com.asyncmanager.AsyncManager;
import com.cloudservey.MainActivity.SoapAccessTask_SaveFeedback;
import com.databasehelper.DatabaseHelper;
import com.utility.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AlertBoxDialog extends Dialog implements android.view.View.OnClickListener 
{
	//public static Button mButtonCancel;
	public Activity mActivity;
	public Dialog dialog;
	public Button mButtonOk;
	public ImageView mDialogImage;
	public TextView mTextdialogMessage, mTextdialogTilte;
	public static Boolean fromAlert=false;
	public int dialogicon;
	public int offlineCount,oflcnt;
	public String message,uniqueID;
	public String dialogTitle;
	public String performAction;
	DatabaseHelper dbHelper;
	AsyncManager manager;

	public AlertBoxDialog(Activity activity,String Msg,int offlineCount) 
	{
		super(activity);
		this.mActivity = activity;
		this.message = Msg;
		this.offlineCount=offlineCount;
		dialogTitle = "Alert";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.cloudservey.R.layout.activity_alert_box_dialog);

		mButtonOk = (Button) findViewById(R.id.adialogOkButton);
		mTextdialogMessage = (TextView) findViewById(R.id.adialog_msg);
		mTextdialogTilte = (TextView) findViewById(R.id.atitle_msg);

		mTextdialogMessage.setText(message.toString());
		mTextdialogTilte.setText(dialogTitle.toString());
		manager=new AsyncManager();

		mButtonOk.setOnClickListener(this);
		setCanceledOnTouchOutside(false);
		uniqueID = UUID.randomUUID().toString(); 
	}

	public class SoapAccessTask_SaveFeedback extends AsyncTask<String, Void, SoapObject > 
	{
		SoapObject response=null;
		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute()
		{

			progressDialog = ProgressDialog.show(mActivity, "", "");
			progressDialog.setContentView(R.layout.progress_main);
			progressDialog.setCancelable(false);
			progressDialog.show();

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


				DatabaseHelper db=new DatabaseHelper(mActivity);
				int qid,qidNext,AnswerSrNo;
				String answertext,answerTextNext,strSQL;

				strSQL="Select QuestionID,AnswerText,AnswerSrNo from CS_Session where OfflineFlag=='"+oflcnt+"'";

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
				String Date = df.format(c.getTime());
				String Time=Date.substring(11);

				Query= Query + " Insert Into cs_logmaster(VoucherNo,GUID,UserID,DeviceIMEINo,SurveyDate,SurveyTime) Values(@VoucherNo,'"+uniqueID+"','"+SplashActivity.Userid+"','"+SplashActivity.IMEI+"','"+Date+"','"+Time+"'); ";

				oflcnt=oflcnt-1;

				manager=new AsyncManager();
				response= manager.insertRecords(mActivity, "ajit", "ajit99",Query);

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
				progressDialog.dismiss();

				String resultmsg=null;
				resultmsg=result.toString();
				if(resultmsg.equals("insertDataResponse{return=success; }"))
				{
					if(oflcnt==0)
					{
						DatabaseHelper db=new DatabaseHelper(mActivity);
						db.DeleteData("Delete from CS_OfflineCount where OfflineFlag!='"+0+"'");
						db.DeleteData("Delete from CS_Session where OfflineFlag!='"+0+"'");
						Toast.makeText(mActivity,"Records successfully uploaded!!",Toast.LENGTH_LONG).show();
						db.CloseDB();

					}
				}
				else
				{
					Toast.makeText(mActivity,"Records not successfully uploaded!!",Toast.LENGTH_LONG).show();
				}

				mActivity.finish();
				dismiss();
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.adialogOkButton:
			if(Settings.frmSettingflag==true)
			{
				try
				{
					DatabaseHelper db=new DatabaseHelper(mActivity);
					String strQuery="Select OfflineFlag from CS_OfflineCount";
					Cursor cur=db.getData(strQuery);
					int cnt=cur.getCount();

					if(cur.getCount()>0)
					{
						if(Utility.isNetworkAvailable(mActivity))
						{
							for(int l=0;l<cnt;l++)
							{
								cur.moveToNext();
								oflcnt=Integer.parseInt(cur.getString(0).toString());
								try
								{  	
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
				//mActivity.finish();
				//dismiss();
			}
			else
				if(SplashActivity.deviceIsActive==0)
				{
					if(offlineCount>0)
					{
						//go to settings class
						fromAlert=true;
						Settings.offlinExpiredflag=true;
						mActivity.finish();
						Intent myIntent = new Intent(mActivity, Settings.class);
						mActivity.startActivity(myIntent); 
						dismiss();
					}
					else
					{
						mActivity.finish();
						break;
					}
				}

				else
				{
					mActivity.finish();
					break;
				}

		default:
			break;
		}
		dismiss();
	}
}
