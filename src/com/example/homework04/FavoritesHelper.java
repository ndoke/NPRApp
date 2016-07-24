/**
 * Homework 04
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.homework04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class FavoritesHelper {
	private static final String PREF_FILENAME = "com.example.homework04.fav.stories";
	private static final String STORY_IDS_KEY = "story.ids.key";

	public static void addStoryToFavourites(Activity activity, long storyId) {
		SharedPreferences sharedPref = activity.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE);
		Set<String> storyIdsAlreadyPresent = sharedPref.getStringSet(STORY_IDS_KEY, Collections.<String>emptySet());
		Editor editor = sharedPref.edit();
		Set<String> newSet = new HashSet<String>(storyIdsAlreadyPresent);
		newSet.add(Long.toString(storyId));
		editor.remove(STORY_IDS_KEY);
		editor.putStringSet(STORY_IDS_KEY, newSet);
		editor.commit();
	}

	public static void removeStoryFromFavourites(Activity activity, long storyId) {
		SharedPreferences sharedPref = activity.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE);
		Set<String> storyIdsAlreadyPresent = sharedPref.getStringSet(STORY_IDS_KEY, Collections.<String>emptySet());
		Set<String> newSet = new HashSet<String>(storyIdsAlreadyPresent);
		newSet.remove(Long.toString(storyId));
		Editor editor = sharedPref.edit();
		editor.remove(STORY_IDS_KEY);
		editor.putStringSet(STORY_IDS_KEY, newSet);
		editor.commit();
	}

	public static boolean isStoryInFavorites(Activity activity, long storyId) {
		SharedPreferences sharedPref = activity.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE);
		Set<String> storyIdsAlreadyPresent = sharedPref.getStringSet(STORY_IDS_KEY, Collections.<String>emptySet());
		for (String storyIdString : storyIdsAlreadyPresent) {
			if (storyIdString.equals(Long.toString(storyId)))
				return true;
		}
		return false;
	}

	public static List<Long> getStoryIdsInFavorites(Activity activity) {
		List<Long> storyIds = new ArrayList<Long>();
		SharedPreferences sharedPref = activity.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE);
		Set<String> storyIdsAlreadyPresent = sharedPref.getStringSet(STORY_IDS_KEY, Collections.<String>emptySet());
		for (String storyIdString : storyIdsAlreadyPresent) {
			storyIds.add(Long.valueOf(storyIdString));
		}
		return storyIds;
	}
	public static String getCommaSepStoryIdsInFavorites(Activity activity){
		SharedPreferences sharedPref = activity.getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE);
		Set<String> storyIdsAlreadyPresent = sharedPref.getStringSet(STORY_IDS_KEY, Collections.<String>emptySet());
		return TextUtils.join(",", storyIdsAlreadyPresent);
	}
}
