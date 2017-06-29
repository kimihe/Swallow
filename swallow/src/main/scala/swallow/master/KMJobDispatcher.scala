package swallow.master

/**
  * Created by zhouqihua on 2017/6/29.
  */

import swallow.core.KMFlow
import swallow.slave.KMSenderActor._

import akka.actor._

object KMJobDispatcher {
  def dispatchNewFlow(dispatcherActorSystem: ActorSystem, flow: KMFlow): Unit = {
    //    val masterActor = dispatcherActorSystem.actorSelection(s"${flow.flowInfo.master}")
    val senderActor = dispatcherActorSystem.actorSelection(s"${flow.flowInfo.from}")

    senderActor ! FLOW
  }
}

class KMJobDispatcher {

}
