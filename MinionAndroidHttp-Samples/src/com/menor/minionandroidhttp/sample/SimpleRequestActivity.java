package com.menor.minionandroidhttp.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.menor.minionandroidhttp.MinionHttpResponseHandler;
import com.menor.minionandroidhttp.SuccessResponse;
import com.menor.minionandroidhttp.RequestParams;

import java.util.Date;

public class SimpleRequestActivity extends Activity {

    private TextView getView;
    private TextView getTimeView;
    private TextView postView;
    private TextView postTimeView;
    private TextView putView;
    private TextView putTimeView;
    private TextView deleteView;
    private TextView deleteTimeView;

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
        postView = (TextView) findViewById(R.id.response_get);
        getView = (TextView) findViewById(R.id.response_get);
        getView = (TextView) findViewById(R.id.response_get);
        getView = (TextView) findViewById(R.id.response_get);
        getView = (TextView) findViewById(R.id.response_get);
        getView = (TextView) findViewById(R.id.response_get);
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
        final long start = new Date().getTime();
        RequestParams params = new RequestParams();
        params.put("clave", "iabadabadu");
        params.put("index", "0");
        DespicableHttpClient.get("", params, new MinionHttpResponseHandler() {

            @Override
            public void onSuccess(SuccessResponse response) {
                super.onSuccess(response);
                getView.setText(response.getContent());
                getTimeView.setText("" +  (new Date().getTime() - start) + " milliseconds");
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                getView.setText(error.getMessage());
                getTimeView.setText("" +  (new Date().getTime() - start) + " milliseconds");
            }

        });

    }

    private void doPutRequest() {

    }

    private void doDeleteRequest() {

    }






}
