package dcu.ie.sdaiancoadyfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DeviceList extends AppCompatActivity {

    public DeviceList(){
        //Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        RecyclerView rv             = findViewById(R.id.deviceView_view);
        FloatingActionButton fab    = findViewById(R.id.addBtn);

        ArrayList<Integer> dDeviceID = new ArrayList<>();
        ArrayList<String> dDeviceModel = new ArrayList<>();
        ArrayList<String> dDeviceEnv = new ArrayList<>();

        dDeviceID.add(123456);
        dDeviceModel.add("PAX S500");
        dDeviceEnv.add("Cert");

        DeviceViewAdapter rvAdapter = new DeviceViewAdapter(getBaseContext(), dDeviceID, dDeviceModel, dDeviceEnv);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAddActivity = new Intent(getApplicationContext(), AddDevice.class);
                startActivity(openAddActivity);
            }
        });

    }
}
