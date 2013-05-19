package pl.edu.agh.sigmobapp;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.ApiKey;
import pl.edu.agh.sigmobapp.utils.Config;
import pl.edu.agh.sigmobapp.utils.SigmobProperties;

import com.example.sigmobapp.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private final static String version = "v.0.6.1 (2013/05/19 23:29)";
	
	private EditText inputName;
	private EditText inputPassword;
	
	private SigmobProperties sigmobProperties;
	private String propertiesFile = "settings_file";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Config.context = this;
		
		setContentView(R.layout.activity_login);
		sigmobProperties = SigmobProperties.getInstance();
		
		loadProperties();
		
		EditText hostIP = (EditText) findViewById(R.id.hostIP);
        hostIP.setText(sigmobProperties.getHostName());
        
		TextView versionText = (TextView) findViewById(R.id.appVersionTextView);
		versionText.setText(version);
		
		inputName = (EditText) findViewById(R.id.name);
		inputPassword = (EditText) findViewById(R.id.password);
        Button btnNextScreen = (Button) findViewById(R.id.btnNextScreen);
        
        
        //Listening to button event - move to method this.initListeners
        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	LongOperation longOperation = new LongOperation();
            	longOperation.execute("test");
            }
        });
        
        
        Button btnPreferences = (Button) findViewById(R.id.btnPreferences);
        btnPreferences.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	hideAllLayouts();
            	LinearLayout preferencesLayout = (LinearLayout) findViewById(R.id.preferencesLayout);
            	preferencesLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });
        
        
        Button btnPreferencesSave = (Button) findViewById(R.id.btnPreferencesSave);
        btnPreferencesSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	EditText hostIP = (EditText) findViewById(R.id.hostIP);
            	
            	String hostIPString = hostIP.getText().toString(); //.replaceAll("\\W", "");
            	sigmobProperties.setHostName(hostIPString);

            	FileOutputStream fos;
				try {
					Properties properties = new Properties();
					properties.setProperty("hostIP", hostIPString);
					fos = openFileOutput(propertiesFile, Context.MODE_PRIVATE); //Context.MODE_PRIVATE); MODE_WORLD_READABLE
					properties.storeToXML(fos, "properties");
	            	fos.close();
				} catch (FileNotFoundException e) {
					Log.e("n", "" + e);
				} catch (IOException e) {
					Log.e("n", "" + e);
				}
            	
            	
            	hideAllLayouts();
            	LinearLayout menuLayout = (LinearLayout) findViewById(R.id.my_root);
            	menuLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });
		
	}

	protected void hideAllLayouts() {
		LinearLayout menuLayout = (LinearLayout) findViewById(R.id.my_root);
    	menuLayout.setVisibility(LinearLayout.GONE);
    	LinearLayout messageLayout = (LinearLayout) findViewById(R.id.preferencesLayout);
    	messageLayout.setVisibility(LinearLayout.GONE);
		
	}

	
	private void loadProperties() {
    	FileInputStream fis;
		try {
			Properties properties = new Properties();
			fis = openFileInput(propertiesFile);
			properties.loadFromXML(fis);
			
			String hostName = properties.getProperty("hostIP", "http://176.31.202.49:7777");
			
			sigmobProperties.setHostName(hostName);
			sigmobProperties.setApiName("/sigmob/clientapi");
        	fis.close();
		} catch (FileNotFoundException e) {
			sigmobProperties.setHostName("http://176.31.202.49:7777");
			sigmobProperties.setApiName("/sigmob/clientapi");
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
	


	private class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			RestCommunication restCommunication = new RestCommunication();
			ObjectMapper objectMapper = new ObjectMapper();
			String user = inputName.getText().toString();
			String password = inputPassword.getText().toString();

			String loginAdress = sigmobProperties.getHostAndApi() + "/login";
			
			JSONObject responseJSON = restCommunication.doGetApiKey(
					loginAdress, user, password);
			ApiKey myApi = null;

			String returnString = "";
			try {
				if (responseJSON == null) {
					Log.e("n", "responseJSON null ");
					return "Connection error.";
				}
				myApi = objectMapper.readValue(responseJSON.toString(),
						ApiKey.class);
				Log.d("n", "" + responseJSON.toString());
			} catch (JsonParseException e) {
				Log.e("n", "" + e);
			} catch (JsonMappingException e) {
				Log.e("n", "" + e);
			} catch (IOException e) {
				Log.e("n", "" + e);
			}
			
			
			if (myApi == null) {
				returnString = "Login incorrect.";
			} else {
				returnString = "";
				Intent nextScreen = new Intent(getApplicationContext(),
						MenuActivity.class);
				nextScreen.putExtra("apikey", myApi.getApikey());
				startActivity(nextScreen);
			}
			
			
			return returnString;
		}

		@Override
		protected void onPostExecute(String result) {
			TextView txt = (TextView) findViewById(R.id.messagesTextView);
			txt.setText(result);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}


}
