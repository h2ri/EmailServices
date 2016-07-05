package models

/**
  * Created by hariprasadk on 30/06/16.
  */
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider
import play.api._

class Messages (tag: Tag) extends Table[Message](tag, "messages") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def message = column[String]("message")
  def * = (id.?, message) <> ((Message.apply _).tupled, Message.unapply _)

}