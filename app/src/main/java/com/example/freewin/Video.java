package com.example.freewin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class Video extends AppCompatActivity {
    public static boolean leave ;
    public static boolean active;
    ImageView imageView;
    // a is the intent string from button
    public  String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        imageView = findViewById(R.id.Video_imageView);
        //get the string from intent
        a = getIntent().getStringExtra("video");
        //if user click the button
        if (a != null && (a.equals("button") )) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!leave){
                        //add 100 points
                        int p = Integer.parseInt(MainActivity.Points.getText().toString());
                        p += 100;
                        String user_id = FirebaseAuth.getInstance().getUid();
                        DatabaseReference current_user_points = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("points");
                        current_user_points.setValue(p);
                    }
                    finish();
                }
            }, 5000);
        }
        //if intent was send by screen Unlock
        if (a!=null && a.equals("screenUnlock") ){
            {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!leave){
                            //add 100 points
                            int p = Integer.parseInt(MainActivity.Points.getText().toString());
                            p += 100;
                            String user_id = FirebaseAuth.getInstance().getUid();
                            DatabaseReference current_user_points = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("points");
                            current_user_points.setValue(p);
                        }
                        finishAffinity();
                    }
                }, 5000);
            }
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //the image uri from firebase
        String imageURI ="https://firebasestorage.googleapis.com/v0/b/freewin-44477.appspot.com/o/89120249_2520183338231825_2116490167559651328_o.jpg?alt=media&token=482099dc-73d4-4e4e-b036-6761ce3538ed";
        Picasso.get().load(imageURI).into(imageView);
    }
    @Override
    public void onBackPressed() {
        if (a!=null && a.equals("idle")) {
            //when press back or press on screen  move to main and close the app
            Intent start = new Intent(Video.this, MainActivity.class);
            start.putExtra("video", "idle");
            this.startActivity(start);
        }
        if (a!=null && a.equals("ringing")){
            finishAffinity();
        }
        else {

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        active=true;
        leave=false;
    }
    @Override
    protected void onStop() {
        active=false;
        super.onStop();
    }

    @Override
    protected void onUserLeaveHint() {
        leave=true;
        super.onUserLeaveHint();
    }

    @Override
    protected void onDestroy() {
        leave=true;
        super.onDestroy();
    }

}
