package com.ghostcompany.hackfest.ghostcompany.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ghostcompany.hackfest.ghostcompany.models.AdapterEntity;
import com.ghostcompany.hackfest.ghostcompany.models.Attributes;
import com.ghostcompany.hackfest.ghostcompany.models.Entity;
import com.ghostcompany.hackfest.ghostcompany.models.OnPostEmpresaInfoCallback;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rabbit on 7/22/2017.
 */

public class AsyncSendInform extends AsyncTask<String, Void, String > {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String empresaCode;
    private String yesNoInfo;
    private Context contexto;
    private String idInfo;

    public AsyncSendInform(Context ctx) {

        this.contexto = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        idInfo = params[0];
        empresaCode = params[1];
        yesNoInfo = params[2];

        try {
            return postInfo(idInfo, empresaCode,yesNoInfo);
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
            ((OnPostEmpresaInfoCallback) contexto).onPostEmpresaInfoCompleted(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String postInfo(String idInfo, String empresaCode, String yesNoInfo) throws JSONException {
        String result = null;
        String idEntity = "";
        String[] parts = empresaCode.split(" ");
        String part = parts[1];
        idEntity = idInfo+part.replaceAll("[^a-zZ-Z1-9 ]", "");
        Entity entity = new Entity();
        List<Attributes> attributes = new ArrayList<>();
        attributes.add(new Attributes("dataInforme", "String", AdapterEntity.df.format(Calendar.getInstance().getTime()),null));
        attributes.add(new Attributes("cnpj", "String", part,null));
        attributes.add(new Attributes("yesNoInfo", "String", yesNoInfo,null));


        entity.setType("Informe");
        entity.setId(idEntity);
        entity.setAttributes(attributes);

        Gson gson = new Gson();
        String uri = "http://130.206.119.206:1026/v1/contextEntities/";
        uri += idEntity;

        try
        {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, gson.toJson(entity));
            Request request = new Request.Builder().url(uri).post(body).build();
            Response response;

            int executeCount = 0;
            do
            {
                response = client.newCall(request).execute();
                executeCount++;
            }
            while(response.code() == 408 && executeCount < 5);
            if( response.code()==200){
                result = String.valueOf(response.code());
            }

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }



}
