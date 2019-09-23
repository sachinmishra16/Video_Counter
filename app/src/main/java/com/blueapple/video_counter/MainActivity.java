package com.blueapple.video_counter;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blueapple.video_counter.Model.Video_Count;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    ProgressBar progressBar;

    DatabaseReference databaseReference, reference;

    TextView textView;
    SeekBar seekBar;
    MediaPlayer mp;
    int stopPosition;
    int running_time;
    int time, check_time;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int complete_Video_time;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoViewid);

        mediaController = new MediaController(this);


/*
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
*/

        databaseReference = FirebaseDatabase.getInstance().getReference("video_count");



        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Video_Count count=dataSnapshot.getValue(Video_Count.class);
          //      Toast.makeText(MainActivity.this, ""+count, Toast.LENGTH_SHORT).show();

                if (count!=null)
                {


                    if (count.getTime_running() != null) {

                        check_time = Integer.parseInt(count.getTime_running());

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("database_error", databaseError.getMessage());
            }
        };

        databaseReference.addValueEventListener(eventListener);


        if (savedInstanceState != null) {
            stopPosition = savedInstanceState.getInt("video_position");
        }

        textView = findViewById(R.id.textViewid);


        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.strike));


            final Handler handler = new Handler();
            // Define the code block to be executed


            Runnable runnableCode = new Runnable() {
                @Override
                public void run() {

                    Log.d("Handlers", "Called on main thread" + running_time);

                    if (videoView.isPlaying()) {
                        Video_Count count = new Video_Count();

                        count.setTime_running(String.valueOf(check_time+8));


                        databaseReference.setValue(count);
                    }


                    handler.postDelayed(this,
                            10000);

                }
            };
// Start the initial runnable task by posting through the handler
            handler.post(runnableCode);





        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next button clicked

                Toast.makeText(MainActivity.this, "next button", Toast.LENGTH_SHORT).show();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //previous button clicked

                Toast.makeText(MainActivity.this, "previous button", Toast.LENGTH_SHORT).show();
            }
        });

        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);



        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(final MediaPlayer mp) {

                final int duration = videoView.getDuration();


                int topContainerId = getResources().getIdentifier("mediacontroller_progress", "id", "android");

                SeekBar seekbar = mediaController.findViewById(topContainerId);

/*
                seekbar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        Log.d("event", String.valueOf(running_time));

//                        editor.putInt("time_running", running_time);
//
//                        editor.commit();

                        check_time = sharedPreferences.getInt("time_running", 0);

                        if (videoView.isPlaying()) {


                            if (((videoView.getDuration() / 1000) + 10) <= check_time) {

                                videoView.stopPlayback();
                                textView.setText("time finished");

                                //  Toast.makeText(MainActivity.this, "time finished", Toast.LENGTH_SHORT).show();

                            } else {

                                running_time = (check_time + 1);

                                editor.putInt("time_running", running_time);

                                editor.commit();


                                Log.d("time_saved", String.valueOf(check_time));


                                Log.d("running_time", String.valueOf(running_time));

                                textView.setText(time + "");

                            }
                        }


                        return false;
                    }
                });*/

                Log.d("duration", String.valueOf(duration));
                Log.d("videoview_position", String.valueOf(videoView.getCurrentPosition()));


                final Thread thread = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {

                                do {
                                    textView.post(new Runnable() {
                                        public void run() {

                                            time = (videoView.getCurrentPosition()) / 1000;
                                            textView.setText(time + "");
//
//                                            check_time = sharedPreferences.getInt("time_running", 0);
//

                                        //    check_time = sharedPreferences.getInt("time_running", 0);

                                            if (videoView.isPlaying())
                                            {


                                                if (((videoView.getDuration() / 1000) + 10) <= check_time) {

                                                    videoView.stopPlayback();
                                                    textView.setText("time finished");

                                                    //  Toast.makeText(MainActivity.this, "time finished", Toast.LENGTH_SHORT).show();

                                                }
                                                else
                                                    {


                                                    running_time = (running_time + 1);
                                                    Log.d("running_time ", String.valueOf(running_time));


//                                                    editor.putInt("time_running", running_time);
//
//                                                    editor.commit();
//
//                                                    int delay = 10000; // delay for 0 sec.
//                                                    int period = 10000; // repeat every 10 sec.
//
//
//                                                    try {
//
//
//                                                        Log.d("time_saved", String.valueOf(check_time));
//
//
//                                                        Log.d("running_time", String.valueOf(running_time));
//
//                                                        textView.setText(time + "");
//
//                                                    } catch (Exception e) {
//                                                        Log.d("exception ", e.getMessage());
//
                                                }
                                            }

                                            else
                                            {
                                               // Toast.makeText(MainActivity.this, "stop", Toast.LENGTH_SHORT).show();
                                            }

                                        }


/*
                                            complete_Video_time=complete_Video_time+1;

                                            Log.d("video_time", String.valueOf(complete_Video_time));*/


//                                            Log.d("time_saved", String.valueOf(check_time));
//
//
//                                            Log.d("running_time", String.valueOf(running_time));


//                                            running_time = running_time + 1;

//                                            if (!videoView.isPlaying()) {
//                                                Toast.makeText(MainActivity.this, "stop", Toast.LENGTH_SHORT).show();
//
//
//                                                editor.putInt("time_running", running_time);
//
//                                                //  Log.d("runningtime", String.valueOf(running_time));
//
//                                                editor.commit();
//
//                                            }



                                    });
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                                while (videoView.getCurrentPosition() < duration);
                            }

                        });

                thread.start();

            }

        });

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        stopPosition = videoView.getCurrentPosition();
        videoView.pause();
        outState.putInt("video_position", stopPosition);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("Tag", "onResume");
        videoView.seekTo(stopPosition);
       // videoView.start(); //Or use videoView.resume() if it doesn't work.
    }


}
