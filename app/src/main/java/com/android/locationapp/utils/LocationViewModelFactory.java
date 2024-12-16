package com.android.locationapp.utils;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.android.locationapp.ui.MainViewModel;

public class LocationViewModelFactory implements ViewModelProvider.Factory {
    private Application application;

    public LocationViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
