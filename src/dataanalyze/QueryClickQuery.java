package dataanalyze;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QueryClickQuery {
	public static void printQueryClickQuery(List<List<SearchNode>> generalList){
		int count = 0;
		for(List<SearchNode> list : generalList){
			for(SearchNode node : list){
				if(node.getType().equals(SearchNode.QUERY)){
					List<String> nextIds = node.getNextList();
					for(String nextId : nextIds){
						SearchNode nextNode = DataExtracter.searchNodeFromId(nextId, list);
						if(nextNode.getType().equals(SearchNode.CLICK)){
							count++;
							List<String> nextNextIds = nextNode.getNextList();
							for(String nextNextId : nextNextIds){
								SearchNode nextNextNode = DataExtracter.searchNodeFromId(nextNextId, list);
								if(nextNextNode.getType().equals(SearchNode.QUERY)){
									System.out.println(node.getNodeString() + " | " + nextNode.getNodeString() + " | " + nextNextNode.getNodeString());
								}
							}
						}
					}
				}
			}
		}
		System.out.println(count);
	}
	
	/**
	 * 
	 * @param xmlPath 如："/Users/liuxl/Desktop/recommendation/graphml/task1"
	 * @return
	 */
	public static List<List<SearchNode>> getGeneralList(String xmlFolderPath){
		List<List<SearchNode>> generalList = new ArrayList<List<SearchNode>>();
		File graphmlFolder = new File(xmlFolderPath);
		File[] xmlFiles = graphmlFolder.listFiles();
		for (File f : xmlFiles) {
			String path = f.getAbsolutePath();
			if (path.endsWith(".xml")) {
				generalList.add(DataExtracter.extractNode(path));
			}
		}
		return generalList;
	}
	
	public static void main(String[] args){
		List<List<SearchNode>> generalListTask1 = getGeneralList("/Users/liuxl/Desktop/recommendation/graphml/task1");
		List<List<SearchNode>> generalListTask2 = getGeneralList("/Users/liuxl/Desktop/recommendation/graphml/task2");
		List<List<SearchNode>> generalListTask3 = getGeneralList("/Users/liuxl/Desktop/recommendation/graphml/task3");
		List<List<SearchNode>> generalListTask4 = getGeneralList("/Users/liuxl/Desktop/recommendation/graphml/task4");
		printQueryClickQuery(generalListTask4);
	}
}
