package com.example.steven.ibeaconmuseum;

import com.example.steven.ibeaconmuseum.GenericPair;

import org.altbeacon.beacon.Identifier;

import java.util.ArrayList;
import java.util.HashMap;

public class GenericPairList {

    public ArrayList<GenericPair<Identifier, Double>> list = new ArrayList<GenericPair<Identifier, Double>>();

    public GenericPairList(ArrayList<GenericPair<Identifier, Double>> list){

        this.list = list;
    }




}
