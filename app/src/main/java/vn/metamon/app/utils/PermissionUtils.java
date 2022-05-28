package vn.metamon.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public final class PermissionUtils {

    private static final int PERMISSION_BASE = 9000;

    public static final int LOCATION_REQUEST_CODE = PERMISSION_BASE + 1;

    public static final int EXTERNAL_STORAGE_REQUEST_CODE = PERMISSION_BASE + 2;

    public static final int CAMERA_AND_AUDIO_REQUEST_CODE = PERMISSION_BASE + 3;

    public static final int CAMERA_REQUEST_CODE = PERMISSION_BASE + 4;

    public static final int CAMERA_RECORDING_REQUEST_CODE = PERMISSION_BASE + 5;

    public static final int REQUEST_AUDIO_PERMISSION_CODE = PERMISSION_BASE + 6;

    public static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final String[] CAMERA_PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    public static final String[] CAMERA_AND_AUDIO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    /**
     * Permission strings for external storage
     */
    public static final String[] EXTERNAL_STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private PermissionUtils() {
        throw new UnsupportedOperationException("Not allow instantiating object.");
    }

    public static void requestPermissions(Activity activity,
                                          String[] permissions,
                                          int requestCode,
                                          Runnable runnable) {
        if (PlatformUtils.hasMarshmallow()) {
            if (hasPermissions(activity, permissions)) {
                if (runnable != null) {
                    runnable.run();
                }
            } else {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }
        } else {
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public static void handlePermissionsResult(boolean isMyRequest,
                                               int[] grantResults,
                                               Runnable okRunnable,
                                               Runnable deniedRunnable) {
        if (isMyRequest) {
            boolean isGranted = true;
            for (int grantResult : grantResults) {
                isGranted &= isGranted(grantResult);
            }
            if (grantResults.length > 0 && isGranted) {
                if (okRunnable != null) {
                    okRunnable.run();
                }
            } else {
                if (deniedRunnable != null) {
                    deniedRunnable.run();
                }
            }
        }
    }

    public static boolean isGranted(int grantFlag) {
        return PackageManager.PERMISSION_GRANTED == grantFlag;
    }

    public static boolean hasExternalStoragePermission(@NonNull Context context) {
        return hasPermissions(context, EXTERNAL_STORAGE_PERMISSIONS);
    }

    public static boolean hasPermissions(Context context, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("permissions must not be null.");
        }
        boolean result = true;
        for (String perm : permissions) {
            result = result && isGranted(ContextCompat.checkSelfPermission(context, perm));
        }
        return result;
    }

    public static boolean hasAudioRecordPermission(@NonNull Context context) {
        final String[] AUDIO_PERMISSIONS = { Manifest.permission.RECORD_AUDIO };
        return hasPermissions(context, AUDIO_PERMISSIONS);
    }
}
