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

public class SurveyActivity extends Activity {
	private String propertiesFile = "settings_file";
	private String hostName; // = "http://176.31.202.49:7777";
	private String apiName = "/sigmob/clientapi";
	
	private String apikey;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
 
        loadProperties();
        
        Intent i = getIntent();
        apikey = i.getStringExtra("apikey");
        
        TextView surveysListTitle = (TextView) findViewById(R.id.surveysListTitle);
        surveysListTitle.setText("Surveys list");
        
        Button btnCloseSurveysList = (Button) findViewById(R.id.btnCloseSurveysList);
        btnCloseSurveysList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
                // TODO: open activity 2
            }
        });
        
        //TODO:
        // 1. pobranie listy z serwera
        RestCommunication restCommunication = new RestCommunication();
        JSONObject responseJSON = restCommunication.doGet(hostName + apiName + "/tasks", apikey);
//        Log.e("n", "response: " + responseJSON.toString());
        
        
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
    			Number surveyId = taskShort.getId();
    			
    			if (type.equalsIgnoreCase("SURVEY")){
    				taskNumber++;
    				Button button = new Button(getApplicationContext());
    				button.setText("survey " + taskNumber);
    				button.setTag((Integer) surveyId );
    				// TODO add listener
    				button.setOnClickListener(new View.OnClickListener() {
    		            public void onClick(View view) {
    		            	Integer surveyId = (Integer)view.getTag();

    		            	setSurvey(surveyId);
    		            	
    		            	hideAllLayouts();
    		            	LinearLayout surveyLayout = (LinearLayout) findViewById(R.id.surveyLayout);
    		            	surveyLayout.setVisibility(LinearLayout.VISIBLE);
    		            }
    		        });
    				surveysListLayout.addView(button);
    			}
    		}
    		
    	} else {
    		surveysListTitle.setText("Surveys list empty");
    	}
        
	}
	
	
	private void setSurvey(Integer surveyId) {
		// TODO Auto-generated method stub
		Log.e("n", "surveyId: " + surveyId);
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
    	LinearLayout surveysListLayout = (LinearLayout) findViewById(R.id.surveysListLayout);
    	surveysListLayout.setVisibility(LinearLayout.GONE);
    	LinearLayout surveyLayout = (LinearLayout) findViewById(R.id.surveyLayout);
    	surveyLayout.setVisibility(LinearLayout.GONE);
    }
}
