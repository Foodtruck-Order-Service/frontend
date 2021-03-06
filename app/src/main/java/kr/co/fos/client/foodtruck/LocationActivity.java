package kr.co.fos.client.foodtruck;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import kr.co.fos.client.foodtruck.map.MarkerEventListener;
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

        if (intent != null) {
            String name = intent.getStringExtra("name");
            searchView.setQuery(name, false);
        }

        loginBtn = findViewById(R.id.loginBtn);
        loginCheck = SharedPreference.getAttribute(getApplicationContext(), "no") != null;

        if(loginCheck) {
            loginBtn.setText("????????????");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                intent = new Intent(getApplicationContext(),SearchResultActivity.class);
                intent.putExtra("name", query);
                startActivity(intent);

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
                    SharedPreference.removeAllAttribute(getApplicationContext());
                    loginBtn.setText("?????????");
                    Toast.makeText(LocationActivity.this, "???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView = new MapView(this);

        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);

        gpsTracker = new GpsTracker(this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(latitude, longitude);

        // ????????? ?????? + ??? ?????? ??????
        mapView.setMapCenterPointAndZoomLevel(MARKER_POINT, -1, true);

        markerListener = new MarkerEventListener(this);

        MapCircle circle1 = new MapCircle(
                MARKER_POINT, // center
                300, // radius
                Color.argb(128, 255, 0, 0), // strokeColor
                Color.argb(0, 0, 0, 0) // fillColor
        );

        mapView.addCircle(circle1);

        MapPOIItem myMarker = new MapPOIItem();
        myMarker.setItemName("??? ??????");
        myMarker.setMapPoint(MARKER_POINT);
        myMarker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
        myMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // ???????????? ???????????? BluePin ?????? ??????.
        myMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // ????????? ???????????????, ???????????? ???????????? RedPin ?????? ??????.

        mapView.addPOIItem(myMarker);

        foodtruckLocationSearch();

        mapView.setPOIItemEventListener(markerListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapViewContainer.addView(mapView);
    }

    @Nullable
    @Override
    public void onPause() {
        mapViewContainer.removeView(mapView);
        super.onPause();
    }

    @Nullable
    @Override
    public void onStop() {
        mapViewContainer.removeView(mapView);
        super.onStop();
    }

    private void setRetrofitInit() {
        client = new Retrofit.Builder()
                .baseUrl(HttpInterface.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        service = client.create(HttpInterface.class);
    }

    // ??????
    public void search() {

    }

    // ????????? ???????????? ??????
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
                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // ??????????????? ????????? ????????? ??????.
                        marker.setShowAnimationType(MapPOIItem.ShowAnimationType.SpringFromGround);
                        marker.setCustomImageResourceId(R.drawable.foodtruck_m_icon); // ?????? ?????????.
                        marker.setCustomImageAutoscale(true); // hdpi, xhdpi ??? ??????????????? ???????????? ???????????? ????????? ?????? ?????? ?????????????????? ????????? ????????? ??????.
                        marker.setCustomImageAnchor(0.5f, 1.0f); // ?????? ???????????? ????????? ?????? ??????(???????????????) ?????? - ?????? ????????? ?????? ?????? ?????? x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) ???.

                        mapView.addPOIItem(marker);
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

    //======GPS ?????? ?????????=========
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
