package swallow.slave

/**
  * Created by zhouqihua on 2017/6/29.
  */

import swallow.core.{KMFlow, KMNode}
import swallow.core.KMActorMessages._

object KMSender {

}

class KMSender (slaveType: KMSlaveType.SlaveType) extends KMNode {
  override def receive: Receive = {

    case SenderTransmitFlow(flow: KMFlow) =>
      log.info(s"[LocalActor] transferFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

      val remoteActor = context.actorSelection(s"${flow.flowInfo.to}")
      remoteActor ! ReceiverGetFlow(flow)

    case SenderCompleteFlow(flow: KMFlow) =>
      log.info(s"[LocalActor] completeFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

      val masterActor = context.actorSelection(s"${flow.flowInfo.master}")
      masterActor ! MasterAggregateFlow(flow)

      val clusterListener = context.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/cLusterSupervisor")
      clusterListener ! ClusterSuperviseFlow(flow)
  }
}
