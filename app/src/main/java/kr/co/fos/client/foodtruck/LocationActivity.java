package kr.co.fos.client.foodtruck;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationActivity  extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Intent intent;
    SearchView searchView;

    MapView mapView;
    ViewGroup mapViewContainer;
    MarkerEventListener markerListener;

    Button loginBtn;

    List<Foodtruck> foodtrucks;
    Boolean loginCheck;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodtruck_location);

        if (checkLocationServicesStatus()) {
            checkRunTimePermission();
        } else {
            showDialogForLocationServiceSetting();
        }

        setRetrofitInit();

        intent = getIntent();

        searchView = findViewById(R.id.searchView);

        mapView = new MapView(this);

        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        gpsTracker = new GpsTracker(this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);

        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MARKER_POINT, 1, true);

        markerListener = new MarkerEventListener(this);

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

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("내 위치");
        marker.setMapPoint(MARKER_POINT);
        marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);

        foodtruckLocationSearch();

        mapView.setPOIItemEventListener(markerListener);

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

    // 내주변 푸드트럭 찾기
    public void foodtruckLocationSearch() {
        if (foodtrucks == null) {
            foodtrucks = new ArrayList<Foodtruck>();
        }

        Call<List<Foodtruck>> call = service.foodtruckLocationSearch(latitude, longitude);
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

    @Override
    protected void onResume() {
        super.onResume();
        mapViewContainer.addView(mapView);
    }

    @Nullable
    @Override
    public void onPause() {
        mapViewContainer.removeAllViews();
        super.onPause();
    }

    @Nullable
    @Override
    public void onStop() {
        mapViewContainer.removeAllViews();
        super.onStop();
    }

    //======GPS 관련 메소드=========
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                startActivity(this.getIntent());
                finish();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "퍼미션이 거부되었습니다. 설정에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    void checkRunTimePermission() {
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
