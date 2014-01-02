package com.globex.triviagame.activities;

import android.app.Activity;
import android.os.Bundle;

import com.globex.triviagame.R;
import com.globex.triviagame.game.QuestionHelper;
import com.globex.triviagame.game.ButtonsHelper;
import com.globex.triviagame.game.PointHelper;
import com.globex.triviagame.game.TimerHelper;

/**
 * GameActivity
 * The main trivia game activity where the user answers qustions.
 * @author sterling
 * 
 */
public class GameActivity extends Activity  {

	private PointHelper pointsHelper;
	private QuestionHelper questionHelper;
	private TimerHelper timerHelper;
	private ButtonsHelper buttonsHelper;

	@Override
	public void onBackPressed() {
		questionHelper.stopWebService();
		timerHelper.stopTimer();
		super.onBackPressed();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);	

		setUpHelpers();
	}

	/**
	 * setUpHelpers: Initializing the helpers. Order matters, since questionHelper depends on other helpers.
	 */
	private void setUpHelpers(){		
		timerHelper = TimerHelper.getInstance(this);
		buttonsHelper = ButtonsHelper.getInstance(this);
		questionHelper = QuestionHelper.getInstance(this, timerHelper, buttonsHelper);	

		buttonsHelper.initAnswerButtons(questionHelper);
		// Disable buttons until the web service finishes.
		buttonsHelper.disableButtons();
		questionHelper.startWebService();
		timerHelper.initTimer();
	}

	public PointHelper getPointsHelper() {
		return pointsHelper;
	}
}
