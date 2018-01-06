package com.example.steven.ibeaconmuseum.LocationClasses;

import android.support.annotation.NonNull;

import org.altbeacon.beacon.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import static java.lang.Math.*;
/**
 * Created by Steven on 11/29/2017.
 */

public class LocationRoom {

    //public LocationRoomMap roomMap;
    public final Identifier majorId; // The MajorID of the beacons associated with the room
    public final double roomAlpha; // The alpha value of the room
    //public GridPoint[][] roomMap; // grid to be populated by GridPoints containing their X,Y TODO: Possibly bypass?
    public final int xdim; // x dimension of the room
    public final int ydim; // y dimension of the room

    public final LinkedList<PointOfInterest> listPointsOfInterest; // List of points of interest in the room
    public final LinkedList<Identifier> listBeacons; // List of beacon Identifiers in the room
    public final LinkedList<GridPoint> listPointsOfInterestLoc;
    public final LinkedList<GridPoint> listBeaconsLoc;


    HashMap<GridPoint, PointOfInterest> gridPointToPointOfInterestHash = new HashMap<>(); // Hashmap for going from a location in the room to the point of interest there
    HashMap<Identifier, GridPoint> identifierToGridPointHash = new HashMap<>(); // Hashmap for locating a beacon based on its identifier


    public LocationRoom(Identifier majorId, double roomAlpha, int xdim, int ydim,
                        LinkedList<PointOfInterest> listPointsOfInterest, LinkedList<GridPoint> listPointsOfInterestLoc,
                        LinkedList<Identifier> listBeacons, LinkedList<GridPoint> listBeaconsLoc){
        this.majorId = majorId;
        this.roomAlpha = roomAlpha;
        this.xdim = xdim;
        this.ydim = ydim;

        this.listPointsOfInterest = listPointsOfInterest;
        this.listPointsOfInterestLoc = listPointsOfInterestLoc;
        this.listBeacons = listBeacons;
        this.listBeaconsLoc = listBeaconsLoc;


        // Populate the location -> POI hashmap
        for(int i=0; i<listPointsOfInterest.size(); i++){
            gridPointToPointOfInterestHash.put(listPointsOfInterestLoc.get(i), listPointsOfInterest.get(i));
        }

        // Populate the Identifer -> Location hasmap
        for(int i=0; i<listBeacons.size(); i++){
            identifierToGridPointHash.put(listBeacons.get(i), listBeaconsLoc.get(i));
        }

    }

}
