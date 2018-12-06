package com.yhsnmt.nmt;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONException;


public class menu extends AppCompatActivity {


    private FusedLocationProviderClient mFusedLocationClient;


    String a = "CjI+3L5fsV83FFBgkif3WjrvVKFCgsajnJxD9jYuRSU";
    private String slo = "", sla = "";
    ODsayService mODsayService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        final ToggleButton tb1 = findViewById(R.id.tb1);
        final ToggleButton tb2 = findViewById(R.id.tb2);
        final Button route = findViewById(R.id.route);
        final Button set = findViewById(R.id.setting);
        final Button mail = findViewById(R.id.mail);

        mODsayService = ODsayService.init(this, a);
        mODsayService.setReadTimeout(5000);
        mODsayService.setConnectionTimeout(5000);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        SharedPreferences pref;
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        final String latit = pref.getString("latitude", null);
        final String longit = pref.getString("longitude", null);


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), setting.class);
                startActivity(intent);
            }
        });


        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                try {
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"koo3751@hanyang.ac.kr"});

                    emailIntent.setType("text/html");
                    emailIntent.setPackage("com.google.android.gm");
                    if (emailIntent.resolveActivity(getPackageManager()) != null)
                        startActivity(emailIntent);

                    startActivity(emailIntent);
                } catch (Exception e) {
                    e.printStackTrace();

                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"koo3751@hanyang.ac.kr"});

                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                }
            }
        });


        tb1.setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), "위치정보 권한을 허용해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    // gps 켜져 있는지 확인 필요


                }

                if (tb1.isChecked()) {

                    if ((latit) == null) {
                        Toast.makeText(getBaseContext(), "우측 상단 설정을 누르고 목적지를 설정해주세요", Toast.LENGTH_SHORT).show();

                    } else {

                        tb1.setBackgroundDrawable(getResources().getDrawable(R.drawable.haon));





                    }


                } else {

                    tb1.setBackgroundDrawable(

                            getResources().getDrawable(R.drawable.haoff)


                    );



                }






            }

        });



        tb2.setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getBaseContext(), "위치정보 권한을 허용해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                    // gps 켜져 있는지 확인 필요


                }

                if (tb2.isChecked()) {

                    if ((latit) == null) {
                        Toast.makeText(getBaseContext(), "우측 상단 설정을 누르고 목적지를 설정해주세요", Toast.LENGTH_SHORT).show();

                    } else {

                        tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.haon));


                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                100, // 통지사이의 최소 시간간격 (miliSecond)
                                1, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);








                    }


                } else {

                    tb2.setBackgroundDrawable(

                            getResources().getDrawable(R.drawable.haoff)


                    );
                    lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                    removeNotification();



                }






            }

        });


    }
    private final LocationListener mLocationListener = new LocationListener() {


        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            double current_long = location.getLongitude(); //경도
            double current_lat = location.getLatitude();   //위도

            sla = Double.toString(current_lat);
            slo = Double.toString(current_long);

            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.

            SharedPreferences pref;
            pref = getSharedPreferences("pref", MODE_PRIVATE);
            final String latit = pref.getString("latitude", null);
            final String longit = pref.getString("longitude", null);

            System.out.println("목적지 위도값 : " + latit);
            System.out.println("목적지 위도값 : " + longit);
            System.out.println("출발지 위도값 : " + sla);
            System.out.println("출발지 경도값 : " + slo);


            mODsayService.requestSearchPubTransPath(slo, sla, longit, latit,"","","", onResultCallbackListener2);

        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }

    };
    OnResultCallbackListener onResultCallbackListener1 = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {


            try {
                if (api == API.SEARCH_PUB_TRANS_PATH) {
                    String Distance = oDsayData.getJson().getJSONObject("result").getString("pointDistance");
                    Log.d("원하는 값은", Distance);

                    int Dist = Integer.parseInt(Distance);

                    if (Dist < 3000) {

                        for(int i=0; i<5; i++)
                            createNotification();
                        // 알람 셋팅
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        @Override
        public void onError(int i, String s, API api) {
            Log.d("Test", "Error");
            if (api == API.SEARCH_PUB_TRANS_PATH) {
            }
        }
    };


    OnResultCallbackListener onResultCallbackListener2 = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {


            try {
                if (api == API.SEARCH_PUB_TRANS_PATH) {
                    String Distance = oDsayData.getJson().getJSONObject("result").getString("pointDistance");
                    Log.d("원하는 값은", Distance);

                    int Dist = Integer.parseInt(Distance);

                    if (Dist < 3000) {

                        for(int i=0; i<5; i++)
                            createNotification();
                        // 알람 셋팅
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        @Override
        public void onError(int i, String s, API api) {
            Log.d("Test", "Error");
            if (api == API.SEARCH_PUB_TRANS_PATH) {
            }
        }
    };


    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");


        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("내려야 해요!!!!");
        builder.setContentText("내리셨다면 하차 알리미를 터치하여 기능을 꺼주세요");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    private void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(1);
    }






}



