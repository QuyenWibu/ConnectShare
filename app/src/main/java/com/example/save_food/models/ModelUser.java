package com.example.save_food.models;

public class ModelUser {
    String name, email, search, image, uid;


    public ModelUser() {
        //Empty constructor required by Firebase
    }

    public ModelUser(String name, String email, String search, String image, String uid){
        this.name = name;
        this.email = email;
        this.search = search;
        this.image = image;
        this.uid = uid;
    }


    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }


    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }

    public String getSearch(){
        return search;
    }
    public void setSearch(String search){
        this.search = search;
    }
    public String getUid(){
        return uid;
    }
    public void setUid(String uid){
        this.uid = uid;
    }
}
