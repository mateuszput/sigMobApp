package pl.edu.agh.sigmobapp;

import pl.edu.agh.sigmobapp.cyclic.CyclicLocationService;
import pl.edu.agh.sigmobapp.cyclic.CyclicMessageService;

import com.example.sigmobapp.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class CyclicTasksActivity extends Activity{
	private String apikey;
	private static final String preferencesFile = "sigmob_prefs_file";

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ctasks);
 
        Intent i = getIntent();
        apikey = i.getStringExtra("apikey");
        
        CheckBox cyclicLocation = (CheckBox) findViewById(R.id.cyclicLocation);
        cyclicLocation.setTextColor(Color.parseColor("#000000"));
        cyclicLocation.setText("Allow location tasks");
        
     // Restore preferences
        SharedPreferences settings = getSharedPreferences(preferencesFile, 0);
        boolean cyclicLocationPref = settings.getBoolean("cyclicLocation", false);
        cyclicLocation.setChecked(cyclicLocationPref);

        cyclicLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	CheckBox cyclicLocation = (CheckBox) findViewById(R.id.cyclicLocation);
            	
            	SharedPreferences settings = getSharedPreferences(preferencesFile, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("cyclicLocation", cyclicLocation.isChecked());
                // Commit the edits!
                editor.commit();
                
            	if(cyclicLocation.isChecked()) {
            		Intent intent = new Intent(getApplicationContext(), CyclicLocationService.class);
            		intent.putExtra("apikey", apikey);
            		startService(intent);
                 
            	} else {
            		stopService(new Intent(getApplicationContext(), CyclicLocationService.class));
            		
            	}
            	
            }
        });
        
        
        CheckBox cyclicMessage = (CheckBox) findViewById(R.id.cyclicMessage);
        cyclicMessage.setTextColor(Color.parseColor("#000000"));
        cyclicMessage.setText("Allow messages from server");
        
     // Restore preferences
        boolean cyclicMessagePref = settings.getBoolean("cyclicMessage", false);
        cyclicMessage.setChecked(cyclicMessagePref);

        cyclicMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	CheckBox cyclicMessage = (CheckBox) findViewById(R.id.cyclicMessage);
            	
            	SharedPreferences settings = getSharedPreferences(preferencesFile, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("cyclicMessage", cyclicMessage.isChecked());
                // Commit the edits!
                editor.commit();
                
            	if(cyclicMessage.isChecked()) {
            		Intent intent = new Intent(getApplicationContext(), CyclicMessageService.class);
            		intent.putExtra("apikey", apikey);
            		startService(intent);
                 
            	} else {
            		stopService(new Intent(getApplicationContext(), CyclicMessageService.class));
            		
            	}
            	
            }
        });        
        
        
        
        Button btnCloseCyclic = (Button) findViewById(R.id.btnCloseCyclic);
        btnCloseCyclic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	Intent nextScreen = new Intent(getApplicationContext(), MenuActivity.class);
               	nextScreen.putExtra("apikey", apikey);
                startActivity(nextScreen);
                finish();
            }
        });
        
	}
	
	@Override
	public void onBackPressed () {
		Intent nextScreen = new Intent(getApplicationContext(), MenuActivity.class);
       	nextScreen.putExtra("apikey", apikey);
        startActivity(nextScreen);
		finish();
	}

	

}
