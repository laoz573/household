package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
 
    public static void main(String[] args) {
        try {
            String strDate = "2017/03/02 01:23:45";
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            
            Date date = sdFormat.parse(strDate);//String型からdate型に(parseがdate型)
            System.out.println("Date型 = " + date);
         
            String str = new SimpleDateFormat("yyyy/MM/dd").format(date);//date型からString型に(formatがString型)
            System.out.println("String型 = " + str);
         
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
 
}