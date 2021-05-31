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
import kr.co.fos.client.foodtruck.RegisterActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener{
    Retrofit client;
    HttpInterface service;

    EditText idEditText;
    EditText pwEditText;
    EditText pwCheckEditText;
    EditText nameEditText;
    EditText rrnEditText;
    EditText emailEditText;
    Button emailCheckBtn;
    EditText phoneEditText;
    Button updateBtn;
    Boolean emailCheck = true;
    Intent intent;
    Member member;
    int no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        idEditText = findViewById(R.id.idEditText);
        pwEditText = findViewById(R.id.pwEditText);
        pwCheckEditText = findViewById(R.id.pwCheckEditText);
        nameEditText = findViewById(R.id.nameEditText);
        rrnEditText = findViewById(R.id.rrnEditText);
        emailEditText = findViewById(R.id.emailEditText);
        emailCheckBtn = findViewById(R.id.emailCheckBtn);
        phoneEditText = findViewById(R.id.phoneEditText);
        updateBtn = findViewById(R.id.updateBtn);

        emailCheckBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        setRetrofitInit();
        no = Integer.valueOf(SharedPreference.getAttribute(getApplicationContext(), "no"));
        memberDetailInquiry(no);
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.emailCheckBtn:    // 이메일 중복확인 버튼
                emailCertification();

                break;
            case R.id.updateBtn:    // 수정버튼
                if(emailCheck == true){
                    intent = new Intent(this, PopupActivity.class);
                    intent.putExtra("data","정말로 수정하겠습니까?");
                    startActivityForResult(intent, 2);
                } else {
                    Toast.makeText(this, "이메일 인증을 하십시오.", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(getApplicationContext(), MyInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if(requestCode == 2){
                String result = data.getStringExtra("result");
                if(result.equals("close") ){
                    //취소 시 코드

                } else if(result.equals("ok")){
                    // 작성 코드
                    Member updateMember = new Member();
                    updateMember.setPassword(pwEditText.getText().toString());
                    updateMember.setName(nameEditText.getText().toString());
                    updateMember.setEmail(emailEditText.getText().toString());
                    updateMember.setPhone(phoneEditText.getText().toString());
                    memberUpdate(no,updateMember);
                    Toast.makeText(this, "RIGHT", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
    public void memberDetailInquiry(int no) {
        Call<ResponseBody> call = service.memberDetailInquiry(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    Member memberData = gson.fromJson(response.body().string(), Member.class);

                    idEditText.setText(memberData.getId());
                    pwEditText.setText(memberData.getPassword());
                    nameEditText.setText(memberData.getName());
                    rrnEditText.setText(memberData.getRrn());
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

    // 이메일 인증
    public void emailCertification() {
        emailCheck = true;
    }

    // 회원 수정
    public void memberUpdate(int no, Member member) {
        Call<ResponseBody> call = service.memberUpdate(no, member);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                intent = new Intent(getApplicationContext(), MyInfoActivity.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
