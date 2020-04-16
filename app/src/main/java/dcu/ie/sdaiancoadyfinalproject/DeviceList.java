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

/**
 * This activity populates the content of the recycler view of the main device list with the entries
 * in the firebase database.
 */
public class DeviceList extends AppCompatActivity {

    private static final String TAG = "DeviceList";
    private FirebaseFirestore dFireStore;

    /**
     * Required empty public constructor
     */
    public DeviceList(){
    }


    /**
     * OnCreate method called when the activity opens first, sets all the gui elements and listeners.
     * Also Gets all the devices from the Firebase Database and populates each individual RV element.
     *
     * Also inside of this method a listener is set on the floating action button.
     *
     * @param savedInstanceState    Intent data from previous activities. Blank in this case.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        /*
         * Sets the GUI using activity_main.xml layout file.
         */
        setContentView(R.layout.activity_main);

        dFireStore = FirebaseFirestore.getInstance();

        final RecyclerView rv                   = findViewById(R.id.deviceView_view);
        FloatingActionButton fab                = findViewById(R.id.addBtn);
        final ArrayList<Integer> dDeviceID      = new ArrayList<>();
        final ArrayList<String> dDeviceModel    = new ArrayList<>();
        final ArrayList<String> dDeviceEnv      = new ArrayList<>();

        /*
         * collection().get() retrieves all entries in the Firebase database and adds them to the respective ArrayLists.
         */
        dFireStore.collection(getString(R.string.dev_path))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " = " + document.getData());

                                //Retrieves the entries from the database and assigns them to variables.
                                int id          = Integer.parseInt(document.getId());
                                String model    = document.getString("DevModel");
                                String env      = document.getString("DevEnv");

                                //Add's the variables above to the respective arraylist.
                                dDeviceID       .add(id);
                                dDeviceModel    .add(model);
                                dDeviceEnv      .add(env);
                            }
                        }

                        else{
                            Log.d(TAG,"Error collecting Data: "+ task.getException());
                        }

                        //Sets the device adapter to the Recycler View. Passes the ArrayLists to the adapter to populate the test fields.
                        DeviceViewAdapter rvAdapter = new DeviceViewAdapter(getBaseContext(), dDeviceID, dDeviceModel, dDeviceEnv);
                        rv.setAdapter(rvAdapter);
                        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    }
                });

        //Floating action button listener, clicking this button will open the AddDevice activity.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAddActivity = new Intent(getApplicationContext(), AddDevice.class);
                startActivity(openAddActivity);
            }
        });

    }
}
