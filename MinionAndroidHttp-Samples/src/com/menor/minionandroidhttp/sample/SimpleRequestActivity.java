package com.menor.minionandroidhttp.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.menor.minionandroidhttp.MinionObjectListener;
import com.menor.minionandroidhttp.MinionStringListener;

import java.lang.reflect.Type;
import java.util.List;

public class SimpleRequestActivity extends Activity {

    private TextView getView;
    private TextView postView;
    private TextView putView;
    private TextView deleteView;

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
        postView = (TextView) findViewById(R.id.response_post);
        putView = (TextView) findViewById(R.id.response_put);
        deleteView = (TextView) findViewById(R.id.response_delete);

        mProgressDialog = new ProgressDialog(SimpleRequestActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
    }

    private void doStuff() {
        doFirstGetRequest();
        doSecondGetRequest();
        doPostRequest();
        doPutRequest();
        doDeleteRequest();
    }

    private void doPostRequest() {

    }

    private void doFirstGetRequest() {
        DespicableHttpClient.get("https://raw.github.com/square/okhttp/master/README.md", null, new MinionStringListener() {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                getView.setText(content);
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

    private void doSecondGetRequest() {
        Type classType = new TypeToken<List<Contributor>>() {}.getType();
        DespicableHttpClient.get("https://api.github.com/repos/square/okhttp/contributors", null, new MinionObjectListener(classType) {
            @Override
            public void onPreExecute() {
                mProgressDialog.show();
            }

            @Override
            public void onSuccess(Object content) {
                String finalText = "";
                List<Contributor> contributors = (List<Contributor>) content;
                for (Contributor contrib : contributors) {
                    finalText += "login: " + contrib.login +"\n" + "contributions: " + contrib.contributions + "\n\n";
                }

                postView.setText(finalText);
            }

            @Override
            public void onFailure(Throwable error, Object errorResponse) {
                super.onFailure(error, errorResponse);    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
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

    class Contributor {
        String login;
        int contributions;
    }




}
