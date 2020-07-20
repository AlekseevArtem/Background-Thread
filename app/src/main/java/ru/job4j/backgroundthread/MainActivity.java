package ru.job4j.backgroundthread;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TestRunnable mStartTest;
    private ImageRunnable mLoadImage;
    private TextView mText;
    private ImageView mImage;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = findViewById(R.id.text);
        mImage = findViewById(R.id.image);
    }

    public void startThread(View view){
        mStartTest = new TestRunnable(10);
    }

    public void stopThread(View view){
        mStartTest.stopThread();
        Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();
    }

    public void loadImage(View view){
        mLoadImage = new ImageRunnable(this,
                "https://2ch.hk/b/arch/2020-05-03/src/219280082/15884658224810.png");
    }

    class TestRunnable implements Runnable {
        private volatile boolean stopFlag = false;
        Thread t;
        private int times;

        public TestRunnable(int times) {
            this.times = times;
            t = new Thread(this);
            t.start();
        }

        @Override
        public void run() {
            int count = 0;
            while (count != times && !stopFlag) {
                String percent = count * 10 + "%";
                runOnUiThread(() -> mText.setText(percent));
                Log.d(TAG, "startThread: " + count);
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        synchronized void stopThread() {
            stopFlag = true;
        }
    }

    static class ImageRunnable implements Runnable {
        private WeakReference<MainActivity> mActivityWeakReference;
        Thread t;
        private String url;

        public ImageRunnable(MainActivity activity, String url) {
            this.url = url;
            this.mActivityWeakReference = new WeakReference<>(activity);
            this.t = new Thread(this);
            this.t.start();
        }

        @Override
        public void run() {
            MainActivity activity = mActivityWeakReference.get();
            if(activity == null || activity.isFinishing()) {
                return;
            }
            Bitmap bitmap = loadImageFromNetwork(url);
            activity.mImage.post(() -> activity.mImage.setImageBitmap(bitmap));
        }

        private Bitmap loadImageFromNetwork(String url) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory
                        .decodeStream((InputStream) new URL(url)
                                .getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }
}