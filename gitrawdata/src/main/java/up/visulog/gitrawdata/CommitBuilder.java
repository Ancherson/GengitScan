package up.visulog.gitrawdata;

import java.time.LocalDateTime;

public class CommitBuilder {
    private final String id;
    private String author;
    private LocalDateTime date;
    private String description;
    private String mergedFrom;

    public CommitBuilder(String id) {
        this.id = id;
    }

    public CommitBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public CommitBuilder setDate(String date) {
    	/* date is a String with expected value : "Mon Aug 31 11:28:28 2020 +0200"
    	 using java.time.LocalDateTime we can store a date with time in an object LocalDateTime*/
    	String[] dateList =date.split("\\s");
    	int day = Integer.parseInt(dateList[2]);
    	int month = stringToMonth(dateList[1]);
    	int year = Integer.parseInt(dateList[4]);
    	String[] time = dateList[3].split(":");
    	int hour = Integer.parseInt(time[0]);
    	int minute = Integer.parseInt(time[1]);
    	int seconds = Integer.parseInt(time[2]);
    	
    	LocalDateTime newDate = LocalDateTime.of(year, month, day, hour, minute, seconds);
    	
        this.date = newDate;
        return this;
    }
    
    int stringToMonth(String month){ //method that return the number of the month based on a string "Jan" -> January -> 1
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

    public CommitBuilder setDescription(String description) {		
 

        this.description = description;
        return this;
    }

    public CommitBuilder setMergedFrom(String mergedFrom) {
        this.mergedFrom = mergedFrom;
        return this;
    }

    public Commit createCommit() {
        return new Commit(id, author, date, description, mergedFrom);
    }
}