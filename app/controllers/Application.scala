package controllers

import play.api._
import play.api.mvc._
import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json._
import play.api.mvc._
import models.{Email, Emails, Message, Messages}

class Application @Inject()(dbConfigProvider: DatabaseConfigProvider) extends Controller {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  val messages = TableQuery[Messages]

  val emails = TableQuery[Emails]

  def findQuery(id: Int) = messages.filter(_.id === id)

  def findQueryEmail(id:Int) = emails.filter(_.id === id)

  def index = Action.async {
    val result = dbConfig.db.run(messages.result)
    result.map(msgs => Ok(Json.obj("status" -> "Ok", "messages" -> Json.toJson(msgs))))
  }

  def getEmail = Action.async {
    val result = dbConfig.db.run(emails.result)
    print(result)
    result.map(msg => Ok(Json.obj("status" -> "Ok", "emails" -> Json.toJson(msg))))
  }

  def create = Action.async(BodyParsers.parse.json) { request =>
    val message = request.body.validate[Message]
    message.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing message failed",
        "error" -> JsError.toJson(errors)))),
      message =>
        dbConfig.db.run(messages returning messages += message).map(m =>
          Ok(Json.obj("status" -> "Success", "message" -> Json.toJson(m)))
        ))
  }

  def createEmail = Action.async(BodyParsers.parse.json) { request =>
    val email = request.body.validate[Email]
    email.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing email failed",
        "error" -> JsError.toJson(errors)))),
      email =>
        dbConfig.db.run(emails returning emails += email).map(e =>
          Ok(Json.obj("status" -> "Success", "email" -> Json.toJson(e)))
        ))
  }

  def createEmailWithTemplate = Action(parse.multipartFormData) { request =>
    //print(request.body.dataParts("test"))
    //val test1 = extract(request.body.dataParts, "test1" , _.as[String]).getOrElse(test1(Nil,""))
    val email = request.body
    //print(email)
    val parms1 = ""
    val parms2 = ""
    request.body.dataParts("test1").map{ a =>
      println("Inside DataPart for Test" + a)
    }
    request.body.dataParts("test").map { b =>
      println("Inside dataPart for Test1 " + b)
    }

    request.body.dataParts.map { a =>

    }

    //val newEmail = Email(0,data.)
    //request.body.dataParts.map{ a =>
    //}
    request.body.file("emailTemplate").map { emailTemplate =>
      import java.io.File
      val filename = emailTemplate.filename
      val contentType = emailTemplate.contentType
      val emailTemplateFilename =  "/Users/hariprasadk/IdeaProjects/EmailService/EmailTemplates/$filename"
      emailTemplate.ref.moveTo(new File(emailTemplateFilename))
      Ok("File uploaded")

    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file")
    }




  }


  //    request.body.file("emailTemplate").map { emailTemplate =>
//      import java.io.File
//      val filename = emailTemplate.filename
//      val contentType = emailTemplate.contentType
//
//      emailTemplate.ref.moveTo(new File(s"/Users/hariprasadk/IdeaProjects/EmailServices/EmailTemplates/$filename"))
//      Ok("File uploaded")
//
//    }.getOrElse {
//      Redirect(routes.Application.index).flashing(
//        "error" -> "Missing file")

      //Ok("File upload failed")

   // }



  def show(id: Int) = Action.async {
    val message = dbConfig.db.run(findQuery(id).result.head)
    message.map(msg => Ok(Json.obj("status" -> "Ok", "message" -> Json.toJson(msg))))
  }

  def showEmail(id:Int) = Action.async {
    val email = dbConfig.db.run(findQueryEmail(id).result.head)
    email.map(msg => Ok(Json.obj("status" -> "Ok" , "message" -> Json.toJson(msg))) )
  }

  def update(id: Int) = Action.async(BodyParsers.parse.json) { request =>
    val message = request.body.validate[Message]
    message.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Message update failed",
        "error" -> JsError.toJson(errors)))),
      message => {
        dbConfig.db.run(findQuery(id).update(message))
        Future(Ok(Json.obj("status" -> "Ok", "message" -> Json.toJson(message))))
      }
    )
  }


  def updateEmail(id:Int) = Action.async(BodyParsers.parse.json) { request =>
    val email = request.body.validate[Email]
    email.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Message update failed",
        "error" -> JsError.toJson(errors)))),
      email => {
        // dbConfig.db.run(findQueryEmail(id).update(email))
        dbConfig.db.run(emails.filter(_.id === id).update(email))
        Future(Ok(Json.obj("status" -> "Ok","message" -> Json.toJson(email))))
      }
    )
  }

  def delete(id: Int) = Action.async {
    dbConfig.db.run(findQuery(id).delete).map(m => Ok(Json.obj("status" -> "Ok")))
  }

  def deleteEmail(id: Int) = Action.async {

    dbConfig.db.run(findQueryEmail(id).delete).map(m => Ok(Json.obj("status" -> "Ok")))
  }

}