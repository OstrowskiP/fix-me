package pl.lodz.p.edu.views

import io.udash._
import io.udash.properties.single.Property
import pl.lodz.p.edu.LoginState
import pl.lodz.p.edu.styles.DemoStyles

import scala.util.{Failure, Success}
import scalacss.ScalatagsCss._

class LoginPresenter extends Presenter[LoginState.type]{
  override def handleState(state: LoginState.type): Unit = {

  }
}

case object LoginViewPresenter extends DefaultViewPresenterFactory[LoginState.type](() => {
  import pl.lodz.p.edu.Context._

  val login = Property[String]
  val password = Property[String]
  val clicked = Property[Boolean]
  val serverResponse = Property[String]("???")

  clicked.listen(_ => {
    serverRpc.login("", login.get).onComplete {
      case Success(resp) => serverResponse.set(resp)
      case Failure(_) => serverResponse.set("Error")
    }
  })

  new LoginView(login, password, serverResponse, clicked)
})

class LoginView(login: Property[String], password: Property[String], serverResponse: Property[String], clicked: Property[Boolean]) extends View {
  import scalatags.JsDom.all._
  import pl.lodz.p.edu.Context._

  private val content = div(
    h2(
      "Zaloguj się"
    ),
    ul(
      li(DemoStyles.navItem)(
        p("Login:")
      ),
      li(DemoStyles.navItem)(
        TextInput.debounced(login, placeholder := "Twój login")
      )
    ),
    ul(
      li(DemoStyles.navItem)(
        p("Hasło:")
      ),
      li(DemoStyles.navItem)(
        PasswordInput.debounced(password, placeholder := "Twoje hasło"),
        Checkbox(clicked)
      )
    ),
    p("Wpisałeś login: ", bind(login)),
    p("Wpisałeś hasło: ", bind(password)),
    p("Hash SHA256 hasła: ", bind(serverResponse))
  )

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}