package kr.co.fos.client.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.basket.InquiryActivity;
import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import kr.co.fos.client.foodtruck.LocationActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailInquiryFragment  extends Fragment {
    Retrofit client;
    HttpInterface service;

    ImageView photoView;
    TextView nameView;

    ListView listView;
    OptionAdapter adapter;

    Button addButton;

    Menu menu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetrofitInit();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.menu_inquiry_fragment, container, false);

        menu = new Menu();
        menu.setNo(getArguments().getInt("menuNo"));

        photoView = (ImageView) rootView.findViewById(R.id.photoView);
        nameView = (TextView) rootView.findViewById(R.id.nameView);

        // list
        listView = (ListView) rootView.findViewById(R.id.listView);

        addButton = (Button) rootView.findViewById(R.id.addButton);

        Glide.with(getContext()).load("http://222.117.135.101:8090/menu/photo/" + menu.getNo()).into(photoView);

        adapter = new OptionAdapter();
        listView.setAdapter(adapter);

        menuDetailInquiry();

        // menuList  item 클릭 시.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

            }
        }) ;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<Menu> basketList = SharedPreference.getBasketList();
                if (!basketList.isEmpty()) {
                    Iterator<Menu> basketMenu = basketList.iterator();
                    if (basketMenu.next().getFoodtruckNo() != menu.getFoodtruckNo()) {
                        Toast.makeText(getActivity().getBaseContext(),"같은 푸드트럭 메뉴만 추가할 수 있습니다!!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Menu checkMenu = menu;

                List<Option> options = new ArrayList<Option>();
                for (Option option : menu.getOptions()) {
                    List<OptionValue> optionValues = new ArrayList<OptionValue>();
                    for (OptionValue optionValue : option.getOptionValues()) {
                        if (optionValue.isChecked()) {
                            optionValues.add(optionValue);
                        }
                    }
                    if (optionValues.size() > 0) {
                        Option temp = option;
                        temp.setOptionValues(optionValues);
                        options.add(temp);
                    }
                }
                checkMenu.setOptions(options);
                SharedPreference.addBasketList(checkMenu);

                Intent intent = new Intent(getContext(), FoodtruckMainActivity.class);
                intent.putExtra("foodtruck", getActivity().getIntent().getSerializableExtra("foodtruck"));
                startActivity(intent);
                getActivity().finish();
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

    // 메뉴 조회
    public void menuDetailInquiry() {
        Call<ResponseBody> call = service.menuDetailInquiry(menu.getNo());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    menu = gson.fromJson(response.body().string(), Menu.class);

                    System.out.println(menu.toString());

                    nameView.setText(menu.getName());

                    for (Option item : menu.getOptions()) {
                        adapter.addItem(item);
                    }

                    listView.setAdapter(adapter);
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
