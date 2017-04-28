package dataanalyze;

import java.util.HashMap;
import java.util.Map;

public class QueryInfo {
	private String _queryText = "";
	private Map<String, Boolean> _subTexts = new HashMap<String, Boolean>();
	
	public String getQueryText(){
		return _queryText;
	}
	
	public Map<String, Boolean> getSubTexts(){
		return _subTexts;
	}
	
	public void setQueryText(String queryText){
		this._queryText = queryText;
	}
	
	public void setSubText(String subText){
		this._subTexts.put(subText, true);
	}
}
