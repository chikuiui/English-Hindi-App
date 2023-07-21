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

public class PhrasesActivity extends AppCompatActivity {
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

        final ArrayList<Word> phrases = new ArrayList<>();
        phrases.add(new Word("Where are you going?", "आप कहां जा रहे हैं?", R.raw.phrase_where_are_you_going));
        phrases.add(new Word("What is your name?", "आपका क्या नाम है?", R.raw.phrase_what_is_your_name));
        phrases.add(new Word("My name is", "मेरा नाम है", R.raw.phrase_my_name_is));
        phrases.add(new Word("How are you feeling?", "तुम कैसा महसूस कर रहे हो?", R.raw.phrase_how_are_you_feeling));
        phrases.add(new Word("I’m feeling good.", "मैं अच्छा महसूस कर रहा हूँ।", R.raw.phrase_im_feeling_good));
        phrases.add(new Word("Are you coming?", "क्या आप आ रहे हैं?", R.raw.phrase_are_you_coming));
        phrases.add(new Word("Yes, I’m coming.", "हाँ, आ रहा हूं।", R.raw.phrase_yes_im_coming));
        phrases.add(new Word("I’m coming.", "मेँ आ रहा हूँ।", R.raw.phrase_im_coming));
        phrases.add(new Word("Let’s go.", "चल दर।", R.raw.phrase_lets_go));
        phrases.add(new Word("Come here.", "यहाँ आओ।", R.raw.phrase_come_here));

        ListView view = findViewById(R.id.word_list);
        WordAdapter items = new WordAdapter(this,phrases,R.color.category_phrases);
        view.setAdapter(items);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word current_word = phrases.get(i);
                // before playing current sound we check if the previous sound is playing then stop. using this below function.
                releaseMediaPlayer();

                int result = Audiom.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    // now we have audio focus now
                    mp = MediaPlayer.create(PhrasesActivity.this,current_word.getAudio_resource_id());
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
