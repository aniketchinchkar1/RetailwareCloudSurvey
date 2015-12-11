package com.cloudservey;

import com.cloudservey.SplashActivity.SoapAsyncDownloadUserInfo;
import com.databasehelper.DatabaseHelper;
import com.utility.Utility;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Settings extends Activity 
{
	EditText edImeiNo,edCreationDate,edExpiryDate;
	TextView tvCreationDate,tvExpiryDate;
	DatabaseHelper db;
	//Button UploadData;
	int wait=2000;
	String strCreationDate,strExpiryDate;
	int flagchk;
	public static Boolean frmSettingflag=false,offlinExpiredflag=false;;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		edImeiNo=(EditText)findViewById(R.id.txtImeiNo);
		edImeiNo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		
		edCreationDate=(EditText)findViewById(R.id.txtCreationDate);
		edCreationDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		
		edExpiryDate=(EditText)findViewById(R.id.txtExpiryDate);
		edExpiryDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		
		tvCreationDate=(TextView)findViewById(R.id.lblCreationDate);
		tvExpiryDate=(TextView)findViewById(R.id.lblExpiryDate);

		//UploadData=(Button)findViewById(R.id.btnUploadData);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		getActionBar().setTitle("Settings");
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#31688C")));

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

		if(AlertBoxDialog.fromAlert==true)
		{
			while(wait!=4000)
			{
				waitfr();
			}
			frmSettingflag=true;
			String msg="You have "+SplashActivity.offlintCnt+" records to upload...click on ok to upload records";
			AlertBoxDialog cdd=new AlertBoxDialog(Settings.this,msg,SplashActivity.offlintCnt);
			cdd.show();  
		}
		if(Utility.isNetworkAvailable(Settings.this))
		{

			strCreationDate=SplashActivity.Creation_Date;
			strExpiryDate=SplashActivity.Expiry_Date;
			try
			{
				if(strCreationDate==null || strExpiryDate==null)
				{
					db=new DatabaseHelper(Settings.this);
					String strQuery="Select CreationDate,Expiry_Date from CS_Users where UserID=='"+Activation.UserId+"'";
					Cursor cur=db.getData(strQuery);

					int cnt=cur.getCount();
					if(cur.getCount()>0)
					{
						for(int l=0;l<cnt;l++)
						{
							cur.moveToFirst();
							strCreationDate=cur.getString(0).toString();
							strExpiryDate=cur.getString(1).toString();
						}
						cur.close();
						db.CloseDB();
					}
					db.CloseDB();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			db=new DatabaseHelper(Settings.this);
			String strQuery="Select CreationDate,Expiry_Date from CS_Users where UserID=='"+MainActivity.Userid+"'";
			Cursor cur=db.getData(strQuery);

			int cnt=cur.getCount();
			if(cur.getCount()>0)
			{
				for(int l=0;l<cnt;l++)
				{
					cur.moveToFirst();
					strCreationDate=cur.getString(0).toString();
					strExpiryDate=cur.getString(1).toString();
				}
				cur.close();
				db.CloseDB();
			}
			db.CloseDB();
		}

		if(Activation.fromact==true)
		{
			edCreationDate.setVisibility(View.GONE);
			edExpiryDate.setVisibility(View.GONE);
			tvExpiryDate.setVisibility(View.GONE);
			tvCreationDate.setVisibility(View.GONE);

			TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Service.TELEPHONY_SERVICE);
			telephonyManager.getDeviceId();

			String imeiNo=telephonyManager.getDeviceId().toString();
			edImeiNo.setText(imeiNo);

			edImeiNo.setEnabled(false);
			flagchk=1;

		}
		else
		{
			TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Service.TELEPHONY_SERVICE);
			telephonyManager.getDeviceId();

			String imeiNo=telephonyManager.getDeviceId().toString();

			edImeiNo.setText(imeiNo);
			edCreationDate.setText(strCreationDate);
			edExpiryDate.setText(strExpiryDate);

			edImeiNo.setClickable(false);
			edImeiNo.setEnabled(false);
			edCreationDate.setEnabled(false);
			edExpiryDate.setEnabled(false);
			flagchk=2;
		}
	}
	public void waitfr()
	{

		while (wait < 4000)
		{
			wait += 200;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			if(Activation.fromact==true)
			{
				Activation.fromact=false;
				Intent intent=new Intent(Settings.this,Activation.class);
				startActivity(intent);
				finish();
			}
			else
			{
				Intent intent=new Intent(Settings.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(offlinExpiredflag==true)
		{
			finish();
		}
		else
		{
			if(flagchk==1)
			{
				Intent i=new Intent(Settings.this,Activation.class);
				startActivity(i);
				finish();
			}
			else
			{
				if(flagchk==2)
				{
					Intent i=new Intent(Settings.this,MainActivity.class);
					startActivity(i);
					finish();
				}
			}
		}
	}
}
