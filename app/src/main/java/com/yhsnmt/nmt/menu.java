package com.yhsnmt.nmt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

public class menu extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final ToggleButton tb1=findViewById(R.id.tb1);
        final ToggleButton tb2=findViewById(R.id.tb2);
        final Button route=findViewById(R.id.route);


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