package com.globex.triviagame.game;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.globex.triviagame.R;
import com.globex.triviagame.activities.GameActivity;
import com.globex.triviagame.datatypes.TextQuestion;
import com.globex.triviagame.transport.ResultReceiverImpl;
import com.globex.triviagame.transport.TextQuestionService;

public class QuestionHelper implements ResultReceiverImpl.Receiver{

	private static QuestionHelper instance = null;
	private GameActivity game;
	private ResultReceiverImpl receiver;

	private boolean getNewList = true;

	// Used to tell if the list should be swapped for the new list immediately after the service returns.
	// or if it should be stored in tempQuestions until the user finishes all of the questions.
	private boolean useNewListNow = true;

	// Used to determine if we need to fetch more questions from the server.	
	private int currentQIndex = 0;
	// The list of questions currently being used.
	private ArrayList<TextQuestion> oldQuestions = new ArrayList<TextQuestion>();
	// Used to get a new list of questions while the user is still answering questions.
	private ArrayList<TextQuestion> newQuestions = new ArrayList<TextQuestion>();

	// Keeps track of the progress of the question service.
	private int serviceStatus;

	private static final int 	STATUS_RUNNING = 0;
	private static final int 	STATUS_FINISHED = 1;
	private static final int 	STATUS_ERROR = 2;

	private ButtonsHelper buttonsHelper;
	private TimerHelper timerHelper;

	private Intent serviceIntent = null;

	private static final int NUM_Q_RECEIVED = 15;
	private static final int SERVICE_START_SIZE = NUM_Q_RECEIVED - 5;
	
	/**
	 * AsyncTQHandler:
	 * @param game
	 */
	private QuestionHelper(GameActivity game, TimerHelper timerHelper, ButtonsHelper buttonsHelper){
		this.game = game;
		this.timerHelper = timerHelper;
		this.buttonsHelper = buttonsHelper;
		this.receiver = new ResultReceiverImpl(new Handler());
		this.receiver.setReceiver(this);
	}
		
	public static QuestionHelper getInstance(GameActivity game, TimerHelper timerHelper, ButtonsHelper buttonsHelper){
		if(instance == null){
			return new QuestionHelper(game, timerHelper, buttonsHelper);
		}
		else{
			instance.setGame(game);
			return instance;
		}
	}
	
	/**
	 * onReceiveResult: When a response with a list of questions is received by
	 * 					the server this method is called. This is a receiver for the TextQuestionService.
	 *  
	 * The flow of getting questions looks like. 
	 * 	initial list -> answer x questions -> start service -> answer more questions -> end of list -> get questions from tempQuestions
	 *   									service running -> store result in tempQuestions 				
	 * 															                            
	 */
	//TODO: Use AsyncTask enum for statuses?
	@Override
	public void onReceiveResult(int result, Bundle resultData) {
		switch (result) {
		case STATUS_RUNNING:
			serviceStatus = STATUS_RUNNING;
			Log.v("AsyncTQHelper", "RUNNING");
			break;
		case STATUS_FINISHED:
			// When the service is initially called there are no 'queued up' questions waiting to be answered
			// before the questions need to be updated. Start the timer when the service is finished.
			if(getNewList == true){
				currentQIndex = 0;
				oldQuestions = resultData.getParcelableArrayList("results");
				timerHelper.startTimer();
				getNewList = false;
				updateQuestions();
			}
			// The newList should be cached until it needs to be used.
			else if (useNewListNow == false)
			{
				newQuestions = resultData.getParcelableArrayList("results");
			}
			else if (useNewListNow == true)
			{
				oldQuestions = resultData.getParcelableArrayList("results");
				updateQuestions();
			}
			
			buttonsHelper.enableButtons();
			serviceStatus = STATUS_FINISHED;
			Log.v("AsyncTQHelper", "FINISHED");
			break;
		case STATUS_ERROR:
			serviceStatus = STATUS_ERROR;
			Log.v("GameActivity", "ERROR");
			break;
		}
	}

	/**
	 * isServiceFinished: Checks whether the service has finished executing, or is still running.
	 */
	public boolean isServiceFinished(){	
		if(serviceStatus == STATUS_FINISHED)
			return true;
		else
			return false;
	}

	private void setGame(GameActivity game) {
		this.game = game;		
	}

	/**
	 * startWebService: Start a service that retrieves the questions from the
	 * db.
	 */
	public void startWebService() {

		serviceIntent = new Intent(Intent.ACTION_SYNC, null, game,
				TextQuestionService.class);
		serviceIntent.putExtra("receiver", receiver);
		serviceIntent.putExtra("command", "getquestions");
		// Get the selected category.
		serviceIntent.putExtra("category", game.getIntent().getExtras().getString("category"));
		serviceStatus = STATUS_RUNNING;
		game.startService(serviceIntent);
	}
	
	/**
	 * stopWebService: Stops the service that is fetching more text questions.
	 **/
	public void stopWebService(){
		game.stopService(serviceIntent);
	}

	/**
	 * updateQuestions: Sets the text fields with a new question from the retrieved
	 * 				 list of random questions.
	 */
	public void updateQuestions() {
		if(oldQuestions.size() < NUM_Q_RECEIVED){
			getNewList = true;
			Log.i("QuestionHelper", "Not full list received.");
			return;
		}
		
		// Randomize which button gets the answer.
		buttonsHelper.shuffleButtons();
		buttonsHelper.updateAnswerClickListeners();

		if(currentQIndex == SERVICE_START_SIZE){
				startWebService();
		}
		
		if (currentQIndex < NUM_Q_RECEIVED) {
			buttonsHelper.enableButtons();
			updateButtons();
			// Still using the old list, so cache webservice result in tempQuestions.
			useNewListNow = false;
		} 
		// Used up all the questions, so get some more 
		else
		{
			// The new list from the web service should be used immediately.
			//buttonsHelper.disableButtons();
			useNewListNow = true;
			currentQIndex = 0;
			oldQuestions = newQuestions;
		}
	}
	
	private void updateButtons(){
		TextView question = (TextView) game.findViewById(R.id.question);
		ArrayList<Button> answerButtons = buttonsHelper.getAnswerButtons();

		question.setText(oldQuestions.get(currentQIndex).getQuestion());
		answerButtons.get(0)
		.setText(oldQuestions.get(currentQIndex).getDistractorA());
		answerButtons.get(1)
		.setText(oldQuestions.get(currentQIndex).getDistractorB());
		answerButtons.get(2)
		.setText(oldQuestions.get(currentQIndex).getDistractorC());
		answerButtons.get(3)
		.setText(oldQuestions.get(currentQIndex).getAnswer());
		currentQIndex++;
	}

}
