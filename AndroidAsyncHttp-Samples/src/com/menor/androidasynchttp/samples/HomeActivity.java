package com.menor.androidasynchttp.samples;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HomeActivity extends Activity {

    ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView textView1 = (TextView) findViewById(R.id.response1);
        final TextView textView2 = (TextView) findViewById(R.id.response2);
        final TextView textView3 = (TextView) findViewById(R.id.response3);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");

        ExampleClient.get("https://raw.github.com/square/okhttp/master/README.md", null, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                textView1.setText(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                Toast.makeText(HomeActivity.this, content, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                mProgressDialog.dismiss();
            }
        });

    }

}




