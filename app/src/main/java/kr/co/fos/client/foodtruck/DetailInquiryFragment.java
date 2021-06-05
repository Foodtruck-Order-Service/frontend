package kr.co.fos.client.foodtruck;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

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

public class DetailInquiryFragment extends Fragment {
    Retrofit client;
    HttpInterface service;

    MapView mapView;
    ViewGroup mapViewContainer;

    ListView listView;
    MenuAdapter adapter;

    Foodtruck foodtruck;
    List<Menu> menus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetrofitInit();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.foodtruck_detail_inquiry_fragment, container, false);

        foodtruck = (Foodtruck) getActivity().getIntent().getSerializableExtra("foodtruck");

        // map
        mapView = new MapView(getActivity());

        mapViewContainer = (ViewGroup) rootView.findViewById(R.id.map_view);

        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(foodtruck.getLat(), foodtruck.getLng());

        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MARKER_POINT, -1, true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(foodtruck.getName());
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        marker.setCustomImageResourceId(R.drawable.foodtruck_l_icon); // 마커 이미지.
        marker.setCustomImageAutoscale(true); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        mapView.addPOIItem(marker);

        // list
        listView = (ListView) rootView.findViewById(R.id.listView);

        adapter = new MenuAdapter();
        listView.setAdapter(adapter);

        menuInquiry();

        // menuList  item 클릭 시.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                Menu item = (Menu) parent.getItemAtPosition(position) ;

                kr.co.fos.client.menu.DetailInquiryFragment menuDetailInquiryFragment = new kr.co.fos.client.menu.DetailInquiryFragment();

                Bundle bundle = new Bundle(1);
                bundle.putInt("menuNo", item.getNo());
                menuDetailInquiryFragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.frameLayout, menuDetailInquiryFragment);
                transaction.addToBackStack(null);

                transaction.commitAllowingStateLoss();
            }
        }) ;

        return rootView;
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    // 메뉴 조회
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

    @Override
    public void onResume() {
        super.onResume();
        mapViewContainer.addView(mapView);
    }

    @Nullable
    @Override
    public void onPause() {
        mapViewContainer.removeAllViews();
        super.onPause();
    }
}