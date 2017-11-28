package com.example.steven.ibeaconmuseum;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.ArrayAdapter;

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
import java.util.Map;
import java.util.Set;

public class RangingActivity extends ListActivity implements BeaconConsumer{

    // List of Strings to display in ListView for each near beacon
    // Removed upon creation of clickable UI
    // TODO Update to ArrayList<DataObject> once adapter is built
    ArrayList<String> listBeaconsInRange = new ArrayList<>();
    ArrayList<DataObject> listDataObjects = new ArrayList<>();

    // ListView adapter for managing the list of nearby beacons
    // Changed upon creation of custom adapter for museum data object
    // TODO Create custom adapter for DataObject
    ArrayAdapter<String> adapterBeaconsInRange;
    //DataObjectAdapter dataObjectAdapter;

    // AlgorithmManager instance for use in calculation
    private AlgorithmManager algorithmManager;

    // BeaconManager for this RangingActivity
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    //private LinkedList<Identifier> seenIdentifiers = new LinkedList<Identifier>(){}; // List of UUIDs seen so far by the phone

    // HashMap containing the beacon minor ID as the key and the last known RSSI as the value
    private HashMap<Identifier, Integer> seenBeaconsHashmap = new HashMap<>();

    // Constant for requesting permission verbosely
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    // On creating the activity; all initializations occur here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ListViewDemo.class);
        this.startActivity(intent);
        /* TESTING CUSTOM ADAPTER */
//        setContentView(R.layout.main_layout_dataobject);
//        List<DataObject> dataObjectList = new ArrayList<>();
//        DataObject dataObject = new DataObject("a", "b", "c");
//        dataObjectList.add(dataObject);
//        ListView myListView = (ListView) findViewById(R.id.listView);
//        DataObjectAdapter dataObjectAdapter = new DataObjectAdapter(this, R.layout.data_object_view_layout, dataObjectList);
//        myListView.setAdapter(dataObjectAdapter);
      //
      //  setListAdapter(dataObjectAdapter);
        //DataObjectAdapter dataObjectAdapter = new DataObjectAdapter(this, R.layout.data_object_view_layout, dataObjectList);


        /* HERE DOWN REMOVED FOR CUSTOM ADAPTER SETTINGS */

//        setContentView(R.layout.activity_ranging);
//        //ListView listView = (ListView)findViewById(R.id.);
//
//        // AlgorithmManager bound to new instance
//        algorithmManager = new AlgorithmManager();
//
//        // Set up the adapter for the listBeaconsInRange
//        adapterBeaconsInRange=new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1,
//                listBeaconsInRange);
//        setListAdapter(adapterBeaconsInRange);
//
//        // Verify that bluetooth and location permissions are enabled and capable of running correctly
//        verifyBluetooth();
//        verifyLocation();
//
//        // Bind a BeaconManager to this activity
//        beaconManager.bind(this);
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
                        seenBeaconsHashmap.put(thisBeacon.getId3(), thisBeacon.getRssi());
                        updateBeaconUiList();
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
                listBeaconsInRange.clear(); // Empty the existing list
                Set set = seenBeaconsHashmap.entrySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext()){ // For each item in the seenbeaconshashmap
                    Map.Entry mentry = (Map.Entry)iterator.next();
                    //editTextContents = editTextContents + "Minor ID: " + mentry.getKey().toString() + "RSSI: " + mentry.getValue().toString() + '\n';
                    listBeaconsInRange.add("Minor ID: " + mentry.getKey().toString() + '\n' + "RSSI: " +
                            //algorithmManager.pathLossDistance((int)mentry.getValue(), 2.6, -60));
                            mentry.getValue().toString());
                }
                adapterBeaconsInRange.notifyDataSetChanged();
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
