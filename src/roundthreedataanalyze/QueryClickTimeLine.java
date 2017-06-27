package roundthreedataanalyze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dataanalyze.QueryAnalyser;

public class QueryClickTimeLine {
	public static Map<String, String> getQueryClickTimeLine(String inputPath){
		File inputFile = new File(inputPath);
		Map<String, String> nodeTimestampMap = new HashMap<String, String>();
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);
			
			// 读取节点信息。
			NodeList nodeList = doc.getElementsByTagName("node");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					String nodeType = ((Element) node).getAttribute("type");
					NodeList vertexOperationList = ((Element)node).getElementsByTagName("vertexOperation");
					String timestamp = "";
					for(int j = 0;j < vertexOperationList.getLength();j++){
						Node vertexOperation = vertexOperationList.item(j);
						String operationType = ((Element)vertexOperation).getAttribute("type");
						if(operationType.equals("1")){
							timestamp = ((Element)vertexOperation).getElementsByTagName("time").item(0).getTextContent();
							break;
						}
					}
					if (nodeType.equals("1")) {
						String queryText = ((Element) node).getElementsByTagName("queryText").item(0).getTextContent();
						nodeTimestampMap.put(queryText, timestamp);
					}else if(nodeType.equals("2")){
						String title = ((Element) node).getElementsByTagName("title").item(0).getTextContent();
						nodeTimestampMap.put(" ----" + title, timestamp);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		Map<String, String> resultMap = QueryAnalyser.sortByValue(nodeTimestampMap);
		return resultMap;
	}
	
	public static void main(String[] args){
		File graphmlFolder = new File("/Users/liuxl/Desktop/毕业设计-not published/basic data/graphml/第二批/task2");
		File[] xmlFiles = graphmlFolder.listFiles();
		for (File f : xmlFiles) {
			String path = f.getAbsolutePath();
			if (path.endsWith(".xml")) {
				String outputName = path.substring(69);
				outputName = outputName.substring(0, outputName.length() - 4);
				String outputPath = "/Users/liuxl/Desktop/recommendation/queryclicktimeline/第二批/" + outputName + ".txt";
				File outputFile = new File(outputPath);
				Map<String, String> map = QueryClickTimeLine.getQueryClickTimeLine(path);
				BufferedWriter writer;
				try{
					if(!outputFile.exists()){
						outputFile.createNewFile();
					}
					writer = new BufferedWriter(new FileWriter(outputFile));
					for(Map.Entry<String, String> entry : map.entrySet()){
						writer.write(entry.getKey() + "\t" + entry.getValue());
						writer.newLine();
					}
					writer.flush();
					writer.close();
				}catch(Exception e){
					System.out.println(e);
				}
			}
		}
	}
}
