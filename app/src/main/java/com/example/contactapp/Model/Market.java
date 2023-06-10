package com.example.contactapp.Model;

public class Market {
    private String localisation ;
    private String Ouver ;
    private String Ferm ;
    ;
    private String nomProprietaire;

    private String urlMark;

    public Market(String nomProprietaire, String urlMark, String localisation, String Ouver, String Ferm  ) {

        this.nomProprietaire = nomProprietaire;
        this.urlMark = urlMark;
        this.localisation = localisation;
        this.Ouver = Ouver;
        this.Ferm = Ferm;
    }

    public String getOuver() {
        return Ouver;
    }

    public void setOuver(String ouver) {
        Ouver = ouver;
    }

    public String getFerm() {
        return Ferm;
    }

    public void setFerm(String ferm) {
        Ferm = ferm;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getNomProprietaire() {
        return nomProprietaire;
    }

    public void setNomProprietaire(String nomProprietaire) {
        this.nomProprietaire = nomProprietaire;
    }


    public String getUrlMark() {
        return urlMark;
    }

    public void setUrlMark(String urlMark) {
        this.urlMark = urlMark;
    }
}
