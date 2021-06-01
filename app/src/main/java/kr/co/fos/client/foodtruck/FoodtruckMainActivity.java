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

    // Member Fragment
    DetailInquiryFragment detailInquiryFragment;
    InfoFragment infoFragment;
    InquiryFragment reviewFragment;

    //Business Fragment
    UpdateFragment updateFragment;

    // data
    Foodtruck foodtruck;
    boolean loginCheck;
    boolean myFoodtruckCheck;
    int memberNo;
    String type;

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

        loginCheck = SharedPreference.getAttribute(getApplicationContext(), "no") != null;

        if(loginCheck) {
            memberNo = Integer.parseInt(SharedPreference.getAttribute(getApplicationContext(), "no"));
            type = SharedPreference.getAttribute(getApplicationContext(), "type");

            myFoodtruckCheck = memberNo == foodtruck.getMemberNo();

            loginButton.setText("로그아웃");

            if (myFoodtruckCheck) {
                introduceButton.setText("정보");
            } else if (type.equals("M")){
                bookmarkSwitch = (Switch)findViewById(R.id.bookmarkSwitch);
                bookmarkSwitch.setVisibility(View.VISIBLE);

                bookmarkInquiry();

                // 즐겨찾기
                bookmarkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked){
                            bookmarkRegister();
                        }else if (!isChecked){
                            bookmarkDelete();
                        }
                    }
                });
            }
        }

        foodtruckNameTextView.setText(foodtruck.getName());

        fragmentManager = getSupportFragmentManager();

        transaction = fragmentManager.beginTransaction();

        if (myFoodtruckCheck) {
            detailInquiryFragment = new DetailInquiryFragment();
            transaction.replace(R.id.frameLayout, detailInquiryFragment).commitAllowingStateLoss();
        } else {
            detailInquiryFragment = new DetailInquiryFragment();
            transaction.replace(R.id.frameLayout, detailInquiryFragment).commitAllowingStateLoss();
        }

        // 메뉴 버튼 클릭 시.
        menuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();

                if (myFoodtruckCheck) {
                    detailInquiryFragment = new DetailInquiryFragment();
                    transaction.replace(R.id.frameLayout, detailInquiryFragment).commitAllowingStateLoss();
                } else {
                    detailInquiryFragment = new DetailInquiryFragment();
                    transaction.replace(R.id.frameLayout, detailInquiryFragment).commitAllowingStateLoss();
                }
            }
        });

        // 소개 버튼 클릭 시.
        introduceButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();

                if (myFoodtruckCheck) {
                    updateFragment = new UpdateFragment();
                    transaction.replace(R.id.frameLayout, updateFragment).commitAllowingStateLoss();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("foodtruck", foodtruck);
                    updateFragment.setArguments(bundle);
                } else {
                    infoFragment = new InfoFragment();
                    transaction.replace(R.id.frameLayout, infoFragment).commitAllowingStateLoss();
                }
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

        // 로그인 버튼 클릭 시.
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginCheck) {
                    SharedPreference.removeAttribute(getApplicationContext(),"no");
                    SharedPreference.removeAttribute(getApplicationContext(),"type");
                    loginButton.setText("로그인");
                    Toast.makeText(FoodtruckMainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FoodtruckMainActivity.class);
                    intent.putExtra("foodtruck", foodtruck);
                    startActivity(intent);
                    finish();
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
    public void bookmarkInquiry() {
        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruck.getNo());

        Call<ResponseBody> call = service.bookmarkInquiry(memberNo, foodtruck.getNo());
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
    public void bookmarkRegister() {
        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruck.getNo());

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
    public void bookmarkDelete() {
        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruck.getNo());

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
