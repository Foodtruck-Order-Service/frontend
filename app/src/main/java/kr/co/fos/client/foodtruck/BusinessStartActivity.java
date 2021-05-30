package kr.co.fos.client.foodtruck;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.member.Member;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessStartActivity  extends AppCompatActivity implements View.OnClickListener {
    Retrofit client;
    HttpInterface service;

    EditText businessEdit;
    EditText foodtruckNameEdit;
    Spinner categorySpinner;
    TimePicker startSpinner;
    TimePicker endSpinner;
    EditText contentEdit;
    TextView photoNameView;

    Button photo_btn;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodtruck_business_start);

        setRetrofitInit();

        // EditText
        businessEdit = (EditText)findViewById(R.id.businessEdit);
        foodtruckNameEdit = (EditText)findViewById(R.id.foodtruckNameEdit);
        contentEdit = (EditText)findViewById(R.id.contentEdit);

        startSpinner = (TimePicker)findViewById(R.id.startSpinner);
        endSpinner = (TimePicker)findViewById(R.id.endSpinner);

        // Button
        photo_btn = (Button)findViewById(R.id.photoButton);
        submit_btn = (Button)findViewById(R.id.submitButton);

        photo_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);

        // Spinner
        categorySpinner = (Spinner)findViewById(R.id.spinner_category);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categorySpinner.setAdapter(categoryAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoButton:     // 사진 등록 버튼
                Toast.makeText(this.getApplicationContext(),"사진 등록", Toast.LENGTH_SHORT).show();
                break;
            case R.id.submitButton:    // 회원가입 버튼
                submitFoodtruck();

//                Intent intent = new Intent(BusinessStartActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                break;
        /*
            Intent intent2 = new Intent(main.this, login.class);
            startActivity(intent2);
            finish();
            break;
            */
        }
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    private void submitFoodtruck() {
        Member member = new Member();
        member.setId("test");
        Foodtruck foodtruck = new Foodtruck();

        foodtruck.setBrn(businessEdit.getText().toString());
        foodtruck.setName(foodtruckNameEdit.getText().toString());
        foodtruck.setCategory(categorySpinner.getSelectedItem().toString());
        String startTime;
        String endTime;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startTime = startSpinner.getHour() + ":" + startSpinner.getMinute();
            endTime = endSpinner.getHour() + ":" + endSpinner.getMinute();
        } else {
            startTime = startSpinner.getCurrentHour() + ":" + startSpinner.getCurrentMinute();
            endTime = endSpinner.getCurrentHour() + ":" + endSpinner.getCurrentMinute();
        }
        foodtruck.setStartTime(startTime);
        foodtruck.setEndTime(endTime);
        foodtruck.setContent(contentEdit.getText().toString());

        member.setFoodtruck(foodtruck);
        System.out.println("brn:" + foodtruck.getBrn() + ", start:" + foodtruck.getStartTime() + ", end:" + foodtruck.getEndTime());

//        foodtruckDAO.setId("foodid");
//        foodtruckDAO.setName("foodname");
//        loginDAO.setFoodtruckDAO(foodtruckDAO);
        Call<ResponseBody> call = service.memberBusinessRegister(member);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    Member member = gson.fromJson(response.body().string(), Member.class);
                    System.out.println(member.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 로그아웃
    public void logout() {

    }

    // 지도 조회
    public void mapInquiry() {

    }

    // 지도 검색
    public void mapSearch() {

    }

    // 푸드트럭 위치 등록
    public void foodtruckLocationRegister() {

    }

    // 영업 시작
    public void startBusiness() {

    }
}
