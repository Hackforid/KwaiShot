package com.smilehacker.kwaishot.common.mvvm;

import android.arch.lifecycle.MutableLiveData;

/**
 * Created by quan.zhou on 2018/6/22.
 */
public class DiffMutableLiveData<T> extends MutableLiveData<T> {

    @Override
    public void postValue(T value) {
        if (value != getValue()) {
            super.postValue(value);
        }
    }

    @Override
    public void setValue(T value) {
        if (value != getValue()) {
            super.setValue(value);
        }
    }
}
