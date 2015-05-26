
import java.util.Date
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.knoldus.twittertrends.SentimentAnalysis
import com.knoldus.model.Tweet


/**
 * @author knoldus
 */


object TestSentiment1 extends App{
val conf=new SparkConf().setMaster("local[*]").setAppName("ss")
val sc=new SparkContext(conf)
  val tweets=List(Tweet(591216111431142400L, "<a href=http://fathir.mazaa.us rel=nofollow>Aplikasi #KakakFathir</a>",
      "i m soo happy and feeling amazing lil sad", false, "Somen||48", "amat_skate48", "http://jkt48.com", 2880640850L, "en",
      new Date, Some("new york"), Some("new york"), Some(23554221), Some(43545423)))
 
  val tweetRDD=sc.parallelize(tweets)
  
  SentimentAnalysis.sentimentAnalysis(tweetRDD)
}