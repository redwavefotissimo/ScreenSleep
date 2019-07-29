package lim.com.screensleep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by user on 3/24/2019.
 */

public class autostart extends BroadcastReceiver
{
    public void onReceive(Context context, Intent arg1)
    {
        Intent intent = new Intent(context, FloatingButtonService.class);

        context.startService(intent);

        Log.i("Autostart", "started");
    }
}
