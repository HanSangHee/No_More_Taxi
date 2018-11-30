package com.yhsnmt.nmt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class menu extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final ToggleButton tb1=findViewById(R.id.tb1);
        final ToggleButton tb2=findViewById(R.id.tb2);
        final Button route = findViewById(R.id.route);
        final Button set = findViewById(R.id.setting);
        final Button mail = findViewById(R.id.mail);


        SharedPreferences pref;
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        final String latitude = pref.getString("latitude",null); //해당값 불러오는 것, 해당값이 없을 경우 null호출
        final String longitude = pref.getString("longitude",null);



        System.out.println("데이터저장값"+ latitude);


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
                    if(emailIntent.resolveActivity(getPackageManager())!=null)
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

                if(tb1.isChecked()){

                    if ((latitude) == null) {
                        Toast.makeText(getBaseContext(), "우측 상단 설정을 누르고 목적지를 설정해주세요", Toast.LENGTH_SHORT).show();

                    }
                    else{

                    tb1.setBackgroundDrawable(

                            getResources().getDrawable(R.drawable.makon));

                    }


                }else{

                    tb1.setBackgroundDrawable(

                            getResources().getDrawable(R.drawable.makoff)

                    );

                }

            }

        });

        tb2.setOnClickListener(new View.OnClickListener() {



            @Override

            public void onClick(View v) {



                if(tb2.isChecked()){

                    if ((latitude) == null) {
                        Toast.makeText(getBaseContext(), "우측 상단 설정을 누르고 목적지를 설정해주세요", Toast.LENGTH_SHORT).show();

                    }
                    else{

                        tb2.setBackgroundDrawable(

                                getResources().getDrawable(R.drawable.haon));

                    }


                }else{

                    tb2.setBackgroundDrawable(

                            getResources().getDrawable(R.drawable.haoff)

                    );

                }


            }

        });




    }
}