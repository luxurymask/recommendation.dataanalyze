package newdataanalyze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataExtractor {
	private Task task;

	public DataExtractor(String graphMLFolderPath, String queryFilePath, String subTextFilePath) {
		task = new Task(graphMLFolderPath, queryFilePath, subTextFilePath);
	}
	
	public Task getTask(){
		return this.task;
	}
	
	public void extractData(String outputFileFolderPath) {
		File queryClickQueryFile = new File(outputFileFolderPath + "/query-click-query.txt");
		File queryQueryClickFile = new File(outputFileFolderPath + "/query-query-click.txt");
		File queryQueryQueryFile = new File(outputFileFolderPath + "/query-query-query.txt");

		try {
			if (!queryClickQueryFile.exists()) {
				queryClickQueryFile.createNewFile();
			}

			if (!queryQueryClickFile.exists()) {
				queryQueryClickFile.createNewFile();
			}

			if (!queryQueryQueryFile.exists()) {
				queryQueryQueryFile.createNewFile();
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		Map<String, Map<String, List<String>>> queryClickQueryMap = task.getQueryClickQueryMap();
		Map<String, Map<String, List<String>>> queryQueryClickMap = task.getQueryQueryClickMap();
		Map<String, Map<String, List<String>>> queryQueryQueryMap = task.getQueryQueryQueryMap();

		// 写入query-click-query类型
		BufferedWriter queryClickQueryWriter;
		try {
			queryClickQueryWriter = new BufferedWriter(new FileWriter(queryClickQueryFile));
			for (Map.Entry<String, Map<String, List<String>>> entry : queryClickQueryMap.entrySet()) {
				String query = entry.getKey();
				Map<String, List<String>> clickQueryMap = entry.getValue();
				if(clickQueryMap == null){
					//TODO 叶子节点是query
					continue;
				}
				for (Map.Entry<String, List<String>> entry2 : clickQueryMap.entrySet()) {
					String click = entry2.getKey();
					List<String> queryList = entry2.getValue();
					if(queryList == null){
						queryClickQueryWriter.write(query + " - " + click);
						queryClickQueryWriter.newLine();
					}else{
						for(String query2 : queryList){
							queryClickQueryWriter.write(query + " - " + click + " - " + query2);
							queryClickQueryWriter.newLine();
						}
					}
				}
			}
			queryClickQueryWriter.flush();
			queryClickQueryWriter.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		//写入query-query-click类型
		BufferedWriter queryQueryClickWriter;
		try{
			queryQueryClickWriter = new BufferedWriter(new FileWriter(queryQueryClickFile));
			for(Map.Entry<String, Map<String, List<String>>> entry : queryQueryClickMap.entrySet()){
				String query = entry.getKey();
				Map<String, List<String>> queryClickMap = entry.getValue();
				if(queryClickMap == null){
					//TODO 叶子节点是query
					continue;
				}
				for(Map.Entry<String, List<String>> entry2 : queryClickMap.entrySet()){
					String query2 = entry2.getKey();
					List<String> clickList = entry2.getValue();
					if(clickList == null){
						queryQueryClickWriter.write(query + " - " + query2);
						queryQueryClickWriter.newLine();
					}else{
						for(String click : clickList){
							queryQueryClickWriter.write(query + " - " + query2 + " - " + click);
							queryQueryClickWriter.newLine();
						}
					}
				}
			}
			queryQueryClickWriter.flush();
			queryQueryClickWriter.close();
		}catch(Exception e){
			System.out.println(e);
		}
		
		//写入query-query-query类型
		BufferedWriter queryQueryQueryWriter;
		try{
			queryQueryQueryWriter = new BufferedWriter(new FileWriter(queryQueryQueryFile));
			for(Map.Entry<String, Map<String, List<String>>> entry : queryQueryQueryMap.entrySet()){
				String query = entry.getKey();
				Map<String, List<String>> queryQueryMap = entry.getValue();
				if(queryQueryMap == null){
					//TODO 叶子节点是query
					continue;
				}
				for(Map.Entry<String, List<String>> entry2 : queryQueryMap.entrySet()){
					String query2 = entry2.getKey();
					List<String> queryList = entry2.getValue();
					if(queryList == null){
						queryQueryQueryWriter.write(query + " - " + query2);
						queryQueryQueryWriter.newLine();
					}else{
						for(String query3 : queryList){
							queryQueryQueryWriter.write(query + " - " + query2 + " - " + query3);
							queryQueryQueryWriter.newLine();
						}
					}
				}
			}
			queryQueryQueryWriter.flush();
			queryQueryQueryWriter.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void main(String[] args){
//		DataExtractor extractorTask1 = new DataExtractor("/Users/liuxl/Desktop/recommendation/graphml/task1", "/Users/liuxl/Desktop/recommendation/input/task1.input", "/Users/liuxl/Desktop/recommendation/data/task1.txt");
//		extractorTask1.extractData("/Users/liuxl/Desktop/recommendation/nodes/task1");
//		
//
//		DataExtractor extractorTask2 = new DataExtractor("/Users/liuxl/Desktop/recommendation/graphml/task2", "/Users/liuxl/Desktop/recommendation/input/task2.input", "/Users/liuxl/Desktop/recommendation/data/task2.txt");
//		extractorTask2.extractData("/Users/liuxl/Desktop/recommendation/nodes/task2");
//		
//
//		DataExtractor extractorTask3 = new DataExtractor("/Users/liuxl/Desktop/recommendation/graphml/task3", "/Users/liuxl/Desktop/recommendation/input/task3.input", "/Users/liuxl/Desktop/recommendation/data/task3.txt");
//		extractorTask3.extractData("/Users/liuxl/Desktop/recommendation/nodes/task3");
//		
//
//		DataExtractor extractorTask4 = new DataExtractor("/Users/liuxl/Desktop/recommendation/graphml/task4", "/Users/liuxl/Desktop/recommendation/input/task4.input", "/Users/liuxl/Desktop/recommendation/data/task4.txt");
//		extractorTask4.extractData("/Users/liuxl/Desktop/recommendation/nodes/task4");
		
		DataExtractor extractorTask1 = new DataExtractor("/Users/liuxl/Desktop/毕业设计-not published/basic data/graphml/第二批/task1", "", "");
		extractorTask1.extractData("/Users/liuxl/Desktop/recommendation/nodes/第二批/task1");
		

		DataExtractor extractorTask2 = new DataExtractor("/Users/liuxl/Desktop/毕业设计-not published/basic data/graphml/第二批/task2", "", "");
		extractorTask2.extractData("/Users/liuxl/Desktop/recommendation/nodes/第二批/task2");
		
	}
}
