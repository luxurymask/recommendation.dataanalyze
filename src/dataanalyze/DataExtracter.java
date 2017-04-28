package dataanalyze;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataExtracter {
	public static List<SearchNode> extractNode(String filePath) {
		File inputFile = new File(filePath);
		Map<String, List<String>> nextMap = new HashMap<String, List<String>>();
		Map<String, String> preMap = new HashMap<String, String>();
		List<SearchNode> searchNodeList = new ArrayList<SearchNode>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);

			// 读取边信息。
			NodeList edgeList = doc.getElementsByTagName("edge");
			for (int i = 0; i < edgeList.getLength(); i++) {
				Node edge = edgeList.item(i);
				if (edge instanceof Element) {
					String source = ((Element) edge).getAttribute("source");
					String target = ((Element) edge).getAttribute("target");
					if (preMap.containsKey(target)) {
						throw new Exception("error: one node connection more than one nodes.");
					} else {
						preMap.put(target, source);
					}

					if (nextMap.containsKey(source)) {
						List<String> nextList = nextMap.get(source);
						nextList.add(target);
						nextMap.replace(source, nextList);
					} else {
						List<String> nextList = new ArrayList<String>();
						nextList.add(target);
						nextMap.put(source, nextList);
					}
				}
			}

			// 读取节点信息。
			NodeList nodeList = doc.getElementsByTagName("node");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					String nodeType = ((Element) node).getAttribute("type");
					String nodeId = ((Element) node).getAttribute("id");
					String preNodeId = preMap.get(nodeId);
					List<String> nextList = nextMap.get(nodeId);
					SearchNode searchNode = SearchNode.produce(DataExtracter.convertTypeToType(nodeType));
					searchNode.setId(nodeId);
					searchNode.setPreNodeId(preNodeId);
					searchNode.setNextList(nextList);
					if (nodeType.equals("1")) {
						String queryText = ((Element) node).getElementsByTagName("queryText").item(0).getTextContent()
								.trim().toLowerCase();
						searchNode.setNodeString(queryText);
					} else if (nodeType.equals("2")) {
						String clickTitle = ((Element) node).getElementsByTagName("title").item(0).getTextContent()
								.trim().toLowerCase();
						searchNode.setNodeString(clickTitle);
					}
					searchNodeList.add(searchNode);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return searchNodeList;
	}

	public static String json(String query, List<List<SearchNode>> generalList) throws JsonProcessingException {
		List<SearchNode> searchNodeList = null;
		for (List l : generalList) {
			if (DataExtracter.containsQuery(l, query)) {
				searchNodeList = l;
				break;
			}
		}

		Map currentMap = new HashMap();
		for (SearchNode currentNode : searchNodeList) {
			if (currentNode.getNodeString().equals(query)) {
				List<String> nextIds = currentNode.getNextList();
				List nextList = new ArrayList();
				for (String nextId : nextIds) {
					SearchNode nextNode = DataExtracter.searchNodeFromId(nextId, searchNodeList);
					Map nextMap = new HashMap();
					String sNext = nextNode.getNodeString();
					List<String> nextNextIds = nextNode.getNextList();
					List nextNextList = new ArrayList();
					for (String nextNextId : nextNextIds) {
						SearchNode nextNextNode = DataExtracter.searchNodeFromId(nextNextId, searchNodeList);
						Set nextNextSet = new HashSet();
						String sNextNext = nextNextNode.getNodeString();
						if (nextNextNode.getType().equals(SearchNode.QUERY)) {
							nextNextSet.add("query" + "\" : \"" + sNextNext);
						} else if (nextNextNode.getType().equals(SearchNode.CLICK)) {
							nextNextSet.add("click" + "\" : \"" + sNextNext);
						}
						if (nextNextSet.size() > 0) {
							nextNextList.add(nextNextSet);
						}
					}
					if (nextNode.getType().equals(SearchNode.QUERY)) {
						nextMap.put("query" + "\" : \"" + sNext, nextNextList);
					} else if (nextNode.getType().equals(SearchNode.CLICK)) {
						nextMap.put("click" + "\" : \"" + sNext, nextNextList);
					}
					if (nextMap.size() > 0) {
						nextList.add(nextMap);
					}
				}
				currentMap.put("query" + "\" : \"" + query, nextList);
				break;
			}
		}
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(currentMap);
	}

	public static String jsonBak(String query, List<List<SearchNode>> generalList) throws JsonProcessingException {
		List<SearchNode> searchNodeList = null;
		for (List l : generalList) {
			if (DataExtracter.containsQuery(l, query)) {
				searchNodeList = l;
				break;
			}
		}

		Map currentMap = new HashMap();
		currentMap.put("query", query);
		for (SearchNode currentNode : searchNodeList) {
			if (currentNode.getNodeString().equals(query)) {
				List<String> nextIds = currentNode.getNextList();
				List nextList = new ArrayList();
				for (String nextId : nextIds) {
					SearchNode nextNode = DataExtracter.searchNodeFromId(nextId, searchNodeList);
					Map nextMap = new HashMap();
					String sNext = nextNode.getNodeString();
					if (nextNode.getType().equals(SearchNode.QUERY)) {
						nextMap.put("query", sNext);
					} else if (nextNode.getType().equals(SearchNode.CLICK)) {
						nextMap.put("click", sNext);
					}
					List<String> nextNextIds = nextNode.getNextList();
					List nextNextList = new ArrayList();
					for (String nextNextId : nextNextIds) {
						SearchNode nextNextNode = DataExtracter.searchNodeFromId(nextNextId, searchNodeList);
						Map nextNextMap = new HashMap();
						String sNextNext = nextNextNode.getNodeString();
						if (nextNextNode.getType().equals(SearchNode.QUERY)) {
							nextNextMap.put("query", sNextNext);
						} else if (nextNextNode.getType().equals(SearchNode.CLICK)) {
							nextNextMap.put("click", sNextNext);
						}
						nextNextList.add(nextNextMap);
					}
					if (nextNextList.size() > 0) {
						nextMap.put("next", nextNextList);
					}
					nextList.add(nextMap);
				}
				if (nextList.size() > 0) {
					currentMap.put("next", nextList);
				}
				break;
			}
		}
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(currentMap);
	}

	public static SearchNode searchNodeFromId(String id, List<SearchNode> searchNodeList) {
		for (SearchNode searchNode : searchNodeList) {
			if (searchNode.getId().equals(id)) {
				return searchNode;
			}
		}
		return null;
	}

	public static boolean containsQuery(List<SearchNode> searchNodeList, String queryText) {
		for (SearchNode node : searchNodeList) {
			if (node.getNodeString().equals(queryText)) {
				return true;
			}
		}
		return false;
	}

	public static String convertTypeToType(String type) throws Exception {
		if (type.equals("1")) {
			return "query";
		} else if (type.equals("2")) {
			return "click";
		} else {
			throw new Exception("wrong type.");
		}
	}

	public static void main(String[] args) throws JsonProcessingException {
		List list = extractNode("/Users/liuxl/Desktop/graphml/1-qiushaojie.xml");
		List generalList = new ArrayList();
		generalList.add(list);
		System.out.println(DataExtracter.json("化疗用药周期", generalList));
	}
}
