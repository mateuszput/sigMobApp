package pl.edu.agh.sigmobapp.gui;

import pl.edu.agh.sigmobapp.MainActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sigmobapp.R;

public class SigmobGUI {

	private LinearLayout my_root;
	private LinearLayout mainLayout;
	private TextView answerTextView;
	private TextView errorView;
	private Button sendRequestButton;
	private Button getTaskButton;
	private Button sendResponseButton;
	
	
//	public void createGui(LinearLayout my_root, LinearLayout mainLayout, Context context){
	public void createGui(MainActivity mainActivity){
		my_root = (LinearLayout) mainActivity.findViewById(R.id.my_root);
		mainLayout = new LinearLayout(mainActivity);
		
		sendRequestButton = new Button(mainActivity.getApplicationContext());
	    sendRequestButton.setText("Send request");
		mainLayout.addView(sendRequestButton);
		
		answerTextView = new TextView(mainActivity.getApplicationContext());
		answerTextView.setTextColor(Color.parseColor("#000000"));
	    answerTextView.setText("click request");
		mainLayout.addView(answerTextView);
		
		my_root.addView(mainLayout);
	}
	
	public void initView(Activity mainActivity) {
		
		my_root = (LinearLayout) mainActivity.findViewById(R.id.my_root);
		mainLayout = new LinearLayout(mainActivity);
	    mainLayout.setOrientation(LinearLayout.VERTICAL);
		answerTextView = new TextView(mainActivity.getApplicationContext());
	    errorView = new TextView(mainActivity.getApplicationContext());
	    
//	    restCommunication.setErrorView(errorView);
	    
	    sendRequestButton = new Button(mainActivity.getApplicationContext());
	    sendRequestButton.setText("Send request");
	    
	    getTaskButton = new Button(mainActivity.getApplicationContext());
	    getTaskButton.setText("Get task");
	    
	    sendResponseButton = new Button(mainActivity.getApplicationContext());
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
}
