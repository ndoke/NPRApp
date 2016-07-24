/**
 * Homework 04
 * Common.java
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.homework04;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;


public class Common {
	public static boolean isNetworkConnected(Activity activity) {
		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}
	public static boolean ifNwDisconnectedDisplayMsg(Activity activity, AsyncTask task){
		if (!isNetworkConnected(activity)) {
			Toast.makeText(activity, "No internet connectivity",
					Toast.LENGTH_LONG).show();
			task.cancel(true);
			return false;
		}
		return true;
	}
}
