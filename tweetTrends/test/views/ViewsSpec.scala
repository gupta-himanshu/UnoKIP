package views

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test.PlaySpecification
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class ViewsSpec extends PlaySpecification {

  "Views" should {
    "render chart template" in new WithApplication {
      val html = views.html.chart()

      contentAsString(html) must contain("table-body")
    }
    
    "render datepicker template" in new WithApplication {
      val html = views.html.datepicker()

      contentAsString(html) must contain("input-group")
    }
    
    "render dayWiseMenu template" in new WithApplication {
      val html = views.html.dayWiseMenu()

      contentAsString(html) must contain("smoothScroll")
    }
    
    "render footer template" in new WithApplication {
      val html = views.html.footer()

      contentAsString(html) must contain("Knoldus Software LLP")
    }
    
    "render handleWisePage template" in new WithApplication {
      val html = views.html.handleWisePage("Person1","#6ee094","userFemale.png")

      contentAsString(html) must contain("Person1")
    }
    
    "render header template" in new WithApplication {
      val html = views.html.header()

      contentAsString(html) must contain("images/twitter.jpg")
    }
    
/*    "render header template" in new WithApplication {
      val html = views.html.main("twitter")("<h1>sd</h1>")

      contentAsString(html) must contain("twitter")
    }
*/    
    "render menubar template" in new WithApplication {
      val html = views.html.menubar()

      contentAsString(html) must contain("Home")
    }
    
    "render sessions template" in new WithApplication {
      val html = views.html.sessions()

      contentAsString(html) must contain("MyApp")
    }
    
    "render showData template" in new WithApplication {
      val html = views.html.showData()

      contentAsString(html) must contain("Twitter Trends")
    }
    
    "render sidebar template" in new WithApplication {
      val html = views.html.sidebar()

      contentAsString(html) must contain("https://www.facebook.com/KnoldusSoftware")
    }
  }
}