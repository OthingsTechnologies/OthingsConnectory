package io.technologies.othings.othingsconnector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Demo extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout bluetoothPrintingButton;
    private LinearLayout networkPrintingButton;
    private int COARSE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        bluetoothPrintingButton = findViewById(R.id.bluetoothPrinting);
        networkPrintingButton = findViewById(R.id.networkPrinting);

        bluetoothPrintingButton.setOnClickListener(this);
        networkPrintingButton.setOnClickListener(this);


        if( !isGrantedPermission() ){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    COARSE_LOCATION_PERMISSION);

        }


    }

    private boolean isGrantedPermission(){

        if( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if( requestCode == COARSE_LOCATION_PERMISSION ){

            if( grantResults[0] == PackageManager.PERMISSION_GRANTED ){



            }

        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.bluetoothPrinting:{

                Intent intent = new Intent(this,BluetoothPrinting.class);
                startActivity(intent);

                break;
            }
            case R.id.networkPrinting:{

                Intent intent = new Intent(this,NetworkPrinting.class);
                startActivity(intent);

                break;
            }

        }

    }
}
