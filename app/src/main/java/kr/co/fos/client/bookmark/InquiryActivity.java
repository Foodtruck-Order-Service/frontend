package kr.co.fos.client.bookmark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckAdapter;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import kr.co.fos.client.foodtruck.SearchResultActivity;
import kr.co.fos.client.review.Review;
import okhttp3.ResponseBody;
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
    BookmarkAdapter adapter;

    List<Foodtruck> foodtrucks;
    Foodtruck foodtruck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_inquiry);
        setRetrofitInit();

        // Adapter 생성
        adapter = new BookmarkAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listView = (ListView) findViewById(R.id.foodtruckListView);
        listView.setAdapter(adapter);
        foodtruck = (Foodtruck) getIntent().getSerializableExtra("foodtruck");
        int memberNo = 2;
       /* foodtruckInquiry(memberNo);*/
        bookmarkInquiry();
        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                Foodtruck item = (Foodtruck) parent.getItemAtPosition(position) ;
                intent = new Intent(getApplicationContext(), FoodtruckMainActivity.class);
                intent.putExtra("foodtruck", item);
                startActivity(intent);
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

    // 푸드트럭 조회
    public void bookmarkInquiry() {
        if (foodtrucks == null) {
            foodtrucks = new ArrayList<Foodtruck>();
        }

        String memberNo = SharedPreference.getAttribute(getApplicationContext(), "no");

        Call<ResponseBody> call = service.bookmarkInquiry(Integer.parseInt(memberNo),0);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    JSONArray jArray = new JSONArray(response.body().string());

                    for(int i = 0; i < jArray.length(); i++){
                        Foodtruck item = gson.fromJson(jArray.get(i).toString(),Foodtruck.class);

                        System.out.println("푸드트럭이름 : " + item.getName());
                        adapter.addItem(item);
                    }

                    adapter.notifyDataSetChanged();
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
}
