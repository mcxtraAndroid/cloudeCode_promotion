package com.cloudcode.PromotionUniquier.Networkin;

import com.cloudcode.PromotionUniquier.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Adam on 7/27/2017.
 */

public interface Login_Model_ {


    @FormUrlEncoded
    @POST("insertupdatefcm.php")
    Call<LoginResponse> post_Login(
            @Field("uid") String uid,
            @Field("Password") String Password,
            @Field("fcmId") String fcmId);

}
