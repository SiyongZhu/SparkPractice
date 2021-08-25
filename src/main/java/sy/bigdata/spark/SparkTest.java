package sy.bigdata.spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SparkTest {

	private JavaSparkContext sc;
	
	public SparkTest(JavaSparkContext sc ) {
		this.sc = sc;
	}
	
	public void mapTest() {
		List<Integer> input = new ArrayList<Integer>();
		input.add(35);
		input.add(12);
		input.add(90);
		input.add(20);
		
		JavaRDD<Integer> myRdd = sc.parallelize(input);
		JavaRDD<Double> sqrtRdd = myRdd.map(i -> Math.sqrt(i));
		
		//foreach() only takes a void function, it cannot return any value
		sqrtRdd.foreach(value -> System.out.println(value));
		
		sqrtRdd.collect().forEach(System.out::println);
	}
	
	public void objectRDDTest() {
		List<Integer> input = new ArrayList<Integer>();
		input.add(35);
		input.add(12);
		input.add(90);
		input.add(20);
		
		JavaRDD<Integer> originalRdd = sc.parallelize(input);
		//or use the following
		JavaRDD<Tuple2<Integer, Double>> sqrtRdd = originalRdd.map(i-> new Tuple2<>(i, Math.sqrt(i)));
		
		sqrtRdd.foreach(value -> System.out.println(value._1+": "+value._2));
	}
	
	
	public void reduceTest() {
		String test = "first\n  \n\nthird\n forth";
		
		List<String> lines = test.lines().filter(line->!line.isBlank()).map(String::strip).collect(Collectors.toList());
		lines.forEach(System.out::println);
		
		JavaRDD<String> myRdd = sc.parallelize(lines);
		
		String combin = myRdd.reduce((str1, str2) -> str1+str2);
		
		System.out.println("Final contancation is "+combin);
	}
	
	public void pairRDDTest() {
		List<String> input = new ArrayList<String>();
		input.add("WARN: Tuesday 4 2020");
		input.add("ERROR: Tuesday 4 2020");
		input.add("FATAL: Wednesday 5 2020");
		input.add("ERROR: Friday 7 2020");
		input.add("WARN: Saturday 8 2020");
		
		JavaRDD<String> origLogMsg = sc.parallelize(input);
		
		JavaPairRDD<String, String> pair = origLogMsg.mapToPair(rawVal -> {
			String[] columns = rawVal.split(":");
			String level = columns[0];
			String date = columns[1];
			return new Tuple2<String, String>(level, date);
		});
		
		//to count the value
		JavaPairRDD<String, Long> pairRdd = origLogMsg.mapToPair(rawVal -> {
			String[] columns = rawVal.split(":");
			String level = columns[0];
			return new Tuple2<String, Long>(level, 1L);
		});
		
		JavaPairRDD<String, Long> sumRdd = pairRdd.reduceByKey((val1, val2) -> val1 + val2);
		sumRdd.foreach(tuple -> System.out.println(tuple._1+" has "+tuple._2));
		
		simplePairRDDTest(origLogMsg);
	}
	
	public void simplePairRDDTest(JavaRDD<String> text) {
		System.out.println("Run with a concise function");
		text.mapToPair(raw -> new Tuple2<String, Long>(raw.split(":")[0], 1L))
			.reduceByKey((val1, val2)->val1+val2)
			.foreach(tuple -> System.out.println(tuple._1+" has "+tuple._2));
		
	}
	
	public void flatMapTest() {
		List<String> input = new ArrayList<String>();
		input.add("WARN: Tuesday 4 2020");
		input.add("ERROR: Tuesday 4 2020");
		input.add("FATAL: Wednesday 5 2020");
		input.add("ERROR: Friday 7 2020");
		input.add("WARN: Saturday 8 2020");
		
		JavaRDD<String> sentences = sc.parallelize(input);
		
		JavaRDD<String> words = sentences.flatMap(val -> Arrays.asList(val.split(" ")).iterator());
		words.foreach(val -> System.out.println(val));
		
		System.out.println("Start filter out single digit number");
		words.filter(word -> word.length()>1).foreach(val -> System.out.println(val));
	}
	
	public void loadFromDiskTest() {
		JavaRDD<String> initialRdd = sc.textFile("src/main/resources/subtitles/input.txt");
		initialRdd.flatMap(value -> Arrays.asList(value.split(" ")).iterator())
					.foreach(val -> System.out.println(val));
	}
}
