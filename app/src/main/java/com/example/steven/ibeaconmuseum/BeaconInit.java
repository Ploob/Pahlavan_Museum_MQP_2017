package com.example.steven.ibeaconmuseum;

import android.app.Application;
import android.content.Intent;
import android.os.RemoteException;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;

public class BeaconInit extends Application implements BootstrapNotifier{

    // BLE packet contents, change to search per id and manufacturer
    // 0x4c000215 is the Apple iBeacon manu. code
    private static final String blePacketString = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";

    // Boolean for first time beacon detection
    private boolean haveDetectedBeaconsSinceBoot;

    @Override
    public void onCreate() {
        super.onCreate();
        // Specifying the altBeacon manager, since there are multiple libraries with the same name
        BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        // Set the number of ms between each scan, and the number of ms per scan
        beaconManager.setForegroundBetweenScanPeriod(500);
        beaconManager.setForegroundScanPeriod(500);
        try {
            beaconManager.updateScanPeriods();
        } catch (RemoteException e) {}

        // Assign the BeaconManager a filter to exclude non-iBeacon packets
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
            setBeaconLayout(blePacketString));

        // Creating allows the app to go into low power mode when no beacons are seen
        BackgroundPowerSaver backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    // Upon entering the region
    @Override
    public void didEnterRegion(Region arg0){
        if(!haveDetectedBeaconsSinceBoot){
            // First time beacons have been seen since launch
            Intent intent = new Intent(this, MainBeaconScanning.class);
            this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        }else{

        }
    }

    // Upon exiting the region
    @Override
    public void didExitRegion(Region region){

    }

    // Upon determining the current state of the region
    @Override
    public void didDetermineStateForRegion(int state, Region region) {

    }

}
