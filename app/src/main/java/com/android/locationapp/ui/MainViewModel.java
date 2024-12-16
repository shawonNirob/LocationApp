package com.android.locationapp.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.locationapp.data.LocationModel;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<LocationModel> locationLiveData = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
    }

    public LiveData<LocationModel> getLocationLiveData() {
        return locationLiveData;
    }

    public void updateLocation(double latitude, double longitude) {
        LocationModel locationModel = new LocationModel(latitude, longitude);
        locationLiveData.setValue(locationModel);
    }
}
