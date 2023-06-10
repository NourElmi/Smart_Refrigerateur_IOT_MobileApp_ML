package com.example.contactapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Meal  implements Serializable {

    private String nomMeal;
    private String descMeal;
    private String timeNeeded;
    private String mealRate;
    private String ingredPrinci;
    private String nbrPersonne;
    private ArrayList<String> ingredient;
    private String img_url;

    public Meal(String nomMeal,String ingredPrinci, String descMeal,String timeNeeded,  String mealRate, String nbrPersonne, ArrayList ingredient, String img_url){
        this.nomMeal=nomMeal;
        this.ingredPrinci=ingredPrinci;
        this.descMeal=descMeal;
        this.img_url=img_url;
        this.timeNeeded=timeNeeded;
        this.ingredient=ingredient;
        this.mealRate=mealRate;
        this.nbrPersonne= nbrPersonne;
    }
    public Meal(String nomMeal, String descMeal,String timeNeeded,  String mealRate, String nbrPersonne, String img_url){
        this.nomMeal=nomMeal;
        this.descMeal=descMeal;
        this.img_url=img_url;
        this.timeNeeded=timeNeeded;
        this.mealRate=mealRate;
        this.nbrPersonne= nbrPersonne;
    }

    public void setNomMeal(String nomMeal) {
        this.nomMeal = nomMeal;
    }

    public void setDescMeal(String descMeal) {
        this.descMeal = descMeal;
    }

    public void setTimeNeeded(String timeNeeded) {
        this.timeNeeded = timeNeeded;
    }

    public void setMealRate(String mealRate) {
        this.mealRate = mealRate;
    }

    public void setNbrPersonne(String nbrPersonne) {
        this.nbrPersonne = nbrPersonne;
    }

    public void setIngredient(ArrayList ingredient) {
        this.ingredient = ingredient;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getNomMeal() {
        return nomMeal;
    }

    public String getDescMeal() {
        return descMeal;
    }

    public String getTimeNeeded() {
        return timeNeeded;
    }

    public String getMealRate() {
        return mealRate;
    }

    public String getNbrPersonne() {
        return nbrPersonne;
    }

    public ArrayList getIngredient() {
        return ingredient;
    }

    public String getImg_url() {
        return img_url;
    }
}