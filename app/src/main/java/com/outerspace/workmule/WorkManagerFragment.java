package com.outerspace.workmule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkManagerFragment extends Fragment {

    public WorkManagerFragment() { } // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewResult = inflater
                .inflate(R.layout.fragment_work, container, false);

        Button btnRequest = container.findViewById(R.id.btn_request);

        return viewResult;
    }
}
