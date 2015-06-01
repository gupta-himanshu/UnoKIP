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
    "render dayWiseMenu template" in new WithApplication {
      val html = views.html.dayWiseMenu()
      contentAsString(html) must contain("smoothScroll")
    }
    
    "render footer template" in new WithApplication {
      val html = views.html.footer()
      contentAsString(html) must contain("Knoldus Software LLP")
    }
    
    "render header template" in new WithApplication {
      val html = views.html.header()
      contentAsString(html) must contain("images/twitter.jpg")
    }
    
    "render menubar template" in new WithApplication {
      val html = views.html.menubar()
      contentAsString(html) must contain("Home")
    }
    
    "render sessions template" in new WithApplication {
      val html = views.html.sessions()
      contentAsString(html) must contain("MyApp")
    }
    
    "render sidebar template" in new WithApplication {
      val html = views.html.sidebar()

      contentAsString(html) must contain("https://www.facebook.com/KnoldusSoftware")
    }
  }
}