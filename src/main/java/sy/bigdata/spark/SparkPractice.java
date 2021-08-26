package sy.bigdata.spark;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;



public class SparkPractice {
	
	public static void main(String[] args) {
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
//		SparkTest test = new SparkTest();
//		test.loadFromDiskTest();
//		test.findTopTenMostFreqWords();
//		test.joinTest();
		
		SparkSQLTest sqlTest = new SparkSQLTest();
//		sqlTest.show();
//		sqlTest.basic();
//		sqlTest.testTempView();
		sqlTest.testInMemory();
		
//		loopForever();
		
		
	}
	
	private static void loopForever() {
		
		while(true) {
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				test.close();
			}
		}
	}
	


}
