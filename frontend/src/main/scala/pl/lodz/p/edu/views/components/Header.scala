package pl.lodz.p.edu.views.components
import pl.lodz.p.edu.{IndexState, LoginState}
import pl.lodz.p.edu.config.ExternalUrls
import pl.lodz.p.edu.styles.GlobalStyles
import pl.lodz.p.edu.styles.partials.HeaderStyles
import org.scalajs.dom.raw.Element

import scalatags.JsDom.all._
import scalacss.ScalatagsCss._
import pl.lodz.p.edu.Context._

object Header {
  private lazy val template = header(HeaderStyles.header)(
    div(GlobalStyles.body, GlobalStyles.clearfix)(
      div(HeaderStyles.headerLeft)(
        a(HeaderStyles.headerLogo, href := IndexState.url)(
          Image("fixme_logo_m.png", "Udash Framework", GlobalStyles.block)
        )
      ),
      div(HeaderStyles.headerRight)(
        ul(HeaderStyles.headerSocial)(
          li(HeaderStyles.headerSocialItem)(
            a(href := ExternalUrls.projectGithub, HeaderStyles.headerSocialLink, target := "_blank")(
              Image("icon_github.png", "Github")
            )
          ),
          li(HeaderStyles.headerSocialItem)(
            a(href := LoginState("","").url, HeaderStyles.headerSocialLinkYellow)(
              Image("icon_login.png", "Login"),
              div(HeaderStyles.tooltip)(
                div(HeaderStyles.tooltipTop),
                div(HeaderStyles.tooltipText)(
                  div(HeaderStyles.tooltipTextInner)(
                    "Zaloguj się do portalu!"
                  )
                )
              )
            )
          )
        )
      )
    )
  ).render

  def getTemplate: Element = template
}