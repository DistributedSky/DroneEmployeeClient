package com.droneemployee.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by simon on 14.07.16.
 */
public class LockUiAsyncTask <Param, Result>
        extends AsyncTask <Param, Void, Result> {

    public interface Execute <Param, Result> {
        Result execute(Param param);
    }

    public interface ProcessResult <Result> {
        void execute(Result result);
    }

    public static final String TAG = "LockUiAsyncTask";

    private ProgressDialog progressDialog;
    private Context context;
    private String title;
    private Execute<Param, Result> execute;
    private ProcessResult<Result> processResult;

    LockUiAsyncTask(Context context,
                    String title,
                    Execute<Param, Result> execute,
                    ProcessResult<Result> processResult) {
        this.context = context;
        this.title = title;
        this.execute = execute;
        this.processResult = processResult;
    }

    LockUiAsyncTask(Context context,
                    String title,
                    Execute<Param, Result> execute) {
        this(context, title, execute, null);
    }

    @Override
    protected void onPreExecute() {
        Log.i(TAG, "In onPreExecute");
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage("Please, wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Result doInBackground(Param... paramses) {
        Log.i(TAG, "In doInBackGround");
        return execute.execute(paramses[0]);
    }

    @Override
    protected void onPostExecute(Result result) {
        Log.i(TAG, "In onPostExecute");
        if(processResult != null) {
            processResult.execute(result);
        }
        progressDialog.dismiss();
    }

    public void run(Param... params) {
        Log.i(TAG, "In run");
        execute(params.length > 0 ? params[0] : null);
    }
}