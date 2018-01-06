package com.example.steven.ibeaconmuseum.LocationClasses;

import org.altbeacon.beacon.Identifier;

import java.util.Objects;

/**
 * Created by Steven on 1/5/2018.
 */

public final class CompositeKeyXYIdentifier_PredictedRssi {

    private final int x;
    private final int y;
    private final Identifier id;

    public CompositeKeyXYIdentifier_PredictedRssi(int x, int y, Identifier id){
        this.x = Objects.requireNonNull(x);
        this.y = Objects.requireNonNull(y);
        this.id = (Identifier) Objects.requireNonNull(id);

    }

    public int x(){ return x; }
    public int y(){ return y; }
    public Identifier id(){ return id; }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CompositeKeyXYIdentifier_PredictedRssi other = (CompositeKeyXYIdentifier_PredictedRssi) obj;
        return Objects.equals(x, other.x) && Objects.equals(y, other.y) && Objects.equals(id, other.id);
    }

    public int hashCode(){
        return Objects.hash(x,y,id);

    }

}
