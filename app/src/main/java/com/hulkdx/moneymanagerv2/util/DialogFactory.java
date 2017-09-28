package com.hulkdx.moneymanagerv2.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import com.hulkdx.moneymanagerv2.R;

public final class DialogFactory {

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }


}
