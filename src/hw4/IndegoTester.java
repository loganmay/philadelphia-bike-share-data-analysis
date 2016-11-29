package hw4;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class will create a FileReader, take user input with a Scanner, and test the methods of FileReader.
 * @author Han Zhu
 *
 */
public class IndegoTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter the name of the trip data file: ");
//		String tripFileName = "Q3_2016_trips.csv";
		String tripFileName = in.nextLine();
		System.out.println("Please enter the name of the station data file: ");
//		String stationFileName = "Station-Table.csv";
		String stationFileName = in.nextLine();
		
		try {
			FileReader fReader = new FileReader();
			System.out.println("Reading files...");
			fReader.readFile(tripFileName, "trip");
			fReader.readFile(stationFileName, "station");
			
			while (true) {
				System.out.println("====================" + '\n' +
						"Which question would you like to ask? Please choose one from 1 - 9 and ec1 - ec3. Press \"q\" to quit.");
				String question = in.nextLine();
				if (question.equalsIgnoreCase("q")) {
					break;
				}
				
				switch (question) {
	
				case "1":
					System.out.println("1. Count the number of trips by passholder type." + '\n' + "Please enter the passholder type: ");
					String type = in.nextLine();
					System.out.println("Please enter the year: ");
					int year1 = Integer.parseInt(in.nextLine());
					int count1 = fReader.countTripsByPassholderType(type, year1);
					System.out.println("There are " + count1 + " " + type + " trips in " + year1 + ".");
					break;
				
				case "2":
					System.out.println("2. Count the number of stations by go-live date and status." + '\n' + "Please enter the go-live year: ");
					int year2 = Integer.parseInt(in.nextLine());
					System.out.println("Please enter the status (active / not active): ");
					String status = in.nextLine();
					int count2 = fReader.countStationsByStatus(status, year2);
					System.out.println("There are " + count2 + " stations that are " + status + " and went live in " + year2 + ".");
					break;
				
				case "3":
					System.out.println("3. Calculate the percentage of trips by start station." + '\n' + "Please enter the station name: ");
					String stationName = in.nextLine();
					double percentage1 = fReader.calculateTripPercentageByStartStation(stationName) * 100;
					System.out.printf("%.2f%% of trips started in " + stationName + "." + '\n', percentage1);
					break;
	
				case "4":
					System.out.println("4. Calculate the percentage of trips by trip route category within trips made by a passholder type." + '\n' + 
							"Please enter the passholder type: ");
					String passholderType = in.nextLine();
					System.out.println("Please enter the trip route category (one way / round trip): ");
					String tripRouteCategory = in.nextLine();
					double percentage2 = fReader.calcTripPercentageByTripCategoryWithinSubtype(passholderType, tripRouteCategory) * 100;
					System.out.printf("%.2f%% of trips made by " + passholderType + " riders are " + tripRouteCategory + "." + '\n', percentage2);
					break;
	
				case "5":
					System.out.println("5. The bike that has traveled the most in terms of duration is bike #" + fReader.getBikeWithLongestDuration() + ".");
					break;
	
				case "6":
					System.out.println("6. Count how many bikes are being used at a specific time." + '\n' + "Please enter the date in \"mm/dd/yyyy\" format: ");
					String date = in.nextLine();
					System.out.println("Please enter the time in 24-hour format: ");
					String time = in.nextLine();
					System.out.println("There are " + fReader.countActiveBikesAtSpecificTime(date, time) + " bikes being used on " + date + " at " + time + ".");
					break;
	
				case "7":
					System.out.println("7. Print all trip information for the longest trip by distance: ");
					Trip longest = fReader.getLongestTripByDistance();
					System.out.println("	trip_id: 		" + longest.tripId);
					System.out.println("	duration:		" + longest.duration);
					System.out.println("	start_time:		" + longest.startTime.getTime());
					System.out.println("	end_time:		" + longest.endTime.getTime());
					System.out.println("	start_station_id:	" + longest.startStationId);
					System.out.println("	start_lat:		" + longest.startLat);
					System.out.println("	start_lon:		" + longest.startLon);
					System.out.println("	end_station_id:		" + longest.endStationId);
					System.out.println("	end_lat:		" + longest.endLat);
					System.out.println("	end_lon:		" + longest.endLon);
					System.out.println("	bike_id:		" + longest.bikeId);
					System.out.println("	plan_duration:		" + longest.planDuration);
					System.out.println("	trip_route_category:	" + longest.tripRouteCategory);
					System.out.println("	passholder_type:	" + longest.passholderType);
					break;
	
				case "8":
					System.out.println("8. Print ids of all trips that involved a station which was the only station to go live on its respective go-live date");
					System.out.println("The ids of the stations that satisfy this condition are: ");
					ArrayList<String> stationIds = fReader.getUniqueStationIdsByGoLiveDate();
					System.out.println(stationIds);
					ArrayList<String> tripIds = fReader.getTripIdsWithUniqueStations(stationIds);
					System.out.println("There are " + tripIds.size() + " such trips.");
//					while (true) {
//						if (in.nextLine().isEmpty()) {
//							for (String s : tripIds) {
//								System.out.println(s);
//							}
//							break;
//						}
//					}
					break;
	
				case "9":
					System.out.println("9. Find the most popular bike.");
					String popularBike = fReader.getMostPopularBike();
					int usage = fReader.getBikeUsage(popularBike);
					System.out.println("Bike #" + popularBike + " is the most popular bike of this period! It has been used " + usage + " times.");
					break;
	
				case "ec1":
					System.out.println("EC1. Find all pairs of stations that are considered close to each other." + '\n' + 
							"Please enter your threshold for closeness: ");
					double threshold = Double.parseDouble(in.nextLine());
					System.out.println("Press enter to see the list of close stations.");
					HashMap<String, ArrayList<String>> map = fReader.getCloseStations(threshold);
					while (true) {
						if (in.nextLine().isEmpty()) {
							for (String s : map.keySet()){
								System.out.println(s + ": " + map.get(s));
							}
							break;
						}
					}
					break;
					
				case "ec2":
					System.out.println("EC2. Find the least/most popular start/end station." + '\n' + "Please enter least or most: ");
					String popularity = in.nextLine();
					System.out.println("Please enter start or end: ");
					String stationType = in.nextLine();
					System.out.println("The " + popularity + " popular " + stationType + " station is " + 
							fReader.getStationByPopularity(popularity, stationType));
					break;
					
				case "ec3":
					System.out.println("EC3: Find the station with the largest difference in arriving and departing traffic.");
					fReader.getStationWithLargestTrafficDiff();
					break;
	
				default:
					System.out.println("Please enter a valid question number.");
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Please enter a valid file name.");
		}
		
		in.close();
	}

}
