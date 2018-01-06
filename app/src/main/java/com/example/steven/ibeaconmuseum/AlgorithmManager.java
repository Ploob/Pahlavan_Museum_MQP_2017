package com.example.steven.ibeaconmuseum;

import com.example.steven.ibeaconmuseum.LocationClasses.GridPoint;
import com.example.steven.ibeaconmuseum.LocationClasses.LocationRoom;
import com.example.steven.ibeaconmuseum.LocationClasses.LocationRoomMap;
import com.example.steven.ibeaconmuseum.LocationClasses.ReadingBeaconPair;

import org.altbeacon.beacon.Beacon;
import org.apache.commons.math3.special.Erf;

import java.util.List;

import static java.lang.Math.*;

public class AlgorithmManager {

    public double sigma; // Sigma of the readings
    public double certainty; // % certainty of a signal
    public double dbTolerance; // DB tolerance to be within based on the sigma and certainty
    public double granularity; // X by X points of measurement per square meter
    public ReadingBeaconPair[][] predictedReadings; // 2d array holding a ReadingBeaconPair per spot, each which has a hashmap of IDs -> readings predicted

    public AlgorithmManager(double sigma, double certainty, double granularity){
        this.sigma = sigma;
        this.certainty = certainty;
        this.granularity = granularity;
        this.dbTolerance = this.sigma * sqrt(2) * Erf.erfcInv(2 - 2 * this.certainty);

    }

    public GridPoint MaximumLikelihoodRoomLocation(LocationRoom room, List<Beacon> readBeacons){
        predictedReadings = new ReadingBeaconPair[room.xdim][room.ydim];
        initPredictedReadings(room);
        return new GridPoint(0,0);
    }

    // Initialize the table of predicted readings
    public void initPredictedReadings(LocationRoom room) {
        for (int j = 0; j < room.ydim; j++) {
            for (int i = 0; i < room.xdim; i++) {
                for (int k = 0; k < room.listBeacons.size(); k++) {
                    predictedReadings[i][j].readingMap.put(room.listBeacons.get(k), pathLoss(-60, room.roomAlpha,
                            meterDistanceBetween(this.granularity,
                                    i, j, room.listBeaconsLoc.get(k).x, room.listBeaconsLoc.get(k).y)));
                }
            }
        }
    }

    public double pathLoss(double p0, double alpha, double distance){
        return p0 - 10 * alpha * log10(distance);
    }
    public double meterDistanceBetween(double granularity, int x1, int y1, int x2, int y2){
        return sqrt(pow((double)x1-(double)x2, 2) + pow((double)y1-(double)y2, 2)) / granularity;
    }

}
