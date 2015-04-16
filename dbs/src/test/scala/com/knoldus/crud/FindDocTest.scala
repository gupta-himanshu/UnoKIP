

import scala.concurrent.Await
import reactivemongo.bson.BSONObjectID
import org.scalatest.BeforeAndAfter
import org.scalatest.FlatSpec
import scala.concurrent.duration.DurationInt
import com.knoldus.dbconnection.People
import com.knoldus.dbcrud.FindDoc
import scala.concurrent.ExecutionContext.Implicits.global
import com.knoldus.dbconnection.DBCrud

class FindDocTest extends FlatSpec with FindDoc with BeforeAndAfter with DBCrud {

  private val objectId = BSONObjectID.generate
  val db = connector("localhost", "rmongo", "rmongo", "pass")
  implicit val coll = db("table1")
  before {
    coll.drop()
    val res = insert(People(objectId, "iii"))
    Await.result(res, 1 second)
  }
  after {
    coll.drop()
  }

  "fetch data with find" should "1" in {
    val res = find(People(objectId, "sss"))
    val finalRes = Await.result(res, 1 second)
    val expectedres = List(People(objectId, "iii"))
    assert(finalRes === expectedres)
  }
}