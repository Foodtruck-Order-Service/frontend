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
            loginBtn.setText("????????????");
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
                    loginBtn.setText("?????????");
                    Toast.makeText(BusinessStartActivity.this, "???????????? ???????????????.", Toast.LENGTH_SHORT).show();
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
            // ????????? ?????? + ??? ?????? ??????
            mapView.setMapCenterPoint(MARKER_POINT, true);
            startBtn.setText("?????? ??????");

            myMarker.setItemName("????????? ??????");
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
            myMarker.setItemName("????????? ??????");
            myMarker.setMapPoint(MARKER_POINT);
            searchRadius = new MapCircle(
                    MARKER_POINT,
                    300,
                    Color.argb(128, 255, 0, 0),
                    Color.argb(0, 0, 0, 0)
            );
        }

        myMarker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
        myMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // ???????????? ???????????? BluePin ?????? ??????.
        myMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // ????????? ???????????????, ???????????? ???????????? RedPin ?????? ??????.

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

    // ???????????? ??????
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
                                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // ??????????????? ????????? ????????? ??????.
                                marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
                                marker.setCustomImageResourceId(R.drawable.foodtruck_m_icon); // ?????? ?????????.
                                marker.setCustomImageAutoscale(true); // hdpi, xhdpi ??? ??????????????? ???????????? ???????????? ????????? ?????? ?????? ?????????????????? ????????? ????????? ??????.
                                marker.setCustomImageAnchor(0.5f, 1.0f); // ?????? ???????????? ????????? ?????? ??????(???????????????) ?????? - ?????? ????????? ?????? ?????? ?????? x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) ???.

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
                Toast.makeText(getBaseContext(),"?????? ??????",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ?????? ??????
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
                        Toast.makeText(getBaseContext(),"???????????? ??????!!",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(),"?????? ????????? ?????????????????????. ?????? ??????????????????.",Toast.LENGTH_SHORT).show();
                    }

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(),"?????? ??????",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ======GPS ?????? ?????????=========
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // ?????? ????????? PERMISSIONS_REQUEST_CODE ??????, ????????? ????????? ???????????? ??????????????????

            boolean check_result = true;

            // ?????? ???????????? ??????????????? ???????????????.

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
                    Toast.makeText(this, "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "???????????? ?????????????????????. ???????????? ???????????? ???????????? ?????????. ", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    void checkRunTimePermission() {
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(this, "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                // ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //??????????????? GPS ???????????? ?????? ????????????
    private void showDialogForLocationServiceSetting() {

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n"
                + "?????? ????????? ???????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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

                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS ????????? ?????????");
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
