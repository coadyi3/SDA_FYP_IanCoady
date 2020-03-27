package dcu.ie.sdaiancoadyfinalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import java.util.Collections;

public class LoginScreen extends AppCompatActivity {


    //onActivityResult Key.
    private static final int RC_SIGN_IN = 1;

    SharedPreferences mUserDetails = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserDetails = getSharedPreferences("user_details", MODE_PRIVATE);
        setContentView(R.layout.activity_login_screen);

        mUserDetails.edit().clear().apply();

        Bundle extras = getIntent().getExtras();
        String isLoggedOut;
        if (extras != null) {
            isLoggedOut = extras.getString("EXTRA_MESSAGE");
            if(isLoggedOut != null &&isLoggedOut.equals("INIG_LOGOUT")) {

            }
        }

        final AuthUI.IdpConfig providers = new AuthUI.IdpConfig.GoogleBuilder().build();
        SignInButton googleSign = findViewById(R.id.sign_in_button);

        googleSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Collections.singletonList(providers))
                                .build(),
                        RC_SIGN_IN);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent mainActivity = new Intent(this, DeviceList.class);
                if (user != null) {
                    startActivity(mainActivity);
                }
                // ...
            } else {

            }
        }
    }

}
