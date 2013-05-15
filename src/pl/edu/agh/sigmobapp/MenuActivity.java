package pl.edu.agh.sigmobapp;

import java.io.File;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.utils.SigmobProperties;

import com.example.sigmobapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
 

public class MenuActivity extends Activity {
	private String apikey;
	private SigmobProperties sigmobProperties;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		StrictMode.ThreadPolicy policy = new StrictMode.
		ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
        
        setContentView(R.layout.activity_menu);
        sigmobProperties = SigmobProperties.getInstance();
 
        Intent i = getIntent();
        apikey = i.getStringExtra("apikey");
 
 
        // Binding Click event to Button - move to method?
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
        
    	
        Button btnMesssageSendFile = (Button) findViewById(R.id.btnMesssageSendFile);
        btnMesssageSendFile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
				File file = getFileStreamPath("samplefile.txt");
				if (file.exists())
					sendFile("samplefile.txt", file);
				
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

					JSONObject responseJSON = restCommunication.doPost(sigmobProperties.getHostAndApi()
							 + "/messages", apikey, jsonToSend);
//					Log.e("n", "response: " + responseJSON.toString());
				}
				messageTitle.setText("");
				messageBody.setText("");
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
        
        
        Button btnCyclicTasks = (Button) findViewById(R.id.btnCyclicTasks);
        btnCyclicTasks.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	Intent nextScreen = new Intent(getApplicationContext(), CyclicTasksActivity.class);
               	nextScreen.putExtra("apikey", apikey);
                startActivity(nextScreen);
                finish();
            }
        });
 
    }
    
    
    private void sendFile(String file, File fileHandler){
    	Log.e("n", "my file exist");
    	RestCommunication restCommunication = new RestCommunication();
    	JSONObject jsonResponse = restCommunication.doPostFile(sigmobProperties.getHostAndApi() +  "/files", apikey, file, fileHandler);
    	// Zamienic na obiekt z json
    	// ukryc przycisk wyslij plik
    	// Pokazac labelke z przesylanym plikiem
    	Log.e("n", "file id: " + jsonResponse.toString());
    }
    
    
	private void hideAllLayouts(){
    	LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
    	menuLayout.setVisibility(LinearLayout.GONE);
    	LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
    	messageLayout.setVisibility(LinearLayout.GONE);
//    	LinearLayout preferencesLayout = (LinearLayout) findViewById(R.id.preferencesLayout);
//    	preferencesLayout.setVisibility(LinearLayout.GONE);
    }
}