package com.globex.triviagame.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;

import com.globex.triviagame.R;
import com.globex.triviagame.datatypes.Category;
import com.globex.triviagame.transport.CategoryService;
import com.globex.triviagame.transport.ResultReceiverImpl;

/**
 * 
 * Used to "remove" load time when categories are retrieved from the 
 * server. 
 * @author sterling
 * 
 */
public class SplashActivity extends Activity implements ResultReceiverImpl.Receiver{

	// I really hate Java enums...
	private static final int 	STATUS_RUNNING = 0;
	private static final int 	STATUS_FINISHED = 1;
	private static final int 	STATUS_ERROR = 2;
	private ArrayList<String> catList = new ArrayList<String>();
	
	// Incremented each time a service finishes. Used to tell if we can start the MenuActivity.
	private int servicesFinished = 0;
	
    private Intent nextIntent; 
		
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	 
	        setContentView(R.layout.activity_splash);
	        setPreferences();
	        nextIntent = new Intent(SplashActivity.this, MenuActivity.class); 
	        startCategoryWebService();
	}

	
	/**
	 * setPreferences: Sets up the applications constant values.
	 */
	// TODO: Store constants in PreferenceManager.
	private void setPreferences(){
		
	}


	/**
	 * startCategoryWebService: Start a service that retrieves the categories from the
	 * 							db.
	 * 							GET /getcategory
	 */
	private void startCategoryWebService() {
		ResultReceiverImpl receiver = new ResultReceiverImpl(new Handler());
		receiver.setReceiver(this);
		final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				CategoryService.class);
		intent.putExtra("receiver", receiver);
		intent.putExtra("command", "getcategories");
		startService(intent);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_splash, menu);
		return true;
	}

	/**
	 * onReceiveResult: Callback with result from GET /getcategory and GET /getquestion.
	 */
	//TODO: Add connection timeouts.
	@Override
	public void onReceiveResult(int result, Bundle resultData) {
		switch (result) {
		case STATUS_RUNNING:
			Log.v("GameActivity", "RUNNING");
			break;
		case STATUS_FINISHED:
			if(resultData.getString("responseType").equals("Categories"))
			{
				handleCategoryResult(resultData);
				servicesFinished++;
			}
			
			if(servicesFinished == 1){
				startActivity(nextIntent);		
		        finish();
			}
			break;
		case STATUS_ERROR:
			Log.v("GameActivity", "ERROR");
			break;
		}		
	}
	
	/**
	 * handleCategoryResult: Forwards the response from GET /getcategory to the next intent.
	 * 	 					 Fills catList with all categories in the DB.
	 */
	private void handleCategoryResult(Bundle resultData){
		ArrayList<Category> results = resultData.getParcelableArrayList("results");
		for(Category c : results){
			catList.add(c.getCategory());
		}							
        nextIntent.putStringArrayListExtra("catList", catList);
		Log.v("GameActivity", "FINISHED");
	}
	
}
