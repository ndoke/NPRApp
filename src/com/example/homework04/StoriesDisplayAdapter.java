/**
 * Homework 04
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.homework04;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class StoriesDisplayAdapter extends ArrayAdapter<Story> {
	private Context context;
	private int resource;
	private List<Story> stories;

	public StoriesDisplayAdapter(Context context, int resource, List<Story> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
		this.stories = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(resource, parent, false);
		}
		Story story = stories.get(position);
		((TextView)convertView.findViewById(R.id.textViewStoryTitle)).setText(story.getTitle());
		((TextView)convertView.findViewById(R.id.textViewTeaser)).setText(story.getTeaser());
		String dateString="";
		if(story.getPubDate() != null){
			dateString = SimpleDateFormat.getInstance().format(story.getPubDate());
		}
		((TextView)convertView.findViewById(R.id.textViewDate)).setText(dateString);
		ImageView iv = (ImageView) convertView.findViewById(R.id.imageViewThumb);
		if(story.getImageUrl() != null){
		
		Picasso.with(context).load(story.getImageUrl()).into(iv);
		}else{
			iv.setImageDrawable(context.getResources().getDrawable(R.drawable.no_img));
		}
		
		return convertView;
	}
}
