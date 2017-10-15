package pl.lodz.p.edu

import io.udash._
import pl.lodz.p.edu.views._

class StatesToViewPresenterDef extends ViewPresenterRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewPresenter[_ <: RoutingState] = state match {
    case RootState => RootViewPresenter
    case IndexState => IndexViewPresenter
    case LoginState(login, password) => LoginViewPresenter(login,password)
    case RPCDemoState => RPCDemoViewPresenter
    case DemoStylesState => DemoStylesViewPresenter
    case _ => ErrorViewPresenter
  }
}