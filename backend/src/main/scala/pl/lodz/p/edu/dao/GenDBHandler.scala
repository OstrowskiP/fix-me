package pl.lodz.p.edu.dao

import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observable}
import pl.lodz.p.edu.model.MongoObject

trait GenDBHandler[T <: MongoObject] {
  def client: MongoClient

  def database: MongoDatabase

  def collection: MongoCollection[T]

  def delete(doc: T): Observable[DeleteResult]

  def findById(id: Int): Observable[T]

  def find(doc: T): Observable[T]

  def findByKey(filter: Map[String, String]): Observable[T]

  def findAll(): Observable[T]

  def update(doc: T): Observable[UpdateResult]

  def synchronize(docs: Seq[T]): String

  def create(doc: T): Observable[Completed]

  def subscribeToResult(id: Int, finder: Int => Observable[T]): Option[T]

  def subscribeToResult(filter: Map[String, String], finder: Map[String, String] => Observable[T]): Option[T]

  def subscribeToResult[A](doc: T, finder: T => Observable[A]): Option[A]

  def subscribeToResults[A](docs: Seq[T], finder: Seq[T] => Observable[A]): Option[A]
}
