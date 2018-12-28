package io.technologies.othings.othingsconnector;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class NetworkPrinting extends AppCompatActivity implements View.OnClickListener {

    private EditText printerIp;
    private EditText printerPort;
    private EditText data;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_printing);

        printerIp = findViewById(R.id.printerIP);
        printerPort = findViewById(R.id.printerPort);
        data = findViewById(R.id.data);
        connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.connectButton:{

                String ip = printerIp.getText().toString();
                int port = Integer.valueOf( (printerPort.getText().toString().length() > 0 )?printerPort.getText().toString():"0" );
                String data = this.data.getText().toString();
                OthingsConnector.NetworkDevice networkDevice = new OthingsConnector.NetworkDevice(this,ip,port);
                networkDevice.setOnConnectNetworkDevice(new OthingsConnector.NetworkDevice.ConnectNetworkDeviceListener() {
                    @Override
                    public void onSuccess() {

                        //The data we

                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(NetworkPrinting.this, error, Toast.LENGTH_SHORT).show();

                    }
                });

                networkDevice.sendData(data); // Here we send the data to the device


            }

        }

    }



}
