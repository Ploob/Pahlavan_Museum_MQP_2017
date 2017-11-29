package com.example.steven.ibeaconmuseum;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// TODO CLASS TO BE REMOVED
// Class currently exists to test isolated ListView implementations
public class ListViewDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_dataobject);

        List<DataObject> dataObjectList = new ArrayList<>();
        DataObject dataObject = new DataObject("a", "b", "c");
        DataObject dataObject2 = new DataObject("d", "e", "f");
        DataObject dataObject3 = new DataObject("g", "h", "i");
        HashMap<Integer, DataObject> seenHashMap = new HashMap<>();

        //dataObjectList.add(dataObject);
        seenHashMap.put(0,dataObject);
        //dataObjectList.add(dataObject2);
        seenHashMap.put(1,dataObject2);
        seenHashMap.put(2,dataObject3);

        ListView listView = (ListView)findViewById(R.id.listView);
        DataObjectAdapter dataObjectAdapter = new DataObjectAdapter(this, R.layout.data_object_view_layout, dataObjectList);
        listView.setAdapter(dataObjectAdapter);



        if(seenHashMap.get(3) == null){
            seenHashMap.put(3, new DataObject("d", "d", "d"));
        }

        if(seenHashMap.get(3) != null){
            seenHashMap.get(3).setCenter("updated value");
        }

        dataObjectList.clear();
        Iterator iterator = seenHashMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, DataObject> mentry = (Map.Entry)iterator.next();
            dataObjectList.add(mentry.getValue());
        }

    }
}
