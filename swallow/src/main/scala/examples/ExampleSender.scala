package examples

/**
  * Created by zhouqihua on 2017/6/30.
  */

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import swallow.slave.{KMSender, KMSlaveType}

object ExampleSender {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val system = ActorSystem("SenderSystem", config.getConfig("localActor").withFallback(config))
    val localActor = system.actorOf(Props(new KMSender(KMSlaveType.Sender)), name = "senderActor")
  }
}
