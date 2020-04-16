package dcu.ie.sdaiancoadyfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
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
 * EditDevice Class - When the user clicks on the edit button on a device on the recycler view they
 * are brought to this activity, similar enough to the add activity but instead of choosing a device to add
 * you are editing an existing device. The textviews in this activity are prepopulated once it starts and
 * it is then up to the user to change the details.
 *
 * @author Ian Coady 2020.
 */
public class EditDevice extends AppCompatActivity {

    //Global variables
    private static final String TAG = "EditDevice";
    private FirebaseFirestore   deviceDb;
    private EditText            dSerial;
    private TextView            dModel;
    private TextView            dEnv;
    private TextView            dActive;
    int                         serialNumber = 0;
    String activeDate, model, environment;

    /**
     * OnCreate - sets the GUI for the activity and also initializes the text view values by reading
     * the data from the firebase database using the serial number brought from the previous activity.
     *
     * @param savedInstanceState previous intent data - used in this case to read in the serial number from the list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);

        deviceDb    = FirebaseFirestore.getInstance();

        dSerial     = findViewById(R.id.editDevSerial);
        dModel      = findViewById(R.id.editDevType);
        dEnv        = findViewById(R.id.editDevEnv);
        dActive     = findViewById(R.id.editDevDate);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            serialNumber = extras.getInt("Serial");

            dSerial.setText(String.valueOf(serialNumber));
        }

        DocumentReference deviceDetails = deviceDb.collection(getString(R.string.dev_path)).document(String.valueOf(serialNumber));
        deviceDetails.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snap = task.getResult();
                            assert snap != null;
                            if(snap.exists()){

                                //if the device in in the database, get the data and write it to the text views on the activity screen.
                               model         = snap.get("DevModel").toString();
                               environment   = snap.get("DevEnv").toString();
                               activeDate    = snap.get("DevActivationDate").toString();

                               dModel       .setText(String.format(getString(R.string.devType), model));
                               dEnv         .setText(String.format(getString(R.string.devEnv), environment));
                               dActive      .setText(String.format(getString(R.string.devActive), activeDate));

                            }
                        }

                        else{
                            Log.i(TAG,"Device Not found");
                            Intent returnToHome = new Intent(getApplicationContext(),DeviceList.class);
                            startActivity(returnToHome);
                            Toast.makeText(getApplicationContext(), R.string.db_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    /**
     * REF: https://firebase.google.com/docs/firestore/manage-data/delete-data
     *
     * I took an alternate approach to editing and deleting devices, rather than using listeners in on create I put them in independant
     * methods because this allows the edit method to use the delete method which will be explained later.
     *
     * For this method - when the button is pressed the device is deleted from the database using the serial number and the user
     * is returned to the list where they will no longer see the device.
     *
     * @param v the view being passed to the method.
     */
    public void deleteButton(View v){
        deviceDb.collection(getString(R.string.dev_path)).document(String.valueOf(serialNumber))
            .delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(TAG, "onSuccess: Device Deleted");
                    Intent returnToHome = new Intent(getApplicationContext(), DeviceList.class);
                    startActivity(returnToHome);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: Error Deleting Device");
                }
            });
    }

    /**
     * As I eluded to above the save/edit button uses the code from the delete method inside of here, because this is a NoSQL database solution
     * I had to delete the device from the database and add a new one, I could not edit a database collection as then I would end up with duplicate entries in the database.
     *
     * Similar to the addDevice class, this class checks the user input valid serialNumber data, checked if it existed already and if not, then added it to the database.
     *
     * @param v view being passed to the method.
     */
    public void saveButton(final View v) {

        final String serialNum = dSerial.getText().toString();

        if(serialNum.equals("")) {
            serialNumber = 0;
        }

        //Calling the delete method in here before the serial number gets changed.
        else{
            deleteButton(v);
            serialNumber = Integer.parseInt(serialNum);
        }

        if (serialNumber < 11111111 || serialNumber > 99999999) {
            Toast.makeText(getApplicationContext(), R.string.serial_toast_error, Toast.LENGTH_SHORT).show();
        } else {
            //REF: https://firebase.google.com/docs/firestore/query-data/get-data
            DocumentReference docRef = deviceDb.collection(getString(R.string.dev_path)).document(String.valueOf(serialNumber));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snap = task.getResult();
                        if (snap.exists()) {
                            Toast.makeText(getApplicationContext(), R.string.dev_exists_error, Toast.LENGTH_SHORT).show();
                        }

                        else {

                            Map<String, Object> deviceDetails = new HashMap<>();
                            deviceDetails.put("DevModel", model);
                            deviceDetails.put("DevEnv", environment);
                            deviceDetails.put("DevActivationDate", activeDate);

                            deviceDb.collection(getString(R.string.dev_path)).document(String.valueOf(serialNumber)).set(deviceDetails);

                            Toast.makeText(getApplicationContext(), R.string.dev_edited, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
