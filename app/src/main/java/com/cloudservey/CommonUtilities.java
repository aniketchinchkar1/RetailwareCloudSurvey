package com.cloudservey;

import android.content.Context;
import android.content.Intent;

public class CommonUtilities  {
	
	// give your server registration url here
//    static final String SERVER_URL = "http://10.0.2.2/gcm_server_php/register.php";
	 
//	static final String SERVER_URL = "http://192.168.200.146/gcm_server_php/register.php";
	
//static final String SERVER_URL = "http://pmitrial.jewelsoft.in/gcm_server/register.php";
	static final String SERVER_URL = "http://retailware.biz/websites/survey/gcm/register.php";
//	static final String SERVER_URL = "http://pmi.jewelsoft.in/gcm_server/register.php";
	
//  static final String SERVER_URL = "http://192.168.1.254:81/gcm_server_php/register.php";

    // Google project id
//    static final String SENDER_ID = ""; 
    
    public static final String SENDER_ID = "780265631134"; 

    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
