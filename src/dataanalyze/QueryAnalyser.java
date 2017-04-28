package dataanalyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class QueryAnalyser {
	
	public static final Map<String, Integer> stringIndexMap = new HashMap<String, Integer>();
	public static final Map<Integer, String> indexStringMap = new HashMap<Integer, String>();
	public static final Map<Integer, Map<Integer, Integer>> indexLengthMap = new HashMap<Integer, Map<Integer, Integer>>();
	
	private static final int VECTOR_LENGTH = 10000;
	
	/**
	 * 提取查询关键词（expired）
	 * @param filePath
	 * @param fileName
	 */
	public static void queryExtract(String filePath, String fileName) {
		try {
			File inputFile = new File(filePath, fileName + ".graphml");
			File outputFile = new File(filePath, fileName + ".out");
			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);
			NodeList nodeList = doc.getElementsByTagName("queryText");
			Set<String> queryTexts = new HashSet<String>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					String queryText = nodeList.item(i).getTextContent();
					queryTexts.add(queryText);
				}
			}
			
			FileOutputStream fos = new FileOutputStream(outputFile);
			for (String s : queryTexts) {
				s += "\r\n";
				byte[] sInBytes = s.getBytes();
				fos.write(sInBytes);
			}
			fos.flush();
			fos.close();
		} catch (Exception e) {

		}
	}

	/**
	 * 查询关键词重复次数提取（to be abandoned）
	 * @param files
	 */
	public static void duplicateQueryExtract(String[] files) {
		Map<String, Integer> queryCount = new HashMap<String, Integer>();

		for (String s : files) {
			File inputFile = new File(s);
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(inputFile));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					String formatedString = tempString.trim().toLowerCase();
					if (queryCount.containsKey(formatedString)) {
						int count = queryCount.get(formatedString);
						queryCount.replace(formatedString, count, count + 1);
					} else {
						queryCount.put(formatedString, 1);
					}
				}
				reader.close();
			} catch (Exception e) {

			}
		}

		Iterator iter = queryCount.entrySet().iterator();
		int count = 0;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String text = (String)entry.getKey();
			System.out.println(text);
			QueryAnalyser.stringIndexMap.put(text, count);
			QueryAnalyser.indexStringMap.put(count++, text);
		}
	}
	
	
	public static void subTextAnalyse(String inputPath) {
		List<Map<String, Boolean>> subTextList = new ArrayList<Map<String, Boolean>>();
		File inputFile = new File(inputPath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] subStrings = tempString.split(" ");
				Map<String, Boolean> subTextMap = new HashMap<String, Boolean>();
				for (String s : subStrings) {
					subTextMap.put(s, true);
				}
				subTextList.add(subTextMap);
			}
			reader.close();
		} catch (Exception e) {

		}
		
		QueryAnalyser.indexLengthMap.clear();
		int subTextSize = subTextList.size();
		for(int i = 0;i < subTextSize;i++){
			Map<Integer, Integer> subTextLengthMap = new HashMap<Integer, Integer>();
			for(int j = i + 1;j < subTextSize;j++){
				longestSubText(subTextList.get(i), subTextList.get(j), subTextLengthMap, j);
			}
			Map<Integer, Integer> sorted = sortByValue7(subTextLengthMap);
			QueryAnalyser.indexLengthMap.put(i, sorted);
		}
	}
	
	public static void getSubTexts(String text, String outputFile, FileOutputStream fos){
		int index = QueryAnalyser.stringIndexMap.get(text);
		try{
			byte[] textInBytes = ("$" + text + "\r\n").getBytes();
			fos.write(textInBytes);
			Map<Integer, Integer> map = QueryAnalyser.indexLengthMap.get(index);
			for(Map.Entry<Integer, Integer> entry : map.entrySet()){
				System.out.println(QueryAnalyser.indexStringMap.get(entry.getKey()) + " | " + entry.getValue());
				byte[] textsInBytes = (QueryAnalyser.indexStringMap.get(entry.getKey()) + " | " + entry.getValue() + "\r\n").getBytes();
				fos.write(textsInBytes);
			}
		}catch(Exception e){
			
		}
	}
	
	public static Map<String, Integer> getTexts(String text){
		int index = QueryAnalyser.stringIndexMap.get(text);
		Map<Integer, Integer> map = QueryAnalyser.indexLengthMap.get(index);
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		for(Map.Entry<Integer, Integer> entry : map.entrySet()){
			resultMap.put(QueryAnalyser.indexStringMap.get(entry.getKey()), entry.getValue());
			//System.out.println(QueryAnalyser.indexStringMap.get(entry.getKey()) + " | " + entry.getValue());
		}
		return sortByValue7(resultMap);
	}
	
	public static String longestSubText(Map<String, Boolean> source, Map<String, Boolean> target, Map<Integer, Integer> result, int comparerIndex){
		String subTexts = "";
		int length = 0;
		for(Map.Entry<String, Boolean> entry : source.entrySet()){
			String key = entry.getKey();
			if(target.containsKey(key)){
				subTexts += (key + " ");
				length++;
			}
		}
		if(!subTexts.equals("")){
			result.put(comparerIndex, length);
		}
		return subTexts;
	}
	
	public static Boolean publicWords(Map<String, Boolean> source, Map<String, Boolean> target, Map<PublicWords, Integer> words){
		return true;
	}
	
	public static Map<String, Integer> termFrequency(String filePath){
		Map<String, Integer> subTextMap = new HashMap<String, Integer>();
		File inputFile = new File(filePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String[] subStrings = tempString.split(" ");
				for (String s : subStrings) {
					if(subTextMap.containsKey(s)){
						int value = subTextMap.get(s);
						subTextMap.replace(s, value + 1);
					}else{
						subTextMap.put(s, 1);
					}
				}
			}
			reader.close();
		} catch (Exception e) {

		}
		return subTextMap;
	}
	
	/**
	 * 求查询词的分词的词频。
	 * @param oriQueryFilePath 原始查询文件路径。
	 * @param subQueryFilePath 查询分词后的文件路径。
	 * @return
	 */
	public static Map<String, Map<String, Integer>> wordsFrequency(String oriQueryFilePath, String subQueryFilePath){
		File oriFile = new File(oriQueryFilePath);
		File subFile = new File(subQueryFilePath);
		
		BufferedReader oriReader = null;
		BufferedReader subReader = null;
		Map<String, Map<String, Integer>> frequencyMap = new HashMap<String, Map<String, Integer>>();
		try{
			oriReader = new BufferedReader(new FileReader(oriFile));
			subReader = new BufferedReader(new FileReader(subFile));
			String oriQuery = null;
			String subQuery = null;
			while((subQuery = subReader.readLine()) != null && (oriQuery = oriReader.readLine()) != null){
				String[] subTexts = subQuery.split(" ");
				Map<String, Integer> subTextFreMap = new HashMap<String, Integer>();
				for(String s : subTexts){
					if(subTextFreMap.containsKey(s)){
						subTextFreMap.replace(s, subTextFreMap.get(s) + 1);
					}else{
						subTextFreMap.put(s, 1);
					}
				}
				frequencyMap.put(oriQuery, subTextFreMap);
			}
		}catch(Exception e){
			
		}
		return frequencyMap;
	}
	
	/**
	 * 求查询词余弦相似度。
	 * @param wordVectorsMap
	 * @return
	 */
	public static Map<String, Map<String, Double>> wordsCosSimilarity(Map<String, float[]> wordVectorsMap){
		Map<String, Map<String, Double>> resultMap = new HashMap<String, Map<String, Double>>();
		for(Map.Entry<String, float[]> entry : wordVectorsMap.entrySet()){
			Map<String, Double> similarityMap = new HashMap<String, Double>();
			String queryText = entry.getKey();
			float[] wordVector = entry.getValue();
			double similarity = 0.0;
			try{
				for(Map.Entry<String, float[]> entryCompare : wordVectorsMap.entrySet()){
					if(entryCompare.getKey().equals(queryText)){
						continue;
					}
					similarity = Calculator.vectorCos(wordVector, entryCompare.getValue());
					similarityMap.put(entryCompare.getKey(), similarity);
				}
			}catch(Exception e){
				System.out.println(e);
			}
			resultMap.put(queryText, sortByValue7(similarityMap));
		}
		return resultMap;
	}
	
	/**
	 * 为每个查询词生成词向量，向量中每位为分词的tf-idf值。
	 * @param tfIdfMap
	 * @return
	 */
	public static Map<String, float[]> wordVectors(Map<String, Map<String, Float>> tfIdfMap){
		Map<String, Integer> subtextNumberMap = new HashMap<String, Integer>();
		Map<Integer, String> numberSubtextMap = new HashMap<Integer, String>();
		Map<String, float[]> resultMap = new HashMap<String, float[]>();
		int number = 0;
		
		for(Map.Entry<String, Map<String, Float>> entry : tfIdfMap.entrySet()){
			String word = entry.getKey();
			Map<String, Float> subTextTfIdfMap = entry.getValue();
			float[] tfIdfs = new float[VECTOR_LENGTH];
			for(Map.Entry<String, Float> entry2 : subTextTfIdfMap.entrySet()){
				String subtext = entry2.getKey();
				Float tfIdf = entry2.getValue();
				if(!subtextNumberMap.containsKey(subtext)){
					tfIdfs[number] = tfIdf;
					subtextNumberMap.put(subtext, number);
					numberSubtextMap.put(number++, subtext);
				}else{
					tfIdfs[subtextNumberMap.get(subtext)] = tfIdf;
				}
			}
			resultMap.put(word, tfIdfs);
		}
		return resultMap;
	}
	
	/**
	 * 去除stopwords。
	 * @param tfIdfMap
	 * @param stopwordsPath
	 */
	public static void filterStopwords(Map<String, Map<String, Float>> tfIdfMap, String stopwordsPath){
		File stopwordsFile = new File(stopwordsPath);
		Map<String, Boolean> stopwordsMap = new HashMap<String, Boolean>();
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(stopwordsFile));
			String stopword = null;
			while((stopword = reader.readLine()) != null){
				stopwordsMap.put(stopword, true);
			}
			reader.close();
		}catch(Exception e){
			
		}
		for(Map.Entry<String, Map<String, Float>> entry : tfIdfMap.entrySet()){
			Iterator<Map.Entry<String, Float>> iterator = entry.getValue().entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<String, Float> entry2 = iterator.next();
				if(stopwordsMap.containsKey(entry2.getKey())){
					iterator.remove();
				}
			}
		}
	}
	
	/**
	 * 计算TfIdf值。
	 * @param textTfMap
	 * @param textIdfMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Map<String, Float>> wordsTfIdf(Map<String, Map<String, Float>> textTfMap, Map<String, Map<String, Float>> textIdfMap) throws Exception{
		Iterator<Entry<String, Map<String, Float>>> tfIterator = textTfMap.entrySet().iterator();
		Iterator<Entry<String, Map<String, Float>>> idfIterator = textIdfMap.entrySet().iterator();
		Map<String, Map<String, Float>> resultMap = new HashMap<String, Map<String, Float>>();
		
		while(tfIterator.hasNext() && idfIterator.hasNext()){
			Map.Entry<String, Map<String, Float>> tfEntry = tfIterator.next();
			Map.Entry<String, Map<String, Float>> idfEntry = idfIterator.next();
			
			String textTf = tfEntry.getKey();
			String textIdf = idfEntry.getKey();
			Map<String, Float> tfMap = null;
			Map<String, Float> idfMap = null;
			Map<String, Float> tfIdfMap = new HashMap<String, Float>();
			if(textTf != textIdf){
				throw new Exception("wrong map order.");
			}else{
				tfMap = tfEntry.getValue();
				idfMap = idfEntry.getValue();
				for(Map.Entry<String, Float> entry : tfMap.entrySet()){
					String subText = entry.getKey();
					Float tf = entry.getValue();
					if(idfMap.containsKey(entry.getKey())){
						Float idf = idfMap.get(subText);
						tfIdfMap.put(subText, tf * idf);
					}else{
						throw new Exception("wrong subtexts.");
					}
				}
				resultMap.put(textTf, sortByValue7(tfIdfMap));
			}
		}
		if(tfIterator.hasNext() || idfIterator.hasNext()){
			throw new Exception("wrong map size.");
		}
		return resultMap;
	}
	
	/**
	 * 计算分词tf值。
	 * @param frequencyMap
	 * @return
	 */
	public static Map<String, Map<String, Float>> wordsTf(Map<String, Map<String, Integer>> frequencyMap){
		Map<String, Map<String, Float>> resultMap = new HashMap<String, Map<String, Float>>();
		for(Map.Entry<String, Map<String, Integer>> entry : frequencyMap.entrySet()){
			int sum = 0;
			Map<String, Float> tfMap = new HashMap<String, Float>();
			for(Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()){
				sum += entry2.getValue();
			}
			for(Map.Entry<String, Integer> entry2 : entry.getValue().entrySet()){
				tfMap.put(entry2.getKey(), Calculator.tf(entry2.getValue(), sum));
			}
			resultMap.put(entry.getKey(), sortByValue(tfMap));
		}
		return resultMap;
	}
	
	/**
	 * 计算分词idf值。
	 * @param frequencyMap
	 * @return
	 */
	public static Map<String, Map<String, Float>> wordsIdf(Map<String, Map<String, Integer>> frequencyMap){
		Map<String, Map<String, Float>> resultMap = new HashMap<String, Map<String, Float>>();
		for(Map.Entry<String, Map<String, Integer>> entry : frequencyMap.entrySet()){
			Map<String, Integer> subTextFreMap = new HashMap<String, Integer>();
			Map<String, Float> subTextIdfMap = new HashMap<String, Float>();
			
			String queryText = entry.getKey();
			subTextFreMap = entry.getValue();
			for(Map.Entry<String, Integer> subEntry : subTextFreMap.entrySet()){
				int appear = 0;
				String subText = subEntry.getKey();
				for(String s : frequencyMap.keySet()){
					if(frequencyMap.get(s).containsKey(subText)){
						appear++;
					}
				}
				Float idf = Calculator.idf(frequencyMap.size(), appear);
				subTextIdfMap.put(subText, idf);
			}
			resultMap.put(queryText, QueryAnalyser.sortByValue7(subTextIdfMap));
		}
		return resultMap;
	}
	
	/**
	 * Map按value排序，代码来自：http://www.cnblogs.com/liu-qing/p/3983496.html
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }
	
	/**
	 * Map按value排序降序（Java7），代码来自：http://www.cnblogs.com/liu-qing/p/3983496.html
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue7(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
	
	public static void main(String[] args){
		Map<String, Map<String, Float>> filteredMap = new HashMap<String, Map<String, Float>>();
		Map<String, Map<String, Float>> tfIdfMap = new HashMap<String, Map<String, Float>>();
		Map<String, Map<String, Float>> tfMap = new HashMap<String, Map<String, Float>>();
		Map<String, Map<String, Float>> idfMap = new HashMap<String, Map<String, Float>>();
		Map<String, Map<String, Integer>> freMap = new HashMap<String, Map<String, Integer>>();
		
		freMap = QueryAnalyser.wordsFrequency("/Users/liuxl/Desktop/recommendation/input/task1.input", "/Users/liuxl/Desktop/recommendation/data/task1.txt");
		idfMap = QueryAnalyser.wordsIdf(freMap);
		tfMap = QueryAnalyser.wordsTf(freMap);
		try {
			tfIdfMap = QueryAnalyser.wordsTfIdf(tfMap, idfMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		QueryAnalyser.filterStopwords(tfIdfMap, "/Users/liuxl/Desktop/recommendation/input/stopwords.txt");
		System.out.println(tfIdfMap.get("化疗药物 药物机理"));
		System.out.println(tfIdfMap.get("静脉滴注 化学疗法"));
//		Map<String, float[]> wordVectorMap = wordVectors(tfIdfMap);
//		Map<String, Map<String, Double>> similarityMap = wordsCosSimilarity(wordVectorMap);
//		for(Map.Entry<String, Map<String, Double>> entry : similarityMap.entrySet()){
//			System.out.println(entry.getKey() + " | " + entry.getValue());
//		}
//		System.out.println("#################################################");
//		for(Map.Entry<String, Map<String, Float>> entry : tfIdfMap.entrySet()){
//			System.out.print(entry.getKey() + " | ");
//			Map<String, Float> map = new HashMap<String, Float>();
//			map = entry.getValue();
//			for(Map.Entry<String, Float> entry2 : map.entrySet()){
//				System.out.print(entry2.getKey() + "(" + entry2.getValue() + ")" + " ");
//			}
//			System.out.println();
//		}
	}
}