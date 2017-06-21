package roundthreedataanalyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class QueryGranularity {

	protected MongoCollection collection = null;

	public static final QueryGranularity instance = new QueryGranularity();

	public static final String MONGODB_SERVER = "127.0.0.1:27017";

	public String check(String query){
		Document document = new Document();
		document.put("query", query);
		MongoCursor<Document> cursor = collection.find(document).iterator();
		if (cursor.hasNext()) {
			Document result = cursor.next();
			return (String) result.get("granularity");
		}
		cursor.close();
		return null;
	}
	
	public void save(String inputPath) {
		File inputFile = new File(inputPath);
		BufferedReader reader;
		try{
			reader = new BufferedReader(new FileReader(inputFile));
			String context;
			while((context = reader.readLine()) != null){
				String[] qgArray = context.split(",");
				String query = qgArray[0];
				String granularity = qgArray[1];
				Document document = new Document();
				document.put("query", query);
				document.put("granularity", granularity);
				long timestamp = System.currentTimeMillis();
				document.put("timestamp", timestamp);
				collection.insertOne(document);
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	private QueryGranularity() {
		collection = new MongoClient(MONGODB_SERVER).getDatabase("recommendation").getCollection("querygranularity");
	}
	
	public static void main(String[] args){
		//QueryGranularity.instance.save("/Users/liuxl/Desktop/query粒度input.txt");
		//QueryGranularity.instance.save("/Users/liuxl/Desktop/query粒度input补充.txt");
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		String[] queries = new String[n];
		in.nextLine();
		for(int i = 0;i < n;i++){
			queries[i] = in.nextLine();
		}
		for(String query : queries){
			System.out.println(QueryGranularity.instance.check(query));
		}
	}
}
