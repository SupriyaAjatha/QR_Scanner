package com.example.barcodescanner_indian_designs_exports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.barcodescanner_indian_designs_exports.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    final static int CAMERA_LOCATION_REQUEST_CODE = 100;
    private LocationManager locationManager;
    public static MainActivity mainActivity;
    boolean activity_backpress = true;


    @Override
    protected void onResume() {
        super.onResume();
        if (!isLocationEnabled(this))
            turnOnLocationDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainActivity = this;
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        binding.clickCameraBtn.setOnClickListener(view -> {
            user_Permission();
            setFragment(new BarcodeScannerFragment(), true);
            binding.clickCameraBtn.setVisibility(View.GONE);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == getPackageManager().PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean user_Permission() {
        boolean value = false;
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                    CAMERA_LOCATION_REQUEST_CODE);
            value = true;
        }
        return value;
    }


    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    public void turnOnLocationDialog(Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
// On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.show();
    }


    public void setFragment(Fragment fragment, Boolean backstackneeded) {
        if (backstackneeded) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Container, fragment, fragment.getClass().getName()).addToBackStack(fragment.getClass().getName()).commit();
        } else
            getSupportFragmentManager().beginTransaction().replace(R.id.Container, fragment, fragment.getClass().getName()).commit();
    }

    @Override
    public void onBackPressed() {
        if (activity_backpress) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
            binding.clickCameraBtn.setVisibility(View.VISIBLE);
        }
    }
}
