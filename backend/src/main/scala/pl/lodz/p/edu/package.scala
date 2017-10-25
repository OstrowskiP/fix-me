package pl.lodz.p.edu

/**
  * Created by postrowski on 10/17/17.
  */
package object utils {
  def sha256Hash(text: String) : String =
    String.format(
      "%064x",
      new java.math.BigInteger(1, java.security.MessageDigest.getInstance("SHA-256").digest(text.getBytes("UTF-8")))
    )
}
