package swallow.slave

/**
  * Created by zhouqihua on 2017/6/29.
  */

import akka.actor.Props
import swallow.core.{KMDataType, KMFlow, KMFlowInfo, KMNode}
import swallow.core.KMActorMessages._

object KMReceiver {
  def props: Props = Props(new KMReceiver(KMSlaveType.Receiver))
}

class KMReceiver (slaveType: KMSlaveType.SlaveType) extends KMNode {
  override def receive: Receive = {
    case ReceiverGetFlow(flow: KMFlow) =>
      log.info(s"[RemoteActor] receiveFlow; From sender: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

      val localActor = context.actorSelection(s"${flow.flowInfo.from}")
      val newFlowInfo = new KMFlowInfo(flow.flowInfo.flowId, flow.flowInfo.taskId, flow.flowInfo.master,
        flow.flowInfo.from, flow.flowInfo.to, "****** Flow Completed !!! ******", flow.flowInfo.description, KMDataType.FAKE)
      val newFlow = KMFlow.initWithFlowInfo(newFlowInfo)
      localActor ! SenderCompleteFlow(newFlow)
  }
}
