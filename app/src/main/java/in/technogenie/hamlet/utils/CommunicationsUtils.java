package in.technogenie.hamlet.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * This utility is created for methods required for all communications like Phone calls etc.
 */
public class CommunicationsUtils {

    public static void onCall(Activity activity, String mobileNumber) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 123);
        } else {
            activity.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+ mobileNumber)));
        }
    }
}
