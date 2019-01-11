package io.technologies.othings.othingsconnector;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

public class BluetoothPrinting extends AppCompatActivity {

    private LinearLayout container;
    private Button buttonSearchBluetoothDevices;
    private Button buttonSearchBluetoothDevicesPrinters;
    private ListView listViewAllBluetoothDevices;
    private ListView listViewPrinters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_printing);

        container = findViewById(R.id.container);
        listViewAllBluetoothDevices = findViewById(R.id.allBluetoothDevices);
        buttonSearchBluetoothDevices = findViewById(R.id.searchBluetoothDevices);
        listViewPrinters = findViewById(R.id.printersBluetoothDevices);
        buttonSearchBluetoothDevicesPrinters = findViewById(R.id.searchBluetoothDevicesPrinters);
        final OthingsConnector.BluetoothDevice bluetoothDevice = new OthingsConnector.BluetoothDevice(container,this);

        buttonSearchBluetoothDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bluetoothDevice.filter(OthingsConnector.BluetoothDevice.BluetoothDevices.PRINTERS).getAllBluetoothDevices(new OthingsConnector.BluetoothDevice.BluetoothDevicesListener() {
                    @Override
                    public void onReceive(List<BluetoothDevice> bluetoothDevices) {

                        BluetoothDevicesAdapter bluetoothDevicesAdapter = new BluetoothDevicesAdapter(bluetoothDevices,R.layout.bluetooth_device_item,BluetoothPrinting.this);
                        listViewAllBluetoothDevices.setAdapter(bluetoothDevicesAdapter);

                    }
                });

            }
        });

        buttonSearchBluetoothDevicesPrinters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bluetoothDevice.filter(OthingsConnector.BluetoothDevice.BluetoothDevices.PRINTERS).getAllBluetoothDevices(new OthingsConnector.BluetoothDevice.BluetoothDevicesListener() {
                    @Override
                    public void onReceive(List<BluetoothDevice> bluetoothDevices) {

                        BluetoothDevicesAdapter bluetoothDevicesAdapter = new BluetoothDevicesAdapter(bluetoothDevices,R.layout.bluetooth_device_item,BluetoothPrinting.this);
                        listViewPrinters.setAdapter(bluetoothDevicesAdapter);

                        if( bluetoothDevices.size() > 0 ){

                            String data = "^XA ^FX Top section with company logo, name and address. ^CF0,60 ^FO50,50^GB100,100,100^FS ^FO75,75^FR^GB100,100,100^FS ^FO88,88^GB50,50,50^FS ^FO220,50^FDInternational Shipping, Inc.^FS ^CF0,40 ^FO220,100^FD1000 Shipping Lane^FS ^FO220,135^FDShelbyville TN 38102^FS ^FO220,170^FDUnited States (USA)^FS ^FO50,250^GB700,1,3^FS ^FX Second section with recipient address and permit information. ^CFA,30 ^FO50,300^FDJohn Doe^FS ^FO50,340^FD100 Main Street^FS ^FO50,380^FDSpringfield TN 39021^FS ^FO50,420^FDUnited States (USA)^FS ^CFA,15 ^FO600,300^GB150,150,3^FS ^FO638,340^FDPermit^FS ^FO638,390^FD123456^FS ^FO50,500^GB700,1,3^FS ^FX Third section with barcode. ^BY5,2,270 ^FO100,550^BC^FD12345678^FS ^FX Fourth section (the two boxes on the bottom). ^FO50,900^GB700,250,3^FS ^FO400,900^GB1,250,3^FS ^CF0,40 ^FO100,960^FDShipping Ctr. X34B-1^FS ^FO100,1010^FDREF1 F00B47^FS ^FO100,1060^FDREF2 BL4H8^FS ^CF0,190 ^FO485,965^FDCA^FS ^XZ";

                            BluetoothDevice b = bluetoothDevices.get(0);

                            bluetoothDevice.sendData(bluetoothDevices.get(0), data, new OthingsConnector.BluetoothDevice.BluetoothConnection() {
                                @Override
                                public void onSucess() {


                                }

                                @Override
                                public void onError(OthingsConnector.BluetoothDevice.Error error) {

                                    switch (error){

                                        case DEVICE_NOT_PAIRED:{

                                            Intent intentOpenBluetoothSettings = new Intent();
                                            intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                                            startActivity(intentOpenBluetoothSettings);

                                            break;
                                        }

                                    }

                                }
                            });

                        }


                    }
                });

            }
        });



    }
}
