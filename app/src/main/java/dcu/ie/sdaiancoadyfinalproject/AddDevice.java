package dcu.ie.sdaiancoadyfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

/**
 * AddDevice class - This class prompts the user to choose a device model and environment located, user
 * is also prompted to enter the device serial number, this is expected behaviour even on the existing
 * web app this application is based on, the user is required to have the device to hand to enter
 * the serial number manually. Must fit the 8 digit format asked for.
 *
 * Once serial number criteria are met, the app will check if a similar device already exists in the database and
 * if not, add it to the database.
 *
 * @author Ian Coady 2020
 */
public class AddDevice extends AppCompatActivity {
    //Global variables
    private static final String TAG = "AddDevice";

    private Spinner devModelSpinner;
    private Spinner devEnvSpinner;
    private TextView deviceSerial;
    private Button addDeviceBtn;
    private FirebaseFirestore deviceDb;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_add_device);

        //Local variable declarations and initializations.
                                    deviceDb            = FirebaseFirestore.getInstance();
        final   CollectionReference deviceList          = deviceDb.collection(getString(R.string.dev_path));
                                    deviceSerial        = findViewById(R.id.editDevSerial);
                                    addDeviceBtn        = findViewById(R.id.button);
                                    devModelSpinner     = findViewById(R.id.spinnerDevType);
                                    devEnvSpinner       = findViewById(R.id.spinnerDevEnv);
                ArrayAdapter<CharSequence> modelAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.device_entries, R.layout.spinner);
                ArrayAdapter<CharSequence> envAdapter   = ArrayAdapter.createFromResource(getApplicationContext(), R.array.environments, R.layout.spinner);

        //Spinner adapters called and enabled.
        devEnvSpinner       .setAdapter(envAdapter);
        devEnvSpinner       .setEnabled(true);
        devModelSpinner     .setAdapter(modelAdapter);
        devModelSpinner     .setEnabled(true);

        /**
         *  On click listener for the add device button, couple of things happen here before a device is
         *  added to the database:
         *      - The app reads the value in the edit text and assigns it to a variable.
         *      - The app checks if the user input a valid serial number (8 Digit Int value)
         *      - The app then queries the database to see if a similar device already exists.
         *      - If a device exists, ask the user to try again.
         *      - If no device is found then add the device to the database and return the user to the device list activity.
         */
        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int serial = 0;
                final String serialNum = deviceSerial.getText().toString();

                if(serialNum.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.serial_toast_error, Toast.LENGTH_SHORT).show();
                }
                else{
                    serial = Integer.parseInt(serialNum);
                }

                if (serial < 11111111 || serial > 99999999) {
                    Log.i(TAG, "onClick: serial number is invalid or wrong format.");

                    Toast.makeText(getApplicationContext(), R.string.serial_toast_error, Toast.LENGTH_SHORT).show();
                }
                else{

                    //REF: https://firebase.google.com/docs/firestore/query-data/get-data
                    DocumentReference docRef = deviceDb.collection(getString(R.string.dev_path)).document(serialNum);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful())
                            {
                                DocumentSnapshot snap = task.getResult();

                                if (snap.exists()) {
                                    Log.i(TAG, "onComplete: Device Already exists in database.");
                                    Toast.makeText(getApplicationContext(), R.string.dev_exists_error, Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Log.i(TAG, "onComplete: No device found, good to add to database.");

                                    Date currentDayTime                 = java.util.Calendar.getInstance().getTime();
                                    String currentDate                  = currentDayTime.toString();
                                    Map<String, Object> deviceDetails   = new HashMap<>();

                                    deviceDetails.put("DevModel", devModelSpinner.getSelectedItem().toString());
                                    deviceDetails.put("DevEnv", devEnvSpinner.getSelectedItem().toString());
                                    deviceDetails.put("DevActivationDate", currentDate);

                                    deviceList.document(serialNum).set(deviceDetails);

                                    Toast.makeText(getApplicationContext(), R.string.dev_created, Toast.LENGTH_SHORT).show();

                                    Intent returnToList = new Intent(getApplicationContext(), DeviceList.class);
                                    startActivity(returnToList);

                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
