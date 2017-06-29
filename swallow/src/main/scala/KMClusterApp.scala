/**
  * Created by zhouqihua on 2017/6/28.
  */

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props

import scala.io.StdIn

object KMClusterApp {

  def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      startup(Seq("2551", "2552"))
    else
      startup(args)

    StdIn.readLine()
  }

  def startup(ports: Seq[String]): Unit = {
    ports foreach { port =>
      // Override the configuration of the port
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
        withFallback(ConfigFactory.load().getConfig("clusterActor"))

      // Create an Akka system
      val system = ActorSystem("ClusterSystem", config)
      // Create an actor that handles cluster domain events
      system.actorOf(Props[KMClusterListener], name = "clusterListener")
    }
  }

}
