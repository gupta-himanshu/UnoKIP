import scala.concurrent.Await
import reactivemongo.bson.BSONObjectID
import org.scalatest.BeforeAndAfter
import org.scalatest.FlatSpec
import scala.concurrent.duration.DurationInt
import com.knoldus.dbcrud.FindDoc
import scala.concurrent.ExecutionContext.Implicits.global
import com.knoldus.dbconnection.DBCrud
import com.knoldus.dbconnection.Connector
import reactivemongo.bson.Macros
import com.knoldus.crud.People

class FindDocTest extends FlatSpec with Connector with BeforeAndAfter {

  private val objectId = BSONObjectID.generate
  implicit val read = Macros.reader[People]
  implicit val write = Macros.writer[People]
  val db = connector("localhost", "rmongo", "rmongo", "pass")
  val findDoc = new FindDoc[People](db, "table1")
  val coll = db("table1")
  before {
    val res = new DBCrud[People](db, "table1").insert(People(objectId, "iii"))
    Await.result(res, 1 second)
  }
  after {
    coll.drop()
  }

  "fetch data with find" should "1" in {
    val res = findDoc.find(objectId.stringify)
    val finalRes = Await.result(res, 1 second)
    val expectedres = List(People(objectId, "iii"))
    assert(finalRes === expectedres)
  }
}