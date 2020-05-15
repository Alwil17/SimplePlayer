package com.grey.simpleplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grey.simpleplayer.adapter.SongAdapter;
import com.grey.simpleplayer.loader.SongLoader;
import static com.grey.simpleplayer.services.ServiceApplication.*;
import com.grey.simpleplayer.services.ServiceApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import static com.grey.simpleplayer.adapter.SongAdapter.songList;


public class MainActivity extends AppCompatActivity implements ServiceConnection {

    public static TextView titre, artist;
    public static ImageView playpause, image;
    public SongAdapter songAdapter;
    private static final int CODE = 123;
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ServiceApplication.ServiceToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        token = ServiceApplication.bindToService(this, this);

        //Demande de permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE);
            return;
        } else {
            Initialize();
        }

    }

    //Résultat de demande de permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Initialize();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void Initialize() {

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        recyclerView = findViewById(R.id.recycler);
        image = findViewById(R.id.song_image);
        playpause = findViewById(R.id.play_pause);
        artist = findViewById(R.id.artist_nom);
        titre = findViewById(R.id.song_nom);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        songAdapter = new SongAdapter(new SongLoader().getAllSongs(this));
        recyclerView.setAdapter(songAdapter);

        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            if(isPlaying()){
                                pause();
                            }else {
                                play();
                                playpause.setImageResource(R.drawable.ic_play_arrow);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 100);
            }
        });
    }

    //Destruction de l'Activité
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(token != null){
            ServiceApplication.unbindToService(token);
            token = null;
        }
    }

    //Connecté au service
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mRemot = MusicInter.Stub.asInterface(iBinder);
    }

    //Déconnecté du service
    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mRemot = null;
    }
}