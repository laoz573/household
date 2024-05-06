package model;

public class SetGetCal {

	//カレンダーの年
	private int year;
	//カレンダーの月
	private int month;
	//カレンダーの日
	private int date;

	/*setter & getter*/

	public int getYear() {
		return this.year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return this.month;
	}
	public void setMonth(int month) {
		this.month = month;
	}

	public void setDate(int date) {
		this.date = date;
	}
	
	public int getDate() {
		return this.date;
	}
}
