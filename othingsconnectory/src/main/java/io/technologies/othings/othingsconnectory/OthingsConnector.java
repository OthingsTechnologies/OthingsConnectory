package io.technologies.othings.othingsconnectory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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

        public NetworkDevice(){}
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


                return null;
            }
        }

        public interface ConnectNetworkDeviceListener{

            void onSuccess(  );
            void onError( String error );

        }


    }

    public static class BluetoothDevice{



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
