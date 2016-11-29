package hw4;

import java.text.ParseException;
import java.util.Calendar;

/**
 * This class represents an Indego bike station.
 * @author Han Zhu
 *
 */
public class Station {
	public String stationId;
	public String stationName;
	public Calendar goLiveDate;
	public String status;
	private DateParser dParser;
	
	/**
	 * Constructor. Used when initiating an empty station object.
	 */
	public Station() {
		
	}
	
	/**
	 * Constructor. Used to create a station that represents an entry in the station table.
	 * @param data a String array parsed by the FileReader
	 */
	public Station(String[] data) {
		stationId = data[0];
		dParser = new DateParser();
		
		try {
			stationName = data[1];
			goLiveDate = dParser.parseDate(data[2], "station");
			status = data[3];
		} catch (ParseException e) {
			StringBuilder sb = new StringBuilder(data[1]);
			sb.append(data[2]);
			
			data[1] = sb.toString();
			stationName = data[1];
			
			data[2] = data[3];
			try {
				goLiveDate = dParser.parseDate(data[2], "station");
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			data[3] = data[4];
			status = data[3];
		}
	}
}
