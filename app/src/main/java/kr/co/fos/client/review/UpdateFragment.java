package kr.co.fos.client.review;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.bookmark.Bookmark;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateFragment extends Fragment {
    FoodtruckMainActivity foodtruckMainActivity;
    Retrofit client;
    HttpInterface service;

    RatingBar ratingBar;

    Button photoRegisterBtn;
    Button reviewUpdateBtn;
    Button reviewDeleteBtn;

    Switch bookmarkSwitch;

    EditText reviewContentEditText;
    EditText photoEditText;
    Foodtruck foodtruck;
    Review reviewObj;

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        foodtruckMainActivity = (FoodtruckMainActivity)getActivity();
    }

    public void onDetch(){
        super.onDetach();

        foodtruckMainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetrofitInit();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.review_update_fragment, container, false);

        foodtruck = (Foodtruck) getActivity().getIntent().getSerializableExtra("foodtruck");
        reviewObj = (Review) getActivity().getIntent().getSerializableExtra("review");
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
        System.out.println("ratingbar : " + ratingBar.getRating());

        reviewContentEditText = (EditText) rootView.findViewById(R.id.reviewContentEditText);
        photoEditText = (EditText) rootView.findViewById(R.id.photoEditText);

        reviewUpdateBtn = (Button) rootView.findViewById(R.id.reviewUpdateButton);
        reviewDeleteBtn = (Button) rootView.findViewById(R.id.reviewDeleteButton);
        photoRegisterBtn = (Button) rootView.findViewById(R.id.photoRegisterbtn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                System.out.println(rating);
            }
        });

        String memberNo = SharedPreference.getAttribute(foodtruckMainActivity.getApplicationContext(), "no");
        //intent에서 리뷰번호 가져오기

        //원래 리뷰 정보 조회
        reviewDetailIuquiry(Integer.parseInt(memberNo), foodtruck.getNo());

        //리뷰 수정 부분
        reviewUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Review review = new Review();

                review.setGrade(String.valueOf(ratingBar.getRating()));
                review.setContent(reviewContentEditText.getText().toString());
                //세션에서 멤버번호 가져올부분
                review.setMemberNo(Integer.parseInt(memberNo));
                //intent에서 푸드트럭 가져올부분
                review.setFoodtruckNo(foodtruck.getNo());
                //intent에서 리뷰 번호 가져올부분

                System.out.println(review);
                reviewUpdate(reviewObj.getNo(), review);

                foodtruckMainActivity.onFragmentChange(1);
            }
        });
        //리뷰 삭제 부분
        reviewDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDelete(reviewObj.getNo());

                foodtruckMainActivity.onFragmentChange(1);
            }
        });
        photoRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    return rootView;

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
                Toast.makeText(getActivity().getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //리뷰 조회
    public void reviewDetailIuquiry(int memberNo, int foodtruckNo){
        Call<ResponseBody> call = service.reviewDetailInquiry(memberNo, foodtruckNo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    Review review = gson.fromJson(response.body().string(), Review.class);
                    reviewObj = review;
                    ratingBar.setRating(Integer.parseInt(review.getGrade()));
                    reviewContentEditText.setText(review.getContent());

                    System.out.println("리뷰 조회 성공");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //리뷰 삭제
    public void reviewDelete(int no){
        Call<ResponseBody> call = service.reviewDelete(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("리뷰 삭제 성공");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
