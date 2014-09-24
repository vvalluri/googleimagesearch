package com.codepath.vvalluri.imagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.vvalluri.imagesearch.R;
import com.codepath.vvalluri.imagesearch.models.imageResult;
import com.squareup.picasso.Picasso;

public class ImageResultAdapter extends ArrayAdapter<imageResult> {

	public ImageResultAdapter(Context context, List<imageResult> images) {
		super(context, R.layout.item_image_result, images);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
	       // Get the data item for this position
	       imageResult imageinfo = getItem(position);    
	       // Check if an existing view is being reused, otherwise inflate the view
	       if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
	          holder = new ViewHolder();
			  holder.imgText = (TextView) convertView.findViewById(R.id.tvTitle);
			  holder.imgPhoto = (ImageView) convertView.findViewById(R.id.ivImage);

			  convertView.setTag(holder);
	       } else {
	    	   holder = (ViewHolder) convertView.getTag();
	       }

	       // Populate the data into the template view using the data object
	       holder.imgText.setText(Html.fromHtml(imageinfo.title));
	       
	       //Clear image data
	       holder.imgPhoto.setImageResource(0);
	       Picasso.with(getContext()).load(imageinfo.tburl).into(holder.imgPhoto);
	       
	       // Return the completed view to render on screen
	       return convertView;
	}
	
	static class ViewHolder {
		TextView imgText;
		ImageView imgPhoto;
	}

}
