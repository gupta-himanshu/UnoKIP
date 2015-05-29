package utils

import models.Sentiment
/**
 * @author knoldus
 */
trait SentimentAnalysisUtility {

  val DEFAULT_SENTIMENT = Sentiment("", None, None, None)

  def getPostiveCount(sentiments: List[Option[Sentiment]]): Option[Int] = {
    val a = sentiments map (sentiment => sentiment.getOrElse(DEFAULT_SENTIMENT).positiveCount.getOrElse(0))
    Some(a.foldRight(0)(_ + _))
  }

  def getNegativeCount(sentiments: List[Option[Sentiment]]): Option[Int] = {
    val a = sentiments map (sentiment => sentiment.getOrElse(DEFAULT_SENTIMENT).negativeCount.getOrElse(0))
    Some(a.foldRight(0)(_ + _))
  }

  def getNeutralCount(sentiments: List[Option[Sentiment]]): Option[Int] = {
    val a = sentiments map (sentiment => sentiment.getOrElse(DEFAULT_SENTIMENT).neutralCount.getOrElse(0))
    Some(a.foldRight(0)(_ + _))
  }
}

object SentimentAnalysisUtility extends SentimentAnalysisUtility
