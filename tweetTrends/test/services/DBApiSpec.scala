package services

import play.api.test.PlaySpecification
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.Handlers
import scala.concurrent.Future
import reactivemongo.api.MongoDriver
import reactivemongo.bson.BSONDocument
import org.specs2.execute.Results
import scala.concurrent.Await
import scala.concurrent.duration._
import models.Sentiment
import models.TweetDetails
import models.Score

object DBApiSpec extends PlaySpecification with Mockito with Results {
  "UserService#isAdmin" should {
    "return value findHandler" in {
      val result =Await.result(DBApi.findHandler("1"),1 seconds) 
      result must be equalTo Option(Handlers("1", List("@odersky")))
    }
    
    "return value sentimentQuery" in {
      val result =Await.result(DBApi.sentimentQuery("@pacoid"),1 seconds) 
      result must be equalTo Option(Sentiment("@pacoid", Some(0), Some(0),Some(2)))
    }
    
    "return value findTweetDetails" in {
      val result =Await.result(DBApi.findTweetDetails("@odersky"),1 seconds) 
      result must be equalTo List(TweetDetails(111,"s",Array("s"),"s","s",Score(1,1,1)))
    }
  }
}