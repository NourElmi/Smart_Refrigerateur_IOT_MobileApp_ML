package com.example.contactapp.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String dateNaiss;
    private String email;
    private String Tel;
    private String nom;
    private String password;
    private String prenom;

    private String url;


    public User() {
        // Constructeur par défaut requis pour Firebase
    }

    public User(String dateNaiss, String email, String nom, String password, String prenom,String Tel,  String url) {
        this.dateNaiss = dateNaiss;
        this.email = email;
        this.nom = nom;
        this.Tel = Tel;
        this.password = password;
        this.prenom = prenom;
        this.url = url;
    }

    public String getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
