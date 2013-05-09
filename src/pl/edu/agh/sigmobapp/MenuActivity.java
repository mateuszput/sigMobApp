package pl.edu.agh.sigmobapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.ApiKey;

import com.example.sigmobapp.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
 

public class MenuActivity extends Activity {
	private String hostName; // = "http://176.31.202.49:7777";
	private String apiName = "/sigmob/clientapi";
	
	private String apikey;
	private String propertiesFile = "settings_file";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
 
//        TextView txtName = (TextView) findViewById(R.id.txtName);
 
        Intent i = getIntent();
        // Receiving the Data
//        String name = i.getStringExtra("name");
        apikey = i.getStringExtra("apikey");
 
        // Displaying Received data
//        txtName.setText("Your login: " + name);
 
        loadProperties();
        EditText hostIP = (EditText) findViewById(R.id.hostIP);
        hostIP.setText(hostName);
        
        
        // TODO: in future move to method
        // Binding Click event to Button
        Button btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });
        
        Button btnMesssage = (Button) findViewById(R.id.btnMesssage);
        btnMesssage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	hideAllLayouts();
            	LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
            	messageLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });
        
        Button btnMessageSend = (Button) findViewById(R.id.btnMessageSend);
        btnMessageSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	
            	RestCommunication restCommunication = new RestCommunication();
            	
            	EditText messageTitle = (EditText) findViewById(R.id.messageTitle);
            	EditText messageBody = (EditText) findViewById(R.id.messageBody);
            	
            	String titleString = messageTitle.getText().toString().replaceAll("\\W", "");
            	String messageString = messageBody.getText().toString().replaceAll("\\W", "");
				if (titleString != "" && messageString != "") {
					
					String jsonToSend = "{\"title\": \"" + titleString 
							+ "\", \"body\": \"" + messageString
							+ "\", \"attachments\": []}";

					JSONObject responseJSON = restCommunication.doPost(hostName
							+ apiName + "/messages", apikey, jsonToSend);
					Log.e("n", "response: " + responseJSON.toString());
				}
				messageTitle.setText("");
				messageBody.setText("");
            	hideAllLayouts();
            	LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
            	menuLayout.setVisibility(LinearLayout.VISIBLE);
            	
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
            	hostName = hostIPString;
            	

            	FileOutputStream fos;
				try {
					Properties properties = new Properties();
					properties.setProperty("hostIP", hostIPString);
					fos = openFileOutput(propertiesFile, Context.MODE_WORLD_READABLE); //Context.MODE_PRIVATE);
					properties.storeToXML(fos, "properties");
	            	fos.close();
				} catch (FileNotFoundException e) {
					Log.e("n", "" + e);
				} catch (IOException e) {
					Log.e("n", "" + e);
				}
            	
            	
            	
            	hideAllLayouts();
            	LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
            	menuLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });
        
        
        Button btnSurveysList = (Button) findViewById(R.id.btnSurveysList);
        btnSurveysList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	Intent nextScreen = new Intent(getApplicationContext(), SListActivity.class);
               	nextScreen.putExtra("apikey", apikey);
                startActivity(nextScreen);
                finish();
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


	private void hideAllLayouts(){
    	LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
    	menuLayout.setVisibility(LinearLayout.GONE);
    	LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
    	messageLayout.setVisibility(LinearLayout.GONE);
    	LinearLayout preferencesLayout = (LinearLayout) findViewById(R.id.preferencesLayout);
    	preferencesLayout.setVisibility(LinearLayout.GONE);
    }
}