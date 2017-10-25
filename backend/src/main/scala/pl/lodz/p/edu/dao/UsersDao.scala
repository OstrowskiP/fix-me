package pl.lodz.p.edu.dao

import org.mongodb.scala.{Document, Observable}
import pl.lodz.p.edu.model.User

trait UsersDao {
  def synchronize(users: Seq[User]): String

  def create(user: User): String

  def update(user: User): Boolean

  def delete(user: User): Boolean

  def find(key: String, value: String): User

  def findAll(): Observable[User]
}



