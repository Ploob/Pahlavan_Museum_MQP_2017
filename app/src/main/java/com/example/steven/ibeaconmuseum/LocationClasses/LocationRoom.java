package com.example.steven.ibeaconmuseum.LocationClasses;

import org.altbeacon.beacon.Identifier;

import java.util.HashMap;
import java.util.LinkedList;

public class LocationRoom {

    public final Identifier majorId; // The MajorID of the beacons associated with the room
    public final double roomAlpha; // The alpha value of the room
    public final int xdim; // x dimension of the room
    public final int ydim; // y dimension of the room

    public final LinkedList<PointOfInterest> listPointsOfInterest; // List of points of interest in the room
    public final LinkedList<Identifier> listBeacons; // List of beacon Identifiers in the room
    public final LinkedList<GridPoint> listPointsOfInterestLoc;
    public final LinkedList<GridPoint> listBeaconsLoc;

    HashMap<GridPoint, PointOfInterest> gridPointToPointOfInterestHash = new HashMap<>(); // Hashmap for going from a location in the room to the point of interest there
    HashMap<Identifier, GridPoint> identifierToGridPointHash = new HashMap<>(); // Hashmap for locating a beacon based on its identifier

    // Defining a room based on a alrge number of inputs
    // MajorID assigned to the beacons in the room, the alpha value of the room, the x and y dimensions in units,
    // A list of POIs and a list of their locations, a list of beacons and their locations
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

        // Populate the location -> POI hashmap, for finding POIs on a given coordinate
        for(int i=0; i<listPointsOfInterest.size(); i++){
            gridPointToPointOfInterestHash.put(listPointsOfInterestLoc.get(i), listPointsOfInterest.get(i));
        }

        // Populate the Identifer -> Location hashmap, for finding beacon locations based on their Major ID
        for(int i=0; i<listBeacons.size(); i++){
            identifierToGridPointHash.put(listBeacons.get(i), listBeaconsLoc.get(i));
        }
    }
}
