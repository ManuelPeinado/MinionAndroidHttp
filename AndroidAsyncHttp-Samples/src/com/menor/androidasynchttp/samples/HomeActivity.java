package com.menor.androidasynchttp.samples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
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
        params.put("idcliente", "15600");
//        ExampleClient.post("sitios-grabar.php", params, this);
    }

}
