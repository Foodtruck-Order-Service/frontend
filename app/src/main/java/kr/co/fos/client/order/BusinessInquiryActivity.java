package kr.co.fos.client.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessInquiryActivity extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;
    CustomBusinessListFragment customBusinessListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_business_inquiry);
        setRetrofitInit();
        customBusinessListFragment = (CustomBusinessListFragment) getSupportFragmentManager().findFragmentById(R.id.orderBusinesslistFragment);
        detailInquiry(Integer.valueOf(SharedPreference.getAttribute(getApplicationContext(),"no")));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    //주문 목록 조회
    public void orderBusinessInquiry(int no){
        Call<List<BusinessListViewItem>> call = service.orderBusinessInquiry(no);
        call.enqueue(new Callback<List<BusinessListViewItem>>() {
            @Override
            public void onResponse(Call<List<BusinessListViewItem>> call, Response<List<BusinessListViewItem>> response) {
                Gson gson = new Gson();

                List<BusinessListViewItem> item = response.body();

                for(int i = 0; i< item.size(); i++) {
                    System.out.println("aaaaaa" + item.get(i).toString());
                    Log.i("a","item.get(i).toString()");
                    customBusinessListFragment.addItem(item.get(i));
                }
                customBusinessListFragment.adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<BusinessListViewItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }

   //푸드트럭 상세조회
   public void detailInquiry(int no){
       Call<ResponseBody> call = service.foodtruckBusinessDetailInquiry(no);
       call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               Gson gson = new Gson();
               try {
                   Foodtruck foodtruck = gson.fromJson(response.body().string(), Foodtruck.class);
                   orderBusinessInquiry(foodtruck.getNo());
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
