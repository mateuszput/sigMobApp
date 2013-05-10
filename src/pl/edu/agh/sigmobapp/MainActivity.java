package pl.edu.agh.sigmobapp;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.ApiKey;

import com.example.sigmobapp.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String version = "v.0.3.1 (2013/05/10 20:44)";
	
	private EditText inputName;
	private EditText inputPassword;
	
	private String hostName = "http://176.31.202.49:7777";
	private String apiName = "/sigmob/clientapi";
	
	private String propertiesFile = "settings_file";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loadProperties();
		
		TextView versionText = (TextView) findViewById(R.id.appVersionTextView);
		versionText.setText(version);
		
		inputName = (EditText) findViewById(R.id.name);
		inputPassword = (EditText) findViewById(R.id.password);
        Button btnNextScreen = (Button) findViewById(R.id.btnNextScreen);
        
        
        //Listening to button event - move to method this.initListeners
        btnNextScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
            	RestCommunication restCommunication = new RestCommunication();
            	ObjectMapper objectMapper = new ObjectMapper();
            	String user = inputName.getText().toString();
            	String password = inputPassword.getText().toString(); 
            	
            	String loginAdress = hostName + apiName + "/login";
            	JSONObject responseJSON = restCommunication.doGetApiKey(loginAdress, user, password);
            	ApiKey myApi = null;
            	
            	try {
            		if (responseJSON == null) {
            			Log.e("n", "responseJSON null ");
            			return;
            		}
					myApi = objectMapper.readValue(responseJSON.toString(), ApiKey.class);
            		
				} catch (JsonParseException e) {
					Log.e("n", "parse exeption");
				} catch (JsonMappingException e) {
					Log.e("n", "jsonmapping exeption");
				} catch (IOException e) {
					Log.e("n", "io exeption");
				}
	        	
            	TextView messagesTextView = (TextView) findViewById(R.id.messagesTextView);
            	if(myApi == null){
            		messagesTextView.setText("Login incorrect.");
            	} else {
            		messagesTextView.setText("");
            		Intent nextScreen = new Intent(getApplicationContext(), MenuActivity.class);
                	nextScreen.putExtra("apikey", myApi.getApikey());
                	startActivity(nextScreen);
            	}
            }
        });
		
	}

	private void loadProperties() {
    	FileInputStream fis;
		try {
			Properties properties = new Properties();
			fis = openFileInput(propertiesFile);
			properties.loadFromXML(fis);
			hostName = properties.getProperty("hostIP", "http://176.31.202.49:7777");
        	fis.close();
		} catch (FileNotFoundException e) {
			Log.e("n", "" + e);
		} catch (IOException e) {
			Log.e("n", "" + e);
		}
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
}
