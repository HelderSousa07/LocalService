package com.example.localservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyService extends Service {
    private final IBinder mBinder = new MyBinder();
    private List<String> resultList = new ArrayList<String>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addResultValues(intent);
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        addResultValues(intent);
        return mBinder;
    }

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    public List<String> getMessageList() {
        return resultList;
    }

    private void addResultValues(Intent intent) {

        LinkedList<String> messages = new LinkedList<>();
        ArrayList<String> deserializableMesssages = intent.getStringArrayListExtra("messages");

        if (deserializableMesssages!=null){
            messages.addAll(deserializableMesssages);
            resultList = removeDup(messages);
        }

    }

    private static LinkedList<String> removeDup(LinkedList<String> linkedList) {

        LinkedList<String> newList = new LinkedList<>();

        for (String message : linkedList){
            if (!newList.contains(message)){
                newList.add(message);
            }
        }
        return newList;
    }

}
