package dataanalyze;

public class QueryNode extends SearchNode{
	private String queryText;
	
	public void setNodeString(String queryText){
		this.queryText = queryText;
	}
	
	public String getNodeString(){
		return this.queryText;
	}
}
