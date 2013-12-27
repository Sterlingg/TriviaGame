package com.globex.triviagame.transport;

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

import com.globex.triviagame.datatypes.Category;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * 
 * @author sterling
 * Adapted from http://stackoverflow.com/questions/3197335/restful-api-service
 * http://www.techrepublic.com/blog/app-builder/calling-restful-services-from-your-android-app/1076
 */
public class CategoryService extends IntentService{

	//private static final String LOCAL_HOST = "http://10.0.2.2:8080";
	private static final String REAL_HOST = "http://2.guestbook5696.appspot.com";
	private static final int STATUS_RUNNING = 0;
	private static final int STATUS_FINISHED = 1;
	private static final int STATUS_ERROR = 2;
	private static final String URL = REAL_HOST;

	public CategoryService() {
		super("Category Service");
	}
		
	/**
	 * onHandleIntent: Gets a question from the server and returns it to the
	 * 	               calling activity.
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 **/
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v("CategoryService","Handling intent");
		
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		String command = intent.getStringExtra("command");
		Bundle b = new Bundle();

		if(command.equals("getcategories")){
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);

			// Get the data from the server.
			try{
				JSONArray categories= getCategories();
				ArrayList<Category> results = parseJSONtoCat(categories);
				Log.v("CategoryService",results.get(1).toString());
				b.putString("responseType", "Categories");
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
	private ArrayList<Category> parseJSONtoCat(JSONArray data) throws JSONException {
		//TODO: Appropriately handle JSONException.
		JSONObject curr= null;
		Category aCategory = null;
		ArrayList<Category> result = new ArrayList<Category>();
		
		for (int i = 0; i < data.length(); i++){
			curr= data.getJSONObject(i);
			aCategory = new Category(curr.getString("category"));
			result.add(aCategory);
		}
		
		return result;
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
	 * getQuestions: Retrieves questions from the external database.
	 * GET: /getquestion	 
	 * */
	private JSONArray getCategories(){
		
		Log.v("CategoryService","getCategories()");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(URL + "/getcategory");
			
		try{
			final HttpResponse response = httpClient.execute(httpGet,httpContext);
			final HttpEntity entity = response.getEntity();
			//Decrypt the encrypted JSON response, so the user can't just grab the answers from
			//the HTTP response... Although they can just get this string from a memory dump.
			String categories = getASCIIContentFromEntity(entity);
			Log.v("CategoryService","CATEGORIRES ===" +categories);
			return new JSONArray(categories);
		}
		catch (Exception e){
			//TODO: Add popup when not connected to internet / failure here.
		Log.v("CategoryService","Caught exception" + e);
		return null;
		}
		
	}
}
