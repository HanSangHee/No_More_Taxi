package com.yhsnmt.nmt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {




    String a = "CjI+3L5fsV83FFBgkif3WjrvVKFCgsajnJxD9jYuRSU";

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DestinationActivity.class);
                startActivity(intent);
            }
        });

        final TextView textView = findViewById(R.id.textView);
        textView.setText("start");

        ODsayService oDsayService = ODsayService.init(this, a);
        oDsayService.setReadTimeout(5000);
        oDsayService.setConnectionTimeout(5000);
        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
            @Override
            public void onSuccess(ODsayData oDsayData, API api) {


                try{
                    if(api==API.SEARCH_PUB_TRANS_PATH) {
                        String station = oDsayData.getJson().getJSONObject("result").getJSONObject("path").getJSONObject("Info").getString("firstStartStation");
                        textView.setText("station information : "+ station);
                        Log.d("Test",oDsayData.getJson().getJSONObject("result").toString());
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                    textView.setText("그만떠라 json error");
                }
            }

            @Override
            public void onError(int i, String s, API api) {
                Log.d("Test", "Error");
                if(api==API.SEARCH_PUB_TRANS_PATH){
                    textView.setText(Integer.toString(i) +" + " + s + " + " + api );
                }
            }
        };
        oDsayService.requestSearchPubTransPath( "127.049495","37.556243", "127.126936754911", "37.5004198786564", "" ,"","", onResultCallbackListener);
    }
}
