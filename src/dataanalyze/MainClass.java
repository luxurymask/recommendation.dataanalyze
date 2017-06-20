package dataanalyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MainClass {
	public static void main(String[] args){
		List generalList = new ArrayList();
		File graphmlFolder = new File("/Users/liuxl/Desktop/recommendation/graphml/task1");
		File[] xmlFiles = graphmlFolder.listFiles();
		for (File f : xmlFiles) {
			String path = f.getAbsolutePath();
			if (path.endsWith(".xml")) {
				generalList.add(DataExtracter.extractNode(path));
			}
		}
		
		File inputFile1 = new File("/Users/liuxl/Desktop/recommendation/input/task1.input");
//		File outputFile1 = new File("/Users/liuxl/Desktop/recommendation/output/task1.output");
//		if(!outputFile1.exists()){
//			try {
//				outputFile1.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
//		Map<String, Map<String, Float>> filteredMap = new HashMap<String, Map<String, Float>>();
//		Map<String, Map<String, Float>> tfIdfMap = new HashMap<String, Map<String, Float>>();
//		Map<String, Map<String, Float>> tfMap = new HashMap<String, Map<String, Float>>();
//		Map<String, Map<String, Float>> idfMap = new HashMap<String, Map<String, Float>>();
//		Map<String, Map<String, Integer>> freMap = new HashMap<String, Map<String, Integer>>();
//		
//		freMap = QueryAnalyser.wordsFrequency("/Users/liuxl/Desktop/recommendation/input/task1.input", "/Users/liuxl/Desktop/recommendation/data/task1.txt");
//		idfMap = QueryAnalyser.wordsIdf(freMap);
//		tfMap = QueryAnalyser.wordsTf(freMap);
//		try {
//			tfIdfMap = QueryAnalyser.wordsTfIdf(tfMap, idfMap);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//QueryAnalyser.filterStopwords(tfIdfMap, "/Users/liuxl/Desktop/recommendation/input/stopwords.txt");
		
//		Map<String, float[]> wordVectorMap = QueryAnalyser.wordVectors(tfIdfMap);
//		Map<String, Map<String, Double>> similarityMap = QueryAnalyser.wordsCosSimilarity(wordVectorMap);
//		Map<String, List<String>> similarQueryMap = new HashMap<String, List<String>>();
//		
//		for(Map.Entry<String, Map<String, Double>> entry : similarityMap.entrySet()){
//			List<String> list = new ArrayList<String>();
//			for(Map.Entry<String, Double> entry2 : entry.getValue().entrySet()){
//				if(entry2.getValue() >= 0.5){
//					list.add(entry2.getKey());
//				}
//			}
//			similarQueryMap.put(entry.getKey(), list);
//		}
		
		BufferedReader reader = null;
//		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile1));
//			writer = new BufferedWriter(new FileWriter(outputFile1));
			String queryText = null;
			while ((queryText = reader.readLine()) != null) {
				ObjectMapper mapper = new ObjectMapper();
				System.out.println(mapper.writeValueAsString(mapper.readValue(DataExtracter.json(queryText, generalList), Map.class)));
				// System.out.println(queryText);
//				Map jsonMap = new HashMap();
//				jsonMap.put("currentQuery", mapper.readValue(DataExtracter.json(queryText, generalList), Map.class));
//				List<String> similarList = similarQueryMap.get(queryText);
//				int i = 0;
//				for(String s : similarList){
//					jsonMap.put("top" + (++i), mapper.readValue(DataExtracter.json(s, generalList), Map.class));
//				}
//				writer.write(mapper.writeValueAsString(jsonMap));
//				writer.newLine();
			}
//			writer.flush();
//			writer.close();
		}catch(Exception e){
			
		}
	}
}
