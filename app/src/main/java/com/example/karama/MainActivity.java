package com.example.karama;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karama.data.SharedPrefManager;
import com.example.karama.helper.CallbackResponse;
import com.example.karama.helper.UrlConfig;
import com.example.karama.helper.UIHelper;
import com.example.karama.model.product.ResAllProducts;
import com.example.karama.model.auth.ResToken;
import com.example.karama.services.KaraServices;
import com.example.karama.views.DialogConfirmOtp;
import com.example.karama.views.MainMenu;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView txt_username, txt_pass;
    Button btn_login, btn_signin;
    public Context mContext =this;
    public static Dialog loadingDialog;
    private static MainActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPrefManager.init(mContext);
        checkConnect();
        checkFirstInstall();
        initView();
        initClick();
        checkTokenInvalid();
//        Intent intent = new Intent(MainActivity.this, MainMenu.class);
//        startActivity(intent);
    }

    public static MainActivity getManagerInstance(){
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    private void checkTokenInvalid() {
        if (!SharedPrefManager.getRefreshToken().equals("NON")) {
            Log.e("==pref_refreshToken:", SharedPrefManager.getRefreshToken());
            if (!SharedPrefManager.getAccessToken().equals("NON")) {
                Log.e("==pref_accessToken:", SharedPrefManager.getAccessToken());
                Log.e("==pref_user:", SharedPrefManager.getUsername());
                //check token valid
                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
//                loadingDialog.show();
//                KaraServices.checkToken(mContext, new CallbackResponse() {
//                    @Override
//                    public void Success(Response<?> response) {
//                        loadingDialog.cancel();
//                        ResAllProducts resAllProducts = (ResAllProducts) response.body();
//                        if (resAllProducts != null) {
//                            if (resAllProducts.getStatus().equals("200")) {
//                                Intent intent = new Intent(MainActivity.this, MainMenu.class);
//                                startActivity(intent);
//                            } else {
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void Error(String error) {
//                        loadingDialog.cancel();
//                        UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.amazing_64);
//
//                    }
//                },"0","30","ASC");
            }
        }
    }

    private void checkFirstInstall() {
        if (!SharedPrefManager.getFirstInstall()) {
            Toast.makeText(this, "First install App", Toast.LENGTH_SHORT).show();
            Log.e("==share:", "First install App");
            SharedPrefManager.setPrefFirstInstall(true);
        }
    }

    private void checkConnect() {
        if (isNetwork(getApplicationContext())){

            Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(), "Internet Is Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    private void initClick() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_username.getText().toString().equals("") || txt_pass.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "VUI LÒNG NHẬP USER-PASSWORD", Toast.LENGTH_SHORT).show();
                } else {
                    String username = txt_username.getText().toString();
                    String password = txt_pass.getText().toString();
                    getToken(username, password);


                }
            }
        });
    }

    public boolean isNetwork(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void getToken(String username, String password) {
        loadingDialog.show();
        KaraServices.getAccessRefreshToken(new CallbackResponse() {
            @Override
            public void Success(Response<?> response) {
                loadingDialog.cancel();
                ResToken resToken = (ResToken) response.body();
                if (resToken != null) {
                    if (resToken.getStatus().equals("200")) {
                        Log.e("==acToken:", resToken.getData().getAccessToken());
                        Log.e("==rfToken:", resToken.getData().getRefreshToken());
                        SharedPrefManager.setAccessToken(resToken.getData().getAccessToken());
                        SharedPrefManager.setRefreshToken(resToken.getData().getRefreshToken());
                        SharedPrefManager.setUsername(username);
                        Log.e("==share_username:", SharedPrefManager.getUsername());
                        Intent intent = new Intent(MainActivity.this, MainMenu.class);
                        startActivity(intent);
                    } else {
                        UIHelper.showAlertDialog(mContext, resToken.getStatus(), resToken.getMessage(),R.drawable.troll_64);
                    }
                }
            }

            @Override
            public void Error(String error) {
                loadingDialog.cancel();
                UIHelper.showAlertDialog(mContext,"ERROR",error,R.drawable.amazing_64);

            }
        },username,password);

    }

    private void initView() {
        instance = this;
        Log.e("==url", UrlConfig.URL);
        loadingDialog = UIHelper.setShowProgressBar(mContext);
        txt_username = findViewById(R.id.txt_username);
        txt_pass = findViewById(R.id.txt_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_signin = findViewById(R.id.btn_signin);
        Log.e("==cre_acTken:", SharedPrefManager.getAccessToken());
        Log.e("==cre_rfTken:", SharedPrefManager.getRefreshToken());
    }

    public void dialogOTP(String user) {
        DialogConfirmOtp dialogConfirmOtp = new DialogConfirmOtp(MainActivity.this,user);
        dialogConfirmOtp.setCanceledOnTouchOutside(false);
        dialogConfirmOtp.show();
    }
}