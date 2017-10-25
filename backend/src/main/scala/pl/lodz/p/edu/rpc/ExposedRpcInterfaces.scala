package pl.lodz.p.edu.rpc

import io.udash.rpc._
import pl.lodz.p.edu.dao.MongoDatabase

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ExposedRpcInterfaces(implicit clientId: ClientId) extends MainServerRPC with MongoDatabase {
  override def hello(name: String): Future[String] =
    Future.successful(s"Hello, $name!")

  override def pushMe(): Unit =
    ClientRPC(clientId).push(42)

  override def login(login: String, password: String): Future[String] = {
    findUserByLogin(login).map(_.head.passwordHash)
  }
}

       