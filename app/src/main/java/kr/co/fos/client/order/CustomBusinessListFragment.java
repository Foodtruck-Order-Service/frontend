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

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomBusinessListFragment extends ListFragment {
    Retrofit client;
    HttpInterface service;
    BusinessListViewAdapter adapter;


    public CustomBusinessListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Adapter 생성 및 Adapter 지정.
        adapter = new BusinessListViewAdapter();
        setListAdapter(adapter);
        setRetrofitInit();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        BusinessListViewItem item = (BusinessListViewItem) this.getListAdapter().getItem(position);
        Intent intent = new Intent(getContext(),BusinessDetailInquiryActivity.class);
        intent.putExtra("businessListViewItem", item);
        startActivity(intent);
    }
    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }


    //아이템 추가
    public void addItem(BusinessListViewItem item) {
        {
            adapter.addItem(item);
        }
    }



}
