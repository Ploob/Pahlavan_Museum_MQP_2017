package com.example.steven.ibeaconmuseum;

import com.example.steven.ibeaconmuseum.LocationClasses.GridPoint;
import com.example.steven.ibeaconmuseum.LocationClasses.LocationRoom;
import com.example.steven.ibeaconmuseum.LocationClasses.LocationRoomMap;

import org.altbeacon.beacon.Beacon;
import org.apache.commons.math3.special.Erf;

import java.util.List;

import static java.lang.Math.*;

public class AlgorithmManager {

    private double sigma; // Sigma of the readings
    private double certainty; // % certainty of a signal
    private double dbTolerance; // DB tolerance to be within based on the sigma and certainty
    private double granularity; // X by X points of measurement per square meter

    public AlgorithmManager(double sigma, double certainty, double granularity){
        this.sigma = sigma;
        this.certainty = certainty;
        this.granularity = granularity;
        this.dbTolerance = this.sigma * sqrt(2) * Erf.erfcInv(2 - 2 * this.certainty);
    }

    public GridPoint MaximumLikelihoodRoomLocation(LocationRoomMap roomMap, LocationRoom room, List<Beacon> readBeacons){
        roomMap.initPredictedReadings(room.roomAlpha, this.granularity); // Init predictions

        // TODO Come up with a way to determine the room to focus on

        // Purge list of beacons that are not in this room
        // Should just have list of beacons in the room we're in
        for(int i=0; i<readBeacons.size(); i++){
            if(readBeacons.get(i).getId2() != room.majorId){
                readBeacons.remove(i);
            }
        }
        int numBeacons = readBeacons.size(); // Total number of beacons seen for this room

        // TODO Protocol for not seeing all beacons in a room?
//        if(readBeacons.size() != roomMap.beaconLoc.length){
//
//        }

        int[][] scoreGrid; // Score for the room
        for(int j=0; j<roomMap.roomGrid.length; j++){
            for(int i=0; i<roomMap.roomGrid[j].length; i++){
                for(int p=0; p<numBeacons; p++){

                }
            }
        }
        // TODO temp
        return new GridPoint(0,0);
    }



    public double getDbTolerance(){ return this.dbTolerance; }
    public double getCertainty(){ return this.certainty; }
    public double getSigma(){ return this.sigma; }
    public double getGranularity(){ return this.sigma; }

    }
