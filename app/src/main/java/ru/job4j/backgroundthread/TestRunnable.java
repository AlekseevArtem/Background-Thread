package ru.job4j.backgroundthread;

import android.util.Log;

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
            Log.d(MainActivity.TAG, "startThread: " + count);
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
