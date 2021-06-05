package kr.co.fos.client.foodtruck;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.member.Member;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResultActivity extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;

    Intent intent;
    SearchView searchView;
    ListView listView;
    FoodtruckAdapter adapter;
    Button loginBtn;

    List<Foodtruck> foodtrucks;
    Boolean loginCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodtruck_search_result);

        setRetrofitInit();

        intent = getIntent();

        searchView = findViewById(R.id.searchView);

        if (intent != null) {
            String name = intent.getStringExtra("name");
            searchView.setQuery(name, false);
        }

        loginBtn = findViewById(R.id.loginBtn);
        loginCheck = SharedPreference.getAttribute(getApplicationContext(), "id") == null;

        if(!loginCheck) {
            loginBtn.setText("로그아웃");
        }

        // Adapter 생성
        adapter = new FoodtruckAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listView = (ListView) findViewById(R.id.foodtruckListView);
        listView.setAdapter(adapter);

        foodtruckInquiry();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.removeItemAll();
                foodtrucks.clear();

                foodtruckInquiry();

                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

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

        loginBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(SharedPreference.getAttribute(getApplicationContext(), "no") == null)) {
                    SharedPreference.removeAllAttribute(getApplicationContext());
                    loginBtn.setText("로그인");
                    Toast.makeText(SearchResultActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    // 푸드트럭 조회
    public void foodtruckInquiry() {
        if (foodtrucks == null) {
            foodtrucks = new ArrayList<Foodtruck>();
        }

        String category = intent.getStringExtra("category");

        Call<List<Foodtruck>> call = service.foodtruckInquiry(searchView.getQuery().toString(), category);
        call.enqueue(new Callback<List<Foodtruck>>() {
            @Override
            public void onResponse(Call<List<Foodtruck>> call, Response<List<Foodtruck>> response) {
                try {
                    foodtrucks = response.body();
                    for (Foodtruck item  : foodtrucks) {
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
    }
}
