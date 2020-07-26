package com.example.localservice;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity implements ServiceConnection {
    private MyService myService;
    private List<String> messages = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private List<String> messagesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messagesList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, messagesList);
        setuplist();
        setListAdapter(adapter);
    }

    private void setuplist() {
        messages.add("Ola Helder, tudo bem?");
        messages.add("Ola IA, tudo bem e com vc?");
        messages.add("Ola Helder, tudo bem?");
        messages.add("Estou bem!");
        messages.add("Ola IA, tudo bem e com vc?");
        messages.add("Ola Helder, tudo bem?");
        messagesList.addAll(messages);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, MyService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_updateList:
                if (myService != null && myService.getMessageList().size() != 0) {
                    messagesList.clear();
                    messagesList.addAll(myService.getMessageList());
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(this, "Please, tap trigger service, then tap update the list to remove duplicated messages", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_triggerServiceUpdate:
                Intent service = new Intent(getApplicationContext(), MyService.class);
                service.putStringArrayListExtra("messages", (ArrayList<String>) messages);
                getApplicationContext().startService(service);
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        MyService.MyBinder b = (MyService.MyBinder) binder;
        myService = b.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        myService = null;
    }
}
