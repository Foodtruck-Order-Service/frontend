package kr.co.fos.client.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindPw3Activity  extends AppCompatActivity implements View.OnClickListener{
    Retrofit client;
    HttpInterface service;

    Intent intent;
    TextView findPwText;
    Button loginMoveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_findpw3);
        setRetrofitInit();
        findPwText = findViewById(R.id.findPwText);
        loginMoveBtn = findViewById(R.id.loginMoveBtn);
        loginMoveBtn.setOnClickListener(this);

        intent = getIntent();
        String id = intent.getStringExtra("id");
        memberDetailInquiry(id);
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
            case R.id.loginMoveBtn:    // 로그인 버튼
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
        }
        //검색

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(getApplicationContext(), FindPw2Activity.class);
        startActivity(intent);
    }

    //회원 정보 찾기
    public void memberDetailInquiry(String id) {
        Call<List<Member>> call = service.memberInquiry(id);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                try {
                    List<Member> memberList = response.body();
                    findPwText.setText(memberList.get(0).getPassword());

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
