package com.example.cholghuri.Common;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static String convertUnixTodate(long dt){
        Date date= new Date(dt*1000L);
        SimpleDateFormat dateSDF= new SimpleDateFormat("dd MMM yyyy"+"\n"+"EEE HH:mm");

        String formateDate=dateSDF.format(date);
        return formateDate;

    }
}
