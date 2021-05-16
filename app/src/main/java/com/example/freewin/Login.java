package com.example.freewin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {
    private static final int My_REQUEST_CODE = 7117; // random number
    List<AuthUI.IdpConfig> providers; // login providers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //check if user isnt signed in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // set firebase  login UI
            providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build()
            );
            // show firebase login ui
            showSignInoptions();
        }
        //check if user is allready signed in
        else if (FirebaseAuth.getInstance().getCurrentUser() != null)
            Move_To_Main();
    }

    // firebase  Login UI
    public void showSignInoptions() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.Authentifacation)
                        .setAlwaysShowSignInMethodScreen(true)
                        .setLogo(R.drawable.ic_user)
                        .setIsSmartLockEnabled(false)
                        .build(), My_REQUEST_CODE
        );
    }
    //firebase after sign in process
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == My_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            //get user database
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String user_id = user.getUid();
            final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference users_db = FirebaseDatabase.getInstance().getReference().child("Users");
            final DatabaseReference current_user_points_db = users_db.child(user_id).child("points");
            //if sign in is succesfull
            if (resultCode == RESULT_OK) {
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.child("Users").child(user_id).exists()) {
                            //put 0 points to user if he logged in first time
                            current_user_points_db.setValue("0");
                            //put code parainage
                            int code = Integer.parseInt(dataSnapshot.child("code").getValue().toString());
                            code+=1;
                            db.child("codeParainage").child(user_id).setValue(code);
                            db.child("code").setValue(String.valueOf(code));
                            //put user FCM token
                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            String token = task.getResult().getToken();
                                            users_db.child(user_id).child("FcmToken").setValue(token);
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                //move to main activity
                Move_To_Main();
                //Toast current user
                Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
            } else {
                // Sign in failed
                if (null == response) {
                    Log.e("Login", "Login canceled by User");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login", "No Internet Connection");
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login", "Unknown Error");
                    return;
                }
            }
            Log.e("Login", "Unknown sign in response");
        }
    }

    public void Move_To_Main() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }



}


