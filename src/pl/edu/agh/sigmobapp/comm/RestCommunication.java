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

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RestCommunication {

	
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
			
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
	        wr.write(jsonToSend);
	        wr.flush();
	        wr.close();
	        
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
			url = new URL(stringURL);
			
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			
			connection.connect();
			
			String jsonToSend = "{\"username\":\""+ user +  "\", \"password\":\"" + password + "\"}";
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
	        wr.write(jsonToSend);
	        wr.flush();
	        wr.close();

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
	
	
	public JSONObject doGet(String stringURL, String apiKey) {
		JSONObject json = null;
		HttpURLConnection connection = null;

		URL url;
		try {
			url = new URL(stringURL);
			
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Authorization", "apikey=" + apiKey);
			
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			connection.connect();

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
	
}
