package com.globex.triviagame.listeners;


import android.view.View;
import android.view.View.OnClickListener;

import com.globex.triviagame.game.AsyncTQHelper;
import com.globex.triviagame.game.ButtonType;
import com.globex.triviagame.game.PointHelper;

/**
 * AnswerButtonHandler
 * 
 * Adds points to the users score if they get an answer correct, or subtracts points if they get
 * an answer wrong. 
 * @author sterling
 *
 */
public class AnswerButtonListener implements OnClickListener {

	private AsyncTQHelper questionHelper;
	private ButtonType buttonType;
		
	public AnswerButtonListener(AsyncTQHelper questionHelper, ButtonType buttonType){
		this.buttonType = buttonType;
		this.questionHelper = questionHelper;
	}

	@Override
	public void onClick(View v) {
		
		switch(buttonType){
			case DISTRACTOR:			
				PointHelper.subPoints();
				questionHelper.updateQuestions();
				
				break;
			case ANSWER:
				PointHelper.addPoints();
				questionHelper.updateQuestions();
				break;
		}

	}

}
