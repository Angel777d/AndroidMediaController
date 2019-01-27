package ru.angelovich.mediacontroller.mediacontrollerserver;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.widget.TextView;

import ru.angelovich.events.IEvent;
import ru.angelovich.events.IEventCallback;
import ru.angelovich.sockets.ISocketEvent;
import ru.angelovich.sockets.SocketA;


public class MainActivity extends AppCompatActivity {
    public static final int PORT = 8083;

    private TextView logTextView;
    private SocketA server;
    private MediaController ctrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logTextView = findViewById(R.id.log);

        ctrl = new MediaController(this);

        server = new SocketA(8083);

        server.addEventListener(SocketA.SOCKET_A_CLIENT_CONNECTED, new IEventCallback() {
            @Override
            public void call(IEvent event) {
                ISocketEvent sEvent = (ISocketEvent) event;
            }
        });

        server.addEventListener(SocketA.SOCKET_A_ON_MESSAGE, new IEventCallback() {
            @Override
            public void call(IEvent event) {
                ISocketEvent sEvent = (ISocketEvent) event;
                String message = sEvent.getMessage();
                LogMe(message);
                ctrl.OnCommand(message);
                sEvent.getClient().Send("OK\n");
            }
        });

        server.addEventListener(SocketA.SOCKET_A_CLIENT_CLOSED, new IEventCallback() {
            @Override
            public void call(IEvent event) {

            }
        });

        server.start();


        showWiFiInfo();
    }

    @SuppressWarnings("deprecation")
    private void showWiFiInfo() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();

        String ipAddress = Formatter.formatIpAddress(ip);
        LogMe(ipAddress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void LogMe(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.setText(message);
            }
        });
    }
}
