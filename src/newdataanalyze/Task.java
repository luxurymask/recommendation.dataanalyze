package newdataanalyze;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Task {
	/**
	 * graphML文件夹路径，例如：/Users/liuxl/Desktop/recommendation/graphml/task1
	 */
	private String graphMLFolderPath;
	/**
	 * 查询词文件路径，例如：/Users/liuxl/Desktop/recommendation/input/task1.input
	 */
	private String queryFilePath;
	
	/**
	 * 查询词分词文件路径，例如：/Users/liuxl/Desktop/recommendation/data/task1.txt
	 */
	private String subTextFilePath;
	
	private Map<String, Map<String, String>> queryClickQueryMap = new HashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> queryQueryClickMap = new HashMap<String, Map<String, String>>();
	private Map<String, Map<String, String>> queryQueryQueryMap = new HashMap<String, Map<String, String>>();
	
	public Task(String graphMLFolderPath, String queryFilePath, String subTextFilePath){
		this.graphMLFolderPath = graphMLFolderPath;
		this.queryFilePath = queryFilePath;
		this.subTextFilePath = subTextFilePath;
		
		File graphmlFolder = new File(graphMLFolderPath);
		File[] xmlFiles = graphmlFolder.listFiles();
		for (File f : xmlFiles) {
			String path = f.getAbsolutePath();
			if (path.endsWith(".xml")) {
				Map<String, String> preMap = new HashMap<String, String>();
				Map<String, List<String>> nextMap = new HashMap<String, List<String>>();
				Map<String, SearchNode> idNodeMap = new HashMap<String, SearchNode>();
				try{
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(f);
					
					//get preMap and nextMap
					NodeList edgeList = doc.getElementsByTagName("edge");
					
					for(int i = 0;i < edgeList.getLength();i++){
						Node edge = edgeList.item(i);
						if (edge instanceof Element) {
							String source = ((Element) edge).getAttribute("source");
							String target = ((Element) edge).getAttribute("target");
							preMap.put(target, source);
							List<String> nextList = nextMap.getOrDefault(source, new ArrayList<String>());
							nextList.add(target);
							nextMap.put(source, nextList);
						}
					}
					
					//get idNodeMap
					NodeList nodeList = doc.getElementsByTagName("node");
					for(int i = 0;i < nodeList.getLength();i++){
						Node node = nodeList.item(i);
						if(node instanceof Element){
							String nodeType = ((Element) node).getAttribute("type");
							String nodeId = ((Element) node).getAttribute("id");
							String nodeText = "";
							if(nodeType.equals("1")){
								nodeText = ((Element) node).getElementsByTagName("queryText").item(0).getTextContent()
										.trim().toLowerCase();
							}else if(nodeType.equals("2")){
								nodeText = ((Element) node).getElementsByTagName("title").item(0).getTextContent()
										.trim().toLowerCase();
							}
							SearchNode searchNode = new SearchNode(nodeType, nodeText, nodeId);
							idNodeMap.put(nodeId, searchNode);
						}
					}
				}catch(Exception e){
					System.out.println(e);
				}
				
				for(Map.Entry<String, String> entry : preMap.entrySet()){
					String currentNodeId = entry.getKey();
					String preNodeId = entry.getValue();
					SearchNode currentNode = idNodeMap.get(currentNodeId);
					SearchNode preNode = idNodeMap.get(preNodeId);
					if(preNode.getType().equals("1")){
						List<String> nextList = nextMap.get(currentNodeId);
						if(nextList == null){
							if(currentNode.getType().equals("2")){
								Map<String, String> clickQueryMap = this.queryClickQueryMap.getOrDefault(preNode.getText(), new HashMap<String, String>());
								clickQueryMap.put(currentNode.getText(), "");
								this.queryClickQueryMap.put(preNode.getText(), clickQueryMap);
							}else{
								Map<String, String> queryClickMap = this.queryQueryClickMap.getOrDefault(preNode.getText(), new HashMap<String, String>());
								queryClickMap.put(currentNode.getText(), "");
								this.queryQueryClickMap.put(preNode.getText(), queryClickMap);
							}
							continue;
						}
						for(String nextId : nextList){
							SearchNode nextNode = idNodeMap.get(nextId);
							if(currentNode.getType().equals("2")){
								Map<String, String> clickQueryMap = this.queryClickQueryMap.getOrDefault(preNode.getText(), new HashMap<String, String>());
								clickQueryMap.put(currentNode.getText(), nextNode.getText());
								this.queryClickQueryMap.put(preNode.getText(), clickQueryMap);
							}else if(nextNode.getType().equals("2")){
								Map<String, String> queryClickMap = this.queryQueryClickMap.getOrDefault(preNode.getText(), new HashMap<String, String>());
								queryClickMap.put(currentNode.getText(), nextNode.getText());
								this.queryQueryClickMap.put(preNode.getText(), queryClickMap);
							}else{
								Map<String, String> queryQueryMap = this.queryQueryQueryMap.getOrDefault(preNode.getText(), new HashMap<String, String>());
								queryQueryMap.put(currentNode.getText(), nextNode.getText());
								this.queryQueryQueryMap.put(preNode.getText(), queryQueryMap);
							}
						}
					}
				}
			}
		}
		
	}
	
	public Map<String, Map<String, String>> getQueryClickQueryMap(){
		return this.queryClickQueryMap;
	}
	
	public Map<String, Map<String, String>> getQueryQueryClickMap()	{
		return this.queryQueryClickMap;
	}
	
	public Map<String, Map<String, String>> getQueryQueryQueryMap()	{
		return this.queryQueryQueryMap;
	}
}
