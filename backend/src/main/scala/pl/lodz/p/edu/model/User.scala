package pl.lodz.p.edu.model

/**
  * Created by postrowski on 10/25/17.
  */

case class User(login: String, passwordHash: String, firstName: String, lastName: String, accessLevel: String, active: Boolean, version: Double)
