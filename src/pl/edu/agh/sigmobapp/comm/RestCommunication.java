package pl.edu.agh.sigmobapp.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.TextView;

public class RestCommunication {

//	private TextView errorView;
	
	
	public JSONObject doPost(String stringURL, String apiKey, String jsonToSend) {
		JSONObject json = null;
		HttpURLConnection connection = null;

		URL url;
		try {
			url = new URL(stringURL);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Authorization", "apikey=" + apiKey);
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(10000);
			connection.connect();
			Log.e("n", "Connect");
			
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
	        wr.write(jsonToSend);
	        wr.flush();
	        wr.close();
	        Log.e("n", "output send");
	        
			InputStream is = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			bufferedReader.close();

			json = new JSONObject(response.toString());
			is.close();

			Log.e("n", "It is ok");
	        
		} catch (MalformedURLException e) {
			Log.e("n", "MalformedURLException");
		} catch (ProtocolException e) {
			Log.e("n", "ProtocolException");
		} catch (IOException e) {
			Log.e("n", "IOException" + e);
		} catch (JSONException e) {
			Log.e("n", "JSONException");
		}
		return json;
	}
	
	
	public JSONObject doGetApiKey(String stringURL, String user, String password) {
		JSONObject json = null;
		HttpURLConnection connection = null;

		URL url;
		try {
			Log.e("n", "url");
			url = new URL(stringURL);
			
			connection = (HttpURLConnection) url.openConnection();
			Log.e("n", "After Connected");
			
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			
			connection.connect();
			Log.e("n", "Connected");
			
			String jsonToSend = "{\"username\":\""+ user +  "\", \"password\":\"" + password + "\"}";
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
	        wr.write(jsonToSend);
	        wr.flush();
	        wr.close();
	        Log.e("n", "send");

			// String result= convertStreamToString(instream);
			InputStream is = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			bufferedReader.close();

			json = new JSONObject(response.toString());
			is.close();

			Log.e("n", "It is ok");
		} catch (MalformedURLException e) {
			Log.e("n", "MalformedURLException");
//			e.printStackTrace();
		} catch (ProtocolException e) {
			Log.e("n", "ProtocolException");
//			e.printStackTrace();
		} catch (IOException e) {
			Log.e("n", "IOException");
//			e.printStackTrace();
		} catch (JSONException e) {
			Log.e("n", "JSONException");
//			e.printStackTrace();
		}

		return json;
	}
	
	
	// TODO: add apikey to authorization
	public JSONObject doGet(String stringURL) {
		JSONObject json = null;
		HttpURLConnection connection = null;

		// gdzie wysylamy?
		URL url;
		try {
			url = new URL(stringURL);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
//			connection.setRequestProperty("Authorization", "alwaysTheSameData");
//			connection.setRequestProperty("Authorization", "mapu_apikey");
			
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			connection.connect();

			// String result= convertStreamToString(instream);
			InputStream is = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			bufferedReader.close();

			json = new JSONObject(response.toString());
			is.close();

			Log.e("n", "It is ok");
		} catch (MalformedURLException e) {
			Log.e("n", "MalformedURLException");
		} catch (ProtocolException e) {
			Log.e("n", "ProtocolException");
		} catch (IOException e) {
			Log.e("n", "IOException");
		} catch (JSONException e) {
			Log.e("n", "JSONException");
		}

		return json;
	}

//	public void setErrorView(TextView errorView) {
//		this.errorView = errorView;
//	}
}
