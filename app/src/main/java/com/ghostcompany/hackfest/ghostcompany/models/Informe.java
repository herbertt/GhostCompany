package com.ghostcompany.hackfest.ghostcompany.models;

/**
 * Created by Rabbit on 7/30/2017.
 */

public class Informe {
    private String id;
    private String cnpj;
    private String yesNoInfo;
    private String dataInforme;

    public Informe() {
    }

    public Informe(String id, String cnpj, String yesNoInfo, String dataInforme) {
        this.id = id;
        this.cnpj = cnpj;
        this.yesNoInfo = yesNoInfo;
        this.dataInforme = dataInforme;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getYesNoInfo() {
        return yesNoInfo;
    }

    public void setYesNoInfo(String yesNoInfo) {
        this.yesNoInfo = yesNoInfo;
    }

    public String getDataInforme() {
        return dataInforme;
    }

    public void setDataInforme(String dataInforme) {
        this.dataInforme = dataInforme;
    }
}
