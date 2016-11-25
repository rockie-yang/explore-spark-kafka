package com.rockiey.spark

import java.nio.file.Files

import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
  * Created by Rockie Yang on 2016/06/16.
  */
object SparkEnv {


  //  lazy val sc = {
  //        val master = "local"
  ////    val master = "spark://Rockies-MacBook-Pro.local:7077"
  //    val conf = new SparkConf()
  //      .setAppName("Simple Application")
  //      .setMaster(master)
  ////      .setJars(Seq("/Users/rockieyang/git/spark-highcharts/target/spark-highcharts-0.6.1.jar"))
  //    new SparkContext(conf)
  //
  //
  //  }

    val checkpointPath = Files.createTempDirectory("query")
    val checkpointDir = checkpointPath.toFile

    checkpointDir.deleteOnExit()

    def deleteRecursively(file: java.io.File): Unit = {
      if (file.isDirectory) {
        file.listFiles().foreach(deleteRecursively)
        file.delete()
      }
      else
        file.delete()
    }

    def clearCheckpointDir: Unit = {
      checkpointDir.listFiles().foreach(deleteRecursively)
    }

    lazy val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.sql.streaming.checkpointLocation",
        checkpointDir.getAbsolutePath)
      .master("local[*]")
      .appName("test")
      .getOrCreate()

  lazy val sc = spark.sparkContext

  //  val sqlContext= new org.apache.spark.sql.SQLContext(sc)
  lazy val sqlContext = spark.sqlContext

  val ssc = new StreamingContext(SparkEnv.sc, Seconds(5))
  //  def createDF(seq: Seq[Any]) = {
  //    sqlContext.createDataFrame(seq)
  //  }

}
