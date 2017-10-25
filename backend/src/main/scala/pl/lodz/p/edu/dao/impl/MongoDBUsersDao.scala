package pl.lodz.p.edu.dao.impl

import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.{Completed, Document, Observable}
import pl.lodz.p.edu.dao.{GenDBHandler, UsersDao}
import pl.lodz.p.edu.model.{User, UserError}

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import scala.util.{Failure, Success, Try}

class MongoDBUsersDao(dbHandler: GenDBHandler[User]) extends UsersDao {
  override def synchronize(users: Seq[User]): String =
    dbHandler.synchronize(users)

  override def create(user: User): String =
    dbHandler
      .subscribeToResult[Completed](user, dbHandler.create)
      .getOrElse("Failed during user creation")
      .toString()

  override def update(user: User): Boolean =
    dbHandler.subscribeToResult[UpdateResult](user, dbHandler.update).get.getModifiedCount == 1


  override def delete(user: User): Boolean =
    dbHandler.subscribeToResult[DeleteResult](user, dbHandler.delete).get.getDeletedCount == 1

  override def find(key: String, value: String): User = {

    val fut = dbHandler.findByKey(Map(key -> value)).toFuture()
    Await.result(fut, Duration(60, SECONDS))
    fut.value.get.get.head
  }

  //FIXME: below
  //      .subscribeToResult(Map(key -> value), dbHandler.findByKey _)
  //      .getOrElse(throw UserError(s"No user matching filter: $key -> $value was found"))


  override def findAll(): Observable[User] =
    dbHandler.findAll

  private def createDocument(user: User): Document = {
    Document(
      "id" -> user._id,
      "login" -> user.login,
      "passwordHash" -> user.passwordHash,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "accessLevel" -> user.accessLevel,
      "active" -> user.active,
      "version" -> user.version
    )
  }

}
