package kr.co.fos.client.review;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.bookmark.Bookmark;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InquiryActivity extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;

    TextView foodtruckNameTextView;
    TextView idTextView;
    TextView gradeTextView;
    TextView contentTextView;
    TextView registDateTextView;
    ListView listView;
    ReviewAdapter adapter;


    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_inquiry);
        setRetrofitInit();
        final Intent getIntent = getIntent();
        final String foodtruckNo = getIntent.getStringExtra("foodtruckNo");

        foodtruckNameTextView = (TextView)findViewById(R.id.foodtruckName);
        gradeTextView = (TextView)findViewById(R.id.grade);
        idTextView = (TextView)findViewById(R.id.id);
        contentTextView = (TextView) findViewById(R.id.content);
        registDateTextView = (TextView)findViewById(R.id.registDate);
        listView = (ListView)findViewById(R.id.listView);

        Button logoutButton = (Button)findViewById(R.id.logoutButton);
        Button menuButton = (Button)findViewById(R.id.menuButton);
        Button introduceButton = (Button)findViewById(R.id.introduceButton);
        Button reviewButton = (Button)findViewById(R.id.reviewButton);
        Button reviweRegisterButton = (Button)findViewById(R.id.reviewRegisterButton);
        final Switch bookmarkSwitch = (Switch)findViewById(R.id.bookmarkSwitch);

        adapter = new ReviewAdapter();
        listView.setAdapter(adapter);

        bookmarkSwitch.setChecked(true);
        foodtruckNameTextView.setText("백종원의 골목식당");
        reviewInquiry(2);
        
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //로그아웃 기능
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.menu.InquiryActivity.class);
                startActivity(intent);
            }
        });

        introduceButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.foodtruck.DetailInquiryActivity.class);
                startActivity(intent);
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.review.InquiryActivity.class);
                startActivity(intent);
            }
        });

        reviweRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.review.registerActivity.class);
                startActivity(intent);
            }
        });

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
    }

    //즐겨찾기 등록
    public void bookmarkRegister(String foodtruckNo) {
        //sharedPreferences안의 로그인 정보속 회원 번호를 저장한다.
        SharedPreferences pref =getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("memberNo","2" );
        editor.commit();

        //sharedPreferences안의 로그인 정보속 회원 번호를 가져온다.
        SharedPreferences prefImport = getSharedPreferences("test", MODE_PRIVATE);
        String memberNo = prefImport.getString("memberNo","");

        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruckNo);

        Call<ResponseBody> call = service.bookmarkRegister(bookmark);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("즐겨찾기 등록 성공");
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

    //즐겨찾기 삭제
    public void bookmarkDelete(String foodtruckNo) {
        //sharedPreferences안의 로그인 정보속 회원 번호를 저장한다.
        SharedPreferences pref =getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("memberNo","2" );
        editor.commit();

        //sharedPreferences안의 로그인 정보속 회원 번호를 가져온다.
        SharedPreferences prefImport = getSharedPreferences("test", MODE_PRIVATE);
        String memberNo = prefImport.getString("memberNo","");

        //bookmark객체에 회원번호, 푸드트럭 번호 저장
        Bookmark bookmark = new Bookmark();
        bookmark.setMemberNo(memberNo);
        bookmark.setFoodtruckNo(foodtruckNo);

        Call<ResponseBody> call = service.bookmarkDelete(bookmark);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("즐겨찾기 삭제 성공");
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

    //로그아웃
    public void logout() {
        SharedPreferences pref = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("auth","false" );
        Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.common.MainActivity.class);
    }

    //리뷰 조회
    public void reviewInquiry(int foodtruckNo) {
        Call<ResponseBody> call = service.reviewInquiry(foodtruckNo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    JSONArray jArray = new JSONArray(response.body().string());

                    for(int i = 0; i < jArray.length(); i++){
                        Review review = gson.fromJson(jArray.get(i).toString(),Review.class);
                        System.out.println(review);

                        //평점 변환
                        String grade;
                        if(review.getGrade().equals("1")){
                            grade="★☆☆☆☆";
                        } else if (review.getGrade().equals("2")){
                            grade="★★☆☆☆";
                        } else if (review.getGrade().equals("3")){
                            grade="★★★☆☆";
                        } else if (review.getGrade().equals("4")){
                            grade="★★★★☆";
                        } else{
                            grade="★★★★★";
                        }

                        adapter.addItem(review.getMemberId(), grade, review.getContent(), review.getRegistDate());

                    }
                        /*adapter.notifyDataSetChanged();*/

                    System.out.println("리뷰 조회 성공");
                    System.out.println(jArray);
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
