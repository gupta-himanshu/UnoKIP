package controllers

import java.util.concurrent.TimeoutException

import com.knoldus.db.DBServices
import com.knoldus.twittertrends.BirdTweet
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Controller
import com.knoldus.utils.ConstantUtil

object Application extends Application {
  val dbService = DBServices
  val birdTweet = BirdTweet
}

trait Application extends Controller{
  this: Controller =>

  val dbService: DBServices
  val birdTweet: BirdTweet

  def trending: Action[AnyContent] = Action.async {
  //  val pageNum=dbService.findTrends().map{ x => x.head.pageNum }
    val tweets = dbService.findWholeDoc()
    val trends = dbService.findTrends() 
    val res = tweets.flatMap { x => trends.map { y => birdTweet.trending(x, y,1) } }
    res.map { r => Ok(views.html.showData(r)) }.recover {
      case t: TimeoutException => InternalServerError(t.getMessage)
    }
  }
  
  def ins=Action.async{
    val ins=dbService.filterQuery(2, ConstantUtil.pageSize)
    ins.map { x => Ok(views.html.showData1(x)) }.recover {
      case t: TimeoutException => InternalServerError(t.getMessage)
    }  
  }
  
//  def chunk=Action{
//    val s=dbService.findchunk(1, 100)
//     s.map { r => Ok(views.html.showData1(r)) }
//    Ok("ss")
//  }
}
