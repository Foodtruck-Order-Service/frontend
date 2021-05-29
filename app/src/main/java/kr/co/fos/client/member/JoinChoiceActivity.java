package kr.co.fos.client.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
            case R.id.businessBtn:    // 로그인 버튼
                intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("status", "business");
                startActivity(intent);
                break;
            case R.id.generalBtn:    // 한식 버튼
                intent = new Intent(getApplicationContext(), JoinChoiceActivity.class);
                intent.putExtra("status", "general");
                startActivity(intent);
                break;

        }
        //검색

    }
}
