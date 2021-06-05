package kr.co.fos.client.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateFragment extends Fragment {
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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.menu_update_fragment, container, false);

        menu = new Menu();
        menu.setNo(getArguments().getInt("menuNo"));

        photoView = (ImageView) rootView.findViewById(R.id.photoView);
        nameView = (TextView) rootView.findViewById(R.id.nameView);

        // list
        listView = (ListView) rootView.findViewById(R.id.listView);

        addButton = (Button) rootView.findViewById(R.id.addButton);

        photoView.setImageResource(R.drawable.chicken);

        adapter = new OptionAdapter();
        listView.setAdapter(adapter);

        // menuList  item 클릭 시.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

            }
        }) ;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    // 메뉴 등록
    public void menuRegister() {
        Call<ResponseBody> call = service.menuDetailInquiry(menu.getNo());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    menu = gson.fromJson(response.body().string(), Menu.class);
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

