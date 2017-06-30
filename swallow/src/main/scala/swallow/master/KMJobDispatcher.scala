package swallow.master

/**
  * Created by zhouqihua on 2017/6/29.
  */

import akka.actor._

import swallow.core.KMFlow
import swallow.core.KMActorMessages._

object KMJobDispatcher {
  def dispatchNewFlow(dispatcherActorSystem: ActorSystem, flow: KMFlow): Unit = {
    val masterActor = dispatcherActorSystem.actorSelection(s"${flow.flowInfo.master}")
    val senderActor = dispatcherActorSystem.actorSelection(s"${flow.flowInfo.from}")

    masterActor ! MasterDispatchNewFlow(flow)
    senderActor ! SenderTransmitFlow(flow)
  }
}

class KMJobDispatcher {

}
