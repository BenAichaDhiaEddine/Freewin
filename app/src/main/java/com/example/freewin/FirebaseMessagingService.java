package com.example.freewin;

import android.app.Service;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
        @Override
        public void onNewToken(String s) {
            super.onNewToken(s);
            Log.d("newToken", s);
            getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();

        }

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);
        }

    }
