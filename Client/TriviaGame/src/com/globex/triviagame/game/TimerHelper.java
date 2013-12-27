package com.globex.triviagame.game;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import com.globex.triviagame.R;
import com.globex.triviagame.activities.RoundFinishActivity;

public class TimerHelper {
	private GameActivity game;
	private PointHelper pointHelper;
	private CountDownTimer timer;
	private boolean isFinishing = false;

	/**
	 * TimerHelper: Constructs a new
	 * 
	 * @param game
	 *            : The activity that uses the time helper.
	 */
	public TimerHelper(GameActivity game, PointHelper pointHelper) {
		this.game = game;
		this.pointHelper = game.getPointsHelper();
		initTimer();
	}

	/**
	 * initTimer: Sets up the round timer with default time of 1 minute.
	 */
	public void initTimer() {
		final int ROUND_TIME = 20000;
		final ProgressBar pBar = (ProgressBar) game
				.findViewById(R.id.time_left);
		pBar.setMax(ROUND_TIME);
		timer = new CountDownTimer(ROUND_TIME, 500) {
			@Override
			public void onFinish() {
				pBar.setProgress(pBar.getMax());
				Intent i = new Intent(game.getApplication(),
						RoundFinishActivity.class);
				i.putExtra("points", pointHelper.getPoints());
				pointHelper.resetPoints();
				game.finish();
				if(isFinishing == false){
				game.startActivity(i);
				}
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
	 * stopTimer: Starts the timer running.
	 */
	public void stopTimer() {
		isFinishing = true;
	}
	

}
