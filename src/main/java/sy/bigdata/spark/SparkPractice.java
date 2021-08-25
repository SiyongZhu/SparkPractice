package sy.bigdata.spark;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;



public class SparkPractice {
	
	public static void main(String[] args) {
		
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		SparkConf conf = new SparkConf().setAppName("start").setMaster("local[*]");
		
		conf.set("spark.task.maxFailures", "10")
            .set("spark.network.timeout", "1200000")
            .set("spark.rpc.lookupTimeout", "1200000")
            .set("spark.mesos.executor.memoryOverhead", "1500");
		
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		
		SparkTest test = new SparkTest(sc);
		test.loadFromDiskTest();
		
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sc.close();
		}
		
		
		
		
		
	}
	


}
