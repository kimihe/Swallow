package swallow.core

/**
  * Created by zhouqihua on 2017/6/29.
  */

import akka.actor._

class KMNode extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg =>
      log.info(s"$msg")
  }
}
