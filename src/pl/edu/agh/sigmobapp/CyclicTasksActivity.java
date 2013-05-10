package pl.edu.agh.sigmobapp;

import pl.edu.agh.sigmobapp.utils.SigmobProperties;

import com.example.sigmobapp.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class CyclicTasksActivity extends Activity{
	private String apikey;
	private SigmobProperties sigmobProperties;
	
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
        
        cyclicLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	CheckBox cyclicLocation = (CheckBox) findViewById(R.id.cyclicLocation);
//            	Log.e("n", "checked: " + cyclicLocation.isChecked());
            	
            	if(cyclicLocation.isChecked()) {
            		// create service
            	} else {
            		// kill service
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

//	android:text="@string/meat"
//	        android:onClick="onCheckboxClicked"
	
	

}
