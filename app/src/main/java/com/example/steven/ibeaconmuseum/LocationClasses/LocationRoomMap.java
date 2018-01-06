package com.example.steven.ibeaconmuseum.LocationClasses;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;

import java.util.HashMap;
import java.util.List;
import static java.lang.Math.*;

/**
 * Created by Steven on 11/29/2017.
 */
/*
    RoomGrid contains the gridpoints to show if the room exists in the given location
    HashMap.get(

 */
public class LocationRoomMap {

    public GridPoint[][] roomGrid;
    public Identifier[] beaconsInRoom;
    // Predicted readings calculated at initialization
    public double[][][] predictedReadings = new double[][][]{};

    public HashMap<GridPoint, PointOfInterest> gridToPoi = new HashMap<>();
    public HashMap<GridPoint, Identifier> gridToBeacon = new HashMap<>(); // Beacon ID from location
    public HashMap<Identifier, GridPoint> beaconToGrid = new HashMap<>(); // Location of beacon from ID
    public PointOfInterest[] pois= new PointOfInterest[]{}; // Points of interest
    public GridPoint[] beaconLoc = new GridPoint[]{}; // Beacon locations, order matters
    public HashMap<Identifier, Integer> beaconToBeaconLocIndex = new HashMap<>();
    // TODO Composite key
    public HashMap<CompositeKeyXYIdentifier_PredictedRssi, Double> findPredictedRssi = new HashMap<>();

    public LocationRoomMap(GridPoint[][] roomGrid, GridPoint[] poiLoc, PointOfInterest[] pois, GridPoint[] beaconLoc, Identifier[] beaconsInRoom){
        this.roomGrid = roomGrid;
        this.beaconsInRoom = beaconsInRoom;
        this.beaconLoc = beaconLoc; // Array of gridpoints that are beacon coordinates
        this.pois = pois;
        this.predictedReadings = new double[roomGrid[0].length][roomGrid.length][beaconLoc.length];
//TODO remake
        // Place each poi in the hashmap
        for(int i=0; i < poiLoc.length; i++){
            gridToPoi.put(roomGrid[poiLoc[i].x][poiLoc[i].y], pois[i]);
        }

        // Place each beacon in the hashmap
        for(int i=0; i<beaconLoc.length; i++){
            //gridToBeacon.put(roomGrid[beaconLoc[i].x][beaconLoc[i].y], beacons[i]);
            //beaconToGrid.put(beacons[i], roomGrid[beaconLoc[i].x][beaconLoc[i].y]);
        }


    }

    // Call to initialize the predicted readings for the room, based on an alpha and granularity
    public void initPredictedReadings(double alpha, double granularity){
        // Calculate the theoretical values for each point
        for(int j=0; j<roomGrid.length; j++){ // For each Y
            for(int i=0; j<roomGrid[j].length; i++){ // For each X
                for(int p=0; p<beaconLoc.length; p++){ // For each point in the list
                    double distance = sqrt(pow((beaconLoc[p].x - i)/granularity, 2) + pow((beaconLoc[p].y - j)/granularity, 2));
                    findPredictedRssi.put(new CompositeKeyXYIdentifier_PredictedRssi(i,j, this.beaconsInRoom[p]), -60 - 10 * alpha * log10(distance));
                }
            }
        }
    }

}
