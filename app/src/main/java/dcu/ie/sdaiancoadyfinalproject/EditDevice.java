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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditDevice extends AppCompatActivity {
    private static final String TAG = "EditDevice";
    private FirebaseFirestore   deviceDb;
    private EditText            dSerial;
    private TextView            dModel;
    private TextView            dEnv;
    private TextView            dActive;
    int                         serialNumber = 0;
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
                               String model         = snap.get("DevModel").toString();
                               String environment   = snap.get("DevEnv").toString();
                               String activeDate    = snap.get("DevActivationDate").toString();

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
                    Toast.makeText(getApplicationContext(),"Device Deleted", Toast.LENGTH_SHORT).show();
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

    public void saveButton(View v){
        
    }

}
