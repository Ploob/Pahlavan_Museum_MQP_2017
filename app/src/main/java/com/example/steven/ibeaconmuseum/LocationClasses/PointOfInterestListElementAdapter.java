package com.example.steven.ibeaconmuseum.LocationClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.steven.ibeaconmuseum.R;

import java.util.List;

/**
 * Created by Steven on 1/10/2018.
 */

public class PointOfInterestListElementAdapter extends ArrayAdapter<PointOfInterestListElement>{

    private int layoutResource;

    public PointOfInterestListElementAdapter(Context context, int layoutResource, List<PointOfInterestListElement> poiles){
        super(context, layoutResource, poiles);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        PointOfInterestListElement poile = getItem(position);

        if(poile != null){
            TextView leftTextView = view.findViewById(R.id.poiInfo1);
            TextView rightTextView = view.findViewById(R.id.poiInfo2);
            TextView centerTextView = view.findViewById(R.id.poiInfo3);

            if(leftTextView != null){
                leftTextView.setText(poile.getLeft());
            }
            if(rightTextView != null){
                rightTextView.setText(poile.getRight());
            }
            if(centerTextView != null){
                centerTextView.setText(poile.getCenter());
            }
        }

        return view;

    }


}
