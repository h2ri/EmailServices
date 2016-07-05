package models

/**
  * Created by hariprasadk on 30/06/16.
  */


import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Email
(
  id: Option[Int],
  template_name : String,
  template_description : String,
  template_location : String

)

object Email{
  implicit val emailReads : Reads[Email] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "template_name").read[String] and
      (JsPath \ "template_description").read[String] and
      (JsPath \ "template_location").read[String]

    )(Email.apply _)

  implicit val emailWrites : Writes[Email] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "template_name").write[String] and
      (JsPath \ "template_description").write[String] and
      (JsPath \ "template_location").write[String]

    )(unlift(Email.unapply _))

}
