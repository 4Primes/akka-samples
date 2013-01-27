package org.fourprimes.db.mongodb.spray;

import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp

object Boot extends App with SprayCanHttpServerApp {

  // create and start our service actor
  val service = system.actorOf(Props[MongoDBServiceActor], "mongodb-service")

  // create a new HttpServer using our handler and tell it where to bind to
  newHttpServer(service) ! Bind(interface = "localhost", port = 8080)
}

