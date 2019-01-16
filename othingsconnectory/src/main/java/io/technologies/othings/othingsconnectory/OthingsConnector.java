package io.technologies.othings.othingsconnectory;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OthingsConnector {

    public static class NetworkDevice{

        private String ip;
        private int port;
        private PrintWriter printWriter;
        private BufferedReader bufferedReader;
        private ConnectNetworkDeviceListener connectNetworkDeviceListener;
        private int responseTime = 500;
        private boolean run = false;
        private Context context;
        private LoaderDialog loaderDialog;

        public NetworkDevice( Context context , String ip , int port ){

            this.ip = ip;
            this.port = port;
            this.context = context;
            loaderDialog = new LoaderDialog(context);

        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void sendData( String data ){

            new connect().execute(data);

        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.loaderDialog = new LoaderDialog(context);
            this.context = context;
        }

        public void setResponseTime(int responseTime) {
            this.responseTime = responseTime;
        }

        public int getResponseTime() {
            return responseTime;
        }

        public void setOnConnectNetworkDevice(ConnectNetworkDeviceListener connectNetworkDevice ){

            this.connectNetworkDeviceListener = connectNetworkDevice;

        }

        private class connect extends AsyncTask<String,Void,Void>{


            @Override
            protected Void doInBackground(String... strings) {

                try{

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            loaderDialog.show("Imprimiendo ...");

                        }
                    });

                    String data = strings[0];

                    try {
                        InetAddress inetAddress =  InetAddress.getByName(ip);
                        Socket socket = new Socket(inetAddress,port);

                        printWriter = new PrintWriter(new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
                        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        if( printWriter != null ){

                            printWriter.write(data);
                            Thread.sleep(responseTime);
                            printWriter.flush();
                            socket.close();

                            if( connectNetworkDeviceListener != null ){

                                (((Activity) context)).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        loaderDialog.hide();
                                        connectNetworkDeviceListener.onSuccess();

                                    }
                                });


                            }

                        }
                        else{

                            if( connectNetworkDeviceListener != null ){

                                (((Activity) context)).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loaderDialog.hide();
                                        connectNetworkDeviceListener.onError("No se pudo conectar con el dispositivo");

                                    }
                                });


                            }

                        }

                    } catch (final UnknownHostException e) {

                        if( connectNetworkDeviceListener != null ){

                            (((Activity) context)).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loaderDialog.hide();
                                    connectNetworkDeviceListener.onError(e.getMessage());

                                }
                            });


                        }

                        e.printStackTrace();
                    } catch ( final IOException e) {

                        if( connectNetworkDeviceListener != null ){
                            (((Activity) context)).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loaderDialog.hide();
                                    connectNetworkDeviceListener.onError(e.getMessage());

                                }
                            });
                        }

                        e.printStackTrace();
                    } catch ( final InterruptedException e) {

                        if( connectNetworkDeviceListener != null ){
                            (((Activity) context)).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loaderDialog.hide();
                                    connectNetworkDeviceListener.onError(e.getMessage());

                                }
                            });
                        }

                        e.printStackTrace();
                    }



                }catch (NullPointerException ex){

                    ex.printStackTrace();

                }




                return null;
            }
        }

        public interface ConnectNetworkDeviceListener{

            void onSuccess();
            void onError(String error);

        }

        public void hideDialog(){

            loaderDialog.hide();

        }


    }

    public static class BluetoothDevice{

        private BluetoothAdapter bluetoothAdapter;
        private Context context;
        private BroadcastReceiver broadcastReceiver;
        private View view;
        private BluetoothDevices filter;
        private List<android.bluetooth.BluetoothDevice> bluetoothDeviceList;
        private BluetoothDevicesListener bluetoothDevicesListener;
        private BluetoothDeviceListener bluetoothDeviceListener;
        private String macAddress;
        private LoaderDialog loaderDialog;

        public BluetoothDevice( Context context){

            this.context = context;
            this.view = ((Activity) context).findViewById(R.id.content);
            this.bluetoothDeviceList = new ArrayList<>();
            this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            this.filter = BluetoothDevices.ALL;
            this.macAddress = "";
            this.loaderDialog = new LoaderDialog(context);
            this.broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {


                    String action = intent.getAction();

                    if(android.bluetooth.BluetoothDevice.ACTION_FOUND.equals(action)){

                        android.bluetooth.BluetoothDevice device = intent.getParcelableExtra(android.bluetooth.BluetoothDevice.EXTRA_DEVICE);

                        if( device.getAddress().equals(macAddress) ){

                            if( bluetoothDeviceListener != null ){

                                bluetoothDeviceListener.onReceive(device);
                                bluetoothAdapter.cancelDiscovery();

                            }

                        }

                        if( BluetoothDevices.PRINTERS.hashCode() == filter.hashCode() ){

                            if( device.getBluetoothClass().getDeviceClass() == 1664 ){
                                bluetoothDeviceList.add(device);
                            }
                        }
                        else{
                            bluetoothDeviceList.add(device);
                        }

                    }
                    else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){

                        loaderDialog.show("Buscando dispositivos ...");

                    }
                    else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){

                        loaderDialog.hide();
                        if( bluetoothDevicesListener != null ){
                            bluetoothDevicesListener.onReceive(bluetoothDeviceList);
                        }




                    }


                }
            };



        }

        public BluetoothDevice filter( BluetoothDevices bluetoothDevices  ){

            this.filter = bluetoothDevices;
            return this;

        }

        public void getAllBluetoothDevices( BluetoothDevicesListener bluetoothDevicesListener ){

            this.bluetoothDevicesListener = bluetoothDevicesListener;
            subscribeBroadcast();
            if( bluetoothAdapter.isDiscovering() ){
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothDeviceList.clear();
            bluetoothAdapter.startDiscovery();


        }

        public void getBluetoothDevice( String macAddress , BluetoothDeviceListener bluetoothDeviceListener){

            this.bluetoothDeviceListener = bluetoothDeviceListener;
            this.macAddress = macAddress;
            subscribeBroadcast();
            if( bluetoothAdapter.isDiscovering() ){
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothDeviceList.clear();
            bluetoothAdapter.startDiscovery();

        }

        private void subscribeBroadcast(){

            IntentFilter filter = new IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND);
            (context).registerReceiver(broadcastReceiver,filter);
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            (context).registerReceiver(broadcastReceiver,filter);
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            (context).registerReceiver(broadcastReceiver,filter);

        }




        public enum BluetoothDevices{

            ALL,
            PRINTERS,
            PHONES,
            COMPUTERS

        }

        public void sendData(final android.bluetooth.BluetoothDevice bluetoothDevice , final String data , final BluetoothConnection bluetoothConnection){


            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        ParcelUuid[] _uuids = bluetoothDevice.getUuids();
                        if( _uuids != null ){

                            String uuids = String.valueOf(_uuids[0]);
                            UUID uuid = UUID.fromString(uuids);
                            BluetoothSocket socket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                            Looper.prepare();
                            socket.connect();

                            if( socket.isConnected() ){

                                OutputStream outputStream = socket.getOutputStream();

                                outputStream.write(data.getBytes());
                                Thread.sleep(500);
                                socket.close();
                                Looper.myLooper().quit();

                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        bluetoothConnection.onSucess();

                                    }
                                });

                            }
                            else{

                                ((Activity)context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        bluetoothConnection.onError(Error.CONNECTION_REFUSED);

                                    }
                                });

                            }

                        }
                        else{

                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    bluetoothConnection.onError(Error.DEVICE_NOT_PAIRED);

                                }
                            });

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }).start();

        }

        public interface BluetoothConnection{
            void onSucess();
            void onError( Error error);
        }

        public interface BluetoothDevicesListener{
            void onReceive(List<android.bluetooth.BluetoothDevice> bluetoothDevices);
        }
        public interface BluetoothDeviceListener{
            void onReceive(android.bluetooth.BluetoothDevice bluetoothDevice);
        }
        public enum Error{
            DEVICE_NOT_PAIRED,
            CONNECTION_REFUSED
        }

        public void hideDialog(){

            loaderDialog.hide();
            bluetoothAdapter.cancelDiscovery();

        }

    }

    private static class LoaderDialog{

        private Context context;
        private AlertDialog dialog;
        private View view;
        private ProgressBar progressBar;
        private TextView status;

        public LoaderDialog( Context context ){

            this.context = context;
            this.view = LayoutInflater.from(context).inflate(R.layout.loader_dialog,null);
            this.progressBar = view.findViewById(R.id.progressBar);
            this.status = view.findViewById(R.id.status);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(view);
            dialog = builder.create();
            dialog.setCancelable(false);

        }

        public void show(String data){

            status.setText(data);
            dialog.show();

        }

        public void hide(){

            dialog.dismiss();

        }


    }



}
