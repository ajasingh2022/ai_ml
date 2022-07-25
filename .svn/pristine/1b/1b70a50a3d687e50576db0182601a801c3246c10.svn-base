package com.capgemini.sesp.ast.android.ui.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class GuiController {
    static String TAG = GuiController.class.getSimpleName();

    public static Builder showErrorDialog(final Activity ctx, String message) {
        return showErrorDialog(ctx, ctx.getString(R.string.error), message);
    }

    public static Builder showErrorDialog(final Activity ctx, String title, String message) {
        Builder dialog = new Builder(ctx);
        try {
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setIcon(R.drawable.ic_cancel_black_24dp);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //dialog.dismiss(); // Method deprecated, and should not use -1
                        }
                    });
            dialog.create();
        } catch (Exception e) {
            writeLog(TAG + "  : showErrorDialog() ", e);
        }
        return dialog;
    }

    public static Builder showWarningDialog(final Activity ctx, String message) {
        return showWarningDialog(ctx, ctx.getString(R.string.warning), message);
    }

    public static Builder showWarningDialog(final Activity ctx, String title, String message) {
        Builder dialog = new Builder(ctx);
        try {
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setIcon(R.drawable.ic_warning2);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Method deprecated, and should not use -1
                        }
                    });
            dialog.create();
        } catch (Exception e) {
            writeLog(TAG + "  : showWarningDialog() ", e);
        }
        return dialog;
    }

    public static void showExceptionDialog(Throwable t) {
        showNotImplementedWarning();
    }

    public static void showExceptionDialog(Throwable t, String title) {
        showNotImplementedWarning();
    }

    public static void showReportErrorDialog(String errorTitle, String errorDescription, String additionalInfo) {
        showNotImplementedWarning();
    }

    /**
     * The method would be used to get handle on a custom
     * alert dialog
     *
     * @param context  ({@link Context}
     * @param question {@link String}
     * @param title    {@link String}
     * @return {@link Builder}
     */
    public static Builder getCustomAlertDialog(final Context context,
                                               final View layoutView, final String question, final String title) {
        Builder builder = null;
        try {
            if (context != null) {
                builder = new Builder(context);
                if (layoutView != null) {
                    if (question != null) {
                        TextView questionTextView = layoutView.findViewById(R.id.msg1);
                        if (questionTextView != null) {
                            questionTextView.setText(question);
                        }
                    }
                    if (title != null) {
                        TextView titleTextView = layoutView.findViewById(R.id.alertTitle);
                        if (titleTextView != null) {
                            titleTextView.setText(title);
                        }
                    }
                    builder.setView(layoutView);
                }
            }
        } catch (Exception e) {
            writeLog(TAG + "  : getCustomAlertDialog() ", e);
        }
        return builder;
    }


    /**
     * The method would be used to get handle on a custom
     * alert dialog
     *
     * @param context  ({@link Context}
     * @param question {@link String}
     * @param title    {@link String}
     * @return {@link Builder}
     */
    public static Builder getCustomAlertDialog(final Context context,
                                               final int layoutId, final String question, final String title) {
        Builder builder = null;
        try{
        if (context != null) {
            final View layoutView
                    = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
            builder = getCustomAlertDialog(context, layoutView, question, title);
        }
        } catch (Exception e) {
            writeLog(TAG + "  : getCustomAlertDialog() ", e);
        }
        return builder;
    }

    public static AlertDialog showConfirmDialog(final Activity ctx, String question, String title) {
        return new Builder(ctx)
                .setTitle(R.string.menu_logout)
                .setMessage(R.string.logout_question)
                .setIcon(R.drawable.ic_menu_logout)
                .setCancelable(false)
                // setPositiveButton(...) should be overridden if another behaviour is wanted then just close the dialog
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Method deprecated, and should not
                                // use -1
                            }
                        })
                // setNegativeButton(...) should be overridden if another behaviour is wanted then just close the dialog
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Method deprecated, and should not use -1
                            }
                        }).create();
    }

    public static Builder showConfirmCancelDialog(final Activity ctx, String title, String question) {
        Builder dialog = new Builder(ctx);
        try{
        dialog.setTitle(title);
        dialog.setMessage(question);
        dialog.setIcon(R.drawable.ic_question);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Method deprecated, and should not use -1
                    }
                });
        dialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Method deprecated, and should not use -1
                    }
                });
        dialog.create();
        } catch (Exception e) {
            writeLog(TAG + "  : showConfirmCancelDialog() ", e);
        }
        return dialog;
    }

    public static Builder showInfoDialog(final Activity ctx, String message) {
        return showInfoDialog(ctx, ctx.getString(R.string.information), message);
    }

    public static Builder showInfoDialog(final Activity ctx, String title, String message) {
        Builder dialog = new Builder(ctx);
        try{
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIcon(R.drawable.ic_info);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Method deprecated, and should not use -1
                    }
                });
        dialog.create();
        } catch (Exception e) {
            writeLog(TAG + "  : showInfoDialog() ", e);
        }
        return dialog;
    }

    public static void showOpenDialog() {
        showNotImplementedWarning();
    }


    public static String showPasswordInputDialog(String message, String title) {
        showNotImplementedWarning();
        return null;
    }

    public static String showInputDialog(String message, String title) {
        showNotImplementedWarning();
        return null;
    }

    public static void showNotImplementedWarning() {
        Toast.makeText(ApplicationAstSep.context, ApplicationAstSep.context.getString(R.string.error_not_implemented), Toast.LENGTH_SHORT).show();

    }
}
