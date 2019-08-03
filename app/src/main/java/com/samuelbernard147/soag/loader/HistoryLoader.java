package com.samuelbernard147.soag.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.samuelbernard147.soag.model.Humidity;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HistoryLoader extends AsyncTaskLoader<ArrayList<Humidity>> {
    private ArrayList<Humidity> mData;
    private boolean mHasResult = false;
    private String tag;

    public HistoryLoader(final Context context, String tag) {
        super(context);
        onContentChanged();
        this.tag = tag;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final ArrayList<Humidity> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            mData = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<Humidity> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        String url = "http://soag.starbhaktefa.com/public/riwayat?jenis=" + tag;
        final ArrayList<Humidity> humidities = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                //Menggunakan synchronous karena pada dasarnya thread yang digunakan sudah asynchronous dan method
                //loadInBackground mengembalikan nilai balikan
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONArray responseObject = new JSONArray(result);
                    for (int i = 0; i < responseObject.length(); i++) {
                        Humidity humidity = new Humidity(responseObject, i);
                        humidities.add(humidity);
                    }
                } catch (Exception e) {
                    //Jika terjadi error pada saat parsing maka akan masuk ke catch()
                    Log.e("Exception", "Data gagal diparsing");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Jika response gagal maka , do nothing
                Log.e("OnFailure", "Data gagal diload");
            }
        });
        return humidities;
    }
}