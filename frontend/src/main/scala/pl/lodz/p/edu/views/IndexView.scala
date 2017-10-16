package pl.lodz.p.edu.views

import io.udash._
import pl.lodz.p.edu._
import org.scalajs.dom.Element
import pl.lodz.p.edu.styles.{DemoStyles, GlobalStyles}
import scalacss.ScalatagsCss._

object IndexViewPresenter extends DefaultViewPresenterFactory[IndexState.type](() => new IndexView)

class IndexView extends View {
  import pl.lodz.p.edu.Context._
  import scalatags.JsDom.all._

  private val content = div(
    p("Nasz serwis oferuję naprawę sprzętu elektronicznego bez konieczności wychodzenia z domu! " +
      "To co wyróżnia nas od konkurencji to możliwość zamówienia serwisu z naprawą na miejscu " +
      "lub z odbiorem sprzętu przez naszego pracownika (jeśli naprawa wymaga specjalistycznego sprzętu i naszego laboratorium)." +
      "Wystarczy, że wypełnisz zgłoszenie serwisowe, nasz praacownik odezwie się do Ciebie i umówi w dogodnej dla Ciebie dacie." +
      "Zachęcamy do założenia swojego konta, będziesz mógł na bieżąco śledzić status swoich zgłoszeń oraz przejrzeć ich historię."),
  )

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}