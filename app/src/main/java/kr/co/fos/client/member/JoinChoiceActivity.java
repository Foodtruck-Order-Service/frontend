package kr.co.fos.client.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.foodtruck.LocationActivity;
import kr.co.fos.client.foodtruck.SearchResultActivity;

public class JoinChoiceActivity extends AppCompatActivity implements View.OnClickListener{
    Button businessBtn;
    Button generalBtn;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join_choice);
        businessBtn = findViewById(R.id.businessBtn);
        generalBtn = findViewById(R.id.generalBtn);

        businessBtn.setOnClickListener(this);
        generalBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.businessBtn:    // 사업자 회원 이동 버튼
                intent = new Intent(getApplicationContext(), JoinEmailActivity.class);
                intent.putExtra("status", "business");
                Toast.makeText(getApplicationContext(), "사업자 회원가입을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            case R.id.generalBtn:    // 일반 회원 이동 버튼
                intent = new Intent(getApplicationContext(), JoinEmailActivity.class);
                intent.putExtra("status", "general");
                Toast.makeText(getApplicationContext(), "일반회원 회원가입을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;

        }
        //검색

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}
