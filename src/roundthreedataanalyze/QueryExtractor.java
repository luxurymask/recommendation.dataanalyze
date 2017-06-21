package roundthreedataanalyze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class QueryExtractor {
	public Map<String, String> extractNode(String graphMlPath, String outputPath) {
		File inputFile = new File(graphMlPath);
		Map<String, String> queryTimestampMap = new LinkedHashMap<String, String>();
		File outputFile = new File(outputPath);
		BufferedWriter writer;
		int count = 0;
		
		try {
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(outputFile));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);
			
			// 读取节点信息。
			NodeList nodeList = doc.getElementsByTagName("node");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					String nodeType = ((Element) node).getAttribute("type");
					if (nodeType.equals("1")) {
						count++;
						String queryText = ((Element) node).getElementsByTagName("queryText").item(0).getTextContent();
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
						writer.write(queryText + "\t" + timestamp);
						writer.newLine();
						queryTimestampMap.put(queryText, timestamp);
					}
				}
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println(count);
		System.out.println(queryTimestampMap.size());
		return queryTimestampMap;
	}
	
	public static void main(String[] args){
		QueryExtractor extractor = new QueryExtractor();

		File graphmlFolder = new File("/Users/liuxl/Desktop/毕业设计-not published/basic data/graphml/task2");
		File[] xmlFiles = graphmlFolder.listFiles();
		for (File f : xmlFiles) {
			String path = f.getAbsolutePath();
			if (path.endsWith(".xml")) {
				String outputPath = path.substring(65);
				outputPath = outputPath.substring(0, outputPath.length() - 4);
				extractor.extractNode(path, "/Users/liuxl/Desktop/recommendation/query with timestamp/" + outputPath + ".txt");
			}
		}
		
	}
}
