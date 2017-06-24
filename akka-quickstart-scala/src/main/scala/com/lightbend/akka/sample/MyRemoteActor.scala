/**
  * Created by zhouqihua on 2017/6/22.
  */

package com.lightbend.akka.sample

import com.typesafe.config._
import akka.actor._

object MyRemoteActor {
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
            port = 17202
          }
        }
      }
    """

    val config = ConfigFactory.parseString(conf)
    val system = ActorSystem("remoteActor", config)
//    val config = ConfigFactory.load()
//    val system = ActorSystem("remoteActor", config.getConfig("remoteActor").withFallback(config))
    val remoteActor = system.actorOf(Props[RemoteActor], name = "remoteActor")
//    println("remoteActor: "+remoteActor)

    //val localActor = system.actorSelection("akka.tcp://localActor@0.0.0.0:17201/user/localActor")
    //localActor ! "hello local"
  }
}

class RemoteActor extends Actor with ActorLogging{
  override def receive: Receive = {
    case msg: String =>
      log.info(s"Received: $msg; From sender: $sender")
      //sender.tell("RemoteActor received: "+msg, self)
      val localActor = context.actorSelection("akka.tcp://localActor@0.0.0.0:17201/user/localActor")
      localActor ! "hello local"
  }
}
