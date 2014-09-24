package com.codepath.vvalluri.imagesearch.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class imageResult implements Serializable{

	private static final long serialVersionUID = -3651531253048982171L;
	public String tburl;
	public String fullurl;
	public String title;
	public int imgHeight;
	public int imgWidth;

	// new ImageResult(raw item json)
	public imageResult(JSONObject json) {
		try {
			this.fullurl = json.getString("url");
			this.tburl = json.getString("tbUrl");
			this.title = json.getString("title");
			this.imgHeight = json.getInt("height");
			this.imgWidth = json.getInt("width");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// Imageresult.fromJSONArray[....]
	public static ArrayList<imageResult> fromJSONArray(JSONArray array) {
		ArrayList<imageResult> results = new ArrayList<imageResult>();
		for (int i = 0; i < array.length(); i++) {
			try {
				results.add(new imageResult(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}	
		return results;
	}
}
