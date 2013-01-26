package org.fourprimes.remoting

import akka.actor.Actor

class PrintlnActor extends Actor {
  def receive = {
    case msg =>
      println("Received message '%s' in actor %s".format(msg, self.path))
  }
}