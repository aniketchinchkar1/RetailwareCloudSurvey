package com.cloudservey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Temp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp);
		
		Intent intent= getIntent();
		String notification_message =intent.getStringExtra("notification_message");
		
		Intent mainintent=new Intent(Temp.this,MainActivity.class);
		mainintent.putExtra("notification_message", notification_message);
		startActivity(mainintent);
	}
}
