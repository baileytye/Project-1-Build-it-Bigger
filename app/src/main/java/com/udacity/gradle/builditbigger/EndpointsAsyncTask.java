package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
    public static final String TAG = "EndpointsAsyncTask";
    private static MyApi myApiService = null;

    private final OnDoneCallback callback;
    @Nullable
    private final SimpleIdlingResource idlingResource;


    public interface OnDoneCallback{
        void onDone(String result);
    }

    public EndpointsAsyncTask(OnDoneCallback c, SimpleIdlingResource ir){
        callback = c;
        idlingResource = ir;
    }

    @Override
    protected String doInBackground(Void... voids) {
        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }


    @Override
    protected void onPostExecute(String result) {
        if (idlingResource != null) {
            idlingResource.setIdleState(true);
        }
//        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        callback.onDone(result);
    }
}