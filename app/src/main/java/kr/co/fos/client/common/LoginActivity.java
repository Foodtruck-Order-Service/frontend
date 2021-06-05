package kr.co.fos.client.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.member.FindId1Activity;
import kr.co.fos.client.member.FindPw1Activity;
import kr.co.fos.client.member.JoinChoiceActivity;
import kr.co.fos.client.member.Member;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Retrofit client;
    HttpInterface service;

    EditText idText;
    EditText pwText;
    Button loginBtn;
    Button signUpBtn;
    Button findIdBtn;
    Button findPwBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_login);
        idText = findViewById(R.id.idText);
        pwText = findViewById(R.id.pwText);
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        findIdBtn = findViewById(R.id.findIdBtn);
        findPwBtn = findViewById(R.id.statusUpdateBtn);

        idText.setOnClickListener(this);
        pwText.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        findIdBtn.setOnClickListener(this);
        findPwBtn.setOnClickListener(this);

        setRetrofitInit();
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
            case R.id.loginBtn:    // 로그인 버튼
                login(idText.getText().toString(), pwText.getText().toString());

                break;
            case R.id.signUpBtn:    // 회원가입 버튼
                intent = new Intent(getApplicationContext(), JoinChoiceActivity.class);
                startActivity(intent);
                break;
            case R.id.findIdBtn:    // 아이디 찾기 버튼
                intent = new Intent(getApplicationContext(), FindId1Activity.class);
                startActivity(intent);
                break;
            case R.id.statusUpdateBtn:    // 비밀번호 찾기 버튼
                intent = new Intent(getApplicationContext(), FindPw1Activity.class);
                startActivity(intent);
                break;
        }
        //검색

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //로그인
    public void login(String id, String pw) {
        Call<List<Member>> call = service.memberLoginInquiry(id, pw);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                try {
                    Boolean check = false;
                    List<Member> memberList = response.body();
                    if (!memberList.toString().equals("[]")) {
                        check = true;
                    }
                    if (check == true) {
                        SharedPreference.setAttribute(getApplicationContext(), "no", String.valueOf(memberList.get(0).getNo()));
                        SharedPreference.setAttribute(getApplicationContext(), "type", String.valueOf(memberList.get(0).getType()));
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);


                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        idText.setText(null);
                        pwText.setText(null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("응답");
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
