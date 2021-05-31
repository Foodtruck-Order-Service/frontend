package kr.co.fos.client.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.common.LoginActivity;
import retrofit2.Retrofit;

public class FindPw2Activity extends AppCompatActivity implements View.OnClickListener{
    EditText emailEditText;
    Button emailCheckBtn;
    Intent intent;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_findpw2);

        emailEditText = findViewById(R.id.emailEditText);
        emailCheckBtn = findViewById(R.id.emailCheckBtn);
        emailCheckBtn.setOnClickListener(this);
        intent = getIntent();
        id = intent.getStringExtra("id");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.emailCheckBtn:    // 로그인 버튼
                emailCertification();
                intent = new Intent(getApplicationContext(), FindPw3Activity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                break;
        }
        //검색

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(getApplicationContext(), FindPw1Activity.class);
        startActivity(intent);
    }
    // 이메일 인증
    public void emailCertification() {

    }
}
