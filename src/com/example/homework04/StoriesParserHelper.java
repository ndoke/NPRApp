/**
 * Homework 04
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.homework04;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class StoriesParserHelper {

	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"E, dd MMM yyyy kk:mm:ss Z");

	public static List<Story> parse(String storiesJson) throws JSONException {
		List<Story> stories = new ArrayList<Story>();
		JSONObject root = new JSONObject(storiesJson);
		JSONArray storiesArray = root.getJSONObject("list").getJSONArray(
				"story");
		for (int i = 0; i < storiesArray.length(); i++) {
			JSONObject storyJson = storiesArray.getJSONObject(i);
			Story story = new Story();
			JSONObject titleJson, teaserJson, dateJson;
			story.setTitle((titleJson = storyJson.getJSONObject("title")) != null ? titleJson.getString("$text") : "");
			story.setTeaser((teaserJson = storyJson.getJSONObject("teaser")) != null ? teaserJson.getString("$text") : "");
			if((dateJson = storyJson.getJSONObject("pubDate")) != null ){
				 try {
					story.setPubDate(dateFormatter.parse(dateJson.getString("$text"))) ;
				} catch (ParseException e) {
					Log.w(MainActivity.LOGGING_KEY, "could not parse date for "+story.getTitle());
					//e.printStackTrace();
				}
			}
			if((dateJson = storyJson.getJSONObject("audioRunByDate")) != null ){
				 try {
					story.setAiredDate(dateFormatter.parse(dateJson.getString("$text"))) ;
				} catch (Exception e) {
					Log.w(MainActivity.LOGGING_KEY, "could not parse aired date for "+story.getTitle());
					//e.printStackTrace();
				}
			}
			JSONObject thumbnailJson =null;
			try {
				thumbnailJson= storyJson.getJSONObject("thumbnail");
			} catch (JSONException e) {
				Log.w(MainActivity.LOGGING_KEY, "No thubnail, setting to null");
			}
			if(thumbnailJson != null){
				story.setImageUrl(thumbnailJson.getJSONObject("medium").getString("$text"));
			}
			//reporter name
			String reporterName = null;
			try {
				reporterName = storyJson.getJSONArray("byline").getJSONObject(0).getJSONObject("name").getString("$text");
			} catch (Exception e) {
				Log.w(MainActivity.LOGGING_KEY, "could not parse reporter name for "+story.getTitle());
				//e.printStackTrace();
			}
			story.setReporterName(reporterName);
			// story id
			story.setId(storyJson.getLong("id"));
			//mp3 url
			String mp3Url = null;
			try {
				mp3Url = storyJson.getJSONArray("audio").getJSONObject(0).getJSONObject("format").getJSONArray("mp3").getJSONObject(0).getString("$text");
			} catch (Exception e) {
				Log.w(MainActivity.LOGGING_KEY, "could not parse mp3 url for "+story.getTitle());
				//e.printStackTrace();
			}
			story.setMp3url(mp3Url);
			String storyUrl = null;
			try {
				storyUrl = storyJson.getJSONArray("link").getJSONObject(0).getString("$text");
			} catch (Exception e) {
				Log.w(MainActivity.LOGGING_KEY, "could not parse url for "+story.getTitle());
				//e.printStackTrace();
			}
			story.setStoryUrl(storyUrl);
			stories.add(story);
		}
		return stories;

	}
}
