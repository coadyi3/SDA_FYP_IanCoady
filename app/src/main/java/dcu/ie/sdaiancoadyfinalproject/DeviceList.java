package dcu.ie.sdaiancoadyfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DeviceList extends AppCompatActivity {

    private static final String TAG = "DeviceList";
    private FirebaseFirestore dFireStore;

    public DeviceList(){
        //Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dFireStore = FirebaseFirestore.getInstance();

        final RecyclerView rv             = findViewById(R.id.deviceView_view);
        FloatingActionButton fab    = findViewById(R.id.addBtn);
        final ArrayList<Integer> dDeviceID = new ArrayList<>();
        final ArrayList<String> dDeviceModel = new ArrayList<>();
        final ArrayList<String> dDeviceEnv = new ArrayList<>();

        dFireStore.collection("Devices")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " = " + document.getData());
                                int id = Integer.parseInt(document.getId());
                                String model = document.getString("DevModel");
                                String env = document.getString("DevEnv");

                                dDeviceID.add(id);
                                dDeviceModel.add(model);
                                dDeviceEnv.add(env);
                            }
                        }

                        else{
                            Log.d(TAG,"Error collecting Data: "+ task.getException());
                        }

                        DeviceViewAdapter rvAdapter = new DeviceViewAdapter(getBaseContext(), dDeviceID, dDeviceModel, dDeviceEnv);
                        rv.setAdapter(rvAdapter);
                        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAddActivity = new Intent(getApplicationContext(), AddDevice.class);
                startActivity(openAddActivity);
            }
        });

    }
}
