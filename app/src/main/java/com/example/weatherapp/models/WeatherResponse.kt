package com.example.weatherapp.models

data class WeatherResponse(
    val coord : Coord,
    val weather : List<Weather>,
    val base : String,
    val main : Main,
    val visibility : String,
    val wind : Wind,
    val clouds : Clouds,
    val dt : Int,
    val sys : Sys,
    val timezone : Int,
    val id : Int,
    val name : String,
    val cod : Int
){
    data class Coord(val lat: Double,val lon : Double)
    data class Clouds(val all: Int)
    data class Wind(val speed: Double,val deg : Int)
    data class  Weather(val id : Int,val main : String,val description : String,val icon : String)
    data class  Sys(val type :Int,val id : Int,val country : String,val sunrise : Int,val sunset : Int)
    data class  Main(val temp : Double,val feels_like : Double,val temp_min : Double,val temp_max : Double,val pressure : Int,val humidity : Int)
}