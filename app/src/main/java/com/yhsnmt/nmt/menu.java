package com.yhsnmt.nmt;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
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

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


public class menu extends AppCompatActivity {


    private FusedLocationProviderClient mFusedLocationClient;

    String a = "CjI+3L5fsV83FFBgkif3WjrvVKFCgsajnJxD9jYuRSU";
    private String slo = "", sla = ""; //
    private String s_latit = "", s_longit = ""; // 서버 저장 값 받아올 변수들


    ODsayService mODsayService;


    private String result1;
    private String result2;
    private String result3 = "";
    private String result4 = "";

    private String makonlongit;
    private String makonlatit;

    FirebaseDatabase DB = FirebaseDatabase.getInstance();
    DatabaseReference server_latit  = DB.getReference("server_latit");
    DatabaseReference server_longit = DB.getReference("server_longit");




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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


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



                    if ((latit) == null && server_latit == null) {
                        Toast.makeText(getBaseContext(), "우측 상단 설정을 누르고 목적지를 설정해주세요", Toast.LENGTH_SHORT).show();

                    } else {

                        tb1.setBackgroundDrawable(getResources().getDrawable(R.drawable.makon));
                        Notify_makon();

                        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {
                                    //logic to handle location object
                                    Double latitude = location.getLatitude();
                                    Double longitude = location.getLongitude();


                                    makonlatit = Double.toString(latitude);
                                    makonlongit = Double.toString(longitude);

                                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
                                    editor.putString("start_latitude", makonlatit);
                                    editor.putString("start_longitude", makonlongit);
                                    editor.commit(); //완료한다.

                                    server_latit.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            s_latit = dataSnapshot.getValue(String.class);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    server_longit.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            s_longit = dataSnapshot.getValue(String.class);

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }


                                    });

                                    if(longit == null) {
                                        mODsayService.requestSearchPubTransPath(makonlongit, makonlatit, s_longit, s_latit, "", "", "1", onResultCallbackListener1);
                                    }
                                    else
                                        mODsayService.requestSearchPubTransPath(makonlongit, makonlatit, longit, latit, "", "", "1",onResultCallbackListener1);
                                }









                        }
                        });

                    }


                } else {

                    tb1.setBackgroundDrawable(

                            getResources().getDrawable(R.drawable.makoff)



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

                    if ((latit) == null && server_latit == null) {
                        Toast.makeText(getBaseContext(), "우측 상단 설정을 누르고 목적지를 설정해주세요", Toast.LENGTH_SHORT).show();

                    } else {

                        tb2.setBackgroundDrawable(getResources().getDrawable(R.drawable.haon));

                        Notify_haon();


                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                10000, // 통지사이의 최소 시간간격 (miliSecond)
                                50, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);
                        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                10000, // 통지사이의 최소 시간간격 (miliSecond)
                                50, // 통지사이의 최소 변경거리 (m)
                                mLocationListener);



                    }


                } else {

                    tb2.setBackgroundDrawable(

                            getResources().getDrawable(R.drawable.haoff)


                    );

                    lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체

                    removeNotification(); // 해제 시 푸시 제거



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

            sla = Double.toString(current_lat); // 출발지 검색
            slo = Double.toString(current_long);

            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.


            SharedPreferences pref;
            pref = getSharedPreferences("pref", MODE_PRIVATE);
            final String latit = pref.getString("latitude", null); //목적지 저장 값 레퍼런스
            final String longit = pref.getString("longitude", null);


            server_latit.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    s_latit = dataSnapshot.getValue(String.class); //목적지 저장 값 서버
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            server_longit.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    s_longit = dataSnapshot.getValue(String.class);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });



            if(longit == null) {
                mODsayService.requestSearchPubTransPath(slo, sla, s_longit, s_latit, "", "", "1", onResultCallbackListener);
            }
            else
                mODsayService.requestSearchPubTransPath(slo, sla, longit, latit, "", "", "1",onResultCallbackListener);
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


    OnResultCallbackListener onResultCallbackListener2 = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {


            try {
                if (api == API.SUBWAY_PATH) {
                    int totaltime = oDsayData.getJson().getJSONObject("result").getInt("globalTravelTime");

                    //String totaltime = String.valueOf(c);

                    System.out.println("걸리는 시간 : " + totaltime);


                    int minus_minute = 20*60 + totaltime;

                    Intent intent = new Intent(getApplicationContext(), NearStartStation.class);
                    startActivity(intent);
                    finish();

                    for(int i=0; i<300 ; i++) {
                        creation();
                    }





                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(int i, String s, API api) {
            Log.d("Test", "Error");
            if (api == API.SUBWAY_PATH) {
            }
            notsofarnoti();
        }
    };


    OnResultCallbackListener onResultCallbackListener1 = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {


            try {
                if (api == API.SEARCH_PUB_TRANS_PATH) {

                    String station = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getString(0);
                    String target1 = "firstStartStation";
                    String target2 = "lastEndStation\":\"";
                    String target3 = "startID\":";
                    String target4 = "endID\":";
                    int target_num1 = station.indexOf(target1)+20;
                    int target_num2 = station.indexOf("\",\"lastEnd");
                    int target_num3 = station.indexOf(target2)+17;
                    int target_num4 = station.indexOf("\",\"totalWalkTime\":");

                    int target_num5 = station.indexOf(target3)+9;
                    int target_num6 = station.indexOf(",\"startName\":");
                    int target_num7 = station.indexOf(target4)+7;
                    int target_num8 = station.indexOf(",\"endNam");


                    result1 = station.substring(target_num1, target_num2);
                    result2 = station.substring(target_num3, target_num4);
                    result3 = station.substring(target_num5, target_num6); // 출발역 지하철역 ID
                    result4 = station.substring(target_num7, target_num8); // 도착역 지하철역 ID


                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();// editor에 put 하기
                    editor.putString("start_station_name", result1);
                    editor.putString("startID", result3);
                    editor.commit(); //완료한다.


                    System.out.println("result... : " + result3);
                    System.out.println("result... : " + result4);

                    mODsayService.requestSubwayPath("1000", result3, result4, "1" ,onResultCallbackListener2);






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


    OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {


            try {
                if (api == API.SEARCH_PUB_TRANS_PATH) {
                    String Distance = oDsayData.getJson().getJSONObject("result").getString("pointDistance");

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

            for(int a=0; a<10; a++)
                createNotification();
        }
    };

    private void Notify_makon() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");


        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("막차 알리미가 실행되었습니다.");
        builder.setContentText("막차 15분 전에 알려드릴게요");

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

    private void Notify_haon() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");


        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("하차 알리미가 실행되었습니다.");
        builder.setContentText("목적지에 가까워지면 알려드릴게요");

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
        notificationManager.notify(4, builder.build());
    }



    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");


        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("목적지의 1km 이내에 진입하셨습니다. 내릴 준비하세요!!!!");
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
        notificationManager.notify(3, builder.build());
    }


    private void notsofarnoti() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");


        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("목적지가 너무 가까워요");
        builder.setContentText("걸어가셔도 될 것 같은데요?");

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
        notificationManager.notify(45, builder.build());
    }

    private void creation() {
        String a = "가장가까운 승차하실 지하철 역은 " + result1  + "역 입니다.\n" + "하차하실 역은 " + result2 + "역 입니다.\n" + "알람을 멈추려면 막차 알리미를 종료해 주세요";
        // 요약 정보.




        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle("막차 15분 전 입니다 준비하세요.");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(a));
        builder.setContentText(a);
        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);



        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(111, builder.build());
    }



    private void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(3);
    }








}