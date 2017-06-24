/**
  * Created by zhouqihua on 2017/6/22.
  */

package com.lightbend.akka.sample

import com.typesafe.config._
import akka.actor._

object MyLocalActor {
  def main(args: Array[String]): Unit = {

    val conf = """
      akka {
        actor {
          provider = "akka.remote.RemoteActorRefProvider"
        }
        remote {
          enabled-transports = ["akka.remote.netty.tcp"]
          netty.tcp {
            hostname = "0.0.0.0"
            port = 17201
          }
        }
      }
    """
    val config = ConfigFactory.parseString(conf)
    val system = ActorSystem("localActor", config)
//        val config = ConfigFactory.load()
//        val system = ActorSystem("localActor", config.getConfig("localActor").withFallback(config))
    val localActor = system.actorOf(Props[LocalActor], name = "localActor")
  }
}

object LocalActor {
  final case class TransferFlow(taskId: String, master: String, from: String, to: String, content: String, description: String)
  final case class CompleteFlow(taskId: String, master: String, from: String, to: String, content: String, description: String)
}

class LocalActor extends Actor with ActorLogging {
  import MasterActor._
  import LocalActor._
  import RemoteActor._

  override def preStart(): Unit = {

  }

  override def receive: Receive = {

    case TransferFlow(taskId, master, from, to, content, description) =>
      log.info(s"[LocalActor] transferFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: $from; to: $to; content: $content")

      val remoteActor = context.actorSelection(s"$to")
      remoteActor ! ReceiveFlow(taskId, master, from, to, content, description)

    case CompleteFlow(taskId, master, from, to, content, description) =>
      log.info(s"[LocalActor] completeFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: $from; to: $to; content: $content")
      val masterActor = context.actorSelection(s"$master")
      masterActor ! AggregateFlow(taskId, master, from, to, content, description)
  }
}

