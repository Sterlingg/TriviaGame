package com.globex.triviagame.game;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Application;
import android.widget.Button;

import com.globex.triviagame.R;
import com.globex.triviagame.activities.GameActivity;
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
public class ButtonsHelper extends Application{
	private static ButtonsHelper instance = null;
	
	private GameActivity game;
	
	private static ArrayList<Button> answerButtons;
	private AnswerButtonListener distractorHandler;
	private AnswerButtonListener answerHandler;
	
	private ButtonsHelper(GameActivity game){
		this.game = game;	
		answerButtons = new ArrayList<Button>();	
	}
	
	public static ButtonsHelper getInstance(GameActivity game){
		
		if(instance == null){
			instance = new ButtonsHelper(game);
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
	 * init_answer_buttons: Gets the answer buttons from the layout.
	 */
	public void initAnswerButtons(QuestionHelper questionHelper){
		answerButtons.clear();
		answerButtons.add((Button) game.findViewById(R.id.answerbtn1));
		answerButtons.add((Button) game.findViewById(R.id.answerbtn2));
		answerButtons.add((Button) game.findViewById(R.id.answerbtn3));
		answerButtons.add((Button) game.findViewById(R.id.answerbtn4));
		disableButtons();
		// Set all of the click handlers to updateQuestions.
		distractorHandler = new AnswerButtonListener(questionHelper, ButtonType.DISTRACTOR);
		answerHandler = new AnswerButtonListener(questionHelper, ButtonType.ANSWER);
		
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

	public void shuffleButtons() {
		Collections.shuffle(answerButtons);
	}
	
}
