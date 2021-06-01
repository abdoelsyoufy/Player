package com.example.player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.player.fragments.Album;
import com.example.player.fragments.Songs;
import com.example.player.model.MusicFiles;
import com.google.android.material.tabs.TabLayout;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_PERMISSION = 1;
    String permissions [];
    public static   ArrayList<MusicFiles> musicFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        inflate();



    }

    private void permission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
           permissions  = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permissions,REQ_CODE_PERMISSION);
        }
        else
        {
            musicFiles = getAllAudio(this);
        }
    }

    private void inflate() {
        ViewPager viewPager = findViewById(R.id.main_viewpager);
        TabLayout tabLayout = findViewById(R.id.main_tablayout);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Songs() , "Songs");
        adapter.addFragment(new Album() , "Album");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_CODE_PERMISSION )
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "can be access", Toast.LENGTH_SHORT).show();
                musicFiles = getAllAudio(this);
            }
            else
            {
                ActivityCompat.requestPermissions(this,permissions,REQ_CODE_PERMISSION);

            }

        }

    }

    class  ViewPagerAdapter extends FragmentPagerAdapter
    {
           List<Fragment> fragments;
           List<String> titles;
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return  titles.get(position);
        }

        public  void addFragment(Fragment fragment , String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }
    }

    private  ArrayList<MusicFiles> getAllAudio(Context context)
    {
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();

        Uri  uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String [] projection =
                {
                  MediaStore.Audio.Media.TITLE,
                  MediaStore.Audio.Media.ALBUM,
                  MediaStore.Audio.Media.ARTIST,
                  MediaStore.Audio.Media.DURATION,
                  MediaStore.Audio.Media.DATA, // for path

                };

        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);

        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                String title = cursor.getString(0);
                String album = cursor.getString(1);
                String artist = cursor.getString(2);
                String duration = cursor.getString(3);
                String data = cursor.getString(4);

                MusicFiles musicFiles ;
                musicFiles = new MusicFiles(data,title,duration,album,artist);
                tempAudioList.add(musicFiles);
                Log.w("EEE",data);

            }
            cursor.close();
        }
        return  tempAudioList;

    }
}