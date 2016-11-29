package hw4;

import java.text.ParseException;
import java.util.Calendar;

/**
 * This class represents a trip on an Indego bike.
 * @author Han Zhu
 *
 */
public class Trip {
	public String tripId;
	public int duration;
	public Calendar startTime;
	public Calendar endTime;
	public String startStationId;
	public double startLat;
	public double startLon;
	public String endStationId;
	public double endLat;
	public double endLon;
	public String bikeId;
	public int planDuration;
	public String tripRouteCategory;
	public String passholderType;
	private DateParser dParser;
	
	/**
	 * Constructor. Used when initiating an empty trip object.
	 */
	public Trip() {
		
	}
	
	/**
	 * Constructor. Used to create a trip that represents an entry in the trips table.
	 * @param data a String array parsed by FileReader
	 */
	public Trip(String[] data) {
		tripId = data[0];
		duration = Integer.parseInt(data[1]);
		dParser = new DateParser();
		
		try {
			startTime = dParser.parseDate(data[2], "trip");
			endTime = dParser.parseDate(data[3], "trip");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		startStationId = data[4];
		
		try {
			startLat = Double.parseDouble(data[5]);
			startLon = Double.parseDouble(data[6]);
		} catch (NumberFormatException nfe) {
			startLat = 0;
			startLon = 0;
		}
		
		try {
			endLat = Double.parseDouble(data[8]);
			endLon = Double.parseDouble(data[9]);
		} catch (NumberFormatException nfe) {
			endLat = 0;
			endLon = 0;
		}
		
		endStationId = data[7];
		bikeId = data[10];
		planDuration = Integer.parseInt(data[11]);
		tripRouteCategory = data[12];
		passholderType = data[13];
	}
}
