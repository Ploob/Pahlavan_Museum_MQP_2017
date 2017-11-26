package com.example.steven.ibeaconmuseum.DataObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.steven.ibeaconmuseum.R;

import java.util.List;

/**
 * Created by Steven on 11/15/2017.
 */

public class DataObjectAdapter extends ArrayAdapter<DataObject>{

    private int layoutResource;

    public DataObjectAdapter(Context context, int layoutResource, List<DataObject> dataObjects){
        super(context, layoutResource, dataObjects);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View view = convertView;

        if(view == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        DataObject dataObject = getItem(position);

        if(dataObject != null){
            TextView leftTextView = (TextView)view.findViewById((R.id.leftTextView));
            TextView rightTextView = (TextView)view.findViewById((R.id.rightTextView));
            TextView centerTextView = (TextView)view.findViewById((R.id.centerTextView));

            if(leftTextView != null){
                leftTextView.setText(dataObject.getLeft());
            }

            if(rightTextView != null){
                leftTextView.setText(dataObject.getRight());
            }

            if(centerTextView != null){
                leftTextView.setText(dataObject.getCenter());
            }
        }

        return view;

    }

}
