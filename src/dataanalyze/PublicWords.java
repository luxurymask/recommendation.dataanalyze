package dataanalyze;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * abandoned.
 * @author liuxl
 *
 */
public class PublicWords {
	public List<String> wordList = new ArrayList<String>();
	
	@Override
	public int hashCode() {
		this.wordList.sort(new Comparator<String>(){
			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				return o1.compareTo(o2);
			}
		});
		
		String code = "";
		Iterator it = wordList.iterator();
		while(it.hasNext()){
			code += it.next().hashCode();
		}
		return Integer.parseInt(code);
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(this == obj){
			return true;
		}
		
		if((obj == null) || (obj.getClass() != this.getClass())){
			return false;
		}
		
		PublicWords pw = (PublicWords)obj;
		
		int wordListSize;
		if((wordListSize = pw.wordList.size()) != this.wordList.size()){
			return false;
		}
		
		for(int i = 0;i < wordListSize;i++){
			String thisWord = this.wordList.get(i);
			for(int j = 0;j < wordListSize;j++){
				if(thisWord.equals(pw.wordList.get(i))){
					break;
				}
				if(j == (wordListSize - 1)){
					return false;
				}
			}
		}
		return true;
	}
}
