package models

/**
  * Created by hariprasadk on 30/06/16.
  */

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Message
(
  id : Option[Int],
  message: String
)

object Message {
  implicit val messageReads: Reads[Message] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "message").read[String]
    )(Message.apply _)


  implicit val messageWrites: Writes[Message] = (
    (JsPath \ "id").writeNullable[Int] and
      (JsPath \ "message").write[String]
    )(unlift(Message.unapply _))
}