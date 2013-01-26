package org.fourprimes.remote

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props

object Master extends App {

  // Create an Akka system
  val config = ConfigFactory.load()
  val system = ActorSystem("master-sys", config.getConfig("master-sys").withFallback(config))

  val actor = system.actorOf(Props[PrintlnActor], "printlnActor")
  actor ! "Hello from master"

}