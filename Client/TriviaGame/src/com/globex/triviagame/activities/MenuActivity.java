package com.globex.triviagame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.globex.triviagame.R;

/**
 * 
 * @author sterling
 *
 */
public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMenuButtons();            
    }
  
    /**
     * setMenuButtons: Sets the click handlers for all of the menu buttons, and passes along any extras.
     * 					Start Game
     * 					High Score
     * 					Add Question
     * 					About
     */
    private void setMenuButtons(){
    	final Button start      = (Button) findViewById(R.id.start);
    	final Button high_score = (Button) findViewById(R.id.high_score);
    	final Button addq       = (Button) findViewById(R.id.addq);
    	final Button about      = (Button) findViewById(R.id.about);

    	start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send the retrieved categories to the CategorySelect activity.
				Intent i = new Intent(v.getContext(), CategorySelectActivity.class);
				startActivity(i);	
			}
		});
    	
    	high_score.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
    		  		  		
    	});
    
    	addq.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
    		  		  		
    	});
    	
    	about.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
    		  		  		
    	});
    	
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
