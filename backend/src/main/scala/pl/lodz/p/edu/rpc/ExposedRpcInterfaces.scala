package pl.lodz.p.edu.rpc

import io.udash.rpc._
import pl.lodz.p.edu.dao.DBConfig
import pl.lodz.p.edu.dao.impl.MongoDBUsersDao
import pl.lodz.p.edu.utils._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, MINUTES}
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

class ExposedRpcInterfaces(implicit clientId: ClientId) extends MainServerRPC with DBConfig{
  val usersRepository = new MongoDBUsersDao(usersDbHandler)

  override def hello(name: String): Future[String] =
    Future.successful(s"Hello, $name!")

  override def pushMe(): Unit =
    ClientRPC(clientId).push(42)

  override def login(login: String, password: String): Future[String] = {
    val passwordHash = sha256Hash(password)

//    val user = usersRepository.find("login", login)
//    val user = usersRepository.findAll()
    val usersFuture = usersRepository.findAll().toFuture()
    Await.result(usersFuture,Duration(1, MINUTES))
    usersFuture.map( users =>
      (users.head.passwordHash == passwordHash).toString
    )
//
//    val validLogin: Boolean = user.passwordHash.equals(passwordHash)
//
//    Future.successful(validLogin.toString)
  }
}

       