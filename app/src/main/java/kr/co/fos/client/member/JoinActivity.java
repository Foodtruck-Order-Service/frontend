package kr.co.fos.client.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.foodtruck.RegisterActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {
    Retrofit client;
    HttpInterface service;

    EditText idEditText;
    Button idCheckBtn;
    EditText pwEditText;
    EditText pwCheckEditText;
    EditText nameEditText;
    EditText rrnEditText;
    EditText emailEditText;
    Button emailCheckBtn;
    EditText phoneEditText;
    Button signUpBtn;

    Intent intent;

    Boolean status = false;

    Boolean idCheck = false;
    String idCheckText;
   //이메일 구현할 때 나중에 false로 바꿔야함
    Boolean emailCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join);

        idEditText = findViewById(R.id.idEditText);
        idCheckBtn = findViewById(R.id.idCheckBtn);
        pwEditText = findViewById(R.id.pwEditText);
        pwCheckEditText = findViewById(R.id.pwCheckEditText);
        nameEditText = findViewById(R.id.nameEditText);
        rrnEditText = findViewById(R.id.rrnEditText);
        emailEditText = findViewById(R.id.emailEditText);
        emailCheckBtn = findViewById(R.id.emailCheckBtn);
        phoneEditText = findViewById(R.id.phoneEditText);
        signUpBtn = findViewById(R.id.signUpBtn);

        idCheckBtn.setOnClickListener(this);
        emailCheckBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);

        intent = getIntent();
        if (intent.getStringExtra("status").equals("business")) {
            status = true;
            signUpBtn.setText("다음");
        }

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
            case R.id.idCheckBtn:    // 아이디 중복확인 버튼
                idCheck(idEditText.getText().toString());

                break;
            case R.id.emailCheckBtn:    // 이메일 체크 버튼
                emailCertification();

                break;
            case R.id.signUpBtn:    // 회원가입, 다음 버튼
                if (idCheck && emailCheck) {
                    Member member = new Member();
                    member.setId(idEditText.getText().toString());
                    member.setPassword(pwEditText.getText().toString());
                    member.setName(nameEditText.getText().toString());
                    member.setRrn(rrnEditText.getText().toString());
                    member.setEmail(emailEditText.getText().toString());
                    member.setPhone(phoneEditText.getText().toString());

                    if (status == true) {
                        member.setType("B");
                        intent = new Intent(getApplicationContext(), RegisterActivity.class);
                        intent.putExtra("info", member);
                        startActivity(intent);
                    } else {
                        if(idCheckText.equals(idEditText.getText().toString())&& pwEditText.getText().toString().equals(pwCheckEditText.getText().toString())){
                            member.setType("M");
                            memberRegister(member);
                            SharedPreference.setAttribute(getApplicationContext(), "id", idEditText.getText().toString());
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            
                        } else if(idCheckText.equals(idEditText.getText().toString()) == false){
                            Toast.makeText(getApplicationContext(), "중복확인을 다시 해주십시오.", Toast.LENGTH_SHORT).show();
                        } else if(pwEditText.getText().toString().equals(pwCheckEditText.getText().toString()) == false){
                            Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "중복확인 및 이메일 체크를 해주십시오.", Toast.LENGTH_SHORT).show();
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

    //아이디 중복확인
    public void idCheck(String id) {
        Call<List<Member>> call = service.memberInquiry(id);
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
                        Toast.makeText(getApplicationContext(), "아이디가 중복입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        idCheck = true;
                        idCheckText = idEditText.getText().toString();
                        Toast.makeText(getApplicationContext(), "아이디 사용 가능", Toast.LENGTH_SHORT).show();

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

    // 이메일 인증
    public void emailCertification() {
        //나중 구현
    }

    public void memberRegister(Member member) {
        Call<ResponseBody> call = service.memberRegister(member);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.isSuccessful());
                Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
