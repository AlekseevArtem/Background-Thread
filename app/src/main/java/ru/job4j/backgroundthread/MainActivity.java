package ru.job4j.backgroundthread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TestRunnable runnable;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startThread(View view){
        runnable = new TestRunnable(10);
    }

    public void stopThread(View view){
        runnable.stopThread();
        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
    }
}