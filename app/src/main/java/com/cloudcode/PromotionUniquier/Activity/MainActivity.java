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
    String val= "";
    AppController appController;
    private static final String PDF_EXTENSION = ".pdf";

    private boolean isLoadingPdfUrl;
    private static final String PDF_VIEWER_URL = "http://docs.google.com/gview?embedded=true&url=";


    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;

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
String pdf ="http://pu.mycit.co.in/server/pdffiles/1578035950PurchaseOrder.pdf";
     //   mywebview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);


        mywebview.setWebViewClient(new WebViewClient(){


          /*  @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);



            }*/

           @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if  (url.endsWith(".pdf")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    try{
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
                    return ;
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

    }


    private void postLogin (String username, String password ,String token)
    {
        Log.e("", "postLogin: "+token );
        AppController app = AppController.getInstance();

        Login_Model_ apiservices = app.getClient().create(Login_Model_.class);
        Call<LoginResponse> call = apiservices.post_Login( username, password,token);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getStatus()) {
                    Log.e("", "dhana: "+response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("", "onFailure: "+t);
            }
        });
    }



    public void loadPdfUrl(String url)
    {
        mywebview.stopLoading();

        if (!TextUtils.isEmpty(url))
        {
            isLoadingPdfUrl = isPdfUrl(url);
            if (isLoadingPdfUrl)
            {
                mywebview.clearHistory();
            }

          //  showProgressDialog();
        }

        mywebview.loadUrl(url);
    }


    private boolean isPdfUrl(String url)
    {
        if (!TextUtils.isEmpty(url))
        {
            url = url.trim();
            int lastIndex = url.toLowerCase().lastIndexOf(PDF_EXTENSION);
            if (lastIndex != -1)
            {
                return url.substring(lastIndex).equalsIgnoreCase(PDF_EXTENSION);
            }
        }
        return false;
    }
}
