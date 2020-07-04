package com.example.ipark.Models;

public class Vehicle {
    public String a,b,c,d,e,f,g;
    private static String reg_number;   //
    private static String reg_date;     //
    private static String chassis;      //
    private static String engine;       //
    private static String vehicle_class;//
    private static String fuel_type;    //
    private static String Maker_model;  //
    private static String fitness;
    private static String insurence;
    private static String fuel_norms;
    private static String road_tax;



    private static String owner;
//    private String location;
//    private String expiry;
//    private final String NOT_AVAILABLE = "n/a";

    public Vehicle() {}

    public Vehicle(String reg_number,String reg_date,String chassis, String engine, String owner, String vehicle_class, String Maker_model){
        a = reg_number;
        b = reg_date;
        c = chassis;
        d = engine;
        e = owner;
        f = vehicle_class;
        g = Maker_model;
    }

    public Vehicle(String reg_number, String reg_date, String chassis, String engine, String owner, String vehicle_class, String fuel_type, String Maker_model, String fitness, String insurence, String fuel_norms, String road_tax)
    {
        this.reg_number = reg_number;
        this.reg_date = reg_date;
        this.chassis = chassis;
        this.engine = engine;
        this.owner = owner;
        this.fuel_type=fuel_type;
        this.vehicle_class = vehicle_class;
        this.Maker_model = Maker_model;
        this.fitness = fitness;
        this.insurence = insurence;
        this.fuel_norms = fuel_norms;
        this.road_tax = road_tax;
    }

    public String getA(){return a;}
    public String getB(){return b;}
    public String getC(){return c;}
    public String getD(){return d;}
    public String getE(){return e;}
    public String getF(){return f;}

    public String getReg_number() {
        return reg_number;
    }
    public void setReg_number(String reg_number){this.reg_number = reg_number;}

    public String getReg_date() {
        return reg_date;
    }
    public void setReg_date(String reg_date){this.reg_date = reg_date;}

    public String getChassis() {
        return chassis;
    }
    public void setChassis(String chassis){this.chassis = chassis;}

    public String getEngine() {
        return engine;
    }
    public void setEngine(String engine){ this.engine = engine;}

    public String getVehicle_class() {
        return vehicle_class;
    }
    public void setVehicle_class(String vehicle_class){this.vehicle_class = vehicle_class;}

    public String getFuel_type() {
        return fuel_type;
    }
    public void setFuel_type(String fuel_type){ this.fuel_type = fuel_type;}

    public String getMaker_model() {
        return Maker_model;
    }
    public void setMaker_model(String Maker_model){this.Maker_model = Maker_model;}

    public String getFitness() {
        return fitness;
    }
    public void setFitness(String fitness){this.fitness = fitness;}

    public String getInsurence() {
        return insurence;
    }
    public void setInsurence(String insurence){this.insurence = insurence;}

    public String getFuel_norms() {
        return fuel_norms;
    }
    public void setFuel_norms(String fuel_norms){this.fuel_norms = fuel_norms;}

    public String getRoad_tax() {
        return road_tax;
    }
    public void setRoad_Tax(String road_tax){this.road_tax = road_tax;}

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner){this.owner = owner;}

}
