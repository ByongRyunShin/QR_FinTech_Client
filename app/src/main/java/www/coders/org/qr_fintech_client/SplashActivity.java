package www.coders.org.qr_fintech_client;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends Activity {
    private final int REQUEST_PERMISSION = 1;

    int CAMERA_PEMISSION;
    int READ_STORAGE_PERMISSION;
    int WRITE_STORAGE_PERMISSION;
    int LOCATION_PERMISSION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        final Intent intent = new Intent(this, MainActivity.class);
        if (Build.VERSION.SDK_INT >= 15) //23
        {
            if (CAMERA_PEMISSION != PackageManager.PERMISSION_GRANTED && READ_STORAGE_PERMISSION != PackageManager.PERMISSION_GRANTED &&
                    WRITE_STORAGE_PERMISSION != PackageManager.PERMISSION_GRANTED && LOCATION_PERMISSION != PackageManager.PERMISSION_GRANTED)
            {
                grantPermission();
            }
            else
            {
                Thread timer= new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            //Display for 3 seconds
                            sleep(3000);
                        }
                        catch (InterruptedException e)
                        {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        finally
                        {
                            intent.putExtra("state", "launch");
                            startActivity(intent);
                            finish();
                        }
                    }
                };
                timer.start();

            }
        }
    }

    public void init()
    {
        CAMERA_PEMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        READ_STORAGE_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        WRITE_STORAGE_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        LOCATION_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

    }

    protected void grantPermission()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permissions are granted");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("TAG", "Permissions are denied");
                    Toast toast = Toast.makeText(getApplicationContext(), "접근 권한을 허가해주세요.", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
                return;
        }
    }
}
