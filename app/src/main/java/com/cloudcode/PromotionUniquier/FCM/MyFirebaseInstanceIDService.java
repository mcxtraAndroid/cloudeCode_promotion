package com.cloudcode.PromotionUniquier.FCM;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cloudcode.PromotionUniquier.Utilities.Config;
import com.cloudcode.PromotionUniquier.Utilities.MyPreference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();



        if (refreshedToken != null) {

            MyPreference.setKEY_Fcm(getApplicationContext(),refreshedToken);
            sendRegistrationToServer(refreshedToken);
            Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", refreshedToken);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }



    }

    private void sendRegistrationToServer(final String token) {
        Log.e(TAG, "sendRegistrationToServer: " + token);
        MyPreference.setKEY_Fcm(getApplicationContext(),token);

    }


}