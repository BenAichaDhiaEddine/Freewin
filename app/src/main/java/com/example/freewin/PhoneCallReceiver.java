package com.example.freewin;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class PhoneCallReceiver extends BroadcastReceiver {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    String user_id = FirebaseAuth.getInstance().getUid();

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new CustomPhoneStateListener(context), PhoneStateListener.LISTEN_CALL_STATE);
    }

    public class CustomPhoneStateListener extends PhoneStateListener {
        //private static final String TAG = "PhoneStateChanged";
        Context context; //Context to make Toast if required

        public CustomPhoneStateListener(Context context) {
            super();
            this.context = context;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING: {
                    //add 50 points
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int p = Integer.parseInt(dataSnapshot.child("Users").child(user_id).child("points").getValue().toString());
                            p += 50;
                            db.child("Users").child(user_id).child("points").setValue(p);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    //start Video activity
                    Intent i = new Intent(context, Video.class);
                    i.putExtra("video", "ringing");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    context.startActivity(i);
                    break;
                }
                case TelephonyManager.CALL_STATE_IDLE: {
                    //add 50 points
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int p = Integer.parseInt(dataSnapshot.child("Users").child(user_id).child("points").getValue().toString());
                            p += 50;
                            db.child("Users").child(user_id).child("points").setValue(p);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    if (!Video.active) {
                        //start Video activity
                        Intent i = new Intent(context, Video.class);
                        i.putExtra("video", "idle");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        context.startActivity(i);
                        break;
                    }
                }
                default:
                    break;
            }
        }
    }
}

