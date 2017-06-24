/**
  * Created by zhouqihua on 2017/6/24.
  */

package com.lightbend.akka.sample

import akka.actor._
import com.typesafe.config._

import scala.io.StdIn

object MyMasterActor {
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
            port = 17200
          }
        }
      }
    """

    val config = ConfigFactory.parseString(conf)
    val system = ActorSystem("masterActor", config)
    //    val config = ConfigFactory.load()
    //    val system = ActorSystem("remoteActor", config.getConfig("remoteActor").withFallback(config))
    val masterActor = system.actorOf(Props[MasterActor], name = "masterActor")


    StdIn.readLine()

    masterActor ! "submitNewFlow"

    StdIn.readLine()
    system.terminate()

  }

  def submitNewFlow(master: String, source: String, destination: String, taskId: String, description: String): Unit = {

  }
}

class MasterActor extends Actor with ActorLogging {


  override def receive: Receive = {
    case "submitNewFlow" =>
      log.info(s"MasterActor submitNewFlow; From sender: $sender")

      val localActor = context.actorSelection("akka.tcp://localActor@0.0.0.0:17201/user/localActor")
      localActor ! "start"
  }
}
