package dcu.ie.sdaiancoadyfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class AddDevice extends AppCompatActivity {

    private Spinner devModelSpinner;
    private Spinner devEnvSpinner;
    private TextView deviceSerial;
    private Button addDeviceBtn;
    private FirebaseFirestore deviceDb;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_add_device);

        deviceDb = FirebaseFirestore.getInstance();
        final CollectionReference deviceList = deviceDb.collection("Devices");

        deviceSerial = findViewById(R.id.serialNumber);
        addDeviceBtn = findViewById(R.id.button);
        devModelSpinner = findViewById(R.id.spinnerDevType);
        devEnvSpinner = findViewById(R.id.spinnerDevEnv);
        ArrayAdapter<CharSequence> modelAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.device_entries, R.layout.spinner);
        ArrayAdapter<CharSequence> envAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.environments, R.layout.spinner);
        devEnvSpinner.setAdapter(envAdapter);
        devEnvSpinner.setEnabled(true);
        devModelSpinner.setAdapter(modelAdapter);
        devModelSpinner.setEnabled(true);

        addDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int serial = Integer.parseInt(deviceSerial.getText().toString());
                final String serialNum = deviceSerial.getText().toString();

                if (serial < 11111111 || serial > 99999999) {
                    Toast.makeText(getApplicationContext(), "Serial Number incompatible: Try Again", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentReference docRef = deviceDb.collection("Devices").document(serialNum);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot snap = task.getResult();
                                if (snap.exists()) {
                                    Toast.makeText(getApplicationContext(), "Device already exists!", Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Date currentDayTime = java.util.Calendar.getInstance().getTime();
                                    String currentDate = currentDayTime.toString();

                                    Map<String, Object> deviceDetails = new HashMap<>();
                                    deviceDetails.put("DevModel", devModelSpinner.getSelectedItem().toString());
                                    deviceDetails.put("DevEnv", devEnvSpinner.getSelectedItem().toString());
                                    deviceDetails.put("DevActivationDate", currentDate);

                                    deviceList.document(serialNum).set(deviceDetails);

                                    Toast.makeText(getApplicationContext(), "Device created!", Toast.LENGTH_SHORT).show();
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
