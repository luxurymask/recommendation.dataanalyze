package newdataanalyze;

import java.util.HashMap;
import java.util.Map;

import dataanalyze.QueryAnalyser;

public class IdfCalculator {
	public static void idfCalculate(String queryTextPath, String subTextPath){
		Map<String, Map<String, Float>> idfMap = new HashMap<String, Map<String, Float>>();
		Map<String, Map<String, Integer>> freMap = new HashMap<String, Map<String, Integer>>();
		
		freMap = QueryAnalyser.wordsFrequency(queryTextPath, subTextPath);
		idfMap = QueryAnalyser.wordsIdf(freMap);

		try {
			Map<String, Float> subtextIdfMap = QueryAnalyser.subtextIdf(idfMap);
			Map<String, Float> subtextIdfMapSorted = QueryAnalyser.sortByValue7(subtextIdfMap);
			System.out.println(subtextIdfMapSorted.size());
			for(Map.Entry<String, Float> entry : subtextIdfMapSorted.entrySet()){
				System.out.println(entry.getKey() + "|" + entry.getValue());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		IdfCalculator.idfCalculate("/Users/liuxl/Desktop/recommendation/input/task3.input", "/Users/liuxl/Desktop/recommendation/data/task3.txt");
	}
}
