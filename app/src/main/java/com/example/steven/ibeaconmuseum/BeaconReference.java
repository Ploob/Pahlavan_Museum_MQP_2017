package com.example.steven.ibeaconmuseum;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;

/*
The central code for starting monitoring on a region, etc.
May be depreciated at a later date, but for now holds init code
Moves to external classes with layouts, etc.
 */

public class BeaconReference extends Application implements BootstrapNotifier{

    // BLE packet contents, change to search per id and manufacturer
    // 0x4c000215 is the iBeacon manu. code
    private static final String blePacketString = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";

    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot;
    private RangingActivity rangingActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // Specifying the altBeacon manager, since there are multiple libraries with the same name
        BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
            setBeaconLayout(blePacketString));

        // Creating allows the app to go into low power mode when no beacons are seen
        backgroundPowerSaver = new BackgroundPowerSaver(this);
    }


    @Override
    public void didEnterRegion(Region arg0){
        if(!haveDetectedBeaconsSinceBoot){
            // First time beacons have been seen since launch
            Log.d("beaconReference", "Saw beacons for the first time");
            Intent intent = new Intent(this, RangingActivity.class);
            this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        }else{

        }
    }

    @Override
    public void didExitRegion(Region region){

    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        //if (rangingActivity != null) {
        //    rangingActivity.logToDisplay("I have just switched from seeing/not seeing beacons: " + state);
        //}
    }

    /*
    @Override
    public void onBeaconServiceConnect(){
        Intent intent = new Intent(this, RangingActivity.class);
        this.startActivity(intent);
        haveDetectedBeaconsSinceBoot = true;
    }
    */

}
