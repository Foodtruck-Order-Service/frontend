package kr.co.fos.client.foodtruck.map;

import android.location.Location;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.List;

import kr.co.fos.client.R;
import kr.co.fos.client.foodtruck.Foodtruck;

public class MapEventListener implements MapView.MapViewEventListener {
    MapPOIItem myMarker;
    MapCircle searchRadius;
    List<Foodtruck> foodtrucks;

    public MapEventListener(MapPOIItem myMarker, MapCircle searchRadius, List<Foodtruck> foodtrucks) {
        this.myMarker = myMarker;
        this.searchRadius = searchRadius;
        this.foodtrucks = foodtrucks;
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        mapView.setZoomLevel(-1, true);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        mapView.removeAllPOIItems();
        mapView.removeAllCircles();

        myMarker.setMapPoint(mapPoint);
        searchRadius.setCenter(mapPoint);

        mapView.addPOIItem(myMarker);
        mapView.addCircle(searchRadius);

        Location myLocation = new Location("");
        myLocation.setLatitude(mapPoint.getMapPointGeoCoord().latitude);
        myLocation.setLongitude(mapPoint.getMapPointGeoCoord().longitude);

        int radius = searchRadius.getRadius();
        for (Foodtruck item : foodtrucks) {
            Location markerLocation = new Location("");
            markerLocation.setLatitude(item.getLat());
            markerLocation.setLongitude(item.getLng());

            float distance = markerLocation.distanceTo(myLocation);
            System.out.println("test, 거리:=>" + distance + ", 반경=>" + radius);
            if (distance <= radius) {
                if (((Foodtruck) myMarker.getUserObject()).getNo() != item.getNo()) {
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
}
