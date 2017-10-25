package pl.lodz.p.edu.dao

import pl.lodz.p.edu.dao.impl.MongoDBUsersHandler
import pl.lodz.p.edu.util.Property

/**
  * Created by postrowski on 10/18/17.
  */
trait DBConfig {
  val usersDbHandler = new MongoDBUsersHandler(Property("databaseName"), Property("usersCollectionName"))
}
