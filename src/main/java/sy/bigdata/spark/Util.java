package sy.bigdata.spark;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;


public class Util {

	private static Set<String> borings = new HashSet<String>();
	
	static {
		InputStream is = SparkPractice.class.getResourceAsStream("/subtitles/boringwords.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		br.lines().forEach(it -> borings.add(it));
	}
	
	public static boolean isBoring(String str) {
		return borings.contains(str);
	}
	
	public static boolean isNotBoring(String str) {
		return !isBoring(str);
	}
}
