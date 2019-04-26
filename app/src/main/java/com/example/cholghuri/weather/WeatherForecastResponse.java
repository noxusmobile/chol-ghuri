package com.example.cholghuri.weather;

import java.util.List;

public class WeatherForecastResponse {

    public String cod;
    public double message ;
    public int cnt ;
    public List<Mylist> list ;
    public City city ;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<Mylist> getList() {
        return list;
    }

    public void setList(List<Mylist> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
