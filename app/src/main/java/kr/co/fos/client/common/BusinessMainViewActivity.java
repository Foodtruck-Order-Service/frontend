package kr.co.fos.client.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import java.io.IOException;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.foodtruck.BusinessStartActivity;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import kr.co.fos.client.foodtruck.SearchResultActivity;
import kr.co.fos.client.member.Member;
import kr.co.fos.client.member.MyInfoActivity;
import kr.co.fos.client.menu.InfoActivity;
import kr.co.fos.client.order.BusinessInquiryActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessMainViewActivity extends Fragment implements View.OnClickListener{
    Retrofit client;
    HttpInterface service;
    Button startSalesBtn;
    Button foodtruckManageBtn;
    Button orderManageBtn;
    Button infoBtn;
    Intent intent;
    Boolean approval;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.business_main_fragment, container, false);
        startSalesBtn = rootView.findViewById(R.id.startSalesBtn);
        foodtruckManageBtn = rootView.findViewById(R.id.foodtruckManageBtn);
        orderManageBtn = rootView.findViewById(R.id.orderManageBtn);

        infoBtn = rootView.findViewById(R.id.infoBtn);

        startSalesBtn.setOnClickListener(this);
        foodtruckManageBtn.setOnClickListener(this);
        orderManageBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        setRetrofitInit();
        foodtruckStatusInquiry(Integer.valueOf(SharedPreference.getAttribute(getContext(),"no")));
        return rootView;
    }

    @Override
    public void onClick(View v) {
        foodtruckStatusInquiry(Integer.valueOf(SharedPreference.getAttribute(getContext(),"no")));
        switch (v.getId()) {
            case R.id.startSalesBtn: //????????????
                if(approval) {
                    Intent intent = new Intent(getContext(), BusinessStartActivity.class);
                    myFoodTruckInquiry(Integer.valueOf(SharedPreference.getAttribute(getContext(),"no")),intent);
                } else {
                    Toast.makeText(getContext(), "????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.foodtruckManageBtn:    // ???????????? ?????? ??????
                // ????????? ???????????????
                if(approval) {
                    Intent intent = new Intent(getContext(), FoodtruckMainActivity.class);
                    myFoodTruckInquiry(Integer.valueOf(SharedPreference.getAttribute(getContext(),"no")),intent);

                } else {
                    Toast.makeText(getContext(), "????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.orderManageBtn:    // ?????? ?????? ??????
                if(approval) {
                    intent = new Intent(getContext(), BusinessInquiryActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.infoBtn:    // ??? ?????? ??????
                if(approval) {
                    intent = new Intent(getContext(), MyInfoActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(), "????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        //??????

    }
    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }


    //???????????? ?????? ??????
    public void foodtruckStatusInquiry(int no){
        Call<ResponseBody> call = service.myFoodtruckInquiry(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    Foodtruck foodtruck = gson.fromJson(response.body().string(), Foodtruck.class);
                    if(foodtruck.getApproval().equals("Y")){
                        approval = true;
                    }else {
                        approval = false;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void myFoodTruckInquiry(int no, Intent intent){
        Call<ResponseBody> call = service.myFoodtruckInquiry(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    Foodtruck foodtruck = gson.fromJson(response.body().string(), Foodtruck.class);

                    intent.putExtra("foodtruck",foodtruck);
                    startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
