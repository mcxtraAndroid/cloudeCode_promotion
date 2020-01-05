package com.cloudcode.PromotionUniquier.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {


    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("PropreitorName")
    @Expose
    private String propreitorName;
    @SerializedName("PrimaryMobileNo")
    @Expose
    private String primaryMobileNo;
    @SerializedName("Usertype")
    @Expose
    private String usertype;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPropreitorName() {
        return propreitorName;
    }

    public void setPropreitorName(String propreitorName) {
        this.propreitorName = propreitorName;
    }

    public String getPrimaryMobileNo() {
        return primaryMobileNo;
    }

    public void setPrimaryMobileNo(String primaryMobileNo) {
        this.primaryMobileNo = primaryMobileNo;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }


}
