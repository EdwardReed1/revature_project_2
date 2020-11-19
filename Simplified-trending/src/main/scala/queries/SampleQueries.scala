package queries

import org.apache.spark.sql.functions.{asc, avg, bround, count, desc, from_unixtime, length, sum}
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{SparkSession, functions}

object SampleQueries{

  def main(args: Array[String]) = {

    val spark = SparkSession.builder()
      .appName("Sample queries")
      .master("local[4]")
      .getOrCreate()

    //trackTrendsOverTime(spark, args(0), args(1), args(2))

    //findHighestRank(spark, args(1), args(2))

    //findTotalAggregateHoursTrending(spark, args(1), args(2))

    //findHoursTrendingWithTrend(spark, args(0), args(1), args(2))

    //findNumberOfTweets(spark, args(1), args(2))

    //findNumberOfTweetsWithFilter(spark, args(0), args(1), args(2))

    //findAverageRankWhileTrending(spark, args(1), args(2))
  }

  /*
      Finds the average rank that each trending topic had while it was trending.
   */
  def findAverageRankWhileTrending(spark: SparkSession, inputPath: String, outputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "false").csv(inputPath)
      .withColumnRenamed("_c0", "Trend_Name")
      .withColumnRenamed("_c1", "Location")
      .withColumnRenamed("_c2", "Date")
      .withColumnRenamed("_c3", "Hour")
      .withColumnRenamed("_c4", "Rank")
      .withColumnRenamed("_c5", "Tweet_Volume")
      .withColumn("Hour", $"Hour".cast(IntegerType))
      .withColumn("Rank", $"Rank".cast(IntegerType))
      .withColumn("Tweet_Volume", $"Tweet_Volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.select("Trend_Name", "Rank")
      .groupBy("Trend_Name")
      .agg(bround(avg("Rank"), 2).alias("Average_rank_while_trending"))
      .orderBy(asc("Average_rank_while_trending"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)
  }

  /*
      Finds the number of tweets that a specific trend had while trending, if the value is 0
        then the tweet volume was not logged by the Twitter API
   */
  def findNumberOfTweetsWithFilter(spark: SparkSession, filtered: String, inputPath: String, outputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "false").csv(inputPath)
      .withColumnRenamed("_c0", "Trend_Name")
      .withColumnRenamed("_c1", "Location")
      .withColumnRenamed("_c2", "Date")
      .withColumnRenamed("_c3", "Hour")
      .withColumnRenamed("_c4", "Rank")
      .withColumnRenamed("_c5", "Tweet_Volume")
      .withColumn("Hour", $"Hour".cast(IntegerType))
      .withColumn("Rank", $"Rank".cast(IntegerType))
      .withColumn("Tweet_Volume", $"Tweet_Volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.filter(trend => trend.trend_Name.equalsIgnoreCase(filtered))
      .select("Trend_Name", "Tweet_Volume")
      .groupBy("Trend_Name")
      .agg(sum("Tweet_Volume").alias("Total_number_of_tweets"))
      .orderBy(desc("Total_number_of_tweets"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)
  }


  /*
      Find the number of tweets that al of the trends had. If the number is 0, then the
        number of tweets was not logged by the Twitter API
   */
  def findNumberOfTweets(spark: SparkSession, inputPath: String, outputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "false").csv(inputPath)
      .withColumnRenamed("_c0", "Trend_Name")
      .withColumnRenamed("_c1", "Location")
      .withColumnRenamed("_c2", "Date")
      .withColumnRenamed("_c3", "Hour")
      .withColumnRenamed("_c4", "Rank")
      .withColumnRenamed("_c5", "Tweet_Volume")
      .withColumn("Hour", $"Hour".cast(IntegerType))
      .withColumn("Rank", $"Rank".cast(IntegerType))
      .withColumn("Tweet_Volume", $"Tweet_Volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.select("Trend_Name", "Tweet_Volume")
      .groupBy("Trend_Name")
      .agg(sum("Tweet_Volume").alias("Total_number_of_tweets"))
      .orderBy(desc("Total_number_of_tweets"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)
  }


  /*
      This method will take a string and an input and output path as parameters and will show
        how many hours a single trend has been trending
   */
  def findHoursTrendingWithTrend(spark: SparkSession, filtered: String, inputPath: String, outputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "false").csv(inputPath)
      .withColumnRenamed("_c0", "Trend_Name")
      .withColumnRenamed("_c1", "Location")
      .withColumnRenamed("_c2", "Date")
      .withColumnRenamed("_c3", "Hour")
      .withColumnRenamed("_c4", "Rank")
      .withColumnRenamed("_c5", "Tweet_Volume")
      .withColumn("Hour", $"Hour".cast(IntegerType))
      .withColumn("Rank", $"Rank".cast(IntegerType))
      .withColumn("Tweet_Volume", $"Tweet_Volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    val newDF = trendDS.filter(trend => trend.trend_Name.equalsIgnoreCase(filtered))

    newDF.select("Trend_Name")
      .groupBy("Trend_Name")
      .agg(count("Trend_Name").alias("Hours_trending"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)
  }

  /*
      This method will retrieve the total number of times that each trending topic comes
      up in the data, which can be represented as the total hours that a trend is trending
   */
  def findTotalAggregateHoursTrending(spark: SparkSession, inputPath: String, outputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "false").csv(inputPath)
      .withColumnRenamed("_c0", "Trend_Name")
      .withColumnRenamed("_c1", "Location")
      .withColumnRenamed("_c2", "Date")
      .withColumnRenamed("_c3", "Hour")
      .withColumnRenamed("_c4", "Rank")
      .withColumnRenamed("_c5", "Tweet_Volume")
      .withColumn("Hour", $"Hour".cast(IntegerType))
      .withColumn("Rank", $"Rank".cast(IntegerType))
      .withColumn("Tweet_Volume", $"Tweet_Volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.select("Trend_Name")
      .groupBy("Trend_Name")
      .agg(count("Trend_Name").alias("Total_aggregate_hours_trending"))
      .orderBy(desc("Total_aggregate_hours_trending"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)

  }


  /*
      This method will show the name, location, date, time, and rank at the time that every trend was at it's
        highest trending point. If a trend was at its highest trending point for more than one hour, then it will
        be displayed each time.
   */
  def findHighestRank(spark: SparkSession, inputPath: String, outputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "false").csv(inputPath)
      .withColumnRenamed("_c0", "Trend_Name")
      .withColumnRenamed("_c1", "Location")
      .withColumnRenamed("_c2", "Date")
      .withColumnRenamed("_c3", "Hour")
      .withColumnRenamed("_c4", "Rank")
      .withColumnRenamed("_c5", "Tweet_Volume")
      .withColumn("Hour", $"Hour".cast(IntegerType))
      .withColumn("Rank", $"Rank".cast(IntegerType))
      .withColumn("Tweet_Volume", $"Tweet_Volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    val joinedDF = trendDS.select(trendDS("Trend_Name").alias("Name"), trendDS("Rank"))
      .groupBy("Name")
      .agg(functions.min("Rank").alias("Highest_trending_rank"))

    trendDS.join(joinedDF, trendDS("Rank") === joinedDF("Highest_trending_rank")
    && trendDS("Trend_Name") === joinedDF("Name"), "inner")
      .orderBy(asc("Highest_trending_rank"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)
  }

  /*
      This method will find every time that a given trend name has been trending
        and display the name, location, date, time, and rank starting with when it first started trending, allowing us
        to see how the the trend changed ranking wise and from location to location
   */
  def trackTrendsOverTime(spark: SparkSession, filtered: String, inputPath: String, outputPath: String) = {

    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF= spark.read.option("header", "false").csv(inputPath)
      .withColumnRenamed("_c0", "Trend_Name")
      .withColumnRenamed("_c1", "Location")
      .withColumnRenamed("_c2", "Date")
      .withColumnRenamed("_c3", "Hour")
      .withColumnRenamed("_c4", "Rank")
      .withColumnRenamed("_c5", "Tweet_Volume")
      .withColumn("Hour", $"Hour".cast(IntegerType))
      .withColumn("Rank", $"Rank".cast(IntegerType))
      .withColumn("Tweet_Volume", $"Tweet_Volume".cast(IntegerType))

    val trendDS = trendDF.as[Trend]

    trendDS.filter(trend => trend.trend_Name.equalsIgnoreCase(filtered))
      .select("Trend_Name", "Location", "Date", "Hour")
     .orderBy(asc("Date"), asc("Hour"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)

  }



  //Creates the case class for the data received from the twitter queries
  case class Trend(trend_Name: String, location: String, date: String, hour: Long, rank: Long, tweet_Volume: Long) {

  }
}
