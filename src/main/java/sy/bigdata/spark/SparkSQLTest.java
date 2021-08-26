package sy.bigdata.spark;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import scala.reflect.ClassTag;

public class SparkSQLTest {

	private SparkSession ss;
	private Dataset<Row> dataset;
	
	private static String MODERN_ARTS = "Modern Art";
	
	public SparkSQLTest() {
		this.ss = SparkSession.builder().appName("testSQL").master("local[*]")
					.getOrCreate();
		dataset = ss.read().option("header", true).csv("src/main/resources/exams/students.csv");
	}
	
	public void show() {
		dataset.show();
		System.out.println("Total number of records in Dataset is "+ dataset.count());
	}
	
	public void basic() {
		Row firstRow = dataset.first();
		String subject = firstRow.getAs("subject");
		System.out.println(subject);
		
		int year = Integer.parseInt(firstRow.getAs("year"));
		System.out.println(year);
		
		//Use String Condition for filtering
		Dataset<Row> modernArts = dataset.filter("subject = 'Modern Art' AND year >= '2007'"); //only get subject with "Modern Art"
		modernArts.show();
		
		//Use lambda function  NOTE: somehow you need to cast the parameter
		Dataset<Row> modernArts2 = dataset.filter((Row row) -> row.getAs("subject").equals(MODERN_ARTS)
													&& Integer.parseInt(row.getAs("year"))>=2007);
		modernArts2.show();
		
		//Use Column type
		Column subjectColumn = dataset.col("subject");
		Column yearColumn = dataset.col("year");
		Dataset<Row> modernArts3 = dataset.filter(subjectColumn.equalTo(MODERN_ARTS)
															.and(yearColumn.geq(2007)));
		modernArts3.show();
		
	}
	
	public void testTempView() {
		dataset.createOrReplaceTempView("student"); //student is the name of the "table" for dataset
		
		Dataset<Row> results = ss.sql("select * from student where subject='French'");
		results.show();
		
		Dataset<Row> results1 = ss.sql("select student_id, score, grade from student where subject='French'");
		results1.show();
		
		ss.sql("select distinct(year) from student order by year").show();
	}
	
	public void testInMemory() {
		// This is an example of how to manually create a dataset
		List<Row> inMemory = new ArrayList<Row>();
		inMemory.add(RowFactory.create("WARN", "16 Dec 2018"));
		
		//StructField defines the two columns and types of the row
		StructField[] fields = new StructField[] {
				new StructField("level", DataTypes.StringType, false, Metadata.empty()),
				new StructField("datetime", DataTypes.StringType, false, Metadata.empty())
		};
		StructType schema = new StructType(fields);
		
		//map the schema to the row
		Dataset<Row> dataset = ss.createDataFrame(inMemory, schema);
		
		dataset.show();
		
	}
	
	public void close() {
		this.ss.close();
	}
}
