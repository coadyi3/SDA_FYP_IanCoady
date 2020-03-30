package dcu.ie.sdaiancoadyfinalproject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AddDevice extends AppCompatActivity {

    private Spinner devModelSpinner;
    private Spinner devEnvSpinner;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_add_device);

        devModelSpinner     =  findViewById(R.id.spinnerDevType);
        devEnvSpinner       = findViewById(R.id.spinnerDevEnv);
        ArrayAdapter<CharSequence> modelAdapter     = ArrayAdapter.createFromResource(getApplicationContext(),R.array.device_entries, R.layout.spinner);
        ArrayAdapter<CharSequence> envAdapter       = ArrayAdapter.createFromResource(getApplicationContext(),R.array.environments, R.layout.spinner);
        devEnvSpinner.setAdapter(envAdapter);
        devEnvSpinner.setEnabled(true);
        devModelSpinner.setAdapter(modelAdapter);
        devModelSpinner.setEnabled(true);

    }


}
