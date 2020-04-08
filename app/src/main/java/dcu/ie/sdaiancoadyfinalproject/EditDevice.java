package dcu.ie.sdaiancoadyfinalproject;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditDevice extends AppCompatActivity {

    private FirebaseFirestore deviceDb;
    private EditText dSerial;
    private TextView dModel;
    private TextView dEnv;
    private TextView dActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);

        dSerial     = findViewById(R.id.editDevSerial);
        dModel      = findViewById(R.id.editDevType);
        dEnv        = findViewById(R.id.editDevEnv);
        dActive     = findViewById(R.id.editDevDate);

        int serialNumber;
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            serialNumber = extras.getInt("Serial");

            dSerial.setText(String.valueOf(serialNumber));
        }


    }


}
