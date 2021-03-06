package org.fourprimes.remoting

import akka.actor.Actor
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

object Slave extends App {
  
  // Create an Akka system
  val config = ConfigFactory.load()
  val system = ActorSystem("slave-sys", config.getConfig("slave-sys").withFallback(config))

}