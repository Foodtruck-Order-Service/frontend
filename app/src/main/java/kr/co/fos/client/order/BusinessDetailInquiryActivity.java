package kr.co.fos.client.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.basket.CustomListFragment;
import kr.co.fos.client.basket.ListViewItem;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.menu.Option;
import kr.co.fos.client.menu.OptionValue;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessDetailInquiryActivity extends AppCompatActivity implements View.OnClickListener {
    Retrofit client;
    HttpInterface service;
    Button statusUpdateBtn;
    Intent intent;
    BusinessListViewItem businessListViewItem;
    BusinessDetailListFragment businessDetailListFrgmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_business_detail_inquiry);

        statusUpdateBtn = findViewById(R.id.statusUpdateBtn);

        statusUpdateBtn.setOnClickListener(this);
        setRetrofitInit();
        businessDetailListFrgmt = (BusinessDetailListFragment) getSupportFragmentManager().findFragmentById(R.id.businessDetailListFragment);
        intent = getIntent();
        businessListViewItem = (BusinessListViewItem) intent.getSerializableExtra("businessListViewItem");
        orderBusinessDetailInquiry(businessListViewItem.getNo());
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
        Order order = new Order();
        switch (v.getId()) {
            case R.id.statusUpdateBtn:
                orderDetailInquiry(businessListViewItem.getNo());
                break;
        }
    }

    //주문 목록 조회
    public void orderBusinessDetailInquiry(int no){
        Call<ResponseBody> call = service.orderBusinessDetailInquiry(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    Order order = gson.fromJson(response.body().string(),Order.class);
                    System.out.println(order);
                        for (int i = 0; i < order.getOrderInfos().size(); i++) {
                            OrderInfo orderInfo = order.getOrderInfos().get(i);
                            BusinessDetailListViewItem businessDetailListViewItem = new BusinessDetailListViewItem();
                            businessDetailListViewItem.setNo(orderInfo.getOrderNo());
                            businessDetailListViewItem.setAmount(orderInfo.getAmount());
                            businessDetailListViewItem.setCount(orderInfo.getCount());
                            businessDetailListViewItem.setName(orderInfo.getMenuName());
                            if(order.getOrderInfos() != null) {
                            businessDetailListViewItem.setMenuDetail(orderInfo.getMenuDetails());
                            }
                            System.out.println("db에서 가져옴 :        " + businessDetailListViewItem);
                            businessDetailListFrgmt.addItem(businessDetailListViewItem);

                        businessDetailListFrgmt.adapter.notifyDataSetChanged();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    //주문 상태 업데이트
    public void orderStatusUpdate(int no, Order order){
        Call<ResponseBody> call = service.orderStatusUpdate(no, order);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //주문 상세 조회
    //주문 상태 업데이트
    public void orderDetailInquiry(int no){
        Call<ResponseBody> call = service.orderDetailInquiry(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    Order order = gson.fromJson(response.body().string(), Order.class);
                    String status = order.getStatus();
                    switch (status) {
                        case "W":
                            order.setStatus("R");
                            orderStatusUpdate(businessListViewItem.getNo(),order);
                            break;
                        case "R":
                            order.setStatus("P");
                            orderStatusUpdate(businessListViewItem.getNo(),order);
                            break;
                        case "P":
                            order.setStatus("C");
                            orderStatusUpdate(businessListViewItem.getNo(),order);
                            break;
                        case "C":
                            businessDetailListFrgmt.adapter.notifyDataSetChanged();
                            break;
                    }
                    intent = new Intent(getApplicationContext(),BusinessInquiryActivity.class);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
