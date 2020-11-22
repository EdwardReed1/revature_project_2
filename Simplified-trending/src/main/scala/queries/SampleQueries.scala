package queries

import org.apache.spark.sql.functions.{asc, avg, bround, count, countDistinct, desc, sum}
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{SparkSession, functions}

object SampleQueries{

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Sample queries")
      .master("local[4]")
      .getOrCreate()

    //trackTrendsOverTime(spark, args(0), args(1), args(2))

    //findHighestRank(spark, args(1), args(2))

    //findHoursTrendingWithTrend(spark, args(0), args(1), args(2))

    //findNumberOfTweets(spark, args(1), args(2))

    //findNumberOfTweetsWithFilter(spark, args(0), args(1), args(2))

    //findAverageRankWhileTrending(spark, args(1), args(2))

    //showTopTrends(spark, args(1), args(2))

    //showTopTrendsWithLocation(spark, args(0), args(1), args(2))
  }

  /**
   * This function will show the name, date, hour, and rank for every trend that was trending at the number one spot
   *  for a given location.
   *
   * This function corresponds with Show-Top-Trends when using three parameters.
   *
   * @param spark:      The sparksession that will run the query on the dataset.
   * @param filter:    The location that the query will filter by
   * @param inputPath:  The path to the dataset that is being queried.
   * @param outputPath  The path to the file where the output will be stored (preferably a csv file.)
   */
  def showTopTrendsWithLocation(spark: SparkSession, filter: String, inputPath: String, outputPath: String): Unit = {
    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    val trendDF = spark.read.option("header", "false").csv(inputPath)
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

    trendDS.filter(trend => trend.rank == 1 && trend.location.equalsIgnoreCase(filter))
      .select("Trend_Name", "Location", "Date", "Hour", "Rank")
      .orderBy(asc("Location"), asc("Date"), asc("Hour"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep", ", ")
      .save(outputPath)

  }

  /**
   * This method will, for every trend in the dataset, show at what point in time and in which
   *  location that it was at it's highest rending point.
   *
   * This function corresponds with Show-Top-Trends when using two parameters.
   *
   * Note: if a trend is at its highest for more than one point (ex. trending # 1 for multiple hours
   *  or cities) then it will appear more than once in the output.
   *
   * @param spark:      The sparksession that runs the sparksql
   * @param inputPath:   The path to the dataset that is being queried.
   * @param outputPath:  The path to where the output file will be stored (preferably a csv file).
   */
  def showTopTrends(spark: SparkSession, inputPath: String, outputPath: String): Unit = {
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

    trendDS.filter(trend => trend.rank == 1)
      .select("Trend_Name", "Location", "Date", "Hour", "Rank")
      .orderBy(asc("Location"), asc("Date"),asc( "Hour"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)

  }

  /**
   * This method will, for every trend in the dataset, show the average rank of the trend and will
   *  find the total number of hours it has been cumulatively trending for throughout all of the locations.
   *
   *  ex: if a trend was trending for one hour in Boston and one hour in Houston, than it has been
   *  trending for two hours total.
   *
   *  This method corresponds with Find-Average-Rank.jar
   *
   * @param spark:      The sparksession that runs the sparksql
   * @param inputPath   The path to the dataset that is being queried.
   * @param outputPath  The path to where the output file will be stored (preferably a csv file).
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

    val averageDS = trendDS.select("Trend_Name", "Rank")
      .groupBy("Trend_Name")
      .agg(bround(avg("Rank"), 2).alias("Average_rank_while_trending"))
      .orderBy(asc("Average_rank_while_trending"))

    val countedDS = trendDS.select(trendDS("Trend_Name").alias("Name"))
      .groupBy("Name")
      .agg(count("Name").alias("Total_hours_trending"))

    val joinedDS = averageDS.join(countedDS, averageDS("Trend_Name") === countedDS("Name"), "inner")
      .select(averageDS("Trend_Name"), averageDS("Average_rank_while_trending"), countedDS("Total_hours_trending"))
      .orderBy(asc("Average_rank_while_trending"), desc("Total_hours_trending"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)
  }

  /**
   * This method will, for a given trend, display how many tweets that tweet had.
   *
   * Note: in the TwitterAPI, some tweets did not have their number of tweets recorded and they
   *  were listed as null. We handled this problem by having them appear as 0 in the dataset.
   *
   * This method corresponds to Find-Number-Of-Tweets.jar when ran using three parameters.
   *
   * @param spark:       the sparksession that will run the query on the dataset.
   * @param filter:    the name of the trend that will be shown in the query.
   * @param inputPath:   the path to the dataset that is being queried.
   * @param outputPath:  the path to the file where the output will be stored (preferably a csv file).
   */
  def findNumberOfTweetsWithFilter(spark: SparkSession, filter: String, inputPath: String, outputPath: String) = {

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

    trendDS.filter(trend => trend.trend_Name.equalsIgnoreCase(filter))
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


/**
 * This method will display how many tweets every trending tweet had in descending order of the
 *  amount of tweets.
 *
 * Note: in the TwitterAPI, some tweets did not have their number of tweets recorded and they
 *  were listed as null. We handled this problem by having them appear as 0 in the dataset.
 *
 * This method corresponds to Find-Number-Of-Tweets.jar when using two parameters.
 *
 * @param spark:       the sparksession that will run the query on the dataset.
 * @param inputPath:   the path to the dataset that is being queried.
 * @param outputPath:  the path to the file where the output will be stored (preferably a csv file).
 * */
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


/**
 * This method will, for a given trend, find the number of hours that it was trending
 *  between every location in the dataset.
 *
 * This method corresponds with Find-Hours-Trending.jar
 *
 * Ex. if the trend was trending at the same time in two different cities, it would count
 *  as one hour.
 *
 * @param spark:       the sparksession that will run the query on the dataset.
 * @param filter:    the name of the trend that will be shown in the query.
 * @param inputPath:   the path to the dataset that is being queried.
 * @param outputPath:  the path to the file where the output will be stored (preferably a csv file).
 */
  def findHoursTrendingWithTrend(spark: SparkSession, filter: String, inputPath: String, outputPath: String) = {

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

    val newDF = trendDS.filter(trend => trend.trend_Name.equalsIgnoreCase(filter))

    newDF.select("Trend_Name", "Hour")
      .groupBy("Trend_Name")
      .agg(countDistinct("Hour").alias("Hours_trending"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)
  }

  /**
   * This method will, for each trend in the dataset, find the location, date, and hour
   *  that the trend was at it's highest trending point.
   *
   * Note: if a trend was at that point more than once (ex. trending # 1 at multiple
   *  locations or times) then it will appear that many times in the output
   *
   * This trend corresponds with Find-Highest-Ranks.jar
   *
   * @param spark:      The sparksession that will run the query on the dataset.
   * @param inputPath   The path to the dataset that is being queried.
   * @param outputPath  The path to where the output file will be stored (preferably a csv file).
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
      .orderBy(asc("Highest_trending_rank"), asc("Trend_Name"))
      .coalesce(1)
      .write
      .format("csv")
      .option("header", "true")
      .mode("overwrite")
      .option("sep",", ")
      .save(outputPath)
  }

  /**
   * This method will, for a given trend, find every time that the trend was trending and will
   *  write the file, the name, location, date, hour, and rank that the trend had at that time.
   *
   * With this method you can can see how a trend moved from location to location throughout
   *  it's trending lifetime.
   *
   * This method corresponds with TrendTracker.
   *
   * @param spark:       the sparksession that will run the query on the dataset.
   * @param filter:    the name of the trend that will be shown in the query.
   * @param inputPath:   the path to the dataset that is being queried.
   * @param outputPath:  the path to the file where the output will be stored (preferably a csv file).
   */
  def trackTrendsOverTime(spark: SparkSession, filter: String, inputPath: String, outputPath: String) = {

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

    trendDS.filter(trend => trend.trend_Name.equalsIgnoreCase(filter))
      .select("Trend_Name", "Location", "Date", "Hour", "Rank")
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
  case class Trend(trend_Name: String, location: String, date: String, hour: Long, rank: Long, tweet_Volume: String) {

  }
}
