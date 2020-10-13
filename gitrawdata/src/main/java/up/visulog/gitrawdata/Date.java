package up.visulog.gitrawdata;

// This class is to make calculation on dates easier
public class Date {
	//TODO : add a method that return a boolean if 2 dates are in the same week
	
	private String[] dateList;
	private int day;
	private int month;
	private int year;
	private int hour;
	private int minute;
	private int seconds;
	private int gmt;
	
	public Date(String date) { //builder that receive a string which is a fieldcontent date from Commit; looks like : "Mon Aug 31 11:28:28 2020 +0200"
		dateList =date.split("\\s");
		this.day = Integer.parseInt(dateList[2]);
		this.month = stringToMonth(dateList[1]);
		this.year = Integer.parseInt(dateList[4]);
		String[] time = dateList[3].split(":");
		this.hour = Integer.parseInt(time[0]);
		this.minute = Integer.parseInt(time[1]);
		this.seconds = Integer.parseInt(time[2]);
		
		this.gmt = stringToGMT(dateList[5]);
	}
	
	int stringToMonth(String month){ //return the number of the month based on a string
		switch (month) {
			case "Jan":
				return 1;
			case "Feb":
				return 2;
			case "Mar":
				return 3;
			case "Apr":
				return 4;
			case "May":
				return 5;
			case "Jun":
				return 6;
			case "Jul":
				return 7;
			case "Aug":
				return 8;
			case "Sep":
				return 9;
			case "Oct":
				return 10;
			case "Nov":
				return 11;
			case "Dec":
				return 12;
			default :
				return 0;
		}
	}
	
	int stringToGMT(String s) { // return the number of the GMT based on a string
		if (s.charAt(0) == '+') {
			if (s.charAt(1) == 0) {
				return Integer.parseInt(s.substring(2,3));
			} else {
				return Integer.parseInt(s.substring(1,3));
			}
			
		} else {
			if (s.charAt(1) == 0) {
				return -Integer.parseInt(s.substring(2,3));
			} else {
				return -Integer.parseInt(s.substring(1,3));
			}
		}
	}
	// getters :
	int getMonth() {
		return month;
	}
	int getYear() {
		return year;
	}
	int getDay() {
		return day;
	}
	int getHour() {
		return hour;
	}
	int getMinute() {
		return minute;
	}
	int getSeconds() {
		return seconds;
	}
	int getGMT() {
		return gmt;
	}
	
	public String toString() {
		String result = "";
		for(int i=0; i<dateList.length; i++) {
			result += dateList[i];
			result += " ";
		}
		return result;
	}
}