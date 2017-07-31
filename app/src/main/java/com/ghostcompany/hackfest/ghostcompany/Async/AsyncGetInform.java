package com.ghostcompany.hackfest.ghostcompany.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ghostcompany.hackfest.ghostcompany.models.AdapterEntity;
import com.ghostcompany.hackfest.ghostcompany.models.Entity;
import com.ghostcompany.hackfest.ghostcompany.models.Informe;
import com.ghostcompany.hackfest.ghostcompany.models.OnGetEmpresaInfoCallback;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rabbit on 7/22/2017.
 */

public class AsyncGetInform extends AsyncTask<String, Void, List<Informe> > {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Context contexto;

    public AsyncGetInform(Context ctx) {

        this.contexto = ctx;
    }

    @Override
    protected List<Informe> doInBackground(String... params) {

        try {
            return getAllInformes();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void onPostExecute(List<Informe> infos) {
        Log.v("AsyncGetEmpresas", "Retorno do servidor");
        super.onPostExecute(infos);
        //   Toast.makeText(contexto, "teste", Toast.LENGTH_LONG).show();

        try {
            ((OnGetEmpresaInfoCallback) contexto).onGetEmpresaInfoCompleted(infos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Informe> getAllInformes() throws Exception {


        String result = "";
        String line = "";
        Gson gson = new Gson();


        String uri = "http://130.206.119.206:1026/v1/queryContext?limit=1000&details=on";
        String getAll = "{\"entities\": [{\"type\": \"Informe\",\"isPattern\": \"true\",\"id\": \".*\"}]}";
        OkHttpClient client = new OkHttpClient();
        try
        {
            RequestBody body = RequestBody.create(JSON, getAll);
            Request request = new Request.Builder()
                    .url(uri)
                    .post(body)
                    .addHeader("Accept","application/json")
                    .build();

            int executeCount = 0;
            Response response;

            do
            {
                response = client.newCall(request).execute();
                executeCount++;
            }
            while(response.code() == 408 && executeCount < 5);

            result = response.body().string();

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        List<Entity> contextElement = AdapterEntity.parseListEntity(result);
        List<Informe> informes = new ArrayList<Informe>();
        for (Entity entity : contextElement) {
            informes.add(AdapterEntity.toInforme(entity));
        }

        // TODO Auto-generated method stub
        return informes;
    }


}
