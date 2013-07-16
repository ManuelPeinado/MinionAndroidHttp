package com.menor.androidasynchttp.samples;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;

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
        params.put("sid", "Cap9lpnb2Fg3Bp,i1RPhL2");
        ExampleClient.get("list.json", params, new JsonHttpResponseHandler() {

            ProgressDialog mProgressDialog;

            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = new ProgressDialog(HomeActivity.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();
            }



            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                textView1.setText(response.toString());
                textView3.setText("sitios-grabar.php" + (new Date().getTime() - start) + " milliseconds");
            }

            @Override
            public void onFailure(Throwable error, JSONObject errorResponse) {
                super.onFailure(error, errorResponse);
                textView1.setText(errorResponse.toString());
                textView3.setText("sitios-grabar.php" + (new Date().getTime() - start) + " milliseconds");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }
        });
    }

}
