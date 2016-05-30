package com.eagle.fragmentsample;

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Service7939 extends Service implements SensorEventListener  {

	public boolean scenariesFlag = true;
	int PubLimit = 2;                   // number of times message will be published
	String LogMsgID = "loghub: ";
	String StatusMessage = "NIL";
	
	private SensorManager mSensorManager;
	private Sensor mLight;
	float mSensorData;
	
	String BROKER_URL = "tcp://81.30.211.70:1884";
	// websockets client at http://www.hivemq.com/demos/websocket-client/
	//String BROKER_URL = "tcp://test.mosquittv o.org:1883";
	// websockets client at http://test.mosquitto.org/ws.html
	String TOPICpub = "cmd_arduino";   // Publish Topic
	String TOPICsub = "scenaries";   // Subscribe Topic
	
	
	String ClientID;
	
	private MqttClient client;
	
	@Override
	   public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.d(LogMsgID, "OnStart");
		
	   mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	   mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	   mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
			
	   ClientID = Secure.getString(getContentResolver(), Secure.ANDROID_ID); 	// From Android
	   // ClientID = MqttClient.generateClientId();                             // From MQTT/Paho
	   
	   try {
           client = new MqttClient(BROKER_URL, ClientID, new MemoryPersistence());
       } catch (MqttException e) {
           e.printStackTrace();
           System.exit(1);
       }
	   
	   new mConSubscribe ().execute();
	   new mPublish ().execute();
	   
	   Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
	   return START_STICKY;
	}

	private class mConSubscribe extends AsyncTask<Void, Void, Void> {
	     protected Void doInBackground(Void...dummy) {
	    	 try {
	             client.connect();
	    		 client.setCallback(new mCallback(Service7939.this));
	             client.subscribe(TOPICsub);
	            
	         } catch (MqttException e) {
	             e.printStackTrace();
	             Log.d(LogMsgID, "Connect Failed");
	             System.exit(1);
	         }
	        return null;
	     }
	}
	
	private class mPublish extends AsyncTask<Void, Void, Void> {
		String Payload = "";
	     protected Void doInBackground(Void...dummy) {
	     
	    	 final MqttTopic commandTopic = client.getTopic(TOPICpub);
	    	 
	    	 for (int PubCount =0;PubCount < PubLimit; PubCount++){
	    		 
	    		// String Payload = "id:"+ClientID+":data:"+String.valueOf(mSensorData)+":cmd:"+StatusMessage;
				 if (scenariesFlag) {
					 Payload = "1:A1;2:94737;3:94741;4:17.30.25;";
					 scenariesFlag = false;
				 }
					 else {
					 Payload = "";
				 }

	    		 Log.d(LogMsgID, Payload);
		         MqttMessage message = new MqttMessage(Payload.getBytes());

		         try {
		        	 commandTopic.publish(message);
				} catch (MqttPersistenceException e) {
					e.printStackTrace();
					Log.d(LogMsgID, "Publish Failed # 1");
				} catch (MqttException e) {
					Log.d(LogMsgID, "Publish Failed # 2");
					e.printStackTrace();
				}
		         //System.out.println("Published [" + commandTopic.getName() + "]: " + mSensorData + ":"+ PubCount);
		         justWait(50);
	    	 
	    	 }
	        return null;
	     }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// The light sensor returns a single value.
	    // Many sensors return 3 values, one for each axis.
	    float lux = event.values[0];
	    // Do something with this sensor value.
	    mSensorData = lux;
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
public class mCallback implements MqttCallback {
		
		private ContextWrapper context;

	    public mCallback(ContextWrapper context) {
	        this.context = context;
	        Log.d(LogMsgID, "in mCallback");
	    }
	   
		@Override
	    public void connectionLost(Throwable cause) {
	        //We should reconnect here
	    }

		@Override
		public void deliveryComplete(IMqttDeliveryToken arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void messageArrived(String Topic, MqttMessage message) throws Exception {
			Log.d(LogMsgID, "Topic is " + Topic);

			Intent intent = new Intent(MainActivity.BROADCAST_ACTION);

	        StatusMessage = new String(message.getPayload());
			intent.putExtra(MainActivity.CMD, StatusMessage);
			sendBroadcast(intent);
	        //processMessage(StatusMessage);
	        new mPublish ().execute();
		}
		
   }

public void processMessage(String arg){
	
//	MediaPlayer mp;
//
//	if (arg.equals("siren")) {
//		mp = MediaPlayer.create(getApplicationContext(), R.raw.warningsiren);
//		mp.start(); justWait(10000); mp.stop();
//	} else {
//		mp = MediaPlayer.create(getApplicationContext(), R.raw.indianbell);
//		mp.start(); justWait(10000); mp.stop();
//	}
//
//	mp.release();
//    mp = null;
	/**
	This is where we call external application that will flash a light
	 */
	
//	String ExternalAppPackage = "org.iothub.flashlight";
//	String ExternalAppActivity = "org.iothub.flashlight.MainActivity";
//
//	Intent intent = new Intent(Intent.ACTION_MAIN);
//    intent.setComponent(new ComponentName(ExternalAppPackage,ExternalAppActivity));
//    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    startActivity(intent);
	}

public void justWait(int delay) {
	try {
	    Thread.sleep(delay);                 //1000 milliseconds is one second.
	 } catch(InterruptedException ex) {
	    Thread.currentThread().interrupt();
	 }
	
}

}
