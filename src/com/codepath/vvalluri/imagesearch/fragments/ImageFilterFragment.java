package com.codepath.vvalluri.imagesearch.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.vvalluri.imagesearch.R;
import com.codepath.vvalluri.imagesearch.activities.SearchFilterData;


public class ImageFilterFragment extends DialogFragment {
    public interface ImageFilterFragmentListener {
        void onFinishFilterDialog(SearchFilterData FilterData);
    }
	
    private EditText imgSite;
    private Spinner spinSize;
    private Spinner spinColor;
    private Spinner spinType;
 	private View customView;
 	private SearchFilterData inputFilterData;


    public ImageFilterFragment() {
        // Empty constructor required for DialogFragment
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        inputFilterData = new SearchFilterData();
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        customView = inflater.inflate(R.layout.fragment_filter, null, false);
        imgSite = (EditText)customView.findViewById(R.id.eTsite);
        // Populate spinner for size
        addItemsOnSpinnerSize();
        // Populate spinner for color
        addItemsOnSpinnerColor();
        // Populate spinner for type
        addItemsOnSpinnerType();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(customView)
        // Add action buttons
               .setPositiveButton("save", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       // save
                       // Return input text to activity
                   		inputFilterData.imgSize = String.valueOf(spinSize.getSelectedItem());
                   		inputFilterData.imgColor = String.valueOf(spinColor.getSelectedItem());;
                   		inputFilterData.imgType = String.valueOf(spinType.getSelectedItem());;
                   		inputFilterData.imgSite = imgSite.getText().toString();
                   		ImageFilterFragmentListener activity = (ImageFilterFragmentListener) getActivity();
                   		activity.onFinishFilterDialog(inputFilterData);
                   		dismiss();
                   }
               })
               .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Cancel
                   }
               }); 

        getArguments().getInt("pos");

        
        // Set the initial filter values
    	inputFilterData.imgSite = getArguments().getString("site");
		imgSite.setText(inputFilterData.imgSite);
		int text_length = imgSite.length();
		imgSite.setSelection(text_length);
		

        return builder.create();
    }
    
    // add items into spinner dynamically
    public void addItemsOnSpinnerSize() {
   
    	spinSize = (Spinner) customView.findViewById(R.id.spinnerImgSize);
    	List<String> list = new ArrayList<String>();
    	list.add("any");
    	list.add("small");
    	list.add("medium");
    	list.add("large");
    	list.add("xlarge");
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
    			android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinSize.setAdapter(dataAdapter);
    	inputFilterData.imgSize = getArguments().getString("size");
    	spinSize.setSelection(dataAdapter.getPosition(inputFilterData.imgSize));
    }
   
    // add items into spinner dynamically
    public void addItemsOnSpinnerColor() {
   
    	spinColor = (Spinner) customView.findViewById(R.id.spinnerImgColor);
    	List<String> list = new ArrayList<String>();
    	list.add("any");
    	list.add("black");
    	list.add("blue");
    	list.add("brown");
    	list.add("gray");
    	list.add("green");
    	list.add("orange");
    	list.add("pink");
    	list.add("purple");
    	list.add("red");
    	list.add("teal");
    	list.add("white");
    	list.add("yellow");
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
    			android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinColor.setAdapter(dataAdapter);
    	// Set default values from previous
    	inputFilterData.imgColor = getArguments().getString("color");
    	spinColor.setSelection(dataAdapter.getPosition(inputFilterData.imgColor));
    }
    
    // add items into spinner dynamically
    public void addItemsOnSpinnerType() {
   
    	spinType = (Spinner) customView.findViewById(R.id.spinnerImgType);
    	List<String> list = new ArrayList<String>();
    	list.add("any");
    	list.add("face");
    	list.add("photo");
    	list.add("clipart");
    	list.add("lineart");
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
    			android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinType.setAdapter(dataAdapter);
    	// Set default values from previous
    	inputFilterData.imgType = getArguments().getString("type");
    	spinType.setSelection(dataAdapter.getPosition(inputFilterData.imgType));
    	
    }  
}