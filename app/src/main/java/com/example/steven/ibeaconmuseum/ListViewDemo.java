package com.example.steven.ibeaconmuseum;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO CLASS TO BE REMOVED
public class ListViewDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_dataobject);

        List<DataObject> dataObjectList = new ArrayList<>();
        DataObject dataObject = new DataObject("a", "b", "c");
        DataObject dataObject2 = new DataObject("d", "e", "f");

        HashMap<Integer, DataObject> seenHashMap = new HashMap<>();

        dataObjectList.add(dataObject);
        seenHashMap.put(0,dataObject);
        dataObjectList.add(dataObject2);
        seenHashMap.put(1,dataObject2);

        ListView listView = (ListView)findViewById(R.id.listView);
        DataObjectAdapter dataObjectAdapter = new DataObjectAdapter(this, R.layout.data_object_view_layout, dataObjectList);
        listView.setAdapter(dataObjectAdapter);
        //dataObjectAdapter.notifyDataSetChanged();
        dataObjectList.add(new DataObject("g", "h", "i"));
    }
}
