package com.cloudservey;

import com.asyncmanager.AsyncManager;
import com.databasehelper.DatabaseHelper;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAlertDialog extends Dialog implements android.view.View.OnClickListener
{
	public static Button mButtonCancel;
	public Activity mActivity;
	public Dialog dialog;
	public Button mButtonOk;
	public ImageView mDialogImage;
	public TextView mTextdialogMessage, mTextdialogTilte;

	public int dialogicon;
	public String message;
	public String dialogTitle;
	public String performAction;
	DatabaseHelper dbHelper;
	AsyncManager manager;

	public CustomAlertDialog(Activity activity) 
	{
		super(activity);
		this.mActivity = activity;
		this.message = "Do you want to exit?";
		dialogTitle = "Exit";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.cloudservey.R.layout.activity_custom_alert_dialog);

		mButtonOk = (Button) findViewById(R.id.dialogOkButton);
		mButtonCancel = (Button) findViewById(R.id.dialogCancelButton);
		mTextdialogMessage = (TextView) findViewById(R.id.dialog_msg);
		mTextdialogTilte = (TextView) findViewById(R.id.title_msg);

		mTextdialogMessage.setText(message.toString());
		mTextdialogTilte.setText(dialogTitle.toString());
		manager=new AsyncManager();

		mButtonOk.setOnClickListener(this);
		mButtonCancel.setOnClickListener(this);

		setCanceledOnTouchOutside(false);

		/*if(performAction.equalsIgnoreCase("login failed")){
			mButtonOk.setText("OK");
		}*/
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialogOkButton:
			mActivity.finish();
			break;
		case R.id.dialogCancelButton:
			dismiss();
			break;
		default:
			break;
		}
		dismiss();
	}

}
