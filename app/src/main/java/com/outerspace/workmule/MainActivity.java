package com.outerspace.workmule;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Fragment[] pageFragments = {
            new IntroFragment(),
            new ViewModelFragment(),
            new WorkManagerFragment(),
    };

    private IndexHandler index = new IndexHandler(pageFragments.length - 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        presentFragment(index.next());
    }

    private void createNotificationChannel() {
        String channel_name = getString(R.string.channel_name);
        String channel_description = getString(R.string.channel_description);
        String channel_id = getString(R.string.channel_id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(
                    channel_id,
                    channel_name,
                    importance);
            channel.setDescription(channel_description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void onClickNextFragment(View view) {
        presentFragment(index.next());
    }

    public void onClickPreviousFragment(View view) {
        presentFragment(index.prev());
    }

    // NOTE: I had a ViewPager working, but it does not stop the fragments
    // For the practice I need the fragments to pause and stop.
    //
    private void presentFragment(int index) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame, pageFragments[index]);
        transaction.commit();
    }

    private class IndexHandler {
        int max;
        int idx;

        IndexHandler(int max) {
            this.max = max;
            idx = max;
        }

        int next() {
            if( ++idx > max ) {
                idx = 0;
            }
            return idx;
        }

        int prev() {
            if( --idx < 0 ) {
                idx = max;
            }
            return idx;
        }
    }
}
