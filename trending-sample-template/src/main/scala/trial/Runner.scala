package trial

import org.apache.spark.sql.functions.{avg, count, desc, from_unixtime, length}
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{SparkSession, functions}

object Runner {

  def main(args: Array[String]) = {

    val spark = SparkSession.builder()
      .appName("Hello Spark SQL")
      .master("local[4]")
      .getOrCreate()

    //use the commented out method declaration to use in a jar file with two arguments
    //twitterDataAsJson(spark, args(0), args(1))
    twitterDataAsJson(spark)
  }

  //use the commented out method declaration to use in a jar file with two arguments
  //def twitterDataAsJson(spark: SparkSession, inputPath: String, outputPath: String) = {
  def twitterDataAsJson(spark: SparkSession) = {
    import spark.implicits._

    spark.sparkContext.setLogLevel("WARN")

    //this will read every json file in a directory and put into the same dataframe
    //this can be changed to
    val Trendingdf = spark.read.option("multiline", "true").json("Phoenix_jsons")

    //comment out the line above and use this one to make it read the arguments passed into it
    //val Trendingdf = spark.read.option("multiline", "true").json(inputPath)

    //in order to write to the second argument put this line at the end of any query that you are running
    //.write.json(outputPath)

    Trendingdf.show()
    Trendingdf.printSchema()


    //simple query to show the names of trending items

    Trendingdf.groupBy("name").count().show()

    //this will show the name and tweet_volume of tweets and will show them in descending order
    //  of tweet volume

    Trendingdf.select("name", "tweet_volume").orderBy(desc("tweet_volume")).show()

    //this will show the name and will display how many times that name showed up in the json files
    //  which should be the total hours trending.  It will then show them in descending order of the
    //  total trending hours.

    Trendingdf.groupBy("name")
      .agg(count("name").alias("Total hours trending"))
      .orderBy(desc("Total hours trending"))
      .show()

    //this is similar to the query above but it takes the average of the tweet volume instead
    Trendingdf.groupBy("name")
      .agg(avg("tweet_volume").alias("Average tweet volume per hour"))
      .orderBy(desc("Average tweet volume per hour"))
      .show()

    //creating a dataset from the dataframe

    val Trendingds = Trendingdf.as[Trend]

    Trendingds.show()

    Trendingds.printSchema()

    Trendingds.filter(trend => trend.name.length < 6).show()


  }

  //Note about the case class, if you look closely you will see that tweet_volume is a String,
  //  That is because some of the elements has a null value for their tweet_volume so it can not
  //  be converted into an integer type.  As a result a null check should be ran before running an
  //  operation on that field
  //Something that is interesting however is that it doesn't seem to be problem when using it as a dataframe
  //  I'm guessing because it creates the variable type at runtime instead of having it be pre-determined like
  //  in a DataSet.

  case class Trend(name: String, promoted_content: String, query: String, tweet_volume: String, url: String) {

  }
}


