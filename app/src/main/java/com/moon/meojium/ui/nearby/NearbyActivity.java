package com.moon.meojium.ui.nearby;

import android.Manifest;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.moon.meojium.R;
import com.moon.meojium.base.util.PermissionChecker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

/**
 * Created by moon on 2017. 8. 11..
 */

public class NearbyActivity extends AppCompatActivity
        implements OnMapReadyCallback, PlacesListener,
        GoogleMap.OnMyLocationButtonClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private LocationManager locationManager;
    private LatLng currentPosition;
    private List<Marker> previous_marker = null;
    private GoogleMap map;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        ButterKnife.bind(this);

        initToolbar();
        initGoogleMap();
    }

    private void initToolbar() {
        toolbar.setTitle("주변 박물관");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initGoogleMap() {
        mapFragment = new MapFragment();
        mapFragment.getMapAsync(this);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_nearby_map_container, mapFragment)
                .commit();
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!(PermissionChecker.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    && PermissionChecker.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION))) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PermissionChecker.REQUEST_LOCATION_INFO);

                return;
            }
        }

        this.map = map;

        previous_marker = new ArrayList<>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);

        initDefaultLocation();
    }

    private void initDefaultLocation() {
        LatLng SEOUL = new LatLng(37.56, 126.97);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionChecker.REQUEST_LOCATION_INFO) {
            if (PermissionChecker.verifyPermission(grantResults)) {
                Log.d("Meojium/Nearby", "Grant Permission");
                initGoogleMap();
            } else {
                Log.d("Meojium/Nearby", "Deny Permission");
                Toasty.info(this, "위치 정보를 얻을 수 없어 지도를 이용할 수 없습니다.").show();
                onBackPressed();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(place.getVicinity());
                    Marker item = map.addMarker(markerOptions);
                    previous_marker.add(item);

                }

                HashSet<Marker> hashSet = new HashSet<>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);
            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toasty.info(NearbyActivity.this, "위치 정보를 얻을 수 없습니다.").show();
        } else {
            try {
                currentPosition = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
                Log.d("Meojium/Nearby", "Latitude: " + String.valueOf(currentPosition.latitude) +
                        ", Longitude: " + String.valueOf(currentPosition.longitude));

                showPlaceInformation(currentPosition);
            } catch (Exception e) {
                e.printStackTrace();
                Toasty.info(NearbyActivity.this, "일시적인 오류가 발생했습니다.").show();
            }
        }

        return false;
    }

    public void showPlaceInformation(LatLng location) {
        map.clear();

        if (previous_marker != null)
            previous_marker.clear();

        new NRPlaces.Builder()
                .listener(this)
                .key("AIzaSyA8sYpTd16DAZezlKE0H8ykf6DrL5nPM-w")
                .latlng(location.latitude, location.longitude)
                .radius(10000)
                .type(PlaceType.MUSEUM)
                .build()
                .execute();
    }
}
