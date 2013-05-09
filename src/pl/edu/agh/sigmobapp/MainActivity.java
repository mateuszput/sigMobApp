package pl.edu.agh.sigmobapp;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
//import pl.edu.agh.sigmobapp.json.Answers;
import pl.edu.agh.sigmobapp.json.ApiKey;
import pl.edu.agh.sigmobapp.json.SurveyAnswer;
import pl.edu.agh.sigmobapp.json.TaskShort;
import pl.edu.agh.sigmobapp.json.TasksList;

import com.example.sigmobapp.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String version = "v.0.2.6";

	// TODO: sprawdzic ktore sa nie uzywane, wyrzucic:
	private LinearLayout my_root;
	private Button sendRequestButton;
	private Button getTaskButton;
	private Button sendResponseButton;
	private TextView answerTextView;
	private RestCommunication restCommunication; 
	private TasksList tasksList;
	private ObjectMapper jsonMapper;
	private LinearLayout mainLayout;
	
	private RadioGroup radioGroup;
//	private ArrayList<Answers> answers;
	private TextView errorView;

	
	private TaskShort shortTask;
	
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
            		
//            		Log.e("n", "response: " + responseJSON.toString());
            		
					myApi = objectMapper.readValue(responseJSON.toString(), ApiKey.class);
            		
//					Log.e("n", "myApi: " + myApi.getApikey());
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
            		//Starting a new Intent
            		Intent nextScreen = new Intent(getApplicationContext(), MenuActivity.class);
 
                	//Sending data to another Activity
//                	nextScreen.putExtra("name", user);
                	nextScreen.putExtra("apikey", myApi.getApikey());
 
//                	Log.e("n", inputName.getText()+"."+ inputPassword.getText());
 
                	startActivity(nextScreen);
            	}
            }
        });
		
		
		
//	    initView();
//	    initBtnOnClickListeners();
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
	
	
	private void initView() {
		my_root = (LinearLayout) findViewById(R.id.my_root);
		mainLayout = new LinearLayout(this);
	    mainLayout.setOrientation(LinearLayout.VERTICAL);
		answerTextView = new TextView(getApplicationContext());
	    errorView = new TextView(getApplicationContext());
//	    restCommunication.setErrorView(errorView);
	    
	    sendRequestButton = new Button(getApplicationContext());
	    sendRequestButton.setText("Send request");
	    
	    getTaskButton = new Button(getApplicationContext());
	    getTaskButton.setText("Get task");
	    
	    sendResponseButton = new Button(getApplicationContext());
	    sendResponseButton.setText("Send response");
	    
	    answerTextView.setTextColor(Color.parseColor("#000000"));
	    answerTextView.setText("click request");
	    errorView.setText("errorView");
	    errorView.setTextColor(Color.parseColor("#000000"));
	    
	    
	    mainLayout.addView(errorView);
	    mainLayout.addView(sendRequestButton);
	    mainLayout.addView(getTaskButton);
	    mainLayout.addView(answerTextView);
	    
//	    A.addView(sendResponseButton);
	    my_root.addView(mainLayout);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/*
	private void initBtnOnClickListeners() {
		sendRequestButton.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	JSONObject responseJSON = restCommunication.doGet("http://176.31.202.49:7777/sigmob/clientapi/tasks");
	        	
	        	try {
					tasksList = jsonMapper.readValue(responseJSON.toString(), TasksList.class);
					answerTextView.setText("tasksList ready");
				} catch (JsonParseException e) {
					errorView.setText(e.toString());
				} catch (JsonMappingException e) {
					errorView.setText(e.toString());
				} catch (IOException e) {
					errorView.setText(e.toString());
				}
	        	
	        	if (tasksList == null){
	        		answerTextView.setText("something's wrong - task list empty");
	        	} else {
//	        		answerTextView.setText(task.getTaskId().toString() + ", " + task.getType());
	        	}
	        	
	        	
	        }
	    });
		
		
		getTaskButton.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	
//	        	TaskShort shortTask = null;
	        	int iterator = 0;
	        	shortTask = tasksList.getTasks().get(iterator);
	        	answerTextView.setText(shortTask.getTaskId().toString() + ", " + shortTask.getType());
	        	
	        	
	        	JSONObject responseJSON = restCommunication.doGet("http://176.31.202.49:7777/sigmob/clientapi/tasks/survey/" + shortTask.getTaskId().toString());
//	        	errorView.setText(responseJSON.toString());
	        	
	        	Task task = null;
	        	try {
					task =  jsonMapper.readValue(responseJSON.toString(), Task.class);
				} catch (JsonParseException e) {
					errorView.setText(e.toString());
				} catch (JsonMappingException e) {
					errorView.setText(e.toString());
				} catch (IOException e) {
					errorView.setText(e.toString());
				}
	        	
	        	answerTextView.setText(task.getQuestion());
	        	
	        	
	        	answers = (ArrayList<Answers>) task.getAnswers();
	        	Iterator<Answers> listIterator = answers.iterator();
	        	
	        	List<RadioButton> radioButtonList = new LinkedList<RadioButton>();
	        	radioGroup = new RadioGroup(getApplicationContext()); 
	        	radioGroup.setOrientation(RadioGroup.VERTICAL);
	        	
	        	RadioButton radioButton = null;
	        	while (listIterator.hasNext()) {
	        		
	        		radioButton = new RadioButton(getApplicationContext());
	        		
	                radioGroup.addView(radioButton); //the RadioButtons are added to the radioGroup instead of the layout
	                radioButton.setTextColor(Color.parseColor("#000000"));
	                radioButton.setChecked(true);
	                
	        		radioButton.setText(listIterator.next().getAnswer());
	        		radioButtonList.add(radioButton);
	        		
	        	}
	        	
	        	mainLayout.addView(radioGroup);//you add the whole RadioGroup to the layout
	            
	        	mainLayout.addView(sendResponseButton);
	        	mainLayout.removeViewInLayout(getTaskButton);
	        	
	        }
	    });
		
		
		sendResponseButton.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	int idChecked = radioGroup.getCheckedRadioButtonId();
	        	View radioButton = radioGroup.findViewById(idChecked);
	        	int answerId = radioGroup.indexOfChild(radioButton);
	        	
	        	String answerTest = answers.get(answerId).getAnswer();
	        	
	        	
	        	answerTextView.setText("Odp: " + answerTest + ", id: " + answerId);
	        	
	        	SurveyAnswer surveyAnswer = new SurveyAnswer();
	        	surveyAnswer.setAnswer(answerId);
	        	
	        	try {
					String answer = jsonMapper.writeValueAsString(surveyAnswer);
					answerTextView.setText(answer);
					
//					restCommunication.doPost("http://176.31.202.49:7777/sigmob/clientapi/responses/survey/" + shortTask.getTaskId().toString(), answer);
					
				} catch (JsonProcessingException e) {
					errorView.setText(e.toString());
				}
	        	
	        	
	        }
	    });
	}
	*/
}
