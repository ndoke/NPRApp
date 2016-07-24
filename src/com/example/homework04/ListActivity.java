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
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListActivity extends Activity {

	private ProgressDialog progressDialogue;
	private String baseUrl = "http://api.npr.org/list?output=JSON&id=";
	private String itemTypeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			itemTypeId = intent.getExtras().getString(MainActivity.ITEM_ID_KEY);
		} else {
			Log.w(MainActivity.LOGGING_KEY, "Extras are null");
			return;
		}
		new FetchItemsTask().execute(itemTypeId);
	}

	/**
	 * Fetch topics or programs
	 * 
	 * @author ajay&aswathi
	 * 
	 */
	private class FetchItemsTask extends AsyncTask<String, Void, List<Item>> {

		@Override
		protected void onPreExecute() {
			if (!Common.ifNwDisconnectedDisplayMsg(ListActivity.this, this)) {
				return;
			}
			progressDialogue = new ProgressDialog(ListActivity.this);
			progressDialogue.setTitle("Loading Results");
			progressDialogue.setCancelable(false);
			progressDialogue.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(final List<Item> result) {
			progressDialogue.dismiss();
			super.onPostExecute(result);
			ListView listView = (ListView) findViewById(R.id.listView1);
			final ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(
					ListActivity.this, android.R.layout.simple_list_item_1,
					result);
			adapter.setNotifyOnChange(true);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Item item = result.get(position);
					Intent intent = new Intent(ListActivity.this,
							StoriesActivity.class);
					intent.putExtra(MainActivity.ITEM_KEY, item);
					startActivity(intent);
					Log.d(MainActivity.LOGGING_KEY, item.toString());
				}
			});
		}

		@Override
		protected List<Item> doInBackground(String... params) {
			BufferedReader breader = null;
			URL url;
			List<Item> photos = null;
			try {
				// GalleryActivity.this.getResources().getString(R.string.baseUrl)
				// + URLEncoder.encode(params[0], "UTF-8")
				url = new URL(baseUrl + params[0]);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				int statusCode = connection.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					InputStream inStream = connection.getInputStream();
					breader = new BufferedReader(
							new InputStreamReader(inStream));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = breader.readLine()) != null) {
						sb.append(line);
					}
					Log.d(MainActivity.LOGGING_KEY, sb.toString());
					photos = ItemParserHelper.parse(sb.toString());
					Log.d(MainActivity.LOGGING_KEY, photos.size() + "");
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
			return photos;
		}
	}
}
