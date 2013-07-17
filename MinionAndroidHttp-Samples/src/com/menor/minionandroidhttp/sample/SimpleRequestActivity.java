package com.menor.minionandroidhttp.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.menor.minionandroidhttp.MinionStringListener;

public class SimpleRequestActivity extends Activity {

    private TextView getView;
    private TextView getTimeView;
    private TextView postView;
    private TextView postTimeView;
    private TextView putView;
    private TextView putTimeView;
    private TextView deleteView;
    private TextView deleteTimeView;

    ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_request);
        setViews();
        doStuff();
    }




    private void setViews() {
        getView = (TextView) findViewById(R.id.response_get);
        getTimeView = (TextView) findViewById(R.id.response_get_time);
        postView = (TextView) findViewById(R.id.response_post);
        postTimeView = (TextView) findViewById(R.id.response_post_time);
        putView = (TextView) findViewById(R.id.response_put);
        putTimeView = (TextView) findViewById(R.id.response_put_time);
        deleteView = (TextView) findViewById(R.id.response_delete);
        deleteTimeView = (TextView) findViewById(R.id.response_delete_time);

        mProgressDialog = new ProgressDialog(SimpleRequestActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
    }

    private void doStuff() {
        doGetRequest();
        doPostRequest();
        doPutRequest();
        doDeleteRequest();
    }

    private void doPostRequest() {

    }

    private void doGetRequest() {
        DespicableHttpClient.get("https://raw.github.com/square/okhttp/master/README.md", null, new MinionStringListener() {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                getView.setText(content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                Toast.makeText(SimpleRequestActivity.this, content, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPostExecute() {
                mProgressDialog.dismiss();
            }
        });
    }

    private void doPutRequest() {

    }

    private void doDeleteRequest() {

    }






}
