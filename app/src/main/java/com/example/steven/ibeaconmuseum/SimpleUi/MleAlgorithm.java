package com.example.steven.ibeaconmuseum.SimpleUi;

import org.altbeacon.beacon.Identifier;
import org.apache.commons.math3.special.Erf;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Steven on 2/27/2018.
 */

public class MleAlgorithm {




    // User variables
    private double x_dim_m = 10; // Room dimensions
    private double y_dim_m = 10;
    private double alpha = 2; // Gradient
    private double firstMeter = -60; // Path loss in the 1st meter
    private double sigma = 1; // Variance
    private double targetAccuracy = 0.9; // Coverage at the boundry
    private int unitPerMeter = 100; // Granularity
    private ArrayList<CustomBeacon> beacons = new ArrayList<>(); // List of physical beacons, default init when MleAlg init

    // TODO: Move prediction outside of locate(), use hashmap for lookup
    public MleAlgorithm(){
        beacons.add(new CustomBeacon(new Point(0,0), 1));
        beacons.add(new CustomBeacon(new Point(0,10), 2));
        beacons.add(new CustomBeacon(new Point(10,10), 3));
        beacons.add(new CustomBeacon(new Point(10,0), 11));
        beacons.add(new CustomBeacon(new Point(0,5), 22));
        beacons.add(new CustomBeacon(new Point(5,0), 33));
        predictInit();

    }

    private void predictInit(){

    }

    public Point locate(ArrayList<CustomReading> readings){
        if(readings.isEmpty()){
            return new Point(-1,-1);
        }

        double dbTolerance = calcTolerance(this.targetAccuracy);
        int x_dim = (int)(this.x_dim_m * this.unitPerMeter);
        int y_dim = (int)(this.y_dim_m * this.unitPerMeter);

        ArrayList<CustomReading> relevantReadings = new ArrayList<>();
        ArrayList<Integer> realIdentifiers = new ArrayList<>();
        for(int b=0; b<beacons.size(); b++){
            realIdentifiers.add(beacons.get(b).id);
        }
        for(int r=0; r<readings.size(); r++){
            int rid = readings.get(r).id.toInt();
            boolean flag = false;
            for(int a=0; a<realIdentifiers.size(); a++){
                if(!flag){
                    if(realIdentifiers.get(a) == rid){
                        flag = true;
                        relevantReadings.add(readings.get(r));
                    }
                }
            }
        }

        int numBeacons = relevantReadings.size();

        double[][][] predictedReadings = new double[x_dim][y_dim][numBeacons];
        int[][] scores = new int[x_dim][y_dim];

        for(int i=0; i<x_dim; i++){
            for(int j=0; j<y_dim; j++){
                for(int k=0; k<numBeacons; k++){
                    double pr = predictReading(getBeaconLocationFromId(relevantReadings.get(k).id.toInt()), new Point(i,j));
                    if(pr == 0){
                        return(new Point(-1,-1));
                    }else{
                        predictedReadings[i][j][k] = pr;
                    }

                }
            }
        }

        // Score matrix
        for(int i=0; i<x_dim; i++){
            for(int j=0; j<x_dim; j++){
                int tot=0;
                for(int k=0; k<numBeacons; k++){
                    if(abs(predictedReadings[i][j][k] - relevantReadings.get(k).value) <= dbTolerance) {
                        tot++;
                    }
                }
                scores[i][j] = tot;
            }
        }

        int highScore = 0;
        int numHigh = 0;
        int x_tot = 0;
        int y_tot = 0;
        for(int i=0; i<x_dim; i++){
            for(int j=0; j<x_dim; j++) {
                if(scores[i][j] >= highScore){
                    if(scores[i][j] > highScore){
                        x_tot = i;
                        y_tot = j;
                        numHigh = 1;
                        highScore = scores[i][j];
                    }else{
                        x_tot += i;
                        y_tot += j;
                        numHigh++;
                    }
                }
            }
        }

        return(new Point((double)x_tot/(numHigh*this.unitPerMeter),(double)y_tot/(numHigh*this.unitPerMeter)));
    }

    private double calcTolerance(double accuracy) {
        return this.sigma * sqrt(2) * Erf.erfcInv(2*(1-accuracy));
    }

    private Point getBeaconLocationFromId(int id){
        for(int i=0; i<beacons.size(); i++){
            if(beacons.get(i).id == id){
                return beacons.get(i).location;
            }
        }
        return null;
    }

    private double predictReading(Point b, Point p){
        if(b == null){
            return 0;
        }else{
            return this.firstMeter - 10*this.alpha*log10(distance(b,p));
        }
    }

    private double distance(Point p1, Point p2){
        return sqrt(pow(p1.x-p2.x,2) + pow(p1.y-p2.y,2));
    }

    public void setBeacons(ArrayList<CustomBeacon> beacons){
        this.beacons.clear();
        this.beacons = beacons;
    }
    public void setX_dim_m(double x_dim_m) {
        this.x_dim_m = x_dim_m;
    }
    public void setY_dim_m(double y_dim_m) {
        this.y_dim_m = y_dim_m;
    }
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
    public void setFirstMeter(double firstMeter) {
        this.firstMeter = firstMeter;
    }
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }
    public void setTargetAccuracy(double targetAccuracy) {
        this.targetAccuracy = targetAccuracy;
    }
    public void setUnitPerMeter(int unitPerMeter) {
        this.unitPerMeter = unitPerMeter;
    }
    public double getX_dim_m() {
        return x_dim_m;
    }
    public double getY_dim_m() {
        return y_dim_m;
    }
    public double getAlpha() {
        return alpha;
    }
    public double getFirstMeter() {
        return firstMeter;
    }
    public double getSigma() {
        return sigma;
    }
    public double getTargetAccuracy() {
        return targetAccuracy;
    }
    public int getUnitPerMeter() {
        return unitPerMeter;
    }
    public ArrayList<CustomBeacon> getBeacons() {
        return beacons;
    }
}
