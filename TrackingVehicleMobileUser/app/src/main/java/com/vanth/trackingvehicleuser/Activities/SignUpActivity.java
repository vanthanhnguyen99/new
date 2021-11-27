package com.vanth.trackingvehicleuser.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.vanth.trackingvehicleuser.API.ApiService;
import com.vanth.trackingvehicleuser.API.ApiUtils;
import com.vanth.trackingvehicleuser.CustumDialog;
import com.vanth.trackingvehicleuser.R;
import com.vanth.trackingvehicleuser.Request.RegistAccountRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText name,email,phone,username,password,confirmPassword;
    RadioButton gender_male;
    RadioButton gender_female;
    AppCompatButton sign_up;

    ApiService apiService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        mapInstance();
        setEvent();
    }

    void mapInstance()
    {
        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        phone = findViewById(R.id.signup_phoneNumber);
        username = findViewById(R.id.signup_username);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_repassword);
        gender_male = findViewById(R.id.signup_gender_male);
        gender_female = findViewById(R.id.signup_gender_female);
        sign_up = findViewById(R.id.signup_buttonSignUp);
    }

    void setEvent()
    {
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    boolean validateData()
    {
        if (name.getText().toString().trim().length() == 0 || name.getText().toString().trim().length() > 100)
        {
            Toast.makeText(SignUpActivity.this, "Họ tên trống hoặc quá dài", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.getText().toString().trim().length() == 0 || email.getText().toString().trim().length() > 100 || !email.getText().toString().trim().matches("^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}"))
        {
            Toast.makeText(SignUpActivity.this, "Email trống hoặc quá dài hoặc không đúng định dạng", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.getText().toString().trim().length() == 0 || !phone.getText().toString().trim().matches("^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}"))
        {
            Toast.makeText(SignUpActivity.this, "Số điện thoại trống hoặc không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.getText().toString().trim().length() == 0 || username.getText().toString().trim().length() > 100)
        {
            Toast.makeText(SignUpActivity.this, "Tên đăng nhập trống hoặc quá dài", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.getText().toString().length() == 0)
        {
            Toast.makeText(SignUpActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (confirmPassword.getText().toString(). length() == 0 || confirmPassword.getText().toString().compareToIgnoreCase(password.getText().toString()) != 0)
        {
            Toast.makeText(SignUpActivity.this, "Chưa xác nhận mật khẩu hoặc mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!gender_male.isChecked() && !gender_female.isChecked())
        {
            Toast.makeText(SignUpActivity.this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    RegistAccountRequest getntance()
    {
        RegistAccountRequest registAccountRequest = new RegistAccountRequest();

        registAccountRequest.setName(name.getText().toString().trim());
        registAccountRequest.setGender(gender_male.isChecked());
        registAccountRequest.setEmail(email.getText().toString().trim());
        registAccountRequest.setPhone(phone.getText().toString());
        registAccountRequest.setUsername(username.getText().toString().trim());
        registAccountRequest.setPassword(password.getText().toString());

        return  registAccountRequest;
    }

    void signup()
    {
        if (!validateData()) return;

        CustumDialog custumDialog = new CustumDialog(this);
        custumDialog.startLoadingdialog();
        apiService = ApiUtils.getApiService();
        apiService.registAccount(getntance()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                {
                    String result = response.body();
                    if (result.compareToIgnoreCase("200") == 0)
                        Toast.makeText(SignUpActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    try
                    {
                        String result = response.errorBody().string();
                        System.out.println(result);
                        if (result.compareToIgnoreCase("101") == 0)
                            Toast.makeText(SignUpActivity.this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                        else if (result.compareToIgnoreCase("101") == 0)
                            Toast.makeText(SignUpActivity.this, "Lỗi hệ thống, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){

                    }
                }

                custumDialog.dismissdialog();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                custumDialog.dismissdialog();
            }
        });
    }
}
