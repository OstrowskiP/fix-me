package pl.lodz.p.edu

import pl.lodz.p.edu.jetty.ApplicationServer
import pl.lodz.p.edu.util.Property

object Launcher {
  def main(args: Array[String]): Unit = {
    val server = new ApplicationServer(Property.getInt("httpPort"), "backend/target/UdashStatic/WebContent")
    server.start()
  }
}

       