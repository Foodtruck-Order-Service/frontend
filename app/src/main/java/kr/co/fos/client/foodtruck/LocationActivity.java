package kr.co.fos.client.foodtruck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapCircle;
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
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.menu.Menu;
import kr.co.fos.client.menu.MenuAdapter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationActivity  extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;

    Intent intent;
    SearchView searchView;

    MapView mapView;
    ViewGroup mapViewContainer;

    Button loginBtn;

    List<Foodtruck> foodtrucks;
    Boolean loginCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodtruck_location);

        setRetrofitInit();

        intent = getIntent();

        searchView = findViewById(R.id.searchView);

        mapView = new MapView(this);

        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.5373752800, 127.0055763300);
        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MARKER_POINT, 3, true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("내 위치");
        marker.setMapPoint(MARKER_POINT);
        marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

        MapCircle circle1 = new MapCircle(
                MARKER_POINT, // center
                300, // radius
                Color.argb(128, 255, 0, 0), // strokeColor
                Color.argb(0, 0, 0, 0) // fillColor
        );

        mapView.addCircle(circle1);

        if (intent != null) {
            String name = intent.getStringExtra("name");
            searchView.setQuery(name, false);
        }

        loginBtn = findViewById(R.id.loginBtn);
        loginCheck = SharedPreference.getAttribute(getApplicationContext(), "no") != null;

        if(loginCheck) {
            loginBtn.setText("로그아웃");
        }

        foodtruckLocationSearch();

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
                Toast.makeText(getBaseContext(),"마커 클릭",Toast.LENGTH_SHORT).show();
                Foodtruck foodtruck = (Foodtruck) mapPOIItem.getUserObject();

                Intent intent = new Intent(getBaseContext(), FoodtruckMainActivity.class);
                intent.putExtra("foodtruck", foodtruck);
                startActivity(intent);
                finish();
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", query);
                startActivity(intent);
                Toast.makeText(LocationActivity.this, "검색 처리됨 : " + query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        loginBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginCheck) {
                    SharedPreference.removeAttribute(getApplicationContext(),"no");
                    loginBtn.setText("로그인");
                    Toast.makeText(LocationActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    // 검색
    public void search() {

    }

    // 로그아웃
    public void logout() {

    }

    // 내주변 푸드트럭 찾기
    public void foodtruckLocationSearch() {
        if (foodtrucks == null) {
            foodtrucks = new ArrayList<Foodtruck>();
        }

        double lat = 37.5373752800;
        double lng = 127.0055763300;

        Call<List<Foodtruck>> call = service.foodtruckLocationSearch(lat, lng);
        call.enqueue(new Callback<List<Foodtruck>>() {
            @Override
            public void onResponse(Call<List<Foodtruck>> call, Response<List<Foodtruck>> response) {
                try {
                    foodtrucks = response.body();

                    for (Foodtruck item : foodtrucks) {
                        MapPOIItem marker = new MapPOIItem();

                        marker.setItemName(item.getName());
                        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(item.getLat(), item.getLng()));
                        marker.setTag(item.getNo());
                        marker.setUserObject(item);
                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                        marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
                        marker.setCustomImageResourceId(R.drawable.foodtruck_icon); // 마커 이미지.
                        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                        marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

                        mapView.addPOIItem(marker);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<Foodtruck>> call, Throwable t) {
                Toast.makeText(getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
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
