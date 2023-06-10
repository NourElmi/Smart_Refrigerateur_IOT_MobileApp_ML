package com.example.contactapp.Model;

public class Ingredients {

    private String ingredName;
    private String url_img_Ingred;


    public Ingredients( String nomIngred, String url_img_Ingred){
        this.ingredName=nomIngred;
        this.url_img_Ingred=url_img_Ingred;
    }

    public String getNomIngred() {
        return ingredName;
    }

    public void setNomIngred(String nomIngred) {
        this.ingredName = nomIngred;
    }

    public String getUrl_img_Ingred() {
        return url_img_Ingred;
    }

    public void setUrl_img_Ingred(String url_img_Ingred) {
        this.url_img_Ingred = url_img_Ingred;
    }
}
