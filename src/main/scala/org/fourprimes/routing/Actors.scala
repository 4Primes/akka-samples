package org.fourprimes.routing

import akka.actor.Actor
import akka.actor.Props
import akka.routing.FromConfig
import scala.util.Random

sealed trait Message
case class MapValue(value: Int) extends Message
case class ReduceValue(value: Int) extends Message
case class MapResult(value: Int) extends Message
case class ReduceResult(value: Int) extends Message
case class Calculate() extends Message

class MapActor extends Actor {

  def receive = {
    case MapValue(value) => sender ! MapResult(value * 2)
  }

}

class ReduceActor extends Actor {

  var result = 0

  def receive = {
    case ReduceValue(value) => {
      result += value
      sender ! ReduceResult(result)
    }
  }

}

class MasterActor extends Actor {

  val mapRouter = context.actorOf(
    Props[MapActor].withRouter(FromConfig), name = "mapRouter")

  val reducer = context.actorOf(Props[ReduceActor], name = "reduceActor")

  val nrOfElements = 2000000
  var nrOfResults = 0
  val values = Array.fill(nrOfElements)(Random.nextInt(70))

  def receive = {
    case Calculate => for (value <- values) mapRouter ! MapValue(value)
    case MapResult(value) => reducer ! ReduceValue(value)
    case ReduceResult(value) => {
      nrOfResults += 1
      if(nrOfResults == nrOfElements){
        println("Result is %d".format(value))
      }
    }
  }
}