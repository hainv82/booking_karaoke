package com.example.karama.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.view.WindowManager;

import com.example.karama.R;

public class UIHelper {
    public static Dialog setShowProgressBar(Context c) {
        Dialog dialog = new Dialog(c, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_item_load);
        dialog.setCancelable(false);
        return dialog;
    }
    public static void showAlertDialog(Context context, String title, String message, int iconResId) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setIcon(iconResId);
            builder.setCancelable(false);
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            try {
                dialog.show();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showAlertDialogV3(Context context, String title, String message, int iconResId, IInterfaceModel.OnBackIInterface onBackIInterface ) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setIcon(iconResId);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    onBackIInterface.onSuccess();
                }
            });
            AlertDialog dialog = builder.create();
            try {
                dialog.show();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            }
        }
    }
    public static void showAlertDialogConfirm(Context context, String title, String message, int iconResId, IInterfaceModel.OnBackIInterface onBackIInterface ) {
        if (context != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setIcon(iconResId);
            builder.setCancelable(true);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    onBackIInterface.onSuccess();
                }
            });
            AlertDialog dialog = builder.create();
            try {
                dialog.show();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            }
        }
    }

}
