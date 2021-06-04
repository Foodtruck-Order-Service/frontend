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

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.basket.ListViewAdapter;
import kr.co.fos.client.basket.ListViewItem;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessDetailListFragment  extends ListFragment {
    BusinessDetailListAdapter adapter;

    public BusinessDetailListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Adapter 생성 및 Adapter 지정.
        adapter = new BusinessDetailListAdapter();
        System.out.println("프레그먼트 입장");
        setListAdapter(adapter);


        return super.onCreateView(inflater, container, savedInstanceState);
    }


    //아이템 추가
    public void addItem(BusinessDetailListViewItem item) {
        {
            adapter.addItem(item);
        }
    }

    //아이템 전부 가져오기
    public ArrayList<BusinessDetailListViewItem> inquiryAllItem() {
        return adapter.inquiryAllItem();
    }

}
