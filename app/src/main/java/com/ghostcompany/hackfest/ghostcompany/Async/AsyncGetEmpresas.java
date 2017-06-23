package com.ghostcompany.hackfest.ghostcompany.Async;

/**
 * Created by helicoptero on 11/06/2017.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ghostcompany.hackfest.ghostcompany.models.AdapterEntity;
import com.ghostcompany.hackfest.ghostcompany.models.Empresa;
import com.ghostcompany.hackfest.ghostcompany.models.Entity;
import com.ghostcompany.hackfest.ghostcompany.models.OnGetEmpresaCompletedCallback;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class AsyncGetEmpresas extends AsyncTask <String, Void, List<Empresa> > {

    private HashMap<String, String> markers; // marcadores das empresas
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    private Context contexto;
    public AsyncGetEmpresas(Context ctx) {
        this.contexto = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Empresa> doInBackground(String... params) {

        try {
            return getAllOccurrences();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(List<Empresa> empresas) {
        Log.v("AsyncGetEmpresas", "Retorno do servidor");
        super.onPostExecute(empresas);
        Toast.makeText(contexto, "teste", Toast.LENGTH_LONG).show();


        ((OnGetEmpresaCompletedCallback) contexto).onGetEmpresaCompleted(empresas);
    }


    public List<Empresa> getAllOccurrences() throws Exception {


        String result = "";
        String line = "";
        Gson gson = new Gson();


        String uri = "http://130.206.119.206:1026/v1/queryContext?limit=500&details=on";
        String getAll = "{\"entities\": [{\"type\": \"Ocurrence\",\"isPattern\": \"true\",\"id\": \".*\"}]}";
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
        List<Empresa> empresas = new ArrayList<Empresa>();
        for (Entity entity : contextElement) {
            empresas.add(AdapterEntity.toEmpresa(entity));
        }

        // TODO Auto-generated method stub
        return empresas;
    }

}
