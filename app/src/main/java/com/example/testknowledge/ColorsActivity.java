package com.example.testknowledge;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {
    private MediaPlayer mp;
    AudioManager Audiom;

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
           releaseMediaPlayer();
        }
    };

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if(i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                mp.pause();
                mp.seekTo(0);
            }else if(i == AudioManager.AUDIOFOCUS_GAIN){
                mp.start();
            }else if(i == AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            }
        }
    };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        Audiom = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> colors = new ArrayList<>();
        colors.add(new Word("red", "लाल", R.raw.color_red, R.drawable.color_red));
        colors.add(new Word("green", "हरा", R.raw.color_green, R.drawable.color_green));
        colors.add(new Word("brown", "भूरा", R.raw.color_brown, R.drawable.color_brown));
        colors.add(new Word("gray", "धुमैला", R.raw.color_gray, R.drawable.color_gray));
        colors.add(new Word("black", "काला", R.raw.color_black, R.drawable.color_black));
        colors.add(new Word("white", "सफेद", R.raw.color_white, R.drawable.color_white));
        colors.add(new Word("dusty yellow", "पीला", R.raw.color_dusty_yellow, R.drawable.color_dusty_yellow));
        colors.add(new Word("mustard yellow", "सरसों-पीला", R.raw.color_mustard_yellow, R.drawable.color_mustard_yellow));

        ListView view = findViewById(R.id.word_list);
        WordAdapter items = new WordAdapter(this,colors,R.color.category_colors);
        view.setAdapter(items);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word current_word = colors.get(i);
                // before playing current sound we check if the previous sound is playing then stop. using this below function.
                releaseMediaPlayer();

                int result = Audiom.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    // now we have audio focus now
                    mp = MediaPlayer.create(ColorsActivity.this,current_word.getAudio_resource_id());
                    mp.start();
                    mp.setOnCompletionListener(completionListener);
                }
            }
        });



    }

    private void releaseMediaPlayer(){
        if(mp != null){
            mp.release();
            mp = null;
            Audiom.abandonAudioFocus(afChangeListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
