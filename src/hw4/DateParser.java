package hw4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class represents a date parser. It takes a String and parse it into a Calendar object. 
 * @author Han Zhu
 *
 */
public class DateParser {
	
	/**
	 * This method parses a String representation of a date depending on whether it's from the trips file or the stations file.
	 * @param s the String
	 * @param type trip or station
	 * @return a Calendar representation of the date.
	 * @throws ParseException
	 */
	public Calendar parseDate(String s, String type) throws ParseException {
		Date date = type.equalsIgnoreCase("trip") ? new SimpleDateFormat("M/d/yyyy H:m").parse(s) : new SimpleDateFormat("M/d/yyyy").parse(s);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		return cal;
	}
}
