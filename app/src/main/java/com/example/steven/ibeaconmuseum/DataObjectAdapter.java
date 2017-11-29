package com.example.steven.ibeaconmuseum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

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
            TextView leftTextView = view.findViewById((R.id.leftTextView));
            TextView rightTextView = view.findViewById((R.id.rightTextView));
            TextView centerTextView = view.findViewById((R.id.centerTextView));

            if(leftTextView != null){
                leftTextView.setText(dataObject.getLeft());
            }
            if(rightTextView != null){
                rightTextView.setText(dataObject.getRight());
            }
            if(centerTextView != null){
                centerTextView.setText(dataObject.getCenter());
            }
        }

        return view;
    }
}
