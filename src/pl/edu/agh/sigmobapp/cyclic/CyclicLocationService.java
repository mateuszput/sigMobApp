package pl.edu.agh.sigmobapp.cyclic;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CyclicLocationService extends Service{

    private static final String TAG = "CyclicLocationService";
    
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
        LongOperation longOperation = new LongOperation();
        longOperation.execute("start me");
        
    }
 
    @Override
    public void onDestroy() {
        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }
    
	
	private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
		protected String doInBackground(String... params) {
        	Log.e("n", "do in background");
			while (true) {
				for (int i = 0; i < 5; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Log.e("n", "hello");
			}

		}

        @Override
        protected void onPostExecute(String result) {
//              TextView txt = (TextView) findViewById(R.id.output);
//              txt.setText("Executed"); // txt.setText(result);
              //might want to change "executed" for the returned string passed into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
  }   

}
