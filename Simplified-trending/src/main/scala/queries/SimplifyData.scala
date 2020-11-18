package queries

import org.apache.spark
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object SimplifyData {

  def main(args: Array[String]) = {

    val appName = "Final simplifier"

    val master = "local[4]"

    //val conf = new SparkConf().setAppName(appName).setMaster(master)

    //val sc = new SparkContext(conf)

    val spark = SparkSession.builder()
      .appName("Other way")
      .master("local[4]")
      .getOrCreate()

    //simplifyOutput(sc)
    otherSimplify(spark)
  }

  def simplifyOutput(sc: SparkContext)= {

    val myRdd = sc.textFile("sample_data.csv")
      .map(f=>{
        f.split(",")
      })


    //println(myRdd.collect().mkString(","))
    myRdd.foreach(f=>{
      //println(f(0) + " " + f(1)
      if (!f(2).equalsIgnoreCase("date")) {
        println(s"${f(0)}, ${f(1)}, ${f(2).substring(11, 13)}, ${f(3)}, ${f(0)}")
      }
      else {
        println(s"${f(0)}, ${f(1)}, ${f(2)}, ${f(3)}, ${f(0)}")
      }
    })

    val secRdd = myRdd.map(trend => (trend(0), trend(1)))
    secRdd.foreach(println)
  }

  def otherSimplify(spark: SparkSession) = {
    val myRdd = spark.read.option("header", "true").csv("sample_data.csv").rdd

   // myRdd.foreach(f => {
   //   println(s"${f(0)}, ${f(1)}, ${f(2)}, ${f(3)}, ${f(4)}")
   // })

    println(" ")

    val secRdd = myRdd.map(trend =>
      if (trend(0).toString.equalsIgnoreCase("name")) {
        (trend(0).toString, trend(1).toString, trend(2).toString, "Hour(24-hour clock)", "Rank", trend(4).toString)
      }
      else {
        (trend(0).toString, trend(1).toString, trend(2).toString.substring(0, 9), trend(2).toString.substring(11, 13), trend(3).toString.toLong, trend(4).toString.toLong)
      }

    )

    secRdd.foreach(println)
  }

}
