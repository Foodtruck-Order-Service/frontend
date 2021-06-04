package kr.co.fos.client.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.foodtruck.RegisterActivity;
import kr.co.fos.client.review.Review;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinEmailActivity extends AppCompatActivity implements View.OnClickListener {
    Retrofit client;
    HttpInterface service;

    TextView codeText;
    EditText codeEditText;
    EditText emailEditText;
    Button emailCheckBtn;
    Button signUpBtn;

    Intent intent;

    Boolean status = false;

    Boolean idCheck = false;
    String idCheckText;
    //이메일 구현할 때 나중에 false로 바꿔야함
    Boolean emailCheck = true;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join_email);
        setRetrofitInit();

        codeText = findViewById(R.id.codeText);
        codeEditText = findViewById(R.id.codeEditText);
        emailEditText = findViewById(R.id.emailEditText);
        emailCheckBtn = findViewById(R.id.emailCheckBtn);

        signUpBtn = findViewById(R.id.signUpBtn);

        emailCheckBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        intent = getIntent();
        if (intent.getStringExtra("status").equals("business")) {
            status = true;
        }
    }

    private void setRetrofitInit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = client.create(HttpInterface.class);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.emailCheckBtn:    // 이메일 체크 버튼
                emailCertification(emailEditText.getText().toString());
                codeText.setVisibility(View.VISIBLE);
                codeEditText.setVisibility(View.VISIBLE);

                break;
            case R.id.signUpBtn:    // 다음 버튼
                // if (idCheck && emailCheck && ssnCheck(rrnEditText.getText().toString())) {
                if ((codeEditText.getText().toString()).equals(code)) {
                    intent = new Intent(getApplicationContext(), JoinActivity.class);
                    intent.putExtra("email", emailEditText.getText().toString());
                    if(status){
                        intent.putExtra("status", "business");
                    } else {
                        intent.putExtra("status", "general");
                    }

                    Toast.makeText(getApplicationContext(), "이메일 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }

                else {
                    Toast.makeText(getApplicationContext(), "인증코드를 다시 확인해주십시오.", Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent(getApplicationContext(), JoinChoiceActivity.class);
        startActivity(intent);
    }

    // 이메일 인증
    public void emailCertification(String email) {
        Call<String> call = service.emailCertification(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    /*code = response.body().string();*/
                    code = response.body();
                }catch(Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "인증 코드가 발송되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

