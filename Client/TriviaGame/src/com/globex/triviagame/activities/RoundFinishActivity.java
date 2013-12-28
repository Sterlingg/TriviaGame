package com.globex.triviagame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.globex.triviagame.R;
import com.globex.triviagame.game.PointHelper;

public class RoundFinishActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_round_finish);
		// Show the Up button in the action bar.;
		TextView lazyText = (TextView)findViewById(R.id.textView1);
		lazyText.setText("You got " + PointHelper.getPoints() +" points!");	

		Button homePageBtn = (Button)findViewById(R.id.home_page_btn);
		
		homePageBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {			
				Intent i = new Intent(v.getContext(), MenuActivity.class);
				startActivity(i);
				
				Activity activity = (Activity) v.getContext();
				activity.finish();
			}});
		
		PointHelper.resetPoints();
	}
}
