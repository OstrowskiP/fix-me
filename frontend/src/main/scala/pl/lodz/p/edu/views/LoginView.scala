package pl.lodz.p.edu.views

import io.udash.{DefaultViewPresenterFactory, _}
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

  val response = Property[String]("???")
  val buttonClicked = Property[Boolean]
  val login = Property[String]
  val password = Property[String]

  buttonClicked.listen(_ => {
    serverRpc.login(login.get, password.get).onComplete {
      case Success(resp) => response.set(resp)
      case Failure(_) => response.set("Error")
    }
  })

  new LoginView(login, password, buttonClicked, response)
})

class LoginView(login: Property[String], password: Property[String], buttonClicked: Property[Boolean], response: Property[String]) extends View {
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
        Checkbox(buttonClicked)
      )
    ),
    p("Wpisałeś login: ", bind(login)),
    p("Wpisałeś hasło: ", bind(password)),
    p("Odpowiedź serwera: ", bind(response))
  )

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}