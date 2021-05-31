package kr.co.fos.client.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;


import java.io.IOException;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.PopupActivity;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.common.MainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
                    
                    if (memberData.getType().equals("M")){
                        typeEditText.setText("일반 회원");
                    } else if (memberData.getType().equals("B")){
                        typeEditText.setText("사업자 회원");
                    }

                    emailEditText.setText(memberData.getEmail());
                    phoneEditText.setText(phone(memberData.getPhone()));
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
    //전화 번호 포맷
    public String phone(String src) {
        if (src == null) {
            return "";
        }
        if (src.length() == 8) {
            return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
            return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
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