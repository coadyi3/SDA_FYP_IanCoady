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
import android.widget.Toast;

import java.util.Collections;
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
 * Some of this code was adapted from the SDA 2020 course materials.
 * All used with permissions of the code writers at DCU circa 2019/2020.
 *
 * More specifically it was taken from the FireChat App in the course notes located at : https://github.com/OpenEducationDCU/SDA-2019
 *
 * This activity is a Google/Gmail sign in activity. The user is prompted with a google sign in button and once clicked
 * they are either taken to a Gmail login screen or prompted to choose a Gmail account already configred on the device to proceed
 * into the app. There is no alternate way to sign in and access the app.
 *
 * @author Chris Coughlan, Richard Boldger.
 */

public class LoginScreen extends AppCompatActivity {


    //onActivityResult Key.
    private static final int RC_SIGN_IN = 1;

    /**
     * OnCreate Method calls the GUI.xml file and inflates it on the screen.
     * The GUI elements are assigned to variables in here and listeners are also set inside this section.
     *
     * Typically the user will click on the google Sign in button on the screen and they will either
     * be prompted to select an account already loaded on their device or prompted to sign in with
     * an existing Gmail account. Gmail is the only accepted account type for this app.
     *
     * @param savedInstanceState previous intent data would be saved here, not in this case.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        final AuthUI.IdpConfig providers = new AuthUI.IdpConfig.GoogleBuilder().build();
        SignInButton googleSignIn = findViewById(R.id.sign_in_button);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
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

    /**
     * OnActivityResult takes the result of the attempted Google sign in. If a user signs in with a valid
     * Gmail account then they can proceed into the app, if not, they're prompted to try again.
     *
     * @param requestCode The response code requested from the activity, if it matches with a valid requestCode, request to sign in is considered valid.
     * @param resultCode  The resultCode expected from the activity, if the resultCode matches expected resultCode, sign in attempt is valid.
     * @param data      N/A
     */
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

                else {
                    Toast.makeText(getApplicationContext(), R.string.failed_login, Toast.LENGTH_SHORT).show();
                }
            }

            else{
                Toast.makeText(getApplicationContext(), R.string.failed_login, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
