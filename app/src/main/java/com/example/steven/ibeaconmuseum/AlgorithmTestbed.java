package com.example.steven.ibeaconmuseum;

import com.example.steven.ibeaconmuseum.LocationClasses.GridPoint;
import com.example.steven.ibeaconmuseum.LocationClasses.LocationRoom;
import com.example.steven.ibeaconmuseum.LocationClasses.PointOfInterest;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Identifier;
import org.apache.commons.math3.special.Erf;

import java.util.LinkedList;
import java.util.List;
import java.lang.Math.*;

import static java.lang.Math.sqrt;

/**
 * Created by Steven on 12/3/2017.
 */

public class AlgorithmTestbed {


    public double sigma = 3.1;
    public double certainty = 0.9;
    public double granularity = 2;
    public double alpha = 2.6;

    public Identifier roomId = Identifier.parse("25000"); // UWB is 25000, standard of 20000
    public int xdim = 6;
    public int ydim = 4;


    public LinkedList<Identifier> beaconArray = new LinkedList<>();
    public LinkedList<GridPoint> beaconLocations = new LinkedList<GridPoint>();

    public AlgorithmManager algorithmManager;
    public LocationRoom room;

    public AlgorithmTestbed(){

        algorithmManager = new AlgorithmManager(sigma, certainty, granularity);

        // Add the beacons and their locations
        beaconLocations.add(new GridPoint(0,0));
        beaconLocations.add(new GridPoint(0,3));
        beaconLocations.add(new GridPoint(3,0));
        beaconLocations.add(new GridPoint(3,3));
        beaconArray.add(Identifier.parse("1"));
        beaconArray.add(Identifier.parse("2"));
        beaconArray.add(Identifier.parse("3"));
        beaconArray.add(Identifier.parse("11"));

        room = new LocationRoom(roomId, alpha, xdim, ydim, new LinkedList<PointOfInterest>(){}, new LinkedList<GridPoint>(){}, beaconArray, beaconLocations );
    }

    public GridPoint newMethod(GenericPairList beacons){
//        GridPoint location = algorithmManager.MaximumLikelihoodRoomLocation(room, beacons);
//        return location;
        return new GridPoint(-1,-1); // TODO Switch in order to test algorithm or have blank return
    }




}
