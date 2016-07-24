/**
 * Homework 04
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.homework04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StoryActivity extends Activity {
	private Story story;
	private FetchMp3StreamTask mp3StreamTask;
	private MediaPlayerControlHelper mpCHelper;
	private boolean isStoryFav = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			story = (Story) intent.getExtras().get(MainActivity.STORY_KEY);
			((TextView) findViewById(R.id.textViewTitle)).setText(story.getTitle());
			((TextView) findViewById(R.id.textViewDateAired)).setText(story.getAiredDate() == null ? "NA"
					: SimpleDateFormat.getInstance().format(story.getAiredDate()));
			((TextView) findViewById(R.id.textViewRepName)).setText(story.getReporterName());
			((TextView) findViewById(R.id.textViewTeaserFull)).setText(story.getTeaser());
			if (FavoritesHelper.isStoryInFavorites(this, story.getId())) {
				ImageView iv = (ImageView) findViewById(R.id.imageViewFav);
				iv.setImageDrawable(getResources().getDrawable(R.drawable.rating_important));
				isStoryFav = true;
			}

		} else {
			Log.w(MainActivity.LOGGING_KEY, "No data with intent");
			return;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mpCHelper != null) {
			mpCHelper.show();
		}
		return true;
	}

	@Override
	protected void onStop() {
		if (mp3StreamTask != null) {
			mp3StreamTask.cancel(true);
			if (mpCHelper != null)
				mpCHelper.stop();
		}
		super.onStop();
	}

	public void backClicked(View view) {
		finish();
	}

	public void browserClicked(View view) {
		try {
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(story.getStoryUrl()));
			startActivity(myIntent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "No application can handle this request." + " Please install a webbrowser",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	public void mp3Clicked(View view) {
		if (story.getMp3url() == null) {
			Toast.makeText(this, "Sorry no mp3 available for this story", Toast.LENGTH_SHORT).show();
			return;
		}
		if (mp3StreamTask != null) {
			if (!mp3StreamTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
				Toast.makeText(this, "Prev mp3 play request still processing", Toast.LENGTH_SHORT).show();
				return;
			}
			if (mpCHelper != null) {
				return;
			}
		}
		mp3StreamTask = new FetchMp3StreamTask();
		mp3StreamTask.execute(story.getMp3url());
	}

	public void favClicked(View view) {
		if (isStoryFav) {
			FavoritesHelper.removeStoryFromFavourites(this, story.getId());
			ImageView iv = (ImageView) findViewById(R.id.imageViewFav);
			iv.setImageDrawable(getResources().getDrawable(R.drawable.rating_not_important));
			isStoryFav = false;
		} else {
			FavoritesHelper.addStoryToFavourites(this, story.getId());
			ImageView iv = (ImageView) findViewById(R.id.imageViewFav);
			iv.setImageDrawable(getResources().getDrawable(R.drawable.rating_important));
			isStoryFav = true;
		}
	}

	private class FetchMp3StreamTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPostExecute(final String result) {
			mpCHelper = new MediaPlayerControlHelper(result, StoryActivity.this);
		}

		@Override
		protected String doInBackground(String... params) {
			String mp3StreamUrl = null;
			try {
				URL url = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				int statusCode = connection.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					InputStream inputStream = connection.getInputStream();
					BufferedReader breader = new BufferedReader(new InputStreamReader(inputStream));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = breader.readLine()) != null) {
						sb.append(line);
					}
					breader.close();
					mp3StreamUrl = sb.toString();
				} else {
					Log.d(MainActivity.LOGGING_KEY, "Http status is not OK");
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mp3StreamUrl;
		}

	}
}
