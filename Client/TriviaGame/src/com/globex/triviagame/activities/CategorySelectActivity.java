package com.globex.triviagame.activities;

import java.util.ArrayList;
import java.util.List;

import com.globex.triviagame.R;
import com.globex.triviagame.game.GameActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class CategorySelectActivity extends Activity
{

    private ListView catListView;
    private ArrayAdapter<String> catAdapter;
    private List<String> catList = new ArrayList<String>();
		
    /**
     * onCreate:
     * Passed:
     * 			catList
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_category_select);
		
	setUpCatList();

    }
    
    /**
     * setUpCatList: Fills the category screen with the list of categories received from Web->Splash -> Game -> CategorySelect.
     * 				 
     */
    private void setUpCatList(){
	catListView = (ListView) findViewById(R.id.cat_list_view);
	catList = getIntent().getStringArrayListExtra("catList");
	
	catAdapter = new ArrayAdapter<String>(this, R.layout.cat_row_layout, catList);
    
	catListView.setAdapter(catAdapter);
	catListView.setOnItemClickListener(new OnItemClickListener(){

		@Override
		    public void onItemClick(AdapterView<?> parent, View view,
					    int position, long id) {
		    TextView category = (TextView)parent.getChildAt(position);					
		    Intent i = new Intent(getApplication(),GameActivity.class);
		    i.putExtra("category", category.getText());
		    Log.v("CategorySelectActivity","Category Selected:" + category.getText());
		    startActivity(i);						
		}		    		    
	    });
	
    }
		
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_category_select, menu);
	return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
	    NavUtils.navigateUpFromSameTask(this);
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }



}
