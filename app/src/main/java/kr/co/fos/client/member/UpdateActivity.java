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

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    Retrofit client;
    HttpInterface service;

    EditText idEditText;
    EditText pwEditText;
    EditText pwCheckEditText;
    EditText nameEditText;
    EditText rrnEditText;
    EditText emailEditText;

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

        phoneEditText = findViewById(R.id.phoneEditText);
        updateBtn = findViewById(R.id.updateBtn);

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
            case R.id.updateBtn:    // ????????????
                intent = new Intent(this, PopupActivity.class);
                intent.putExtra("data", "????????? ??????????????????????");
                startActivityForResult(intent, 2);

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

            if (requestCode == 2) {
                String result = data.getStringExtra("result");
                if (result.equals("close")) {
                    //?????? ??? ??????

                } else if (result.equals("ok")) {
                    // ?????? ??????
                    if (pwEditText.getText().toString().equals(pwCheckEditText.getText().toString()) == true && emailCheck == true) {
                        Member updateMember = new Member();
                        updateMember.setPassword(pwEditText.getText().toString());
                        updateMember.setName(nameEditText.getText().toString());
                        updateMember.setEmail(emailEditText.getText().toString());
                        updateMember.setPhone(phoneEditText.getText().toString());
                        memberUpdate(no, updateMember);
                    } else if (pwEditText.getText().toString().equals(pwCheckEditText.getText().toString()) == false) {
                        Toast.makeText(this, "??????????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    } else if (emailCheck == false) {
                        Toast.makeText(this, "????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

    //?????? ?????? ??????
    public String phone(String src) {
        if (src == null) {
            return "";
        }
        if (src.length() == 8) {
            return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
            return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

    //?????? ?????? ??????
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
                    phoneEditText.setText(phone(memberData.getPhone()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // ????????? ??????
    public void emailCertification() {
        emailCheck = true;
    }

    // ?????? ??????
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
                Toast.makeText(getApplicationContext(), "???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
