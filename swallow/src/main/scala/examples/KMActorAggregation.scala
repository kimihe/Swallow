package examples

/**
  * Created by zhouqihua on 2017/6/25.
  */

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.Future
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global


case class Result(_sneder: Any, _receiver: Any)
case object Request

object KMActorAggregation {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("MyTestActor2")
    val channelActor = system.actorOf(Props[Channel], "Channel")

    channelActor ! "start"

    Thread.sleep(3000)

    channelActor ! "query"

    Thread.sleep(3000)
    channelActor ! "stop"
  }
}

class Channel extends Actor {
  val senderActor = context.actorOf(Props[Sender], "Sender")
  val receiverActor = context.actorOf(Props[Receiver], "Receiver")

  override def receive: Receive = {
    case "start" => {
      println("Channel Started !!!")
    }

    case "query" => {
      val aggregationActor = context.actorOf(Props[Aggregation], "aggregation")
      query() pipeTo aggregationActor
    }

    case "stop" => {
      println("Channel Stoped !!!")
    }
    case _ => println("Unresolved Message !!!")
  }

  implicit val TIMEOUT = Timeout(10, TimeUnit.SECONDS)
  def query(): Future[Result] = for {

    _sender <- ask(senderActor, Request)
    _receiver <- receiverActor ask Request
  //_receiver<- receiverActor ? Request
  }yield Result(_sender, _receiver)
}

class Aggregation extends Actor {
  override def receive: Receive = {
    case msg => println("[Aggregation]: Channel Completed !!!")
      println("[Channel]: " + msg.toString)
    case _ => println("Unresolved Message !!!")
  }
}



class Sender extends Actor {
  val path = "[Sender]: " + self.path

  override def preStart(): Unit = {
    println(path)
  }

  override def receive: Receive = {
    case Request => {
      sender ! path
    }

    case _ => println("Unresolved Message !!!")
  }
}

class Receiver extends Actor {
  val path = "[Sender]: " + self.path
  //val path = self.path.getElements.toString

  override def preStart(): Unit = {
    println(path)
  }

  override def receive: Receive = {
    case Request => {
      sender ! path
    }
    case _ => println("Unresolved Message !!!")
  }
}
