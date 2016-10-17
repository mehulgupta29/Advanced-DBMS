package edu.stevens.cs562.queryprocessor;

public class Util {
	
	/**
	 * Utility function to get the count of '_' in the variable name and thus distinguish between grouping attributes and grouping variables
	 * @param str1
	 * @param str2
	 * @return count
	 */
	public static int countMatches(String str1,String str2){
		int count = str1.length() - str1.replace(str2, "").length();
		return count;
	}

	/**
	 * Utility function to check if the input column is present in the grouping attribute list
	 * @param ga_arr
	 * @param col
	 * @return boolean
	 */
	public static boolean isExistInGroupingAttribute(String[] ga_arr, String col){
		
		for(String ga : ga_arr){
			if(ga.equalsIgnoreCase(col))
				return true;
		}
		return false;
	}
}
