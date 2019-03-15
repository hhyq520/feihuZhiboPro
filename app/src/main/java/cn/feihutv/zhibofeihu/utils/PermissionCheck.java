package cn.feihutv.zhibofeihu.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class PermissionCheck {
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private Activity mActivity;

    public PermissionCheck(Activity activity) {
        mActivity = activity;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    private boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1){
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkPermission() {
        boolean ret = true;

        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
            permissionsNeeded.add("相机");
        }
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO)) {
            permissionsNeeded.add("麦克风");
        }
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("存储");
        }

        if (permissionsNeeded.size() > 0) {
            // Need Rationale
            String message = "你需要允许权限:" + permissionsNeeded.get(0);
            for (int i = 1; i < permissionsNeeded.size(); i++) {
                message = message + ", " + permissionsNeeded.get(i);
            }
            // Check for Rationale Option
            if (!mActivity.shouldShowRequestPermissionRationale(permissionsList.get(0))) {
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mActivity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
            }
            else {
                mActivity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            ret = false;
        }

        return ret;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean ret = true;
        if (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            ret = false;
        }
        return ret;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            if (verifyPermissions(grantResults)) {
                // all permissions granted
            } else {
                // some permissions denied
                //FHUtils.showToast("some permissions denied");
            }
        }
    }
}
