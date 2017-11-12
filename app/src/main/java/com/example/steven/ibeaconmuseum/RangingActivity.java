package com.example.steven.ibeaconmuseum;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.EditText;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class RangingActivity extends Activity implements BeaconConsumer{

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    //private LinkedList<Identifier> seenIdentifiers = new LinkedList<Identifier>(){}; // List of UUIDs seen so far by the phone
    private HashMap<Identifier, Integer> seenBeaconsHashmap = new HashMap<Identifier, Integer>();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        verifyBluetooth();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }

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
                if(beacons.size() > 0){
                    for(Iterator<Beacon> iterator = beacons.iterator(); iterator.hasNext();){
                        Beacon thisBeacon = iterator.next();

                        seenBeaconsHashmap.put(thisBeacon.getId3(), thisBeacon.getRssi());


                        //String line = "Beacon Minor: " + thisBeacon.getId3() + " RSSI: " + thisBeacon.getRssi();
                        //logToDisplay(line);
                        updateBeaconUiList();

                    }
                }

                //if (beacons.size() > 0){
                //   Beacon firstBeacon = beacons.iterator().next();
                    /*Identifier beaconIdentifier = firstBeacon.getId1();
                    if(!seenIdentifiers.contains(beaconIdentifier)){
                        seenIdentifiers.add(firstBeacon.getId1());
                    }*/
                //    logToDisplay("Beacon: " + firstBeacon.getId3().toString() + " RSSI:  " + /*firstBeacon.getDistance()*/firstBeacon.getRssi());
                    //logToDisplay("C\n");

                    // TODO: Change UI elements to reflect readings
                    //changeui(beaconIdentifier, firstBeacon.getRssi());
                //}
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
                // TODO Figure out why the fuck the text doesn't take the newline character
                editText.append('\n'+line);
                //editText.setText(line);
            }
        });
    }

    private void updateBeaconUiList(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText editText = RangingActivity.this.findViewById(R.id.readingText);

                String editTextContents = "";
                Set set = seenBeaconsHashmap.entrySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext()){
                    Map.Entry mentry = (Map.Entry)iterator.next();
                    editTextContents = editTextContents + "Minor ID: " + mentry.getKey().toString() + "RSSI: " + mentry.getValue().toString() + '\n';
                }
                editText.setText(editTextContents);
            }
        });
    }

    // Verify that bluetooth is enabled
    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }

            });
            builder.show();

        }

    }

}
