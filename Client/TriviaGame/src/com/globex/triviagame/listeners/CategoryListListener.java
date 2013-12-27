package com.globex.triviagame.listeners;

import com.globex.triviagame.activities.CategorySelectActivity;
import com.globex.triviagame.game.GameActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * CategoryListListener: Fired when category selected.
 * 
 * Defines how things react to
 * @author sterling
 *
 */
public class CategoryListListener implements OnItemClickListener {
	
	private CategorySelectActivity cat;
	
	public CategoryListListener(CategorySelectActivity cat){
		this.cat= cat;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent i = new Intent(cat.getApplication(),GameActivity.class);
		Log.v("CategoryListListener","Clicky Clicky");
		cat.startActivity(i);	
		cat.finish();
	}

}