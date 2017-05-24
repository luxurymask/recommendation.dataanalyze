package newdataanalyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class QueryNumCheck {
	public static void main(String[] args){
		File forcheck2 = new File("/Users/liuxl/Desktop/recommendation/nodes/task1/forcheck1.txt");
		File forcheck1 = new File("/Users/liuxl/Desktop/recommendation/nodes/task1/forcheck2.txt");
		File forcheck0 = new File("/Users/liuxl/Desktop/recommendation/input/task1.input");
		
		Set<String> set1 = new HashSet<String>();
		Set<String> set2 = new HashSet<String>();
		
		BufferedReader reader1;
		BufferedReader reader2;
		BufferedReader reader0;
		try{
			reader1 = new BufferedReader(new FileReader(forcheck1));
			reader2 = new BufferedReader(new FileReader(forcheck2));
			reader0 = new BufferedReader(new FileReader(forcheck0));
			String text1 = null;
			String text2 = null;
			String text0 = null;
			while((text1 = reader1.readLine()) != null){
				if(!set1.contains(text1)){
					set1.add(text1);
				}
			}
			

			while((text0 = reader0.readLine()) != null){
				if(!set2.contains(text0)){
					set2.add(text0);
				}
			}
			
			while((text2 = reader2.readLine()) != null){
				if(!set1.contains(text2)){
					System.out.println(text2);
				}
			}
			System.out.println("----------------------------------------------------");
			reader2 = new BufferedReader(new FileReader(forcheck2));
			while((text2 = reader2.readLine()) != null){
				if(!set2.contains(text2)){
					System.out.println(text2);
				}
			}
		}catch(Exception e){
			
		}
	}
}
