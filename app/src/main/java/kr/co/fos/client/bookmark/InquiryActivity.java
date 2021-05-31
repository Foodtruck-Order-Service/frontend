package kr.co.fos.client.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckAdapter;
import kr.co.fos.client.foodtruck.SearchResultActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InquiryActivity extends AppCompatActivity{
    Retrofit client;
    HttpInterface service;

    Intent intent;
    SearchView searchView;
    ListView listView;
    FoodtruckAdapter adapter;

    List<Foodtruck> foodtrucks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodtruck_search_result);

        setRetrofitInit();

        searchView = findViewById(R.id.searchView);

        // Adapter 생성
        adapter = new FoodtruckAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listView = (ListView) findViewById(R.id.foodtruckListView);
        listView.setAdapter(adapter);

        int memberNo = 2;
       /* foodtruckInquiry(memberNo);*/

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                Foodtruck item = (Foodtruck) parent.getItemAtPosition(position) ;
                Toast.makeText(InquiryActivity.this, "item click : " + item.getNo(), Toast.LENGTH_SHORT).show();
            }
        }) ;
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    // 검색
    public void login() {

    }

    // 로그아웃
    public void logout() {

    }

   /* // 푸드트럭 조회
    public void foodtruckInquiry(int memberNo) {
        if (foodtrucks == null) {
            foodtrucks = new ArrayList<Foodtruck>();
        }

        Call<List<Foodtruck>> call = service.bookmarkInquiry(memberNo,0);
        call.enqueue(new Callback<List<Foodtruck>>() {
            @Override
            public void onResponse(Call<List<Foodtruck>> call, Response<List<Foodtruck>> response) {
                try {
                    foodtrucks = response.body();

                    for (Foodtruck item  : foodtrucks) {
                        System.out.println(item.getName());
                        adapter.addItem(item);
                    }

                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Foodtruck>> call, Throwable t) {
                Toast.makeText(getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
