package org.fourprimes.db.routing

import akka.actor.Actor
import akka.actor.Props
import akka.routing.FromConfig
import scala.util.Random
import org.fourprimes.db.Prop
import org.fourprimes.db.DAO
import scala.slick.driver.PostgresDriver
import akka.dispatch.OnSuccess
import akka.util.Timeout

case class Insert(key: String, value: String)
case class Select(key: String)
case class Save
case class Get

object Actors {
  import scala.slick.session.{ Database, Session }

  lazy val db: Database = Database.forURL("jdbc:postgresql://localhost/akkadb", driver = "org.postgresql.Driver", user = "akka", password = "akka")
  lazy val dao: DAO = new DAO(PostgresDriver)
  db withSession { implicit session: Session =>
    dao.create
  }

  class PostgresActor extends Actor {

    def receive = {
      case Insert(key, value) => db withSession { implicit session: Session =>
        dao.insert(key, value)
      }

      case Select(key) => db withSession { implicit session: Session =>
        sender ! dao.get(key).getOrElse("None")
      }
    }

  }

  class PostgresMasterActor extends Actor {

    import scala.concurrent.duration._
    import akka.pattern.ask
    import context.dispatcher

    implicit val timeout = Timeout(1 second)

    val postgresRouter = context.actorOf(
      Props[PostgresActor].withRouter(FromConfig), name = "postgresRouter")

    val values = ("foo", "bar") :: ("a", "b") :: ("1", "2") :: Nil

    def receive = {
      case Save => for (value <- values) postgresRouter ! Insert(value._1, value._2)
      case Get => for (value <- values) postgresRouter ? Select(value._1) onSuccess {
        case v: String => println("%s -> %s".format(value._1, v))
      }
    }
  }
}

