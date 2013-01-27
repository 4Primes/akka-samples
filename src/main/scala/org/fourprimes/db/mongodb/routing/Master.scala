package org.fourprimes.db.mongodb.routing

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.routing.FromConfig
import scala.util.Random
import org.fourprimes.db.mongodb.routing.Actors._

object Master extends App {

  // Create an Akka system
  val config = ConfigFactory.load()
  val system = ActorSystem("master-sys", config.getConfig("master-sys").withFallback(config))

  val master = system.actorOf(Props[MongoDBMasterActor], name = "mongoDBMasterActor")
  master ! Save
  for (ln <- io.Source.stdin.getLines) master ! Get

}