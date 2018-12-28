package io.technologies.othings.othingsconnector;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.LayoutRes;

public class BluetoothDevicesAdapter extends BaseAdapter {

    private List<BluetoothDevice> bluetoothDevices;
    @LayoutRes private int layout;
    private Context context;

    public BluetoothDevicesAdapter(List<BluetoothDevice> bluetoothDevices, int layout, Context context) {
        this.bluetoothDevices = bluetoothDevices;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bluetoothDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return bluetoothDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.bluetooth_device_item,null);
        TextView name = view.findViewById(R.id.name);
        TextView macAddress = view.findViewById(R.id.macAddress);

        name.setText(bluetoothDevices.get(i).getName());
        macAddress.setText(bluetoothDevices.get(i).getAddress());

        return view;
    }
}
