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

public class EditDevice extends AppCompatActivity {
    private static final String TAG = "EditDevice";
    private FirebaseFirestore   deviceDb;
    private EditText            dSerial;
    private TextView            dModel;
    private TextView            dEnv;
    private TextView            dActive;
    int                         serialNumber = 0;
    String activeDate, model, environment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);

        deviceDb = FirebaseFirestore.getInstance();

        dSerial     = findViewById(R.id.editDevSerial);
        dModel      = findViewById(R.id.editDevType);
        dEnv        = findViewById(R.id.editDevEnv);
        dActive     = findViewById(R.id.editDevDate);
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            serialNumber = extras.getInt("Serial");

            dSerial.setText(String.valueOf(serialNumber));
        }

        DocumentReference deviceDetails = deviceDb.collection("Devices").document(String.valueOf(serialNumber));
        deviceDetails.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snap = task.getResult();
                            if(snap.exists()){
                               model         = snap.get("DevModel").toString();
                               environment   = snap.get("DevEnv").toString();
                               activeDate    = snap.get("DevActivationDate").toString();

                               dModel       .setText("Device Type: "+model);
                               dEnv         .setText("Device Environment: "+environment);
                               dActive      .setText("Active Since: " +activeDate);

                            }
                        }

                        else{
                            Log.i(TAG,"Device Not found");
                            Intent returnToHome = new Intent(getApplicationContext(),DeviceList.class);
                            startActivity(returnToHome);
                            Toast.makeText(getApplicationContext(),"Error: Device not logged in database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    public void deleteButton(View v){
        deviceDb.collection("Devices").document(String.valueOf(serialNumber))
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

    public void saveButton(final View v) {

        final String serialNum = dSerial.getText().toString();

        if(serialNum.equals("")) {
            serialNumber = 0;
        }
        else{
            serialNumber = Integer.parseInt(serialNum);
        }

        if (serialNumber < 11111111 || serialNumber > 99999999) {
            Toast.makeText(getApplicationContext(), "Serial Number incompatible: Try Again", Toast.LENGTH_SHORT).show();
        } else {
            DocumentReference docRef = deviceDb.collection("Devices").document(String.valueOf(serialNumber));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snap = task.getResult();
                        if (snap.exists()) {
                            Toast.makeText(getApplicationContext(), "Device already exists!", Toast.LENGTH_SHORT).show();
                        }

                        else {

                            Map<String, Object> deviceDetails = new HashMap<>();
                            deviceDetails.put("DevModel", model);
                            deviceDetails.put("DevEnv", environment);
                            deviceDetails.put("DevActivationDate", activeDate);

                            deviceDb.collection("Devices").document(String.valueOf(serialNumber)).set(deviceDetails);

                            Toast.makeText(getApplicationContext(), "Device Edited!", Toast.LENGTH_SHORT).show();
                            deleteButton(v);
                        }
                    }
                }
            });
        }
    }
}
