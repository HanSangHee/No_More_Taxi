package com.yhsnmt.nmt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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
        final TextView textView = findViewById(R.id.textView);


        textView.setText("start");

        ODsayService oDsayService = ODsayService.init(this, a);
        oDsayService.setReadTimeout(5000);
        oDsayService.setConnectionTimeout(5000);
        OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
            @Override
            public void onSuccess(ODsayData oDsayData, API api) {
                Log.d("Test",oDsayData.getJson().toString());

                try{
                    if(api==API.POINT_SEARCH) {
                        String station = oDsayData.getJson().getJSONObject("result").getJSONArray("station").getString(0);
                        textView.setText("station information : "+ station);
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                    textView.setText("그만떠라 json error");
                }
            }

            @Override
            public void onError(int i, String s, API api) {
                Log.d("Test", "Error");
                if(api==API.POINT_SEARCH){
                    textView.setText(Integer.toString(i) +" + " + s + " + " + api );
                }
            }
        };
        oDsayService.requestPointSearch( "127.049495","37.556243", "1000" , "1:2" ,onResultCallbackListener);
    }
}
