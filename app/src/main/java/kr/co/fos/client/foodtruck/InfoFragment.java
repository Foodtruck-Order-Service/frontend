package kr.co.fos.client.foodtruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.menu.MenuAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoFragment  extends Fragment {
    Retrofit client;
    HttpInterface service;

    // Image
    ImageView imageView;

    // Text
    TextView foodtruckNameTextView;
    TextView timeView;
    TextView introduceView;

    Foodtruck foodtruck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetrofitInit();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.foodtruck_info_fragment, container, false);

        foodtruck = (Foodtruck) getActivity().getIntent().getSerializableExtra("foodtruck");

        // Image
        imageView = (ImageView) rootView.findViewById(R.id.photoView);

        // Text
        foodtruckNameTextView = (TextView) rootView.findViewById(R.id.foodtruckName);
        timeView = (TextView) rootView.findViewById(R.id.timeView);
        introduceView = (TextView) rootView.findViewById(R.id.introduceView);

        imageView.setImageResource(R.drawable.chicken);
        timeView.setText("영업 시간 : " + foodtruck.getStartTime() + " ~ " + foodtruck.getEndTime());
        introduceView.setText(foodtruck.getContent());

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
}