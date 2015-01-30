package com.lkh.android.video;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.lkh.android.video.service.AudioService;

import java.io.File;


public class MainActivity extends FragmentActivity {

    AudioService mAudioService;

    TextView txtFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vd_activity_main);

        Intent intent = new Intent(this, AudioService.class);
        startService(intent);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

        txtFiles = (TextView) findViewById(R.id.txtFile);
        findViewById(R.id.txtStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioService.startPlay();
            }
        });
        findViewById(R.id.txtPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioService.pauseOrStart();
            }
        });
        findViewById(R.id.txtStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioService.stopPlay();
            }
        });
        findViewById(R.id.txtScan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(conn);
                stopService(new Intent(MainActivity.this, AudioService.class));
                finish();
            }
        });

        findViewById(R.id.txtInit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] p = getFilesPath();
                mAudioService.load(p);
            }
        });
        findViewById(R.id.txtPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioService.previous();
            }
        });
        findViewById(R.id.txtNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioService.next();
            }
        });
        findViewById(R.id.txtAddVolume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioService.addVolume();
            }
        });
        findViewById(R.id.txtSubVolume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioService.subVolume();
            }
        });

    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAudioService = (AudioService) ((AudioService.AudioBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAudioService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private String[] getFilesPath(){
        String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/ttpod/song";
        File file = new File(root);
        if (file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            final int len = files.length;
            String[] paths = new String[len];
            for (int i=0; i<len; i++){
                paths[i] = files[i].getAbsolutePath();
            }
            return paths;
        }
        return null;
    }

}
