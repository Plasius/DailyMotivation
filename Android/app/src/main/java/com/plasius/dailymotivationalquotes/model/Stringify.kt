package com.plasius.dailymotivationalquotes.model

class Stringify (val field :String, val value :String){
    fun getJson() : String{
        return "{ \"$field\" : \"$value\"}";
    }
}