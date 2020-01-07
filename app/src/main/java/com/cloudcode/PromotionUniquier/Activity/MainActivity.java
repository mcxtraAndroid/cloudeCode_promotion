package com.cloudcode.PromotionUniquier.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cloudcode.PromotionUniquier.Model.LoginResponse;
import com.cloudcode.PromotionUniquier.Networkin.Login_Model_;
import com.cloudcode.PromotionUniquier.R;
import com.cloudcode.PromotionUniquier.Utilities.AppController;
import com.cloudcode.PromotionUniquier.Utilities.MyPreference;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private WebView mywebview;
    private BroadcastReceiver broadcastReceiver;
    String url = "http://pu.mycit.co.in/server/login.php";
  //  String url = "http://pu.mycit.co.in/server/index.php";


    String val = "";
    AppController appController;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    //  private final Handler handler = new Handler(this);
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appController = (AppController) getApplication();
        mywebview = (WebView) findViewById(R.id.webView);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.getSettings().setDomStorageEnabled(true);
        mywebview.getSettings().setAllowFileAccessFromFileURLs(true);
        mywebview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mywebview.getSettings().setBuiltInZoomControls(true);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();

        mywebview.loadUrl(url);

        mywebview.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.endsWith(".pdf")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    try {
                        view.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        //user does not have a pdf viewer installed
                    }
                } else {
                    //  mywebview.loadUrl(url);

                    //    view.loadUrl(url);
                    CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(view.getContext());
                    cookieSyncManager.startSync();
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
                    //   cookieManager.removeSessionCookie();
                    //   cookieManager.setCookie(url, COOKIE);
                    cookieSyncManager.sync();

                    mywebview.getSettings().setJavaScriptEnabled(true);
                    mywebview.loadUrl(url);


                }
                return true;


            }


            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookies = CookieManager.getInstance().getCookie(url);
                Log.d("", "All the cookies in a string:" + cookies);

                if (cookies == null) {
                    return;
                } else {

                    Map<String, String> map = new HashMap<>();
                    String str = "ABCD 1 key1=value1 key2=value2 key3=value3";
                    String pattern = "\\b([^\\s]+)=([^\\s]+)\\b";

                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(cookies);

                    while (m.find()) {
                        System.out.println("Found a key/value: (" + m.group(1) + ", " + m.group(2) + ")");
                        map.put(m.group(1), m.group(2));
                    }
                    map.size();

                    if (map.containsKey("userid")) {
                        val = map.get("userid");
                        System.out.println("The Value mapped to Key 4 is:" + val);


                        if (!(val.equals(null) || val.equals("") || val.isEmpty())) {


                            if (appController.inNetwork()) {

                                postLogin(val, "", MyPreference.getKEY_Fcm(getApplicationContext()));
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.net_required), Toast.LENGTH_LONG).show();


                            }
                        }

                    }
                    //   view.loadUrl(url);
                }


            }

        });

        mywebview.setWebChromeClient(new WebChromeClient()
        {
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
            {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
    private void postLogin(String username, String password, String token) {
        Log.e("", "postLogin: " + token);
        AppController app = AppController.getInstance();

        Login_Model_ apiservices = app.getClient().create(Login_Model_.class);
        Call<LoginResponse> call = apiservices.post_Login(username, password, token);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getStatus()) {
                    Log.e("", "dhana: " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("", "onFailure: " + t);
            }
        });
    }



    @Override
    public void onBackPressed(){
        if(mywebview.canGoBack()) {
            mywebview.goBack();
        } else
        {
            super.onBackPressed();
        }
    }
}



