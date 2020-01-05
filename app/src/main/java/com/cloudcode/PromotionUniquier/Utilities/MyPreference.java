package com.cloudcode.PromotionUniquier.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreference {
    private static final String PREF_NAME = "APP_Noti";
    private static final String KEY_FCM = "fcm";


    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getKEY_Fcm(Context context) {
        return getSharedPreferences(context).getString(KEY_FCM, "");
    }

    public static void setKEY_Fcm(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_FCM, newValue);
        editor.commit();
    }
}