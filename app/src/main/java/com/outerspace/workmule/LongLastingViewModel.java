package com.outerspace.workmule;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class LongLastingViewModel extends ViewModel {

    private MutableLiveData<String> currentMessage;

    public LongLastingViewModel() {
        this.currentMessage = new MutableLiveData<>();
    }

    public MutableLiveData<String> getNext() {
        String message = MessageStore.getInstance().getNext();
        currentMessage.setValue(message);
        return  currentMessage;
    }

    public void reset() {
        MessageStore.getInstance().reset();
    }
}
