package kr.co.fos.client.review;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import org.json.JSONArray;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InquiryFragment  extends Fragment{
    FoodtruckMainActivity foodtruckMainActivity;

    Retrofit client;
    HttpInterface service;

    TextView foodtruckNameTextView;
    TextView idTextView;
    TextView gradeTextView;
    TextView contentTextView;
    TextView registDateTextView;

    Button reviweRegisterButton;
    Button reviewUpdateButton;

    ListView listView;
    ReviewAdapter adapter;

    Foodtruck foodtruck;
    // Fragment
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    RegisterFragment registerFragment;
    InquiryFragment inquiryFragment;

    Review reviewObj;

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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.review_inquiry_fragment, container, false);

        foodtruck = (Foodtruck) getActivity().getIntent().getSerializableExtra("foodtruck");
        reviewObj = (Review) getActivity().getIntent().getSerializableExtra("review");
        foodtruckNameTextView = (TextView) rootView.findViewById(R.id.foodtruckName);
        gradeTextView = (TextView) rootView.findViewById(R.id.grade);
        idTextView = (TextView) rootView.findViewById(R.id.id);
        contentTextView = (TextView) rootView.findViewById(R.id.content);
        registDateTextView = (TextView) rootView.findViewById(R.id.registDate);

        reviweRegisterButton = (Button) rootView.findViewById(R.id.reviewRegisterButton);
        reviewUpdateButton = (Button) rootView.findViewById(R.id.reviewUpdateBtn);

        listView = (ListView) rootView.findViewById(R.id.listView);

        adapter = new ReviewAdapter();
        listView.setAdapter(adapter);

        reviewInquiry();

        reviweRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                foodtruckMainActivity.onFragmentChange(0);
            }
        });
        reviewUpdateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                foodtruckMainActivity.onFragmentChange(2);
            }
        });

        return rootView;
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    //리뷰 조회
    public void reviewInquiry() {
        Call<ResponseBody> call = service.reviewInquiry(foodtruck.getNo());
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

                        adapter.addItem(review.getId(), grade, review.getContent(), review.getRegistDate());

                    }
                    adapter.notifyDataSetChanged();

                    System.out.println("리뷰 조회 성공");
                    System.out.println(jArray);
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
