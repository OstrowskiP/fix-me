package pl.lodz.p.edu.dao

import pl.lodz.p.edu.model.User
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, Macros, document}

import scala.concurrent.Future


/**
  * Created by postrowski on 10/25/17.
  */
trait MongoDatabase {
  val mongoUri = "mongodb://localhost:27017/mydb?authMode=scram-sha1"

  import scala.concurrent.ExecutionContext.Implicits.global

  val driver = MongoDriver()
  val parsedUri = MongoConnection.parseURI(mongoUri)
  val connection = parsedUri.map(driver.connection(_))

  val futureConnection = Future.fromTry(connection)
  def db: Future[DefaultDB] = futureConnection.flatMap(_.database("fixMeDB"))
  def usersCollection: Future[BSONCollection] = db.map(_.collection("users"))

  implicit def userWriter: BSONDocumentWriter[User] = Macros.writer[User]

  def createUser(user: User): Future[Unit] =
    usersCollection.flatMap(_.insert(user).map(_ => {}))

  def updateUser(user: User): Future[Int] = {
    val selector = document(
      "firstName" -> user.firstName,
      "lastName" -> user.lastName
    )
    usersCollection.flatMap(_.update(selector, user).map(_.n))
  }

  implicit def userReader: BSONDocumentReader[User] = Macros.reader[User]

  def findUserByLogin(login: String): Future[List[User]] =
    usersCollection.flatMap(_.find(document("login" -> login)).cursor[User]().collect[List]())
}
