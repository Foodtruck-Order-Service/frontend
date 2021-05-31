package kr.co.fos.client.member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;


import java.io.IOException;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.PopupActivity;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.common.BusinessActivity;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.foodtruck.SearchResultActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.sql.DriverManager.println;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    Retrofit client;
    HttpInterface service;

    EditText idEditText;
    EditText nameEditText;
    EditText typeEditText;
    EditText emailEditText;
    EditText phoneEditText;
    Button memberUpdateBtn;
    Button memberDeleteBtn;

    Intent intent;
    Member member;
    int no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_my_info);
        idEditText = findViewById(R.id.idEditText);
        nameEditText = findViewById(R.id.nameEditText);
        typeEditText = findViewById(R.id.typeEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        memberUpdateBtn = findViewById(R.id.memberUpdateBtn);
        memberDeleteBtn = findViewById(R.id.memberDeleteBtn);
        memberUpdateBtn.setOnClickListener(this);
        memberDeleteBtn.setOnClickListener(this);
        setRetrofitInit();
        //intent = getIntent();
        no = Integer.valueOf(SharedPreference.getAttribute(getApplicationContext(), "no"));
        memberDetailInquiry(no);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.memberUpdateBtn:    // 회원 수정 버튼
                intent = new Intent(getApplicationContext(), UpdateActivity.class);
                startActivity(intent);
                break;
            case R.id.memberDeleteBtn:    // 회원 삭제
                intent = new Intent(this, PopupActivity.class);
                intent.putExtra("data", "정말로 탈퇴하겠습니까?");
                startActivityForResult(intent, 2);

                break;
        }
    }


    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 2) {
                String result = data.getStringExtra("result");
                if (result.equals("close")) {
                    //취소 시 코드

                } else if (result.equals("ok")) {
                    // 작성 코드
                    memberDelete(no);
                    Toast.makeText(this, "RIGHT", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    // 회원 상세 조회
    public void memberDetailInquiry(int no) {
        Call<ResponseBody> call = service.memberDetailInquiry(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    Member memberData = gson.fromJson(response.body().string(), Member.class);

                    idEditText.setText(memberData.getId());
                    nameEditText.setText(memberData.getName());
                    typeEditText.setText(memberData.getType());
                    emailEditText.setText(memberData.getEmail());
                    phoneEditText.setText(memberData.getPhone());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }




    // 회원 탈퇴
    public void memberDelete(int no) {
        Call<ResponseBody> call = service.memberDelete(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                SharedPreference.removeAttribute(getApplicationContext(), "no");
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}