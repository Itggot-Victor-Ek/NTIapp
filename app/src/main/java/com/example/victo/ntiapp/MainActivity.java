package com.example.victo.ntiapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import java.lang.Runnable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.os.Message;
import java.util.Random;
import java.util.Arrays;
import java.io.IOException;
import android.widget.Switch;

import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{

    private TextView mTextMessage;
    private static final String TAG = "myMessage";
    private TextView snackText;
    private GestureDetectorCompat gestureDetector;

    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        Log.i(TAG, "onCreate");

        this.gestureDetector = new GestureDetectorCompat(this,this);
        gestureDetector.setOnDoubleTapListener(this);

        Button randomizeButton = (Button)findViewById(R.id.randomizeButton);
        dbHandler = new MyDBHandler(this, null, null, 1);
        Switch mjölkSwitch = (Switch)findViewById(R.id.mjölkSwitch);
        Switch nutSwitch = (Switch)findViewById(R.id.nutSwitch);
        Switch glutenSwitch = (Switch)findViewById(R.id.glutenSwitch);

        randomizeButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        try {

                            dbHandler.createDataBase();

                        } catch (IOException ioe) {

                            Log.i(TAG, "Unable to create database!");

                        }

                        Boolean switchStateMjölk = mjölkSwitch.isChecked();
                        Boolean switchStateNut = nutSwitch.isChecked();
                        Boolean switchStateGluten = glutenSwitch.isChecked();

                        Object[] list = dbHandler.getSnacks(switchStateMjölk, switchStateNut, switchStateGluten).toArray(new String[dbHandler.getSnacks(switchStateMjölk, switchStateNut, switchStateGluten).size()]);
                        String[] snacks = Arrays.copyOf(list, list.length, String[].class);

                        Object[] list2 = dbHandler.getDrinks(switchStateMjölk, switchStateNut, switchStateGluten).toArray(new String[dbHandler.getDrinks(switchStateMjölk, switchStateNut, switchStateGluten).size()]);
                        String[] drinks = Arrays.copyOf(list2, list2.length, String[].class);


                        final TextView snackText = (TextView)findViewById(R.id.snackText);
                        final TextView drinkText = (TextView)findViewById(R.id.drinkText);
                        final TextView priceText = (TextView)findViewById(R.id.priceBox);
                        Random rand = new Random();


                        Handler handler = new Handler(){
                          @Override
                            public void handleMessage(Message msg){
                              int randomNum = rand.nextInt(snacks.length);
                              String theSnack = snacks[randomNum];
                              snackText.setText(theSnack);

                              int snackPrice = dbHandler.getPriceSnack(theSnack);

                              int randomNum2 = rand.nextInt(drinks.length);
                              String theDrink = drinks[randomNum2];
                              drinkText.setText(theDrink);

                              int drinkPrice = dbHandler.getPriceDrink(theDrink);

                              int price = drinkPrice + snackPrice;
                              String stringPrice = Integer.toString(price);
                              priceText.setText(stringPrice + "kr");
                          }
                        };

                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                for(int i=0;i<50;i++){
                                    handler.sendEmptyMessage(0);
                                    try {
                                        Thread.sleep(50);
                                    }catch (InterruptedException ie){
                                        Log.i(TAG, "Thread interrupted");
                                    }

                                }
                            }
                        };
                        Thread myThread = new Thread(r);
                        myThread.start();


                    }
                }
        );
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG, "onInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }


}
