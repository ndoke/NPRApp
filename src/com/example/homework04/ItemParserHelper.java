/**
 * Homework 04
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.homework04;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ItemParserHelper {
	public static List<Item> parse(String itemsJson) throws JSONException{
		 List<Item> items = new ArrayList<Item>();
		 JSONObject root = new JSONObject(itemsJson);
		 JSONArray itemArrayJson = root.getJSONArray("item");
		 for(int i=0; i<itemArrayJson.length(); i++){
			 JSONObject itemJson = itemArrayJson.getJSONObject(i);
			 Item item = new Item();
			 item.setId(itemJson.getInt("id"));
			 item.setTitle(itemJson.getJSONObject("title").getString("$text"));
			 item.setAdditionalInfo(itemJson.getJSONObject("additionalInfo").getString("$text"));
			 items.add(item);
		 }
		return items;
		
	}
}
