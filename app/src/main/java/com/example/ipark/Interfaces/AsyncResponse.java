package com.example.ipark.Interfaces;


import com.example.ipark.Models.Vehicle;

public interface AsyncResponse
{
    void processFinish(Vehicle vehicle, int statuscode);
}
