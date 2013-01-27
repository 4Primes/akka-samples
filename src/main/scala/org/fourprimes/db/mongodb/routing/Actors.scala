package org.fourprimes.db.mongodb.routing

import akka.actor.Actor
import akka.actor.Props
import akka.routing.FromConfig
import scala.util.Random
import org.fourprimes.db.Prop
import org.fourprimes.db.DAO
import scala.slick.driver.PostgresDriver
import akka.dispatch.OnSuccess
import akka.util.Timeout
import com.mongodb.casbah.Imports._

case class Insert(key: String, value: String)
case class Select(key: String)
case class Save()
case class Get()

object Actors {

  lazy val mongoClient = MongoClient()
  lazy val mongoColl = mongoClient("test_db")("test_coll")

  class MongoDBActor extends Actor {

    def receive = {
      case Insert(key, value) => {
        val newObj = MongoDBObject("key" -> key, "value" -> value)
        mongoColl.insert(newObj)
      }

      case Select(key) => {
        val newObj = MongoDBObject("key" -> key)
        sender ! mongoColl.findOne(newObj).getOrElse("None")
      }
    }

  }

  class MongoDBMasterActor extends Actor {

    import scala.concurrent.duration._
    import akka.pattern.ask
    import context.dispatcher

    implicit val timeout = Timeout(1 second)

    val mongoDBRouter = context.actorOf(
      Props[MongoDBActor].withRouter(FromConfig), name = "mongoDBRouter")

    val values = ("foo", "bar") :: ("a", "b") :: ("1", "2") :: Nil

    def receive = {
      case Save => for (value <- values) mongoDBRouter ! Insert(value._1, value._2)
      case Get => for (value <- values) mongoDBRouter ? Select(value._1) onSuccess {
        case v:DBObject => println("%s -> %s".format(value._1, v))
      }
    }
  }
}

