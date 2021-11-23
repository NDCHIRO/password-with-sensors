package com.example.assignment2_sensor_and_snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    ConstraintLayout root;
    TextView password;
    Button done;
    ProgressBar progress;
    SensorManager senorManger;
    Sensor accelerometerSensor;
    float currentX,currentY,currentZ;
    float lastX,lastY,lastZ;
    float diffX,diffY,diffZ;
    boolean firstTime=true;
    int countShakes=0;
    String pass="";
    int delay=0;
    boolean secondPassed=false;
    int progressCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password=findViewById(R.id.textViewPassword);
        progress=findViewById(R.id.progressBar);
        progress.setMax(10);
        root=findViewById(R.id.layout1);
        senorManger=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        activateAccSensor();
        done=findViewById(R.id.buttonDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activatesnackbar();
                resetEveryThing();
            }
        });

    }




    public void activateAccSensor()
    {
        accelerometerSensor=senorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currentX = sensorEvent.values[0];   //x direction
        currentY = sensorEvent.values[1];   //y direction
        currentZ = sensorEvent.values[2];   //z direction

        if(firstTime==false && currentX==lastX && currentY==lastY && currentZ==lastZ && secondPassed )
        {
            secondPassed=false;
            delay=0;
            if(countShakes!=0) {
                pass += countShakes + "";
                if (pass.length() < 15 && pass.length() > 10) {
                    Toast.makeText(getApplicationContext(), "maximum size of password is 10", Toast.LENGTH_SHORT).show();
                    //pass="";
                }
                else if(pass.length()<=10) {
                    password.setText(pass);
                    countShakes = 0;
                    setProgress();
                }
                else
                {}
            }
        }
        if(firstTime==false)
        {
            diffX=(float)Math.abs(currentX-lastX);
            diffY=(float)Math.abs(currentY-lastY);
            diffZ=(float) Math.abs(currentZ-lastZ);
            if(diffX>(accelerometerSensor.getMaximumRange()/3) || diffY>(accelerometerSensor.getMaximumRange()/3)
                    || diffZ>(accelerometerSensor.getMaximumRange()/3))
            {
                countShakes++;
                Log.d("sensor","========= countShakes = "+ countShakes + "\n");
            }
        }

        lastX=currentX;
        lastY=currentY;
        lastZ=currentZ;
        delay++;
        if((delay>SensorManager.SENSOR_DELAY_NORMAL*10))  // SENSOR_DELAY_NORMAL 200,000 microseconds(200 milliseconds)
            secondPassed=true;                            // so that means 2 seconds passed

        firstTime=false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //to register the accelerometer Sensor
    @Override
    protected void onStart() {
        super.onStart();
        senorManger.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    //to unregister the accelerometer Sensor
    @Override
    protected void onStop() {
        super.onStop();
        senorManger.unregisterListener(this);
    }


    public void activatesnackbar(){
        Snackbar snackbar = Snackbar.make(root, "TEST SNACK BAR", Snackbar.LENGTH_LONG);
        snackbar.setText("your password was added successfully!");
        snackbar.show();
    }

    public void setProgress()
    {
        progress.setProgress(++progressCount);
    }

    public void resetEveryThing()
    {
        password.setText("");
        progress.setProgress(0);
        delay=0;
        secondPassed=false;
        countShakes=0;
        firstTime=true;
        pass="";
    }
}