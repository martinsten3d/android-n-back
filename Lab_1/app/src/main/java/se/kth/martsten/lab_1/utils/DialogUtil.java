package se.kth.martsten.lab_1.utils;

import android.app.Activity;
import android.app.Dialog;

import androidx.appcompat.app.AlertDialog;

public class DialogUtil {

    /**
     * creates a dialog to display for the user
     * @param activity the current activity
     * @param title the title of the dialog box
     * @param msg the message of the dialog box
     * @return a new dialog which can now be shown
     */
    public static Dialog createDialog(Activity activity, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", (dialog, id) -> { });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return(dialog);
    }
}
