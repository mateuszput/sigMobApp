package pl.edu.agh.sigmobapp.cyclic;

import java.io.IOException;
import java.util.Iterator;

import org.json.JSONObject;

import com.example.sigmobapp.R;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.edu.agh.sigmobapp.MessageActivity;
import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.Message;
import pl.edu.agh.sigmobapp.json.TaskShort;
import pl.edu.agh.sigmobapp.json.TasksList;
import pl.edu.agh.sigmobapp.utils.SigmobProperties;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class CyclicMessageService extends Service {

	private static final String TAG = "CyclicMessageService";
	private CyclicLocationThread cyclicLocationThread;
	private String apikey;
	
	private SigmobProperties sigmobProperties;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	  
	@Override
	public void onCreate() {
		sigmobProperties = SigmobProperties.getInstance();
		Log.d(TAG, "onCreate");
	}
	
	
	@Override
	public void onStart(Intent intent, int startId) {
		apikey = intent.getStringExtra("apikey");

		// Note: You can start a new thread and use it for long background
		// processing from here.
		cyclicLocationThread = new CyclicLocationThread(getApplicationContext());
		cyclicLocationThread.execute("start me ");
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		cyclicLocationThread.stop();
	}
	
	
	
	private class CyclicLocationThread extends AsyncTask<String, Message, String> {
		Context context;
		
		public CyclicLocationThread(Context contextin){ 
			context = contextin;
		}
		
		@Override
		protected String doInBackground(String... params) {
			Log.e("n", "do in background ");
			while (true) {
				for (int i = 0; i < 5; i++) {
					if (isCancelled())
						break;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Log.e("n", "" + e);
					}
				}

				if (isCancelled())
					break;
				
				int taskNumber = getMessageTask();
				if (taskNumber != -1) {
					Message message = getMessage(taskNumber);
					
					String jsonToSend = "{\"messageId\":\"" + taskNumber
							+ "\", \"status\":\"read\"}";

					// TODO: odkomentowac
					sendMessageReceived(apikey, taskNumber, jsonToSend);
					if(message == null){
						continue;
					}
					
					publishProgress(message);
				}
			}
			return "stoped";
		}

		
		public void stop() {
			 cyclicLocationThread.cancel(true);
		}

		
		@Override
		protected void onPostExecute(String result) {
			Log.d(TAG, "thread stopped");
		}

		
		@Override
		protected void onPreExecute() {
		}

		
//		private void displayNotification(String extra, String contentTitle, String contentText, Class<?> cls, int id) {
//			NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//	        Notification notifyDetails = new Notification(R.drawable.ic_launcher, "New Alert!", System.currentTimeMillis());
//	        Intent intent = new Intent(context, cls);
//	        intent.putExtra("extra", extra);
//	        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//	        notifyDetails.setLatestEventInfo(getApplicationContext(), contentTitle, contentText, contentIntent);
//	        mNotificationManager.notify(id, notifyDetails);
//	    }
		
		@Override
		protected void onProgressUpdate(Message... values) {
			Message message = values[0];
			String title = message.getTitle();
			String body = message.getBody();
			int messageId = message.getMessageId().intValue();
			
			Toast.makeText(getApplicationContext(), title,
					Toast.LENGTH_LONG).show();
			
			
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        Notification notifyDetails = new Notification(R.drawable.ic_launcher, "New Alert!", System.currentTimeMillis());
	        Intent intent = new Intent(context, MessageActivity.class);
	        intent.putExtra("mTitle", title);
	        intent.putExtra("mBody", body);
	        intent.putExtra("mId", String.valueOf(messageId));
	        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), messageId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	        notifyDetails.setLatestEventInfo(getApplicationContext(), title, body, contentIntent);
	        mNotificationManager.notify(messageId, notifyDetails);
	        
	        
	        
	        /*
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(context)
			        .setSmallIcon(R.drawable.ic_launcher)
			        .setContentTitle(title)
			        .setContentText(body);
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(context, MessageActivity.class);
			resultIntent.putExtra("mTitle", title);
			resultIntent.putExtra("mBody", body);
			resultIntent.putExtra("mId", String.valueOf(messageId));
			
			resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			
			
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addParentStack(MessageActivity.class);
			
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			
			
//			PendingIntent resultPendingIntent =
//			        PendingIntent.getActivity(
//			        context,
//			        0,
//			        resultIntent,
//			        PendingIntent.FLAG_UPDATE_CURRENT
//			);

			
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			
			
			
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(messageId, mBuilder.build());
			
			*/
		}
		

		private void sendMessageReceived(String apikey, int taskNumber,
				String jsonToSend) {
			RestCommunication restCommunication = new RestCommunication();
			String adress = "/response/" + taskNumber + "/message";
			restCommunication.doPostNoAnswer(sigmobProperties.getHostAndApi()
					+ adress, apikey, jsonToSend);
		}
		
		
		private Message getMessage(int taskNumber) {
			RestCommunication restCommunication = new RestCommunication();
			
			String request = "/task/"+ taskNumber + "/message";
			JSONObject responseJSON = restCommunication.doGet(
					sigmobProperties.getHostAndApi() + request, apikey);

			ObjectMapper objectMapper = new ObjectMapper();
			Message message = null;
			try {
				message = objectMapper.readValue(responseJSON.toString(),
						Message.class);
			} catch (JsonParseException e) {
				Log.e("n", "" + e);
			} catch (JsonMappingException e) {
				Log.e("n", "" + e);
			} catch (IOException e) {
				Log.e("n", "" + e);
			}
			
			return message;
		}

		
		private int getMessageTask() {
			int taskNumber = -1;

			RestCommunication restCommunication = new RestCommunication();
			JSONObject responseJSON = restCommunication.doGet(
					sigmobProperties.getHostAndApi() + "/tasks", apikey);

			ObjectMapper objectMapper = new ObjectMapper();
			TasksList tasksList = null;
			try {
				tasksList = objectMapper.readValue(responseJSON.toString(),
						TasksList.class);
			} catch (JsonParseException e) {
				Log.e("n", "" + e);
			} catch (JsonMappingException e) {
				Log.e("n", "" + e);
			} catch (IOException e) {
				Log.e("n", "" + e);
			}

			if (tasksList != null && tasksList.getTasks().size() != 0) {
				Iterator<TaskShort> iterator = tasksList.getTasks().iterator();
				TaskShort taskShort = null;
				while (iterator.hasNext()) {
					taskShort = iterator.next();
					String type = taskShort.getType();
					Number taskId = taskShort.getId();

					if (type.equalsIgnoreCase("MESSAGE")) {
						taskNumber = taskId.intValue();
						break;

					}
				}
			}

			return taskNumber;
		}

	}
	
}
