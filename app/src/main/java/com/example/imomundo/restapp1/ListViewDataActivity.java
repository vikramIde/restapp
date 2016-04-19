package com.example.imomundo.restapp1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.imomundo.restapp1.api.Constants;
import com.example.imomundo.restapp1.api.MyApiCall;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;

public class ListViewDataActivity extends AppCompatActivity {

    private String accessToken = "";
    private ListView eventListView;
    private List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_data);
        Intent intent = getIntent();
        accessToken = intent.getStringExtra(Constants.ACCESS_TOKEN_INTENT);
        eventListView = (ListView) findViewById(R.id.eventListView);
        if(accessToken != null && accessToken.length() > 0) {
            getEventsFromServer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("IDE Events");
        return super.onCreateOptionsMenu(menu);
    }
    private void getEventsFromServer() {
        MyApiCall.get("api/v1/events?access_token=" + accessToken, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                System.out.println("SUCCESS RESPONSE OF EVENTS :  " + str);
                try {
                    parseEventsData(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                eventListView.setAdapter(new MyAdapter());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String str = new String(responseBody);
                System.out.println("FAILED RESPONSE OF EVENTS :  " + str);
            }
        });
    }

    private void parseEventsData(String serverResponse) throws JSONException {
        JSONObject jsonObject = new JSONObject(serverResponse);
        JSONArray dataArray = jsonObject.getJSONArray("data");
        for(int i=0; i<dataArray.length(); i++) {
            JSONObject eventData = dataArray.getJSONObject(i);
            String event = eventData.getString("event");
            String date = eventData.getString("date");
            String country =  eventData.getString("country");
            String city = eventData.getString("city");
            String imgUrl = eventData.getString("imgurl");
            
            if(imgUrl == null || imgUrl.isEmpty())
                imgUrl="https://ide-global.com/images/logo.png";

            Event eventObject = new Event();
            eventObject.setEvent(event);
            eventObject.setDate(date);
            eventObject.setCity(city);
            eventObject.setCountry(country);
            eventObject.setImageurl(imgUrl);
            eventList.add(eventObject);
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return eventList.size();
        }

        @Override
        public Object getItem(int position) {
            return eventList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.event_list_view_layout, null);
            TextView eventName = (TextView) view.findViewById(R.id.eventName);
            TextView date = (TextView) view.findViewById(R.id.date);
            TextView city = (TextView) view.findViewById(R.id.city);
            TextView country = (TextView) view.findViewById(R.id.country);
            ImageView logoImg = (ImageView) view.findViewById(R.id.imageView2);

//            Picasso.with(getApplicationContext())
//                    .load("https://cms-assets.tutsplus.com/uploads/users/21/posts/19431/featured_image/CodeFeature.jpg")
//                    .into(logoImg);

            //Picasso.with(getApplicationContext()).load("https://ide-global.com/images/logo.png").into(logoImg);
            Picasso.with(getApplicationContext()).load(eventList.get(position).getImageurl()).into(logoImg);
            Event event = eventList.get(position);
            eventName.setText(event.getEvent());
            date.setText(event.getDate());
            city.setText(event.getCity());
            country.setText(event.getCountry());
            return view;
        }
    }
}
