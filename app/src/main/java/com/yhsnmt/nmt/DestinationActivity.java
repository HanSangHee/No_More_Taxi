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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.location.LocationListener;
import android.location.Location;
import android.widget.Toast;

import static android.app.ProgressDialog.show;


public class DestinationActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button;
    private EditText editText;
    private double latitude = 0, longitude = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    private ImageButton current;
    FirebaseDatabase DB = FirebaseDatabase.getInstance();
    DatabaseReference server_latit  = DB.getReference("server_latit");
    DatabaseReference server_longit = DB.getReference("server_longit");







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        current= (ImageButton) findViewById(R.id.current);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        current.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {

                mMap.clear(); // 마커 제거


                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), "위치정보 권한을 허용해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    // gps 켜져 있는지 확인 필요



                    mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            //got last known location, in some rate situations this can be null
                            if (location != null) {
                                //logic to handle location object
                                MarkerOptions mOptions3 = new MarkerOptions();
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                Log.d("위도", String.valueOf(latitude));
                                Log.d("경도", String.valueOf(longitude));
                                LatLng b = new LatLng(latitude, longitude);

                                mOptions3.position(b);
                                mOptions3.title("현위치!");
                                mMap.addMarker(mOptions3);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(b, 15));



                                String latit = Double.toString(latitude);
                                String longit = Double.toString(longitude);
                                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
                                editor.putString("latitude", latit);
                                editor.putString("longitude", longit);
                                editor.commit(); //완료한다.
                                server_latit.setValue(latit);
                                server_longit.setValue(longit);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), menu.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 2500);
                                // 위도 경도 정보를 4-ㄱ ,4-ㄴ으로 전달(목적 클래스 재지정 필요)

                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                mMap.clear(); // 마커 초기화
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);




                String latit = Double.toString(latitude);
                String longit = Double.toString(longitude);
                SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
                editor.putString("latitude", latit);
                editor.putString("longitude", longit);
                editor.commit(); //완료한다.
                server_latit.setValue(latit);
                server_longit.setValue(longit);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), menu.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2500);


                // 위도 경도 정보를 4-ㄱ ,4-ㄴ으로 전달(목적 클래스 재지정 필요)


            }
        });
        ////////////////////

        // 버튼 이벤트
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                String str = editText.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(addressList != null){
                    if(!addressList.isEmpty()) {
                        System.out.println(addressList.get(0).toString());
                        // 콤마를 기준으로 split
                        String[] splitStr = addressList.get(0).toString().split(",");
                        String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소
                        System.out.println(address);

                        String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                        String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                        System.out.println(latitude);
                        System.out.println(longitude);

                        // 좌표(위도, 경도) 생성
                        LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        // 마커 생성
                        MarkerOptions mOptions2 = new MarkerOptions();
                        mOptions2.title("search result");
                        mOptions2.snippet(address);
                        mOptions2.position(point);
                        // 마커 추가
                        mMap.addMarker(mOptions2);
                        // 해당 좌표로 화면 줌
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));



                        String latit = latitude;
                        String longit = longitude;
                        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
                        editor.putString("latitude", latit);
                        editor.putString("longitude", longit);
                        editor.commit(); //완료한다.
                        server_latit.setValue(latit);
                        server_longit.setValue(longit);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(), menu.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 2500);

                        // 위도 경도 정보를 4-ㄱ ,4-ㄴ으로 전달(목적 클래스 재지정 필요)

                    }else{
                        Toast.makeText(getBaseContext(), "주소가 잘못되었습니다. 정확한 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(), "주소가 비어있습니다. 정확한 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LatLng a = new LatLng(37.5572321, 127.04532189999998);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(a, 13));

    }

}