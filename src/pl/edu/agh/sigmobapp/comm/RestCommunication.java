package pl.edu.agh.sigmobapp.comm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

	
	public JSONObject doPostFile(String stringURL, String apiKey, String filePath, File fileHandler) {
		JSONObject json = null;
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			FileInputStream fileInputStream = new FileInputStream(fileHandler);
			
			URL url = new URL(stringURL);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Authorization", "apikey=" + apiKey);
			
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			
//			uploadedfile
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
							+ filePath + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			// Responses from the server (code and message)
			/*
			int serverResponseCode = connection.getResponseCode();
			Log.e("n", "Log 3 rc: " + serverResponseCode);
			String serverResponseMessage = connection.getResponseMessage();
			Log.e("n", "Log 3 rm: " + serverResponseMessage);
			*/
			
			
			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
			
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

		} catch (Exception e) {
			Log.e("n", "" + e);
		}

		return json;
	}
	
	
	public void doPostNoAnswer(String stringURL, String apiKey, String jsonToSend) {
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
			connection.setReadTimeout(10000);
			connection.connect();
			
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
	        wr.write(jsonToSend);
	        wr.flush();
	        wr.close();
	        
	        int serverResponseCode = connection.getResponseCode();
	        Log.d("n", "response code: " + serverResponseCode);
	        
		} catch (MalformedURLException e) {
			Log.e("n", "" + e);
		} catch (ProtocolException e) {
			Log.e("n", "" + e);
		} catch (IOException e) {
			Log.e("n", "" + e);
		}
	}
	
	
	
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
			Log.e("n", "" + e);
		} catch (ProtocolException e) {
			Log.e("n", "" + e);
		} catch (IOException e) {
			Log.e("n", "" + e);
		} catch (JSONException e) {
			Log.e("n", "" + e);
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
			
			connection.setRequestMethod("POST");
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
			Log.e("n", "" + e);
		} catch (ProtocolException e) {
			Log.e("n", "" + e);
		} catch (IOException e) {
			Log.e("n", "" + e);
		} catch (JSONException e) {
			Log.e("n", "" + e);
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
			
			//connection.setDoOutput(true); z tym nie dzia�a na Android 4.*
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
			Log.e("n", "" + e);
		} catch (ProtocolException e) {
			Log.e("n", "" + e);
		} catch (IOException e) {
			Log.e("n", "" + e);
		} catch (JSONException e) {
			Log.e("n", "" + e);
		}

		return json;
	}
	
}
