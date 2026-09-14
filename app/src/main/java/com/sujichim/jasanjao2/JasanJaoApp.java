package com.sujichim.jasanjao2;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Applies the window insets to every screen, so no activity has to remember to.
 */
public class JasanJaoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new NoOpLifecycleCallbacks() {
            @Override
            public void onActivityStarted(Activity activity) {
                // onStart, not onCreate: the layout is in place by then, and the insets are
                // applied before the first frame is drawn either way.
                SystemBars.applyTo(activity);
            }
        });
    }

    private static class NoOpLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }
}
