package com.moon.meojium.ui.nearby;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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
import com.moon.meojium.base.util.Dlog;
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
        GoogleMap.OnMapClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final double NEARBY_DISTANCE = 20;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private LatLng currentPosition;
    private List<Museum> nearbyMuseumList;
    private GoogleMap map;
    private MapFragment mapFragment;
    private MuseumDao museumDao;
    private GoogleApiClient googleApiClient;

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
        if (Build.VERSION.SDK_INT >= 23) {
            if (!(PermissionChecker.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    && PermissionChecker.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION))) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PermissionChecker.REQUEST_LOCATION_INFO);

                return;
            }
        }

        mapFragment = new MapFragment();
        mapFragment.getMapAsync(this);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_nearby_map_container, mapFragment)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionChecker.REQUEST_LOCATION_INFO) {
            if (PermissionChecker.verifyPermission(grantResults)) {
                Dlog.d("Grant Permission");
                initGoogleMap();
            } else {
                Dlog.d("Deny Permission");
                Toasty.info(this, "위치 정보를 얻을 수 없어 지도를 이용할 수 없습니다.").show();
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerDragListener(this);
        map.setOnMapClickListener(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();

        this.map = map;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        map.clear();

        getDeviceLocation();

        requestNearbyMuseumData();

        return true;
    }

    private void getDeviceLocation() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        Dlog.d("Latitude: " + String.valueOf(currentPosition.latitude) +
                ", Longitude: " + String.valueOf(currentPosition.longitude));
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
                focusMap();
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

    private void focusMap() {
        map.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getDeviceLocation();
        requestNearbyMuseumData();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toasty.info(this, getString(R.string.fail_connection)).show();
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
}
