package kr.co.fos.client.review;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.bookmark.Bookmark;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateActivity extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;

    RatingBar ratingBar;

    TextView idTextView;
    TextView gradeTextView;
    TextView contentTextView;
    TextView registDateTextView;

    Button logoutBtn;
    Button menuBtn;
    Button introduceBtn;
    Button reviewBtn;
    Button photoRegisterBtn;
    Button reviewUpdateBtn;

    Switch bookmarkSwitch;

    EditText reviewContentEditText;
    EditText photoEditText;


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
        setContentView(R.layout.review_update);
        setRetrofitInit();

        final Intent getIntent = getIntent();
        final String foodtruckNo = getIntent.getStringExtra("foodtruckNo");

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);


        reviewContentEditText = (EditText)findViewById(R.id.reviewContentEditText);
        photoEditText = (EditText)findViewById(R.id.photoEditText);

        logoutBtn = (Button)findViewById(R.id.logoutButton);
        menuBtn = (Button)findViewById(R.id.menuButton);
        introduceBtn = (Button)findViewById(R.id.introduceButton);
        reviewBtn = (Button)findViewById(R.id.reviewButton);
        reviewUpdateBtn = (Button)findViewById(R.id.reviewUpdateButton);
        photoRegisterBtn = (Button)findViewById(R.id.photoRegisterbtn);
        bookmarkSwitch = (Switch)findViewById(R.id.bookmarkSwitch);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                System.out.println(rating);
            }
        });

        //intent에서 리뷰번호 가져오기
        int reviewNo = 15;
        //원래 리뷰 정보 조회
        reviewDetailIuquiry(reviewNo);

        bookmarkInquiry("2");

        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.common.MainActivity.class);
                startActivity(intent);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.menu.InquiryActivity.class);
                startActivity(intent);
            }
        });

        introduceBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.foodtruck.DetailInquiryActivity.class);
                startActivity(intent);
            }
        });

        reviewBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.review.InquiryActivity.class);
                startActivity(intent);
            }
        });

        //리뷰 수정 부분
        reviewUpdateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Review review = new Review();

                review.setGrade(String.valueOf(ratingBar.getRating()));
                review.setContent(reviewContentEditText.getText().toString());
                //세션에서 멤버번호 가져올부분
                review.setMemberNo(2);
                //intent에서 푸드트럭 가져올부분
                review.setFoodtruckNo(2);
                //intent에서 리뷰 번호 가져올부분
                review.setNo(15);


                System.out.println(review);
                reviewUpdate(15, review);

                Intent intent = new Intent(getApplicationContext(), kr.co.fos.client.review.InquiryActivity.class);
                startActivity(intent);
            }
        });

        photoRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

        bookmarkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    //intent에서 받은 푸드트럭 번호
                    bookmarkRegister("2");
                }else if (!isChecked){
                    bookmarkDelete("2");
                }
            }
        });
    }
    //즐겨찾기 조회
    public void bookmarkInquiry(String foodtruckNo) {
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
        System.out.println("즐겨찾기 조회 bookmarkINquiry");
        Call<ResponseBody> call = service.bookmarkInquiry(Integer.parseInt(memberNo), Integer.parseInt(foodtruckNo));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("즐겨찾기 등록 성공");

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



    //사진 등록
    public void photoRegister() {

    }

    //리뷰 수정
    public void reviewUpdate(int no, Review review) {
        Call<ResponseBody> call = service.reviewUpdate(no, review);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("등록 성공!");
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

    //리뷰 조회
    public void reviewDetailIuquiry(int reviewNo){
        Call<ResponseBody> call = service.reviewDetailInquiry(reviewNo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    Review review = gson.fromJson(response.body().string(), Review.class);

                    ratingBar.setRating(Integer.parseInt(review.getGrade()));
                    reviewContentEditText.setText(review.getContent());

                    System.out.println("리뷰 조회 성공");

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

