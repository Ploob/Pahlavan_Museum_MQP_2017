package com.example.steven.ibeaconmuseum;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;

import android.widget.ArrayAdapter;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;


import com.example.steven.ibeaconmuseum.LocationClasses.GridPoint;
import com.example.steven.ibeaconmuseum.LocationClasses.PointOfInterest;

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

// This class does most of the heavy lifting for the application
// TODO: Replace DataObject with POIs and needed objects for mapping
//TESTING
public class MainBeaconScanning extends AppCompatActivity implements BeaconConsumer{

    // List of DataObject which contain seen beacon information
    List<DataObject> seenBeaconsDataObjectList = new ArrayList<>();

    // Custom ListView adapter for creating a ListView of DataObjects
    DataObjectAdapter dataObjectAdapter;

    // ListView UI element used for displaying seenBeaconsDataObjectList
    ListView seenBeaconsDataObjectListView;

    // BeaconManager for this MainBeaconScanning: manages all beacons interaction this activity performs
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    // HashMap containing the beacon minor ID as the key and a DataObject to represent the seen beacon
    private HashMap<Identifier, DataObject> seenBeaconsHashmap = new HashMap<>();

    // Constant for requesting permission verbosely
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    // TODO: Remove after testing
    private ArrayList<Beacon> seenBeaconList;
    public AlgorithmTestbed algorithmTestbed = new AlgorithmTestbed();

    // On creating the activity; all initializations occur here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Switch back after testing
        setContentView(R.layout.point_of_interest_listview_layout);
        //setContentView(R.layout.algorithm_testbed_layout);

        //TODO Remove after testing
        seenBeaconList = new ArrayList<Beacon>();

        // TODO Uncomment block
        // Instantiate the DataObjectAdapter used to manage the list of DataObject to display
        dataObjectAdapter = new DataObjectAdapter(this, R.layout.data_object_view_layout, seenBeaconsDataObjectList);
//        dataObjectAdapter = new DataObjectAdapter(this, R.layout.point_of_interest_list_element_layout, seenBeaconsDataObjectList);

        // Find the ListView used in the layout, and assign the DataObject adapter to it
        seenBeaconsDataObjectListView = (ListView) findViewById(R.id.listView);
        seenBeaconsDataObjectListView.setAdapter(dataObjectAdapter);
// TODO End of uncomment block
        // Verify that bluetooth and location permissions are enabled and capable of running correctly
        verifyBluetooth();
        verifyLocation();

        // Bind a BeaconManager to this activity
        beaconManager.bind(this);
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
                    seenBeaconList.clear(); // TODO Remove
                    for(Iterator<Beacon> iterator = beacons.iterator(); iterator.hasNext();){ // For each beacon seen; references the beacons collection
                        Beacon thisBeacon = iterator.next();
                        Identifier beaconMajor = thisBeacon.getId2();
                        Identifier beaconMinor = thisBeacon.getId3();
                        Integer beaconRssi = thisBeacon.getRssi();
                        seenBeaconList.add(thisBeacon);
                        if (seenBeaconsHashmap.get(beaconMinor) == null) {
                            seenBeaconsHashmap.put(beaconMinor, new DataObject("Major: " + beaconMajor, "Minor: " + beaconMinor, "RSSI: " + beaconRssi));
//                            seenBeaconsHashmap.put(beaconMinor, new PointOfInterest("Major: " + beaconMajor, "Minor: " + beaconMinor, "RSSI: " + beaconRssi));
                        } else {
                            seenBeaconsHashmap.get(beaconMinor).setCenter("RSSI: " + beaconRssi);
                        }


                    }
                    updateBeaconUiList();


                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    // Updating the list of Beacons seen
    private void updateBeaconUiList(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seenBeaconsDataObjectList.clear(); // Empty the existing list
                Iterator iterator = seenBeaconsHashmap.entrySet().iterator();
                while(iterator.hasNext()){ // For each item in the seenBeaconsHashMap
                    Map.Entry<Identifier, DataObject> mentry = (Map.Entry)iterator.next();
                    seenBeaconsDataObjectList.add((DataObject)mentry.getValue());
                }
                dataObjectAdapter.notifyDataSetChanged();


                // TODO Remove
                GridPoint gp = algorithmTestbed.newMethod(seenBeaconList);
                EditText editTextX = findViewById(R.id.editTextX);
                EditText editTextY = findViewById(R.id.editTextY);
                //editTextX.setText(gp.x);
                editTextX.setText("a");
                //editTextY.setText(gp.y);
                editTextY.setText("b");

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
