package com.outerspace.workmule;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
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

    private LongLastingViewModel model;
    private Context context;
    private String channel_id;
    private NotificationCompat.Builder builder;
    private int notificationId = 927627;

    public TestViewModelFragment() { }     // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        channel_id = getString(R.string.channel_id);
        builder = new NotificationCompat.Builder(context, channel_id);

        View fragment;
        fragment = inflater.inflate(R.layout.fragment_test_view_model, container, false);

        Button btnReset = (Button) fragment.findViewById(R.id.btn_reset);
        Button btnRequest = (Button) fragment.findViewById(R.id.btn_request);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnReset(view);
            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnRequest(view);
            }
        });

        model = ViewModelProviders.of(this).get(LongLastingViewModel.class);

        final Observer<String> messageObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String message) {
                // Toast.makeText(getActivity(), "Message:" +  message, Toast.LENGTH_SHORT).show();
                String notificationTitle = getString(R.string.work_mule_notification_title);
                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(notificationTitle)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat manager = NotificationManagerCompat.from(context);
                manager.notify(notificationId, builder.build());
            }
        };

        model.getNext().observe(this, messageObserver);

        return fragment;
    }

    public void onClickBtnReset(View view) {
        model.reset();
    }

    public void onClickBtnRequest(View view) {
        model.getNext();
    }

}
