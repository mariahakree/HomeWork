package com.example.statisticsscreen;


import retrofit2.Call;
import java.util.Map;
import java.util.HashMap;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitAPI {
    //============top left pie - Activities=========

    @GET("http://10.0.2.2:8090/getactivitiesperweek/{number}")
     Call<HashMap<String, Integer>> getActivitiesInHoursPerWeek();

    @GET("http://10.0.2.2:8090/getactivitiespermonth/{number}")
    Call <HashMap<String, Integer>>getActivitiesInHoursPerMonth();

    @GET("http://10.0.2.2:8090/getactivitiesperyear/{number}")
    Call <HashMap<String, Integer>>getActivitiesInHoursPerYear();


    //=============bottom left pie kids=========
    @GET("http://10.0.2.2:8090/getallactivekidsbyweek/{number}")
    Call <HashMap<String, Integer>>getAllActiveKidsByWeek();

    @GET("http://10.0.2.2:8090/getallactivekidsbymonth/{number}")
    Call <HashMap<String, Integer>>getAllActiveKidsByMonth();

    @GET("http://10.0.2.2:8090/getallactivekidsbyyear/{number}")
    Call <HashMap<String, Integer>>getAllActiveKidsByYear();


    @GET("http://10.0.2.2:8090/getpercentactivekidsbyweek/{number}")
    Call <Integer> getPercentActiveKidsByWeek();

    @GET("http://10.0.2.2:8090/getpercentactivekidsbymonth/{number}")
    Call <Integer> getPercentActiveKidsPerMonth();

    @GET("http://10.0.2.2:8090/getpercentactivekidssbyyear/{number}")
    Call <Integer> getPercentActiveKidsByYear();

    //============= top right pie - parent ==========
    @GET("http://10.0.2.2:8090/getallactiveparentsbyweek/{number}")
    Call <HashMap<String, Integer>>getAllActiveParentsByWeek();

    @GET("http://10.0.2.2:8090/getallactiveparentsbymonth/{number}")
    Call <HashMap<String, Integer>>getAllActiveParentsByMonth();

    @GET("http://10.0.2.2:8090/getallactiveparentsbyyear/{number}")
    Call <HashMap<String, Integer>>getAllActiveParentsByYear();


    @GET("http://10.0.2.2:8090/getpercentactiveparentsbyweek/{number}")
    Call <Integer> getPercentActiveParentsByWeek();

    @GET("http://10.0.2.2:8090/getpercentactiveparentsbymonth/{number}")
    Call <Integer> getPercentActiveParentsByMonth();

    @GET("http://10.0.2.2:8090/getpercentactiveparentsbyyear/{number}")
    Call <Integer>getPercentActiveParentsByYear();


    //======== bottom right pie - category========

    @GET("http://10.0.2.2:8090/getlistofactivekidspercategory/{number}")
    Call <HashMap<String, Integer>>getActiveKidsOfCategory();
//=================== bar chart ===============================
  @GET("http://10.0.2.2:8090/getlistofactivekidspercategoryperyear/{number}")
      Call <HashMap<String,Integer[]>> getKidsByCategoriesPeryear();
    @GET("http://10.0.2.2:8090/getlistofactivekidspercategorypermonth/{number}")
    Call < HashMap<String,Integer[]>> getKidsByCategoriesPermonth();
    @GET("http://10.0.2.2:8090/getlistofactivekidspercategoryperweek/{number}")
    Call <HashMap<String,Integer[]>> getKidsByCategoriesPerweek();
}
