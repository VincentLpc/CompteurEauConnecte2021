package com.exemple.ble;

/**
 * Développez une application Android - Sylvain Hébuterne - 2017 Edition ENI.
 */

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    final static String TAG="MainActivity";

    TextView text;
    TextView batteryLevel;
    Button connect;

    BluetoothManager bluetoothManager = null;
    BluetoothAdapter bluetoothAdapter=null;
    BluetoothLeScanner bluetoothLeScanner;
    BluetoothGatt bluetoothGatt;

    final static int REQUEST_PERMISSON = 102;
    final static int REQUEST_ENABLE_BLE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text  =(TextView)findViewById(R.id.main_text);
        batteryLevel =(TextView)findViewById(R.id.main_batteryLevel);
        connect  =(Button)findViewById(R.id.main_connect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothGatt!=null) {

                }

            }
        });

        ensurePermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bluetoothGatt!=null) {
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }

    private void ensurePermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.demande_permission_titre);
                builder.setMessage(R.string.explication_permission);
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, R.string.permission_obligatoire, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        askPermission();
                    }
                });
                builder.show();
            } else {
                askPermission();
            }
        } else {
            startBLEScan();
        }
    }

    private void askPermission() {
        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSON);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSON) {
            if(permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBLEScan();
            }
        }

    }

    private void startBLEScan() {
        if(bluetoothManager==null)
            bluetoothManager= (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        if(bluetoothAdapter==null)
            bluetoothAdapter = bluetoothManager.getAdapter();

        if(!bluetoothAdapter.isEnabled()) {
            Intent askBLE = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(askBLE, REQUEST_ENABLE_BLE);
            return;
        }
        if(android.os.Build.VERSION.SDK_INT<21) {
            bluetoothAdapter.startLeScan(leScanCallback);
            Log.d(TAG,"Scan lancé en version 18");
        } else {
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
            bluetoothLeScanner.startScan(scanCallback);
            Log.d(TAG,"Scan lancé en version 21");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopBLEScan();
            }
        }, 5000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_BLE && requestCode==RESULT_OK)
            startBLEScan();
    }

    private void stopBLEScan() {
        if(android.os.Build.VERSION.SDK_INT<21)
            bluetoothAdapter.stopLeScan(leScanCallback);
        else
            bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }

    // API 18
     BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            onDeviceDetected(device,rssi);
        }
    };

    // API 21
    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            onDeviceDetected(result.getDevice(), result.getRssi());
        }
    };

    private void onDeviceDetected(BluetoothDevice device, int rssi) {
        String info = "Objet détecté :";
        info+="\nNom :" + device.getName();
        info+="\nAdresse :" + device.getAddress();
        info+="\nRSSI : " + String.valueOf(rssi);
        text.setText(info);
        bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback);
    }

    private void showToastFromBackground(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBatteryLevelFromBackground(final int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                batteryLevel.setText(String.format("Niveau batterie :%d %%",value));
            }
        });
    }

    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if(status==BluetoothGatt.GATT_SUCCESS) {
                String message ="";
                if(newState== BluetoothProfile.STATE_CONNECTED) {
                    message = "Objet connecté";
                    bluetoothGatt.discoverServices();
                }
                else {
                    message = "Objet déconnecté";
                }
                showToastFromBackground(message);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if(status==BluetoothGatt.GATT_SUCCESS) {
                UUID batteryLevelServiceUUID = UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb");
                UUID batteryLevelCharacteristicUUUID = UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb");
                for(BluetoothGattService service : gatt.getServices()) {
                    Log.d(TAG,"Service :" + service.getUuid());
                    if(service.getUuid().equals(batteryLevelServiceUUID)) {
                        Log.d(TAG,"Service trouvé !");
                        for(BluetoothGattCharacteristic c : service.getCharacteristics()) {
                            Log.d(TAG,"Caracteristique :" + c.getUuid());
                            if(c.getUuid().equals(batteryLevelCharacteristicUUUID)) {
                                bluetoothGatt.readCharacteristic(c);

                            }
                        }
                    }
                }
            }
        }


        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d(TAG,"Caracteristique lue :" + characteristic.getUuid());
            if(status==BluetoothGatt.GATT_SUCCESS) {
                if (characteristic.getUuid().equals(UUID.fromString("00002a19-0000-1000-8000-00805f9b34fb"))) {
                    int batteryLevelValue = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                    Log.d(TAG, "Niveau de battery :" + batteryLevelValue);
                    setBatteryLevelFromBackground(batteryLevelValue);
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }
    };

}
