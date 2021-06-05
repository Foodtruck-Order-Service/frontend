package kr.co.fos.client.foodtruck.map;

import android.app.Activity;
import android.content.Intent;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import kr.co.fos.client.foodtruck.Foodtruck;
import kr.co.fos.client.foodtruck.FoodtruckMainActivity;

public class MarkerEventListener implements MapView.POIItemEventListener{
    Activity activity;

    public MarkerEventListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        if (mapPOIItem.getMarkerType() == MapPOIItem.MarkerType.CustomImage) {
            Foodtruck foodtruck = (Foodtruck) mapPOIItem.getUserObject();

            Intent intent = new Intent(activity.getBaseContext(), FoodtruckMainActivity.class);
            intent.putExtra("foodtruck", foodtruck);
            activity.startActivity(intent);
        }
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
}
