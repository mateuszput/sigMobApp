package pl.edu.agh.sigmobapp;

import pl.edu.agh.sigmobapp.cyclic.CyclicLocationService;
import pl.edu.agh.sigmobapp.utils.SigmobProperties;

import com.example.sigmobapp.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class CyclicTasksActivity extends Activity{
	private String apikey;
	private SigmobProperties sigmobProperties;
	private static final String preferencesFile = "sigmob_prefs_file";

//	android:enabled="false"
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ctasks);
        sigmobProperties = SigmobProperties.getInstance();
 
        Intent i = getIntent();
        apikey = i.getStringExtra("apikey");
        
        CheckBox cyclicLocation = (CheckBox) findViewById(R.id.cyclicLocation);
        cyclicLocation.setTextColor(Color.parseColor("#000000"));
//        cyclicLocation.setChecked(true);
        cyclicLocation.setText("Allow location tasks");
        
     // Restore preferences
        SharedPreferences settings = getSharedPreferences(preferencesFile, 0);
        boolean cyclicLocationPref = settings.getBoolean("cyclicLocation", false);
        cyclicLocation.setChecked(cyclicLocationPref);
//        setSilent(silent);

        
        
        cyclicLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	CheckBox cyclicLocation = (CheckBox) findViewById(R.id.cyclicLocation);
            	Log.e("n", "checked: " + cyclicLocation.isChecked());
            	
//            	Intent intent = new Intent(getApplicationContext(), CyclicLocationService.class);
            	
            	SharedPreferences settings = getSharedPreferences(preferencesFile, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("cyclicLocation", cyclicLocation.isChecked());
                // Commit the edits!
                editor.commit();
                
            	
            	if(cyclicLocation.isChecked()) {
            		
            		startService(new Intent(getApplicationContext(), CyclicLocationService.class));
            		// create service
            		
//            		Intent intent = new Intent(this, CyclicLocationService.class);
//            		Intent cyclicLocationService = new Intent(getApplicationContext(), CyclicLocationService.class);
//                    startActivity(cyclicLocationService);
                    
                    
//                    AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    // Start every 30 seconds
//                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent); 
                 
            	} else {
            		// kill service
            		stopService(new Intent(getApplicationContext(), CyclicLocationService.class));
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

	

}
