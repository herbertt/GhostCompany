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
                case "GPSCoord":
                    String[] tokensVal = att.getValue().split(",");
                    emp.setLat(tokensVal[0].trim());
                    emp.setLng(tokensVal[1].trim());
                    break;
                case "position":
                    String[] tokensVal2 = att.getValue().split(",");
                    emp.setLat(tokensVal2[0].trim());
                    emp.setLng(tokensVal2[1].trim());
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
    public static Informe toInforme(Entity e) throws ParseException {
        Informe info = new Informe();
        info.setId(e.getId());
        for (Attributes att : e.getAttributes()) {
            switch (att.getName()) {
                case "yesNoInfo":
                    info.setYesNoInfo(att.getValue());
                    break;
                case "cnpj":
                    info.setCnpj(att.getValue());
                    break;
                case "dataInforme":
                    Date date = null;
                    date = df.parse(att.getValue());
                    info.setDataInforme(date.toString());
                    break;
            }

        }
        return info;
    }
}
