package com.example.setprojects;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.setprojects.interfaces.JsonApiHolder;
import com.example.setprojects.model.RestApi;
import com.example.setprojects.model.TestClas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
//    Snackbar snackbar;
    ProgressDialog dialog;


    public void showProgressDialogue(String title, String message){
        dialog = new ProgressDialog(BaseActivity.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }
    public void dissmissProgressDialogue(){
       dialog.dismiss();
    }
    /*public  void showSnackBar(String dialogue, String color){
        snackbar =Snackbar.make((findViewById(android.R.id.content)), dialogue, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        view.setBackgroundColor(Color.RED);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }*/
    public boolean checkAccountNumber(String account){
        String[] accountPortions = account.split("-");
        if(accountPortions.length != 3){
            return false;
        }else {
            return true;
        }

    }
    public boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }


    public boolean checkCNICFormat(String userName) {
        String[] cnicPortions = userName.split("-");
        if(cnicPortions.length != 3){
            return false;
        }
        if(cnicPortions[0].length() == 5 && cnicPortions[1].length() == 7 && cnicPortions[2].length() == 1){
            Log.d(TAG, "checkCNICFormat: " +  String.valueOf(cnicPortions[0] + cnicPortions[1] + cnicPortions[2]));
            return true;
        }
        return false;
    }
    public boolean checkPasswordType(String passwordS) {
        return passwordS.length() > 7;
    }


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    /*public void checkConnection() {
        JsonApiHolder service = RestApi.getApi();

        Call<TestClas> call = service.checkConnection();

        call.enqueue(new Callback<TestClas>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<TestClas> call, Response<TestClas> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse: Successfully create connection" + response.body().getSuccess());
                }else {
                    Log.d(TAG, "onResponse: Failed!");
                    showSnackBar("Please check connection please" , "");
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<TestClas> call, Throwable t) {
                Log.d(TAG, "onFailure: Failed with message"+ t.getMessage());
                showSnackBar("Please check the connection either wifi or data network." , "");
            }
        });
    }*/


}
