package com.codepath.vvalluri.imagesearch.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.codepath.vvalluri.imagesearch.R;
import com.codepath.vvalluri.imagesearch.models.imageResult;
import com.ortiz.touch.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;



public class ImageDisplayActivity  extends SherlockFragmentActivity  {

	private ShareActionProvider miShareAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		
		// Get the image result which is serialized
		imageResult imgResult = (imageResult)getIntent().getSerializableExtra("result");
		
		// get the image view
		 TouchImageView ivImage = (TouchImageView) findViewById(R.id.ivImageResult);
		
		// Photo to fit Screen size
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenwidth = size.x;
		int screenheight = size.y;

		/* Set the imageview layout params to the screen size width and aspect ratio */
		float fittedimgheight;
		float fittedimgwidth;
		fittedimgwidth = (float) screenwidth;
		fittedimgheight = fittedimgwidth * ((float) imgResult.imgHeight / (float) imgResult.imgWidth);
		
		// Set the layout params to the newly calculated dimensions
		ViewGroup.LayoutParams iv_lparams = ivImage.getLayoutParams();
		iv_lparams.height = (int) fittedimgheight;
		iv_lparams.width = (int) fittedimgwidth;
		ivImage.setLayoutParams(iv_lparams);
		
		// cleanup subview if recycled to clear the previous image content
		ivImage.getLayoutParams().height = (int) fittedimgheight;
		ivImage.setImageResource(0);

		// fetch the photo from the url using Picasso asynchronously in background not in main thread
		// It downloads the imagebytes, converts to bitmap and loads the image
		// VV Picasso.with(this).load(imgResult.fullurl).fit().centerInside().into(ivImage,  new Callback() {
		Picasso.with(this).load(imgResult.fullurl).resize(600, 600).into(ivImage,  new Callback() {
		//Picasso.with(this).load(imgResult.fullurl).resize(screenwidth, screenheight).into(ivImage,  new Callback() {
			@Override
	        public void onSuccess() {
	            // Setup share intent now that image has loaded
	            setupShareIntent();
	        }
	        
	        @Override
	        public void onError() { 
	            Log.i("INFO", "Picasso image detail load failure");
	        }
	   });
	}
	
	// Gets the image URI and setup the associated share intent to hook into the provider
	public void setupShareIntent() {
	    // Fetch Bitmap Uri locally
	     TouchImageView ivImage = (TouchImageView) findViewById(R.id.ivImageResult);
	    
	    if (ivImage == null) {
	    	return;
	    }
	    // Get the URI for the image
	    Uri bmpUri = getLocalBitmapUri(ivImage);
	    if (bmpUri != null) {
	    	// Create share intent as described above
	    	Intent shareIntent = new Intent();
	    	if (shareIntent != null) {
	    		shareIntent.setAction(Intent.ACTION_SEND);
	    		shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
	    		shareIntent.setType("image/*");
	    		// Attach share event to the menu item provider
	    		if (miShareAction != null) {
	    			miShareAction.setShareIntent(shareIntent);
	    		}
	    	}
	    } else {
	    	Log.i("INFO", "URI failure for image sharing");
	    }
	}

	// Returns the URI path to the Bitmap displayed in specified ImageView
	public Uri getLocalBitmapUri(TouchImageView imageView) {
	    // Extract Bitmap from ImageView drawable
	    Drawable drawable = imageView.getDrawable();
	    Bitmap bmp = null;
	    if (drawable instanceof BitmapDrawable){
	       bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
	    } else {
	       return null;
	    }
	    // Store image to default external storage directory
	    Uri bmpUri = null;
	    try {
	        File file =  new File(Environment.getExternalStoragePublicDirectory(  
		        Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
	        file.getParentFile().mkdirs();
	        FileOutputStream out = new FileOutputStream(file);
	        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
	        out.close();
	        bmpUri = Uri.fromFile(file);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return bmpUri;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.image_display, menu);
		
	    // Locate MenuItem with ShareActionProvider
	    MenuItem item = menu.findItem(R.id.menu_item_share);
	    // Fetch reference to the share action provider
	    miShareAction = (ShareActionProvider) item.getActionProvider();
	    // Return true to display menu
	    return true;
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
}
