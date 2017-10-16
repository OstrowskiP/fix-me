package pl.lodz.p.edu.views.components
import pl.lodz.p.edu.config.ExternalUrls
import pl.lodz.p.edu.styles.{DemoStyles, GlobalStyles}
import pl.lodz.p.edu.styles.partials.FooterStyles
import org.scalajs.dom.raw.Element
import pl.lodz.p.edu.IndexState

import scalatags.JsDom.all._
import scalacss.ScalatagsCss._
import pl.lodz.p.edu.Context._

object Footer {
  private lazy val template = footer(FooterStyles.footer)(
    div(GlobalStyles.body)(
      div(FooterStyles.footerInner)(
        a(FooterStyles.footerLogo, href := IndexState.url)(
          Image("fixme_logo.png", "Udash Framework", GlobalStyles.block)
        ),
        div(FooterStyles.footerLinks)(
          p(FooterStyles.footerMore)("Nasze dane"),
          ul(
            li(DemoStyles.navItem)(
              p("FixME, ul. Wólczańska 180, 90-530 Łódź")
            ),
            li(DemoStyles.navItem)(
              a(href := ExternalUrls.googleMapLocation, target := "_blank")("Dojazd")
            )
          )
        ),
        p(FooterStyles.footerCopyrights)("Wykonane przez ", a(FooterStyles.footerAvsystemLink, href := ExternalUrls.authorMail, target := "_blank")("Piotr Ostrowski"))
      )
    )
  ).render

  def getTemplate: Element = template
}