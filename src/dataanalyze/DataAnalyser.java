package dataanalyze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataAnalyser {
	
	public static void main(String[] args) {
		List generalList = new ArrayList();
		File graphmlFolder = new File("/Users/liuxl/Desktop/graphml/task1");
		File[] xmlFiles = graphmlFolder.listFiles();
		for (File f : xmlFiles) {
			String path = f.getAbsolutePath();
			if (path.endsWith(".xml")) {
				generalList.add(DataExtracter.extractNode(path));
			}
		}

		File inputFile1 = new File("/Users/liuxl/Desktop/input/task1.input");

		String[] qInputFile1 = { "/Users/liuxl/Desktop/ori/qiushaojie/1.out", "/Users/liuxl/Desktop/ori/xinenhui/1.out",
				"/Users/liuxl/Desktop/ori/zhaohaitong/1.out", "/Users/liuxl/Desktop/ori/zhaomingqing/1.out" };
		QueryAnalyser.duplicateQueryExtract(qInputFile1);
		System.out.println();
		QueryAnalyser.subTextAnalyse("/Users/liuxl/Desktop/task1.txt");
		
		File outputFile1 = new File("/Users/liuxl/Desktop/input/task1.output");
		if(!outputFile1.exists()){
			try {
				outputFile1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile1));
			writer = new BufferedWriter(new FileWriter(outputFile1));
			String queryText = null;
			while ((queryText = reader.readLine()) != null) {
				ObjectMapper mapper = new ObjectMapper();
				// System.out.println(queryText);
				Map jsonMap = new HashMap();
				jsonMap.put("currentQuery", mapper.readValue(DataExtracter.json(queryText, generalList), Map.class));
				Map<String, Integer> textsMap = QueryAnalyser.getTexts(queryText);
				List l = new ArrayList();
				int flag = 0;
				for(Map.Entry<String, Integer> entry : textsMap.entrySet()){
					if(entry.getValue() <= 2){
						break;
					}
					flag = (flag == 0 ? entry.getValue() : flag);
					if(flag != entry.getValue()){ 
						jsonMap.put(flag, l);
						flag = entry.getValue();
						l = new ArrayList();
					}
					l.add(mapper.readValue(DataExtracter.json(entry.getKey(), generalList), Map.class));
				}
				jsonMap.put(flag, l);
				writer.write(mapper.writeValueAsString(jsonMap));
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
