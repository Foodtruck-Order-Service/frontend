package kr.co.fos.client.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.basket.ListViewItem;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import kr.co.fos.client.menu.Option;
import kr.co.fos.client.menu.OptionValue;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NumberActivity extends AppCompatActivity implements View.OnClickListener{
    Retrofit client;
    HttpInterface service;
    TextView receptionNoText;
    Button mainBtn;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_number);
        receptionNoText = findViewById(R.id.receptionNoText);
        mainBtn = findViewById((R.id.mainBtn));
        mainBtn.setOnClickListener(this);
        intent = getIntent();
        int orderNo = intent.getIntExtra("orderNo",1);
        setRetrofitInit();
        detailInquiry(orderNo);
        SharedPreference.deleteAllBasketList();

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
            case R.id.mainBtn:    // 로그인 버튼
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
        }
    }

    //주문 상세 조회
    public void detailInquiry(int no){
        Call<ResponseBody> call = service.orderDetailInquiry(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    Order order = gson.fromJson(response.body().string(),Order.class);
                    receptionNoText.setText(String.valueOf(order.getReceptionNo()));
                } catch (IOException e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
