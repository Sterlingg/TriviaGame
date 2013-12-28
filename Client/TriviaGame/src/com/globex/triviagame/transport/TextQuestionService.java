package com.globex.triviagame.transport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.globex.triviagame.datatypes.TextQuestion;

/**
 * 
 * @author sterling
 * Adapted from http://stackoverflow.com/questions/3197335/restful-api-service
 * http://www.techrepublic.com/blog/app-builder/calling-restful-services-from-your-android-app/1076
 */
public class TextQuestionService extends IntentService{

	private static final String HOST = "http://10.0.2.2:9090";
	//private static final String REAL_HOST = "http://2.guestbook5696.appspot.com";
	
	private static final int STATUS_RUNNING = 0;
	private static final int STATUS_FINISHED = 1;
	private static final int STATUS_ERROR = 2;
	private static final String URL = HOST;
	
	public TextQuestionService() {
		super("Text Question Service");
	}
		
	/**
	 * 
	 * getASCIIContentFromEntity: Gets the string out of an entity.
	 * @param entity
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);
			if (n>0) out.append(new String(b, 0, n));
			}
			return out.toString();
			}

	/**
	 * 
	 * getBinContentFromEntity: Gets the string out of an entity.
	 * @param entity
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
		protected ByteArrayOutputStream getBinContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int n = 1;
			while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);
			if (n>0) out.write(b, 0, n);
			}
			return out;
			}

	/**
	 * getQuestions: Retrieves questions from the external database.
	 * GET: /getquestion	 
	 * */
	private JSONArray getQuestions(Intent intent){
		
		Log.v("TextQuestionService","getQuestions()");
		
		String category= intent.getStringExtra("category");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		// Filter by category.
		HttpGet httpGet = new HttpGet(URL + "/getquestion" + "?category=" + category);
		
		String questions;
		
		try{
			HttpResponse response = httpClient.execute(httpGet,httpContext);
			HttpEntity entity = response.getEntity();
			ByteArrayOutputStream b = getBinContentFromEntity(entity);
			questions = new String(b.toByteArray());
			
			return new JSONArray(questions);
		}
		catch (Exception e){
			//TODO: Add popup when not connected to internet / failure here.
		Log.v("TextQuestionService","Caught exception" + e);
		return null;
		}
		
	}

	/**
	 * onHandleIntent: Gets a question from the server and returns it to the
	 * 	               calling activity.
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 **/
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v("TextQuestionService","Handling intent");
		
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		String command = intent.getStringExtra("command");
		
		Bundle b = new Bundle();

		if(command.equals("getquestions")){
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);

			// Get the data from the server.
			try{				
				JSONArray questions = getQuestions(intent);
				ArrayList<TextQuestion> results = parseJSONtoTQ(questions);
				Log.v("TextQuestionService",results.get(1).toString());
				b.putParcelableArrayList("results", results);
				receiver.send(STATUS_FINISHED, b);
			}
			catch(Exception e){
				//TODO: Handle this..
			    b.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, b);
			}
		}
		this.stopSelf();
	}
	
	/**
	 * parseJSONtoTQ: Parses the given JSONArray into a list of TextQuestions.
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	private ArrayList<TextQuestion> parseJSONtoTQ(JSONArray data) throws JSONException {
		//TODO: Appropriately handle JSONException.
		JSONObject curr= null;
		TextQuestion aQuestion = null;
		ArrayList<TextQuestion> result = new ArrayList<TextQuestion>();
		
		for (int i = 0; i < data.length(); i++){
			curr= data.getJSONObject(i);
			aQuestion = new TextQuestion(curr.getString("question"),
									curr.getString("category"),
									curr.getString("distractorA"),
									curr.getString("distractorB"),
									curr.getString("distractorC"),
									curr.getString("answer"));
			result.add(aQuestion);
		}
		
		return result;
	}
	
	

}



