package com.yhsnmt.nmt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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



        SharedPreferences pref;
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        String latitude = pref.getString("latitude",null); //해당값 불러오는 것, 해당값이 없을 경우 null호출
        String longitude = pref.getString("longitude",null);



        System.out.println("데이터저장값"+ latitude);


        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DestinationActivity.class);
                startActivity(intent);
            }
        });




        tb1.setOnClickListener(new View.OnClickListener() {



            @Override

            public void onClick(View v) {

                if(tb1.isChecked()){

                    tb1.setBackgroundDrawable(

                            getResources().

                                    getDrawable(R.drawable.makon)

                    );

                }else{

                    tb1.setBackgroundDrawable(

                            getResources().

                                    getDrawable(R.drawable.makoff)

                    );

                }

            }

        });

        tb2.setOnClickListener(new View.OnClickListener() {



            @Override

            public void onClick(View v) {

                if(tb2.isChecked()){

                    tb2.setBackgroundDrawable(

                            getResources().

                                    getDrawable(R.drawable.haon)

                    );

                }else{

                    tb2.setBackgroundDrawable(

                            getResources().

                                    getDrawable(R.drawable.haoff)

                    );

                }

            }

        });




    }
}