package com.globex.triviagame.transport;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
/**
 * 
 * TextQuestionResultReceiver: Used in creating a callback for the HTTP GET request for 
 * 						   retrieving questions from the external database.
 * @author sterling
 *
 **/
public class ResultReceiverImpl extends ResultReceiver {
	
	private Receiver aReceiver;
	
	public ResultReceiverImpl(Handler handler) {
		super(handler);		
	}

	public Receiver getReceiver() {
		return aReceiver;
	}

	public void setReceiver(Receiver aReceiver) {
		this.aReceiver = aReceiver;
	}

	public interface Receiver{		
		public void onReceiveResult(int result, Bundle resultData);	
	}
	
	  @Override
	    protected void onReceiveResult(int resultCode, Bundle resultData) {
	        if (aReceiver != null) {
	            aReceiver.onReceiveResult(resultCode, resultData);
	        }
	    }
	
}
