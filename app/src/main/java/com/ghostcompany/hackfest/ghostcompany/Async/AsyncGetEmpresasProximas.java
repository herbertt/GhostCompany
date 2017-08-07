package com.ghostcompany.hackfest.ghostcompany.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ghostcompany.hackfest.ghostcompany.models.OnGetEmpresaProximaCompletedCallback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.HashMap;

/**
 * Created by Rabbit on 7/22/2017.
 */

public class AsyncGetEmpresasProximas extends AsyncTask<String, Void, String > {

    private HashMap<String, String> markers; // marcadores das empresas
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String latitudeString;
    private String longitudeString;
    private Context contexto;

    public AsyncGetEmpresasProximas(Context ctx) {

        this.contexto = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        latitudeString = params[0];
        longitudeString = params[1];

        try {
            return requestNearCompany();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        Log.v("AsyncGetEmpresas", "Retorno do servidor");
        super.onPostExecute(s);
        //   Toast.makeText(contexto, "teste", Toast.LENGTH_LONG).show();


        try {
            ((OnGetEmpresaProximaCompletedCallback) contexto).onGetEmpresaProximaCompleted(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String requestNearCompany() throws Exception {

        String json = "";
        String result= "";


        BufferedReader rd;
        try {
            String uri = "http://130.206.119.206:1026/v1/queryContext";
            String getAll = "{\"entities\": [{\"type\": \"Empresa\",\"isPattern\": \"true\",\"id\": \".*\"}],\"restriction\": " +
                    "{\"scopes\": [{\"type\" : \"FIWARE::Location\",\"value\" : {\"circle\": {\"centerLatitude\": \"" +
                    latitudeString +"\",\"centerLongitude\": \"" +longitudeString +"\",\"radius\": \"25\"}}}]}}";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, getAll);
            Request request = new Request.Builder()
                    .url(uri)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();

            Response response;

            int executeCount = 0;
            do
            {
                response = client.newCall(request).execute();
                executeCount++;
            }
            while(response.code() == 408 && executeCount < 5);

            result = response.body().string();
            json = new JSONObject(result).toString();

        } catch (Exception e) {

            e.printStackTrace();
        }
        Thread.sleep(500);
        return json;
    }


}
