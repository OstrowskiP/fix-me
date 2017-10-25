package pl.lodz.p.edu.dao.impl

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.{BsonDouble, BsonInt32, BsonObjectId, BsonString, BsonValue}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.FindOneAndReplaceOptions
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observable}
import org.slf4j.{Logger, LoggerFactory}
import pl.lodz.p.edu.dao.GenDBHandler
import pl.lodz.p.edu.model.User
import pl.lodz.p.edu.util._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

class MongoDBUsersHandler(dbName: String, collectionName: String)
  extends GenDBHandler[User] {

  import MongoDBHandler._

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  val codecRegistry = fromRegistries(fromProviders(classOf[User]), DEFAULT_CODEC_REGISTRY)

  val client: MongoClient = MongoClient()
  val database: MongoDatabase = client.getDatabase(dbName).withCodecRegistry(codecRegistry)
  val collection: MongoCollection[User] =
    database.getCollection(collectionName)

  override def delete(doc: User): Observable[DeleteResult] =
    collection.deleteOne(equal("id", doc._id))

  override def findById(id: Int): Observable[User] =
    collection.find(equal("id", id))

  override def find(doc: User): Observable[User] =
    collection.find(equal("id", doc._id))

  override def findByKey(filter: Map[String, String]): Observable[User] =
    collection.find(equal(filter.keySet.head, filter.values.head))

  override def findAll(): Observable[User] =
    collection.find()

  override def update(doc: User): Observable[UpdateResult] =
    collection.replaceOne(and(equal("id", doc._id), lt("version", doc.version)), doc)

  def getBsonValue(optBson: Option[BsonValue]) = {
    optBson match {
      case Some(bson: BsonString) => bson.getValue
      case Some(bson: BsonDouble) => bson.getValue
      case Some(bson: BsonObjectId) => bson.getValue
      case Some(bson: BsonInt32) => bson.getValue
      case _ => logger.error(Property("getBsonValueError"))
    }
  }

  override def synchronize(docs: Seq[User]): String = {
    for (doc <- docs) {
      collection.findOneAndReplace(and(equal("id", doc._id), lt("version", doc.version)), doc, FindOneAndReplaceOptions().upsert(true)).toFuture
    }
    "Synchronization completed."
  }

  override def create(doc: User): Observable[Completed] =
    collection.insertOne(doc)

  override def subscribeToResult[A](doc: User, finder: User => Observable[A]): Option[A] = {
    val fut: Future[Seq[A]] = finder(doc).toFuture()
    Await.ready(fut, Duration(1000, MILLISECONDS))

    fut.value match {
      case Some(fromDB) => extractDoc[A](fromDB)
      case _ =>
        logger.error(Property("documentNotFound") + doc)
        None
    }
  }

  override def subscribeToResult(filter: Map[String, String], finder: Map[String, String] => Observable[User]): Option[User] = {
    val fut: Future[Seq[User]] = finder(filter).toFuture()
    Await.ready(fut, Duration(10, SECONDS))

    fut.value match {
      case Some(fromDB) => extractDoc[User](fromDB)
      case _ =>
        logger.error(Property("documentNotFound") + s"filter: $filter")
        None
    }
  }

  override def subscribeToResult(id: Int, finder: Int => Observable[User]): Option[User] = {
    val fut: Future[Seq[User]] = finder(id).toFuture()
    Await.ready(fut, Duration(1000, MILLISECONDS))

    fut.value match {
      case Some(fromDB) => extractDoc[User](fromDB)
      case _ =>
        logger.error(Property("documentNotFound") + id)
        None
    }
  }

  override def subscribeToResults[A](docs: Seq[User], finder: Seq[User] => Observable[A]): Option[A] = {
    val fut: Future[Seq[A]] = finder(docs).toFuture()
    Await.ready(fut, Duration(1000, MILLISECONDS))

    fut.value match {
      case Some(fromDB) => extractDoc[A](fromDB)
      case _ =>
        logger.error(Property("documentNotFound") + docs)
        None
    }
  }
}

object MongoDBHandler {
  def extractDoc[A](maybeDoc: Try[Seq[A]]): Option[A] = {
    maybeDoc match {
      case Success(docList) =>
        if (docList.isEmpty) {
          None
        } else {
          val result = docList.head
          Some(result)
        }
      case Failure(_) =>
        None
    }
  }

  def extractDocs[A](maybeDoc: Try[Seq[A]]): Option[Seq[A]] = {
    maybeDoc match {
      case Success(docList) =>
        if (docList.isEmpty) {
          None
        } else {
          val result = docList
          Some(result)
        }
      case Failure(_) =>
        None
    }
  }
}
