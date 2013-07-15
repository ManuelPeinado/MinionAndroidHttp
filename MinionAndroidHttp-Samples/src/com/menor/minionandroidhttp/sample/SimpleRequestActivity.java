package com.menor.minionandroidhttp.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.menor.minionandroidhttp.JsonHttpResponseHandler;
import com.menor.minionandroidhttp.RequestParams;
import org.json.JSONObject;

import java.util.Date;

public class SimpleRequestActivity extends Activity implements JsonHttpResponseHandler {

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
        postView = (TextView) findViewById(R.id.response_post);
        postTimeView = (TextView) findViewById(R.id.response_post_time);
        putView = (TextView) findViewById(R.id.response_put);
        putTimeView = (TextView) findViewById(R.id.response_put_time);
        deleteView = (TextView) findViewById(R.id.response_delete);
        deleteTimeView = (TextView) findViewById(R.id.response_delete_time);
    }

    private void doStuff() {
        doGetRequest();
        doPostRequest();
        doPutRequest();
        doDeleteRequest();
    }

    private void doPostRequest() {
        final long start = new Date().getTime();
        RequestParams params = new RequestParams();
        params.put("clave", "iabadabadu");
        params.put("usuario", "18");
        params.put("idcliente", "15600");
        DespicableHttpClient.post("sitios-grabar.php", params, this);
    }

    private void doGetRequest() {
        final long start = new Date().getTime();
        RequestParams params = new RequestParams();
        params.put("clave", "iabadabadu");
        params.put("index", "0");
        DespicableHttpClient.get("", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                getView.setText(response.toString());
                getTimeView.setText("sitios-grabar.php" + (new Date().getTime() - start) + " milliseconds");
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
