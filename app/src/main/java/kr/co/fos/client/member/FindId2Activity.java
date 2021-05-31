package kr.co.fos.client.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.fos.client.R;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;

public class FindId2Activity  extends AppCompatActivity implements View.OnClickListener{
    Intent intent;
    TextView findIdText;
    Button loginMoveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_findid2);

        findIdText = findViewById(R.id.findIdText);
        loginMoveBtn = findViewById(R.id.loginMoveBtn);
        loginMoveBtn.setOnClickListener(this);

        intent = getIntent();
        String id = intent.getStringExtra("id");
        findIdText.setText(id);
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
        intent = new Intent(getApplicationContext(), FindId1Activity.class);
        startActivity(intent);
    }
}
