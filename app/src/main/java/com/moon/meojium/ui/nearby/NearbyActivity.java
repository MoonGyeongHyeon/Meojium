package com.moon.meojium.ui.nearby;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.moon.meojium.R;
import com.moon.meojium.base.util.PermissionChecker;
import com.moon.meojium.database.dao.MuseumDao;
import com.moon.meojium.model.museum.Museum;
import com.moon.meojium.ui.detail.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moon on 2017. 8. 11..
 */

public class NearbyActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapClickListener {
    private static final double NEARBY_DISTANCE = 20;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private LocationManager locationManager;
    private LatLng currentPosition;
    private List<Museum> nearbyMuseumList;
    private GoogleMap map;
    private MapFragment mapFragment;
    private MuseumDao museumDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        ButterKnife.bind(this);

        museumDao = MuseumDao.getInstance();

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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerDragListener(this);
        map.setOnMapClickListener(this);

        this.map = map;

        initDefaultLocation();
    }

    private void initDefaultLocation() {
        LatLng SEOUL = new LatLng(37.56, 126.97);

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
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
                finish();
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
    public boolean onMyLocationButtonClick() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toasty.info(NearbyActivity.this, "위치 정보를 얻을 수 없습니다.").show();
        } else {
            map.clear();

            try {
                currentPosition = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
                Log.d("Meojium/Nearby", "Latitude: " + String.valueOf(currentPosition.latitude) +
                        ", Longitude: " + String.valueOf(currentPosition.longitude));

                requestNearbyMuseumData();

            } catch (Exception e) {
                e.printStackTrace();
                Toasty.info(NearbyActivity.this, "잠시 후 다시 시도해주세요.").show();
            }
        }

        return false;
    }

    private void requestNearbyMuseumData() {
        Call<List<Museum>> call = museumDao.getNearbyMuseumList(currentPosition.latitude, currentPosition.longitude, NEARBY_DISTANCE);
        call.enqueue(new Callback<List<Museum>>() {
            @Override
            public void onResponse(Call<List<Museum>> call, Response<List<Museum>> response) {
                nearbyMuseumList = response.body();
                Toasty.info(NearbyActivity.this, "반경 " + NEARBY_DISTANCE + "km 내에 위치한 박물관을 검색합니다.").show();

                addCurrentPositionMarker();
                addNearbyMuseumMarker();
                addNearbyCircle();
            }

            @Override
            public void onFailure(Call<List<Museum>> call, Throwable t) {
                Toasty.info(NearbyActivity.this, getResources().getString(R.string.fail_connection)).show();
            }
        });
    }

    private void addCurrentPositionMarker() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentPosition);
        markerOptions.draggable(true);
        markerOptions.title("기준");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(98f));
        map.addMarker(markerOptions);
    }

    private void addNearbyMuseumMarker() {
        for (Museum museum : nearbyMuseumList) {
            LatLng latLng
                    = new LatLng(museum.getLatitude()
                    , museum.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(museum.getName());
            markerOptions.snippet(museum.getAddress());
            map.addMarker(markerOptions);
        }
    }

    private void addNearbyCircle() {
        CircleOptions circleOptions = new CircleOptions().center(currentPosition)
                .radius(NEARBY_DISTANCE * 1000)
                .strokeWidth(0f)
                .fillColor(ContextCompat.getColor(this, R.color.colorPrimaryLightOpacity));

        map.addCircle(circleOptions);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Museum sendMuseum = null;
        for (Museum museum : nearbyMuseumList) {
            if (!museum.getName().equals(marker.getTitle())) {
                continue;
            }
            sendMuseum = museum;
        }

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", sendMuseum.getId());
        startActivity(intent);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        currentPosition = marker.getPosition();
        map.clear();

        requestNearbyMuseumData();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        currentPosition = latLng;
        map.clear();

        requestNearbyMuseumData();
    }
}
