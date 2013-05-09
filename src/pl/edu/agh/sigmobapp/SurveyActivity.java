package pl.edu.agh.sigmobapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.Answer;
import pl.edu.agh.sigmobapp.json.Question;
import pl.edu.agh.sigmobapp.json.Survey;
import pl.edu.agh.sigmobapp.json.TaskShort;

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
        
        String surveyId = i.getStringExtra("surveyId");
        
        Button btnCloseSurveysList = (Button) findViewById(R.id.btnCloseSurvey);
        btnCloseSurveysList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	Intent sListScreen = new Intent(getApplicationContext(), SListActivity.class);
            	sListScreen.putExtra("apikey", apikey);
            	finish();
            	startActivity(sListScreen);
            }
        });
        
        setSurveyDetails(surveyId);
        
	}
	
	
	private void setSurveyDetails(String surveyId) {
		RestCommunication restCommunication = new RestCommunication();
		JSONObject responseJSON = restCommunication.doGet(hostName + apiName
				+ "/task/" + surveyId + "/survey", apikey);
		
		ObjectMapper objectMapper = new ObjectMapper();
        Survey survey = null;
        try {
        	survey = objectMapper.readValue(responseJSON.toString(), Survey.class);
		} catch (JsonParseException e) {
			Log.e("n", "" + e);
		} catch (JsonMappingException e) {
			Log.e("n", "" + e);
		} catch (IOException e) {
			Log.e("n", "" + e);
		}
        
        TextView surveysListTitle = (TextView) findViewById(R.id.surveyTitle);
        surveysListTitle.setText(survey.getName());
        
        Iterator<Question> iterator = survey.getQuestions().iterator();
		Question question = null;
		int taskNumber = 0;
		
		// TODO - tutaj znowu zaczac
		// TODO:
		// 1. Tworzone pytania do tablicy
		// 2. Przycisk wysylajacy odpowiedz
		// 3. Pod przyciskiem przechodzimy przez wszystkie pytania z tablicy
		
//		"questionId": 2,
//        "question": "Inne pytanie",
//        "answers": [
//            {
//                "answerId": 3,
//                "answer": "Tak"
//            },
//            {
//                "answerId": 4,
//                "answer": "Nie"
//            }
//        ]
		while(iterator.hasNext()){
			question = iterator.next();
			question.getQuestion();
			// Wyswietlanie pytania - text
			List<Answer> answers = question.getAnswers();
			
			Iterator<Answer> answersAterator = answers.iterator();
			Answer answer = null;
			while(answersAterator.hasNext()){
				answer = answersAterator.next();
				// Wyswietlanie odpowiedzi - radio buttony
				// answerId, answer
			}
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
