package com.cloudservey;

import com.databasehelper.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ThankYou extends Activity {

	TextView tvThankuMsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_thank_you);

		tvThankuMsg=(TextView)findViewById(R.id.tvThankuMsg);
		
		tvThankuMsg.setText(MainActivity.strThanku);
		final int thankuScreenDisplay = 4000;

		Thread thankuThread = new Thread() 
		{
			int wait = 0;
			public void run() 
			{ 
				try 
				{
					super.run();

					while (wait < thankuScreenDisplay)
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
					DatabaseHelper db=new DatabaseHelper(ThankYou.this);
					String dltStr;
					dltStr="DELETE FROM CS_BackSession";
					db.DeleteData(dltStr);

					db.CloseDB();
					startActivity(new Intent(ThankYou.this,MainActivity.class));
					finish();
				}
			}
		};
		thankuThread.start();
	}
}
