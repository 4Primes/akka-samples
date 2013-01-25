package org.fourprimes

import akka.actor.ActorSystem
import akka.actor.Actor

object FibonacciClient extends App {

  // Create an Akka system
  val system = ActorSystem("ubuntu-sys")

  class PrintlnActor extends Actor {
    def receive = {
      case msg =>
        println("Received message '%s' in actor %s".format(msg, self.path.name))
    }
  }

  system.shutdown()

}