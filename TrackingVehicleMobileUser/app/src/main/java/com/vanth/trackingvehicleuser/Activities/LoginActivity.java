        package com.vanth.trackingvehicleuser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vanth.trackingvehicleuser.API.ApiService;
import com.vanth.trackingvehicleuser.API.ApiUtils;
import com.vanth.trackingvehicleuser.CustumDialog;
import com.vanth.trackingvehicleuser.Entity.Account;
import com.vanth.trackingvehicleuser.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class LoginActivity extends AppCompatActivity {

    static String userLogin;

    EditText username;
    EditText password;
    Button loginButton;
    Button signUpButton;
    Button forgetPassword;

    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mapInstance();
        setEvent();

    }

    void mapInstance()
    {
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_buttonLogin);
        signUpButton = findViewById(R.id.login_buttonSignUp);
        forgetPassword = findViewById(R.id.login_buttonForgetPassword);

    }

    void setEvent()
    {
        // event for sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        // event for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
    }

    Account getLogin()
    {
        Account account = new Account();
        account.setUsername(username.getText().toString().trim());
        account.setPassword(password.getText().toString().trim());

        return account;
    }


    void signin()
    {
        if (!validateData()) return;
        CustumDialog custumDialog = new CustumDialog(this);
        custumDialog.startLoadingdialog();
        apiService = ApiUtils.getApiService();
        apiService.sign_in(getLogin()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful())
                {
                    boolean result = response.body();
                    if (result)
                    {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        userLogin = username.getText().toString().trim();
                    }

                    else
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }

                custumDialog.dismissdialog();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                custumDialog.dismissdialog();
            }
        });
    }

    boolean validateData()
    {
        if (username.getText().toString().trim().length() == 0)
        {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.getText().toString().trim().length() == 0)
        {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}