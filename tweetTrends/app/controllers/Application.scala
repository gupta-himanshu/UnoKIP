package controllers

import scala.concurrent.duration.DurationInt
import models.User
import play.api.mvc.Action
import play.api.mvc.Controller
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.Macros
import reactivemongo.bson.BSONDocumentWriter
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.util.Failure
import scala.util.Success
import models.FindDoc
import models.DBCrud
import play.api.mvc.AnyContent

object Application extends Controller {

  implicit val reader: BSONDocumentReader[User] = Macros.reader[User]
  implicit val writer: BSONDocumentWriter[User] = Macros.writer[User]
  
  val findDoc = FindDoc
  val dbcrud = DBCrud
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def save: Action[AnyContent] = Action.async {
    val user = User("Pushpendu", "Purkait", 23)
    val insert = dbcrud.insert(user)
    insert.map { x =>
      Ok(x.toString())
    }
  }

  def show:Action[AnyContent] = Action.async {
    val show = findDoc.findWholeDoc().collect[List]()
    show.map { x =>
      Ok(x.toString())
    }
  }
}
