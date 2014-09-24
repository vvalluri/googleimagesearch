package com.codepath.vvalluri.imagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.codepath.vvalluri.imagesearch.R;
import com.codepath.vvalluri.imagesearch.adapters.ImageResultAdapter;
import com.codepath.vvalluri.imagesearch.fragments.ImageFilterFragment;
import com.codepath.vvalluri.imagesearch.fragments.ImageFilterFragment.ImageFilterFragmentListener;
import com.codepath.vvalluri.imagesearch.models.imageResult;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class SearchActivity  extends SherlockFragmentActivity implements ImageFilterFragmentListener {
	//private GridView gvResults;
	private StaggeredGridView gvResults;
	private ArrayList<imageResult> imageresults;
	private ImageResultAdapter aImageResults;
	private SearchView searchView;
	private SearchFilterData filterData;
	private String searchUrl;
	private String queryStr;
	private int lastStartPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    	searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
        setupViews();
        // Create filter data
        filterData = new SearchFilterData();
        // Creates data source
        imageresults = new ArrayList<imageResult>();
        // Attaches adapter
        aImageResults = new ImageResultAdapter(this, imageresults);

        // Attach the listener to the AdapterView onCreate
        gvResults.setOnScrollListener(new EndlessScrollListener() {
        	@Override
        	public void onLoadMore(int page, int totalItemsCount) {
        		// Triggered only when new data needs to be appended to the list
        		// Add whatever code is needed to append new items to your AdapterView
        		if (totalItemsCount > 0) {
        			onActionBarSearch(searchUrl, queryStr, totalItemsCount);
        		}
        	}
        });
        gvResults.setAdapter(aImageResults);

    }
    
    public void setupViews() {
    	gvResults = (StaggeredGridView) findViewById(R.id.gvResults);
    	gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Launch the image display activity
				Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
				imageResult result = imageresults.get(position);
				i.putExtra("result", result);
				// Start the new actvitiy
				startActivity(i);			
			}		
		});
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    

    
    public void onActionBarSearch(String searchurl, String query, int itemCount) {
    	
    	if (lastStartPos > itemCount) {
    		Log.i("INFO", "laststart (" + Integer.toString(lastStartPos) + ") is more than itemcount (" + Integer.toString(itemCount) + ")" );
    		return;
    	}

    	// Check for Internet connectivity
    	if (isNetworkAvailable() == false) {
    		Toast.makeText(this, "No INTERNET Connectivity", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	// Create the HTTP client
    	AsyncHttpClient client = new AsyncHttpClient();
    	
    	//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=android&rsz=8
    	//searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
    	
    	// Add request params
    	RequestParams params = new RequestParams();
    	params.put("q", query);
    	// Request 8 results per page
    	params.put("rsz", "8");
    	// Specify start index
    	params.put("start", Integer.toString(lastStartPos));
    	
    	
    	// Specify filter params
    	if (!filterData.imgColor.equals("any")) {
    		params.put("imgcolor", filterData.imgColor);
    	}
    	if (!filterData.imgSize.equals("any")) {
    		params.put("imgsz", filterData.imgSize);
    	}
    	if (!filterData.imgType.equals("any")) {
    		params.put("imgtype", filterData.imgType);
    	}
    	if (!filterData.imgSite.equals("any")) {
    		params.put("as_sitesearch", filterData.imgSite);
    	}

    	client.get(searchUrl, params, new JsonHttpResponseHandler() {
    		@Override
    		public void onSuccess(int statusCode, Header[] headers,
    				JSONObject response) {
    			JSONArray imgResultsJson = null;

    			// responseData -> results ->[x] -> tburl, fullurl ....
    			try {
    				if ((response.optJSONObject("responseData") != null) &&
    						(response.getJSONObject("responseData").optJSONArray("results") != null)) {
					imgResultsJson = response.getJSONObject("responseData").getJSONArray("results");
					// Clear existing items: TBD only for new search
					//imageresults.clear();
					
					// Add the items to adapter directly
					aImageResults.addAll(imageResult.fromJSONArray(imgResultsJson));
					lastStartPos = lastStartPos + imgResultsJson.length();
					
    				}
    			} catch (JSONException e) {
					e.printStackTrace();
				}
    			Log.i("INFO", imageresults.toString());
    		}
    	});
    	
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
        	searchView.setOnQueryTextListener(new OnQueryTextListener() {
        		@Override
        		public boolean onQueryTextSubmit(String query) {
        			// perform query here
        			queryStr = query;
        			// New search clear the list
        			//imageresults.clear();
        			// Clear Adapter
        			aImageResults.clear();
        			lastStartPos = 0;
        			onActionBarSearch(searchUrl, queryStr, 0);
        			return true;
        		}

        		@Override
        		public boolean onQueryTextChange(String newText) {
        			return false;
        		}
        	});
        }
       return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //onQueryTextChange("");
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
    
    @Override
    public void onFinishFilterDialog(SearchFilterData inFilterData) {
    	//Toast.makeText(this, "Color " + inFilterData.imgColor, Toast.LENGTH_SHORT).show();
    	filterData.imgSize = inFilterData.imgSize;
    	filterData.imgColor = inFilterData.imgColor;
    	filterData.imgType = inFilterData.imgType;
    	filterData.imgSite = inFilterData.imgSite;
    }
    
    public void onFilterAction(MenuItem mi) {
        FragmentManager fm = getSupportFragmentManager();
        ImageFilterFragment filterItem = new ImageFilterFragment();
        Bundle args = new Bundle();
        // Pass the current Filter values to DialogFragment
        args.putString("size", filterData.imgSize);
        args.putString("color", filterData.imgColor);
        args.putString("type", filterData.imgType);
        args.putString("site", filterData.imgSite);
        filterItem.setArguments(args);
        filterItem.show(fm, "fragment_edit_text");
    }
}
