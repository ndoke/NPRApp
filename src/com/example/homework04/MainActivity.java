/**
 * Homework 04
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.homework04;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	public static final String LOGGING_KEY = "hw4";
	public static final String ITEM_KEY = "item";
	public static final String ITEM_ID_KEY = "itemIdKey";
	public static final String STORY_KEY = "storyKey";
	public static final String FAV_KEY = "favKey";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void itemBtnClicked(View view) {
		String itemId = "3004";
		if (view.getId() == R.id.buttonPrograms) {
			itemId = "3002";
		}
		Intent intent = new Intent(this, ListActivity.class);
		intent.putExtra(ITEM_ID_KEY, itemId);
		startActivity(intent);
	}

	public void exitBtnClicked(View view) {
		finish();
	}

	public void favoritesBtnClicked(View view){
		Intent intent = new Intent(this, StoriesActivity.class);
		intent.putExtra(FAV_KEY, true);
		startActivity(intent);
	}
}
