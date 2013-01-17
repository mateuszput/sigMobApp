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

import android.widget.TextView;

public class RestCommunication {

	private TextView errorView;
	
	
	public void doPost(String stringURL, String jsonToSend) {
//		JSONObject json = null;
		HttpURLConnection connection = null;

		URL url;
		try {
			url = new URL(stringURL);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Authorization", "alwaysTheSameData");
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(10000);
			connection.connect();

			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
	        wr.write(jsonToSend);
	        wr.flush();
	        wr.close();
			
	        
	        InputStream is = connection.getInputStream();
	        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	        String line;
	        StringBuffer response = new StringBuffer(); 
	        while((line = rd.readLine()) != null) {
	          response.append(line);
	          response.append('\r');
	        }
	        rd.close();
	        errorView.setText("Response: " + response);

		} catch (MalformedURLException e) {
			errorView.setText(e.toString());
		} catch (ProtocolException e) {
			errorView.setText(e.toString());
		} catch (IOException e) {
			errorView.setText(e.toString());
		}

	}
	
	
	
	public JSONObject doGet(String stringURL) {
		JSONObject json = null;
		HttpURLConnection connection = null;

		URL url;
		try {
			url = new URL(stringURL);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Authorization", "alwaysTheSameData");
			
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

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

	public void setErrorView(TextView errorView) {
		this.errorView = errorView;
	}
}
