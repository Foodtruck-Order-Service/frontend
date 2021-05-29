package kr.co.fos.client.common;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.bookmark.InquiryActivity;
import kr.co.fos.client.foodtruck.LocationActivity;
import kr.co.fos.client.foodtruck.SearchResultActivity;
import kr.co.fos.client.member.MyInfoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SearchView searchView;
    Button loginBtn;
    ImageButton koreanFoodBtn;
    ImageButton japanFoodBtn;
    ImageButton chinaFoodBtn;
    ImageButton westernFoodBtn;
    ImageButton asianFoodBtn;
    ImageButton snackFoodBtn;
    ImageButton dessertFoodBtn;
    Button foodtruckLocationBtn;
    Button bookmarkBtn;
    Button infoBtn;
    Button orderBtn;
    Button basketBtn;
    Intent intent;
    Boolean loginCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_main);
        searchView = findViewById(R.id.searchView);
        loginBtn = findViewById(R.id.loginBtn);
        koreanFoodBtn = findViewById(R.id.koreanFoodBtn);
        japanFoodBtn = findViewById(R.id.japanFoodBtn);
        chinaFoodBtn = findViewById(R.id.chinaFoodBtn);
        westernFoodBtn = findViewById(R.id.westernFoodBtn);
        asianFoodBtn = findViewById(R.id.asianFoodBtn);
        snackFoodBtn = findViewById(R.id.snackFoodBtn);
        dessertFoodBtn = findViewById(R.id.dessertFoodBtn);
        foodtruckLocationBtn = findViewById(R.id.foodtruckLocationBtn);
        bookmarkBtn = findViewById(R.id.bookmarkBtn);
        infoBtn = findViewById(R.id.infoBtn);
        orderBtn = findViewById(R.id.orderBtn);
        basketBtn = findViewById(R.id.basketBtn);

        loginBtn.setOnClickListener(this);
        koreanFoodBtn.setOnClickListener(this);
        japanFoodBtn.setOnClickListener(this);
        chinaFoodBtn.setOnClickListener(this);
        westernFoodBtn.setOnClickListener(this);
        asianFoodBtn.setOnClickListener(this);
        snackFoodBtn.setOnClickListener(this);
        dessertFoodBtn.setOnClickListener(this);
        foodtruckLocationBtn.setOnClickListener(this);
        bookmarkBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        orderBtn.setOnClickListener(this);
        basketBtn.setOnClickListener(this);
        loginCheck = SharedPreference.getAttribute(getApplicationContext(), "id") == null;

        if(!loginCheck) {
            loginBtn.setText("로그아웃");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", query);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "검색 처리됨 : " + query, Toast.LENGTH_SHORT).show();
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
            case R.id.loginBtn:    // 로그인 버튼
                if(!(SharedPreference.getAttribute(getApplicationContext(), "id") == null)) {
                    SharedPreference.removeAttribute(getApplicationContext(),"id");
                    loginBtn.setText("로그인");
                } else {
                    intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.koreanFoodBtn:    // 한식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", "koreanFood");
                startActivity(intent);
                break;
            case R.id.japanFoodBtn:    // 일식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", "japanFood");
                startActivity(intent);
                break;
            case R.id.chinaFoodBtn:    // 중식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", "chinaFood");
                startActivity(intent);
                break;
            case R.id.westernFoodBtn:    // 양식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", "westernFood");
                startActivity(intent);
                break;
            case R.id.asianFoodBtn:    // 아시안 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", "asianFood");
                startActivity(intent);
                break;
            case R.id.snackFoodBtn:    // 분식 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", "snackFood");
                startActivity(intent);
                break;
            case R.id.dessertFoodBtn:    // 디저트 버튼
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", "dessertFood");
                startActivity(intent);
                break;
            case R.id.foodtruckLocationBtn:    // 내 주변 푸드트럭 검색 버튼
                intent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.bookmarkBtn:    // 즐겨찾기 버튼
                intent = new Intent(getApplicationContext(),kr.co.fos.client.bookmark.InquiryActivity.class);
                startActivity(intent);
                break;
            case R.id.infoBtn:    // 내 정보 버튼
                intent = new Intent(getApplicationContext(), MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.orderBtn:    // 주문 버튼
                intent = new Intent(getApplicationContext(), kr.co.fos.client.order.InquiryActivity.class);
                startActivity(intent);
                break;
            case R.id.basketBtn:    // 장바구니 버튼
                intent = new Intent(getApplicationContext(), kr.co.fos.client.basket.InquiryActivity.class);
                startActivity(intent);
                break;

        }
        //검색

    }


    // 로그아웃
    public void logout() {

    }



}