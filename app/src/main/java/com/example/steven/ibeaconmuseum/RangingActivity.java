package com.example.steven.ibeaconmuseum;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RangingActivity extends AppCompatActivity implements BeaconConsumer{

    // List of DataObject which contain seen beacon information
    List<DataObject> seenBeaconsDataObjectList = new ArrayList<>();

    // Custom ListView adapter for creating a ListView of DataObjects
    DataObjectAdapter dataObjectAdapter;

    // ListView UI element used for displaying seenBeaconsDataObjectList
    ListView seenBeaconsDataObjectListView;

    // BeaconManager for this RangingActivity: manages all beacons interaction this activity performs
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    // HashMap containing the beacon minor ID as the key and a DataObject to represent the seen beacon
    private HashMap<Identifier, DataObject> seenBeaconsHashmap;

    // Constant for requesting permission verbosely
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    // On creating the activity; all initializations occur here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        dataObjectAdapter = new DataObjectAdapter(this, R.layout.data_object_view_layout, seenBeaconsDataObjectList);
//        setListAdapter(dataObjectAdapter);
        seenBeaconsDataObjectListView = (ListView) findViewById(R.id.listView);
        seenBeaconsDataObjectListView.setAdapter(dataObjectAdapter);
        // Initialize the HashMap with key beacon minor and data DataObject
        seenBeaconsHashmap = new HashMap<Identifier, DataObject>();

        // Verify that bluetooth and location permissions are enabled and capable of running correctly
        verifyBluetooth();
        verifyLocation();

        // Bind a BeaconManager to this activity
        beaconManager.bind(this);
//
//        List<DataObject> dataObjectList = new ArrayList<>();
//        DataObject dataObject = new DataObject("a", "b", "c");
//        DataObject dataObject2 = new DataObject("d", "e", "f");
//        DataObject dataObject3 = new DataObject("g", "h", "i");
//        HashMap<Integer, DataObject> seenHashMap = new HashMap<>();
//        seenHashMap.put(0, dataObject);
//        seenHashMap.put(1,dataObject2);
//        seenHashMap.put(2,dataObject3);
//        ListView listView = (ListView)findViewById(R.id.listView);
//        DataObjectAdapter dataObjectAdapter = new DataObjectAdapter(this, R.layout.data_object_view_layout, dataObjectList);

        Log.d("NOTICE", "Finished RangingActivity onCreate");
    }

    // On exiting and destroying the activity
    @Override
    protected void onDestroy(){
        super.onDestroy();
        beaconManager.unbind(this);
    }

    // On pausing the activity
    @Override
    protected void onPause(){
        super.onPause();
        if(beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    // On resuming the activity
    @Override
    protected void onResume(){
        super.onResume();
        if(beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    // Run this method when beacons are found and connected to
    @Override
    public void onBeaconServiceConnect(){
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if(beacons.size() > 0){
                    for(Iterator<Beacon> iterator = beacons.iterator(); iterator.hasNext();){ // For each beacon seen; references the beacons collection
                        Beacon thisBeacon = iterator.next();
                        Identifier beaconMajor = thisBeacon.getId2();
                        Identifier beaconMinor = thisBeacon.getId3();
                        Integer beaconRssi = thisBeacon.getRssi();
                        //if (seenBeaconsHashmap.get(beaconMinor) == null) {
                        //seenBeaconsHashmap.put(beaconMinor, new DataObject("Major: " + beaconMajor, "Minor: " + beaconMinor, "RSSI: " + beaconRssi));
                        //} else {
                        //    seenBeaconsHashmap.get(beaconMinor).setCenter("RSSI: " + beaconRssi);
                        //}
                        seenBeaconsDataObjectList.add(new DataObject("ping", "pong", "test"));
                        dataObjectAdapter.notifyDataSetChanged();
                        //                        updateBeaconUiList();

                    }
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    // Updating the list of Beacons seen
    // TODO Turn into a list of DataObjects instead
    private void updateBeaconUiList(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seenBeaconsDataObjectList.clear(); // Empty the existing list
                Iterator iterator = seenBeaconsHashmap.entrySet().iterator();
                while(iterator.hasNext()){ // For each item in the seenBeaconsHashMap
                    Map.Entry<Identifier, DataObject> mentry = (Map.Entry)iterator.next();
                    seenBeaconsDataObjectList.add((DataObject)mentry.getValue());
                    //iterator.remove();
                }
                dataObjectAdapter.notifyDataSetChanged();
            }
        });
    }

    // Possible replacement for the RunOnUiThread, avoid concurrent HashMap access problem?
    private void processBeacon(Beacon beacon) {
        Identifier beaconMajor = beacon.getId2();
        Identifier beaconMinor = beacon.getId3();
        Integer beaconRssi = beacon.getRssi();
        if (seenBeaconsHashmap.get(beaconMinor) == null) {
            seenBeaconsHashmap.put(beaconMinor, new DataObject("Major: " + beaconMajor, "Minor: " + beaconMinor, "RSSI: " + beaconRssi));
        } else {
            seenBeaconsHashmap.get(beaconMinor).setCenter("RSSI: " + beaconRssi);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seenBeaconsDataObjectList.clear(); // Empty the existing list
                Iterator iterator = seenBeaconsHashmap.entrySet().iterator();
                while (iterator.hasNext()) { // For each item in the seenBeaconsHashMap
                    Map.Entry<Identifier, DataObject> mentry = (Map.Entry) iterator.next();
                    seenBeaconsDataObjectList.add(mentry.getValue());
                    //iterator.remove();
                }
                //dataObjectAdapter.notifyDataSetChanged();
            }
        });
    }
        //if(seenBeaconsHashmap.get(beaconMinor) == null){
            //seenBeaconsHashmap.put(beaconMinor, new DataObject("Major: " + beaconMajor.toString(), "Minor: " + beaconMinor.toString(), "RSSI:" + beaconRssi));
        //}else{
        //    seenBeaconsHashmap.get(beaconMinor).setCenter("RSSI: " + beaconRssi);
        //}

//        seenBeaconsDataObjectList.clear(); // Empty the existing list
//        Iterator iterator = seenBeaconsHashmap.entrySet().iterator();
//        while(iterator.hasNext()){ // For each item in the seenBeaconsHashMap
//            Map.Entry mentry = (Map.Entry)iterator.next();
//            seenBeaconsDataObjectList.add((DataObject)mentry.getValue());
//            //iterator.remove();
//        }
//        dataObjectAdapter.notifyDataSetChanged();

 //   }

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

    // Verify that coarse location permissions are enabled
    private void verifyLocation(){
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
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }
    }

}
