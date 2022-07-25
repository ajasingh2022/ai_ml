package com.capgemini.sesp.ast.android.module.util;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.capgemini.sesp.ast.android.R;
import com.capgemini.sesp.ast.android.module.communication.CommunicationException;
import com.capgemini.sesp.ast.android.ui.layout.GuiController;
import com.skvader.rsp.cft.common.util.Utils;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * Use this class when you need to do something time consuming in a background thread,
 * while occupying the user with a waiting dialog.
 *
 * @param <Result> The type of data that <code>runInBackground</code> will pass on to <code>onPostExecute</code>
 */
//@SuppressLint("InflateParams")
public abstract class GuiWorker<Result> extends AsyncTask<Void, String, Result> {
    private static int n = 1;
    protected final Context ctx;
    protected final Class<? extends Activity> nextActivityClass;
    private String message = null;
    private Exception exception = null;
    private ProgressDialog progressDialog = null;
    private Integer launchIntentFlag = null;

    protected GuiWorker(Activity ownerActivity) {
        this(ownerActivity, null);
    }

    protected GuiWorker(Activity ownerActivity, boolean useSingleTop) {
        this(ownerActivity, null, null);
        if (useSingleTop) {
            this.launchIntentFlag = Intent.FLAG_ACTIVITY_SINGLE_TOP;
        }
    }

    protected GuiWorker(Activity ownerActivity, Class<? extends Activity> nextActivityClass) {
        this(ownerActivity, nextActivityClass, null);

    }

    protected GuiWorker(final Activity ownerActivity,
                        final Class<? extends Activity> nextActivityClass, final String message) {
        this.ctx = ownerActivity;
        this.nextActivityClass = nextActivityClass;
        this.message = (message == null) ? ctx.getString(R.string.loading) : message;
    }

    protected GuiWorker(final Activity ownerActivity,
                        final Class<? extends Activity> nextActivityClass, final String message, Integer launchIntentFlag) {
        this.ctx = ownerActivity;
        this.nextActivityClass = nextActivityClass;
        this.message = (message == null) ? ctx.getString(R.string.loading) : message;
        this.launchIntentFlag = launchIntentFlag;
    }

    @Override
    protected final void onPreExecute() {
        progressDialog = new ProgressDialog(ctx);
        // Load the custom title view
        final LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customTitleLayout = inflater.inflate(R.layout.progress_bar_custom_layout, null);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCustomTitle(customTitleLayout);
        ((TextView) customTitleLayout.findViewById(R.id.progressVwId)).setText(R.string.loading);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    protected final Result doInBackground(Void... nothing) {
        try {
            Thread.currentThread().setName(getClass().getSimpleName() + " - " + ctx.getClass().getSimpleName() + " #" + n++);
            Result result = runInBackground();
            return result;
        } catch (Exception e) {
            exception = e;
            writeLog("GuiWorker : doInBackground()", e);
            return null;
        }
    }

    @Override
    protected final void onProgressUpdate(String... messages) {
        if (progressDialog != null) {
            progressDialog.setMessage(messages[0]);
        }
    }

    @Override
    protected final void onCancelled() {
        onPostExecute(null);
    }

    @Override
    protected void onCancelled(Result result) {
        onPostExecute(null);
    }

    @Override
    protected void onPostExecute(Result result) {
        progressDialog.dismiss();
        progressDialog = null;
        String errorMessage = null;
        //For offline mode to work  isloggedin check
        if (!isSuccessful() && SessionState.getInstance().isLoggedInOnline()) {
            if (exception != null && exception instanceof CommunicationException) {
                errorMessage = (Utils.safeToString(exception.getMessage()).isEmpty() ? exception.getClass().getSimpleName() : exception.getMessage());
            } else if (isCancelled()) {
                errorMessage = ctx.getString(R.string.action_cancelled);
            } else {
                errorMessage = ctx.getString(R.string.network_error);
            }
            // commented no need to show url and port number if network is not available
            Builder builder = GuiController.showErrorDialog((Activity) ctx, errorMessage);
            builder.show();
        } /*else if(!SessionState.getInstance().isLoggedInOnline()) {
            errorMessage = ctx.getString(R.string.not_connected_online);
			Builder builder = GuiController.showErrorDialog((Activity) ctx, errorMessage);
			builder.show();
		}*/

        onPostExecute(isSuccessful(), result);
    }

    public final void start() {
        execute();
    }

    public final boolean isSuccessful() {
        return (!isCancelled() && exception == null);
    }

    public final Exception getException() {
        return exception;
    }

    /**
     * @see GuiWorker#setMessage(String)
     */
    protected final void setMessage(int messageId) {
        setMessage(ctx.getString(messageId));
    }

    /**
     * Use this to change the message being displayed in the loading dialog
     *
     * @param message The message
     */
    protected final void setMessage(String message) {
        this.message = message;
        if (progressDialog != null) {
            publishProgress(message);
        }
    }

    /**
     * Override to add custom background tasks, eg webservice communication
     *
     * @return The return result will be passed on to <code>onPostExecute</code>.
     * @throws Exception
     */
    protected abstract Result runInBackground() throws Exception;

    /**
     * Override to add custom foreground tasks. Called when the background job is completed.
     * The default implementation will jump to the next activity if set.
     *
     * @param successful True if the <code>runInBackground</code> completed without any exception
     * @param result     The returned result from <code>runInBackground</code>
     */
    protected void onPostExecute(boolean successful, Result result) {
        if (successful) {
            if (nextActivityClass != null) {
                AndroidUtilsAstSep.launchExplicitActivity(ctx, nextActivityClass, launchIntentFlag);
            }
        }
    }

}
