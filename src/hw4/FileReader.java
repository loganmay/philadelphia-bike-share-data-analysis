package hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class will read the Indego Bikeshare's trip data, and provide methods to answer the questions listed in homework.
 * @author Han Zhu
 *
 */
public class FileReader {
	private ArrayList<Station> stations;
	private ArrayList<Trip> trips;
	private int tripsTotal;
	private boolean isHeader;
	private DateParser dParser;
	
	/**
	 * Constructor. Initiates stations and trips to empty ArrayLists. Initiates tripsTotal to 0 for counting. Initiates isHeader to true in order to skip
	 * header when reading files. Creates a date parser.
	 */
	public FileReader() {
		stations = new ArrayList<Station>();
		trips = new ArrayList<Trip>();
		tripsTotal = 0;
		isHeader = true;
		dParser = new DateParser();
	}
	
	/**
	 * Read a file and parse the data. If it's a trips file, store the data in trips, which is an ArrayList of Trips. Each line in the file is wrapped as a 
	 * Trip object. If it's a stations file, store the data in stations, and ArrayList of Stations. Each line in the file is wrapped as a Station object.
	 * @param fileName
	 * @param type trips or stations
	 * @throws FileNotFoundException
	 */
	public void readFile(String fileName, String type) throws FileNotFoundException {
		File inputFile = new File(fileName);
		Scanner in = new Scanner(inputFile);
		
		while (in.hasNextLine()) {
			if (isHeader) {
				in.nextLine();
				isHeader = false;
				continue;
			}
			
			String line = in.nextLine();
			String[] data = line.split(",");
			
			if (type.equalsIgnoreCase("trip")) {
				Trip trip = new Trip(data);
				trips.add(trip);
				tripsTotal++;
			} else if (type.equalsIgnoreCase("station")) {
				Station station = new Station(data);
				stations.add(station);
			}
		}
		
		isHeader = true;
		in.close();
	}
	
//	1
	/**
	 * Answers question #1. Traverse trips and return the count of trips that satisfy the criteria.
	 * @param type passholder type
	 * @param year the year when the trips took place
	 * @return number of trips
	 */
	public int countTripsByPassholderType(String type, int year) {
		int count = 0;
		
		for (Trip t : trips) {			
			if (t.startTime.get(Calendar.YEAR) == year && t.passholderType.equalsIgnoreCase(type)) {
				count++;
			}
		}
		
		return count;
	}
	
//	2
	/**
	 * Answers question #2. Traverse stations and return the count of stations that satisfy the criteria.
	 * @param status active / not active
	 * @param year the year the station went live
	 * @return number of stations
	 */
	public int countStationsByStatus(String status, int year) {
		int count = 0;
		
		for (Station s : stations) {
			if (s.goLiveDate.get(Calendar.YEAR) == year && s.status.equalsIgnoreCase(status)) {
				count++;
			}
		}
		
		return count;
	}
	
//	3
	/**
	 * Answers question #3. Convert station name into station id, and search for trips that started at the station.
	 * @param stationName
	 * @return the percentage of trips that started at the station in all trips.
	 */
	public double calculateTripPercentageByStartStation(String stationName) {
		String stationId = null;
		int count = 0;
		
		for (Station s : stations) {
			if (s.stationName.equalsIgnoreCase(stationName)) {
				stationId = s.stationId;
			}
		}
		
		for (Trip t : trips) {
			if (t.startStationId.equals(stationId)) {
				count++;
			}
		}
		
		return (double) count / tripsTotal;
	}
	
//	4
	/**
	 * Answers question #4. In the trips carried by a certain passholder type, calculate the percentage of one way or round trips. This method traverses all 
	 * trips and keep track of the number of trips that satisfy the criteria.
	 * @param passholderType
	 * @param tripRouteCategory one way / round trip
	 * @return the percentage of one way / round trips in all trips by the given passholder type
	 */
	public double calcTripPercentageByTripCategoryWithinSubtype(String passholderType, String tripRouteCategory) {
		int typeTotal = 0;
		int count = 0;
		
		for (Trip t : trips) {
			if (t.passholderType.equalsIgnoreCase(passholderType)) {
				if (t.tripRouteCategory.equalsIgnoreCase(tripRouteCategory)) {
					count++;
				}
				typeTotal++;
			}
		}
		
		return (double) count / typeTotal;
	}
	
//	5
	/**
	 * Answers question #5. Use a HashMap to map bike id against its usage. Traverse the map and get the most used bike.
	 * @return id of the bike with the longest duration
	 */
	public String getBikeWithLongestDuration() {
		HashMap<String, Long> map = new HashMap<String, Long>();
		
		for (Trip t : trips) {
			Long duration = t.endTime.getTimeInMillis() - t.startTime.getTimeInMillis();
			
			if (!map.containsKey(t.bikeId)) {
				map.put(t.bikeId, duration);
			} else {
				map.put(t.bikeId, map.get(t.bikeId) + duration);
			}
		}
		
		String theBike = null;
		long max = 0;
		
		for (String bike : map.keySet()) {
			if (map.get(bike) > max) {
				theBike = bike;
				max = map.get(bike);
			}
		}
		
		return theBike;
	}
	
//	6
	/**
	 * Answers question #6. Take user inputs of date and time separately. Concatenate them and parse it using DateParser. Traverse all trips to find the trips
	 * that have start time earlier than input and end time later than input.
	 * @param date mm/dd/yyyy
	 * @param time hh:mm in 24-hour format
	 * @return the number of trips that meet the criteria
	 */
	public int countActiveBikesAtSpecificTime(String date, String time) {
		int count = 0;
		
		StringBuilder sb = new StringBuilder(date);
		sb.append(' ');
		sb.append(time);
		
		try {
			Calendar inputTime = dParser.parseDate(sb.toString(), "trip");
			
			for (Trip t : trips) {
				if (t.startTime.getTimeInMillis() <= inputTime.getTimeInMillis() && inputTime.getTimeInMillis() <= t.endTime.getTimeInMillis()) {
					count++;
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
//	7
	/**
	 * Answers question #7. Assume the trips longitude and latitude are coordinates of a 2d point, get the distance of the trips by calculating its Euclidean
	 * distance between start and end locations. Ignore the trips that involved "Virtual Station". 
	 * @return the longest trip by distance
	 */
	public Trip getLongestTripByDistance() {
		Trip longest = new Trip();
		double max = 0;
		
		for (Trip t : trips) {
			if (t.endLat == 0 || t.endLon == 0 || t.startLat == 0 || t.startLon == 0) {
				continue;
			}
			
			double side1 = Math.abs(t.endLat - t.startLat);
			double side2 = Math.abs(t.endLon - t.startLon);
			double distance = Math.sqrt(side1 * side1 + side2 * side2);
			
			if (distance > max) {
				longest = t;
				max = distance;
			}
		}
		
		return longest;
	}
	
//	8
	/**
	 * Answers question #8. Map a go-live date with its count. If there is a go-live date with a single count, find the corresponding station and add it to an
	 * ArrayList of ids of stations.
	 * @return ids of stations that went live alone
	 */
	public ArrayList<String> getUniqueStationIdsByGoLiveDate() {
		HashMap<Calendar, Integer> map = new HashMap<Calendar, Integer>();
		
		for (Station s : stations) {
			if (map.containsKey(s.goLiveDate)) {
				map.put(s.goLiveDate, 0);
			} else {
				map.put(s.goLiveDate, 1);
			}
		}
		
		ArrayList<Calendar> uniqueDates = new ArrayList<Calendar>();
		
		for (Calendar c : map.keySet()) {
			if (map.get(c) == 1) {
				uniqueDates.add(c);
			}
		}
		
		ArrayList<String> stationIds = new ArrayList<String>();
		
		for (Station s : stations) {
			if (uniqueDates.contains(s.goLiveDate)) {
				stationIds.add(s.stationId);
			}
		}
		
		return stationIds;
	}
	
	/**
	 * Take a list of ids of stations, traverse all trips and find the trips that involve these stations.
	 * @param stationIds
	 * @return trips that involve the given stations
	 */
	public ArrayList<String> getTripIdsWithUniqueStations(ArrayList<String> stationIds) {
		ArrayList<String> tripIds = new ArrayList<String>();
		
		for (Trip t : trips) {
			if (stationIds.contains(t.startStationId) || stationIds.contains(t.endStationId)) {
				tripIds.add(t.tripId);
			}
		}
		
		return tripIds;
	}
	
//	9
	/**
	 * Answers question #9. Map a bike id with the number of trips with the bike. 
	 * @return the id of the bike that was used the most times
	 */
	public String getMostPopularBike() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String popularBike = null;
		int max = 0;
		
		for (Trip t : trips) {
			if (map.containsKey(t.bikeId)) {
				map.put(t.bikeId, map.get(t.bikeId) + 1);
			} else {
				map.put(t.bikeId, 1);
			}
		}
		
		for (String s : map.keySet()) {
			if (map.get(s) > max) {
				popularBike = s;
				max = map.get(s);
			}
		}
		
		return popularBike;
	}
	
	/**
	 * Map a bike id with the number of trips with the bike. 
	 * @param bikeId
	 * @return the number of trips with the given bike id
	 */
	public int getBikeUsage(String bikeId) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		for (Trip t : trips) {
			if (map.containsKey(t.bikeId)) {
				map.put(t.bikeId, map.get(t.bikeId) + 1);
			} else {
				map.put(t.bikeId, 1);
			}
		}
		
		return map.get(bikeId);
	}
	
//	ec1
	/**
	 * Answers extra credit question #1. Check if a station is close to another.
	 * @param t a trip object which contains start and end stations
	 * @param threshold the threshold for closeness
	 * @return if the stations are close
	 */
	public boolean isClose(Trip t, double threshold) {
		double latDiff = Math.abs(t.endLat - t.startLat);
		double lonDiff = Math.abs(t.endLon - t.startLon);
		
		if ((latDiff + lonDiff) / 2 < threshold) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Traverse all trips and check whether its start station is close to its end station. Ignore round trips.
	 * @param threshold the threshold for closeness
	 * @return a HashMap that maps a station to a list of its close stations
	 */
	public HashMap<String, ArrayList<String>> getCloseStations(double threshold) {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> iteratedStart = new ArrayList<String>();
		ArrayList<String> iteratedEnd = new ArrayList<String>();
		
		for (Trip t : trips) {
			if (t.startStationId.equals(t.endStationId)) {
				continue;
			} else if ((iteratedStart.contains(t.startStationId) && iteratedEnd.contains(t.endStationId)) ||
					(iteratedStart.contains(t.endStationId) && iteratedEnd.contains(t.startStationId))) {
				continue;
			}
			
			if (isClose(t, threshold)) {
				if (map.containsKey(t.startStationId)) {
					map.get(t.startStationId).add(t.endStationId);
					
					if (map.containsKey(t.endStationId)) {
						map.get(t.endStationId).add(t.startStationId);
					} else {
						ArrayList<String> list = new ArrayList<String>();
						list.add(t.startStationId);
						map.put(t.endStationId, list);
					}
				} else {
					ArrayList<String> list = new ArrayList<String>();
					list.add(t.endStationId);
					map.put(t.startStationId, list);
					
					if (map.containsKey(t.endStationId)) {
						map.get(t.endStationId).add(t.startStationId);
					} else {
						ArrayList<String> list2 = new ArrayList<String>();
						list2.add(t.startStationId);
						map.put(t.endStationId, list2);
					}
				}
			}
			
			iteratedStart.add(t.startStationId);
			iteratedEnd.add(t.endStationId);
		}
		
		return map;
	}
	
//	ec2
	/**
	 * Answers extra credit question #2. Map a station with the number of trips that involve the station. The number of trips that involve the station
	 * implies the station's popularity. Ignore trips with "Virtual Station".
	 * @param popularity least / most popular
	 * @param stationType start / end station
	 * @return the id of the station that meets the criteria
	 */
	public String getStationByPopularity(String popularity, String stationType) {
		HashMap<String, Integer> startMap = new HashMap<String, Integer>();
		HashMap<String, Integer> endMap = new HashMap<String, Integer>();
		int startMin = Integer.MAX_VALUE;
		int startMax = 0;
		int endMin = Integer.MAX_VALUE;
		int endMax = 0;
		String leastStart = null;
		String mostStart = null;
		String leastEnd = null;
		String mostEnd = null;

		for (Trip t : trips) {
			if (t.startStationId.equals("3000")) {
				
			} else if (startMap.containsKey(t.startStationId)) {
				startMap.put(t.startStationId, startMap.get(t.startStationId) + 1);
			} else {
				startMap.put(t.startStationId, 1);
			}
			
			if (t.endStationId.equals("3000")) {
				
			} else if (endMap.containsKey(t.endStationId)) {
				endMap.put(t.endStationId, endMap.get(t.endStationId) + 1);
			} else {
				endMap.put(t.endStationId, 1);
			}
		}
		
		for (String s : startMap.keySet()) {
			if (startMap.get(s) < startMin) {
				leastStart = s;
				startMin = startMap.get(s);
			}
			if (startMap.get(s) > startMax) {
				mostStart = s;
				startMax = startMap.get(s);
			}
		}
		
		for (String s : endMap.keySet()) {
			if (endMap.get(s) < endMin) {
				leastEnd = s;
				endMin = endMap.get(s);
			}
			if (endMap.get(s) > endMax) {
				mostEnd = s;
				endMax = endMap.get(s);
			}
		}
		
		if (popularity.equalsIgnoreCase("least")) {
			if (stationType.equalsIgnoreCase("start")) {
				return getStationNameById(leastStart);
			} else if (stationType.equalsIgnoreCase("end")) {
				return getStationNameById(leastEnd);
			} else {
				System.out.println("Please enter a valid argument.");
			}
		} else if (popularity.equalsIgnoreCase("most")){
			if (stationType.equalsIgnoreCase("start")) {
				return getStationNameById(mostStart);
			} else if (stationType.equalsIgnoreCase("end")) {
				return getStationNameById(mostEnd);
			} else {
				System.out.println("Please enter a valid argument.");
			}
		} else {
			System.out.println("Please enter a valid argument.");
		}
		
		return null;
	}
	
	/**
	 * Get a station's name given its id.
	 * @param id id of the station
	 * @return name of the station
	 */
	public String getStationNameById(String id) {
		for (Station s : stations) {
			if (s.stationId.equals(id)) {
				return s.stationName;
			}
		}
		
		return null;
	}
	
//	ec3
	/**
	 * Answers extra credit question #3. Find the station with the largest difference in arrving and departing traffic (number of trips). Map a station with
	 * its trip difference: if departing trips > arriving trips, trip diff > 0 and vice versa. Traverse the map and print the station with the largest
	 * difference. 
	 */
	public void getStationWithLargestTrafficDiff() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		for (Trip t : trips) {
			if (t.startStationId.equals("3000")){
				
			} else if (map.containsKey(t.startStationId)) {
				map.put(t.startStationId, map.get(t.startStationId) + 1);
			} else {
				map.put(t.startStationId, 1);
			}
			
			if (t.endStationId.equals("3000")) {
				
			} else if (map.containsKey(t.endStationId)) {
				map.put(t.endStationId, map.get(t.endStationId) - 1);
			} else {
				map.put(t.endStationId, -1);
			}
		}
		
		int max = 0;
		int diff = 0;
		String id = null;
		
		for (String s : map.keySet()) {
			if (Math.abs(map.get(s)) > max) {
				id = s;
				max = Math.abs(map.get(s));
				diff = map.get(s);
			}
		}
		
		System.out.println("The station with the largest difference in arriving and departing traffic is " + getStationNameById(id) + ".");
		System.out.println(diff > 0 ? "It has " + diff + " more departing trips than arriving trips." : 
			"It has " + Math.abs(diff) + " more arriving trips than departing trips.");
	}
}
