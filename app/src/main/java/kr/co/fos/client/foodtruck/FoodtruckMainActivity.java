package kr.co.fos.client.foodtruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import java.io.IOException;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.bookmark.Bookmark;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.review.InquiryFragment;
import kr.co.fos.client.review.RegisterFragment;
import kr.co.fos.client.review.UpdateFragment;
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
    InfoFragment foodtruckInfoFragment;
    InquiryFragment reviewInquiryFragment;
    RegisterFragment reviewRegisterFragment;
    kr.co.fos.client.review.UpdateFragment reviewUpdateFragment;

    //Business Fragment
    kr.co.fos.client.foodtruck.UpdateFragment foodtruckUpdateFragment;
    kr.co.fos.client.menu.InquiryFragment menuInquiryFragment;
    kr.co.fos.client.menu.RegisterFragment menuRegisterFragment;
    kr.co.fos.client.menu.UpdateFragment menuUpdateFragment;

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

        foodtruckNameTextView = (TextView)findViewById(R.id.foodtruckName);

        loginButton = (Button)findViewById(R.id.loginButton);
        menuButton = (Button)findViewById(R.id.menuButton);
        introduceButton = (Button)findViewById(R.id.introduceButton);
        reviewButton = (Button)findViewById(R.id.reviewButton);

        loginCheck = SharedPreference.getAttribute(getApplicationContext(), "no") != null;
        foodtruck = (Foodtruck) getIntent().getSerializableExtra("foodtruck");
        type = SharedPreference.getAttribute(getApplicationContext(), "type");

        if(loginCheck) {
            memberNo = Integer.parseInt(SharedPreference.getAttribute(getApplicationContext(), "no"));
            myFoodtruckCheck = (memberNo == foodtruck.getMemberNo());

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
            // 비교
            int clickCheck = getIntent().getIntExtra("manageClick", 0);
            if (clickCheck == 1) {
                foodtruckUpdateFragment = new kr.co.fos.client.foodtruck.UpdateFragment();
                transaction.replace(R.id.frameLayout, foodtruckUpdateFragment).commitAllowingStateLoss();
            } else {
                menuInquiryFragment = new kr.co.fos.client.menu.InquiryFragment();
                transaction.replace(R.id.frameLayout, menuInquiryFragment).commitAllowingStateLoss();
            }
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
                    menuInquiryFragment = new kr.co.fos.client.menu.InquiryFragment();
                    transaction.replace(R.id.frameLayout, menuInquiryFragment).commitAllowingStateLoss();
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
                    foodtruckUpdateFragment = new kr.co.fos.client.foodtruck.UpdateFragment();
                    transaction.replace(R.id.frameLayout, foodtruckUpdateFragment).commitAllowingStateLoss();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("foodtruck", foodtruck);
                    foodtruckUpdateFragment.setArguments(bundle);
                } else {
                    foodtruckInfoFragment = new InfoFragment();
                    transaction.replace(R.id.frameLayout, foodtruckInfoFragment).commitAllowingStateLoss();
                }
            }
        });

        // 리뷰 버튼 클릭 시.
        reviewButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                transaction = fragmentManager.beginTransaction();
                reviewInquiryFragment = new InquiryFragment();
                transaction.replace(R.id.frameLayout, reviewInquiryFragment).commitAllowingStateLoss();
            }
        });

        // 로그인 버튼 클릭 시.
        loginButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginCheck) {
                    SharedPreference.removeAllAttribute(getApplicationContext());
                    loginButton.setText("로그인");
                    Toast.makeText(FoodtruckMainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    public void onFragmentChange(int index){
        if(index == 0){
            reviewRegisterFragment = new RegisterFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, reviewRegisterFragment).commit();
        } else if(index == 1){
            reviewInquiryFragment = new InquiryFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, reviewInquiryFragment).commit();
        } else if (index == 2){
            reviewUpdateFragment = new UpdateFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, reviewUpdateFragment).commit();
        }
    }

    public void onMenuFragmentChange(int index){
        if(index == 0){
            menuRegisterFragment = new kr.co.fos.client.menu.RegisterFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, menuRegisterFragment).commit();
        } else if(index == 1){
            menuInquiryFragment = new kr.co.fos.client.menu.InquiryFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, menuInquiryFragment).commit();
        } else if (index == 2){
            menuUpdateFragment = new kr.co.fos.client.menu.UpdateFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, menuUpdateFragment).commit();
        }
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
