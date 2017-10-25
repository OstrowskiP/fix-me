package pl.lodz.p.edu.model

import org.mongodb.scala.bson.ObjectId

trait MongoObject {
  val _id: ObjectId
  val version: Double
}

case class User(_id: ObjectId, login: String, passwordHash: String, firstName: String, lastName: String, accessLevel: String, active: Boolean, version: Double) extends MongoObject
