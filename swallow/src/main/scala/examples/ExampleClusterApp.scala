package examples

/**
  * Created by zhouqihua on 2017/6/30.
  */

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import swallow.Util.KMAkkaKit
import swallow.cluster.KMClusterSupervisor

import scala.io.StdIn

object ExampleClusterApp {
  def main(args: Array[String]): Unit = {

//    if (args.isEmpty)
//      startup(Seq("2551", "2552"))
//    else
//      startup(args)

    startup2

    StdIn.readLine()
  }

  def startup2: Unit = {
    KMAkkaKit.initActorRefWith("ClusterSystem", "127.0.0.1", 2551, KMClusterSupervisor.props, "clusterSupervisor1")
    KMAkkaKit.initActorRefWith("ClusterSystem", "127.0.0.1", 2552, KMClusterSupervisor.props, "clusterSupervisor2")
  }



  def startup(ports: Seq[String]): Unit = {
    ports foreach { port =>
      // Override the configuration of the port
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
        withFallback(ConfigFactory.load().getConfig("clusterActor"))

      // Create an Akka system
      val system = ActorSystem("ClusterSystem", config)
      // Create an actor that handles cluster domain events
      system.actorOf(Props[KMClusterSupervisor], name = "clusterSupervisor")
    }
  }
}
