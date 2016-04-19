package com.example.imomundo.restapp1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.imomundo.restapp1.api.Constants;
import com.example.imomundo.restapp1.api.MyApiCall;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private TextView userName, password;
    private Button signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (TextView) findViewById(R.id.userName);
        password = (TextView) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.signButton);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignIn();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("IDE Events");
        return super.onCreateOptionsMenu(menu);
    }

    private void onClickSignIn() {

        RequestParams params = new RequestParams();
        params.put("username", userName.getText().toString());
        params.put("password", password.getText().toString());
        params.put("client_id", "f3d259ddd3ed8ff3843839b");
        params.put("client_secret", "4c7f6f8fa93d59c45502c0ae8c4a95b");
        params.put("grant_type", "password");

        MyApiCall.post("oauth/access_token", params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println("SUCCESS RESPONSE OF YOUR REQUEST : " + responseString);
                try {
                    System.out.println("ACCESS TOKEN FROM SERVER : " + getAccessToken(responseString));
                    Intent intent = new Intent(getApplicationContext(), ListViewDataActivity.class);
                    intent.putExtra(Constants.ACCESS_TOKEN_INTENT, getAccessToken(responseString));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("FAILED RESPONSE OF YOUR REQUEST : " + responseString);
            }
        });
    }

    private String getAccessToken(String serverResponse) throws JSONException {
        String accessToken = "";
        JSONObject jsonObject = new JSONObject(serverResponse);
        accessToken = jsonObject.getString("access_token");
        return accessToken;
    }
}
