package kr.co.fos.client.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.foodtruck.Foodtruck;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InquiryFragment extends Fragment {
    Retrofit client;
    HttpInterface service;

    ListView listView;
    BusinessMenuAdapter adapter;

    Button addButton;

    Foodtruck foodtruck;
    List<Menu> menus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetrofitInit();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.menu_business_inquiry_fragment, container, false);

        foodtruck = (Foodtruck) getActivity().getIntent().getSerializableExtra("foodtruck");

        addButton = (Button) rootView.findViewById(R.id.addButton);

        // list
        listView = (ListView) rootView.findViewById(R.id.listView);

        adapter = new BusinessMenuAdapter();
        listView.setAdapter(adapter);

        menuInquiry();

        // menuList  item 클릭 시.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                Menu item = (Menu) parent.getItemAtPosition(position) ;

                ImageButton button = (ImageButton) parent.findViewById(R.id.removeButton);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("click");
                        menuRemove(item);
                    }
                });

                // 메뉴 수정 프래그먼트 생성 후 이동
                // kr.co.fos.client.menu.DetailInquiryFragment menuDetailInquiryFragment = new kr.co.fos.client.menu.DetailInquiryFragment();

                Bundle bundle = new Bundle(1);
                bundle.putInt("menuNo", item.getNo());
                // menuDetailInquiryFragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // transaction.replace(R.id.frameLayout, menuDetailInquiryFragment);
                transaction.addToBackStack(null);

                transaction.commitAllowingStateLoss();
            }
        }) ;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getBaseContext(),"메뉴 등록",Toast.LENGTH_SHORT).show();
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

    // 메뉴 목록 조회
    public void menuInquiry() {
        if (menus == null) {
            menus = new ArrayList<Menu>();
        }

        Call<List<Menu>> call = service.menuInquiry(foodtruck.getNo());
        call.enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                try {
                    menus = response.body();
                    for (Menu item  : menus) {
                        System.out.println(item.getName());
                        adapter.addItem(item);
                    }

                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 메뉴 삭제
    public void menuRemove(Menu menu) {
        Call<ResponseBody> call = service.menuDelete(menu.getNo());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    boolean result = gson.fromJson(response.body().toString(), Boolean.class);
                    if (result) {
                        adapter.removeItem(menu);
                        adapter.notifyDataSetChanged();
                    }
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

