package lim.com.screensleep;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class BlankActivity extends Activity {

    private static final int OVERLAY_PERMISSION_CODE = 5463;
    private static final int REQUEST_ENABLE = 4421;

    DevicePolicyManager mDPM;
    ComponentName mAdminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {

                // Open the permission page
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
                return;
            }
            else{
                startService();
            }
        }

        mDPM = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, MyAdmin.class);

        if (!mDPM.isAdminActive(mAdminName)) {
            Intent intentAdmin = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intentAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    mAdminName);
            startActivityForResult(intentAdmin, REQUEST_ENABLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        // If the permission has been checked
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Settings.canDrawOverlays(this)) {

            }
        }

        if (requestCode == REQUEST_ENABLE)
        {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Device Admin not Allowed to this App.", Toast.LENGTH_SHORT);
            }
        }
    }

    private void startService()
    {
        Intent intent = new Intent(this, FloatingButtonService.class);
        this.startService(intent);
    }
}
