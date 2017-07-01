package swallow.core

/**
  * Created by zhouqihua on 2017/6/29.
  */

import akka.actor._
import com.typesafe.config.ConfigFactory

object KMNode {
  def props: Props = Props(new KMNode)
}

class KMNode extends Actor with ActorLogging {



  def description: String = {
    self.path.toString
  }

  def getActorSystem: ActorSystem = {
    context.system
  }

  override def receive: Receive = {
    case msg =>
      log.info(s"Received msg: $msg")
  }
}
