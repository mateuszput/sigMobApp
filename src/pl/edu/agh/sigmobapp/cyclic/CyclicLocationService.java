package pl.edu.agh.sigmobapp.cyclic;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.edu.agh.sigmobapp.comm.RestCommunication;
import pl.edu.agh.sigmobapp.json.TaskShort;
import pl.edu.agh.sigmobapp.json.TasksList;
import pl.edu.agh.sigmobapp.utils.Config;
import pl.edu.agh.sigmobapp.utils.SigmobProperties;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class CyclicLocationService extends Service {

	private static final String TAG = "CyclicLocationService";
	private CyclicLocationThread cyclicLocationThread;
	private String apikey;
	private SigmobProperties sigmobProperties;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		// Toast.makeText(this, "Congrats! MyService Created",
		// Toast.LENGTH_LONG).show();
		// Intent i = getIntent();
		// apikey = i.getStringExtra("apikey");
		sigmobProperties = SigmobProperties.getInstance();

		Log.d(TAG, "onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		apikey = intent.getStringExtra("apikey");
		Log.d(TAG, "onStart " + apikey);

		// Note: You can start a new thread and use it for long background
		// processing from here.
		cyclicLocationThread = new CyclicLocationThread();
		cyclicLocationThread.execute("start me ");

	}

	@Override
	public void onDestroy() {
		// Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDestroy");
		cyclicLocationThread.stop();
	}

	private class CyclicLocationThread extends AsyncTask<String, Void, String> {

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

				// TODO: send request to server
				// send current location
				// apikey - uzyc

				// get task id
				int taskNumber = getLocationTask();

				// if task id = -1 - nic nie rob

				if (taskNumber != -1) {

					LocationManager locationManager;
					String context = Context.LOCATION_SERVICE;
					locationManager = (LocationManager) Config.context
							.getSystemService(context);
					if (locationManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						Location location; // location
						double latitude = 0; // latitude
						double longitude = 0; // longitude
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}

						DateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd'T'HH:mm'Z'");
						String nowAsString = df.format(new Date());
						// latitude = 1.0;
						// longitude = 1.0;
						// nowAsString = "2013-05-15T19:50:50Z";
						// 2013-05-15 19:51:25.0
						// 2013-05-15 19:34:49.0
						String jsonToSend = "{\"obtainedAt\":\"" + nowAsString
								+ "\", \"longitude\":\"" + longitude
								+ "\", \"latitude\":\"" + latitude + "\"}";
						
						Log.e("n", "js: " + jsonToSend);
						sendLocation(apikey, taskNumber, jsonToSend);

						// Log.e("n", "la, lo: " + latitude + ", " + longitude);
					}

				}
			}
			return "stoped";

		}
		

		public void stop() {
			cyclicLocationThread.cancel(true);
			// running = false;
		}

		@Override
		protected void onPostExecute(String result) {
			// TextView txt = (TextView) findViewById(R.id.output);
			// txt.setText("Executed"); // txt.setText(result);
			// might want to change "executed" for the returned string passed
			// into onPostExecute() but that is upto you
			Log.d(TAG, "thread stopped");
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

		private void sendLocation(String apikey, int taskNumber,
				String jsonToSend) {
			RestCommunication restCommunication = new RestCommunication();
			String adress = "/response/" + taskNumber + "/location";
			restCommunication.doPostNoAnswer(sigmobProperties.getHostAndApi()
					+ adress, apikey, jsonToSend);
			Log.e(TAG, "sending to: " + sigmobProperties.getHostAndApi() + adress);
			Log.d(TAG, "location response ok ");
			Log.d(TAG, "apikey " + apikey);
		}

		private int getLocationTask() {
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

					if (type.equalsIgnoreCase("LOCATION")) {
						taskNumber = taskId.intValue();
						break;

					}
				}
			}

			return taskNumber;
		}

	}

}
