package com.yhsnmt.nmt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.location.LocationListener;
import android.location.Location;
import android.widget.Toast;

import org.json.JSONException;

import static android.app.ProgressDialog.show;


public class NearStartStation extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Geocoder geocoder;
    private FusedLocationProviderClient mFusedLocationClient;
    String a = "CjI+3L5fsV83FFBgkif3WjrvVKFCgsajnJxD9jYuRSU";

    ODsayService mODsayService;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_start_station);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mODsayService = ODsayService.init(this, a);
        mODsayService.setReadTimeout(5000);
        mODsayService.setConnectionTimeout(5000);




    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

            }
        });

        mMap.clear(); // 마커 제거

        SharedPreferences pref;
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        final String la = pref.getString("start_latitude", null);
        final String lo = pref.getString("start_longitude", null);



        double latit = Double.parseDouble(la);
        double longit = Double.parseDouble(lo);




        final String startID = pref.getString("startID", null);
        mODsayService.requestSubwayStationInfo(startID, onResultCallbackListener);

        MarkerOptions mOptions2 = new MarkerOptions();// 현위치
        LatLng a = new LatLng(latit, longit);
        mOptions2.position(a);
        mOptions2.title("현위치");
        mMap.addMarker(mOptions2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(a, 20));





    }

    OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {


            try {
                if (api == API.SUBWAY_STATION_INFO) {
                    Double x = oDsayData.getJson().getJSONObject("result").getDouble("x");
                    Double y = oDsayData.getJson().getJSONObject("result").getDouble("y");



                    SharedPreferences pref;
                    pref = getSharedPreferences("pref", MODE_PRIVATE);
                    MarkerOptions mOptions3 = new MarkerOptions(); // 가까운 역
                    final String station_name = pref.getString("start_station_name", null); //안될수도
                    mOptions3.title(station_name+"역");
                    LatLng b = new LatLng(y, x);
                    mOptions3.position(b);
                    mMap.addMarker(mOptions3);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        @Override
        public void onError(int i, String s, API api) {
            Log.d("Test", "Error");
            if (api == API.SUBWAY_STATION_INFO) {
            }

        }
    };

}