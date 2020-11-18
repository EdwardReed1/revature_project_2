package queries

import org.apache.spark.sql.functions.{asc, avg, count, desc, from_unixtime, length, sum}
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{SparkSession, functions}

object SampleQueries{

  def main(args: Array[String]) = {

    val spark = SparkSession.builder()
      .appName("Sample queries")
      .master("local[4]")
      .getOrCreate()

    //trackTrendsOverTime(spark, args(0), args(1))

    //findHighestRank(spark, args(1))

    //findTotalAggregateHoursTrending(spark, args(1))

    //findHoursTrendingWithTrend(spark, args(0), args(1))

    //findNumberOfTweets(spark, args(1))

    //findNumberOfTweetsWithFilter(spark, args(0), args(1))

    //findAverageRankWhileTrending(spark, args(1))
  }

  /*
      Finds the average rank that each trending topic had while it was trending.
   */
  def findAverageRankWhileTrending(spark: SparkSession, inputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "true").csv(inputPath)
      .withColumn("hour", $"hour".cast(IntegerType))
      .withColumn("rank", $"rank".cast(IntegerType))
      .withColumn("volume", $"volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.select("name", "rank")
      .groupBy("name")
      .agg(avg("rank").alias("Average rank while trending"))
      .orderBy(asc("Average rank while trending"))
      .show()
  }

  /*
      Finds the number of tweets that a specific trend had while trending, if the value is 0
        then the tweet volume was not logged by the Twitter API
   */
  def findNumberOfTweetsWithFilter(spark: SparkSession, filtered: String, inputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "true").csv(inputPath)
      .withColumn("hour", $"hour".cast(IntegerType))
      .withColumn("rank", $"rank".cast(IntegerType))
      .withColumn("volume", $"volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.filter(trend => trend.name.equalsIgnoreCase(filtered))
      .select("name", "volume")
      .groupBy("name")
      .agg(sum("volume").alias("Total number of tweets"))
      .orderBy(desc("Total number of tweets"))
      .show()
  }


  /*
      Calculates the total number of all of the trends
   */
  def findNumberOfTweets(spark: SparkSession, inputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "true").csv(inputPath)
      .withColumn("hour", $"hour".cast(IntegerType))
      .withColumn("rank", $"rank".cast(IntegerType))
      .withColumn("volume", $"volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.select("name", "volume")
      .groupBy("name")
      .agg(sum("volume").alias("Total number of tweets"))
      .orderBy(desc("Total number of tweets"))
      .show()
  }


  /*
      This method will take a string and an input and output path as parameters and will show
        how many hours a single trend has been trending, not aggregate just actual hours
   */
  def findHoursTrendingWithTrend(spark: SparkSession, filtered: String, inputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "true").csv(inputPath)
      .withColumn("hour", $"hour".cast(IntegerType))
      .withColumn("rank", $"rank".cast(IntegerType))
      .withColumn("volume", $"volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    val newDF = trendDS.filter(trend => trend.name.equalsIgnoreCase(filtered))

    newDF.select("name")
      .groupBy("name")
      .agg(count("name").alias("Hours trending"))
      .show()
  }

  /*
      This method will retrieve the total number of times that each trending topic comes
      up in the data, which can be represented as the total hours that a trend is trending
   */
  def findTotalAggregateHoursTrending(spark: SparkSession, inputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "true").csv(inputPath)
      .withColumn("hour", $"hour".cast(IntegerType))
      .withColumn("rank", $"rank".cast(IntegerType))
      .withColumn("volume", $"volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.select("name")
      .groupBy("name")
      .agg(count("name").alias("Total aggregate hours trending"))
      .orderBy(desc("Total aggregate hours trending"))
      .show()

  }


  /*
      This method will show the name, location, date, time, and rank at the time that every trend was at it's
        highest trending point.
   */
  def findHighestRank(spark: SparkSession, inputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "true").csv(inputPath)
      .withColumn("hour", $"hour".cast(IntegerType))
      .withColumn("rank", $"rank".cast(IntegerType))
      .withColumn("volume", $"volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]


    val joinedDF = trendDS.select("name","rank")
      .groupBy("name")
      .agg(functions.min("rank").alias("Highest trending rank"))

    joinedDF.printSchema()

    trendDS.join(joinedDF, trendDS("rank") === joinedDF("Highest trending rank")
    && trendDS("name") === joinedDF("name"), "inner")
      .orderBy(asc("Highest trending rank"))
      .show()

  }

  /*
      This method will find every time that a given trend name has been trending
        and display the name, location, date, time, and rank starting with when it first started trending, allowing us
        to see how the the trend changed ranking wise and from location to location
   */
  def trackTrendsOverTime(spark: SparkSession, filtered: String, inputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "true").csv(inputPath)
      .withColumn("hour", $"hour".cast(IntegerType))
      .withColumn("rank", $"rank".cast(IntegerType))
      .withColumn("volume", $"volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.filter(trend => trend.name.equalsIgnoreCase(filtered))
      .select("name", "location", "date", "hour")
     .orderBy(asc("date"), asc("hour"))
     .show()
  }



  //Creates the case class for the data received from the twitter queries
  case class Trend(name: String, location: String, date: String, hour: Long, rank: Long, volume: Long) {

  }
}
