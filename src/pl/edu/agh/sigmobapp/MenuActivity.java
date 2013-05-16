package pl.edu.agh.sigmobapp;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.FileAnswer;
import pl.edu.agh.sigmobapp.utils.Config;
import pl.edu.agh.sigmobapp.utils.SigmobProperties;

import com.example.sigmobapp.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipaulpro.afilechooser.utils.FileUtils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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
	private File file;
	/*
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (0): {
			if (resultCode == Activity.RESULT_OK) {
				String filePath = data.getStringExtra("filePath");
				String fileName = data.getStringExtra("fileName");

				TextView textSelectedFile = (TextView) findViewById(R.id.textSelectedFile);
				textSelectedFile.setText(filePath + "/" + fileName);
				textSelectedFile.setVisibility(LinearLayout.VISIBLE);
			}
			break;
		}
		}
	}
	*/
	
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
//						final File file = FileUtils.getFile(uri);
						
						this.file = new File(getRealPathFromURI(uri));
//						this.file = file;
						Toast.makeText(MenuActivity.this, 
								"File Selected: "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
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
		// Create the chooser Intent
//		Intent intent = Intent.createChooser(
//				target, getString(R.string.chooser_title));
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
        
    	
        
        Button btnMesssageSelectFile = (Button) findViewById(R.id.btnMesssageSelectFile);
        btnMesssageSelectFile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
//            	Intent fileChooser = new Intent(getApplicationContext(), AndroidExplorer.class);
//               	nextScreen.putExtra("apikey", apikey);
//                startActivity(fileChooser);
//                startActivityForResult
            	
                // TODO: zamienic
//                startActivityForResult(fileChooser, 0);
                showChooser();
                
                
//            	String selectedFile = "samplefile.txt";
				
//				TextView textSelectedFile = (TextView) findViewById(R.id.textSelectedFile);
//				textSelectedFile.setText(selectedFile);
//				textSelectedFile.setVisibility(LinearLayout.VISIBLE);
				
            }
        });
        
        
        Button btnMessageSend = (Button) findViewById(R.id.btnMessageSend);
        btnMessageSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	EditText messageTitle = (EditText) findViewById(R.id.messageTitle);
            	EditText messageBody = (EditText) findViewById(R.id.messageBody);
            	TextView textSelectedFile = (TextView) findViewById(R.id.textSelectedFile);
            	
				String fileName = textSelectedFile.getText().toString();
            	
            	String titleString = messageTitle.getText().toString().replaceAll("\\W", "");
            	String messageString = messageBody.getText().toString().replaceAll("\\W", "");
            	
            	if (titleString != "" && messageString != "") {
            		SendMessageThread sendMessageThread = new SendMessageThread();
            		sendMessageThread.setFile(file);
                	sendMessageThread.execute(titleString, messageString, fileName);
    			} 
            	
                messageTitle.setText("");
        		messageBody.setText("");
        		textSelectedFile.setText("");
        		textSelectedFile.setVisibility(LinearLayout.GONE);
        		
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
    
    
    private FileAnswer sendFile(String file, File fileHandler){
    	Log.e("n", "my file exist");
    	RestCommunication restCommunication = new RestCommunication();
//    	JSONObject jsonResponse = restCommunication.doPostFile(sigmobProperties.getHostAndApi() +  "/files", apikey, file, fileHandler);
    	JSONObject jsonResponse = restCommunication.doPostFile(sigmobProperties.getHostAndApi() +  "/files", apikey, file, this.file);
    	
    	
    	Log.e("n", "file id: " + jsonResponse.toString());
    	
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

		private File file;

		@Override
		protected String doInBackground(String... params) {
			String titleString = params[0];
			String messageString = params[1];
			String fileName = params[2];
			
			Log.e("n", "do in background ");
			
//			File file = getFileStreamPath(fileName);
			// TODO: zamienic
//			File file = Config.context.getFileStreamPath(fileName);
//			File file = this.file;
			
			FileAnswer fileAnswer = null;
			if(file == null){
				Log.e("n", "file null");
			} else {
				Log.e("n", "not null ");
				Log.e("n", "file: " + file.getAbsoluteFile());
				Log.e("n", "path: " + file.getAbsolutePath());
			}
			
			
			
			String attachementBody = "";
			if (file.exists() ) {
//			if (file.exists() && fileName.compareTo("") != 0) {
				Log.e("n", "file exists:" + fileName + ".");
				// Send file
				fileAnswer = sendFile(fileName, file);
			
			} else {
				Log.e("n", "doesn't exists ");
			}
			
			if (fileAnswer != null){
				attachementBody = "{\"fileId\":" + fileAnswer.getFileId().intValue() + ",\"type\":\"image\"}";
			}
			
			RestCommunication restCommunication = new RestCommunication();

			String jsonToSend = "{\"title\": \"" + titleString
					+ "\", \"body\": \"" + messageString
					+ "\", \"attachments\": [" + attachementBody + "]}";

			Log.e("n", "json: " + jsonToSend);
			JSONObject responseJSON = restCommunication.doPost(
					sigmobProperties.getHostAndApi() + "/messages", apikey,
					jsonToSend);

			 Log.e("n", "response: " + responseJSON.toString());

			return "stoped";

		}
		


		@Override
		protected void onPostExecute(String result) {
			// TextView txt = (TextView) findViewById(R.id.output);
			// txt.setText("Executed"); // txt.setText(result);
			// might want to change "executed" for the returned string passed
			// into onPostExecute() but that is upto you
			Log.d("n", "thread stopped");
			// TODO: tutaj moge zrobic wyskakujace okno
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

		public void setFile(File file){
			this.file = file;
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