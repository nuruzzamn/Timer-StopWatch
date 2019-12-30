package com.example.timerstopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //FOR STOP WATCH
    TextView textViewForCountDawn;
    long startTime=0L,timeMs=0L,timeswapBuff=0L,updatetime=0L;

    Handler handler=new Handler();

    //THIS RUNABLE CODE FOR STOP WATCH RUNABLE FUNCTION
    Runnable runable=new Runnable() {
        @Override
        public void run() {
            TextView stopWatchTextView= (TextView) findViewById(R.id.textViewForStopWatch);

            timeMs = SystemClock.uptimeMillis() - startTime;

            updatetime = timeswapBuff + timeMs;

            int  Seconds = (int) (updatetime / 1000);

            int  Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            int MilliSeconds = (int) (updatetime % 1000);

            stopWatchTextView.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%02d", MilliSeconds));

            handler.postDelayed(this,0);
        }
    };

    //Declare a variable to hold count down timer's paused status
    private boolean isPaused = false;
    //Declare a variable to hold count down timer's paused status
    private boolean isCanceled = false;

    //Declare a variable to hold CountDownTimer remaining time
    private long timeRemaining = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get reference of the XML layout's widgets
        final TextView textViewForCountDown = (TextView) findViewById(R.id.text_view_countdown);
        final Button btnStart = (Button) findViewById(R.id.btn_start);
        final Button btnPause = (Button) findViewById(R.id.btn_pause);
        final Button btnResume = (Button) findViewById(R.id.btn_resume);
        final Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        final Button btnSet = (Button) findViewById(R.id.button_set);
        final  EditText inputEditText=(EditText)findViewById(R.id.edit_text_input);

        //Initially disabled the start, pause, resume and cancel button
        btnStart.setEnabled(false);
        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnCancel.setEnabled(false);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textViewForCountDown.setText(inputEditText.getText().toString());
                inputEditText.setText("0");

                //Enabled the start and cancel other all
                btnStart.setEnabled(true);
                btnSet.setEnabled(true);
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnCancel.setEnabled(false);
            }
        });

        //Click Listener for start button
        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                isPaused = false;
                isCanceled = false;

                //Disable the start,set and pause button
                btnStart.setEnabled(false);
                btnResume.setEnabled(false);
                //Enabled the pause and cancel button
                btnPause.setEnabled(true);
                btnCancel.setEnabled(true);
                btnSet.setEnabled(true);

                CountDownTimer timer;
                // long millisInFuture = 30000; //30 seconds
                long millisInFuture;
                long countDownInterval = 1000; //1 second

                Long inputValue=Long.parseLong(String.valueOf(textViewForCountDown.getText()));

                millisInFuture=inputValue*1000;

                //Initialize a new CountDownTimer instance
                timer = new CountDownTimer(millisInFuture,countDownInterval){
                    public void onTick(long millisUntilFinished){
                        //do something in every tick
                        if(isPaused || isCanceled)
                        {
                            //If the user request to cancel or paused the
                            //CountDownTimer we will cancel the current instance
                            cancel();
                        }
                        else {
                            //Display the remaining seconds to app interface
                            //1 second = 1000 milliseconds
                            textViewForCountDown.setText("" + millisUntilFinished / 1000);
                            //Put count down timer remaining time in a variable
                            timeRemaining = millisUntilFinished;
                        }
                    }
                    public void onFinish(){
                        //Do something when count down finished
                        textViewForCountDown.setText("0");

                        //Enable the start button
                        btnStart.setEnabled(true);
                        //Disable the pause, resume and cancel button
                        btnPause.setEnabled(false);
                        btnResume.setEnabled(false);
                        btnCancel.setEnabled(false);
                    }
                }.start();
            }
        });

        //Set a Click Listener for pause button
        btnPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //When user request to pause the CountDownTimer
                isPaused = true;

                //Enable the resume and cancel button
                btnResume.setEnabled(true);
                btnCancel.setEnabled(true);
                //Disable the start,set and pause button
                btnStart.setEnabled(false);
                btnPause.setEnabled(false);
                btnSet.setEnabled(false);
            }
        });

        //Set a Click Listener for resume button
        btnResume.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Disable the start,set and resume button
                btnStart.setEnabled(false);
                btnResume.setEnabled(false);
                btnSet.setEnabled(false);
                //Enable the pause and cancel button
                btnPause.setEnabled(true);
                btnCancel.setEnabled(true);

                //Specify the current state is not paused and canceled.
                isPaused = false;
                isCanceled = false;

                //Initialize a new CountDownTimer instance
                long millisInFuture = timeRemaining;
                long countDownInterval = 1000;
                new CountDownTimer(millisInFuture, countDownInterval){
                    public void onTick(long millisUntilFinished){
                        //Do something in every tick
                        if(isPaused || isCanceled)
                        {
                            //If user requested to pause or cancel the count down timer
                            cancel();
                        }
                        else {
                            textViewForCountDown.setText("" + millisUntilFinished / 1000);
                            //Put count down timer remaining time in a variable
                            timeRemaining = millisUntilFinished;
                        }
                    }
                    public void onFinish(){
                        //Do something when count down finished
                        textViewForCountDown.setText("0");
                        //Disable the pause, resume and cancel button
                        btnPause.setEnabled(false);
                        btnResume.setEnabled(false);
                        btnCancel.setEnabled(false);
                        //Enable the start button
                        btnStart.setEnabled(true);
                    }
                }.start();

                //Set a Click Listener for cancel/stop button
                btnCancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //When user request to cancel the CountDownTimer
                        isCanceled = true;

                        //Disable the cancel, pause and resume button
                        btnPause.setEnabled(false);
                        btnResume.setEnabled(false);
                        btnCancel.setEnabled(false);
                        //Enable the start button
                        btnStart.setEnabled(true);
                        btnSet.setEnabled(true);

                        //Notify the user that CountDownTimer is canceled/stopped
                        textViewForCountDown.setText("0");
                    }
                });
            }
        });

        //Set a Click Listener for cancel/stop button
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //When user request to cancel the CountDownTimer
                isCanceled = true;

                //Disable the cancel, pause and resume button
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnCancel.setEnabled(false);
                //Enable the start button
                btnStart.setEnabled(true);
                btnSet.setEnabled(true);

                //Notify the user that CountDownTimer is canceled/stopped
                textViewForCountDown.setText("0");
                inputEditText.setText("0");
            }
        });

        //END OF COUNTDOWN TIMER

        //START FOR STOP WATCH CODE
        final Button start= (Button) findViewById(R.id.startBtn);
        final Button pause= (Button) findViewById(R.id.puseBtn);
        final TextView stopWatchTextView= (TextView) findViewById(R.id.textViewForStopWatch);
        final Button reset= (Button) findViewById(R.id.resetBtn);


        reset.setEnabled(false);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isPaused = false;
                isCanceled = false;

                //Disable the start and pause button
                start.setEnabled(false);
                //Enabled the pause and cancel button
                pause.setEnabled(true);
                reset.setEnabled(false);

                startTime= SystemClock.uptimeMillis();

                handler.postDelayed(runable,0);

                 reset.setEnabled(!reset.isEnabled());
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeswapBuff += timeMs;
                handler.removeCallbacks(runable);
                reset.setEnabled(!reset.isEnabled());

                pause.setEnabled(false);
                start.setEnabled(true);
                reset.setEnabled(true);

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView stopWatchTextView= (TextView) findViewById(R.id.textViewForStopWatch);
                startTime=0L;
                timeMs = 0L;
                timeswapBuff=0L;
                updatetime =0L;

                int  Seconds =0;

                int  Minutes =0;

                int MilliSeconds =0;

                stopWatchTextView.setText("00:00:00");

                 handler.removeCallbacks(runable);

            }
        });

        //END OF STOP WATCH CODE
    }

}