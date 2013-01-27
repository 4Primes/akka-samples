package org.fourprimes.db

import scala.slick.driver.ExtendedProfile

case class Prop(id: Option[Int], key: String, value: String)

/**
 * All database code goes into the DAO (data access object) class which is
 * parameterized by a SLICK driver that implements ExtendedProfile.
 */
class DAO(val driver: ExtendedProfile) {

  import driver.simple._

  object Props extends Table[(Int, String, String)]("properties") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def key = column[String]("key")
    def value = column[String]("value")
    def * = id ~ key ~ value
    def forInsert = key ~ value <> ({ t => Prop(None, t._1, t._2) }, { (u: Prop) => Some((u.key, u.value)) })
  }

  type P = Props.type

  def create(implicit session: Session) = {
    Props.ddl.create
  }

  def insert(k: String, v: String)(implicit session: Session) =
    Props.forInsert returning Props.id insert Prop(None, k, v)

  def get(k: String)(implicit session: Session): Option[String] =
    (for (p <- Props if p.key === k) yield p.value).firstOption

  def getFirst[M, U](q: Query[M, U])(implicit s: Session) = q.first
}
