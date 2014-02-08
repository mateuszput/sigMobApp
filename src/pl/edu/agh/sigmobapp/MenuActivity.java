package pl.edu.agh.sigmobapp;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.ChoosenAnswer;
import pl.edu.agh.sigmobapp.json.FileAnswer;
import pl.edu.agh.sigmobapp.json.SpecialMessage;
import pl.edu.agh.sigmobapp.json.SpecialMessageAttachment;
import pl.edu.agh.sigmobapp.utils.Config;
import pl.edu.agh.sigmobapp.utils.SigmobProperties;

import com.example.sigmobapp.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipaulpro.afilechooser.utils.FileUtils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
 

public class MenuActivity extends Activity {
	private static final int REQUEST_CODE = 6384; // onActivityResult request code
	private static final String CHOOSER_TITLE = "Select a file";
	
	private String apikey;
	private SigmobProperties sigmobProperties;
	
	private List <File> filesList;
	
	
	public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE:
			// If the file selection was successful
			if (resultCode == RESULT_OK) {		
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();

					try {
						// Create a file instance from the URI
						File myFile = new File(getRealPathFromURI(uri));
						
						LinearLayout messageFilesList = (LinearLayout) findViewById(R.id.messageFilesList);
						if(filesList.isEmpty()){
							TextView headerTextView = new TextView(Config.context);
							headerTextView.setText("Added files list:");
							messageFilesList.addView(headerTextView);
						}
						
						filesList.add(myFile);
						
						TextView textView = new TextView(Config.context);
						textView.setText(myFile.getAbsolutePath());
						messageFilesList.addView(textView);
						
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error", e);
					}
				}
			} 
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	

	
	private void showChooser() {
		// Use the GET_CONTENT intent from the utility class
		Intent target = FileUtils.createGetContentIntent();
		Intent intent = Intent.createChooser(target, CHOOSER_TITLE);
		try {
			startActivityForResult(intent, REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			// The reason for the existence of aFileChooser
			Log.e("n", "" + e);
		}				
	}
	
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
 
        
        Button btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });
        
        
        Button btnMesssage = (Button) findViewById(R.id.btnMesssage);
        btnMesssage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	hideAllLayouts();
            	LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
            	messageLayout.setVisibility(LinearLayout.VISIBLE);
            	if(filesList == null){
                	filesList = new LinkedList<File>();
                }
            }
        });
        
        
        Button btnMesssageSelectFile = (Button) findViewById(R.id.btnMesssageSelectFile);
        btnMesssageSelectFile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showChooser();
            }
        });
        
        
        Button btnMessageSend = (Button) findViewById(R.id.btnMessageSend);
        btnMessageSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	EditText messageTitle = (EditText) findViewById(R.id.messageTitle);
            	EditText messageBody = (EditText) findViewById(R.id.messageBody);
            	
            	String titleString = messageTitle.getText().toString().trim();
            	String messageString = messageBody.getText().toString().trim();
            	
            	if (titleString != "" && messageString != "") {
            		SendMessageThread sendMessageThread = new SendMessageThread();
            		sendMessageThread.setFileList(filesList);
            		
                	sendMessageThread.execute(titleString, messageString);
    			} 
            	
                messageTitle.setText("");
        		messageBody.setText("");
        		
        		// remove from view 
        		LinearLayout messageFilesList = (LinearLayout) findViewById(R.id.messageFilesList);
        		messageFilesList.removeAllViews();
        		filesList = null;
        		
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
    
    
    private FileAnswer sendFile(String fileName, File fileHandler){
    	RestCommunication restCommunication = new RestCommunication();
    	JSONObject jsonResponse = restCommunication.doPostFile(sigmobProperties.getHostAndApi() +  "/files", apikey, fileName, fileHandler);
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	FileAnswer fileAnswer = null;
		try {
			fileAnswer = objectMapper.readValue(jsonResponse.toString(),
					FileAnswer.class);
		} catch (JsonParseException e) {
			Log.e("n", "" + e);
		} catch (JsonMappingException e) {
			Log.e("n", "" + e);
		} catch (IOException e) {
			Log.e("n", "" + e);
		}
		
		return fileAnswer;
    }
    
    
	private void hideAllLayouts(){
    	LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
    	menuLayout.setVisibility(LinearLayout.GONE);
    	LinearLayout messageLayout = (LinearLayout) findViewById(R.id.messageLayout);
    	messageLayout.setVisibility(LinearLayout.GONE);
    }
	
	
	
	
	private class SendMessageThread extends AsyncTask<String, Void, String> {
		private List <File> filesList;
		
		@Override
		protected String doInBackground(String... params) {
			String titleString = params[0];
			String messageString = params[1];
			
			SpecialMessage specialMessage = new SpecialMessage();
			specialMessage.setTitle(titleString);
			specialMessage.setBody(messageString);
			
			List<SpecialMessageAttachment> attachments = new LinkedList<SpecialMessageAttachment>();
			
			Iterator<File> listIterator = filesList.iterator();
			SpecialMessageAttachment specialMessageAttachment = null;
			while(listIterator.hasNext()){
				File currentFile = listIterator.next();
				FileAnswer fileAnswer = sendFile(currentFile.getName(), currentFile);
				
				specialMessageAttachment = new SpecialMessageAttachment();
				specialMessageAttachment.setFileId(fileAnswer.getFileId());
				specialMessageAttachment.setType("image");
				
				attachments.add(specialMessageAttachment);
			}
			specialMessage.setAttachments(attachments);
			
			ObjectMapper objectMapper = new ObjectMapper();
	    	try {
				String specialMessageJson = objectMapper.writeValueAsString(specialMessage);
				
				RestCommunication restCommunication = new RestCommunication();
				JSONObject responseJSON = restCommunication.doPost(
						sigmobProperties.getHostAndApi() + "/messages", apikey,
						specialMessageJson);
				 
			} catch (JsonProcessingException e) {
				Log.e("n", "" + e);
			}
	    	
			return "stoped";
		}
		

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(Config.context, "Message send.", Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

		
		public void setFileList(List<File> filesList){
			this.filesList = filesList;
		}

	}
	
	@Override
	public void onBackPressed () {
		LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
		if(menuLayout.getVisibility() == LinearLayout.VISIBLE){
			finish();
		} else {
			hideAllLayouts();
			menuLayout.setVisibility(LinearLayout.VISIBLE);
		}
	}
	
}