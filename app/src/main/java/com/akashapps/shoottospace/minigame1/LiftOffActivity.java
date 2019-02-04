package com.akashapps.shoottospace.minigame1;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class LiftOffActivity extends Activity {
    private LiftOffView liftOffView;
    //public static GLRendererActivity activityManager = null;
    public static Context context;
    public static LiftOffActivity instance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        instance = LiftOffActivity.this;

        super.onCreate(savedInstanceState);
        context = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        liftOffView = new LiftOffView(getApplication());

        setContentView(liftOffView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        liftOffView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        liftOffView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //finish();
    }
}
