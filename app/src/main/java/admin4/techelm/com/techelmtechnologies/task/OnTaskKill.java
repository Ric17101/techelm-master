package admin4.techelm.com.techelmtechnologies.task;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

/**
 * Created by admin 4 on 18/04/2017.
 * This will listen if the app being swiped out and closed
 *
 * Implemented on ServiceJobViewPagerActivity to revert the Status of the ServiceJob
 */

/**
 * @deprecated "NOT USED... NOT WORKING"
 */
@Deprecated
public class OnTaskKill extends Service {

    private onStopCallbackInterface mCallback = null;

    public interface onStopCallbackInterface {
        void onAppStop();
    }

    public OnTaskKill(Activity act) {
        Log.e("OnTaskKill", "Im on the OntaskKill");
        this.mCallback = (onStopCallbackInterface) act;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        mCallback.onAppStop();
        stopSelf();
    }
}
