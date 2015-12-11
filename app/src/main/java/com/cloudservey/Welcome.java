package com.cloudservey;

import com.databasehelper.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome extends Activity {

	DatabaseHelper db;
	Bitmap bitmapbmp=null;
	String strWelcomeText;
	TextView tvwelcometxt;
	String strTemplateName="";
	int Useridd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		if(Activation.fromActivation==true)
		{
			Useridd=Activation.UserId;
		}
		else
		{
			Useridd=SplashActivity.Userid;
		}
		tvwelcometxt=(TextView)findViewById(R.id.tvWelcomeMsg);
		db=new DatabaseHelper(Welcome.this);
		strTemplateName=db.GetSingleText("SELECT TemplateName FROM CS_Template where CustId='"+Useridd+"'");
		String strMainLogo=db.GetSingleText("SELECT MainLogo FROM CS_Template where CustId='"+Useridd+"'");
		if(strTemplateName==null)
		{
			Toast.makeText(Welcome.this, "No template is downloaded...try again later", Toast.LENGTH_LONG).show();
			finish();
		}
		else
		{
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
			try
			{
				if(strTemplateName!=null)
				{
					getActionBar().setTitle(strTemplateName);
				}

			}
			catch(Exception e)
			{
				e.printStackTrace();
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

			db=new DatabaseHelper(Welcome.this);
			int SurveyTmpId=db.GetSingleValue("Select SurveyTemplateID from CS_Template where CustID='"+Useridd+"'");
			strWelcomeText=db.GetSingleText("Select HeadingText from CS_Template where CustID='"+Useridd+"' and SurveyTemplateID='"+SurveyTmpId+"'");
			try
			{
				tvwelcometxt.setText(strWelcomeText);

			}catch(Exception e)
			{
				e.printStackTrace();
			}
			db.CloseDB();

			final int welcomeScreenDisplay = 2500;

			Thread welcomeThread = new Thread() 
			{
				int wait = 0;
				public void run() 
				{ 
					try 
					{
						super.run();

						while (wait < welcomeScreenDisplay)
						{
							sleep(100);
							wait += 100;
						}
					} 
					catch (Exception e)
					{
						System.out.println("EXc=" + e);
					}
					finally 
					{
						startActivity(new Intent(Welcome.this,MainActivity.class));
						finish();
					}
				}
			};
			welcomeThread.start();
		}
		
	}

	/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId())
		{
		case R.id.action_settings:
			Intent intent=new Intent(Welcome.this,Setting.class);
			startActivity(intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}*/
}
