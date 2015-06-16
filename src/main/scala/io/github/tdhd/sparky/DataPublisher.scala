package io.github.tdhd.sparky

import language.postfixOps
import scala.concurrent.duration._
import scala.collection.mutable
import scala.concurrent.Future
import scala.util.{ Failure, Success, Try }
import org.apache.spark.{ SparkConf, SparkContext }
import org.jsoup.Jsoup
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.ActorLogging
import akka.actor.Cancellable

case object StartDataCollection
case object StopDataCollection

class DataPublisher extends Actor with ActorLogging {
  import context.dispatcher

  var scheduler: Option[Cancellable] = Option.empty[Cancellable]

  override def preStart(): Unit = {
    self ! StartDataCollection
  }

  // creates a random file with contents from a random url
  def fileAppender() = {
    val urls = List(("http://en.wikipedia.org/", "#mp-tfa"), ("http://karpathy.github.io/2015/05/21/rnn-effectiveness/", ".post"))
    val featureArticle = scala.util.Random.shuffle(urls).head match {
      case (url, selector) =>
        Jsoup.connect(url).get().select(selector).text()
    }

    val filename = s"file_${scala.util.Random.nextInt}.csv"
    scala.tools.nsc.io.File(s"/tmp/$filename").writeAll(featureArticle) //appendAll(s"${scala.util.Random.nextString(10)}\n")
  }

  def receive = {
    case StartDataCollection =>
      // set up scheduler to append to a file every second
      scheduler = Option { context.system.scheduler.schedule(1 seconds, 0.5 seconds)(fileAppender) }
    case StopDataCollection =>
      // stop scheduler
      scheduler.foreach { _.cancel }
      // stop data collection actor
      context.stop(self)
    case a: Any =>
      log.info("Received UNKNOWN message {}", a)
  }
}
