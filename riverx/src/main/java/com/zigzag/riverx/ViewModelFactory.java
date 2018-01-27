package com.zigzag.riverx;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.HashMap;

/**
 * Created by salah on 24/1/18.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    @SuppressLint("StaticFieldLeak")
    private static ViewModelFactory INSTANCE;

    public ViewModelFactory() {
    }

    public static ViewModelFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ViewModelFactory();
        }
        return INSTANCE;
    }

    private HashMap<Class<? extends ViewModel>, ViewModel> viewModelHashMap = new HashMap<>();

    public void registerViewModel(ViewModel viewModel){
        if(!viewModelHashMap.containsKey(viewModel.getClass())){
            viewModelHashMap.put(viewModel.getClass(),viewModel);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        for (HashMap.Entry<Class<? extends ViewModel>, ViewModel> entry : viewModelHashMap.entrySet()) {
            Class<? extends ViewModel> key = entry.getKey();
            if (modelClass.isAssignableFrom(key)) {
                return (T) entry.getValue();
            }
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}