package com.example.steven.ibeaconmuseum;

import com.example.steven.ibeaconmuseum.LocationClasses.GridPoint;
import com.example.steven.ibeaconmuseum.LocationClasses.LocationRoom;

import org.apache.commons.math3.special.Erf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import static java.lang.Math.*;

public class AlgorithmManager {

    public double sigma; // Sigma of the readings
    public double certainty; // % certainty of a signal
    public double dbTolerance; // DB tolerance to be within based on the sigma and certainty
    public double granularity; // X by X points of measurement per square meter
    private GridPoint ERROR_GRIDPOINT = new GridPoint(-1,-1);
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
    public GridPoint MaximumLikelihoodRoomLocation(LocationRoom room, GenericPairList readBeacons){

        HashMap<Integer, Double> minorToReading = new HashMap<>(); // Given the minor value, return the rssi read from a beacon of that minor
        if(readBeacons.list.isEmpty()){ // Return -1, -1 on empty list, meaning there are no points of the given major seen
            return ERROR_GRIDPOINT;
        }
        for(int i=0; i<readBeacons.list.size(); i++){
            GenericPair gp = readBeacons.list.get(i);
            minorToReading.put((Integer)gp.getFirst(), (Double)gp.getSecond());
        }

        // Fill the array of predicted readings, predictedReadings
        int xdim = room.xdim;
        int ydim = room.ydim;
        PredictedReadingHashMap[][] predictedReadings = new PredictedReadingHashMap[xdim][ydim];
        for(int j=0; j<ydim; j++){
            for(int i=0; i<xdim; i++){
                for(int k=0; k<room.listBeacons.size(); k++){
                    Double prssi = pathLoss(-60, room.roomAlpha, meterDistanceBetween(granularity,
                            room.listBeaconsLoc.get(k).x, room.listBeaconsLoc.get(k).y, i, j));
                    predictedReadings[i][j].hm.put(room.listBeacons.get(k).toInt(), prssi);
                }
            }
        }

        // Begin filling the score matrix with values
        int[][] score = new int[xdim][ydim]; // Matrix for holding the scores for each point in the room
        // Move through and score each point
        // TODO: Handle not seeing a point
        int scoreTotal;
        for(int j=0; j<ydim; j++){
            for(int i=0; i<xdim; i++){
                scoreTotal = 0;
                // For each beacon in the minorToReadng list, hitting each of the read beacons
                Iterator iterator = minorToReading.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<Integer, Double> mentry = (Map.Entry)iterator.next();
                    double readRssi = mentry.getValue();
                    double predictedRssi = predictedReadings[i][j].hm.get(mentry.getKey());

                    // Check that the differences between read and predicted are within dbTolerance
                    if(abs(readRssi - predictedRssi) <= dbTolerance){
                        scoreTotal++;
                    }
                }
                score[i][j] = scoreTotal;
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
            return ERROR_GRIDPOINT; // Something got very messed up, return null and hope things will work a different time
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
