package com.outerspace.workmule;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestViewModelFragment extends Fragment {

    private Context context;

    private LongLastingViewModel model;
    private String channelId;
    private String notificationTitle;
    private int notificationId;

    public TestViewModelFragment() { }     // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment;
        fragment = inflater.inflate(R.layout.fragment_test_view_model, container, false);

        context = this.getContext();

        channelId = getString(R.string.channel_id);
        notificationTitle = getString(R.string.work_mule_notification_title);
        notificationId = 927627;

        Button btnReset = (Button) fragment.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnReset(view);
            }
        });

        Button btnRequest = (Button) fragment.findViewById(R.id.btn_request);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnRequest(view);
            }
        });

        /**
         *  How does this work? :
         *
         *  The pieces:
         *  LifecycleOwner - interface to denote a class has a lifecycle. typically an Activity or Fragment
         *  LifecycleObserver - will receive lifecycle events from the lifecycleOwner
         *
         *      LifecycleOwner : fragment
         *              ^
         *              |
         *              +---<- observes--- LifecycleObserver : MutableLiveData in the ViewModel.
         *
         *   The model is an object of LongLastingViewModel, a class extending ViewModel, and
         *   therefore a lifecycleObserver.
         *
         *   This practice app uses ViewModelProviders, method of the ViewModelStore.
         *   ViewModelProviders implements a factory pattern. when we call of, we get a
         *   ViewModelProvider for the Fragment or the FragmentActivity that we specify.
         *
         *   a ViewModelProvider will retain ViewModel objects as long as the scope is valid
         *   the get method retrieves the ViewModel or creates a new one. The provider can
         *   hold one or more ViewModels identified by a key.
         */

        model = ViewModelProviders.of(this).get(LongLastingViewModel.class);

        model.getCurrentMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                NotificationCompat.Builder builder;
                builder = new NotificationCompat.Builder(context, channelId);;
                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(notificationTitle)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat manager = NotificationManagerCompat.from(context);
                manager.notify(notificationId, builder.build());
            }
        });

        model.getCurrentProgress().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(@Nullable Float progress) {
                if(progress != null) {
                    int iProgress = Float.valueOf(progress * 100.0f).intValue();

                    NotificationCompat.Builder builder;
                    builder = new NotificationCompat.Builder(context, channelId);;
                    builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle(notificationTitle)
                            .setContentText(model.getCurrentMessage().getValue())
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setProgress(100, iProgress, false);

                    NotificationManagerCompat
                            .from(context)
                            .notify(notificationId, builder.build());
                } else {
                    NotificationManagerCompat
                            .from(context)
                            .cancel(notificationId);
                }
            }
        });

        return fragment;
    }  // onCreate

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onClickBtnReset(View view) {
        model.doResetOperation();
    }

    public void onClickBtnRequest(View view) {
        model.doLongLastingOperation();
    }
}
