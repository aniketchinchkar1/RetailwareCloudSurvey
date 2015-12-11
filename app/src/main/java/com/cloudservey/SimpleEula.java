package com.cloudservey;
import com.databasehelper.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

public class SimpleEula
{
	
	private String EULA_PREFIX = "eula_";
	private Activity mActivity;
	//	TallyLoginInterface tli = null;
	DatabaseHelper dbHelper;

	public SimpleEula(Activity context) {
		mActivity = context; 
	}

	private PackageInfo getPackageInfo() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageInfo; 
	}
	
	public void show() 
	{
	
		PackageInfo versionInfo = getPackageInfo();

		// the eulaKey changes every time you increment the version number in the AndroidManifest.xml
		final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		boolean hasBeenShown = prefs.getBoolean(eulaKey, false);
		if(hasBeenShown == false)
		{
			// Show the Eula
			String title = mActivity.getString(R.string.app_name) + " v" + versionInfo.versionName;

			//Includes the updates as well so users know what changed. 
			String message = mActivity.getString(R.string.updates) + "\n\n" + mActivity.getString(R.string.eula);
	
			try
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton("Accept", new Dialog.OnClickListener() 
				{
					//private String listType;

					public void onClick(DialogInterface dialogInterface, int i)
					{
						// Mark this version as read.

						dbHelper= new DatabaseHelper(mActivity);
						dbHelper.UpdateEulaAccepted("1");
						dbHelper.CloseDB();

						SharedPreferences.Editor editor = prefs.edit();
						editor.putBoolean(eulaKey, true);
						editor.commit();
						dialogInterface.dismiss();

						/* tli=new TallyLoginInterface();
                         	tli.SetLoginModeOnline(true);
                         	Intent intent = new Intent(mActivity, tli.getClass()); 
                         	mActivity.startActivityForResult(intent, 1);*/

						//   UseLoginLib.TallyLogin(mActivity,1);

						//dbHelper= new DatabaseHelper(mActivity);
						//SplashActivity.EulaAcceptedBy=Integer.parseInt(dbHelper.GetOptionsAll());
						//dbHelper.CloseDB();

						if(SplashActivity.EulaAcceptedBy==0)
						{
							Intent intent = new Intent(mActivity, Activation.class);
							mActivity.startActivity(intent);
							mActivity.finish();
						}
						else
						{
							Intent intent = new Intent(mActivity, MainActivity.class);
							mActivity.startActivity(intent);
							mActivity.finish();
						}
					}
				})
				.setNegativeButton("Decline", new Dialog.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						mActivity.finish(); 
					}
				});
				 AlertDialog alert=builder.create();
				 alert.show();
				alert.setCanceledOnTouchOutside(false);
				alert.setCancelable(false);
		
			}
			catch (Exception e) 
			{
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		else
		{

		}
	}
}
