package com.grabbddemoapp.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.grabbddemoapp.R;

/**
 * Developer: Sandy
 */
public final class ProgressDialog {
    private static final float DIM_AMOUNT = 0.6f;
    private static Dialog progressDialog;
    private static TextView tvProgress;
    private static TextView innerProgress;

    /**
     * Empty Constructor
     * not called
     */
    private ProgressDialog() {
    }

    /**
     * Is showing boolean.
     *
     * @return the boolean
     */
    public static boolean isShowing() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * Shows the progress dialog
     *
     * @param context the context
     */
    public static void showProgressDialog(final Context context) {
        showProgressDialog(context, context.getString(R.string.text_loading));
    }

    /**
     * Method to show the progress dialog with a message
     *
     * @param context the context
     * @param message the message
     * @return
     */
    public static void showProgressDialog(final Context context, final String message) {
        try {
            /* Check if the last instance is alive */
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    tvProgress.setText(message);
                    return;
                }
            }
            /*  Ends Here   */
            progressDialog = new Dialog(context,
                    android.R.style.Theme_Translucent_NoTitleBar);
            progressDialog.setContentView(R.layout.dialog_progress);
            tvProgress = (TextView) progressDialog
                    .findViewById(R.id.tvProgress);
            innerProgress = (TextView) progressDialog
                    .findViewById(R.id.progress);
//            tvProgress.setTypeface(Font.getRegular(activity));
            tvProgress.setText(message);
            innerProgress.setText("");
            ((ProgressWheel) progressDialog.findViewById(R.id.progress_wheel))
                    .spin();
            Window dialogWindow = progressDialog.getWindow();
            WindowManager.LayoutParams layoutParams = dialogWindow
                    .getAttributes();
            layoutParams.dimAmount = DIM_AMOUNT;
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update progress.
     *
     * @param percentage the percentage
     */
    public static void updateProgress(final int percentage) {
        innerProgress.setText(Integer.toString(percentage) + "%");
    }

    /**
     * Dismisses the Progress Dialog
     *
     * @return the boolean
     */
    public static boolean dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                try {
                    progressDialog.dismiss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                progressDialog = null;
                tvProgress = null;
                innerProgress = null;
                return true;
            }
        }
        return false;
    }
}