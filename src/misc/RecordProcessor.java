package misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class RecordProcessor {	
	private static Record[] records;
	private static StringBuffer outputString = new StringBuffer();
	private static Scanner scanner;
	private static int sumOfAges = 0;
	private static int numberOfCommissionEmployees = 0;
	private static double sumOfCommissions = 0;
	private static int numberOfHourlyEmployees = 0;
	private static double sumOfHourly = 0;
	private static int numberOfSalariedEmployees = 0;
	private static double sumOfSalary = 0;
	private static int numberOfRecords = 0;
	private static int recordNumber = 0;
	private static String[] parsedRecord;

	public static Scanner openScanner(String fileName){
		Scanner scanner;
		try {
			scanner = new Scanner(new File(fileName));
		} catch (FileNotFoundException err) {
			System.err.println(err.getMessage());
			return null;
		}
		return scanner;
	}
	
	public static void initializeRecords(String fileName){
		Scanner scanner = openScanner(fileName);
		findNumberOfRecords(scanner);
		
		records = new Record[numberOfRecords];
		for(int i = 0; i < numberOfRecords; i++)
			records[i] = new Record();
		scanner.close();
	}
	
	public static void findNumberOfRecords(Scanner scanner){
		numberOfRecords = 0;
		while (scanner.hasNextLine()) {
			String employeeRecord = scanner.nextLine();
			if (employeeRecord.length() > 0)
				numberOfRecords++;
		}
	}
	
	public static int createEmployeeRecord() throws Exception{
		while (scanner.hasNextLine()) {
			String recordString = scanner.nextLine();
			if (recordString.length() > 0) {
				int i = parseRecord(recordString);
				
				if(setEmployeeInfo(records[i], parsedRecord) == false)
					throw new Exception();
				
				recordNumber++;
			}
		}
		return recordNumber;
	}
	
	public static int parseRecord(String recordString){
		parsedRecord = recordString.split(",");

		int i = 0;
		for (; i < records.length; i++) {
			if (records[i].getLastName() == null)
				break;

			if (records[i].getLastName().compareTo(parsedRecord[1]) > 0) {
				pushRecordBack(i);
				break;
			}
		}
		return i;
	}
	
	public static void pushRecordBack(int i){
		for (int j = recordNumber; j > i; j--) {
			records[j].setFirstName(records[j - 1].getFirstName());
			records[j].setLastName(records[j - 1].getLastName());
			records[j].setAge(records[j - 1].getAge());
			records[j].setEmployeeType(records[j - 1].getEmployeeType());
			records[j].setPayRate(records[j - 1].getPayRate());
		}
	}
	
	public static boolean setEmployeeInfo(Record record, String[] parsedRecord){
		boolean isValidInput = true;
		
		record.setFirstName(parsedRecord[0]);
		record.setLastName(parsedRecord[1]);
		record.setEmployeeType(parsedRecord[3]);
		try {
			record.setAge(Integer.parseInt(parsedRecord[2]));
			record.setPayRate(Double.parseDouble(parsedRecord[4]));
		} catch (Exception err) {
			System.err.println(err.getMessage());
			scanner.close();
			isValidInput = false;
		}
		
		return isValidInput;
	}
	
	public static boolean noRecordsInFile(){
		if (numberOfRecords == 0) {
			System.err.println("No records found in data file");
			scanner.close();
			return true;
		}
		return false;
	}
	
	public static void printHeader(StringBuffer outputString){
		outputString.append(String.format("# of people imported: %d\n", records.length));

		outputString.append(String.format("\n%-30s %s  %-12s %12s\n", "Person Name", "Age", "Emp. Type", "Pay"));
		for (int i = 0; i < 30; i++)
			outputString.append(String.format("-"));
		outputString.append(String.format(" ---  "));
		for (int i = 0; i < 12; i++)
			outputString.append(String.format("-"));
		outputString.append(String.format(" "));
		for (int i = 0; i < 12; i++)
			outputString.append(String.format("-"));
		outputString.append(String.format("\n"));

		for (int i = 0; i < records.length; i++) {
			outputString.append(String.format("%-30s %-3d  %-12s $%12.2f\n", records[i].getFirstName() + " " + records[i].getLastName(), records[i].getAge(), records[i].getEmployeeType(), records[i].getPayRate()));
		}
	}
	
	public static void calculateSumAndNumber(){
		sumOfAges += records[recordNumber].getAge();
		if (records[recordNumber].getEmployeeType().equals("Commission")) {
			sumOfCommissions += records[recordNumber].getPayRate();
			numberOfCommissionEmployees++;
		} else if (records[recordNumber].getEmployeeType().equals("Hourly")) {
			sumOfHourly += records[recordNumber].getPayRate();
			numberOfHourlyEmployees++;
		} else if (records[recordNumber].getEmployeeType().equals("Salary")) {
			sumOfSalary += records[recordNumber].getPayRate();
			numberOfSalariedEmployees++;
		}
	}
	
	public static void printAverages(){
		float averageOfAges = (float) sumOfAges / records.length;
		outputString.append(String.format("\nAverage age:         %12.1f\n", averageOfAges));
		double averageOfCommissions = sumOfCommissions / numberOfCommissionEmployees;
		outputString.append(String.format("Average commission:  $%12.2f\n", averageOfCommissions));
		double averageOfHourly = sumOfHourly / numberOfHourlyEmployees;
		outputString.append(String.format("Average hourly wage: $%12.2f\n", averageOfHourly));
		double averageOfSalary = sumOfSalary / numberOfSalariedEmployees;
		outputString.append(String.format("Average salary:      $%12.2f\n", averageOfSalary));
	}
	
	public static void createHashMap(){
		HashMap<String, Integer> numberOfFirstNameOccurrences = new HashMap<String, Integer>();
		for (int i = 0; i < records.length; i++) {
			numberOfFirstNameOccurrences = addToHashMap(numberOfFirstNameOccurrences, records[i].getFirstName());
		}
		printNameOccurrence(numberOfFirstNameOccurrences, "first");

		HashMap<String, Integer> numberOfLastNameOccurrences = new HashMap<String, Integer>();
		for (int i = 0; i < records.length; i++) {
			numberOfLastNameOccurrences = addToHashMap(numberOfLastNameOccurrences, records[i].getLastName());
		}
		printNameOccurrence(numberOfLastNameOccurrences, "last");
	}
	
	public static HashMap<String, Integer> addToHashMap(HashMap<String, Integer> numberOfNameOccurrences, String name){
		if (numberOfNameOccurrences.containsKey(name)) {
			numberOfNameOccurrences.put(name, numberOfNameOccurrences.get(name) + 1);
		} else {
			numberOfNameOccurrences.put(name, 1);
		}
		return numberOfNameOccurrences;
	}
	
	public static void printNameOccurrence(HashMap<String, Integer> numberOfNameOccurrences, String typeOfName){
		String capsTypeOfName = Character.toUpperCase(typeOfName.charAt(0)) + typeOfName.substring(1);
		outputString.append(String.format("\n" + capsTypeOfName + " names with more than one person sharing it:\n"));
		if (sharedNameExists(numberOfNameOccurrences)) {
			countNameOccurrence(numberOfNameOccurrences);
		} else {
			outputString.append(String.format("All " + typeOfName + " names are unique"));
		}
	}

	public static boolean sharedNameExists(HashMap<String, Integer> numberOfNameOccurrences){
		boolean sharedName = false;
		for (int nameCount : numberOfNameOccurrences.values()) {
			if (nameCount > 1) {
				sharedName = true;
			}
		}
		return sharedName;
	}
	
	public static void countNameOccurrence(HashMap<String, Integer> numberOfNameOccurrences){
		Set<String> set = numberOfNameOccurrences.keySet();
		for (String str : set) {
			if (numberOfNameOccurrences.get(str) > 1) {
				outputString.append(String.format("%s, # people with this name: %d\n", str, numberOfNameOccurrences.get(str)));
			}
		}
	}
	
	public static String printEmployeesStats(String fileName) {
		scanner = openScanner(fileName);
		initializeRecords(fileName);

		try {
			recordNumber = createEmployeeRecord();
		} catch (Exception e) {
			return null;
		}
			
		if(noRecordsInFile())
			return null;

		printHeader(outputString);

		for (int i = 0; i < records.length; i++) {
			recordNumber = i;
			calculateSumAndNumber();
		}

		printAverages();
		createHashMap();
		scanner.close();
		return outputString.toString();
	}	
}

