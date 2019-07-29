package lim.com.screensleep;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class FloatingButtonService extends Service {

    DevicePolicyManager mDPM;
    ComponentName mAdminName;

    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams mParams;
    private Button mMouseView;

    public FloatingButtonService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDPM = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, MyAdmin.class);

        mMouseView = new Button(getApplicationContext());
        mMouseView.setText(R.string.sleepBtn);
        mMouseView.setOnClickListener(slpBtnClick);

        mWindowManager = (WindowManager) getApplicationContext().getSystemService(
                WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();

        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.width = 150;
        mParams.height = 80;

        mMouseView.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY;
            int paramX, paramY;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = mParams.x;
                        paramY = mParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        mParams.x = paramX + dx;
                        mParams.y = paramY + dy;

                        mWindowManager.updateViewLayout(mMouseView, mParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        //v.performClick();
                        break;
                }
                return false;
            }
        });

        mWindowManager.addView(mMouseView, mParams);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private View.OnClickListener slpBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mDPM.isAdminActive(mAdminName)) {
                mDPM.lockNow();
            }
        }
    };
}
