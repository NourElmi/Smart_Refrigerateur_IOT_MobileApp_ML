
package com.example.contactapp.Model;

import java.io.Serializable;

public class Contact implements Serializable {

    private String nomContact;
//    private String prenomContact;
//    private String serviceContact;
//    private String emailContact;
//    private String Tel;
    private String img_url;

    public Contact(String nomContact, String img_url){
        this.nomContact=nomContact;
//        this.prenomContact=prenomContact;
        this.img_url=img_url;
//        this.serviceContact=serviceContact;
//        this.emailContact=emailContact;
//        this.Tel= Tel;
    }

    public String getNomContact() {
        return nomContact;
    }

    public void setNomContact(String nomContact) {
        this.nomContact = nomContact;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}