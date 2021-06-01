package kr.co.fos.client.foodtruck;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.bookmark.Bookmark;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.member.Member;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.menu.MenuAdapter;
import kr.co.fos.client.review.InquiryFragment;
import kr.co.fos.client.review.ReviewAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodtruckMainActivity extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;

    // Button
    Button loginButton;
    Button menuButton;
    Button introduceButton;
    Button reviewButton;

    // Switch
    Switch bookmarkSwitch;

    // Text
    TextView foodtruckNameTextView;

    // Fragment
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    DetailInquiryFragment detailInquiryFragment;
    InfoFragment infoFragment;
    InquiryFragment reviewFragment;

    // data
    Foodtruck foodtruck;
    Boolean loginCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodtruck_main);

        setRetrofitInit();

        foodtruck = (Foodtruck) getIntent().getSerializableExtra("foodtruck");

        foodtruckNameTextView = (TextView)findViewById(R.id.foodtruckName);

        loginButton = (Button)findViewById(R.id.loginButton);
        menuButton = (Button)findViewById(R.id.menuButton);
        introduceButton = (Button)findViewById(R.id.introduceButton);
        reviewButton = (Button)findViewById(R.id.reviewButton);

        bookmarkSwitch = (Switch)findViewById(R.id.bookmarkSwitch);

        loginCheck = SharedPreference.getAttribute(getApplicationContext(), "id") == null;

        if(!loginCheck) {
            loginButton.setText("로그아웃");
        }

        /*bookmarkSwitch.setChecked(true);*/
        foodtruckNameTextView.setText(foodtruck.getName());

        fragmentManager = getSupportFragmentManager();

        detailInquiryFragment = new DetailInquiryFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, detailInquiryFragment).commitAllowingStateLoss();
        bookmarkInquiry(String.valueOf(foodtruck.getNo()));
        // 메뉴 버튼 클릭 시.
        menuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                detailInquiryFragment = new DetailInquiryFragment();
                transaction.replace(R.id.frameLayout, detailInquiryFragment).commitAllowingStateLoss();
            }
        });

        // 소개 버튼 클릭 시.
        introduceButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                infoFragment = new InfoFragment();
                transaction.replace(R.id.frameLayout, infoFragment).commitAllowingStateLoss();
            }
        });

        // 리뷰 버튼 클릭 시.
        reviewButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                reviewFragment = new InquiryFragment();
                transaction.replace(R.id.frameLayout, reviewFragment).commitAllowingStateLoss();
            }
        });

        // 즐겨찾기
        bookmarkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    bookmarkRegister("2");
                }else if (!isChecked){
                    bookmarkDelete("2");
                }
            }
        });

        // 로그인 버튼 클릭 시.
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(SharedPreference.getAttribute(getApplicationContext(), "id") == null)) {
                    SharedPreference.removeAttribute(getApplicationContext(),"id");
                    loginButton.setText("로그인");
                    Toast.makeText(FoodtruckMainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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

    //즐겨찾기 조회(스위치 기본값 설정)
    public void bookmarkInquiry(String foodtruckNo) {
        //sharedPreferences안의 로그인 정보속 회원 번호를 가져온다.
        String memberNo = SharedPreference.getAttribute(getApplicationContext(),"no");

        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruckNo);

        Call<ResponseBody> call = service.bookmarkInquiry(Integer.parseInt(memberNo), Integer.parseInt(foodtruckNo));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.body().string() != "" ){
                        bookmarkSwitch.setChecked(true);
                    } else {
                        bookmarkSwitch.setChecked(false);
                    }

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

    //즐겨찾기 등록(스위치 ON)
    public void bookmarkRegister(String foodtruckNo) {
        //sharedPreferences안의 로그인 정보속 회원 번호를 가져온다.
        String memberNo = SharedPreference.getAttribute(getApplicationContext(),"no");

        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruckNo);

        Call<ResponseBody> call = service.bookmarkRegister(bookmark);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response.body().string());
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

    //즐겨찾기 삭제(스위치 OFF)
    public void bookmarkDelete(String foodtruckNo) {
        //sharedPreferences안의 로그인 정보속 회원 번호를 가져온다.
        String memberNo = SharedPreference.getAttribute(getApplicationContext(),"no");

        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruckNo);

        Call<ResponseBody> call = service.bookmarkDelete(bookmark);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
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
