package dataanalyze;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchNode {
	public static final String CLICK = "click";
	public static final String QUERY = "query";
	public static final String UNKNOWN = "unknown type";
	private String id;
	private String preNodeId;
	private List<String> nextList;
	
	public void setId(String id){
		this.id = id;
	}

	public void setPreNodeId(String id){
		this.preNodeId = id;
	}
	
	public void setNextList(List<String> nextList){
		this.nextList = nextList;
	}
	
	public String getId(){
		return this.id;
	}

	public String getPreNodeId(){
		return this.preNodeId;
	}	
	
	public List<String> getNextList(){
		if(this.nextList != null){
			return this.nextList;
		}else{
			return new ArrayList<String>();
		}
		
	}
	
	public String getType(){
		if(this instanceof ClickNode){
			return SearchNode.CLICK;
		}else if(this instanceof QueryNode){
			return SearchNode.QUERY;
		}else{
			return SearchNode.UNKNOWN;
		}
	}
	
	public abstract void setNodeString(String s);
	public abstract String getNodeString();
	
	public static SearchNode produce(String type) throws Exception{
		if(type.equals(SearchNode.CLICK)){
			return new ClickNode();
		}else if(type.equals(SearchNode.QUERY)){
			return new QueryNode();
		}else{
			throw new Exception("Wrong Type.");
		}
	}
	
}
