package pl.edu.agh.sigmobapp.cyclic;

import pl.edu.agh.sigmobapp.utils.Config;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CyclicLocationService extends Service{

    private static final String TAG = "CyclicLocationService";
    private CyclicLocationThread cyclicLocationThread;
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
 
    @Override
    public void onCreate() {
        Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
    }
 
    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
        //Note: You can start a new thread and use it for long background processing from here.
        cyclicLocationThread = new CyclicLocationThread();
        cyclicLocationThread.execute("start me");
        
    }
 
    @Override
    public void onDestroy() {
        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
        cyclicLocationThread.stop();
    }
    
	
	private class CyclicLocationThread extends AsyncTask<String, Void, String> {

        @Override
		protected String doInBackground(String... params) {
        	Log.e("n", "do in background");
			while (true ) {
				for (int i = 0; i < 5; i++) {
					if(isCancelled())
		                  break; 
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Log.e("n", "" + e);
					}
				}
				
				if(isCancelled())
	                  break; 
				
				// TODO: send request to server
				// send current location
				
				LocationManager locationManager;
				String context = Context.LOCATION_SERVICE;
				locationManager = (LocationManager) Config.context.getSystemService(context);
				
//				Log.e("n", "" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
				
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
				
				Log.e("n", "la, lo: " + latitude + ", " + longitude);
//				Log.e("n", "hello");
			}
			return "stoped";

		}

        
        public void stop(){
        	cyclicLocationThread.cancel(true);
//        	running = false;
        }
        
        
        @Override
        protected void onPostExecute(String result) {
//              TextView txt = (TextView) findViewById(R.id.output);
//              txt.setText("Executed"); // txt.setText(result);
              //might want to change "executed" for the returned string passed into onPostExecute() but that is upto you
        	Log.d(TAG, "thread stopped");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
  }   

}
