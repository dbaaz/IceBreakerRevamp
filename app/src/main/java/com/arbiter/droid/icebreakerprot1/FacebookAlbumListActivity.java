package com.arbiter.droid.icebreakerprot1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.arbiter.droid.icebreakerprot1.Common.getPreference;
import static com.arbiter.droid.icebreakerprot1.Common.setCurrentUser;

public class FacebookAlbumListActivity extends AppCompatActivity {
    @Override
    protected void onStart(){
        super.onStart();
        setCurrentUser(getSharedPreferences("Icebreak",0).getString("saved_name",""));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_album_list);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ListView lv = findViewById(R.id.album_listView);
        final ArrayList<String> albumNames = new ArrayList<>();
        final ArrayList<String> albumIds = new ArrayList<>();
        final ArrayAdapter adap = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,albumNames);
        lv.setAdapter(adap);
        GraphRequest request1 = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        if(response!=null) {
                            final JSONObject jsonObject = response.getJSONObject();

                            try {
                                final JSONObject json_album = jsonObject.getJSONObject("albums");
                                final JSONArray jarray_album = json_album.getJSONArray("data");
                                /*final JSONObject j_data = jarray_album.getJSONObject(0);
                                final String id = j_data.getString("id");
                                Log.v("myapp",json_album.toString());
                                Log.v("myapp",jarray_album.toString());
                                Log.v("myapp",j_data.toString());
                                Log.v("myapp",id);*/
                                for(int i=0;i<jarray_album.length();i++)
                                {
                                    JSONObject j_data = jarray_album.getJSONObject(i);
                                    albumNames.add(j_data.getString("name"));
                                    albumIds.add(j_data.getString("id"));
                                }
                                adap.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
        Bundle parameters1 = new Bundle();
        parameters1.putString("fields","albums");
        //location,picture,education,hometown,interested_in,political,education,locale,age_range,is_verified,photo,relationship_status
        request1.setParameters(parameters1);
        request1.executeAsync();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),ImageListActivity.class);
                i.putExtra("album_id",albumIds.get(position));
                startActivity(i);
            }
        });
    }

}
