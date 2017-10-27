package roundthreedataanalyze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

public class ClickCounter {
	private Map<String, String> queryMap = new HashMap<String, String>();
	private Map<String, String> clickMap = new HashMap<String, String>();
	private Map<String, List<String>> queryClickMap = new HashMap<String, List<String>>();
	
	public Map<String, Integer> setMaps(String graphMlPath){
		Map<String, Integer> queryClickCountMap = new HashMap<String, Integer>();
		File inputFile = new File(graphMlPath);
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);

			// 读取节点信息。
			NodeList nodeList = doc.getElementsByTagName("node");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					String nodeType = ((Element) node).getAttribute("type");
					String id = ((Element) node).getAttribute("id");
					if (nodeType.equals("1")) {
						String queryText = ((Element) node).getElementsByTagName("queryText").item(0).getTextContent();
						queryMap.put(id, queryText);
					}else if(nodeType.equals("2")){
						String title = ((Element) node).getElementsByTagName("title").item(0).getTextContent();
						clickMap.put(id, title);
					}else{
						throw new Exception("wrong type.");
					}
				}
			}
			
			//读取边信息,set节点关系map
			NodeList edgeList = doc.getElementsByTagName("edge");
			for (int i = 0; i < edgeList.getLength(); i++) {
				Node edge = edgeList.item(i);
				if (edge instanceof Element) {
					String source = ((Element) edge).getAttribute("source");
					List<String> clickList = queryClickMap.getOrDefault(source, new ArrayList<String>());
					String target = ((Element) edge).getAttribute("target");
					if(clickMap.containsKey(target)){
						clickList.add(target);
						queryClickMap.put(source, clickList);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		for(Map.Entry<String, String> entry : queryMap.entrySet()){
			String id = entry.getKey();
			String queryText = entry.getValue();
			if(queryClickCountMap.containsKey(queryText)){
				queryText = queryText + "#duplicated#";
			}
			queryClickCountMap.put(queryText, queryClickMap.containsKey(id) ? queryClickMap.get(id).size() : 0);
		}
		return queryClickCountMap;
	}
	
	public static void main(String[] args){
		File graphmlFolder = new File("/Users/liuxl/Desktop/毕业设计-not published/basic data/graphml/task4");
		File[] xmlFiles = graphmlFolder.listFiles();
		for (File f : xmlFiles) {
			String path = f.getAbsolutePath();
			if (path.endsWith(".xml")) {
				ClickCounter counter = new ClickCounter();
				Map<String, Integer> countMap = counter.setMaps(path);
				String outputName = path.substring(65);
				outputName = outputName.substring(0, outputName.length() - 4);
				File outputFile = new File("/Users/liuxl/Desktop/recommendation/phase1/click count/" + outputName + ".txt");
				BufferedWriter writer;
				try{
					if(!outputFile.exists()){
						outputFile.createNewFile();
					}
					writer = new BufferedWriter(new FileWriter(outputFile));
					for(Map.Entry<String, Integer> entry : countMap.entrySet()){
						writer.write(entry.getKey() + "\t" + entry.getValue());
						writer.newLine();
					}
					writer.flush();
					writer.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
}
