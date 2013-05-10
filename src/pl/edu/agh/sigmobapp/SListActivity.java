package pl.edu.agh.sigmobapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.TaskShort;
import pl.edu.agh.sigmobapp.json.TasksList;

import com.example.sigmobapp.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SListActivity extends Activity {
	private String propertiesFile = "settings_file";
	private String hostName; // = "http://176.31.202.49:7777";
	private String apiName = "/sigmob/clientapi";
	
	private String apikey;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slist);
 
        loadProperties();
        
        Intent i = getIntent();
        apikey = i.getStringExtra("apikey");
        
        TextView surveysListTitle = (TextView) findViewById(R.id.surveysListTitle);
        surveysListTitle.setText("Surveys list");
        
        Button btnCloseSurveysList = (Button) findViewById(R.id.btnCloseSurveysList);
        btnCloseSurveysList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	Intent menuScreen = new Intent(getApplicationContext(), MenuActivity.class);
            	menuScreen.putExtra("apikey", apikey);
            	finish();
            	startActivity(menuScreen);
            }
        });
        
        RestCommunication restCommunication = new RestCommunication();
        JSONObject responseJSON = restCommunication.doGet(hostName + apiName + "/tasks", apikey);
        
        ObjectMapper objectMapper = new ObjectMapper();
        TasksList tasksList = null;
        try {
			tasksList = objectMapper.readValue(responseJSON.toString(), TasksList.class);
		} catch (JsonParseException e) {
			Log.e("n", "" + e);
		} catch (JsonMappingException e) {
			Log.e("n", "" + e);
		} catch (IOException e) {
			Log.e("n", "" + e);
		}
    	
        
    	if (tasksList != null && tasksList.getTasks().size() != 0){
    		LinearLayout surveysListLayout = (LinearLayout) findViewById(R.id.surveysListLayout);
    		
    		Iterator<TaskShort> iterator = tasksList.getTasks().iterator();
    		TaskShort taskShort = null;
    		int taskNumber = 0;
    		while(iterator.hasNext()){
    			taskShort = iterator.next();
    			String type = taskShort.getType();
    			Number taskId = taskShort.getId();
    			
    			if (type.equalsIgnoreCase("SURVEY")){
    				taskNumber++;
    				Button button = new Button(getApplicationContext());
    				button.setText("survey " + taskNumber);
    				button.setTag( taskId );
    				
    				button.setOnClickListener(new View.OnClickListener() {
    		            public void onClick(View view) {
    		            	Number taskId = (Number)view.getTag();

    		            	Intent surveyScreen = new Intent(getApplicationContext(), SurveyActivity.class);
    	                	surveyScreen.putExtra("apikey", apikey);
    	                	surveyScreen.putExtra("taskId", taskId.toString());
    	                	finish();
    	                	startActivity(surveyScreen);
    		            }
    		        });
    				surveysListLayout.addView(button);
    			}
    		}
    		
    	} else {
    		surveysListTitle.setText("Surveys list empty");
    	}
        
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
	
}
