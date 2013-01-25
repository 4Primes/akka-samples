package org.fourprimes

import akka.actor.AddressFromURIString
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor
import scala.annotation.tailrec
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinRouter

object Fibonacci extends App {

  // Create an Akka system
  val system = ActorSystem("mac-sys")

  class PrintlnActor extends Actor {
    def receive = {
      case msg =>
        println("Received message '%s' in actor %s".format(msg, self.path.name))
    }
  }

  case class FibonacciNumber(nbr: Int)

  class FibonacciActor extends Actor {
    def receive = {
      case FibonacciNumber(nbr) => sender ! fibonacci(nbr)
    }

    private def fibonacci(n: Int): Int = {
      @tailrec
      def fib(n: Int, b: Int, a: Int): Int = n match {
        case 0 => a
        case _ => fib(n - 1, a + b, b)
      }

      fib(n, 1, 0)
    }
  }

  val addresses = Seq(
    AddressFromURIString("akka://mac-sys@192.168.1.4:2552"),
    AddressFromURIString("akka://ubuntu-sys@192.168.1.5:2552"))

  val routerRemote = system.actorOf(Props[PrintlnActor].withRouter(
    RemoteRouterConfig(RoundRobinRouter(5), addresses)))

  1 to 10 foreach {
    i => routerRemote ! i
  }
  
  system.shutdown()

}