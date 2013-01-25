package org.fourprimes

import akka.actor.ActorSystem
import akka.actor.Actor
import com.typesafe.config.ConfigFactory

object FibonacciClient extends App {

  // Create an Akka system
  val config = ConfigFactory.load()
  val system = ActorSystem("ubuntu-sys", config.getConfig("ubuntu-sys").withFallback(config))

  class PrintlnActor extends Actor {
    def receive = {
      case msg =>
        println("Received message '%s' in actor %s".format(msg, self.path.name))
    }
  }

}