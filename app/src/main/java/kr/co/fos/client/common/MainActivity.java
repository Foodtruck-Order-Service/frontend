package kr.co.fos.client.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.foodtruck.SearchResultActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SearchView searchView;
    ImageButton koreanFoodBtn;
    ImageButton japanFoodBtn;
    ImageButton chinaFoodBtn;
    ImageButton westernFoodBtn;
    ImageButton asianFoodBtn;
    ImageButton snackFoodBtn;
    ImageButton dessertFoodBtn;
    Intent intent;
    Boolean typeCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_main);
        searchView = findViewById(R.id.searchView);
        koreanFoodBtn = findViewById(R.id.koreanFoodBtn);
        japanFoodBtn = findViewById(R.id.japanFoodBtn);
        chinaFoodBtn = findViewById(R.id.chinaFoodBtn);
        westernFoodBtn = findViewById(R.id.westernFoodBtn);
        asianFoodBtn = findViewById(R.id.asianFoodBtn);
        snackFoodBtn = findViewById(R.id.snackFoodBtn);
        dessertFoodBtn = findViewById(R.id.dessertFoodBtn);

        koreanFoodBtn.setOnClickListener(this);
        japanFoodBtn.setOnClickListener(this);
        chinaFoodBtn.setOnClickListener(this);
        westernFoodBtn.setOnClickListener(this);
        asianFoodBtn.setOnClickListener(this);
        snackFoodBtn.setOnClickListener(this);
        dessertFoodBtn.setOnClickListener(this);

        typeCheck = SharedPreference.getAttribute(getApplicationContext(), "type") != null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (typeCheck){
            if(SharedPreference.getAttribute(getApplicationContext(), "type").equals("M")){
                MainViewActivity mainViewActivity = new MainViewActivity();
                transaction.replace(R.id.fragment_container, mainViewActivity);
                transaction.commit();
            } else if(SharedPreference.getAttribute(getApplicationContext(), "type").equals("B")){
                BusinessMainViewActivity businessMainViewActivity = new BusinessMainViewActivity();
                transaction.replace(R.id.fragment_container, businessMainViewActivity);
                transaction.commit();
            }
        } else {
            MainViewActivity mainViewActivity = new MainViewActivity();
            transaction.replace(R.id.fragment_container, mainViewActivity);
            transaction.commit();
        }




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                intent = new Intent(getApplicationContext(), SearchResultActivity.class);
                intent.putExtra("name", query);
                startActivity(intent);
                return true;

            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.koreanFoodBtn:    // 한식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("category", "한식");
                startActivity(intent);
                break;
            case R.id.japanFoodBtn:    // 일식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("category", "일식");
                startActivity(intent);
                break;
            case R.id.chinaFoodBtn:    // 중식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("category", "중식");
                startActivity(intent);
                break;
            case R.id.westernFoodBtn:    // 양식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("category", "양식");
                startActivity(intent);
                break;
            case R.id.asianFoodBtn:    // 아시안 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("category", "아시안");
                startActivity(intent);
                break;
            case R.id.snackFoodBtn:    // 분식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("category", "분식");
                startActivity(intent);
                break;
            case R.id.dessertFoodBtn:    // 디저트 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("category", "디저트");
                startActivity(intent);
                break;
        }
        //검색

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(this);
    }

    // 로그아웃
    public void logout() {

    }



}