package org.fourprimes.db

import scala.slick.driver.PostgresDriver

object PostgresApp extends App {
  // We only need the DB/session imports outside the DAO
  import scala.slick.session.{ Database, Session }

  def run(name: String, dao: DAO, db: Database) {
    println("Running test against " + name)
    db withSession { implicit session: Session =>
      dao.create
      println(dao.insert("foo", "bar"))
      println(dao.insert("a", "b"))
      println("  Value for key 'foo': " + dao.get("foo"))
      println("  Value for key 'baz': " + dao.get("baz"))
    }
  }

  run("Postgres", new DAO(PostgresDriver),
    Database.forURL("jdbc:postgresql://localhost/akkadb", driver = "org.postgresql.Driver", user = "akka", password = "akka"))

}