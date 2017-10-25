package pl.lodz.p.edu.views

import io.udash._
import io.udash.core.DefaultViewPresenterFactory
import pl.lodz.p.edu.RPCDemoState
import pl.lodz.p.edu.styles.DemoStyles

import scala.util.{Failure, Success}
import scalacss.ScalatagsCss._

case object RPCDemoViewPresenter extends DefaultViewPresenterFactory[RPCDemoState.type](() => {
  import pl.lodz.p.edu.Context._

  val serverResponse = Property[String]("???")
  val input = Property[String]("")
  input.listen((value: String) => {
    serverRpc.hello(value).onComplete {
      case Success(resp) => serverResponse.set(resp)
      case Failure(_) => serverResponse.set("Error")
    }
  })

  serverRpc.pushMe()

  new RPCDemoView(input, serverResponse)
})

class RPCDemoView(input: Property[String], serverResponse: Property[String]) extends View {
  import scalatags.JsDom.all._

  private val content = div(
    h2(
      "You can find this demo source code in: ",
      i("pl.lodz.p.edu.views.RPCDemoView")
    ),
    h3("Example"),
    TextInput.debounced(input, placeholder := "Type your name..."),
    p("Server response: ", bind(serverResponse)),
    h3("Read more"),
    a(DemoStyles.underlineLinkBlack)(href := "http://guide.udash.io/#/rpc", target := "_blank")("Read more in Udash Guide.")
  )

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}