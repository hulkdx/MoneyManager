package com.hulkdx.moneymanager.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.ui.main.MainActivity;

public final class DialogFactory {

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context,
                                                   @StringRes int titleResource,
                                                   @StringRes int messageResource) {

        return createSimpleOkErrorDialog(context,
                context.getString(titleResource),
                context.getString(messageResource));
    }

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, @StringRes int messageResource) {
        return createGenericErrorDialog(context, context.getString(messageResource));
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context,
                                                      @StringRes int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }

    public static PopupMenu createPicturePopup(Activity activity, View view){
        PopupMenu popup = new PopupMenu(activity, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu_select_pictures, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.take_new_picture:
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // This check is required according to google.
                    // @link: https://developer.android.com/training/camera/photobasics.html
                    if (intent.resolveActivity(activity.getPackageManager()) == null) {
                        break;
                    }
                    activity.startActivityForResult(intent, MainActivity.CAPTURED_IMAGE);
                    break;
                case R.id.choose_gallery:
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    if (intent.resolveActivity(activity.getPackageManager()) == null) {
                        break;
                    }
                    activity.startActivityForResult(intent, MainActivity.PICKED_IMAGE);
                    break;
            }
            return true;
        });

        return popup;
    }

}
