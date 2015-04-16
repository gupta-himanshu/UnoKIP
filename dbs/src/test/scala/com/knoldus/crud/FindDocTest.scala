import scala.concurrent.Await
import reactivemongo.bson.BSONObjectID
import org.scalatest.BeforeAndAfter
import org.scalatest.FlatSpec
import scala.concurrent.duration.DurationInt
import com.knoldus.dbconnection.People
import com.knoldus.dbcrud.FindDoc
import scala.concurrent.ExecutionContext.Implicits.global
import com.knoldus.dbconnection.DBCrud
import com.knoldus.dbconnection.Connector
import reactivemongo.bson.Macros

class FindDocTest extends FlatSpec with Connector with BeforeAndAfter{

  private val objectId = BSONObjectID.generate
  val db = connector("localhost", "rmongo", "rmongo", "pass")
  val findDoc=new FindDoc(db,"table1")
  val coll=db("table1")
  implicit val write=Macros.reader[People]
    implicit val read=Macros.writer[People]
  before {
    coll.drop()
    val res = new DBCrud(db,"table1").insert(People(objectId, "iii"))
    Await.result(res, 1 second)
  }
  after {
    coll.drop()
  }

  "fetch data with find" should "1" in {
  
    val res = findDoc.find(People(objectId, "sss"))
    val finalRes = Await.result(res, 1 second)
    val expectedres = List(People(objectId, "iii"))
    assert(finalRes === expectedres)
  }
}