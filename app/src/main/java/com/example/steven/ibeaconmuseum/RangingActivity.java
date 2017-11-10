package com.example.steven.ibeaconmuseum;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class RangingActivity extends Activity implements BeaconConsumer{

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    private LinkedList<Identifier> seenIdentifiers = new LinkedList<Identifier>(){}; // List of UUIDs seen so far by the phone


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public void onBeaconServiceConnect(){
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0){
                    Beacon firstBeacon = beacons.iterator().next();
                    /*Identifier beaconIdentifier = firstBeacon.getId1();
                    if(!seenIdentifiers.contains(beaconIdentifier)){
                        seenIdentifiers.add(firstBeacon.getId1());
                    }*/
                    logToDisplay("Beacon: " + firstBeacon.toString() + " RSSI:  " + /*firstBeacon.getDistance()*/firstBeacon.getRssi());

                    // TODO: Change UI elements to reflect readings
                    //changeui(beaconIdentifier, firstBeacon.getRssi());
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }
/*
    private void changeui(final Identifier id, final int rssi){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Change edittext, etc.
                EditText readingText = (EditText)RangingActivity.this.findViewById(R.id.readingText);
                String line = "Beacon Id0: " + id.toHexString() + " Beacon RSSI: " + rssi;
                readingText.append(line + "\n");
            }
        });
    }
    */
    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                EditText editText = RangingActivity.this.findViewById(R.id.readingText);
                editText.append(line+"\n");
            }
        });
    }

}
