package zzzzzzzforexperiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QueryExtractor {
	public static void extractRandom10(String queryFilePath, String outputPath) {
		File inputFile = new File(queryFilePath);
		File outputFile = new File(outputPath);
		BufferedReader reader;
		BufferedWriter writer;
		List<String> queryList = new ArrayList<>();
		
		try {
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(outputFile));
			reader = new BufferedReader(new FileReader(inputFile));
			String content;
			while((content = reader.readLine()) != null){
				queryList.add(content);
			}
			String[] queries = queryList.toArray(new String[queryList.size()]);
			
			Random random = new Random();
	        int result[] = new int[10];
	        for (int i = 0; i < result.length; i++) {
	        	result[i] = random.nextInt(queries.length);
	            for (int j = 0; j < i; j++) {
	                if (result[i] == result[j]) {
	                    i--;
	                    break;
	                }
	            }
	        }
	        
	        for(int index : result){
	        	String query = queries[index];
	        	writer.write(query);
	        	writer.newLine();
	        }
	        writer.flush();
	        writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		String queryFilePath = "/Users/liuxl/Desktop/recommendation/for experiment/";
		String resultQueriesPath = "/Users/liuxl/Desktop/recommendation/for experiment/10queries/";
		File inputFolder = new File(queryFilePath);
		File[] inputFiles = inputFolder.listFiles();
		for (File f : inputFiles) {
			String path = f.getAbsolutePath();
			String name = f.getName();
			if (path.endsWith(".txt")) {
				QueryExtractor.extractRandom10(path, resultQueriesPath + name);
			}
		}
	}
}
