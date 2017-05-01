package dataanalyze;

/**
 * 计算器类，包含数据分析中需要用到的各种计算工具。
 * @author liuxl
 */
public class Calculator {
	/**
	 * 计算tf值。
	 * @param n 词i在文档j中出现的次数。
	 * @param nSum 文档j的词总数。
	 * @return 词i对于文档j的tf值。
	 */
	public static float tf(int n, int nSum){
		return (float)n / nSum;
	}
	
	/**
	 * 计算idf值。
	 * @param d 文档集中文档的总数量。
	 * @param dW 文档集中包含词i的文档数量。
	 * @return 词i对于文档集D的idf值。
	 */
	public static float idf(int d, int dW){
		return (float) Math.log((float)d / dW);
	}
	
	/**
	 * tf-idf值计算。
	 * @param n 待查词i在文档j中出现的次数。
	 * @param nSum 文档j中的词总数。
	 * @param d 文档集总文档数量。
	 * @param dW 文档集中出现待查词i的文档数量。
	 * @return 词i对于文档j的tf-idf值。
	 */
	public static float tfIdf(int n, int nSum, int d, int dW){
		return tf(n, nSum) * idf(d, dW);
	}
	
	/**
	 * 计算向量余弦夹角。
	 * @param f1 向量1。
	 * @param f2 向量2。
	 * @return 余弦夹角。
	 * @throws Exception
	 */
	public static double vectorCos(float[] f1, float[] f2) throws Exception{
		if(f1.length != f2.length){
			throw new Exception("wrong vector dimension.");
		}
		
		int size = f1.length;
		double squareSum1 = 0.0f;
		double squareSum2 = 0.0f;
		double scalarProduct = 0.0f;
		
		for(int i = 0;i < size;i++){
			squareSum1 += Math.pow(f1[i], 2);
			squareSum2 += Math.pow(f2[i], 2);
			scalarProduct += f1[i] * f2[i];
		}
		
		return scalarProduct / (Math.sqrt(squareSum1) * Math.sqrt(squareSum2));
	}
	
	public static void main(String[] args){
		//两词有同样的高tf-idf分词，但低tf-idf分词不相同
		float[] f1 = {0.923f, 0f, 0.134f, 0f, 0f};
		float[] f2 = {0.923f, 0.134f, 0f, 0f, 0f};
		
		//两词有很多同样的低tf-idf分词，但高tf-idf分词不相同
		//这造成了“癌症化疗药物作用机理”与“癌症化疗药物机理”的相似度大大下降。
		float[] f3 = {0.123f, 0.234f, 0.134f, 0f, 0.933f};
		float[] f4 = {0.123f, 0.134f, 0f, 0.933f, 0f};
		
		try {
			System.out.println("两词有同样的高tf-idf分词，但低tf-idf分词不相同");
			System.out.println(vectorCos(f1, f2));
			System.out.println("两词有很多同样的低tf-idf分词，但高tf-idf分词不相同");
			System.out.println(vectorCos(f3, f4));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
