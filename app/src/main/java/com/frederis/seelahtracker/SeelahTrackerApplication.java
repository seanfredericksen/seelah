package com.frederis.seelahtracker;

import android.app.Application;

import com.frederis.seelahtracker.module.ApplicationModule;

import dagger.ObjectGraph;

public class SeelahTrackerApplication extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        mObjectGraph = ObjectGraph.create(new ApplicationModule());
    }

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

}
