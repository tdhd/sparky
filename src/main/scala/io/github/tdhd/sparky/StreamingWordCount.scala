package io.github.tdhd.sparky

import language.postfixOps

import scala.concurrent.duration._
import scala.collection.mutable
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }
import org.apache.spark._
import org.apache.spark.streaming._
import akka.actor.ActorSystem
import akka.actor.Props

object StreamingWordCount {
  def main(args: Array[String]): Unit = {
    // "local[2]" to allow streaming
    val conf = new SparkConf().setAppName("Streaming wordcount example").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(1))

    // watch for new files in /tmp/crawl/
    val lines = ssc.textFileStream("file:///tmp/")
    val words = lines.flatMap(_.split(" "))
    // Count each word in each batch
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)
    // Print the first 20 elements of each RDD generated in this DStream to the console
    wordCounts.print(20)

    // text collection
    val system = ActorSystem("DataPublisher")
    val textCollector = system.actorOf(Props[DataPublisher], "textCollector")

    ssc.start() // Start the computation
    ssc.awaitTermination() // Wait for the computation to terminate

    // tell the text collector stop
    textCollector ! StopDataCollection
  }
}
