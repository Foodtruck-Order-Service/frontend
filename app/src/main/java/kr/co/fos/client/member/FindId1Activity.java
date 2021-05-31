package kr.co.fos.client.member;

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
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindId1Activity extends AppCompatActivity implements View.OnClickListener{
    Retrofit client;
    HttpInterface service;

    EditText nameEditText;
    EditText rrnEditText;
    Button findIdBtn;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_findid1);
        nameEditText = findViewById(R.id.nameEditText);
        rrnEditText = findViewById(R.id.rrnEditText);
        findIdBtn = findViewById(R.id.findIdBtn);
        findIdBtn.setOnClickListener(this);
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
            case R.id.findIdBtn:    // 로그인 버튼
                memberDetailInquiry(nameEditText.getText().toString(), rrnEditText.getText().toString());

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

    //회원 정보 찾기
    public void memberDetailInquiry(String name, String rrn) {
        Call<List<Member>> call = service.memberInquiry(name, rrn);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                try {
                    Boolean check = false;

                    List<Member> memberList = response.body();
                    if(!memberList.toString().equals("[]")){
                        check = true;
                    }
                    if(check == true) { ;
                        intent = new Intent(getApplicationContext(), FindId2Activity.class);
                        intent.putExtra("id", memberList.get(0).getId());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "아이디 찾기 실패", Toast.LENGTH_SHORT).show();
                        nameEditText.setText(null);
                        rrnEditText.setText(null);
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
