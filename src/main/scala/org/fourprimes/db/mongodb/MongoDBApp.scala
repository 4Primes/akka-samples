package org.fourprimes.db.mongodb

import com.mongodb.casbah.Imports._

object MongoDBApp extends App {

  lazy val mongoClient = MongoClient()
  lazy val mongoColl = mongoClient("test_db")("test_coll")

  val newObj = MongoDBObject("foo" -> "bar",
    "x" -> "y")
   mongoColl.insert(newObj)
   
   mongoColl.find().foreach(println(_))
}