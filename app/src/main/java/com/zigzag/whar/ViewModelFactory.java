package com.zigzag.whar;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.zigzag.whar.ui.login.LoginViewModel;

import javax.inject.Inject;

/**
 * Created by salah on 24/1/18.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {
    private LoginViewModel mViewModel;

    @Inject
    public ViewModelFactory(LoginViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) mViewModel;
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}