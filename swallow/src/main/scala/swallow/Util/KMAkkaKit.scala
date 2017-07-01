package swallow.Util

/**
  * Created by zhouqihua on 2017/6/30.
  */

import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

object KMAkkaKit {

  def initConfigWith(hostname: String, port: Int): Config = {
    // Create an config
    val parseString: String =
        """
          |akka {
          |  remote {
          |    netty.tcp {
          |      hostname =
        """.stripMargin + "\"" + hostname + "\"" +
        """
          |      port =
        """.stripMargin + port +
        """
          |    }
          |  }
          |}
        """.stripMargin

    //    println(parseString)

    ConfigFactory.parseString(parseString).
      withFallback(ConfigFactory.load().getConfig("KMNode"))
  }




  def initActorSystemWith(actorSystemName: String, config: Config): ActorSystem = {
    // Create an Akka system
    ActorSystem(actorSystemName, config)
  }

  def initActorSystemWith(actorSystemName: String, hostname: String, port: Int): ActorSystem = {
    // Create an Akka system
    val config = this.initConfigWith(hostname, port)
    this.initActorSystemWith(actorSystemName, config)
  }




  def initActorRefWith(actorSystem: ActorSystem, props: Props, actorName: String): ActorRef = {
    // Create an Akka Actor Reference
    actorSystem.actorOf(props, name = actorName)
  }

  def initActorRefWith(actorSystemName: String, hostname: String, port: Int, props: Props, actorName: String): ActorRef = {
    // Create an Akka Actor Reference
    val config = this.initConfigWith(hostname, port)
    val actorSystem = this.initActorSystemWith(actorSystemName, config)
    actorSystem.actorOf(props, name = actorName)
  }




  def serializedWith(sth: Any): Unit = {
    //      val serialization = SerializationExtension(context.system)
    //      // Have something to serialize
    //      val original = flow.flowInfo
    //
    //      // Find the Serializer for it
    //      val serializer = serialization.findSerializerFor(original)
    //      // Turn it into bytes
    //      val bytes = serializer.toBinary(original)
    //      // Turn it back into an object
    //      //val back = serializer.fromBinary(bytes, manifest = None)
    //
    //      localActor ! TransferFlowSeri(bytes)
  }
}
