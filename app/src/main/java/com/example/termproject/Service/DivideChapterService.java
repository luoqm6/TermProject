package com.example.termproject.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

public class DivideChapterService extends Service {
    private static Context context ;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        context = this;
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        context = this;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public static Context getServiceContext(){
        Log.i("context",context.toString());
        return context;
    }
}
