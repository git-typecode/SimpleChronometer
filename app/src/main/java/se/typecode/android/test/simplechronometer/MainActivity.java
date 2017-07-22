package se.typecode.android.test.simplechronometer;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity {

    public Chronometer chronometer;
    public Button startButton;
    public Button stopButton;
    public Button resetButton;
    public Long stopTime;
    public Long startTime;
    public boolean chronometerStarted;
    public boolean resetButtonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        chronometer = (Chronometer)findViewById(R.id.chronometer);
        startButton = (Button)findViewById(R.id.startbutton);
        stopButton = (Button)findViewById(R.id.stopbutton);
        resetButton = (Button)findViewById(R.id.resetbutton);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            startTime = savedInstanceState.getLong("startTime", 0L);
            stopTime = savedInstanceState.getLong("stopTime", 0L);
            chronometerStarted = savedInstanceState.getBoolean("chronometerStarted", false);

            if(chronometerStarted) {
                chronometer.setBase(startTime + (SystemClock.elapsedRealtime() - stopTime));
                chronometer.start();
            }
            else if (stopTime == 0L || startTime == 0L) {
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
            else {
                chronometer.setBase(startTime + (SystemClock.elapsedRealtime() - stopTime));
            }
        }
        else {
            // Probably initialize members with default values for a new instance
            startTime = 0L;
            stopTime = 0L;
            chronometerStarted = false;
            resetButtonPressed = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state

        savedInstanceState.putLong("startTime", startTime);
        savedInstanceState.putLong("stopTime", stopTime);
        savedInstanceState.putBoolean("chronometerStarted", chronometerStarted);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        if(chronometerStarted) {
            stopTime = SystemClock.elapsedRealtime();
        }
    }

    @Override
    protected void onStop() {
        // call the superclass method first
        super.onStop();
    }

    public void start(View v) {

        if(!chronometerStarted) {
            if (stopTime == 0L) {
                chronometer.setBase(SystemClock.elapsedRealtime());

            } else {
                // SystemClock.elapsedRealtime() - stopTime - Gives how long the chronometer has been stopped.
                chronometer.setBase(startTime + (SystemClock.elapsedRealtime() - stopTime));
            }

            chronometer.start();
            startTime = chronometer.getBase();
            chronometerStarted = true;
            resetButtonPressed = false;
        }
    }

    public void stop(View v) {
        chronometer.stop();

        if(chronometerStarted && !resetButtonPressed) {
            stopTime = SystemClock.elapsedRealtime();
        }
        chronometerStarted = false;
        resetButtonPressed = false;
    }

    public void reset(View v) {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        stopTime = 0L;
        startTime = 0L;
        chronometerStarted = false;
        resetButtonPressed = true;
    }
}