package com.digitalcurrencyexperts.rocketwallet;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;

import com.digitalcurrencyexperts.rocketwallet.common.Constants;
import com.digitalcurrencyexperts.rocketwallet.common.Interfaces;


/**
 * Created by Santiago Del Valle on 19/03/2017.
 * Company name Zombytes Devs
 * Contact sdelvalle57@gmail.com
 */

public class BaseActivity extends AppCompatActivity {
    Interfaces.PermissionCallback permissionCallback;


    public void setPermissionCallBack(Interfaces.PermissionCallback permissionCallBack){
        this.permissionCallback = permissionCallBack;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.CAMERA_PERMISSION) {
            if (permissions.length == 1 && permissions[0].equals(android.Manifest.permission.CAMERA)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (permissionCallback!=null) permissionCallback.onCameraPermissionGranted();
            }
        }
    }
}
