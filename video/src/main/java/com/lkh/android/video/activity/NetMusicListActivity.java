package com.lkh.android.video.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.lkh.android.video.MainActivity;
import com.lkh.android.video.R;
import com.lkh.android.video.adapter.NetMusicListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by kk on 2015/2/1.
 */
public class NetMusicListActivity extends FragmentActivity {
    public static String IP = "58.247.140.146";
    private ListView mListView;
    private NetMusicListAdapter mAdapter;
    private String[] mPaths;
    private EditText mEdtIp;
    private Button mBtnSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vd_activity_net_music_list);
        IP = getSharedPreferences("config", MODE_PRIVATE).getString("ip", IP);
        initView();
        getMusicPath();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NetMusicListActivity.this, MainActivity.class);
                intent.putExtra("paths", mPaths);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        mEdtIp = (EditText) findViewById(R.id.edtIp);
        mBtnSure = (Button) findViewById(R.id.btnSure);

        mEdtIp.setText(IP);

        mBtnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IP = mEdtIp.getText().toString();
                getSharedPreferences("config", MODE_PRIVATE).edit().putString("ip", IP).commit();
                getMusicPath();
            }
        });

    }

    private void getMusicPath(){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest("http://"+IP+":8080/strut/FileListServlet",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        final int len = response.length();
                        String[] path = new String[len];
                        String[] filenames = new String[len];
                        for (int i=0; i<len; i++){
                            try {
                                Log.i("filename=", response.getString(i));
                                filenames[i] = response.getString(i);
                                path[i] = getURL_HEAD() + URLEncoder.encode(response.getString(i), "UTF-8");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(NetMusicListActivity.this, "加载成功", Toast.LENGTH_LONG).show();
                        mPaths = path;
                        mAdapter = new NetMusicListAdapter(NetMusicListActivity.this, filenames);
                        mListView.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NetMusicListActivity.this, "加载失败", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    private String getURL_HEAD(){
        return "http://"+IP+":8080/strut/DownLoadFileServlet?filename=";
    }
}
