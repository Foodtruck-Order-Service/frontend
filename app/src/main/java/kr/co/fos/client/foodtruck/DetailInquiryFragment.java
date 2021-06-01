package kr.co.fos.client.foodtruck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.bookmark.Bookmark;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.menu.MenuAdapter;
import okhttp3.ResponseBody;
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

        mapViewContainer.addView(mapView);

        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(foodtruck.getLat(), foodtruck.getLng());

        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MARKER_POINT, 1, true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(foodtruck.getName());
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        marker.setCustomImageResourceId(R.drawable.foodtruck_icon); // 마커 이미지.
        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        mapView.addPOIItem(marker);

        mapView.setMapViewEventListener(new MapView.MapViewEventListener() {

            @Override
            public void onMapViewInitialized(MapView mapView) {

            }

            @Override
            public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewZoomLevelChanged(MapView mapView, int i) {

            }

            @Override
            public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

            }

            @Override
            public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

            }
        });

        mapView.setPOIItemEventListener(new MapView.POIItemEventListener() {
            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

            }

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

            }
        });

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
                Intent intent = new Intent(getActivity().getApplicationContext(), kr.co.fos.client.menu.DetailInquiryActivity.class);
                intent.putExtra("menu", item);
                startActivity(intent);
            }
        }) ;

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

    @Nullable
    @Override
    public void onPause() {
        mapViewContainer.removeAllViews();
        super.onPause();
    }
}