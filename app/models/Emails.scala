package models

import slick.driver.PostgresDriver.api._

/**
  * Created by hariprasadk on 30/06/16.
  */
class Emails(tag:Tag) extends Table[Email](tag,"emailservices"){

  // def nameofvarialble = coloum[datatype](coloumn name in table , [with constraints ])
  def id = column[Int]("id",O.PrimaryKey, O.AutoInc)
  def template_name = column[String] ("template_name")
  def template_description = column[String] ("template_description")
  def template_location = column[String] ("template_location")

  def * = (id.?,template_name,template_description,template_location) <> ((Email.apply _).tupled, Email.unapply _)

}
