package com.example.steven.ibeaconmuseum;

import com.example.steven.ibeaconmuseum.LocationClasses.GridPoint;
import com.example.steven.ibeaconmuseum.LocationClasses.LocationRoom;
import com.example.steven.ibeaconmuseum.LocationClasses.ReadingBeaconPair;

import org.altbeacon.beacon.Beacon;
import org.apache.commons.math3.special.Erf;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;

public class AlgorithmManager {

    public double sigma; // Sigma of the readings
    public double certainty; // % certainty of a signal
    public double dbTolerance; // DB tolerance to be within based on the sigma and certainty
    public double granularity; // X by X points of measurement per square meter
    public ReadingBeaconPair[][] predictedReadings; // 2d array holding a ReadingBeaconPair per spot, each which has a hashmap of IDs -> readings predicted

    // Initialization requires a sigma, certainty target, and granularity (units per meter)
    public AlgorithmManager(double sigma, double certainty, double granularity){
        this.sigma = sigma;
        this.certainty = certainty;
        this.granularity = granularity;
        this.dbTolerance = this.sigma * sqrt(2) * Erf.erfcInv(2 - 2 * this.certainty); // Inverse erfc function for finding tolerance

    }

    /*
        Start of the maximum likelihood algorithm
        Returns a GridPoint which contains the X and Y closest to the predicted coordinate within the given room based on the beacons
        TODO: Import set of data rather than one set of readings
     */
    public GridPoint MaximumLikelihoodRoomLocation(LocationRoom room, List<Beacon> readBeacons){
        int xdim = room.xdim;
        int ydim = room.ydim;
        predictedReadings = new ReadingBeaconPair[xdim][ydim]; // Table for holding the predicted readings
        initPredictedReadings(room); // Fill out the table of predicted readings
        int[][] score = new int[xdim][ydim]; // Matrix for holding the scores for each point in the room

        // Move through and score each point
        // TODO: Handle not seeing a point
        int scoreTotal = 0;
        for(int j=0; j<ydim; j++){
            for(int i=0; i<xdim; i++){
                scoreTotal = 0;
                for(int k=0; k<readBeacons.size(); k++){
                    Beacon thisBeacon = readBeacons.get(k);
                    double readRssi = thisBeacon.getRssi();
                    double predictedRssi = predictedReadings[i][j].readingMap.get(thisBeacon.getId3());
//                    if(predictedRssi == null){
//                      TODO: Possibly check for no return from the hashmap, if somehow incorrect beacons made it through
//                    }
                    if(abs(readRssi - predictedRssi) <= dbTolerance){ // Difference between read and measured within dbTolerance
                        scoreTotal++;
                    }
                }
                score[i][j] = scoreTotal; // Set the score for i,j to the scoreTotal accumulated
            }
        }

        // TODO: Play with weights for high scores, include more than the best, etc.
        // Determine points of highest score
        int highScore = 0;
        LinkedList<GridPoint> highScoringPoints = new LinkedList<>();
        for(int j=0; j<ydim; j++){
            for(int i=0; i<xdim; i++){
                if(score[i][j] > highScore){
                    highScore = score[i][j];
                    if(!highScoringPoints.isEmpty()){ highScoringPoints.clear(); } // Clear list and update the highest score
                    highScoringPoints.add(new GridPoint(i,j));
                }else if(score[i][j] == highScore){
                    highScoringPoints.add(new GridPoint(i,j)); // Add the high scoring point
                }
            }
        }

        // Have list of the best scoring points, find the centroid
        int numPts = highScoringPoints.size();
        if(numPts == 1){
            return highScoringPoints.getFirst(); // Return the only high point
        }else if(numPts > 1){
            int xtot = 0;
            int ytot = 0;
            for(int i=0; i<numPts; i++){
                xtot = xtot + highScoringPoints.get(i).x;
                ytot = ytot + highScoringPoints.get(i).y;
            }
            return new GridPoint(round(xtot/numPts), round(ytot/numPts)); // Round to the nearest point, return the resulting gridpoint

        }else{
            return null; // Something got very messed up, return null and hope things will work a different time
        }
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

    // Path Loss equation, takes P0, alpha, and the distance in meters, returns the dB ratio
    public double pathLoss(double p0, double alpha, double distance){
        return p0 - 10 * alpha * log10(distance);
    }

    // Calculates distance between two points in meters, takes granularity, two points, returns the distance
    public double meterDistanceBetween(double granularity, int x1, int y1, int x2, int y2){
        return sqrt(pow((double)x1-(double)x2, 2) + pow((double)y1-(double)y2, 2)) / granularity;
    }

}
