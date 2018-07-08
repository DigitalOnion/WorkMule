package com.outerspace.workmule;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class LongLastingViewModel extends ViewModel {

    private MutableLiveData<String> currentMessage;
    private MutableLiveData<Float> currentProgress;

    public LongLastingViewModel() {
        currentMessage = new MutableLiveData<>();
        currentProgress = new MutableLiveData<>();
    }

    public MutableLiveData<String> getCurrentMessage() {
        return currentMessage;
    }

    public MutableLiveData<Float> getCurrentProgress() {
        return currentProgress;
    }

    public void doLongLastingOperation() {
        String message = MessageStore.getInstance().getNext();
        currentMessage.setValue(message);

        MyLongLastingTask task = new MyLongLastingTask();
        task.execute(2000, 10);
    }

    public void doResetOperation() {
        MessageStore.getInstance().reset();
    }

    private class MyLongLastingTask extends AsyncTask<Integer, Float, Float> {

        @Override
        protected Float doInBackground(Integer... waitParams) {
            if(waitParams.length == 2) {
                Integer milliSeconds = waitParams[0];
                Integer nPeriods = waitParams[1];
                Float period = (float) milliSeconds / (float) nPeriods;
                Float elapsed = 0.0f;

                while(elapsed.intValue() < milliSeconds) {
                    try {
                        Thread.sleep(period.longValue());
                        elapsed += period;
                    } catch (InterruptedException e) {
                        elapsed = (float) milliSeconds;
                    }
                    // currentProgress.setValue(elapsed);
                    publishProgress(elapsed / (float) milliSeconds);
                }
            }
            return 1.0f;
        }

        @Override
        protected void onProgressUpdate(Float... progressParams) {
            if(progressParams.length == 1) {
                currentProgress.setValue(progressParams[0]);
            }
        }

        @Override
        protected void onPostExecute(Float finalProgress) {
            new CountDownTimer(100, 100) {
                @Override public void onTick(long l) { }
                @Override
                public void onFinish() {
                    currentProgress.setValue(null);
                }
            }.start();
        }
    }

}
