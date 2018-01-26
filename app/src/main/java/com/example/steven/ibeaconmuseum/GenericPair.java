package com.example.steven.ibeaconmuseum;

/**
 * Created by Steven on 1/20/2018.
 */

public class GenericPair<T, S> {

    private T first;
    private S second;
    public GenericPair(T f, S s){
        first = f;
        second = s;
    }

    public T getFirst() { return first; }
    public S getSecond() { return second; }

}
