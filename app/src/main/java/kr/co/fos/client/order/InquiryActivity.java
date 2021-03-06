package kr.co.fos.client.order;

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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InquiryActivity extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;
    CustomListFragment customListFrgmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_inquiry);
        setRetrofitInit();
        customListFrgmt = (CustomListFragment) getSupportFragmentManager().findFragmentById(R.id.orderlistFragment);
        //회원 번호
        orderInquiry(Integer.valueOf(SharedPreference.getAttribute(getApplicationContext(),"no")));

    }


    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    //주문 목록 조회
    public void orderInquiry(int no){
        Call<List<ListViewItem>> call = service.orderInquiry(no);
        call.enqueue(new Callback<List<ListViewItem>>() {
            @Override
            public void onResponse(Call<List<ListViewItem>> call, Response<List<ListViewItem>> response) {

                List<ListViewItem> item = response.body();

                for(int i = 0; i< item.size(); i++) {

                    customListFrgmt.addItem(item.get(i));
                }
                customListFrgmt.adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ListViewItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    //주문 조회
    public void orderInquiry(){

    }

    //주문 취소 신청
    public void orderCancleAuth(){

    }

    //리뷰 삭제
    public void reviewDelete(){

    }
}
