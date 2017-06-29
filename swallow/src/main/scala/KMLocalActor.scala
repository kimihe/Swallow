/**
  * Created by zhouqihua on 2017/6/25.
  */

import swallow.core._
import com.typesafe.config._
import akka.actor._

object KMLocalActor {
  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val system = ActorSystem("localActor", config.getConfig("localActor").withFallback(config))
    val localActor = system.actorOf(Props[LocalActor], name = "localActor")
  }
}

object LocalActor {
  final case class TransferFlow(flow: KMFlow)
  final case class TransferFlowSeri(flow: Any)
  final case class CompleteFlow(flow: KMFlow)
}

class LocalActor extends Actor with ActorLogging {
  import MasterActor._
  import LocalActor._
  import RemoteActor._
  import KMClusterListener._

  override def preStart(): Unit = {

  }

  override def receive: Receive = {

    case TransferFlow(flow: KMFlow) =>
      log.info(s"[LocalActor] transferFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

      val remoteActor = context.actorSelection(s"${flow.flowInfo.to}")
      remoteActor ! ReceiveFlow(flow)

    case CompleteFlow(flow: KMFlow) =>
      log.info(s"[LocalActor] completeFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

      val masterActor = context.actorSelection(s"${flow.flowInfo.master}")
      masterActor ! AggregateFlow(flow)

      val clusterListener = context.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/clusterListener")
      clusterListener ! SuperviseFlow(flow)
  }
}


