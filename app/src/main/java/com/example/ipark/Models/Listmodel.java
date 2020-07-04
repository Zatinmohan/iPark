package com.example.ipark.Models;

public class Listmodel {
    private String name,library,link;

    public Listmodel(){}

    public Listmodel(String name, String library,String link){
        this.name = name;
        this.library = library;
        this.link = link;
    }

    public void setN(String name){this.name = name;}
    public String getN(){return name;}

    public void setL(String library){this.library = library;}
    public String getL(){return library;}

    public void setLink(String link){this.link = link;}
    public String getLink(){return link;}
}
