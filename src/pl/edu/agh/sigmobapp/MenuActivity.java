package pl.edu.agh.sigmobapp;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;

import com.example.sigmobapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
 

public class MenuActivity extends Activity {
	private String hostName = "http://176.31.202.49:7777";
	private String apiName = "/sigmob/clientapi";
	
	private String apikey;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
 
        TextView txtName = (TextView) findViewById(R.id.txtName);
 
        Intent i = getIntent();
        // Receiving the Data
        String name = i.getStringExtra("name");
        apikey = i.getStringExtra("apikey");
 
        // Displaying Received data
        txtName.setText("Your login: " + name);
 
        
        
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
            	LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
            	menuLayout.setVisibility(LinearLayout.GONE);
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
            	LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
            	messageLayout.setVisibility(LinearLayout.GONE);
            	LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
            	menuLayout.setVisibility(LinearLayout.VISIBLE);
            	
            }
        });
        
 
    }
}