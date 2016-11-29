============
Instructions
============
	This program reads and analyzes Indego bike share's stations and trips data. Please run IndegoTester.java and enter the file names of the trip and station files. The program will then ask you which question you would like to be answered. You will be able to input the arguments for the questions. Additionally, the program will keep prompting you for questions until you press "q".

============
Design
============
Original vs. Final

	Originally I only had three classes: StationReader, TripReader and IndegoTester. I had 2 different readers because I thought since the station and trip files are in different formats, their readers should work differently. Later I realized that I should use one file reader because for some questions, I need to make trip and station objects interact with each other. And using one file reader will make the interaction easier and make the code cleaner. And although the files are in different formats, they can both be splitted by comma and stored in an ArrayList, which is an instance variable of the file reader class.

	In my old design I did not have the Station or Trip classes, because I thought the file reader could simply create two ArrayLists of ArrayLists of Strings to store the data. But later I realized that there are a lot of columns in the files, i.e. a lot of properties for an object. So it would be better to design a class each for trip and station.

DataParser

	This class takes in a date in String and converts it into a Calendar object. Note that the dates from the trip file and the station file have different formats. So DateParser takes an additional parameter type, which indicates whether the date is from a trip file or a station file.

FileReader

	This class reads the trip and station files, stores the data into an ArrayList of Stations and an ArrayList of Trips, and answers all the questions. Its constructor initializes two empty ArrayLists. Then readFile(String filename, String type) is called. The "type" tells the file reader whether the file is a trips file or a stations file. After the constructor, each question is answered using one or two methods. The methods are designed to be able to take in user input as arguments.

IndegoTester

	This class tests the FileReader class and serves as user interface of the program. It first prompts user for the files to analyze. Then it creates a FileReader object to read the files. After that it goes into a loop to prompt user for a question. According to user input, it calls the corresponding method from FileReader and asks the user for arguments (if any). Finally, the program terminates if the user presses "q" when it asks for the question number.

Station

	The Station class has four public instance variables, each matching a column in the station table. I gave it public instance variables instead of making them private and providing getter methods, because I anticipated that the properties will be queried quite frequently in FileReader. Compared to getter methods, public instance variables will make the program faster and cleaner.

Trip

	The Trip class contains 14 public instance variables, each matching a column in the trip table. The reason for designing it this way is same as the Station class.
