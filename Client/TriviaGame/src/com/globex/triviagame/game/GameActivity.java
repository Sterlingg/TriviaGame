package com.globex.triviagame.game;

import android.app.Activity;
import android.os.Bundle;

import com.globex.triviagame.R;

/**
 * GameActivity
 * The main trivia game activity where the user answers qustions.
 * @author sterling
 * 
 */
public class GameActivity extends Activity  {

	private PointHelper pointsHelper;
	private AsyncTQHelper questionHelper;
	private TimerHelper timerHelper;
	private ButtonsHelper buttonsHelper;
		
	
	public void onStop () {
		timerHelper.stopTimer();
		super.onStop(); 
		}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);	
		
		setUpHelpers();
		buttonsHelper.initAnswerButtons(questionHelper);
		// Disable buttons until the web service finishes.
		buttonsHelper.disableButtons();
		questionHelper.startWebService();
		timerHelper.initTimer();
	}
	
	/**
	 * getButtonsHelper: Returns the buttons handler for the game.
	 * @return ButtonsHelper instance of GameActivity.
	 */
	public ButtonsHelper getButtonsHelper() {
		return buttonsHelper;
	}
	
	/**
	 * getPointsHelper: Returns the object handling the question web service.
	 * @return PointsHelper instance of GameActivity.
	 */
	public AsyncTQHelper getQuestionHelper() {
		return questionHelper;
	}
	
	/**
	 * getTimerHelper: Returns the object handling the timer.
	 * @return TimerHelper instance of GameActivity.
	 */
	public TimerHelper getTimerHelper() {
		return timerHelper;
	}
	
	/**
	 * getPointsHelper: Returns the points handler for the game.
	 * @return PointsHelper instance of GameActivity.
	 */
	public PointHelper getPointsHelper() {
		return pointsHelper;
	}
	
	/**
	 * setUpHelpers: Initializing the helpers. Order matters, since questionHelper depends on other helpers.
	 */
	private void setUpHelpers(){		
		pointsHelper = new PointHelper();		
		timerHelper = new TimerHelper(this, pointsHelper);
		buttonsHelper = new ButtonsHelper(this, pointsHelper);
		questionHelper = new AsyncTQHelper(this, timerHelper, buttonsHelper);		
	}

}
