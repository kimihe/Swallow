package examples

/**
  * Created by zhouqihua on 2017/6/30.
  */

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import swallow.slave.{KMReceiver, KMSlaveType}

object ExampleReceiver {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val system = ActorSystem("ReceiverSystem", config.getConfig("remoteActor").withFallback(config))
    val remoteActor = system.actorOf(Props(new KMReceiver(KMSlaveType.Receiver)), name = "receiverActor")
  }
}
