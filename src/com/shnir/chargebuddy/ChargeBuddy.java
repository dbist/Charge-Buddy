package com.shnir.chargebuddy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

public class ChargeBuddy extends Activity {
	private BatteryManager bm = new BatteryManager();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.registerReceiver(this.batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        TextView tv = new TextView(this);
        tv.setText("ChargeBuddy is on.");
        setContentView(tv);
    }
    
    private void setText(String text) {
    	TextView tv = new TextView(this);
    	tv.setText(text);
    	setContentView(tv);
    }

    private void setLocation(Location l) {
    	double lat = l.getLatitude();
    	double lon = l.getLongitude();

    	setText("Location found: " + lat + " " + lon);
    }

    private void requestLocation() {
    	LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	LocationListener locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	      // Called when a new location is found by the network location provider.
    	    	setText("Location request");
    	    	setLocation(location);
    	    }

    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };

    	// Register the listener with the Location Manager to receive location updates
    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
	    @Override
		public void onReceive(Context arg0, Intent intent) {
		      int level = intent.getIntExtra(bm.EXTRA_LEVEL, -1);
		      int charge = intent.getIntExtra(bm.EXTRA_PLUGGED, -1);

		      if (charge == 1) {
		    	  // Charging, proceed to location request.
		    	  setText("Charging, requesting location.");
		    	  requestLocation();
		      } else {
		    	  setText("Charging off.");
		      }

		      // setText(level + " Charging: " + charge);
		}
	  };
}