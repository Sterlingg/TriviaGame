package com.globex.triviagame.game;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.globex.triviagame.R;
import com.globex.triviagame.datatypes.TextQuestion;
import com.globex.triviagame.transport.ResultReceiverImpl;
import com.globex.triviagame.transport.TextQuestionService;

public class AsyncTQHelper implements ResultReceiverImpl.Receiver{

	private GameActivity game;
	private ResultReceiverImpl receiver;

	// Used to tell if the timer should be started.
	private boolean isStartGame = true;

	// Used to tell if the list should be swapped for the new list immediately after the service returns.
	// or if it should be stored in tempQuestions until the user finishes all of the questions.
	private boolean useNewListNow = true;

	// Used to determine if we need to fetch more questions from the server.	
	private int currentQIndex = 0;
	// The list of questions currently being used.
	private ArrayList<TextQuestion> questions = new ArrayList<TextQuestion>();
	// Used to get a new list of questions while the user is still answering questions.
	private ArrayList<TextQuestion> tempQuestions = new ArrayList<TextQuestion>();

	// Keeps track of the progress of the question service.
	private int serviceStatus;

	private static final int 	STATUS_RUNNING = 0;
	private static final int 	STATUS_FINISHED = 1;
	private static final int 	STATUS_ERROR = 2;

	private ButtonsHelper buttonsHelper;
	private TimerHelper timerHelper;

	/**
	 * AsyncTQHandler:
	 * @param game
	 */
	public AsyncTQHelper(GameActivity game, TimerHelper timerHelper, ButtonsHelper buttonsHelper){
		this.game = game;
		this.timerHelper = timerHelper;
		this.buttonsHelper = buttonsHelper;
	}
	
	/**
	 * startWebService: Start a service that retrieves the questions from the
	 * db.
	 */
	public void startWebService() {
		receiver = new ResultReceiverImpl(new Handler());
		receiver.setReceiver(this);
		final Intent intent = new Intent(Intent.ACTION_SYNC, null, game,
				TextQuestionService.class);
		intent.putExtra("receiver", receiver);
		intent.putExtra("command", "getquestions");
		// Get the selected category.
		intent.putExtra("category", game.getIntent().getExtras().getString("category"));
		game.startService(intent);
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
			Log.v("GameActivity", "RUNNING");
			break;
		case STATUS_FINISHED:									
			// When the service is initially called there is no 'queued up' questions waiting to be answered
			// before the questions need to be updated. Start the timer when the service is finished.
			if(isStartGame == true){
				resetQIndex();
				questions = resultData.getParcelableArrayList("results");
				timerHelper.startTimer();
				isStartGame=false;
				updateQuestions();
			}
			// The newList should be cached until it needs to be used.
			else if (useNewListNow == false)
			{
				tempQuestions = resultData.getParcelableArrayList("results");
			}
			else if (useNewListNow == true)
			{
				questions = resultData.getParcelableArrayList("results");
				updateQuestions();
			}

			serviceStatus = STATUS_FINISHED;
			Log.v("GameActivity", "FINISHED");
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


	/**
	 * updateQuestions: Sets the text fields with a new question from the retrieved
	 * 				 list of random questions.
	 */
	public void updateQuestions() {
		ArrayList<Button> answerButtons = buttonsHelper.getAnswerButtons();

		// Buttons always start disabled.
		if(currentQIndex == 0){
			buttonsHelper.enableButtons();		
		}

		TextView question = (TextView) game.findViewById(R.id.question);

		// How many questions from the end of the list the service will start running at.
		final int SERVICE_START_SIZE = questions.size() - 10;

		// Randomize which button gets the answer.
		Collections.shuffle(answerButtons);
		buttonsHelper.updateAnswerClickListeners();

		// Start getting a new list of questions some time before the end of the current question list.
		if(currentQIndex == SERVICE_START_SIZE){
			startWebService();			
		}

		if (currentQIndex < questions.size()) {
			// Still using the old list, so cache webservice result in tempQuestions.
			useNewListNow = false;
			question.setText(questions.get(currentQIndex).getQuestion());
			answerButtons.get(0)
			.setText(questions.get(currentQIndex).getDistractorA());
			answerButtons.get(1)
			.setText(questions.get(currentQIndex).getDistractorB());
			answerButtons.get(2)
			.setText(questions.get(currentQIndex).getDistractorC());
			answerButtons.get(3)
			.setText(questions.get(currentQIndex).getAnswer());
			currentQIndex++;
		} 
		// Used up all the questions, so get some more 
		else
		{
			// The new list from the web service should be used immediately.
			useNewListNow = true;
			buttonsHelper.disableButtons();
			resetQIndex();

			// The service has finished, so it is fine to use the temp questions list. If it hasn't finished
			// updateQuestions() will be called when it does finish.
			if(isServiceFinished() == true){
				questions = tempQuestions;
				updateQuestions();
			}
		}
	}

	/**
	 * resetQIndex: Resets the index used for retrieving questions when a new list from the server is used.
	 */
	private void resetQIndex() {
		currentQIndex = 0;	
	}

}
