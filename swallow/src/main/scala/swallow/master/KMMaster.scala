package swallow.master

/**
  * Created by zhouqihua on 2017/6/29.
  */

import swallow.core.{KMFlow, KMNode}
import swallow.core.KMActorMessages._

object KMMaster {

}

class KMMaster extends KMNode {
  override def receive: Receive = {

    case MasterSubmitNewFlow(flow: KMFlow) =>
      log.info(s"[MasterActor] submitNewFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

    case MasterDispatchNewFlow(flow: KMFlow) =>
      log.info(s"[MasterActor] dispatchNewFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")

    case MasterAggregateFlow(flow: KMFlow) =>
      log.info(s"[MasterActor] aggregateFlow; [From sender]: $sender")
      log.info(s"[Flow Info] from: ${flow.flowInfo.from}; to: ${flow.flowInfo.to}; content: ${flow.flowInfo.content}")
  }
}

