package pl.edu.agh.sigmobapp;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.Answer;
import pl.edu.agh.sigmobapp.json.ChoosenAnswer;
import pl.edu.agh.sigmobapp.json.Question;
import pl.edu.agh.sigmobapp.json.Survey;
import pl.edu.agh.sigmobapp.json.SurveyAnswer;
import pl.edu.agh.sigmobapp.utils.SigmobProperties;
import pl.edu.agh.sigmobapp.utils.SurveyRadioTag;

import com.example.sigmobapp.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SurveyActivity extends Activity {
	private String apikey;
	
	private List<RadioGroup> radioGrupsList;
	private String taskId;
	private SigmobProperties sigmobProperties;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		StrictMode.ThreadPolicy policy = new StrictMode.
		ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
        
        setContentView(R.layout.activity_survey);
 
        sigmobProperties = SigmobProperties.getInstance();
        
        Intent i = getIntent();
        apikey = i.getStringExtra("apikey");
        
        String taskId = i.getStringExtra("taskId");
        
        Button btnCloseSurveysList = (Button) findViewById(R.id.btnCloseSurvey);
        btnCloseSurveysList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	Intent sListScreen = new Intent(getApplicationContext(), SListActivity.class);
            	sListScreen.putExtra("apikey", apikey);
            	finish();
            	startActivity(sListScreen);
            }
        });
        
        setSurveyDetails(taskId);
        
	}
	
	
	private void setSurveyDetails(String taskId) {
		this.taskId = taskId;
		
		RestCommunication restCommunication = new RestCommunication();
		JSONObject responseJSON = restCommunication.doGet(sigmobProperties.getHostAndApi()
				+ "/task/" + taskId + "/survey", apikey);
		
		radioGrupsList = new LinkedList<RadioGroup>();
		
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
        
        
        Iterator<Question> qmestionsIterator = survey.getQuestions().iterator();
		Question question = null;
		
		LinearLayout surveyLayout = (LinearLayout) findViewById(R.id.surveyLayout);
		TextView questionText = null;
		while(qmestionsIterator.hasNext()){
			question = qmestionsIterator.next();
			String questrionString = question.getQuestion();
			
			questionText = new TextView(getApplicationContext());
			questionText.setText(questrionString);
			questionText.setTextColor(Color.parseColor("#000000"));
			surveyLayout.addView(questionText);
			
			List<Answer> answers = question.getAnswers();
			
			List<RadioButton> radioButtonList = new LinkedList<RadioButton>();
			RadioGroup radioGroup = new RadioGroup(getApplicationContext()); 
			radioGroup.setOrientation(RadioGroup.VERTICAL);
			
			
			RadioButton radioButton = null;
			Iterator<Answer> answersIterator = answers.iterator();
			Answer answer = null;
			while(answersIterator.hasNext()){
				answer = answersIterator.next();
				
				radioButton = new RadioButton(getApplicationContext());
				radioGroup.addView(radioButton); //the RadioButtons are added to the radioGroup instead of the layout
				radioButton.setTextColor(Color.parseColor("#000000"));
		        radioButton.setChecked(true);
		        radioButton.setText(answer.getAnswer());
		        
		        SurveyRadioTag surveyRadioTag = new SurveyRadioTag(survey.getSurveyId(), question.getQuestionId(), answer.getId());
		        radioButton.setTag(surveyRadioTag);
				radioButtonList.add(radioButton);
			}
			radioGrupsList.add(radioGroup);
			surveyLayout.addView(radioGroup);//you add the whole RadioGroup to the layout
		}
		
		
	    
		Button btnSendSurvey = new Button(getApplicationContext());
		btnSendSurvey.setText("Send survey");
		
		btnSendSurvey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	sendAnswer();
            }
        });
		
		surveyLayout.addView(btnSendSurvey);
		
	}

	
	protected void sendAnswer() {
		Iterator<RadioGroup> radioGrupsIterator = radioGrupsList.iterator();
    	
    	SurveyAnswer surveyAnswer = new SurveyAnswer();
    	while(radioGrupsIterator.hasNext()){
    		int radioId = radioGrupsIterator.next().getCheckedRadioButtonId();
    		RadioButton checkedRadioButton = (RadioButton) findViewById(radioId);
    		SurveyRadioTag surveyRadioTag = (SurveyRadioTag) checkedRadioButton.getTag();
    		
    		ChoosenAnswer choosenAnswer = new ChoosenAnswer();
    		choosenAnswer.setAnswerId(surveyRadioTag.getAnswerId());
    		choosenAnswer.setQuestionId(surveyRadioTag.getQuestionId());
    		
    		surveyAnswer.setSurveyId(surveyRadioTag.getSurveyId());
    		
    		List<ChoosenAnswer> chosenAnswersList = surveyAnswer.getChosenAnswers();
    		if(chosenAnswersList == null) {
    			chosenAnswersList = new LinkedList<ChoosenAnswer>();
    			surveyAnswer.setChosenAnswers(chosenAnswersList);
    		}
    		
    		surveyAnswer.getChosenAnswers().add(choosenAnswer);
    		
    	}
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	try {
			String answer = objectMapper.writeValueAsString(surveyAnswer);
			RestCommunication restCommunication = new RestCommunication();
			restCommunication.doPost(sigmobProperties.getHostAndApi() + "/response/" + taskId + "/survey" , apikey, answer);
			
		} catch (JsonProcessingException e) {
			Log.e("n", "" + e);
		}
    	
    	Intent sListScreen = new Intent(getApplicationContext(), SListActivity.class);
    	sListScreen.putExtra("apikey", apikey);
    	finish();
    	startActivity(sListScreen);
	}


	@Override
	public void onBackPressed () {
		Intent sListScreen = new Intent(getApplicationContext(), SListActivity.class);
    	sListScreen.putExtra("apikey", apikey);
    	finish();
    	startActivity(sListScreen);
	}
}
