package kr.co.fos.client.foodtruck;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import kr.co.fos.client.HttpInterface;
import kr.co.fos.client.R;
import kr.co.fos.client.SharedPreference;
import kr.co.fos.client.common.LoginActivity;
import kr.co.fos.client.common.MainActivity;
import kr.co.fos.client.foodtruck.map.GpsTracker;
import kr.co.fos.client.foodtruck.map.MapEventListener;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessStartActivity  extends AppCompatActivity {
    Retrofit client;
    HttpInterface service;

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Intent intent;

    MapView mapView;
    ViewGroup mapViewContainer;
    MapPOIItem myMarker;
    MapCircle searchRadius;
    MapEventListener mapListener;

    Button loginBtn;
    Button startBtn;

    Foodtruck myFoodtruck;
    List<Foodtruck> foodtrucks;
    Boolean loginCheck;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodtruck_business_start);

        if (checkLocationServicesStatus()) {
            checkRunTimePermission();
        } else {
            showDialogForLocationServiceSetting();
        }

        setRetrofitInit();

        intent = getIntent();

        if (intent != null) {
            myFoodtruck = (Foodtruck) intent.getSerializableExtra("foodtruck");
        }

        startBtn = (Button) findViewById(R.id.startBtn);

        gpsTracker = new GpsTracker(this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginCheck = SharedPreference.getAttribute(getApplicationContext(), "id") == null;

        if(!loginCheck) {
            loginBtn.setText("로그아웃");
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBusiness();
            }
        });

        loginBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(SharedPreference.getAttribute(getApplicationContext(), "no") == null)) {
                    SharedPreference.removeAllAttribute(getApplicationContext());
                    loginBtn.setText("로그인");
                    Toast.makeText(BusinessStartActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mapView = new MapView(this);

        myMarker = new MapPOIItem();
        myMarker.setUserObject(myFoodtruck);
        if (myFoodtruck.getStatus().equals("Y")) {
            MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(myFoodtruck.getLat(), myFoodtruck.getLng());
            // 중심점 변경 + 줌 레벨 변경
            mapView.setMapCenterPoint(MARKER_POINT, true);
            startBtn.setText("영업 종료");

            myMarker.setItemName("등록된 위치");
            myMarker.setMapPoint(MARKER_POINT);
            searchRadius = new MapCircle(
                    MARKER_POINT, // center
                    300, // radius
                    Color.argb(128, 255, 0, 0), // strokeColor
                    Color.argb(0, 0, 0, 0) // fillColor
            );
        } else {
            MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);
            mapView.setMapCenterPoint(MARKER_POINT, true);
            myMarker.setItemName("등록할 위치");
            myMarker.setMapPoint(MARKER_POINT);
            searchRadius = new MapCircle(
                    MARKER_POINT,
                    300,
                    Color.argb(128, 255, 0, 0),
                    Color.argb(0, 0, 0, 0)
            );
        }

        myMarker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
        myMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        myMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(myMarker);
        mapView.addCircle(searchRadius);

        foodtruckLocationSearch();
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

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    // 푸드트럭 찾기
    public void foodtruckLocationSearch() {
        if (foodtrucks == null) {
            foodtrucks = new ArrayList<Foodtruck>();
        }

        Call<List<Foodtruck>> call = service.foodtruckInquiry(null, null);
        call.enqueue(new Callback<List<Foodtruck>>() {
            @Override
            public void onResponse(Call<List<Foodtruck>> call, Response<List<Foodtruck>> response) {
                try {
                    foodtrucks = response.body();

                    mapListener = new MapEventListener(myMarker, searchRadius, foodtrucks);
                    mapView.setMapViewEventListener(mapListener);

                    Location myLocation = new Location("");
                    myLocation.setLatitude(latitude);
                    myLocation.setLongitude(longitude);

                    int radius = searchRadius.getRadius();
                    for (Foodtruck item : foodtrucks) {
                        Location markerLocation = new Location("");
                        markerLocation.setLatitude(item.getLat());
                        markerLocation.setLongitude(item.getLng());

                        float distance = markerLocation.distanceTo(myLocation);
                        if (distance <= radius) {
                            if (myFoodtruck.getNo() != item.getNo()) {
                                MapPOIItem marker = new MapPOIItem();

                                marker.setItemName(item.getName());
                                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(item.getLat(), item.getLng()));
                                marker.setTag(item.getNo());
                                marker.setUserObject(item);
                                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
                                marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
                                marker.setCustomImageResourceId(R.drawable.foodtruck_m_icon); // 마커 이미지.
                                marker.setCustomImageAutoscale(true); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                                marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

                                mapView.addPOIItem(marker);
                            }
                        }
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

    // 영업 시작
    public void startBusiness() {
        if (myFoodtruck.getStatus().equals("Y")) {
            myFoodtruck.setLat(0);
            myFoodtruck.setLng(0);
            myFoodtruck.setStatus("N");
        } else {
            myFoodtruck.setLat(myMarker.getMapPoint().getMapPointGeoCoord().latitude);
            myFoodtruck.setLng(myMarker.getMapPoint().getMapPointGeoCoord().longitude);
            myFoodtruck.setStatus("Y");
        }

        Call<ResponseBody> call = service.startBusiness(myFoodtruck.getNo(), myFoodtruck);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    boolean result = gson.fromJson(response.body().string(), Boolean.class);
                    if (result) {
                        Toast.makeText(getBaseContext(),"업데이터 완료!!",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(),"영업 등록이 실패하였습니다. 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                    }

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ======GPS 관련 메소드=========
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

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
