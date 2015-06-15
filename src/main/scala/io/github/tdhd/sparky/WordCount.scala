package io.github.tdhd.sparky

import language.postfixOps
import scala.concurrent.duration._
import scala.collection.mutable
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }
import org.apache.spark.{ SparkConf, SparkContext }
import org.jsoup.Jsoup

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Wordcount example").setMaster("local[1]")
    val ctx = new SparkContext(conf)

    // fetch featured article
    val featureArticle = Jsoup.connect("http://en.wikipedia.org/").get().select("#mp-tfa").text()

    // TODO: transform to DataFrame
    // TODO: add better NLP

    // map array to spark RDD[String] with 1 partition
    val news = ctx.parallelize(featureArticle.split(" ").map { _.toLowerCase }, 1)
    val transformations = news.map {
      word => (word, 1)
    }.reduceByKey {
      case (c1, c2) => c1 + c2
    }.map {
      case (word, count) => (count, word)
    }.sortByKey(false).map {
      case (count, word) => (word, count)
    }.map {
      case (word, count) => s"$word($count)"
    }

    println(transformations.collect.mkString(" "))
  }
}
