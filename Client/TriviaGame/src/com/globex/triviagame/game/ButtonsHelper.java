package com.globex.triviagame.game;

import java.util.ArrayList;

import android.widget.Button;

import com.globex.triviagame.R;
import com.globex.triviagame.listeners.AnswerButtonListener;

/**
 * ButtonsHelper: Helper class for GameActivity to handle setting of which buttons are correct
 * 				  or incorrect answers. It also handles the disabling/enabling of buttons.
 * 			
 * 				  Depends on QuestionHelper and PointHelper through the listeners.
 * 					 
 * @author sterling
 *
 */
public class ButtonsHelper {
	
	private GameActivity game;
	private PointHelper pointHelper;
	
	private ArrayList<Button> answerButtons;
	private AnswerButtonListener distractorHandler;
	private AnswerButtonListener answerHandler;
	
	public ButtonsHelper(GameActivity game, PointHelper pointHelper){
		this.game = game;
		this.pointHelper = pointHelper;
		
		answerButtons = new ArrayList<Button>();
		
		// Disable buttons while the first set of questions is retrieved.
		disableButtons();
	}
	
	/**
	 * init_answer_buttons: Gets the answer buttons from the layout.
	 */
	public void initAnswerButtons(AsyncTQHelper questionHelper){
		answerButtons.add((Button) game.findViewById(R.id.answerbtn1));enableButtons();
		answerButtons.add((Button) game.findViewById(R.id.answerbtn2));
		answerButtons.add((Button) game.findViewById(R.id.answerbtn3));
		answerButtons.add((Button) game.findViewById(R.id.answerbtn4));
		
		// Set all of the click handlers to updateQuestions.
		distractorHandler = new AnswerButtonListener(pointHelper, questionHelper, ButtonType.DISTRACTOR);
		answerHandler = new AnswerButtonListener(pointHelper, questionHelper, ButtonType.ANSWER);
		
		//First 3 indices are fake buttons
		for(int i = 0; i < answerButtons.size() - 1; i++)
			answerButtons.get(i).setOnClickListener(distractorHandler);

		//Last indice is the answer button, so handle it differently.
		answerButtons.get(answerButtons.size() - 1).setOnClickListener(answerHandler);		
	}
		
	/**
	 * disableButtons: Disables all of the answer buttons.
	 */
	public void disableButtons(){
		for(Button b : answerButtons){
			b.setEnabled(false);
		}		
	}
	
	public ArrayList<Button> getAnswerButtons() {
		return answerButtons;
	}

	/**
	 * enableButtons: Enables all of the answer buttons.
	 */
	public void enableButtons(){
		for(Button b : answerButtons){
			b.setEnabled(true);
		}		
	}
	
	/**
	 * updateAnswerClickListeners: Updates the clicks listeners after distractor/answer buttons are shuffled.
	 */
	public void updateAnswerClickListeners(){
		//First 3 indices are fake buttons
				for(int i = 0; i < answerButtons.size() - 1; i++)
					answerButtons.get(i).setOnClickListener(distractorHandler);
				answerButtons.get(answerButtons.size() - 1).setOnClickListener(answerHandler);
	}
	
}
