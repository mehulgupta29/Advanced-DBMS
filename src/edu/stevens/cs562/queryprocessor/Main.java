package edu.stevens.cs562.queryprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

	private Map<String, String> input;
	private static final String INPUT_FILE = "resources/input.properties";
	private Map<String, String> mf_structure_datatype;
	private DatabaseOperations dbop;

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		Main m = new Main();
		m.input = new TreeMap<String, String>();

		// Take user input
		System.out.print("User input from console[Y/N] : ");
		if (sc.hasNext() && sc.nextLine().equalsIgnoreCase("y")) {
			m.getConsoleUserInput(sc); //function to get user console input
		} else {
			/* SAMPLE File Paths
			 * 
			 * /Volumes/CS562/Guardians/resources/emf1.properties
			 * /Volumes/CS562/Guardians/resources/s4.properties
			 * /Volumes/CS562/Guardians/resources/input2.properties
			 */
			System.out.println("Enter the input file path: ");
			String fpath = sc.nextLine();
			m.readInputPropertyFile(fpath); // function that parses the input file and generates the Map <Key, value> input.
		}

		m.initiateMfStructureDataTypeMap(); // function reads the input map and generates the MfStructure
		
		// just for testing - printing the generated the mf-structure
		//m.printMap(m.mf_structure_datatype);
		
		
		try {
			m.generateMFStructureSource(); //function that generates the MfStructure and QP java files
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	/**
	 * This function reads from the input map and generates MfStructure.
	 */
	public void initiateMfStructureDataTypeMap() {
		dbop = new DatabaseOperations();

		mf_structure_datatype = new TreeMap<String, String>();

		//Process for Grouping attributes if any
		String[] gas = input.get(Constants.GROUPING_ATTRIBUTES).split(",");

		if (gas.length == 1 && gas[0].length() == 0) {

		} else {

			for (String gattr : gas) {

				System.out.println("gas = " + gattr);

				String result = dbop.getColumnDataType(gattr); //query the data base for column data type , if column exists in the data base
				System.out.println("result = " + result);

				if (result.contains("character"))
					mf_structure_datatype.put(gattr, "String");
				else if (result.contains("integer"))
					mf_structure_datatype.put(gattr, "int");

			}
		}

		//Process for F vector, if any
		String[] fvect_cols = input.get(Constants.F_VECT).split(",");
		if (fvect_cols.length == 1 && fvect_cols[0].length() == 0) {

		} else {
			for (String col : fvect_cols) {

				String substr = col.substring(0, 3);

				if (substr.equalsIgnoreCase("avg")) {

					String remstr = col.substring(3);

					if (!mf_structure_datatype.containsKey("sum" + remstr))
						mf_structure_datatype.put("sum" + remstr, "int");
					if (!mf_structure_datatype.containsKey("count" + remstr))
						mf_structure_datatype.put("count" + remstr, "int");
					if (!mf_structure_datatype.containsKey("avg" + remstr))
						mf_structure_datatype.put("avg" + remstr, "double");
				} else {
					if (!mf_structure_datatype.containsKey(col))
						mf_structure_datatype.put(col, "int");
				}
			}
		}

	}

	/**
	 * This function is used to get the user input and parse it to generate the map - input
	 * @param sc
	 */
	public void getConsoleUserInput(Scanner sc) {

		System.out.print("Enter Selection Attributes Seperated by ',' :\n");
		String value = "";
		value = sc.nextLine();
		input.put(Constants.SELECTION_ATTRIBUTES, value);

		System.out.print("Enter Where Condtion (if any)\n");
		value = sc.nextLine();
		input.put(Constants.WHERE_CLAUSE, value);

		System.out.print("Enter Grouping Attributes Seperated by ',' :\n");
		value = sc.nextLine();
		input.put(Constants.GROUPING_ATTRIBUTES, value);

		System.out.print("Enter Number Of Grouping Variables:\n");
		value = sc.nextLine();
		input.put(Constants.NO_OF_GRP_VAR, value);

		System.out.print("Enter F Vect Seperated by ',' :\n");
		value = sc.nextLine();
		input.put(Constants.F_VECT, value);

		System.out.print("Enter Condition Vect Seperated by ',' :\n");
		value = sc.nextLine();
		input.put(Constants.CONDITION_VECT, value);

		System.out.print("Enter Having Condtion (if any)\n");
		value = sc.nextLine();
		input.put(Constants.HAVING_CLAUSE, value);

	}

	/**
	 * This function reads the input properties file and creates a Map from it
	 * @param fpath
	 */
	public void readInputPropertyFile(String fpath) {
		Properties prop = new Properties();
		File file = new File(fpath);
		FileInputStream fi;
		try {
			fi = new FileInputStream(file); //create object for reading input file

			prop.load(fi); //load the file to properties obj - this identifies the keys and values from the input
			fi.close(); //close the file obj
			
			//loop for every key in the input file
			Enumeration<Object> enumKeys = prop.keys();
			while (enumKeys.hasMoreElements()) {
				String key = (String) enumKeys.nextElement();
				String value = prop.getProperty(key);
				System.out.println("Keys : " + key + " value: " + value);
				// put key value pair into a hash map.
				input.put(key, value); //add the key-value to Map - input
			}

		} catch (FileNotFoundException e) {
			System.out.println("Error in reading input file: " + e.getMessage());
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This function is used to print the generated input map
	 * @param mp
	 */
	public static void printMap(Map mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			// it.remove(); // avoids a ConcurrentModificationException
		}
	}

	/**
	 * This function is used to generate the output files
	 */
	public void generateMFStructureSource() {
		boolean isCreated = false;
		
		if (mf_structure_datatype != null) //condition to check if mf-structure is already generated by parsing the input
			isCreated = GenerateJavaSource.generateMfStructureClass(mf_structure_datatype); //function to generate the mf-structure output file

		if (isCreated) //condition to check if mf-structure output file is successfully created
			GenerateJavaSource.generateQPClass(input, mf_structure_datatype); //function to generate the QP output file
	}

}
