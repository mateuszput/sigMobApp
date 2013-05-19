package pl.edu.agh.sigmobapp;

import com.example.sigmobapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MessageActivity extends Activity {
	private String mId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_message);
		
		Intent intent = getIntent();
		mId = intent.getStringExtra("mId");
		
		TextView titleMessage = (TextView) findViewById(R.id.titleMessage);
		titleMessage.setText(intent.getStringExtra("mTitle"));
		
		TextView bodyMessage = (TextView) findViewById(R.id.bodyMessage);
		bodyMessage.setText(intent.getStringExtra("mBody"));

		// TODO: mozna dodac przycisk send confirmation
		Button btnCloseMessage = (Button) findViewById(R.id.btnCloseMessage);
		btnCloseMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	finish();
            }
        });
	}
}
