package com.yhsnmt.nmt;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class setting extends AppCompatActivity {


    final ImageButton email = findViewById(R.id.email);
    final ImageButton reset = findViewById(R.id.reset);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DestinationActivity.class);
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
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
    }
}
