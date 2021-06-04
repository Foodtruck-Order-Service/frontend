package kr.co.fos.client.order;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomListFragment extends ListFragment {
    Retrofit client;
    HttpInterface service;
    ListViewAdapter adapter;


    public CustomListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Adapter 생성 및 Adapter 지정.
        adapter = new ListViewAdapter();
        setListAdapter(adapter);
        setRetrofitInit();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }


    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        ListViewItem item = (ListViewItem) this.getListAdapter().getItem(position);
        detailInquiry(item.getFoodtruckNo());
    }

    //푸드트럭 상세 조회
    public void detailInquiry(int no){
        Call<ResponseBody> call = service.foodtruckDetailInquiry(no);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    Foodtruck foodtruck = gson.fromJson(response.body().string(), Foodtruck.class);
                    Intent intent = new Intent(getContext(), FoodtruckMainActivity.class);
                    intent.putExtra("foodtruck", foodtruck);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "시스템에 문제가 있습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    //아이템 추가
    public void addItem(ListViewItem item) {
        {
            adapter.addItem(item);
        }
    }



}
