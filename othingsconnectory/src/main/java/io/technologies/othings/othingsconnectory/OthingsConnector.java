package io.technologies.othings.othingsconnectory;

import android.os.AsyncTask;

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

        public NetworkDevice(){}
        public NetworkDevice( String ip , int port ){

            this.ip = ip;
            this.port = port;

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
                            connectNetworkDeviceListener.onSuccess();
                        }

                    }
                    else{

                        if( connectNetworkDeviceListener != null ){
                            connectNetworkDeviceListener.onError("No se pudo conectar con el dispositivo");
                        }

                    }

                } catch (UnknownHostException e) {

                    if( connectNetworkDeviceListener != null ){
                        connectNetworkDeviceListener.onError(e.getMessage());
                    }

                    e.printStackTrace();
                } catch (IOException e) {

                    if( connectNetworkDeviceListener != null ){
                        connectNetworkDeviceListener.onError(e.getMessage());
                    }

                    e.printStackTrace();
                } catch (InterruptedException e) {

                    if( connectNetworkDeviceListener != null ){
                        connectNetworkDeviceListener.onError(e.getMessage());
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


}
