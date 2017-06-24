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

class LocalActor extends Actor with ActorLogging {
//  val remoteActor = context.actorSelection("akka://remoteActor@127.0.0.1:17202")
//  implicit val timeout = Timeout(5, TimeUnit.SECONDS)

  override def preStart(): Unit = {
//    val remoteActor = context.actorSelection("akka.tcp://remoteActor@0.0.0.0:17202/user/remoteActor")
//    remoteActor ! "hello world"
  }



  override def receive: Receive = {

    case "start" =>
      log.info(s"LocalActor received start; From sender: $sender")

      val remoteActor = context.actorSelection("akka.tcp://remoteActor@0.0.0.0:17202/user/remoteActor")
      remoteActor ! "hello remote"
    case msg: String =>
//      val furure = remoteActor  ask msg
//      val result = Await.result(furure, timeout)
      log.info("Received: " + msg + "; From sender: " + sender)
  }
}

