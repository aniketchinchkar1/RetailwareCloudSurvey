
package com.asyncmanager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.cloudservey.SplashActivity;
import com.databasehelper.DatabaseHelper;

@SuppressLint("SimpleDateFormat")
public class AsyncManager {

	final String OsName = "Android";
	final String versionNo=SplashActivity.versionNumber;
	public String IMEINumber=SplashActivity.IMEI;
	final String NAMESPACE = "http://www.retailware.in/";

	String latitude;
	String Longitude;

	// For Local

	//	final String RsplUrl = "http://192.168.200.78//mydoctor/webservices/mydoctorv1.asmx";

	// For Nagar
	//	final String RsplUrl = "http://192.168.1.160//mydoctor/webservices/mydoctorv1.asmx";

	// For Azure Trial
	//final String RsplUrl = SplashActivity.DomainName+"WebServices/MyDoctorV1.asmx";

	// For Live 
	//final String RsplUrl = SplashActivity.DomainName+"WebServices/MyDoctorv1.asmx";

	static Context mContext;
	DatabaseHelper dbHelper;

	public SoapObject ActivationUser(Context context,String loginname,String password,String DeviceIMEINo,int SurveyTemplateID,int UserID,String ShopName)
	{
		mContext =context;
		SoapObject webResponse =null;
		dbHelper= new DatabaseHelper(mContext);
		
		final String NAMESPACE ="http://retailware.biz/websites/survey/webservice/activation.php:Activation";
		final String RsplUrl="http://retailware.biz/websites/survey/webservice/activation.php?wsdl";
		final String SOAP_ACTION="http://retailware.biz/websites/survey/webservice/activation.php/Activation";
		final String METHOD_NAME="Activation";
		
		try
		{
			SoapObject request= new SoapObject(NAMESPACE,METHOD_NAME);
			request.addProperty("loginName",loginname);
			request.addProperty("password",password);
			request.addProperty("DeviceIMEINo",DeviceIMEINo);
			request.addProperty("SurveyTemplateID",SurveyTemplateID);
			request.addProperty("UserID",UserID);
			request.addProperty("ShopName",ShopName);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet =true;

			envelope.setOutputSoapObject(request);

			Log.i("TAG", "Envelope: " + envelope.toString());
			HttpTransportSE httpTransport = new HttpTransportSE(RsplUrl);

			Object response=null;

			httpTransport.call(SOAP_ACTION, envelope);
			response = envelope.getResponse();

			System.out.println("Response " +response);
			SoapObject result = (SoapObject)envelope.bodyIn;

			webResponse=result;
			dbHelper.CloseDB();
		}
		catch(Exception e)
		{
			dbHelper.CloseDB();

		}
		return webResponse;	
	}
	
	public SoapObject insertRecords(Context context,String loginname,String password,String strSql)
	{
		mContext =context;
		SoapObject webResponse =null;
		dbHelper= new DatabaseHelper(mContext);

		final String NAMESPACE ="http://retailware.biz/websites/survey/webservice/insertRecords.php:insertData";
		final String RsplUrl="http://retailware.biz/websites/survey/webservice/insertRecords.php?wsdl";
		final String SOAP_ACTION="http://retailware.biz/websites/survey/webservice/insertRecords.php/insertData";
		final String METHOD_NAME="insertData";
		try
		{
			SoapObject request= new SoapObject(NAMESPACE,METHOD_NAME);
			request.addProperty("loginName",loginname);
			request.addProperty("password",password);
			request.addProperty("sqlquery",strSql);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet =true;

			envelope.setOutputSoapObject(request);

			Log.i("TAG", "Envelope: " + envelope.toString());
			HttpTransportSE httpTransport = new HttpTransportSE(RsplUrl);

			Object response=null;

			httpTransport.call(SOAP_ACTION, envelope);
			response = envelope.getResponse();

			System.out.println("Response " +response);
			SoapObject result = (SoapObject)envelope.bodyIn;

			webResponse=result;
			dbHelper.CloseDB();
		}
		catch(Exception e)
		{
			dbHelper.CloseDB();

		}
		return webResponse;	

	}
	public SoapObject getSoapData(Context context,String loginName, String Password, String strSQL){
		mContext =context;
		SoapObject webResponse =null;
		dbHelper= new DatabaseHelper(mContext);

		final String NAMESPACE ="http://retailware.biz/websites/survey/webservice/downloadData.php:fetchData";
		final String RsplUrl="http://retailware.biz/websites/survey/webservice/downloadData.php?wsdl";
		final String SOAP_ACTION="http://retailware.biz/websites/survey/webservice/downloadData.php/fetchData";
		final String METHOD_NAME="fetchData";

		try
		{
			SoapObject request= new SoapObject(NAMESPACE,METHOD_NAME);
			request.addProperty("loginName",loginName);
			request.addProperty("password",Password);
			request.addProperty("sqlquery",strSQL);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet =true;

			envelope.setOutputSoapObject(request);

			Log.i("TAG", "Envelope: " + envelope.toString());
			HttpTransportSE httpTransport = new HttpTransportSE(RsplUrl);

			Object response=null;

			httpTransport.call(SOAP_ACTION, envelope);
			response = envelope.getResponse();

			System.out.println("Response " +response);
			SoapObject result = (SoapObject)envelope.bodyIn;

			webResponse=result;
			dbHelper.CloseDB();
		}
		catch(Exception e)
		{
			dbHelper.CloseDB();
		}
		return webResponse;	
	}

/*	public SoapObject validateVersion(Context context,String key){
		String	IMEI = null;
		try{
			TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			IMEI=telephonyManager.getDeviceId().toString();
			//			IMEI="000000000000000";
		}
		catch(Exception e){
			e.printStackTrace();
		}

		mContext =context;
		SoapObject result = null;
		dbHelper= new DatabaseHelper(mContext);

		final String SOAP_ACTION = "http://www.retailware.in/CheckAppVersion";
		final String METHOD_NAME = "CheckAppVersion";

		try{
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 

			request.addProperty("IMEINumber",IMEI);
			request.addProperty("VersionNo",versionNo);
			/*request.addProperty("OSName",OsName);
				request.addProperty("Latitude","");
				request.addProperty("Longitude","");
				request.addProperty("RequestParameters",key);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransport = new HttpTransportSE(RsplUrl);
			Object response=null;

			httpTransport.call(SOAP_ACTION, envelope);
			response = envelope.getResponse();  
			System.out.println("Response " +response);
			result = (SoapObject)envelope.bodyIn;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;	
	}
*/
	public String updateTime(int hours, int mins) {
		String time;
		String timeSet = "";
		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";

		String minutes = "";
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);

		// Append in a StringBuilder
		time = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
		return time;
	}
}
