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
import java.util.Collections;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class StoriesActivity extends Activity {
	private String id;
	private ProgressDialog progressDialogue;
	private String baseUrl = "http://api.npr.org/query?fields=all&numResults=25&output=JSON&apiKey=MDE4MzQ4NjIzMDE0MjQ1ODc3MjAwMDg2Zg001&id=";
	private String favBaseUrl = "http://api.npr.org/query?apiKey=MDE4MzA1MDQ4MDE0MjQxOTI4MzlhNDA5NQ001&numResults=25&output=JSON&id=";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stories);
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			if (intent.getExtras().get(MainActivity.FAV_KEY) != null) {
				baseUrl = favBaseUrl;
				id = FavoritesHelper.getCommaSepStoryIdsInFavorites(this);
				if (checkFavEmptyAndDisplayMsg(id))
					return;
			} else {
				id = ((Item) intent.getExtras().get(MainActivity.ITEM_KEY)).getId() + "";
			}
		} else {
			Log.w(MainActivity.LOGGING_KEY, "Extras are null");
			return;
		}
		new FetchStoriesTask().execute(id);
	}

	private boolean checkFavEmptyAndDisplayMsg(String id) {
		if (id == null || id.isEmpty()) {
			Toast.makeText(this, "Oops. nothing in favorites", Toast.LENGTH_LONG).show();
			return true;
		}
		return false;
	}

	@Override
	protected void onRestart() {
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			if (intent.getExtras().get(MainActivity.FAV_KEY) != null) {
				baseUrl = favBaseUrl;
				id = FavoritesHelper.getCommaSepStoryIdsInFavorites(this);
				//if (!checkFavEmptyAndDisplayMsg(id)) {
					new FetchStoriesTask().execute(id);
				//}
			}
		}

		super.onRestart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stories, menu);
		return  super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class FetchStoriesTask extends AsyncTask<String, Void, List<Story>> {

		@Override
		protected void onPreExecute() {
			if (!Common.ifNwDisconnectedDisplayMsg(StoriesActivity.this, this)) {
				return;
			}
			progressDialogue = new ProgressDialog(StoriesActivity.this);
			progressDialogue.setTitle("Loading Results");
			progressDialogue.setCancelable(false);
			progressDialogue.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(final List<Story> result) {
			progressDialogue.dismiss();
			super.onPostExecute(result);
			ListView listView = (ListView) findViewById(R.id.listViewStories);
			final ArrayAdapter<Story> adapter = new StoriesDisplayAdapter(StoriesActivity.this,
					R.layout.story_list_item, result);
			adapter.setNotifyOnChange(true);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Story story = result.get(position);
					Intent intent = new Intent(StoriesActivity.this, StoryActivity.class);
					intent.putExtra(MainActivity.STORY_KEY, story);
					startActivity(intent);
					Log.d(MainActivity.LOGGING_KEY, story.toString());
				}
			});
		}

		@Override
		protected List<Story> doInBackground(String... params) {
			BufferedReader breader = null;
			URL url;
			List<Story> stories = null;
			try {
				// GalleryActivity.this.getResources().getString(R.string.baseUrl)
				// + URLEncoder.encode(params[0], "UTF-8")
				if (params[0].isEmpty())
					return Collections.<Story> emptyList();
				url = new URL(baseUrl + params[0]);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				int statusCode = connection.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					InputStream inStream = connection.getInputStream();
					breader = new BufferedReader(new InputStreamReader(inStream));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = breader.readLine()) != null) {
						sb.append(line);
					}
					Log.d(MainActivity.LOGGING_KEY, sb.toString());
					stories = StoriesParserHelper.parse(sb.toString());
					Log.d(MainActivity.LOGGING_KEY, stories.size() + "");
				} else {
					Log.d(MainActivity.LOGGING_KEY, "Http status not OK");
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return stories;
		}
	}

}
