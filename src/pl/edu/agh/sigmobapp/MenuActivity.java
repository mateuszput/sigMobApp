package pl.edu.agh.sigmobapp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.utils.SigmobProperties;

import com.example.sigmobapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
 

public class MenuActivity extends Activity {
	private String apikey;
	private String propertiesFile = "settings_file";
	
	private SigmobProperties sigmobProperties;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sigmobProperties = SigmobProperties.getInstance();
 
        Intent i = getIntent();
        apikey = i.getStringExtra("apikey");
 
 
        EditText hostIP = (EditText) findViewById(R.id.hostIP);
        hostIP.setText(sigmobProperties.getHostName());
        
        
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
    
    
	private void hideAllLayouts(){
    	LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
    	menuLayout.setVisibility(LinearLayout.GONE);
    	LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
    	messageLayout.setVisibility(LinearLayout.GONE);
    	LinearLayout preferencesLayout = (LinearLayout) findViewById(R.id.preferencesLayout);
    	preferencesLayout.setVisibility(LinearLayout.GONE);
    }
}