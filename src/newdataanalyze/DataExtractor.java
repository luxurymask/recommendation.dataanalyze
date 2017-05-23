package newdataanalyze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class DataExtractor {
	private Task task;

	public DataExtractor(String graphMLFolderPath, String queryFilePath, String subTextFilePath) {
		task = new Task(graphMLFolderPath, queryFilePath, subTextFilePath);
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

		Map<String, Map<String, String>> queryClickQueryMap = task.getQueryClickQueryMap();
		Map<String, Map<String, String>> queryQueryClickMap = task.getQueryQueryClickMap();
		Map<String, Map<String, String>> queryQueryQueryMap = task.getQueryQueryQueryMap();

		// 写入query-click-query类型
		BufferedWriter queryClickQueryWriter;
		try {
			queryClickQueryWriter = new BufferedWriter(new FileWriter(queryClickQueryFile));
			for (Map.Entry<String, Map<String, String>> entry : queryClickQueryMap.entrySet()) {
				String query = entry.getKey();
				Map<String, String> clickQueryMap = entry.getValue();
				for (Map.Entry<String, String> entry2 : clickQueryMap.entrySet()) {
					String click = entry2.getKey();
					String query2 = entry2.getValue();
					queryClickQueryWriter.write(query + " - " + click + (query2.equals("") ? "" : " - ") + query2);
					queryClickQueryWriter.newLine();
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
			for(Map.Entry<String, Map<String, String>> entry : queryQueryClickMap.entrySet()){
				String query = entry.getKey();
				Map<String, String> queryClickMap = entry.getValue();
				for(Map.Entry<String, String> entry2 : queryClickMap.entrySet()){
					String query2 = entry2.getKey();
					String click = entry2.getValue();
					queryQueryClickWriter.write(query + " - " + query2 + (click.equals("") ? "" : " - ") + click);
					queryQueryClickWriter.newLine();
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
			for(Map.Entry<String, Map<String, String>> entry : queryQueryQueryMap.entrySet()){
				String query = entry.getKey();
				Map<String, String> queryQueryMap = entry.getValue();
				for(Map.Entry<String, String> entry2 : queryQueryMap.entrySet()){
					String query2 = entry2.getKey();
					String query3 = entry2.getValue();
					queryQueryQueryWriter.write(query + " - " + query2 + (query3.equals("") ? "" : " - ") + query3);
					queryQueryQueryWriter.newLine();
				}
			}
			queryQueryQueryWriter.flush();
			queryQueryQueryWriter.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void main(String[] args){
		DataExtractor extractorTask1 = new DataExtractor("/Users/liuxl/Desktop/recommendation/graphml/task1", "/Users/liuxl/Desktop/recommendation/input/task1.input", "/Users/liuxl/Desktop/recommendation/data/task1.txt");
		extractorTask1.extractData("/Users/liuxl/Desktop/recommendation/nodes");
	}
}
