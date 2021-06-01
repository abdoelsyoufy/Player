package com.example.player;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.player.model.MusicFiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.player.MainActivity.musicFiles;

public class PlayerActivity extends AppCompatActivity {
     TextView song_name , song_artist , song_duration , song_total_time;
     ImageView cover_art , btn_per , btn_next , back_btn , shuffle_btn ,repeat_btn;
     FloatingActionButton fab_player;
     SeekBar seekBar ;
     static ArrayList<MusicFiles> listSong = new ArrayList<>();
     int position = -1  ;
     Uri uri ;
     static MediaPlayer mediaPlayer;
     Handler handler = new Handler();
     private Thread playThread , perThread, nextThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        inflate();
        getIntete();
        song_name.setText(listSong.get(position).getTitle());
        song_artist.setText(listSong.get(position).getArtist());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser)
                {
                    mediaPlayer.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
                     PlayerActivity.this.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             if(mediaPlayer!=null)
                             {
                                 int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                                 seekBar.setProgress(mCurrentPosition);
                                 song_duration.setText(formattedTime(mCurrentPosition));
                             handler.postDelayed(this::run,1000);
                             }
                         }
                     });


    }

    @Override
    protected void onResume() {
        super.onResume();
        playThreadBtn();
        perThreadBtn();
        nextThreadBtn();
    }

    private void nextThreadBtn() {
        nextThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      nextBtnClick();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnClick() {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position + 1) % listSong.size();
            uri =Uri.parse( listSong.get(position).getPath());
            mediaPlayer = MediaPlayer.create(this,uri);
            song_name.setText(listSong.get(position).getTitle());
            song_artist.setText(listSong.get(position).getArtist());
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.toString());
            byte [] art = retriever.getEmbeddedPicture();
            if(art !=null)
            {
                Glide.with(this)
                        .asBitmap()
                        .load(art)
                        .into(cover_art);
            }
            else
            {
                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.song)
                        .into(cover_art);
            }
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                        handler.postDelayed(this::run,1000);
                    }
                }
            });
            fab_player.setImageResource(R.drawable.ic_baseline_pause);

            mediaPlayer.start();

        }

        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position + 1) % listSong.size();
            uri =Uri.parse( listSong.get(position).getPath());
            mediaPlayer = MediaPlayer.create(this,uri);
            song_name.setText(listSong.get(position).getTitle());
            song_artist.setText(listSong.get(position).getArtist());
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.toString());
            byte [] art = retriever.getEmbeddedPicture();
            if(art !=null)
            {
                Glide.with(this)
                        .asBitmap()
                        .load(art)
                        .into(cover_art);
            }
            else
            {
                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.song)
                        .into(cover_art);
            }
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                        handler.postDelayed(this::run,1000);
                    }
                }
            });
            fab_player.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    private void perThreadBtn() {
        perThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                btn_per.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        perBtnClick();
                    }
                });
            }
        };
        perThread.start();
    }

    private void perBtnClick() {

        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position - 1) <0 ?listSong.size() -1 : position -1 ;
            uri =Uri.parse( listSong.get(position).getPath());
            mediaPlayer = MediaPlayer.create(this,uri);
            song_name.setText(listSong.get(position).getTitle());
            song_artist.setText(listSong.get(position).getArtist());
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.toString());
            byte [] art = retriever.getEmbeddedPicture();
            if(art !=null)
            {
                Glide.with(this)
                        .asBitmap()
                        .load(art)
                        .into(cover_art);
            }
            else
            {
                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.song)
                        .into(cover_art);
            }
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                        handler.postDelayed(this::run,1000);
                    }
                }
            });
            fab_player.setImageResource(R.drawable.ic_baseline_pause);

            mediaPlayer.start();

        }

        else
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position - 1) <0 ?listSong.size() -1 : position -1 ;
            uri =Uri.parse( listSong.get(position).getPath());
            mediaPlayer = MediaPlayer.create(this,uri);
            song_name.setText(listSong.get(position).getTitle());
            song_artist.setText(listSong.get(position).getArtist());
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(uri.toString());
            byte [] art = retriever.getEmbeddedPicture();
            if(art !=null)
            {
                Glide.with(this)
                        .asBitmap()
                        .load(art)
                        .into(cover_art);
            }
            else
            {
                Glide.with(this)
                        .asBitmap()
                        .load(R.drawable.song)
                        .into(cover_art);
            }
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                        handler.postDelayed(this::run,1000);
                    }
                }
            });
            fab_player.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
    }

    private void playThreadBtn() {
        playThread = new Thread()
        {
            @Override
            public void run() {
                super.run();
                fab_player.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPausedBtnClick();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPausedBtnClick() {
        if(mediaPlayer.isPlaying())
        {
            fab_player.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        mediaPlayer.pause();
        seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                        handler.postDelayed(this::run,1000);
                    }
                }
            });

        }

        else
        {
            fab_player.setImageResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null)
                    {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                        handler.postDelayed(this::run,1000);
                    }
                }
            });

        }
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut , totalNew;
        String second = String.valueOf(mCurrentPosition%60);
        String minutes = String.valueOf(mCurrentPosition/60);
        totalOut = minutes + ":"+second;
        totalNew = minutes + ":0"+second;
        if(second.length() == 1)
        {
            return totalNew;
        }
        else
        {
            return totalOut;
        }


    }

    private void getIntete() {
       Intent intent = getIntent();
       position = intent.getIntExtra("position",-1);
       listSong = musicFiles;
       if(listSong!= null)
       {
           fab_player.setImageResource(R.drawable.ic_baseline_pause);
           uri = Uri.parse(listSong.get(position).getPath());
       }
       if(mediaPlayer != null)

       {
           mediaPlayer.stop();
           mediaPlayer.release();
           mediaPlayer = MediaPlayer.create(getApplicationContext() , uri);
           mediaPlayer.start();
       }
       else
       {
           mediaPlayer = MediaPlayer.create(getApplicationContext() , uri);
           mediaPlayer.start();
       }
       seekBar.setMax(mediaPlayer.getDuration()/1000);
       mateData(uri);

    }

    private void inflate() {
        song_name = findViewById(R.id.song_name);
        song_artist = findViewById(R.id.song_artist);
        song_duration = findViewById(R.id.seek_time_duration);
        song_total_time = findViewById(R.id.seek_time_total);
        cover_art = findViewById(R.id.layout_center_iv);
        btn_per = findViewById(R.id.iv_skip_pervice);
        btn_next = findViewById(R.id.iv_skip_next);
        back_btn = findViewById(R.id.layout_top_iv_back);
        shuffle_btn = findViewById(R.id.iv_shuffle);
        repeat_btn = findViewById(R.id.iv_repeat_off);
        fab_player = findViewById(R.id.fab_player);
        seekBar = findViewById(R.id.seek_bar);

    }

    private void mateData(Uri uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int duration = Integer.parseInt(listSong.get(position).getDuration())/1000;
        song_total_time.setText(formattedTime(duration));

        byte [] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if(art !=null)
        {
            Glide.with(this)
                    .asBitmap()
                    .load(art)
                    .into(cover_art);
          //  bitmap = BitmapFactory.decodeByteArray(art,)
        }
        else
        {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.song)
                    .into(cover_art);
        }


    }
}