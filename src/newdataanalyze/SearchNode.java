package newdataanalyze;

public class SearchNode {
	private final String type;
	private final String text;
	private final String id;
	
	public SearchNode(String type, String text, String id){
		this.type = type;
		this.text = text;
		this.id = id;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getText(){
		return this.text;
	}
	
	public String getId(){
		return this.id;
	}
}
