package model;

import java.util.Date;
import java.text.SimpleDateFormat;

public class HHD  {//HouseHoldDataの略
    private Integer id;
    private String contents;
    private Date registerday;
    private int price;
    private String spending;
    private String income;
    private String category;
    private String remarks;
    private int userID;
    private int year;
    private int month;
    private int date;
    
    public HHD(Integer id,Date registerday, String contents,int price,String spending ,String income ,String category, String remarks, int userID) {
        this.id = id;
        this.registerday = registerday;
        this.contents = contents;
        this.price = price;
        this.spending = spending;
        this.income = income;
        this.remarks = remarks;
        this.category = category;
        this.userID = userID;
        this.year = registerday.getYear() + 1900;
        this.month = registerday.getMonth() + 1;
        this.date = registerday.getDate();
    }
    
    public int getId() {
    	return this.id;    	
    }
    
    public Date getRegisterday() {
    	return this.registerday;
    }
    
    
    public String getContents() {
    	return this.contents;
    }
    
    public int getPrice() {
    	return this.price;
    }
    
    public String getSpending() {
    	return this.spending;
    }
    
    public String getIncome() {
    	return this.income;
    }
    
    public String getRemarks() {
    	return this.remarks;
    }

    public String getCategory() {
    	return this.category;
    }
    
	public int getUserID() {
		return this.userID;
	}

    public int getYear() { 
        return this.year; 
    }
    
    public int getMonth() { 
        return this.month; 
    }
    
    public int getDate() { 
        return this.date; 
    }
}

