package examples

/**
  * Created by zhouqihua on 2017/6/25.
  */

import swallow.core._
import com.typesafe.config._
import akka.actor._

object KMRemoteActor {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val system = ActorSystem("remoteActor", config.getConfig("remoteActor").withFallback(config))
    val remoteActor = system.actorOf(Props[RemoteActor], name = "remoteActor")
  }
}

object RemoteActor {
  final case class ReceiveFlow(flow: KMFlow)
}

class RemoteActor extends Actor with ActorLogging{
  import LocalActor._
  import RemoteActor._

  override def receive: Receive = {
    case ReceiveFlow(flow: KMFlow) =>
      log.info(s"[RemoteActor] receiveFlow; From sender: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

      val localActor = context.actorSelection(s"${flow.flowInfo.from}")
      val newFlowInfo = new KMFlowInfo(flow.flowInfo.flowId, flow.flowInfo.taskId, flow.flowInfo.master,
        flow.flowInfo.from, flow.flowInfo.to, "****** Flow Completed !!! ******", flow.flowInfo.description, KMDataType.FAKE)
      val newFlow = KMFlow.initWithFlowInfo(newFlowInfo)
      localActor ! CompleteFlow(newFlow)
  }
}


