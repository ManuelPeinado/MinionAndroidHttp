package com.menor.androidasynchttp.samples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Date;

public class HomeActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView textView1 = (TextView) findViewById(R.id.response1);
        final TextView textView2 = (TextView) findViewById(R.id.response2);
        final TextView textView3 = (TextView) findViewById(R.id.response3);

        final long start = new Date().getTime();
        RequestParams params = new RequestParams();
        params.put("clave", "iabadabadu");
        params.put("usuario", "18");
        ExampleClient.post("sitios-grabar.php", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                textView1.setText(response);
                textView3.setText("sitios-grabar.php" + (new Date().getTime() - start) + " milliseconds");
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                final long start = new Date().getTime();
                textView1.setText(error.toString());
                textView2.setText(content);
                textView3.setText("" + (new Date().getTime() - start) + " milliseconds");
            }

        });
    }

}
