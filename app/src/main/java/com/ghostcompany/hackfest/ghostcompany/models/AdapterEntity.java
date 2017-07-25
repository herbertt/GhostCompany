package com.ghostcompany.hackfest.ghostcompany.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AdapterEntity {

    public static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static Entity parseEntity(String s) {
        Entity e = new Entity();
        Gson gson = new Gson();
        List<Attributes> lAtt = new ArrayList<Attributes>();
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            JSONObject structure = (JSONObject) jsonObject.get("contextElement");
            Type listType = new TypeToken<ArrayList<Attributes>>() {}.getType();
            lAtt =  gson.fromJson(structure.get("attributes").toString(), listType);

            e.setId(structure.get("id").toString());
            e.setType(structure.get("type").toString());
            e.setAttributes(lAtt);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;

    }



    public static List<Entity> parseListEntity(String s) throws Exception {
        List<Entity> listEntity = new ArrayList<Entity>();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(s.trim());
        JSONArray lang = (JSONArray) jsonObject.get("contextResponses");
        if(lang != null){
            Iterator i = lang.iterator();
            // take each value from the json array separately
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();
                if(innerObj != null)
                    listEntity.add(AdapterEntity.parseEntity(innerObj.toString()));
            }
        }
        return listEntity;

    }

    public static Empresa toEmpresa(Entity e) throws ParseException {
        Empresa emp = new Empresa();
        emp.setIdEmpresa(Long.parseLong(e.getId()));
        for (Attributes att : e.getAttributes()) {
            switch (att.getName()) {
                case "title":
                    emp.setTitle(att.getValue());
                    break;
              //  case "GPSCoord":
                case "position":
                    String[] tokensVal = att.getValue().split(",");
                    emp.setLat(tokensVal[0].trim());
                    emp.setLng(tokensVal[1].trim());
                    break;
                case "endereco":
                    emp.setEndereco(att.getValue());
                    break;
                case "dataOcorrencia":
                    Date date = null;
                    date = df.parse(att.getValue());
                    emp.setDataEmpresa(date);
                    break;
                /*
                case "userId":
                    User u = new User();
                    u.setId(Long.parseLong(att.getValue()));
                    o.setUser(u);
                    break;*/
                case "empresaCode":
                    emp.setEmpresaCode((int)Long.parseLong(att.getValue()));
            }

        }
        return emp;
    }
/*
    public static String toJsonEmpresa(Entity entity) throws ParseException {

        List<Attributes> attributes = new ArrayList<Attributes>();
        attributes.add(new Attributes("title", "String", "setar aqui o valor do Titulo", null));
        List<Metadata> metadatas = new ArrayList<Metadata>();
        metadatas.add(new Metadata("location", "String", "WGS84"));
        attributes.add(new Attributes("GPSCoord","coords", "setar aqui o valor do Latitude" + ", " + "setar aqui o valor do Longitude", metadatas));
        attributes.add(new Attributes("endereco", "String", "setar aqui o valor do Endereco", null));
        attributes.add(new Attributes("dataOcorrencia", "String",AdapterEntity.df.format(Calendar.getInstance().getTime()),null));
        attributes.add(new Attributes("userId", "String", "1",null));
        attributes.add(new Attributes("empresaCode", "String", "setar aqui o valor do CNPJ",null));

        entity.setType("Empresa");
        entity.setId("setar aqui o valor do ID");
        entity.setAttributes(attributes);

        Gson gson = new Gson();
       return gson.toJson(entity);

    }

    */
}
