package com.globex.triviagame.game;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import com.globex.triviagame.R;
import com.globex.triviagame.activities.GameActivity;
import com.globex.triviagame.activities.RoundFinishActivity;

public class TimerHelper {
	private static TimerHelper instance = null;
	
	private GameActivity game;
	private CountDownTimer timer;

	/**
	 * TimerHelper: Constructs a new
	 * 
	 * @param game
	 *            : The activity that uses the time helper.
	 */
	private TimerHelper(GameActivity game) {
		this.game = game;
	}

	public static TimerHelper getInstance(GameActivity game){
		if(instance == null){
			instance = new TimerHelper(game);
			return instance;
		}
		else{
			instance.setGame(game);
			return instance;
		}
	}
	
	private void setGame(GameActivity game) {
		this.game = game;
	}

	/**
	 * initTimer: Sets up the round timer with default time of 1 minute.
	 */
	public void initTimer() {
		final int ROUND_TIME = 200000;
		final ProgressBar pBar = (ProgressBar) game
				.findViewById(R.id.time_left);
		pBar.setMax(ROUND_TIME);
		timer = new CountDownTimer(ROUND_TIME, 500) {
			@Override
			public void onFinish() {
				pBar.setProgress(pBar.getMax());				
				Intent i = new Intent(game,
						RoundFinishActivity.class);
			
				game.startActivity(i);
				game.finish();
			}

			@Override
			public void onTick(long timeLeft) {
				pBar.setProgress((int) timeLeft);
			}
		};

	}

	/**
	 * startTimer: Starts the timer running.
	 */
	public void startTimer() {
		timer.start();
	}

	/**
	 * startTimer: Starts the timer running.
	 */
	public void stopTimer() {
		timer.cancel();
	}
}
