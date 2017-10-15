package pl.lodz.p.edu.views

import io.udash._
import pl.lodz.p.edu.LoginState
import pl.lodz.p.edu.styles.DemoStyles
import scalacss.ScalatagsCss._

case class LoginViewPresenter(loginStr: String, passwordStr: String) extends DefaultViewPresenterFactory[LoginState](() => {
  import pl.lodz.p.edu.Context._

  val login = Property[String](loginStr)
  val password = Property[String](passwordStr)
  new LoginView(login, password)
})

class LoginView(login: Property[String], password: Property[String]) extends View {
  import scalatags.JsDom.all._

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
        TextInput.debounced(password, placeholder := "Twoje hasło")
      )
    ),
    p("Wpisałeś login: ", bind(login)),
    p("Wpisałeś hasło: ", bind(password))
  )

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}